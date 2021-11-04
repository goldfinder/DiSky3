package info.itsthesky.disky3.api.migration;

import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.migration.types.MigrationFile;
import info.itsthesky.disky3.api.migration.types.MigrationStep;
import info.itsthesky.disky3.api.migration.types.MigrationVersion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * SkriptMigrate was entirely made by Olyno, and every copy right belong to him.
 * <br> Only made small edition to make it compatible with DiSky.
 * @author Olyno
 */
public class Migration {

    private static File migrationsDir;
    private static ArrayList<MigrationFile> migrationFiles = new ArrayList<>();

    public static void load() {
        Migration.migrationsDir = migrationsDir;
        File migrationDir = new File(DiSky.getInstance().getDataFolder(), "migrations");
        if (!migrationDir.exists())
            migrationDir.mkdirs();
    }
    
    public static void migrate() {
        ConsoleCommandSender sender = Bukkit.getConsoleSender();
        for (MigrationFile migrationFile : migrationFiles) {
            String addonVersion = Bukkit.getServer().getPluginManager().getPlugin(migrationFile.getAddonName()).getDescription().getVersion();
            // System.out.println("Addon version: " + addonVersion);
            if (!migrationFile.hasVersion(addonVersion)) {
                sender.sendMessage(ChatColor.YELLOW + "[Migration] Migration for " + migrationFile.getAddonName() + " " + addonVersion + " does not exist, skipped...");
                continue;
            }
            List<MigrationStep> migrationSteps = migrationFile.getStepsFromVersion(addonVersion);
            if (migrationSteps.size() == 0) {
                sender.sendMessage(ChatColor.YELLOW + "[Migration] Migration for " + migrationFile.getAddonName() + " " + addonVersion + " has empty steps, skipped...");
                continue;
            }
            try (Stream<Path> walk = Files.walk(Paths.get("plugins/Skript/scripts"))) {
                walk.filter(Files::isRegularFile)
                        .filter(scriptPath -> scriptPath.toString().endsWith(".sk"))
                        .forEach(scriptPath -> {
                            try {
                                List<String> scriptLines = Files.readAllLines(scriptPath);
                                List<String> newScriptLines = scriptLines
                                        .stream()
                                        .map(line -> {
                                            for (MigrationStep migrationStep : migrationSteps) {
                                                line = line.replaceAll(migrationStep.getFind(), migrationStep.getResult());
                                            }
                                            return line;
                                        })
                                        .collect(Collectors.toList());
                                Files.write(scriptPath, newScriptLines, StandardOpenOption.TRUNCATE_EXISTING);
                            } catch (IOException ex) {
                                sender.sendMessage(ChatColor.RED + "[Migration] Something wrong happened for the migration of " + migrationFile.getAddonName()
                                        + (sender instanceof Player ? " (Look in your server console)" : ":\n"));
                                ex.printStackTrace();
                            }
                        });
                sender.sendMessage(ChatColor.GREEN + "[Migration] Migration for " + migrationFile.getAddonName() + " has been a success!");
            } catch (IOException ex) {
                sender.sendMessage(ChatColor.RED + "[Migration] Something wrong happened for the migration of " + migrationFile.getAddonName()
                        + (sender instanceof Player ? " (Look in your server console)" : ":\n"));
                ex.printStackTrace();
            }
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sk reload all");
    }

    @SuppressWarnings("unchecked")
    public static void reload() {
        migrationFiles.clear();
        for (File migrationFile : migrationsDir.listFiles()) {
            try {
                String fileContent = String.join("\n", Files.readAllLines(migrationFile.toPath()));
                Yaml yaml = new Yaml();
                LinkedHashMap<String, Object> loadedMigration = (LinkedHashMap<String, Object>) yaml.load(fileContent);
                MigrationFile migration = new MigrationFile();
                migration.setAddonName((String) loadedMigration.get("addon_name"));
                migration.setAuthor((String) loadedMigration.get("author"));
                LinkedHashMap<String, Object> migrationSteps = (LinkedHashMap<String, Object>) loadedMigration.get("steps");
                for (Object stepVersionObject : migrationSteps.keySet()) {
                    String stepVersion = stepVersionObject.toString();
                    MigrationVersion migrationVersion = new MigrationVersion(stepVersion);
                    for (LinkedHashMap<String, Object> step : (List<LinkedHashMap<String, Object>>) migrationSteps.get(stepVersionObject)) {
                        MigrationStep migrationStep = new MigrationStep();
                        migrationStep.setFind((String) step.get("find"));
                        migrationStep.setResult((String) step.get("result"));
                        migrationVersion.addStep(migrationStep);
                    }
                    migration.addVersion(migrationVersion);
                }
                migrationFiles.add(migration);
            } catch (YAMLException | IOException ex) {
                System.out.println(ChatColor.RED + "[Migration] Something bad happened when loading this migration file: " + migrationFile.toPath().toString());
                ex.printStackTrace();
            }
        }
    }

    /**
     * Load the migration file of a plugin
     * @param plugin The plugin with a migration file.
     */
    public static void load(JavaPlugin plugin) {
        plugin.saveResource("migrations.yml", true);
        String pluginName = plugin.getName();
        Path migrationsFile = Paths.get("plugins/" + pluginName + "/migrations.yml");
        Path migrationsFileFinal = Paths.get("plugins/DiSky/migrations/" + pluginName.toLowerCase() + ".yml");
        try {
            Files.copy(migrationsFile, migrationsFileFinal);
        } catch (IOException ex) {
            if (ex instanceof FileAlreadyExistsException) {
                return;
            }
            ex.printStackTrace();
        }
    }

}

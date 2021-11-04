package info.itsthesky.disky3.api.migration.types;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * SkriptMigrate was entirely made by Olyno, and every copy right belong to him.
 * <br> Only made small edition to make it compatible with DiSky.
 * @author Olyno
 */
public class MigrationFile {
    
    private String author;
    private String addon_name;
    private List<MigrationVersion> versions = new ArrayList<MigrationVersion>();

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAddonName() {
        return addon_name;
    }

    public void setAddonName(String addon_name) {
        this.addon_name = addon_name;
    }

    public List<MigrationVersion> getVersions() {
        return versions;
    }

    public void addVersion(MigrationVersion version) {
        versions.add(version);
    }

    public void removeVersion(MigrationVersion version) {
        versions.remove(version);
    }

    public boolean hasVersion(String version) {
        return versions.stream().anyMatch(migrationVersion -> migrationVersion.getVersion().equalsIgnoreCase(version));
    }

    public List<MigrationStep> getStepsFromVersion(String version) {
        AtomicInteger i = new AtomicInteger();
        int index = versions.stream()
            .peek(v -> i.incrementAndGet())
            .anyMatch(migrationVersion -> migrationVersion.getVersion().equals(version)) ?
            i.get() - 1 : -1;
        if (index == -1) {
            return new ArrayList<>();
        }
        return versions.subList(index, versions.size())
            .stream()
            .flatMap(migrationVersion -> migrationVersion.getSteps().stream())
            .collect(Collectors.toList());
    }

}

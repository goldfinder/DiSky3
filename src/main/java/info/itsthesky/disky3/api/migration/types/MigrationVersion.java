package info.itsthesky.disky3.api.migration.types;

import java.util.ArrayList;
import java.util.List;

/**
 * SkriptMigrate was entirely made by Olyno, and every copy right belong to him.
 * <br> Only made small edition to make it compatible with DiSky.
 * @author Olyno
 */
public class MigrationVersion {
    
    private String version;
    private List<MigrationStep> steps = new ArrayList<MigrationStep>();

    public MigrationVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<MigrationStep> getSteps() {
        return steps;
    }

    public void addStep(MigrationStep step) {
        steps.add(step);
    }

    public void removeStep(MigrationStep step) {
        steps.remove(step);
    }

}

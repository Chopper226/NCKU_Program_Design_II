public class Technician {
    public String technicianId;
    public String specialization;
    public boolean repairDevice(ElectronicDevice device) {return false;}
    public boolean upgradeDevice(Smartphone smartphone) {return false;}
    private String name;
    private int yearsOfExperience;
    public String getName() {
        return name;
    }
    public int getYearsOfExperience() {
        return yearsOfExperience;
    }
    public String conductInspection(ElectronicDevice device) {return "";}
}

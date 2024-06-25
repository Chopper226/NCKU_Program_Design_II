public class Company {
    private String name;
    private String address;
    public void setName(String name) {
        this.name = name;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public int establishedYear;
    public boolean registerEmployee(Employee employee) {return false;}
    public boolean terminateEmployee(String employeeId) {return false;}
}

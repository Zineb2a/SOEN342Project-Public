import java.util.ArrayList;
import java.util.List;

public class Organization {

    private String name;
    private List<Offering> offerings;
    private Administrator administrator;

    public Organization(String name, Administrator administrator) {
        this.name = name;
        this.administrator = administrator;
        this.offerings = new ArrayList<>();
    }

    public void addOffering(Offering offering) {
        offerings.add(offering);
    }

    public void removeOffering(Offering offering) {
        offerings.remove(offering);
    }

    public List<Offering> getOfferings() {
        return offerings;
    }

    public String getName() {
        return name;
    }

    public Administrator getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Administrator administrator) {
        this.administrator = administrator;
    }
}
//In process

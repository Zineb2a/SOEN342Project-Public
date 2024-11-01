import java.util.ArrayList;
import java.util.List;

public class Location {

    private String address;
    private List<Space> spaces;

    public Location(String address) {
        this.address = address;
        this.spaces = new ArrayList<>();
    }

    public void addSpace(Space space) {
        spaces.add(space);
    }

    public void removeSpace(Space space) {
        spaces.remove(space);
    }

    public List<Space> getSpaces() {
        return spaces;
    }

    public String getAddress() {
        return address;
    }
}
//In process
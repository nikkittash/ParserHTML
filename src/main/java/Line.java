import java.util.ArrayList;

public class Line {

    private String name;
    private String number;
    private ArrayList<Station> stations;

    public Line(String name, String number) {
        this.name = name;
        this.number = number;
        stations = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public ArrayList<Station> getStations() {
        return stations;
    }

    public void setStations(ArrayList<Station> stations) {
        this.stations = stations;
    }
}

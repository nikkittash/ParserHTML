import java.util.ArrayList;

public class MetroMap {

    private ArrayList<Connection> connections;
    private ArrayList<Line> lines;

    public MetroMap() {
        lines = new ArrayList<>();
        connections = new ArrayList<>();
    }

    public ArrayList<Connection> getConnections() {
        return connections;
    }

    public void setConnections(ArrayList<Connection> connections) {
        this.connections = connections;
    }

    public ArrayList<Line> getLines() {
        return lines;
    }

    public void setLines(ArrayList<Line> lines) {
        this.lines = lines;
    }
}

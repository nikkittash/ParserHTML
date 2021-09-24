import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ParserJson {

    public ParserJson() {
    }

    protected static void readJson(String dataFile) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonData = (JSONObject) parser.parse(getJsonFile(dataFile));

        MetroMap metroMap = new MetroMap();

        JSONArray linesArray = (JSONArray) jsonData.get("lines");
        JSONObject stationsObject = (JSONObject) jsonData.get("stations");
        parseLines(linesArray, stationsObject, metroMap);

        JSONArray connectionsArray = (JSONArray) jsonData.get("connections");
        parseConnections(connectionsArray, metroMap);

        getInfoMetro(metroMap);
    }

    private static void parseConnections(JSONArray connectionsArray, MetroMap metroMap) {
        ArrayList<Connection> connections = new ArrayList<>();
        connectionsArray.forEach(connection -> {
            Connection connection1 = new Connection(connection.toString());
            connections.add(connection1);
        });
        metroMap.setConnections(connections);
    }


    private static void parseLines(JSONArray linesArray, JSONObject stationsObject, MetroMap metroMap) {
        ArrayList<Line> lines = new ArrayList<>();
        linesArray.forEach(lineObject -> {
            JSONObject lineJsonObject = (JSONObject) lineObject;
            String lineName = (String) lineJsonObject.get("name");
            String lineNumber = (String) lineJsonObject.get("number");
            Line line = new Line(lineName, lineNumber);
            JSONArray stationsArray = (JSONArray) stationsObject.get(lineNumber);
            stationsArray.forEach(station -> {
                Station station1 = new Station(station.toString());
                line.getStations().add(station1);
            });
            lines.add(line);
        });
        metroMap.setLines(lines);
    }

    private static void getInfoMetro(MetroMap metroMap) {
        System.out.println("Количество переходов в метро: " + metroMap.getConnections().size());
        System.out.println("Количество станций на линиях метро: ");
        metroMap.getLines().forEach(line ->
                System.out.println("\t" + line.getName() + " - " + line.getStations().size()));
    }

    private static String getJsonFile(String dataFile) {
        StringBuilder builder = new StringBuilder();
        try {
            List<String> lines = Files.readAllLines(Paths.get(dataFile));
            lines.forEach(builder::append);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return builder.toString();
    }
}

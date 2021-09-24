import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParserHTML {

    private static JSONObject jsonMetro;
    private static JSONObject jsonLine;
    private static JSONArray linesArray;
    private static JSONObject jsonStations;
    private static JSONArray allConnections;
    private static JSONArray oneConnection;
    private static JSONObject firstConnection;
    private static JSONObject secondConnection;
    private static JSONArray stationsArray;

    public ParserHTML() {
    }

    protected static void getMap(String fileJson, String url) throws IOException {
        Document doc = Jsoup.connect(url).maxBodySize(0).get();
// создаем линии
        HashMap<String, String> lineMap = new HashMap<>();
        Elements lineNames = doc.select("span.js-metro-line");
        lineNames.forEach(lineName -> {
            String nameLine = lineName.text();
            String numberLine = lineName.attr("data-line");
            lineMap.put(numberLine, nameLine);
        });
// создаем станции
        HashMap<String, ArrayList> linesWithStations = new HashMap<>();
        Elements stationNames = doc.select("div.js-metro-stations");
        stationNames.forEach(names -> {
            String numberLine = names.attr("data-line");
            Elements stations = names.select("a");
            ArrayList<String> stationArray = new ArrayList<>();
            stations.forEach(station -> {
                Element name = station.selectFirst("span.name");
                String nameStation = name.text().replaceAll("[^A-Za-zА-Яа-я]", "");
                stationArray.add(nameStation);
            });
            linesWithStations.put(numberLine, stationArray);
        });
//создаем пересадки
        ArrayList<ArrayList> connectionList = new ArrayList<>();
        Elements linesWithConnections = doc.select("div.js-metro-stations");
        linesWithConnections.forEach(lineWithConnection -> {
            ArrayList<String> connection = new ArrayList<>();
            String numberFirst = lineWithConnection.attr("data-line");
            Elements stationsOnLine = lineWithConnection.select("a");
            stationsOnLine.forEach(stationOnLine -> {
                String nameFirst = stationOnLine.selectFirst("span.name").text();
                Elements spans = stationOnLine.select("span");
                spans.forEach(span ->{
                    if(span.hasClass("t-icon-metroln")) {
                        String numberSecond = span.attr("class");
                        numberSecond = span.attr("class").substring(numberSecond.length() - 2).
                                replaceAll("[^\\dA-Za-z]", "");
                        String title = span.attr("title");
                        String nameSecond = title.substring(title.indexOf("«"), title.indexOf("»"));
                        connection.add(numberFirst);
                        connection.add(nameFirst);
                        connection.add(numberSecond);
                        connection.add(nameSecond);
                        connectionList.add(connection);
                    }
                });
            });
        });

        jsonWriter(lineMap, linesWithStations, connectionList, fileJson);
    }

    protected static void jsonWriter(HashMap<String, String> lineMap,
                                     HashMap<String, ArrayList> linesWithStations,
                                     ArrayList<ArrayList> connectionList, String fileJson) {
        linesArray = new JSONArray();
        for(String key : lineMap.keySet()) {
            jsonLine = new JSONObject();
            jsonLine.put("number", key);
            jsonLine.put("name", lineMap.get(key));
            linesArray.add(jsonLine);
        }

        jsonStations = new JSONObject();
        for(String key : linesWithStations.keySet()) {
            stationsArray = new JSONArray();
            linesWithStations.get(key).forEach(station -> stationsArray.add(station));
            jsonStations.put(key, stationsArray);
        }

        allConnections = new JSONArray();
        connectionList.forEach(connection -> {
            firstConnection = new JSONObject();
            firstConnection.put("line", connection.get(0));
            firstConnection.put("station", connection.get(1));
            secondConnection = new JSONObject();
            secondConnection.put("line", connection.get(2));
            secondConnection.put("station", connection.get(3));
            oneConnection = new JSONArray();
            oneConnection.add(firstConnection);
            oneConnection.add(secondConnection);
            allConnections.add(oneConnection);
        });

        jsonMetro = new JSONObject();
        jsonMetro.put("stations", jsonStations);
        jsonMetro.put("connections", allConnections);
        jsonMetro.put("lines", linesArray);

        try (FileWriter file = new FileWriter(fileJson)) {

            file.write(jsonMetro.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

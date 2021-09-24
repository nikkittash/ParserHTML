import org.json.simple.parser.ParseException;

import java.io.IOException;

public class Main {

    private static final String URL_WEB = "https://www.moscowmap.ru/metro.html#lines";
    private static String fileJson = "data/newMap.json";

    public static void main(String[] args) throws IOException, ParseException {

        ParserHTML.getMap(fileJson, URL_WEB);
        ParserJson.readJson(fileJson);
    }
}

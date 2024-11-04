package pacman.model.engine;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;

public class GameConfigurationReader {

    private JSONObject gameConfig;

    public GameConfigurationReader(String configPath) {
        JSONParser parser = new JSONParser();

        try {
            // Get resource as stream
            InputStream inputStream = Objects.requireNonNull(
                    getClass().getResourceAsStream(configPath),
                    "Config file not found: " + configPath
            );

            // Parse the config
            try (Reader reader = new InputStreamReader(inputStream)) {
                this.gameConfig = (JSONObject) parser.parse(reader);
            }
        } catch (Exception e) {
            System.out.println("Error reading/parsing config file: " + e.getMessage());
            System.exit(0);
        }
    }

    public String getMapFile() {
        String mapPath = (String) gameConfig.get("map");
        if (!mapPath.startsWith("/")) {
            mapPath = "/" + mapPath;
        }
        return mapPath;
    }

    public int getNumLives() {
        return ((Number) gameConfig.get("numLives")).intValue();
    }

    public JSONArray getLevelConfigs() {
        return (JSONArray) gameConfig.get("levels");
    }
}
package smartmeters;

import java.io.FileReader;
import java.util.*;

import org.aeonbits.owner.ConfigFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


/**
 * Created by nikolay on 19.02.15.
 */

public class BuildingStore {

    private SmartMetersConfig simulationConfig  = ConfigFactory.create(SmartMetersConfig.class);

    private int currentIndex         = 0;
    private List<Building> buildings = new ArrayList<Building>();
    protected Random randomGenerator = new Random();

    public int getNextBuildingId() {
        return currentIndex++;
    }

    public Building getBuilding(int index) {
        return buildings.get(index);
    }

    private Building normaliseBuilding(JSONObject data, int index) {
        int quarters = randomGenerator.nextInt((simulationConfig.quarters_max() - simulationConfig.quarters_min()) + 1) + simulationConfig.quarters_min();
        String address = data.get("addr:street").toString() + ", " + data.get("addr:housenumber").toString();
        return new Building(index, address, quarters);
    }

    private static BuildingStore instance = new BuildingStore();
    public BuildingStore() {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(getClass().getResource("buildings.geojson").getPath()));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray features = (JSONArray) jsonObject.get("features");

            for (int i = 0; i < simulationConfig.meters_count(); i++) {
                JSONObject feature = (JSONObject) features.get(randomGenerator.nextInt(features.size()));
                JSONObject featureProps = (JSONObject) feature.get("properties");
                while (featureProps.get("addr:street") == null || featureProps.get("addr:housenumber") == null ) {
                    feature = (JSONObject) features.get(randomGenerator.nextInt(features.size()));
                    featureProps = (JSONObject) feature.get("properties");
                }
                buildings.add(normaliseBuilding(featureProps, i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BuildingStore getInstance() {
        return instance;
    }

}

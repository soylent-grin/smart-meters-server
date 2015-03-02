package smartmeters;

import madkit.kernel.Agent;
import org.aeonbits.owner.ConfigFactory;
import java.util.Random;

/**
 * Created by nikolay on 19.02.15.
 */

public class HeatAgent extends Agent {

    private BuildingStore buildings = BuildingStore.getInstance();
    private int building_id         = buildings.getNextBuildingId();

    protected int quarters                       = buildings.getBuilding(building_id).getQuarters();
    protected TestimonialStore store             = TestimonialStore.getInstance();
    protected Random randomGenerator             = new Random();
    protected SmartMetersConfig simulationConfig = ConfigFactory.create(SmartMetersConfig.class);

    protected void setHeat() {
        store.setData(calculateHeat(), Integer.toString(building_id));
    }

    protected String calculateHeat() {
        return "undefined";
    }

    @Override
    protected void activate() {
        pause(randomGenerator.nextInt((simulationConfig.time_to_start()) + 1));
    }

    @Override
    protected void live() {
        while (true) {
            setHeat();
            pause(simulationConfig.meters_heartbeat());
        }
    }

    @Override
    protected void end() {
        if (logger != null) {
            logger.info(this.getName() + " stopping..");
        }
    }
}
package smartmeters;

import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;

import smartmeters.TestimonialStore;

import java.util.Random;


/**
 * Created by nikolay on 19.02.15.
 */

public class HeatAgent extends Agent {

    private TestimonialStore store = TestimonialStore.getInstance();

    @Override
    protected void activate() {
        if (logger != null) {
            logger.info(this.getName() + " starting..");
        }
    }

    @Override
    protected void live() {
        Random randomGenerator = new Random();
        String generated;
        while (true) {
            generated = Integer.toString(randomGenerator.nextInt(100));
            if (logger != null) {
                logger.info("Generated new testimony: " + generated);
            }
            store.setData(generated);
            pause(2000);
        }
    }

    @Override
    protected void end() {
        if (logger != null) {
            logger.info(this.getName() + " stopping..");
        }
    }
}
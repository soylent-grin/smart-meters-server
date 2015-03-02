package smartmeters;

/**
 * Created by nikolay on 02.03.15.
 */

public class HeatMeterSPT943_4 extends HeatAgent {

    @Override
    protected String calculateHeat() {
        // TODO: introduce normal calculation
        Double anchor = (quarters / 30.2) - (simulationConfig.outside_temperature() / 10.7);
        return String.format( "%.2f", randomGenerator.nextInt(10) * 0.1 * anchor );
    }
}

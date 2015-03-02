package smartmeters;

import org.aeonbits.owner.Config;
/**
 * Created by nikolay on 02.03.15.
 */
public interface SmartMetersConfig extends Config {
    int meters_count();
    int meters_heartbeat();
    int quarters_max();
    int quarters_min();
    int outside_temperature();
    int coap_ttl();
    int time_to_start();
}

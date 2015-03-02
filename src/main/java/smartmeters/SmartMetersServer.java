package smartmeters;

import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.aeonbits.owner.ConfigFactory;
import org.eclipse.californium.core.CaliforniumLogger;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.network.Endpoint;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.network.interceptors.MessageTracer;

import madkit.kernel.Madkit;

/**
 * Created by nikolay on 19.02.15.
 */

public class SmartMetersServer extends CoapServer implements IListener {
    static {
        CaliforniumLogger.initialize();
        CaliforniumLogger.setLevel(Level.FINER);
    }

    // exit codes for runtime errors
    public static final int ERR_INIT_FAILED = 1;
    private static final int port = NetworkConfig.getStandard().getInt(NetworkConfig.Keys.COAP_PORT);

    private SmartMetersConfig simulationConfig  = ConfigFactory.create(SmartMetersConfig.class);
    private TestimonialStore store              = TestimonialStore.getInstance();

    private static Map<String, SubscribeSingle> handlers = new HashMap<String, SubscribeSingle>();
    private SubscribeHub hub = new SubscribeHub();

    public static void main(String[] args) {
        try {
            CoapServer server = new SmartMetersServer();
            server.start();
            System.out.println(SmartMetersServer.class.getSimpleName() + " listening on port " + port);
        } catch (Exception e) {
            System.err.println("Exiting");
            System.exit(ERR_INIT_FAILED);
        }
    }

    @Override
    public void onCreated(String _id) {
        System.out.println("New meter registered and available on /subscribe/" + _id);
        registerHandler(_id);
    }

    @Override
    public void onUpdated(String id, String data) {
        System.out.println("Meter with id " + id + " updated; heat = " + data);
        if (handlers.containsKey(id)) {
            handlers.get(id).changed();
        }
    }

    private void registerHandler(String id) {
        SubscribeSingle s = new SubscribeSingle(id);
        handlers.put(id, s);
        hub.add(s);
    }

    private void launchAgents() {
        new Madkit(
                "--launchAgents",
                HeatMeterSPT943_4.class.getName() + ",false," + Integer.toString(simulationConfig.meters_count())
        );
    }

    public SmartMetersServer() throws SocketException {
        NetworkConfig.getStandard()
                .setInt(NetworkConfig.Keys.MAX_MESSAGE_SIZE, 64)
                .setInt(NetworkConfig.Keys.PREFERRED_BLOCK_SIZE, 64)
                .setInt(NetworkConfig.Keys.NOTIFICATION_CHECK_INTERVAL_COUNT, 4)
                .setInt(NetworkConfig.Keys.NOTIFICATION_CHECK_INTERVAL_TIME, 30000)
                .setString(NetworkConfig.Keys.HEALTH_STATUS_PRINT_LEVEL, "INFO");

        // add observe handler
        add(hub);

        store.addListener(this);

        launchAgents();

    }

}

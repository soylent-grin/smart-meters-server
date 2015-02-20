package smartmeters;

import java.net.SocketException;
import java.util.logging.Level;

import org.eclipse.californium.core.CaliforniumLogger;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.network.Endpoint;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.network.interceptors.MessageTracer;

import madkit.kernel.Madkit;

/**
 * Created by nikolay on 19.02.15.
 */

public class SmartMetersServer extends CoapServer {
    static {
        CaliforniumLogger.initialize();
        CaliforniumLogger.setLevel(Level.FINER);
    }

    // exit codes for runtime errors
    public static final int ERR_INIT_FAILED = 1;

    // allows port configuration in Californium.properties
    private static final int port = NetworkConfig.getStandard().getInt(NetworkConfig.Keys.COAP_PORT);

    public static void main(String[] args) {
        try {
            CoapServer server = new SmartMetersServer();
            server.start();

            for (Endpoint ep:server.getEndpoints()) {
                ep.addInterceptor(new MessageTracer());
            }

            System.out.println(SmartMetersServer.class.getSimpleName() + " listening on port " + port);
        } catch (Exception e) {
            System.err.println("Exiting");
            System.exit(ERR_INIT_FAILED);
        }
    }

    private void registerHandlers() {
        add(new Subscribe());
    }

    public SmartMetersServer() throws SocketException {
        NetworkConfig.getStandard()
                .setInt(NetworkConfig.Keys.MAX_MESSAGE_SIZE, 64)
                .setInt(NetworkConfig.Keys.PREFERRED_BLOCK_SIZE, 64)
                .setInt(NetworkConfig.Keys.NOTIFICATION_CHECK_INTERVAL_COUNT, 4)
                .setInt(NetworkConfig.Keys.NOTIFICATION_CHECK_INTERVAL_TIME, 30000)
                .setString(NetworkConfig.Keys.HEALTH_STATUS_PRINT_LEVEL, "INFO");

        // add observe handler
        registerHandlers();

        // launch one agent in console mode
        new Madkit(
            "--launchAgents",
            HeatAgent.class.getName() + ",false"
        );

    }

}

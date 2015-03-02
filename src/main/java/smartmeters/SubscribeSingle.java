package smartmeters;

/**
 * Created by nikolay on 02.03.15.
 */

import org.aeonbits.owner.ConfigFactory;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.server.resources.CoapExchange;

import static org.eclipse.californium.core.coap.CoAP.ResponseCode.*;
import static org.eclipse.californium.core.coap.CoAP.ResponseCode.DELETED;
import static org.eclipse.californium.core.coap.MediaTypeRegistry.TEXT_PLAIN;

/**
 * Created by nikolay on 19.02.15.
 */

public class SubscribeSingle extends CoapResource {

    private SmartMetersConfig simulationConfig  = ConfigFactory.create(SmartMetersConfig.class);
    private TestimonialStore store              = TestimonialStore.getInstance();

    private String id;

    public SubscribeSingle(String _id) {
        super(_id);

        id = _id;

        setObservable(true);
        getAttributes().setTitle("Observer of the meter with id " + id);
        getAttributes().addResourceType("observe");
        getAttributes().setObservable();
        setObserveType(CoAP.Type.CON);
    }

    @Override
    public void handleGET(CoapExchange exchange) {
        exchange.setMaxAge(simulationConfig.coap_ttl());
        exchange.respond(CONTENT, store.getData(id), TEXT_PLAIN);
    }

    @Override
    public void handleDELETE(CoapExchange exchange) {
        clearAndNotifyObserveRelations(NOT_FOUND);
        exchange.respond(DELETED);
        //timer.cancel();
    }

}

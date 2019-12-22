package com.iotproject.nahin.smart_home_system.Sensors;

import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.server.resources.ConcurrentCoapResource;

import java.util.Timer;
import java.util.TimerTask;

public class DistanceSensor extends ConcurrentCoapResource {

    public static int counter = 0;

    public DistanceSensor(String name)
    {
        super(name);

        setObservable(true);
        setObserveType(CoAP.Type.CON);
        getAttributes().setObservable();
        Timer timer = new Timer();
        timer.schedule(new ContinuousTask(), 0, 5000);

    }

    private class ContinuousTask extends TimerTask{
        @Override
        public void run(){
            counter++;
            changed();
        }
    }

    @Override
    public void handleGET(CoapExchange exchange) {
        exchange.respond("data from coap "+ counter);
    }

}

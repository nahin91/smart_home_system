package com.iotproject.nahin.smart_home_system;

import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.server.resources.ConcurrentCoapResource;

import java.util.Timer;

public class DistanceSensor extends ConcurrentCoapResource {
    public DistanceSensor(String name) {
        super(name);
    }

    class Timer {

    }

    @Override
    public void handleGET(CoapExchange exchange) {
        exchange.respond("Hello distance sensor");
    }
}

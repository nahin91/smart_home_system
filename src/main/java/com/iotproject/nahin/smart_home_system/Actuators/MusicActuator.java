package com.iotproject.nahin.smart_home_system.Actuators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iotproject.nahin.smart_home_system.Model.MusicRequest;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.server.resources.ConcurrentCoapResource;
import org.json.JSONObject;

public class MusicActuator extends ConcurrentCoapResource {

    MusicRequest music = null;

    public MusicActuator(String name) {
        super(name);
        music = new MusicRequest();
    }

    /*converts the object data into json format*/
    @Override
    public void handlePOST(CoapExchange exchange) {
        exchange.accept();

        try {
            String jsonRequest = exchange.getRequestText();
            ObjectMapper objectMapper = new ObjectMapper();

            MusicRequest request = objectMapper.readValue(jsonRequest, MusicRequest.class);
            music.currentSong = request.currentSong;

            String jsonString = objectMapper.writeValueAsString(music);

            JSONObject json = new JSONObject(jsonString);
            exchange.respond(CoAP.ResponseCode.CREATED, json.toString(), MediaTypeRegistry.APPLICATION_JSON);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

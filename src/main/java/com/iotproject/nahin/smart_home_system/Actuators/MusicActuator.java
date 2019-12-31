package com.iotproject.nahin.smart_home_system.Actuators;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iotproject.nahin.smart_home_system.Model.DistanceObject;
import com.iotproject.nahin.smart_home_system.Model.ResponseDistanceData;
import com.iotproject.nahin.smart_home_system.Sensors.DistanceSensor;
import com.iotproject.nahin.smart_home_system.SmartHomeSystemApplication;
import jdk.internal.org.objectweb.asm.Handle;
import org.eclipse.californium.core.*;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.server.resources.ConcurrentCoapResource;
import org.eclipse.californium.core.server.resources.Resource;

import java.util.List;
import java.util.Map;

public class MusicActuator{

    private static ResponseDistanceData response;
    public static ObjectMapper objectMapper;
    public static String[] songList;

    public static void main(String[] args){

        objectMapper = new ObjectMapper();

        /*coap server configuration.*/
        //CoapServer coapServer = new CoapServer(8086);
        //coapServer.add((new SmartHomeSystemApplication()));
        //coapServer.start();

        /*client config with the corresponding server*/
        CoapClient coapClient = new CoapClient("coap://localhost:8086/distance");

        CoapObserveRelation relation = coapClient.observe(new CoapHandler() {
            @Override
            public void onLoad(CoapResponse coapResponse) {
                String jsonFromIoT = coapResponse.getResponseText();
                System.out.println(jsonFromIoT);
                /*try {
                    response = objectMapper.readValue(jsonFromIoT, ResponseDistanceData.class);
                    for (int i=0; i < response.getSongList().length; i++)
                        System.out.println("this is music actuator: "+ response.getSongList()[i]);

                } catch (JsonProcessingException e) {}*/
            }

            @Override
            public void onError() {

            }
        });

    }

}

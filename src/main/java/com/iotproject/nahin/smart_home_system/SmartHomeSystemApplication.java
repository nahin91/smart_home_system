package com.iotproject.nahin.smart_home_system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iotproject.nahin.smart_home_system.Sensors.DistanceSensor;
import org.eclipse.californium.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmartHomeSystemApplication {

    @Autowired
    public static ResponseDistanceData responseData;

    public static void main(String[] args) {
        SpringApplication.run(SmartHomeSystemApplication.class, args);

        //initialized once but inner properties gets changed.
        responseData = new ResponseDistanceData();

        //coap server configuration
        CoapServer coapServer = new CoapServer(8085);
        coapServer.add(new DistanceSensor("distance"));
        coapServer.start();

        //client config with the corresponding server
        CoapClient coapClient = new CoapClient("coap://localhost:8085/distance");

        CoapObserveRelation relation = coapClient.observe(new CoapHandler() {
            @Override
            public void onLoad(CoapResponse coapResponse) {
                String jsonString = coapResponse.getResponseText();
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    DistanceObject distanceObject = objectMapper.readValue(jsonString, DistanceObject.class);

                    responseData.setPerson(distanceObject.getPerson());
                    responseData.setDistance(distanceObject.getDistance());
                    responseData.setSongList(distanceObject.getSongList());

                    System.out.println("data from coap = " + jsonString);
                }
                catch (Exception e){}

            }

            @Override
            public void onError() {

            }
        });

    }

}

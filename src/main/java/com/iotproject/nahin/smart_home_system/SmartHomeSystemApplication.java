package com.iotproject.nahin.smart_home_system;

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

        responseData = new ResponseDistanceData();

        CoapServer coapServer = new CoapServer(8085);
        coapServer.add(new DistanceSensor("distance"));
        coapServer.start();

        CoapClient coapClient = new CoapClient("coap://localhost:8085/distance");

        CoapObserveRelation relation = coapClient.observe(new CoapHandler() {
            @Override
            public void onLoad(CoapResponse coapResponse) {
                responseData.setDistance(coapResponse.getResponseText());
                System.out.println("data from coap = " + responseData);
            }

            @Override
            public void onError() {

            }
        });

    }

}

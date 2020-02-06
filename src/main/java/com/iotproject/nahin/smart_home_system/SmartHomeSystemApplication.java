package com.iotproject.nahin.smart_home_system;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iotproject.nahin.smart_home_system.Actuators.MusicActuator;
import com.iotproject.nahin.smart_home_system.Model.DistanceObject;
import com.iotproject.nahin.smart_home_system.Model.MusicRequest;
import com.iotproject.nahin.smart_home_system.Model.ResponseDistanceData;
import com.iotproject.nahin.smart_home_system.Sensors.DistanceSensor;
import org.eclipse.californium.core.*;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.elements.exception.ConnectorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
public class SmartHomeSystemApplication {

    @Autowired
    public static ResponseDistanceData responseData;
    public static Map<String, String[]> sensorData;
    public static Map<String, Integer> iotPlayList;
    public static ObjectMapper objectMapper;

    public static void main(String[] args) {
        SpringApplication.run(SmartHomeSystemApplication.class, args);

        /*initialized once but inner properties gets changed on time being.*/
        responseData = new ResponseDistanceData();
        sensorData = new HashMap<>();
        iotPlayList = new HashMap<>();
        objectMapper = new ObjectMapper();

        /*coap server configuration.*/
        CoapServer distanceSensorServer = new CoapServer(5683);
        distanceSensorServer.add(new DistanceSensor("distance"));
        distanceSensorServer.start();

        CoapServer musicActuatorServer = new CoapServer(5684);
        musicActuatorServer.add(new MusicActuator("music"));
        musicActuatorServer.start();

        /*client config with the corresponding server*/
        CoapClient distantSensorClient = new CoapClient("coap://localhost:5683/distance");
        CoapClient musicActuatorClient = new CoapClient("coap://localhost:5684/music");

        CoapObserveRelation relation = distantSensorClient.observe(new CoapHandler() {
            @Override
            public void onLoad(CoapResponse coapResponse) {

                String jsonFromDistanceSensor = coapResponse.getResponseText();
                System.out.println(jsonFromDistanceSensor);

                try {
                    /*json-to-object mapper*/
                    DistanceObject distanceObject = objectMapper.readValue(jsonFromDistanceSensor, DistanceObject.class);
                    /*checking distance and putting in the list*/
                    if (Integer.valueOf(distanceObject.getDistance()) < 11) {
                        if (sensorData.containsKey(distanceObject.getPerson()) == false) {
                            sensorData.put(distanceObject.getPerson(), distanceObject.getSongList());
                        }
                    } else {
                        if (sensorData.containsKey(distanceObject.getPerson()) == true) {
                            sensorData.remove(distanceObject.getPerson());
                        }
                    }

                    iotPlayList = new HashMap<>();
                    for (String key : sensorData.keySet()) {

                        String songs[] = sensorData.get(key);

                        for (int i = 0; i < songs.length; i++) {
                            String songName = songs[i];
                            if (iotPlayList.containsKey(songName) == false) {
                                iotPlayList.put(songName, 1);
                            } else {
                                iotPlayList.put(songName, iotPlayList.get(songName) + 1);
                            }
                        }
                    }

                    Map<String, Integer> sortedIoTPlayList = sortByReverseOrder(iotPlayList);

                    /*  array of songs based on priority from sorted playlist*/
                    Collection<String> finalSongCollection = sortedIoTPlayList.keySet();
                    String[] finalPlayList = finalSongCollection.toArray(new String[finalSongCollection.size()]);

                    // array of current guests
                    Collection<String> currentGuests = sensorData.keySet();
                    String[] guestList = currentGuests.toArray(new String[currentGuests.size()]);

                    //call to music actuator to play song
                    if(finalPlayList.length > 0) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                MusicRequest musicRequest = new MusicRequest();
                                musicRequest.currentSong = finalPlayList[0];
                                String jsonRequest = null;
                                try {
                                    jsonRequest = objectMapper.writeValueAsString(musicRequest);
                                } catch (JsonProcessingException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    musicActuatorClient.post(jsonRequest, MediaTypeRegistry.APPLICATION_JSON);
                                } catch (ConnectorException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                responseData.setCurrentSong(finalPlayList[0]);
                            }
                        }).start();
                    }

                    responseData.setPerson(distanceObject.getPerson());
                    responseData.setDistance(distanceObject.getDistance());
                    responseData.setSongList(finalPlayList);
                    responseData.setGuestList(guestList);

                    //System.out.println(iotPlayList.toString());
                    //System.out.println("size of array: "+finalPlayList.length);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            /*playlist sorting (reverse order)*/
            private Map<String, Integer> sortByReverseOrder(Map<String, Integer> songPriorityMapping) {
                return songPriorityMapping.entrySet()
                        .stream()
                        .sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
            }

            @Override
            public void onError() {

            }
        });
    }

}

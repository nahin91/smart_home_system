package com.iotproject.nahin.smart_home_system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iotproject.nahin.smart_home_system.Model.DistanceObject;
import com.iotproject.nahin.smart_home_system.Model.ResponseDistanceData;
import com.iotproject.nahin.smart_home_system.Sensors.DistanceSensor;
import org.eclipse.californium.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
public class SmartHomeSystemApplication {

    @Autowired
    public static ResponseDistanceData responseData;
    public static Map<String, String[]> songPriorityMapping;
    public static Map<String, Integer> finalSortedSongs;
    public static String currentPerson = null;
    public static ObjectMapper objectMapper;

    public static void main(String[] args) {
        SpringApplication.run(SmartHomeSystemApplication.class, args);

        /*initialized once but inner properties gets changed on time being.*/
        responseData = new ResponseDistanceData();
        songPriorityMapping = new HashMap<>();
        finalSortedSongs = new HashMap<>();
        objectMapper = new ObjectMapper();

        /*coap server configuration.*/
        CoapServer coapServer = new CoapServer(5683);
        coapServer.add(new DistanceSensor("distance"));
        coapServer.start();

        /*client config with the corresponding server*/
        CoapClient coapClient = new CoapClient("coap://localhost:5683/distance");

        CoapObserveRelation relation = coapClient.observe(new CoapHandler() {
            @Override
            public void onLoad(CoapResponse coapResponse) {
                String jsonFromDistanceSensor = coapResponse.getResponseText();
                System.out.println(jsonFromDistanceSensor);

                try {
                    /*json-to-object-to-xml mapper*/
                    DistanceObject distanceObject = objectMapper.readValue(jsonFromDistanceSensor, DistanceObject.class);
                    //System.out.println(jsonFromDistanceSensor);

                    if (Integer.valueOf(distanceObject.getDistance()) < 11) {
                        if (songPriorityMapping.containsKey(distanceObject.getPerson()) == false) {
                            songPriorityMapping.put(distanceObject.getPerson(), distanceObject.getSongList());
                        }
                    } else {
                        if (songPriorityMapping.containsKey(distanceObject.getPerson()) == true) {
                            songPriorityMapping.remove(distanceObject.getPerson());
                        }
                    }

                    finalSortedSongs = new HashMap<>();
                    for (String key : songPriorityMapping.keySet()) {

                        String songs[] = songPriorityMapping.get(key);

                        for (int i = 0; i < songs.length; i++) {
                            String songName = songs[i];
                            if (finalSortedSongs.containsKey(songName) == false) {
                                finalSortedSongs.put(songName, 1);
                            } else {
                                finalSortedSongs.put(songName, finalSortedSongs.get(songName) + 1);
                            }
                        }
                    }

                    Map<String, Integer> sortedList = sortByReverseOrder(finalSortedSongs);

                    /* creating array of songs based on priority from sorted playlist*/
                    Collection<String> selectedSongs = sortedList.keySet();
                    String[] selectedSongList = selectedSongs.toArray(new String[selectedSongs.size()]);

                    responseData.setPerson(distanceObject.getPerson());
                    responseData.setDistance(distanceObject.getDistance());
                    responseData.setSongList(selectedSongList);

                    System.out.println(finalSortedSongs.toString());
                    System.out.println("size of array: "+selectedSongList.length);

                    /*if (Integer.valueOf(distanceObject.getDistance()) < 11) {

                        String songs[] = distanceObject.getSongList();

                        for (int i = 0; i < songs.length; i++) {
                            String songName = songs[i];
                            if(songPriorityMapping.containsKey(songName) == false) {
                                songPriorityMapping.put(songName, 1);
                            } else {
                                songPriorityMapping.put(songName, songPriorityMapping.get(songName) + 1);
                            }
                        }
                    } else {
                        String songs[] = distanceObject.getSongList();
                        for (int i = 0; i < songs.length; i++) {
                            String songName = songs[i];
                            if(songPriorityMapping.containsKey(songName) == true) {
                                songPriorityMapping.put(songName, songPriorityMapping.get(songName) - 1);
                            }

                            if(songPriorityMapping.containsKey(songName) && songPriorityMapping.get(songName) == 0) {
                                songPriorityMapping.remove(songName);
                            }
                        }
                    }*/

                    //System.out.println(distanceObject.getPerson() + " = " + songPriorityMapping.toString());

                    /*creating most common song list from all guests.*/
                    /*for (int i = 0; i < distanceObject.getSongList().length; i++) {

                     *//*checking if the distance and applying condition*//*
                        if (Integer.valueOf(distanceObject.getDistance()) < 11) {
                            if (Integer.valueOf(distanceObject.getDistance()) < 11 && !(currentPerson == distanceObject.getPerson()) && songPriorityMapping.containsKey(distanceObject.getSongList()[i])) {
                                *//*assigning priority to the songs and putting in the play list*//*
                                int newPriority = songPriorityMapping.get(distanceObject.getSongList()[i]) + 1;
                                songPriorityMapping.put(distanceObject.getSongList()[i], newPriority);
                            } else {
                                songPriorityMapping.putIfAbsent(distanceObject.getSongList()[i], 0);
                            }
                        } else {
                            int newPriority = songPriorityMapping.get(distanceObject.getSongList()[i]) - 1;
                            songPriorityMapping.put(distanceObject.getSongList()[i], newPriority);
                        }
                    }*/
                    //currentPerson = distanceObject.getPerson();
                    /*sorting playlist based on priority value*/
                    //Map<String, Integer> sortedList = sortByReverseOrder(songPriorityMapping);

                    /* creating array of songs based on priority from sorted playlist*/
                    //Collection<String> selectedSongs = sortedList.keySet();
                    //String[] selectedSongList = selectedSongs.toArray(new String[selectedSongs.size()]);

                    /*responseData.setPerson(currentPerson);
                    responseData.setDistance(distanceObject.getDistance());
                    responseData.setSongList(selectedSongList);


                    ConcurrentCoapResource coapResource = new ConcurrentCoapResource("actuator") {
                        @Override
                        public void handleGET(CoapExchange exchange) {
                            try {
                                String jsonToMusicActuator = objectMapper.writeValueAsString(selectedSongList);
                                exchange.respond(jsonToMusicActuator);
                            } catch (JsonProcessingException e) {
                            }
                        }
                    };*/

                    /*testing values*/
                    //System.out.println("current song priorities: " + songPriorityMapping);
                    //System.out.println("data from coap sensor = " + jsonFromDistanceSensor);

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

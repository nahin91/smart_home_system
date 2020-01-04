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

                    //enlisting current guests
                    Collection<String> currentGuests = songPriorityMapping.keySet();
                    String[] guestList = currentGuests.toArray(new String[currentGuests.size()]);


                    responseData.setPerson(distanceObject.getPerson());
                    responseData.setDistance(distanceObject.getDistance());
                    responseData.setSongList(selectedSongList);
                    responseData.setGuestList(guestList);

                    System.out.println(finalSortedSongs.toString());
                    System.out.println("size of array: "+selectedSongList.length);

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

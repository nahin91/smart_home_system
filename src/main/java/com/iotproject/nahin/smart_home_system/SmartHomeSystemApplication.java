package com.iotproject.nahin.smart_home_system;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    public static Map<String, Integer> songPriorityMapping;
    public static String currentPerson = null;

    public static void main(String[] args) {
        SpringApplication.run(SmartHomeSystemApplication.class, args);

        //initialized once but inner properties gets changed on time being.
        responseData = new ResponseDistanceData();
        songPriorityMapping = new HashMap<>();

                //coap server configuration.
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
                    //json-to-object-to-xml mapper
                    ObjectMapper objectMapper = new ObjectMapper();
                    DistanceObject distanceObject = objectMapper.readValue(jsonString, DistanceObject.class);

                    //creating most common song list from all guests.
                    for(int i=0; i<distanceObject.getSongList().length; i++){
                        //checking if the distance is more than 10m.
                        if(Integer.valueOf(distanceObject.getDistance())<11)
                        {
                            if(Integer.valueOf(distanceObject.getDistance())<11 && !(currentPerson ==distanceObject.getPerson()) &&  songPriorityMapping.containsKey(distanceObject.getSongList()[i])){
                                int newPriority = songPriorityMapping.get(distanceObject.getSongList()[i])+1;
                                songPriorityMapping.put(distanceObject.getSongList()[i], newPriority);
                            }else{
                                songPriorityMapping.putIfAbsent(distanceObject.getSongList()[i], 0);
                            }
                        }else{
                            int newPriority = songPriorityMapping.get(distanceObject.getSongList()[i])-1;
                            songPriorityMapping.put(distanceObject.getSongList()[i], newPriority);
                        }


                    }
                    currentPerson = distanceObject.getPerson();
                    //sorting playlist based on priority value
                    Map<String, Integer> sortedList = sortByReverseValue(songPriorityMapping);

                    // creating array of songs based on priority from sorted playlist
                    Collection<String> selected = sortedList.keySet();
                    String[] selectedSongList = selected.toArray(new String[selected.size()]);

                    responseData.setPerson(currentPerson);
                    responseData.setDistance(distanceObject.getDistance());
                    responseData.setSongList(selectedSongList);

                    //testing values
                    System.out.println(songPriorityMapping);
                    System.out.println("data from coap = " + jsonString);

                }
                catch (Exception e){}

            }

            // playlist sorting (reverse order)
            private Map<String, Integer> sortByReverseValue(Map<String, Integer> songPriorityMapping) {
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

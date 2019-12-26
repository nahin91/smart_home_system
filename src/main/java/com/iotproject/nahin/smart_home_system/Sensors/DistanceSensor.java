package com.iotproject.nahin.smart_home_system.Sensors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iotproject.nahin.smart_home_system.DistanceObject;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.server.resources.ConcurrentCoapResource;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class DistanceSensor extends ConcurrentCoapResource {
    public  static DistanceObject distanceObject;

    public static String[] people = {"nahin","pretom","akib","limon","rony"};
    public static String[] listOfNahin = {"A","B","C","D","E",};
    public static String[] listOfPretom = {"C","a","D","B","g",};
    public static String[] listOfAkib = {"E","D","g","A","c",};
    public static String[] listOfLimon = {"C","f","1","2","c",};
    public static String[] listOfRony = {"2","A","c","a","E",};


    public DistanceSensor(String name)
    {
        super(name);
        distanceObject = new DistanceObject();

        setObservable(true);
        setObserveType(CoAP.Type.CON);
        getAttributes().setObservable();

        Timer timer = new Timer();
        timer.schedule(new ContinuousTask(), 0, 3000);

    }

    private class ContinuousTask extends TimerTask{
        @Override
        public void run(){
            String person = getRandomPerson(people);

            switch (person){
                case "nahin":
                    distanceObject.setPerson(person);
                    distanceObject.setDistance(getRandomDistance() + "");
                    distanceObject.setSongList(listOfNahin);
                    break;
                case "pretom":
                    distanceObject.setPerson(person);
                    distanceObject.setDistance(getRandomDistance() + "");
                    distanceObject.setSongList(listOfPretom);
                    break;
                case "akib":
                    distanceObject.setPerson(person);
                    distanceObject.setDistance(getRandomDistance() + "");
                    distanceObject.setSongList(listOfAkib);
                    break;
                case "limon":
                    distanceObject.setPerson(person);
                    distanceObject.setDistance(getRandomDistance() + "");
                    distanceObject.setSongList(listOfLimon);
                    break;
                case "rony":
                    distanceObject.setPerson(person);
                    distanceObject.setDistance(getRandomDistance() + "");
                    distanceObject.setSongList(listOfRony);
                    break;
            }
/*            distanceObject.setPerson(person);
            distanceObject.setDistance(getRandomDistance() + "");*/

            changed();
        }
    }

    public String getRandomPerson(String[] array) {
        int randomIndex = new Random().nextInt(array.length);
        return array[randomIndex];
    }
    public int getRandomDistance() {
        return new Random().nextInt(21);
    }

    @Override
    public void handleGET(CoapExchange exchange) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(distanceObject);
            exchange.respond(jsonString);
        }
        catch (Exception  e){

        }
    }

}

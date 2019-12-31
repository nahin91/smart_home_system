package com.iotproject.nahin.smart_home_system.Sensors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iotproject.nahin.smart_home_system.Model.DistanceObject;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.server.resources.ConcurrentCoapResource;

import java.util.*;

public class DistanceSensor extends ConcurrentCoapResource {

    public static DistanceObject distanceObject;
    public static ArrayList<String> alreadyTakenPerson = new ArrayList<>();

    public static String[] people = {"nahin", "pretom", "akib", "limon", "rony"};

    public static String[] listOfNahinSong = {"Numb - Linkin Park", "Your Sky - Coldplay", "Take Me Home, Country Roads - John Denver ♥",
            "Rockabye - Clean Bandit", "Annie's Song - John Denver ♥","Miftah Zaman- Obelai"};
    public static String[] listOfPretomSong = {"Numb - Linkin Park", "Viva la Vida - Coldplay", "Rockabye - Clean Bandit",
            "Your Sky - Coldplay", "Wavin' Flag - K'NAAN (FIFA 2010)"};
    public static String[] listOfAkibSong = {"Annie's Song - John Denver ♥", "Rockabye - Clean Bandit", "Sing Nachtigall Sing - Evelyn Künneke",
            "Numb - Linkin Park", "Revolverheld feat. Marta Jandová - Halt Dich an mir fest"};
    public static String[] listOfLimonSong = {"Take Me Home, Country Roads - John Denver ♥", "Panchi. Jal featuring Quratulain Balouch Coke Studio",
            "Sing Nachtigall Sing - Evelyn Künneke", "Revolverheld feat. Marta Jandová - Halt Dich an mir fest"};
    public static String[] listOfRonySong = {"Sing Nachtigall Sing - Evelyn Künneke", "Numb - Linkin Park", "Revolverheld feat. Marta Jandová - Halt Dich an mir fest",
            "Viva la Vida - Coldplay", "Annie's Song - John Denver ♥"};


    public DistanceSensor(String name) {
        super(name);
        distanceObject = new DistanceObject();

        setObservable(true);
        setObserveType(CoAP.Type.CON);
        getAttributes().setObservable();

        Timer timer = new Timer();
        timer.schedule(new ContinuousTask(), 0, 7000);
    }

    private class ContinuousTask extends TimerTask {
        @Override
        public void run() {

            distanceObject.setDistance(getRandomDistance() + "");
            String person = getRandomPerson(people);

            switch (person) {
                case "nahin":
                    distanceObject.setPerson(person);
                    distanceObject.setSongList(listOfNahinSong);
                    break;
                case "pretom":
                    distanceObject.setPerson(person);
                    distanceObject.setSongList(listOfPretomSong);
                    break;
                case "akib":
                    distanceObject.setPerson(person);
                    distanceObject.setSongList(listOfAkibSong);
                    break;
                case "limon":
                    distanceObject.setPerson(person);
                    distanceObject.setSongList(listOfLimonSong);
                    break;
                case "rony":
                    distanceObject.setPerson(person);
                    distanceObject.setSongList(listOfRonySong);
                    break;
            }

            changed();
        }
    }

    /*random selection from people list*/
    public String getRandomPerson(String[] array) {

        int randomIndex = new Random().nextInt(array.length);
        String personName = array[randomIndex];

        /*if(alreadyTakenPerson.contains(personName) == false && Integer.valueOf(distanceObject.getDistance()) > 0) {

        }*/

        return personName;
    }

    /*random distance for selected person from randoms*/
    public int getRandomDistance() {
        return new Random().nextInt(21);
    }

    /*converts the object data into json format*/
    @Override
    public void handleGET(CoapExchange exchange) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(distanceObject);
            exchange.respond(jsonString);
        } catch (Exception e) {
        }
    }

}

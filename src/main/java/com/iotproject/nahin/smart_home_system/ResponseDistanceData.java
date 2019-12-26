package com.iotproject.nahin.smart_home_system;

public class ResponseDistanceData {

    private String distance;
    private String person;
    private String[] songList;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance){
        this.distance = distance;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String[] getSongList() { return songList; }

    public void setSongList(String[] songList) { this.songList = songList; }
}

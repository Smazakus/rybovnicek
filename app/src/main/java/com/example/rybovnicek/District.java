package com.example.rybovnicek;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class District implements Serializable {

    private String name;
    private String link;
    private int distance;
    private List<List<Float>> coords;

    public District(String name, int distance) {
        this.name = name;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public List<List<Float>> getCoords() { return coords; }

    public void setCoords(List<List<Float>> coords) { this.coords = coords; }

    public String getLink() { return link; }

    public void setLink(String link) { this.link = link; }
}

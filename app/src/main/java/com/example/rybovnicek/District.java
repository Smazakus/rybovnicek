package com.example.rybovnicek;

import java.io.Serializable;
import java.util.List;

public class District implements Serializable {

    private String name;
    private String link;
    private String regNum;
    private double distance;
    private List<List<Float>> coords;

    public District(String name, int distance) {
        this.name = name;
        this.distance = distance;
    }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public double getDistance() { return distance; }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public List<List<Float>> getCoords() { return coords; }

    public void setCoords(List<List<Float>> coords) { this.coords = coords; }

    public String getLink() { return link; }

    public void setLink(String link) { this.link = link; }

    public String getRegNumber() { return regNum; }

    public void setRegNumber(String regNum) { this.regNum = regNum; }

    @Override
    public String toString() {
        return "District{" +
                "name='" + name + '\'' +
                ", link='" + link + '\'' +
                ", regNum=" + regNum +
                ", distance=" + distance +
                ", coords=" + coords +
                '}';
    }
}

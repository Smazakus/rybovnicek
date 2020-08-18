package com.example.rybovnicek;

import java.io.Serializable;

public class Fish implements Serializable {

    private long date;
    private String name;
    private int length;

    public Fish(int date, String name, int length) {
        this.date = date;
        this.name = name;
        this.length = length;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "Fish{" +
                "date=" + date +
                ", name='" + name + '\'' +
                ", length=" + length +
                '}';
    }
}

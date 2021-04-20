package com.example.scaleapplication;

public class ScaleData {
    public float weight;
    public String time;
    public String version;

    public ScaleData(float weight, String version, String time) {
        this.weight = weight;
        this.version = version;
        this.time = time;
    }

    public String toString() {
        return "ScaleData [weight=" + weight + "]";
    }
}
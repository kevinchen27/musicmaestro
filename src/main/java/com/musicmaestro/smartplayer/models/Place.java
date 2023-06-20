package com.musicmaestro.smartplayer.models;

public enum Place {
    BEACH("Beach"),
    LIBRARY("Library"),
    CAR("Car"),
    GYM("Gym"),
    CLUB("Club"),
    HOME("Home");

    private final String displayName;

    Place(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

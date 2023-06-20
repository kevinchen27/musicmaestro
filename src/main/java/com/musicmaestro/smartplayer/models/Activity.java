package com.musicmaestro.smartplayer.models;

public enum Activity {
    DATE("Date"),
    STUDYING("Studying"),
    DRIVING("Driving"),
    WORKING_OUT("Working Out"),
    PARTYING("Partying"),
    RELAXING("Relaxing");

    private final String displayName;

    Activity(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

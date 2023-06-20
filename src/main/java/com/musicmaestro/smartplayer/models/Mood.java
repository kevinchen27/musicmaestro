package com.musicmaestro.smartplayer.models;

public enum Mood {
    HAPPY("Happy"),
    SAD("Sad"),
    ENERGETIC("Energetic"),
    CALM("Calm"),
    FOCUSED("Focused");

    private final String displayName;

    Mood(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}

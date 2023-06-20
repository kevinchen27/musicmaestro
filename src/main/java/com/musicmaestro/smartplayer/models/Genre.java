package com.musicmaestro.smartplayer.models;

public enum Genre {
    POP("Pop"),
    ROCK("Rock"),
    HIP_HOP_RAP("Hip-Hop/Rap"),
    ELECTRONIC_DANCE("Electronic/Dance"),
    RNB_SOUL("R&B/Soul"),
    COUNTRY("Country"),
    JAZZ("Jazz"),
    CLASSICAL("Classical"),
    REGGAE("Reggae"),
    ALTERNATIVE("Alternative");

    private final String displayName;

    Genre(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

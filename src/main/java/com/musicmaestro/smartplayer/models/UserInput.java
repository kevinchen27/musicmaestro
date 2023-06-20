package com.musicmaestro.smartplayer.models;

import lombok.Data;

@Data
public class UserInput {
    private Mood mood;
    private Place place;
    private Time time;
    private Genre genre;
    private Activity activity;

    // Constructors, getters, and setters

    // Additional methods as needed
}

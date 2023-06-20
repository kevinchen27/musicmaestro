package com.musicmaestro.smartplayer.models;

import lombok.Data;

import java.util.List;

@Data
public class SpotifyArtist {
    private String id;
    private String name;
    private List<String> genres;
    private int popularity;
}

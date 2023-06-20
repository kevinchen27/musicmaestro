package com.musicmaestro.smartplayer.models;

import lombok.Data;

import java.util.List;

@Data
public class SpotifyTrack {
    private String id;
    private String name;
    private SpotifyAlbum album;
    private List<SpotifyArtist> artists;
    private int durationMs;
}


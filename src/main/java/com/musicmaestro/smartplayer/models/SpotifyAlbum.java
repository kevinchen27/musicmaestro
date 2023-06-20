package com.musicmaestro.smartplayer.models;

import lombok.Data;

import java.util.List;

@Data
public class SpotifyAlbum {
    private String id;
    private String name;
    private List<String> genres;
    private List<SpotifyArtist> artists;
}

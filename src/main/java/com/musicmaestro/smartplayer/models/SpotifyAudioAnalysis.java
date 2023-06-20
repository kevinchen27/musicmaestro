package com.musicmaestro.smartplayer.models;

import lombok.Data;

import java.util.List;

@Data
public class SpotifyAudioAnalysis {
    private String trackId;
    private double loudness;
    private double tempo;
    private int timeSignature;
    private int key;
    private int mode;
    private String rhythmString;
    private List<Double> pitches;
    private List<Double> timbre;
}

package com.musicmaestro.smartplayer.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PKCEUtils {

    public static String generateCodeVerifier() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] codeVerifier = new byte[64];
        secureRandom.nextBytes(codeVerifier);
        return base64UrlEncode(codeVerifier);
    }

    public static String generateCodeChallenge(String codeVerifier) throws NoSuchAlgorithmException {
        byte[] codeVerifierBytes = codeVerifier.getBytes();
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] codeChallengeBytes = messageDigest.digest(codeVerifierBytes);
        return base64UrlEncode(codeChallengeBytes);
    }

    private static String base64UrlEncode(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
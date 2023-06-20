package com.musicmaestro.smartplayer.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.musicmaestro.smartplayer.utils.PKCEUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@PropertySource("classpath:application.properties")
@Controller
public class SpotifyAuthController {

    private String CLIENT_ID = System.getenv("SPOTIFY_CLIENT_ID");

    @Value("${spotify.redirectUri}")
    private String REDIRECT_URI;

    @Value("${spotify.scope}")
    private String SCOPE;

    private final String STATE = generateRandomStateValue(16);

    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        // Generate the code verifier and code challenge
        String codeVerifier = PKCEUtils.generateCodeVerifier();
        String codeChallenge;
        try {
            codeChallenge = PKCEUtils.generateCodeChallenge(codeVerifier);
        } catch (NoSuchAlgorithmException e) {
            // Handle the exception
            // For simplicity, let's redirect to an error page
            return "redirect:/error";
        }

        // Save the code verifier in the session for later use
        HttpSession session = request.getSession();
        session.setAttribute("codeVerifier", codeVerifier);

        // Redirect the user to the Spotify authorization endpoint
        String authorizationUrl = buildAuthorizationUrl(codeChallenge);
        return "redirect:" + authorizationUrl;
    }

    private String buildAuthorizationUrl(String codeChallenge) {
        // Build the authorization URL with the required parameters
        String authorizationUrl = "https://accounts.spotify.com/authorize";
        authorizationUrl += "?client_id=" + CLIENT_ID;
        authorizationUrl += "&response_type=code";
        authorizationUrl += "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, StandardCharsets.UTF_8);
        authorizationUrl += "&scope=" + URLEncoder.encode(SCOPE, StandardCharsets.UTF_8);
        authorizationUrl += "&code_challenge_method=S256";
        authorizationUrl += "&code_challenge=" + URLEncoder.encode(codeChallenge, StandardCharsets.UTF_8);
        authorizationUrl += "&state=" + STATE;

        return authorizationUrl;
    }


    @GetMapping("/callback")
    public String callback(@RequestParam("code") String authorizationCode, @RequestParam("state") String state, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String savedState = (String) session.getAttribute("state");
        if (state == null || !state.equals(savedState)) {
            return "redirect:/error";
        }

        String accessToken = exchangeAuthorizationCode(authorizationCode);
        if (accessToken == null) {
            return "redirect:/error";
        }

        session.setAttribute("accessToken", accessToken);
        return "redirect:/dashboard";
    }

    private String exchangeAuthorizationCode(String authorizationCode) {
        try {
            HttpClient httpClient = HttpClient.newBuilder().build();
            String tokenUrl = "https://accounts.spotify.com/api/token";
            String clientId = System.getenv("SPOTIFY_CLIENT_ID");
            String clientSecret = System.getenv("SPOTIFY_CLIENT_SECRET");
            String redirectUri = "http://localhost:8080/callback";

            Map<String, String> parameters = new HashMap<>();
            parameters.put("grant_type", "authorization_code");
            parameters.put("code", authorizationCode);
            parameters.put("redirect_uri", redirectUri);

            String credentials = clientId + ":" + clientSecret;
            String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(tokenUrl))
                    .header("Authorization", "Basic " + encodedCredentials)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(buildFormData(parameters))
                    .build();

            HttpResponse<String> response = null;
            try {
                response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response.statusCode() == 200) {
                JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
                return json.get("access_token").getAsString();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private HttpRequest.BodyPublisher buildFormData(Map<String, String> data) {
        String encodedData = data.entrySet().stream()
                .map(entry -> encode(entry.getKey()) + "=" + encode(entry.getValue()))
                .collect(Collectors.joining("&"));
        return HttpRequest.BodyPublishers.ofString(encodedData);
    }

    private String encode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String generateRandomStateValue(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[length];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

}



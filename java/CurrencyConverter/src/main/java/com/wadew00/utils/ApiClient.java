package com.wadew00.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiClient {
    private static final String API_KEY;
    private static final String BASE_URL;
    private static final String CONFIG_FILE = "config.properties";
    private static final String PROP_API_KEY = "api.key";
    private static final String PROP_BASE_URL = "base.url";

    static {
        Properties prop = new Properties();
        try (InputStream input = ApiClient.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new RuntimeException("Unable to find " + CONFIG_FILE);
            }
            prop.load(input);
            API_KEY = prop.getProperty(PROP_API_KEY);
            if (API_KEY == null || API_KEY.trim().isEmpty()) {
                throw new RuntimeException("Missing or empty property: " + PROP_API_KEY);
            }

            String baseUrl = prop.getProperty(PROP_BASE_URL);
            if (baseUrl == null || baseUrl.trim().isEmpty()) {
                throw new RuntimeException("Missing or empty property: " + PROP_BASE_URL);
            }
            BASE_URL = baseUrl + "?app_id=" + API_KEY;
        } catch (Exception e) {
            throw new RuntimeException("Error loading configuration: " + e.getMessage(), e);
        }
    }
    public static String getApiKey() {
        return API_KEY;
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static JSONObject fetchExchangeRates() {
        try {
            URL url = new URL(BASE_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                try {
                    return new JSONObject(response.toString());
                } catch (JSONException e) {
                    System.err.println("Invalid JSON: " + e.getMessage());
                    return null;
                }
            } else {
                System.err.println("HTTP Error: " + responseCode);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            return null;
        } 
    }
}
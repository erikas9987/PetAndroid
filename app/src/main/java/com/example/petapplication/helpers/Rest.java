package com.example.petapplication.helpers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Rest {

    private static HttpURLConnection setupConnection(String url, String method) throws IOException {
        URL urlObj = new URL(url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) urlObj.openConnection();
        httpURLConnection.setRequestMethod(method);
        httpURLConnection.setConnectTimeout(20000);
        httpURLConnection.setReadTimeout(20000);
        httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        httpURLConnection.setRequestProperty("Accept", "application/json");
        if (!method.equals("GET")) {
            httpURLConnection.setDoOutput(true);
        }
        httpURLConnection.setDoInput(true);
        return httpURLConnection;
    }

    private static void write(HttpURLConnection httpURLConnection, String jsonInfo) throws IOException {
        try (OutputStream outputStream = httpURLConnection.getOutputStream();
             BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream))) {
            bufferedWriter.write(jsonInfo);
        }
    }

    private static String read(HttpURLConnection httpURLConnection) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }

    public static String sendGet(String getUrl) throws IOException {
        HttpURLConnection httpURLConnection = setupConnection(getUrl, "GET");
        try {
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return read(httpURLConnection);
            } else {
                return "Response code: " + responseCode;
            }
        } finally {
            httpURLConnection.disconnect();
        }
    }

    public static String sendPost(String postUrl, String jsonInfo) throws IOException {
        HttpURLConnection httpURLConnection = setupConnection(postUrl, "POST");
        try {
            write(httpURLConnection, jsonInfo);
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return read(httpURLConnection);
            } else {
                return "Response code: " + responseCode;
            }
        } finally {
            httpURLConnection.disconnect();
        }
    }

    public static String sendPut(String postUrl, String jsonInfo) throws IOException {
        HttpURLConnection httpURLConnection = setupConnection(postUrl, "PUT");
        try {
            write(httpURLConnection, jsonInfo);
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return read(httpURLConnection);
            } else {
                return "Response code: " + responseCode;
            }
        } finally {
            httpURLConnection.disconnect();
        }
    }

    public static String sendDelete(String postUrl, String jsonInfo) throws IOException {
        HttpURLConnection httpURLConnection = setupConnection(postUrl, "DELETE");
        try {
            write(httpURLConnection, jsonInfo);
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return read(httpURLConnection);
            } else {
                return "Response code: " + responseCode;
            }
        } finally {
            httpURLConnection.disconnect();
        }
    }
}

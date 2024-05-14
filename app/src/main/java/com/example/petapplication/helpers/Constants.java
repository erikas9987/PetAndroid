package com.example.petapplication.helpers;

public class Constants {

    public static final String IP_ADDRESS = "192.168.0.7";
    public static final String PORT = "8080";
    public static final String BASE_URL = "http://" + IP_ADDRESS + ":" + PORT;
    public static final String AUTH_URL = BASE_URL + "/auth";
    public static final String LOGIN_URL = AUTH_URL + "/authenticate";
    public static final String REGISTER_URL = AUTH_URL + "/register";
}

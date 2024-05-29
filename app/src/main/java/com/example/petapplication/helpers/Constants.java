package com.example.petapplication.helpers;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

public class Constants {

    public static final String IP_ADDRESS = "192.168.0.7";
    public static final String PORT = "8080";
    public static final String BASE_URL = "http://" + IP_ADDRESS + ":" + PORT;
    public static final String AUTH_URL = BASE_URL + "/auth";
    public static final String LOGIN_URL = AUTH_URL + "/authenticate";
    public static final String REGISTER_URL = AUTH_URL + "/register";
    public static final String ALL_SPECIES_URL = BASE_URL + "/species";
    @NonNull
    @Contract(pure = true)
    public static String ADD_PET_URL(int id) {
        return BASE_URL + "/pets/user/" + id;
    }
}

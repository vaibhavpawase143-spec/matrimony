package com.example.model;

import lombok.Data;

@Data
public class BirthDetails {
    private String date;     // yyyy-MM-dd
    private String time;     // HH:mm
    private double latitude;
    private double longitude;
}
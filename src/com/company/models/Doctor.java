package com.company.models;

public class Doctor {
    private int id;
    private String name;
    private String specialization;

    public Doctor(String name, String specialization) {
        this.name = name;
        this.specialization = specialization;
    }

    public Doctor(int id, String name, String specialization) {
        this(name, specialization);
        this.id = id;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getSpecialization() { return specialization; }
}

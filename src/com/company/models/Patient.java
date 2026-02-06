package com.company.models;

public class Patient {
    private int id;
    private String name;
    private int age;
    private String gender;

    public Patient(String name, int age, String gender) {
        if (name == null || name.isBlank()) throw new RuntimeException("Name cannot be empty");
        if (age <= 0) throw new RuntimeException("Age must be positive");
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    public Patient(int id, String name, int age, String gender) {
        this(name, age, gender);
        this.id = id;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGender() { return gender; }

    @Override
    public String toString() {
        return name + " | " + age + " | " + gender;
    }
}

package com.company.models;

public class Doctor {
    private int id;
    private String name;
    private String specialization;

    // конструктор для нового врача (без id)
    public Doctor(String name, String specialization) {
        this.name = name;
        this.specialization = specialization;
    }

    // конструктор для врача, который уже есть в базе (с id)
    public Doctor(int id, String name, String specialization) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
    }

    // get методы
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpecialization() {
        return specialization;
    }

    // set методы
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    // удобный вывод в консоль
    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", specialization='" + specialization + '\'' +
                '}';
    }
}

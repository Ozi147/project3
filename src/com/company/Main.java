package com.company;

import com.company.data.PostgresDB;
import com.company.models.Patient;

public class Main {
    public static void main(String[] args) {
        PostgresDB db = new PostgresDB(); // проверка соединения
        Patient p = new Patient("Alice", 25, "Female", "NORMAL");
        System.out.println(p.getName() + " создан!");
    }
}
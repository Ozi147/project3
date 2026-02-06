package com.company;

import java.util.Scanner;

public final class InputValidator {
    private InputValidator() {}

    public static String readRealName(Scanner sc) {
        while (true) {
            String name = sc.nextLine().trim();
            if (name.matches("^[A-Za-z]+(?:[ -][A-Za-z]+)*$")) return name;
            System.out.println("Invalid name. Use English letters only (example: John Smith). Try again:");
        }
    }

    public static int readAge(Scanner sc) {
        while (true) {
            String s = sc.nextLine().trim();
            if (s.matches("^\\d+$")) {
                int age = Integer.parseInt(s);
                if (age >= 1 && age <= 120) return age;
            }
            System.out.println("Invalid age. Digits only (1-120). Try again:");
        }
    }

    public static String readUsername(Scanner sc) {
        while (true) {
            String username = sc.nextLine().trim();
            if (username.matches("^[A-Za-z0-9_]{3,20}$")) return username;
            System.out.println("Invalid username. Use 3-20 chars: letters, digits, underscore. Try again:");
        }
    }

    public static String readPassword(Scanner sc) {
        while (true) {
            String password = sc.nextLine();
            if (password != null && password.length() >= 4) return password;
            System.out.println("Password is too short. Minimum 4 characters. Try again:");
        }
    }
}
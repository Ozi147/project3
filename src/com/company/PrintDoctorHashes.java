package com.company;

public class PrintDoctorHashes {
    public static void main(String[] args) {
        String[] passwords = {
                "1111", "2222", "3333", "4444",
                "5555", "6666", "7777", "8888"
        };

        for (String p : passwords) {
            System.out.println(p + " -> " + Password.sha256(p));
        }
    }
}
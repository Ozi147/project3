package com.company.repositories;

public interface IDoctorAccountRepository {
    boolean createAccount(int doctorId, String username, String passwordHash);
    Integer findDoctorIdByCredentials(String username, String passwordHash);
}
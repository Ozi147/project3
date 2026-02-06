package com.company.repositories;

public interface IPatientAccountRepository {
    boolean createAccount(int patientId, String username, String passwordHash);
    Integer findPatientIdByCredentials(String username, String passwordHash);
}
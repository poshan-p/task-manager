package com.poshanp.task.manager.application.services.interfaces;

public interface IPasswordService {
    String encode(String password);

    boolean matches(String password, String hashedPassword);
}

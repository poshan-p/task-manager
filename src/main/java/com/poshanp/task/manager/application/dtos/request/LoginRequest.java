package com.poshanp.task.manager.application.dtos.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {
    private String login;
    private String password;

    public boolean isEmail() {
        return this.login != null && this.login.contains("@");
    }

    public boolean isUsername() {
        return this.login != null && !this.login.contains("@");
    }
}

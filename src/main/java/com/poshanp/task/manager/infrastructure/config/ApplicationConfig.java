package com.poshanp.task.manager.infrastructure.config;

import com.poshanp.task.manager.application.repositories.IUserRepository;
import com.poshanp.task.manager.application.services.impl.AuthServiceImpl;
import com.poshanp.task.manager.application.services.impl.TaskServiceImpl;
import com.poshanp.task.manager.application.services.interfaces.IAuthService;
import com.poshanp.task.manager.application.services.interfaces.IPasswordService;
import com.poshanp.task.manager.application.services.interfaces.ITaskService;
import com.poshanp.task.manager.application.services.interfaces.ITokenService;
import com.poshanp.task.manager.application.repositories.ITaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public IAuthService authService(
            IPasswordService passwordService,
            ITokenService tokenService,
            IUserRepository userRepository) {
        return new AuthServiceImpl(passwordService, tokenService, userRepository);
    }

    @Bean
    public ITaskService taskService(
            ITaskRepository taskRepository,
            IUserRepository userRepository) {
        return new TaskServiceImpl(taskRepository, userRepository);
    }

}
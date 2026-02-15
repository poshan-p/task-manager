package com.poshanp.task.manager.application.dtos.mappers;

import com.poshanp.task.manager.application.dtos.request.CreateTaskRequest;
import com.poshanp.task.manager.application.dtos.request.UpdateTaskRequest;
import com.poshanp.task.manager.application.dtos.response.TaskResponse;
import com.poshanp.task.manager.domain.entities.Task;
import com.poshanp.task.manager.domain.entities.User;

public class TaskMapper {
    public static Task toEntity(CreateTaskRequest request, User user) {
        Task task = new Task();
        task.setUser(user);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setStatus(request.getStatus());
        task.setDueDate(request.getDueDate());
        return task;
    }

    public static Task toEntity(UpdateTaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setStatus(request.getStatus());
        task.setDueDate(request.getDueDate());
        return task;
    }

    public static TaskResponse toTaskResponseDto(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setUser(task.getUser());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setPriority(task.getPriority());
        response.setStatus(task.getStatus());
        response.setDueDate(task.getDueDate());
        response.setCreatedAt(task.getCreatedAt());
        response.setUpdatedAt(task.getUpdatedAt());
        return response;
    }


}

package com.poshanp.task.manager.application.services.interfaces;

import com.poshanp.task.manager.application.dtos.request.CreateTaskRequest;
import com.poshanp.task.manager.application.dtos.request.UpdateTaskRequest;
import com.poshanp.task.manager.application.dtos.response.ApiResponse;
import com.poshanp.task.manager.application.dtos.response.TaskResponse;
import com.poshanp.task.manager.domain.enums.Status;

import java.util.List;

public interface ITaskService  {
    ApiResponse<TaskResponse> createTask(CreateTaskRequest request, long userId);

    ApiResponse<List<TaskResponse>> getUserTasks(long userId, Status status);

    ApiResponse<TaskResponse> getTaskById(long taskId, long userId);

    ApiResponse<TaskResponse> updateTask(long taskId, UpdateTaskRequest request, long userId);

    ApiResponse<Boolean> deleteTask(long taskId, long userId);

    ApiResponse<List<TaskResponse>> getOverdueTasks(long userId);
}

package com.poshanp.task.manager.application.services.impl;

import com.poshanp.task.manager.application.dtos.mappers.TaskMapper;
import com.poshanp.task.manager.application.dtos.request.CreateTaskRequest;
import com.poshanp.task.manager.application.dtos.request.UpdateTaskRequest;
import com.poshanp.task.manager.application.dtos.response.ApiResponse;
import com.poshanp.task.manager.application.dtos.response.TaskResponse;
import com.poshanp.task.manager.application.repository.ITaskRepository;
import com.poshanp.task.manager.application.repository.IUserRepository;
import com.poshanp.task.manager.application.services.interfaces.ITaskService;
import com.poshanp.task.manager.domain.constants.ErrorMessage;
import com.poshanp.task.manager.domain.entities.Task;
import com.poshanp.task.manager.domain.entities.User;
import com.poshanp.task.manager.domain.enums.Status;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements ITaskService {
    private final ITaskRepository taskRepository;
    private final IUserRepository userRepository;

    public TaskServiceImpl(ITaskRepository taskRepository, IUserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ApiResponse<TaskResponse> createTask(CreateTaskRequest request, long userId) {
        User user = userRepository.findById(userId);
        if (user == null) return ApiResponse.error(ErrorMessage.AUTH_USER_NOT_FOUND);

        Task newTask = TaskMapper.toEntity(request, user);
        newTask = taskRepository.save(newTask);

        if (newTask == null) return ApiResponse.error(ErrorMessage.TASK_UNKNOWN_ERROR_WHILE_CREATING_TASK);

        return ApiResponse.success(TaskMapper.toTaskResponseDto(newTask));

    }

    @Override
    public ApiResponse<List<TaskResponse>> getUserTasks(long userId, Status status) {
        User user = userRepository.findById(userId);
        if (user == null) return ApiResponse.error(ErrorMessage.AUTH_USER_NOT_FOUND);

        List<Task> tasks = taskRepository.findByUserIdAndStatus(userId, status);
        if (tasks == null) return ApiResponse.error(ErrorMessage.TASK_UNKNOWN_ERROR_WHILE_FETCHING_TASKS);

        return ApiResponse.success(tasks.stream().map(TaskMapper::toTaskResponseDto).toList());
    }

    @Override
    public ApiResponse<TaskResponse> getTaskById(long taskId, long userId) {
        User user = userRepository.findById(userId);
        if (user == null) return ApiResponse.error(ErrorMessage.AUTH_USER_NOT_FOUND);

        Task task = taskRepository.findById(taskId);
        if (task == null) return ApiResponse.error(ErrorMessage.TASK_TASK_NOT_FOUND);

        return ApiResponse.success(TaskMapper.toTaskResponseDto(task));
    }

    @Override
    public ApiResponse<TaskResponse> updateTask(long taskId, UpdateTaskRequest request, long userId) {
        User user = userRepository.findById(userId);
        if (user == null) return ApiResponse.error(ErrorMessage.AUTH_USER_NOT_FOUND);

        Task task = taskRepository.findById(taskId);
        if (task == null) return ApiResponse.error(ErrorMessage.TASK_TASK_NOT_FOUND);

        Task updatedTask = TaskMapper.toEntity(request);
        updatedTask = taskRepository.update(updatedTask);

        if (updatedTask == null) return ApiResponse.error(ErrorMessage.TASK_UNKNOWN_ERROR_WHILE_UPDATING_TASK);
        return ApiResponse.success(TaskMapper.toTaskResponseDto(updatedTask));
    }

    @Override
    public ApiResponse<Boolean> deleteTask(long taskId, long userId) {
        User user = userRepository.findById(userId);
        if (user == null) return ApiResponse.error(ErrorMessage.AUTH_USER_NOT_FOUND);

        Task task = taskRepository.findById(taskId);
        if (task == null) return ApiResponse.error(ErrorMessage.TASK_TASK_NOT_FOUND);

        taskRepository.deleteByIdAndUserId(taskId, userId);
        return ApiResponse.success(true);
    }

    @Override
    public ApiResponse<List<TaskResponse>> getOverdueTasks(long userId) {
        User user = userRepository.findById(userId);
        if (user == null) return ApiResponse.error(ErrorMessage.AUTH_USER_NOT_FOUND);

        List<Task> tasks = taskRepository.findOverdueTasksByUserId(userId);
        if (tasks == null) return ApiResponse.error(ErrorMessage.TASK_UNKNOWN_ERROR_WHILE_FETCHING_TASKS);

        return ApiResponse.success(tasks.stream().map(TaskMapper::toTaskResponseDto).toList());
    }
}

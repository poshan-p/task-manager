package com.poshanp.task.manager.application.services.impl;

import com.poshanp.task.manager.application.dtos.mappers.TaskMapper;
import com.poshanp.task.manager.application.dtos.request.CreateTaskRequest;
import com.poshanp.task.manager.application.dtos.request.UpdateTaskRequest;
import com.poshanp.task.manager.application.dtos.response.ApiResponse;
import com.poshanp.task.manager.application.dtos.response.TaskResponse;
import com.poshanp.task.manager.application.repositories.ITaskRepository;
import com.poshanp.task.manager.application.repositories.IUserRepository;
import com.poshanp.task.manager.application.services.interfaces.ITaskService;
import com.poshanp.task.manager.domain.constants.ErrorMessage;
import com.poshanp.task.manager.domain.entities.Task;
import com.poshanp.task.manager.domain.entities.User;
import com.poshanp.task.manager.domain.enums.Status;
import com.poshanp.task.manager.application.exceptions.BusinessException;
import com.poshanp.task.manager.application.exceptions.ResourceNotFoundException;
import com.poshanp.task.manager.application.exceptions.UnauthorizedException;

import java.util.List;

public class TaskServiceImpl implements ITaskService {
    private final ITaskRepository taskRepository;
    private final IUserRepository userRepository;

    public TaskServiceImpl(ITaskRepository taskRepository, IUserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ApiResponse<TaskResponse> createTask(CreateTaskRequest request, long userId) {
        try {
            User user = userRepository.findById(userId);
            if (user == null) {
                throw new ResourceNotFoundException(ErrorMessage.AUTH_USER_NOT_FOUND);
            }

            Task newTask = TaskMapper.toEntity(request, user);
            newTask = taskRepository.save(newTask);

            if (newTask == null) {
                return ApiResponse.error(ErrorMessage.TASK_UNKNOWN_ERROR_WHILE_CREATING_TASK);
            }

            return ApiResponse.success(TaskMapper.toTaskResponseDto(newTask));

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorMessage.TASK_UNKNOWN_ERROR_WHILE_CREATING_TASK);
        }
    }

    @Override
    public ApiResponse<List<TaskResponse>> getUserTasks(long userId, Status status) {
        try {
            User user = userRepository.findById(userId);
            if (user == null) {
                throw new ResourceNotFoundException(ErrorMessage.AUTH_USER_NOT_FOUND);
            }

            List<Task> tasks;
            if (status != null) {
                tasks = taskRepository.findByUserIdAndStatus(userId, status);
            } else {
                tasks = taskRepository.findByUserId(userId);
            }

            if (tasks == null) {
                return ApiResponse.error(ErrorMessage.TASK_UNKNOWN_ERROR_WHILE_FETCHING_TASKS);
            }

            return ApiResponse.success(
                    tasks.stream()
                            .map(TaskMapper::toTaskResponseDto)
                            .toList()
            );

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorMessage.TASK_UNKNOWN_ERROR_WHILE_FETCHING_TASKS);
        }
    }

    @Override
    public ApiResponse<TaskResponse> getTaskById(long taskId, long userId) {
        try {

            Task task = getTask(taskId, userId);

            return ApiResponse.success(TaskMapper.toTaskResponseDto(task));
        } catch (ResourceNotFoundException | UnauthorizedException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorMessage.TASK_UNKNOWN_ERROR_WHILE_FETCHING_TASKS);
        }
    }

    private Task getTask(long taskId, long userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new ResourceNotFoundException(ErrorMessage.AUTH_USER_NOT_FOUND);
        }

        Task task = taskRepository.findById(taskId);
        if (task == null) {
            throw new ResourceNotFoundException(ErrorMessage.TASK_TASK_NOT_FOUND);
        }

        if (task.getUser().getId() != userId) {
            throw new UnauthorizedException(ErrorMessage.TASK_UNAUTHORIZED_ACCESS);
        }
        task.setUser(user);
        return task;
    }

    @Override
    public ApiResponse<TaskResponse> updateTask(long taskId, UpdateTaskRequest request, long userId) {
        try {
            Task task = getTask(taskId, userId);

            Task updatedTask = TaskMapper.toEntity(request);
            updatedTask.setId(taskId);
            updatedTask.setUser(task.getUser());
            updatedTask = taskRepository.save(updatedTask);

            if (updatedTask == null) {
                return ApiResponse.error(ErrorMessage.TASK_UNKNOWN_ERROR_WHILE_UPDATING_TASK);
            }

            return ApiResponse.success(TaskMapper.toTaskResponseDto(updatedTask));

        } catch (ResourceNotFoundException | UnauthorizedException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorMessage.TASK_UNKNOWN_ERROR_WHILE_UPDATING_TASK);
        }
    }

    @Override
    public ApiResponse<Boolean> deleteTask(long taskId, long userId) {
        try {
            getTask(taskId, userId);

            taskRepository.deleteByIdAndUserId(taskId, userId);
            return ApiResponse.success(true);

        } catch (ResourceNotFoundException | UnauthorizedException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorMessage.TASK_UNKNOWN_ERROR_WHILE_DELETING_TASK);
        }
    }

    @Override
    public ApiResponse<List<TaskResponse>> getOverdueTasks(long userId) {
        try {
            User user = userRepository.findById(userId);
            if (user == null) {
                throw new ResourceNotFoundException(ErrorMessage.AUTH_USER_NOT_FOUND);
            }

            List<Task> tasks = taskRepository.findOverdueTasksByUserId(userId);

            if (tasks == null) {
                return ApiResponse.error(ErrorMessage.TASK_UNKNOWN_ERROR_WHILE_FETCHING_TASKS);
            }

            return ApiResponse.success(
                    tasks.stream()
                            .map(TaskMapper::toTaskResponseDto)
                            .toList()
            );

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorMessage.TASK_UNKNOWN_ERROR_WHILE_FETCHING_TASKS);
        }
    }
}
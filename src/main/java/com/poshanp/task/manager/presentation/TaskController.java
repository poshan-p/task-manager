package com.poshanp.task.manager.presentation;

import com.poshanp.task.manager.application.dtos.request.CreateTaskRequest;
import com.poshanp.task.manager.application.dtos.request.UpdateTaskRequest;
import com.poshanp.task.manager.application.dtos.response.ApiResponse;
import com.poshanp.task.manager.application.dtos.response.TaskResponse;
import com.poshanp.task.manager.application.services.interfaces.ITaskService;
import com.poshanp.task.manager.domain.enums.Status;
import com.poshanp.task.manager.presentation.utils.AuthUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/tasks")
@RestController
public class TaskController {
    private final ITaskService taskService;

    @PostMapping("")
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(Authentication authentication, @RequestBody CreateTaskRequest request) {
        long userId = AuthUtility.getUserId(authentication);
        ApiResponse<TaskResponse> response = taskService.createTask(request, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getTasks(Authentication authentication, @RequestParam(required = false) Status status) {
        long userId = AuthUtility.getUserId(authentication);
        ApiResponse<List<TaskResponse>> response = taskService.getUserTasks(userId, status);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/overdue")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getTasks(Authentication authentication) {
        long userId = AuthUtility.getUserId(authentication);
        ApiResponse<List<TaskResponse>> tasks = taskService.getOverdueTasks(userId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<ApiResponse<TaskResponse>> getTask(Authentication authentication, @PathVariable Long taskId) {
        long userId = AuthUtility.getUserId(authentication);
        ApiResponse<TaskResponse> response = taskService.getTaskById(taskId, userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTask(Authentication authentication, @RequestBody UpdateTaskRequest request, @PathVariable Long taskId) {
        long userId = AuthUtility.getUserId(authentication);
        ApiResponse<TaskResponse> response = taskService.updateTask(taskId, request, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<ApiResponse<Boolean>> deleteTask(Authentication authentication, @PathVariable Long taskId) {
        long userId = AuthUtility.getUserId(authentication);
        ApiResponse<Boolean> response = taskService.deleteTask(taskId, userId);
        return ResponseEntity.ok(response);
    }
}

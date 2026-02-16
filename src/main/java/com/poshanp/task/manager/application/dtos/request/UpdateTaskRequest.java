package com.poshanp.task.manager.application.dtos.request;

import com.poshanp.task.manager.domain.enums.Status;
import com.poshanp.task.manager.domain.enums.TaskPriority;

import java.util.Date;

public class UpdateTaskRequest {
    private String title;
    private String description;
    private TaskPriority priority;
    private Status status;
    private Date dueDate;

    public UpdateTaskRequest(String title, String description, TaskPriority priority, Status status, Date dueDate) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.dueDate = dueDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}

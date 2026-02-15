package com.poshanp.task.manager.application.dtos.request;

import com.poshanp.task.manager.domain.enums.Status;
import com.poshanp.task.manager.domain.enums.TaskPriority;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class CreateTaskRequest {
    private String title;
    private String description;
    private TaskPriority priority;
    private Status status;
    private Date dueDate;
}

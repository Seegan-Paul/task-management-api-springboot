package com.myprojects.tms.service;

import com.myprojects.tms.model.Task;
import com.myprojects.tms.model.TaskStatus;

import java.util.List;

public interface TaskService {

    Task createTask(Task task);
    List<Task> getAllTasks();
    Task getTaskById(Long id);
    List<Task> getTaskByStatus(TaskStatus taskStatus);
    Task updateTask(Long id, Task updatedTask);
    void deleteTask(Long id);
}

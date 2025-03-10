package com.myprojects.tms.service;

import com.myprojects.tms.exception.ResourceNotFoundException;
import com.myprojects.tms.model.Task;
import com.myprojects.tms.model.TaskStatus;
import com.myprojects.tms.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService{

    private final TaskRepository taskRepository;

    private static final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * @param task - Request Body to create a task
     */
    @Override
    public Task createTask(Task task) {
        if(task.getStatus() == null){
            task.setStatus(TaskStatus.TODO);
        }
        log.info("Created Task {}", task);
        return taskRepository.save(task);
    }

    @Override
    public List<Task> getAllTasks() {
        log.info("retrieving tasks");
        return taskRepository.findAll();
    }

    /**
     * @param id - id of a particular task
     */
    @Override
    public Task getTaskById(Long id) {
        log.info("retrieving task");
        return taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("Task not found");
                    return new ResourceNotFoundException("Task not found with id: " + id);
                });
    }

    /**
     * @param taskStatus
     * @return
     */
    @Override
    public List<Task> getTaskByStatus(TaskStatus taskStatus) {
        log.info("retrieving tasks");
        List<Task> tasks = taskRepository.findByStatus(taskStatus);
        if(tasks.isEmpty()){
            log.info("No tasks present with status {}", taskStatus);
            throw new ResourceNotFoundException("No tasks present with status " + taskStatus);
        }
        return tasks;
    }

    /**
     * @param id
     * @param updatedTask
     * @return
     */
    @Override
    public Task updateTask(Long id, Task updatedTask) {
        Task fetchedTask = getTaskById(id);
        fetchedTask.setTitle(updatedTask.getTitle());
        fetchedTask.setDescription(updatedTask.getDescription());
        fetchedTask.setStatus(updatedTask.getStatus());
        log.info("updating task");
        return taskRepository.save(fetchedTask);
    }

    /**
     * @param id
     */
    @Override
    public void deleteTask(Long id) {
        Task fetchedTask = getTaskById(id);
        log.info("deleting task: {}", fetchedTask);
        taskRepository.delete(fetchedTask);
    }
}

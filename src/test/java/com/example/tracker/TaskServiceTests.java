package com.example.tracker.service;

import com.example.tracker.model.Task;
import com.example.tracker.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class TaskServiceTests {

    private TaskRepository taskRepository;
    private TaskService taskService;

    @BeforeEach
    public void setUp() {
        taskRepository = mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
    }

    @Test
    public void testAddTask() {
        Task task = new Task(null, "Write tests", false);
        Task saved = new Task(1L, "Write tests", false);

        when(taskRepository.save(task)).thenReturn(saved);

        Task result = taskService.addTask(task);

        assertThat(result.getId()).isEqualTo(1L);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    public void testGetAllTasks() {
        List<Task> tasks = List.of(
            new Task(1L, "One", false),
            new Task(2L, "Two", true)
        );

        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.getAllTasks();

        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void testGetTaskById() {
        Task task = new Task(1L, "Task", false);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Optional<Task> result = taskService.getTask(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getDescription()).isEqualTo("Task");
    }

    @Test
    public void testMarkCompleted() {
        Task task = new Task(1L, "Do this", false);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Optional<Task> updated = taskService.markCompleted(1L);

        assertThat(updated).isPresent();
        assertThat(updated.get().isCompleted()).isTrue();
    }

    @Test
    public void testDeleteTask() {
        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }
}

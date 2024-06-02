package com.pestrotech.task.service;

import com.pestrotech.task.model.Task;
import com.pestrotech.task.repository.FirebaseTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private FirebaseTaskRepository firebaseTaskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        task = new Task(1L, "Test Task", "Test Description", false);
    }

    @Test
    void testGetAllTasks() throws Exception {
        when(firebaseTaskRepository.findAll()).thenReturn(CompletableFuture.completedFuture(Arrays.asList(task)));
        List<Task> tasks = taskService.getAllTasks();
        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals("Test Task", tasks.get(0).getId());
    }

    @Test
    void testGetTaskById() throws Exception {
        when(firebaseTaskRepository.findById(1L)).thenReturn(CompletableFuture.completedFuture(Optional.of(task)));
        Optional<Task> retrievedTask = taskService.getTaskById(1L);
        assertTrue(retrievedTask.isPresent());
        assertEquals("Test Task", retrievedTask.get().getId());
    }

    @Test
    void testCreateTask() throws Exception {
        when(firebaseTaskRepository.saveTask(task)).thenReturn(CompletableFuture.completedFuture(task));
        Task createdTask = taskService.createTask(task);
        assertNotNull(createdTask);
        assertEquals("Test Task", createdTask.getId());
    }

    @Test
    void testUpdateTask() throws Exception {
        when(firebaseTaskRepository.existsById(1L)).thenReturn(CompletableFuture.completedFuture(true));
        when(firebaseTaskRepository.saveTask(task)).thenReturn(CompletableFuture.completedFuture(task));
        Task updatedTask = taskService.updateTask(1L, task);
        assertNotNull(updatedTask);
        assertEquals("Test Task", updatedTask.getId());
    }

    @Test
    void testDeleteTask() throws Exception {
        doNothing().when(firebaseTaskRepository).deleteTask(1L);
        taskService.deleteTask(1L);
        verify(firebaseTaskRepository, times(1)).deleteTask(1L);
    }
}

package com.pestrotech.task.service;

import com.pestrotech.task.model.Task;
import com.pestrotech.task.repository.FirebaseTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class TaskService {

    @Autowired
    private FirebaseTaskRepository firebaseTaskRepository;

    public List<Task> getAllTasks() throws ExecutionException, InterruptedException {
        CompletableFuture<List<Task>> future = firebaseTaskRepository.findAll();
        return future.get();
    }

    public Optional<Task> getTaskById(Long id) throws ExecutionException, InterruptedException {
        CompletableFuture<Optional<Task>> future = firebaseTaskRepository.findById(id);
        return future.get();
    }

    public Task createTask(Task task) throws ExecutionException, InterruptedException {
        CompletableFuture<Task> future = firebaseTaskRepository.saveTask(task);
        return future.get();
    }

    public Task updateTask(Long id, Task task) throws ExecutionException, InterruptedException {
        CompletableFuture<Boolean> existsFuture = firebaseTaskRepository.existsById(id);
        if (existsFuture.get()) {
            task.setId(id);
            CompletableFuture<Task> saveFuture = firebaseTaskRepository.saveTask(task);
            return saveFuture.get();
        }
        return null; // or throw exception
    }

    public void deleteTask(Long id) throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = firebaseTaskRepository.deleteTask(id);
        future.get();
    }
}

package com.pestrotech.task.repository;

import com.google.api.core.ApiFuture;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pestrotech.task.model.Task;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Repository
public class FirebaseTaskRepository {

    private final DatabaseReference databaseReference;

    public FirebaseTaskRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.databaseReference = database.getReference("tasks");
    }

    public CompletableFuture<Task> saveTask(Task task) {
        CompletableFuture<Task> future = new CompletableFuture<>();
        databaseReference.child(task.getId().toString()).setValue(task, (error, ref) -> {
            if (error != null) {
                future.completeExceptionally(error.toException());
            } else {
                future.complete(task);
            }
        });
        return future;
    }

    public CompletableFuture<Void> deleteTask(Long id) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        databaseReference.child(id.toString()).removeValue((error, ref) -> {
            if (error != null) {
                future.completeExceptionally(error.toException());
            } else {
                future.complete(null);
            }
        });
        return future;
    }

    public CompletableFuture<Optional<Task>> findById(Long id) {
        CompletableFuture<Optional<Task>> future = new CompletableFuture<>();
        databaseReference.child(id.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Task task = dataSnapshot.getValue(Task.class);
                future.complete(Optional.ofNullable(task));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });
        return future;
    }

    public CompletableFuture<Boolean> existsById(Long id) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        databaseReference.child(id.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                future.complete(dataSnapshot.exists());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });
        return future;
    }

    public CompletableFuture<List<Task>> findAll() {
        CompletableFuture<List<Task>> future = new CompletableFuture<>();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Task> tasks = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Task task = snapshot.getValue(Task.class);
                    tasks.add(task);
                }
                future.complete(tasks);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });
        return future;
    }
}

package com.javatechie.executor.api.controller;

import com.javatechie.executor.api.entity.User;
import com.javatechie.executor.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class UserController {
    @Autowired
    private UserService service;

    @PostMapping(value = "/users", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    public ResponseEntity saveUsers(@RequestParam(value = "files") MultipartFile[] files) throws Exception {
        for (MultipartFile file : files) {
            service.saveUsers(file);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/users", produces = "application/json")
    public CompletableFuture<ResponseEntity> findAllUsers() {
       return  service.findAllUsers().thenApply(ResponseEntity::ok);
    }


    @GetMapping(value = "/getUsersByThread", produces = "application/json")
    public  ResponseEntity getUsers(){
      /*  CompletableFuture<List<User>> users1=service.findAllUsers();
        CompletableFuture<List<User>> users2=service.findAllUsers();
        CompletableFuture<List<User>> users3=service.findAllUsers();
        CompletableFuture.allOf(users1,users2,users3).join();
        return  ResponseEntity.status(HttpStatus.OK).build();*/
    	// Initiate asynchronous calls to findAllUsers with different delays
        CompletableFuture<List<User>> users1 = service.findAllUsers(10000); // Delay 1 second
        CompletableFuture<List<User>> users2 = service.findAllUsers(); // Delay 2 seconds
        CompletableFuture<List<User>> users3 = service.findAllUsers(3000); // Delay 3 seconds

        // Don't wait for all threads to finish here (prevents observing multithreading)
        // You can process the results individually or use whenAll() for combining results

        // Print a message to indicate the controller thread is still running
        System.out.println("Controller thread: " + Thread.currentThread().getName() + " is still running.");

        // Example using whenAll for combining results (if applicable)
        CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(users1, users2, users3);
        allDoneFuture.thenAccept(v -> {
            System.out.println("All futures are complete!");
            // Process results from users1, users2, and users3 here (if applicable)
        });

        return ResponseEntity.status(HttpStatus.OK).build(); // Send empty response for now
    }
    }


package com.example.multithread.controller;

import com.example.multithread.entity.User;
import com.example.multithread.service.UserService;
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

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/saveUsers", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE} ,produces = "application/json")
    public ResponseEntity saveUsers(@RequestParam(value = "files")MultipartFile[] files) throws Exception {
        for (MultipartFile file: files) {
            //userService.saveUsers(file);
            CompletableFuture.allOf(userService.saveUsers(file), userService.saveUsers(file), userService.saveUsers(file));
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/findUsers", produces = "application/json")
    public CompletableFuture<ResponseEntity> findAllUsers() {
        CompletableFuture<List<User>> users = userService.findAllUsers();
        return users.thenApply(ResponseEntity::ok);
        //return userService.findAllUsers().thenApply(ResponseEntity::ok);
    }

    @GetMapping(value = "/deleteUsers", produces = "application/json")
    public ResponseEntity delete() {
        userService.deleteAllUsers();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

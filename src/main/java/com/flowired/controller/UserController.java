package com.flowired.controller;

import com.flowired.model.User;
import com.flowired.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Integer id, @RequestBody User user) throws Exception {
        return ResponseEntity.ok(userService.update(id, user));
    }

    // Puedes agregar findAll, findById, etc. si deseas.
}

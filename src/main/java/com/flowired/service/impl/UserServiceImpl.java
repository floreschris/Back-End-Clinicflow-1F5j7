// src/main/java/com/flowired/service/impl/UserServiceImpl.java
package com.flowired.service.impl;

import com.flowired.exception.ModelValidationException;
import com.flowired.model.User;
import com.flowired.repo.IGenericRepo;
import com.flowired.repo.IUserRepo;
import com.flowired.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends CRUDImpl<User, Integer> implements IUserService {

    private final IUserRepo userRepo;

    @Override
    protected IGenericRepo<User, Integer> getRepo() {
        return userRepo;
    }

    @Override
    public User findByUsername(String username) throws Exception {
        return userRepo.findOneByUsername(username);
    }

    @Override
    public User update(Integer id, User user) throws Exception {
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            throw new ModelValidationException("Debe asignar al menos un rol al usuario.");
        }

        userRepo.findById(id).orElseThrow(() -> new Exception("ID NOT FOUND: " + id));
        return userRepo.save(user);
    }
}

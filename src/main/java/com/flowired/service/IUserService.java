package com.flowired.service;

import com.flowired.model.User;

public interface IUserService extends ICRUD<User, Integer> {
    User findByUsername(String username) throws Exception;
}

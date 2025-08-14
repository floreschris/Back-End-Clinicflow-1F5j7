package com.flowired.service;

import com.flowired.model.User;

public interface ILoginService {
    User checkUsername(String username);
    void changePassword(String password, String username);
}

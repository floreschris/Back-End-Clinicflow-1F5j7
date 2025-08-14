package com.flowired.service;

import com.flowired.model.ResetMail;

public interface IResetMailService {

    ResetMail findByRandom(String random);

    void save(ResetMail random);

    void delete(ResetMail random);

}

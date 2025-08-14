package com.flowired.service.impl;

import com.flowired.model.Medic;
import com.flowired.repo.IGenericRepo;
import com.flowired.repo.IMedicRepo;
import com.flowired.service.IMedicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MedicServiceImpl extends CRUDImpl<Medic, Integer> implements IMedicService {

    private final IMedicRepo repo;

    @Override
    protected IGenericRepo<Medic, Integer> getRepo() {
        return repo;
    }

}

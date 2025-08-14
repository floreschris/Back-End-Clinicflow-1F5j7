package com.flowired.service.impl;

import com.flowired.model.Specialty;
import com.flowired.repo.IGenericRepo;
import com.flowired.repo.ISpecialtyRepo;
import com.flowired.service.ISpecialtyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpecialtyServiceImpl extends CRUDImpl<Specialty, Integer> implements ISpecialtyService {

    private final ISpecialtyRepo repo;

    @Override
    protected IGenericRepo<Specialty, Integer> getRepo() {
        return repo;
    }

}

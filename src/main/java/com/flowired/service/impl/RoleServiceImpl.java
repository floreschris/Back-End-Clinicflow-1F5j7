package com.flowired.service.impl;

import com.flowired.model.Role;
import com.flowired.repo.IGenericRepo;
import com.flowired.repo.IRoleRepo;
import com.flowired.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends CRUDImpl<Role, Integer> implements IRoleService {

    private final IRoleRepo roleRepo;

    @Override
    protected IGenericRepo<Role, Integer> getRepo() {
        return roleRepo;
    }
}

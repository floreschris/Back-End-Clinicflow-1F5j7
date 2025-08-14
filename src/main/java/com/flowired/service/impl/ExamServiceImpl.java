package com.flowired.service.impl;

import com.flowired.model.Exam;
import com.flowired.repo.IGenericRepo;
import com.flowired.repo.IExamRepo;
import com.flowired.service.IExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl extends CRUDImpl<Exam, Integer> implements IExamService {

    private final IExamRepo repo;

    @Override
    protected IGenericRepo<Exam, Integer> getRepo() {
        return repo;
    }

}

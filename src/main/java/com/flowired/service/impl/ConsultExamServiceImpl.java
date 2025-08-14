package com.flowired.service.impl;

import com.flowired.model.ConsultExam;
import com.flowired.repo.IConsultExamRepo;
import com.flowired.service.IConsultExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultExamServiceImpl implements IConsultExamService {

    private final IConsultExamRepo repo;

    @Override
    public List<ConsultExam> getExamsByConsultId(Integer id) {
        return repo.getExamsByConsultId(id);
    }

}

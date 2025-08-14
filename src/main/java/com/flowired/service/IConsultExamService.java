package com.flowired.service;

import com.flowired.model.ConsultExam;

import java.util.List;

public interface IConsultExamService {

    List<ConsultExam> getExamsByConsultId(Integer id);
}

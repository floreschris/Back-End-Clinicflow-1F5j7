package com.flowired.service;

import com.flowired.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPatientService extends ICRUD<Patient, Integer>{

    Page<Patient> listPage(Pageable pageable);
    //Patient sayHelloAndValid(Patient patient);
    /*Patient save(Patient patient) throws Exception;
    Patient update(Integer id, Patient patient) throws Exception;
    List<Patient> findAll() throws Exception;
    Patient findById(Integer id) throws Exception;
    void delete(Integer id) throws Exception;*/
}

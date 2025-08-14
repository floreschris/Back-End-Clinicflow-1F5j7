package com.flowired.repo;

import com.flowired.model.ConsultExam;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IConsultExamRepo extends IGenericRepo<ConsultExam, Integer> {

    //JPQL Java Persistence Query Language | nativo = SQL
    //@Transactional
    @Modifying
    @Query(value = "INSERT INTO consult_exam(id_consult, id_exam) VALUES(:idConsult, :idExam)", nativeQuery = true)
    Integer saveExam(@Param("idConsult") Integer idConsult, @Param("idExam") Integer idExam);

    @Query("SELECT new com.flowired.model.ConsultExam(ce.exam) FROM ConsultExam ce WHERE ce.consult.idConsult = :idConsult")
    List<ConsultExam> getExamsByConsultId(@Param("idConsult") Integer id);

    /*
    [
        {consult, exam},
        {consult, exam},
        {consult, exam}
    ]
     */

    @Modifying
    @Query("DELETE FROM ConsultExam ce WHERE ce.consult.idConsult = :idConsult")
    void deleteByConsultId(@Param("idConsult") Integer idConsult);



}

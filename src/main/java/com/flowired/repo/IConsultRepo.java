package com.flowired.repo;

import com.flowired.dto.IConsultProcDTO;
import com.flowired.model.Consult;
import com.flowired.model.Exam;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface IConsultRepo extends IGenericRepo<Consult, Integer> {

    //FR: Jaime Medina -> jaime medina
    //BK BD // JAIME MEDINA -> jaime medina
    //JPQL
    @Query("FROM Consult c WHERE c.patient.dni = :dni OR LOWER(c.patient.firstName) LIKE %:fullname% OR LOWER(c.patient.lastName) LIKE %:fullname%")
    List<Consult> search(@Param("dni") String dni, @Param("fullname") String fullname);

    // >= | <
    //05-02-2024 al 13-02-2024 +1
    @Query("FROM Consult c WHERE c.consultDate BETWEEN :date1 AND :date2")
    List<Consult> searchByDates(@Param("date1") LocalDateTime date1, @Param("date2") LocalDateTime date2);

    @Query(value = "select * from fn_list()", nativeQuery = true)
    List<Object[]> callProcedureOrFunctionNative();

    @Query(value = "select * from fn_list()", nativeQuery = true)
    List<IConsultProcDTO> callProcedureOrFunctionProjection();

    @Query(value = "SELECT e.* FROM consult_exam ce INNER JOIN exam e ON ce.id_exam = e.id_exam WHERE ce.id_consult = :idConsult", nativeQuery = true)
    List<Exam> getExamsByConsult(@Param("idConsult") Integer idConsult);


}

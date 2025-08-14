package com.flowired.service;

import com.flowired.dto.ConsultProcDTO;
import com.flowired.dto.IConsultProcDTO;
import com.flowired.model.Consult;
import com.flowired.model.Exam;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;


public interface IConsultService extends ICRUD<Consult, Integer> {

    Consult saveTransactional(Consult consult, List<Exam> exams);
    List<Consult> search(String dni, String fullname);
    List<Consult> searchByDates(LocalDateTime date1, LocalDateTime date2);

    List<ConsultProcDTO> callProcedureOrFunctionNative();
    List<IConsultProcDTO> callProcedureOrFunctionProjection();
    void reschedule(Integer id, LocalDateTime newDate);

    byte[] generateReport() throws Exception;
    byte[] exportToExcel(String date1, String date2) throws IOException;
    byte[] exportToExcelByConsult(Integer idConsult) throws IOException;

}

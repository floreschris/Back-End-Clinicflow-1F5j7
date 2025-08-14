package com.flowired.service.impl;

import com.flowired.dto.ConsultProcDTO;
import com.flowired.dto.IConsultProcDTO;
import com.flowired.model.Consult;
import com.flowired.model.ConsultDetail;
import com.flowired.model.Exam;
import com.flowired.repo.IConsultExamRepo;
import com.flowired.repo.IGenericRepo;
import com.flowired.repo.IConsultRepo;
import com.flowired.service.IConsultService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ConsultServiceImpl extends CRUDImpl<Consult, Integer> implements IConsultService {

    private final IConsultRepo consultRepo;
    private final IConsultExamRepo ceRepo;

    @Override
    protected IGenericRepo<Consult, Integer> getRepo() {
        return consultRepo;
    }

    @Transactional
    @Override
    public Consult saveTransactional(Consult consult, List<Exam> exams) {

        consultRepo.save(consult); //GUARDAR MAESTRO DETALLE
        exams.forEach(ex -> ceRepo.saveExam(consult.getIdConsult(), ex.getIdExam())); //INSERTANDO EN TABLA CONSULT_EXAM

        return consult;
    }

    @Override
    public List<Consult> search(String dni, String fullname) {
        return consultRepo.search(dni, fullname);
    }

    @Override
    public List<Consult> searchByDates(LocalDateTime date1, LocalDateTime date2) {
        final int OFFSET_DAYS = 1;
        return consultRepo.searchByDates(date1, date2.plusDays(OFFSET_DAYS));
    }

    @Override
    public List<ConsultProcDTO> callProcedureOrFunctionNative() {
        List<ConsultProcDTO> list = new ArrayList<>();

        consultRepo.callProcedureOrFunctionNative().forEach(e -> {
            ConsultProcDTO dto = new ConsultProcDTO();
            dto.setQuantity((Integer) e[0]);
            dto.setConsultdate((String) e[1]);
            list.add(dto);
        });

        return list;
    }

    @Override
    public List<IConsultProcDTO> callProcedureOrFunctionProjection() {
        return consultRepo.callProcedureOrFunctionProjection();
    }

    @Override
    public byte[] generateReport() throws Exception {
        byte[] data = null;

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("txt_title", "Report Title");

        File file = new ClassPathResource("/reports/consultas.jasper").getFile();
        JasperPrint print = JasperFillManager.fillReport(file.getPath(), parameters, new JRBeanCollectionDataSource(callProcedureOrFunctionNative()));
        data = JasperExportManager.exportReportToPdf(print);

        return data;
    }

    @Override
    public byte[] exportToExcel(String date1Str, String date2Str) throws IOException {
        List<Consult> consults;

        if (date1Str != null && date2Str != null) {
            LocalDateTime date1 = LocalDateTime.parse(date1Str);
            LocalDateTime date2 = LocalDateTime.parse(date2Str);
            consults = consultRepo.searchByDates(date1, date2.plusDays(1));
        } else {
            consults = consultRepo.findAll();
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Lista de Consultas");
        CreationHelper createHelper = workbook.getCreationHelper();

        int rowNum = 0;

        // === Estilo título principal ===
        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 14);
        titleFont.setColor(IndexedColors.WHITE.getIndex());
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Row titleRow = sheet.createRow(rowNum++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("LISTA DE CONSULTAS");
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

        // === Estilo encabezado ===
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        Row header = sheet.createRow(rowNum++);
        String[] titles = { "Patient", "Medic", "Specialty", "Date" };
        for (int i = 0; i < titles.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(titles[i]);
            cell.setCellStyle(headerStyle);
        }

        // === Estilos para cada columna ===
        CellStyle patientStyle = workbook.createCellStyle();
        patientStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        patientStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        patientStyle.setBorderBottom(BorderStyle.THIN);
        patientStyle.setBorderTop(BorderStyle.THIN);
        patientStyle.setBorderLeft(BorderStyle.THIN);
        patientStyle.setBorderRight(BorderStyle.THIN);

        CellStyle medicStyle = workbook.createCellStyle();
        medicStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        medicStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        medicStyle.setBorderBottom(BorderStyle.THIN);
        medicStyle.setBorderTop(BorderStyle.THIN);
        medicStyle.setBorderLeft(BorderStyle.THIN);
        medicStyle.setBorderRight(BorderStyle.THIN);

        CellStyle specialtyStyle = workbook.createCellStyle();
        specialtyStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        specialtyStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        specialtyStyle.setBorderBottom(BorderStyle.THIN);
        specialtyStyle.setBorderTop(BorderStyle.THIN);
        specialtyStyle.setBorderLeft(BorderStyle.THIN);
        specialtyStyle.setBorderRight(BorderStyle.THIN);

        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));
        dateStyle.setBorderBottom(BorderStyle.THIN);
        dateStyle.setBorderTop(BorderStyle.THIN);
        dateStyle.setBorderLeft(BorderStyle.THIN);
        dateStyle.setBorderRight(BorderStyle.THIN);

        // === Datos ===
        for (Consult c : consults) {
            Row row = sheet.createRow(rowNum++);
            Cell patientCell = row.createCell(0);
            patientCell.setCellValue(c.getPatient().getFirstName() + " " + c.getPatient().getLastName());
            patientCell.setCellStyle(patientStyle);

            Cell medicCell = row.createCell(1);
            medicCell.setCellValue(c.getMedic().getFirstName());
            medicCell.setCellStyle(medicStyle);

            Cell specialtyCell = row.createCell(2);
            specialtyCell.setCellValue(c.getSpecialty().getName());
            specialtyCell.setCellStyle(specialtyStyle);

            Cell dateCell = row.createCell(3);
            dateCell.setCellValue(java.sql.Timestamp.valueOf(c.getConsultDate()));
            dateCell.setCellStyle(dateStyle);
        }

        // Auto-ajustar columnas
        for (int i = 0; i < titles.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // === Fila de resumen ===
        // === Fila de resumen ===
        Row summaryRow = sheet.createRow(rowNum++);
        Cell summaryLabel = summaryRow.createCell(0);
        summaryLabel.setCellValue("Total de consultas:");

        // Crear estilo con negrita, bordes y color de fondo
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);

        CellStyle summaryStyle = workbook.createCellStyle();
        summaryStyle.setFont(boldFont);
        summaryStyle.setBorderBottom(BorderStyle.THIN);
        summaryStyle.setBorderTop(BorderStyle.THIN);
        summaryStyle.setBorderLeft(BorderStyle.THIN);
        summaryStyle.setBorderRight(BorderStyle.THIN);
        summaryStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        summaryStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        summaryStyle.setAlignment(HorizontalAlignment.CENTER); // Centrado horizontal

        summaryLabel.setCellStyle(summaryStyle);

        Cell summaryValue = summaryRow.createCell(1);
        summaryValue.setCellValue(consults.size());
        summaryValue.setCellStyle(summaryStyle);

        // Autoajustar columnas si es necesario
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }


    @Override
    public byte[] exportToExcelByConsult(Integer idConsult) throws IOException {
        Consult consult = consultRepo.findById(idConsult)
                .orElseThrow(() -> new IllegalArgumentException("Consulta no encontrada"));

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Consulta");
        CreationHelper createHelper = workbook.getCreationHelper();

        int rowNum = 0;

        // === Estilo principal azul ===
        CellStyle mainTitleStyle = workbook.createCellStyle();
        Font mainFont = workbook.createFont();
        mainFont.setBold(true);
        mainFont.setColor(IndexedColors.WHITE.getIndex());
        mainTitleStyle.setFont(mainFont);
        mainTitleStyle.setAlignment(HorizontalAlignment.CENTER);
        mainTitleStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        mainTitleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Row mainTitleRow = sheet.createRow(rowNum++);
        Cell titleCell = mainTitleRow.createCell(0);
        String title = "CONSULTA NÚMERO: " + consult.getIdConsult();
        titleCell.setCellValue(title);
        titleCell.setCellStyle(mainTitleStyle);

        // Combinar celdas de A1 hasta E1 (índices 0 a 4)
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

        // === Estilo encabezados ===
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        // === Estilo cuerpo ===
        CellStyle bodyStyle = workbook.createCellStyle();
        bodyStyle.setBorderBottom(BorderStyle.THIN);
        bodyStyle.setBorderTop(BorderStyle.THIN);
        bodyStyle.setBorderLeft(BorderStyle.THIN);
        bodyStyle.setBorderRight(BorderStyle.THIN);

        // === Estilo fecha ===
        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.cloneStyleFrom(bodyStyle);
        dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy HH:mm:ss"));

        // === Estilo rojo para N° consulta ===
        CellStyle redStyle = workbook.createCellStyle();
        redStyle.cloneStyleFrom(bodyStyle);
        Font redFont = workbook.createFont();
        redFont.setColor(IndexedColors.RED.getIndex());
        redStyle.setFont(redFont);

        // === Encabezado tabla ===
        Row header = sheet.createRow(rowNum++);
        String[] titles = { "Paciente", "Médico", "Especialidad", "Fecha", "N° Consulta" };
        for (int i = 0; i < titles.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(titles[i]);
            cell.setCellStyle(headerStyle);
        }

        // === Datos ===
        Row data = sheet.createRow(rowNum++);
        data.createCell(0).setCellValue(consult.getPatient().getFirstName() + " " + consult.getPatient().getLastName());
        data.getCell(0).setCellStyle(bodyStyle);

        data.createCell(1).setCellValue(consult.getMedic().getFirstName() + " " + consult.getMedic().getLastName());
        data.getCell(1).setCellStyle(bodyStyle);

        data.createCell(2).setCellValue(consult.getSpecialty().getName());
        data.getCell(2).setCellStyle(bodyStyle);

        Cell dateCell = data.createCell(3);
        dateCell.setCellValue(java.sql.Timestamp.valueOf(consult.getConsultDate()));
        dateCell.setCellStyle(dateStyle);

        Cell redCell = data.createCell(4);
        redCell.setCellValue(consult.getNumConsult());
        redCell.setCellStyle(redStyle);

        for (int i = 0; i < titles.length; i++) {
            sheet.autoSizeColumn(i);
        }
        // === Sección de detalles ===
        rowNum++;
        Row detailTitleRow = sheet.createRow(rowNum++);
        Cell detailTitleCell = detailTitleRow.createCell(0);
        detailTitleCell.setCellValue("DETALLES DE CONSULTA (Diagnóstico y Tratamiento)");
        detailTitleCell.setCellStyle(mainTitleStyle);
        sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 1));

        Row detailHeader = sheet.createRow(rowNum++);
        String[] detailTitles = { "Diagnóstico", "Tratamiento" };
        for (int i = 0; i < detailTitles.length; i++) {
            Cell cell = detailHeader.createCell(i);
            cell.setCellValue(detailTitles[i]);
            cell.setCellStyle(headerStyle);
        }

        for (ConsultDetail detail : consult.getDetails()) {
            Row detailRow = sheet.createRow(rowNum++);
            Cell diagnosisCell = detailRow.createCell(0);
            diagnosisCell.setCellValue(detail.getDiagnosis());
            diagnosisCell.setCellStyle(bodyStyle);

            Cell treatmentCell = detailRow.createCell(1);
            treatmentCell.setCellValue(detail.getTreatment());
            treatmentCell.setCellStyle(bodyStyle);
        }
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();

        return bos.toByteArray();
    }

    @Override
    public void reschedule(Integer id, LocalDateTime newDate) {
        Consult consult = consultRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta no encontrada"));
        consult.setConsultDate(newDate);
        consultRepo.save(consult);
    }

    @Transactional
    @Override
    public void delete(Integer id) throws Exception {
        ceRepo.deleteByConsultId(id);
        consultRepo.deleteById(id);
    }



}

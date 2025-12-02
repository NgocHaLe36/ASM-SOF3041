package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelWriter {

    private Workbook workbook;
    private Sheet sheet;
    private int currentRow = 1;

    public ExcelWriter() {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("TestCases");
        createHeader();
    }

    // ===========================
    // 1) HEADER
    // ===========================
    private void createHeader() {
        Row header = sheet.createRow(0);
        String[] cols = {
            "TEST CASE ID", "TEST SCENARIO", "TEST CASE", "PRE-CONDITION",
            "TEST STEPS", "TEST DATA", "EXPECTED RESULT", "POST CONDITION",
            "ACTUAL RESULT", "STATUS (PASS/FAIL)"
        };

        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        for (int i = 0; i < cols.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(cols[i]);
            cell.setCellStyle(style);
            sheet.autoSizeColumn(i);
        }
    }

    // ===========================
    // 2) GHI 1 DÒNG DỮ LIỆU
    // ===========================
    public void writeRow(
            String testCaseID,
            String testScenario,
            String testCase,
            String preCondition,
            String testSteps,
            String testData,
            String expectedResult,
            String postCondition,
            String actualResult,
            String status
    ) {
        Row row = sheet.createRow(currentRow++);
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        String[] values = {
            testCaseID, testScenario, testCase, preCondition,
            testSteps, testData, expectedResult, postCondition,
            actualResult, status
        };

        for (int i = 0; i < values.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(values[i]);
            cell.setCellStyle(style);
            sheet.autoSizeColumn(i);
        }
    }

    // ===========================
    // 3) GHI FILE
    // ===========================
    public void save(String filePath) {
        try (FileOutputStream out = new FileOutputStream(filePath)) {
            workbook.write(out);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

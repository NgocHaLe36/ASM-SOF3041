package testcase;

import utils.ExcelWriter;

/**
 * ExcelHelper – viết 1 dòng kết quả test vào Excel.
 * 10 cột: 
 * TEST CASE ID, TEST SCENARIO, TEST CASE, PRE-CONDITION, TEST STEPS, TEST DATA, EXPECTED RESULT, POST CONDITION, ACTUAL RESULT, STATUS (PASS/FAIL)
 */
public class ExcelHelper {

    private static ExcelWriter excel;

    /** Khởi tạo ExcelWriter trước khi ghi dữ liệu */
    public static void init(ExcelWriter e) {
        if (e == null) {
            throw new IllegalArgumentException("ExcelWriter cannot be null!");
        }
        excel = e;
    }

    /**
     * Ghi 1 dòng dữ liệu đầy đủ 10 cột vào Excel
     */
    public static void write(
            String testCaseId,
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
        if (excel == null) {
            throw new IllegalStateException(
                    "ExcelWriter not initialized! Call ExcelHelper.init(excel) first."
            );
        }
        excel.writeRow(
                testCaseId,
                testScenario,
                testCase,
                preCondition,
                testSteps,
                testData,
                expectedResult,
                postCondition,
                actualResult,
                status
        );
    }


}

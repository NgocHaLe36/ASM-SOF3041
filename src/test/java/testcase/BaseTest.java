package testcase;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import utils.DriverFactory;
import utils.ExcelWriter;

import java.io.File;

public class BaseTest {

    protected ExcelWriter excel;
    protected String excelPath;

    // =======================
    // BEFORE SUITE
    // =======================
    @BeforeSuite
    @Parameters({"testType"})
    public void beforeSuite(@Optional("Login") String testType) {
        try {
            // tạo folder results nếu chưa có
            File dir = new File("results");
            if (!dir.exists()) dir.mkdirs();

            // tạo file riêng cho từng loại test
            excelPath = "results/TestResults" + testType + ".xlsx";
            excel = new ExcelWriter();

            System.out.println("✔ TestSuite STARTED -> Excel initialized: " + excelPath);
        } catch (Exception e) {
            System.out.println("❌ ERROR tạo Excel: " + e.getMessage());
        }
    }

    // =======================
    // AFTER SUITE
    // =======================
    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        try {
            excel.save(excelPath);
            System.out.println("✔ Excel SAVED → " + excelPath);
        } catch (Exception e) {
            System.out.println("❌ ERROR lưu Excel: " + e.getMessage());
        }

        try {
            DriverFactory.quitDriver();
            System.out.println("✔ Browser CLOSED");
        } catch (Exception ignored) {}
    }

    // =======================
    // PAUSE 3 GIÂY
    // =======================
    protected void pause3s() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ignored) {}
    }

    // =======================
    // LOGIN HÀM DÙNG CHUNG
    // =======================
    protected void loginAs(String id, String password) throws Exception {
        WebDriver driver = DriverFactory.getDriver(false);

        driver.get("http://localhost:8080/PolyLab7/login");
        Thread.sleep(800);

        try { driver.findElement(org.openqa.selenium.By.name("id")).clear(); } catch (Exception ignored) {}
        try { driver.findElement(org.openqa.selenium.By.name("password")).clear(); } catch (Exception ignored) {}

        driver.findElement(org.openqa.selenium.By.name("id")).sendKeys(id);
        driver.findElement(org.openqa.selenium.By.name("password")).sendKeys(password);

        driver.findElement(org.openqa.selenium.By.className("btn-login")).click();
        Thread.sleep(1200);
    }
}

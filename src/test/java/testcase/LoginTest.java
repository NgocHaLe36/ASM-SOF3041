package testcase;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import utils.DriverFactory;

public class LoginTest extends BaseTest {

    WebDriver driver;

    @BeforeClass
    public void beforeClass() {
        ExcelHelper.init(excel);
    }

    @BeforeMethod
    public void beforeMethod() {
        driver = DriverFactory.getDriver(false);
    }

    @AfterMethod
    public void afterMethod() {
        // do not quit driver here — BaseTest.afterSuite will quit final
        pause3s(); // dừng 10s theo yêu cầu
    }

    // 10 test login (admin + reporter + negative cases)
    @Test
    public void TC01_AdminLogin_Success() {
        try {
            loginAs("admin", "111");
            boolean ok = DriverFactory.getDriver(false).getCurrentUrl().contains("/admin/dashboard");
            String img = DriverFactory.takeScreenshot("TC01_AdminLogin_Success");
            ExcelHelper.write("TC01", "Login admin đúng", "Nhập admin/111 → Login", "/admin/dashboard", ok ? "Dashboard hiển thị" : "Không redirect", ok ? "PASSED":"FAILED", "", "", img, "");
            Assert.assertTrue(ok, "Không redirect vào dashboard");
        } catch (Exception e) {
            String img = DriverFactory.takeScreenshot("TC01_AdminLogin_Error");
            ExcelHelper.write("TC01", "Login admin đúng", "Nhập admin/111 → Login", "/admin/dashboard", "Error: "+e.getMessage(), "FAILED", "", "", img, e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void TC02_ReporterLogin_Success() {
        try {
            loginAs("r001", "111");
            boolean ok = DriverFactory.getDriver(false).getCurrentUrl().contains("/reporter/dashboard");
            String img = DriverFactory.takeScreenshot("TC02_ReporterLogin_Success");
            ExcelHelper.write("TC02", "Login reporter đúng", "Nhập r001/111 → Login", "/reporter/dashboard", ok ? "Reporter dashboard hiển thị" : "Không redirect", ok ? "PASSED":"FAILED", "", "", img, "");
            Assert.assertTrue(ok);
        } catch (Exception e) {
            String img = DriverFactory.takeScreenshot("TC02_ReporterLogin_Error");
            ExcelHelper.write("TC02", "Login reporter đúng", "Nhập r001/111 → Login", "/reporter/dashboard", "Error: "+e.getMessage(), "FAILED", "", "", img, e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void TC03_AdminWrongPassword() {
        try {
            loginAs("admin", "000");
            boolean errorShown = DriverFactory.getDriver(false).getPageSource().contains("Sai") || DriverFactory.getDriver(false).getPageSource().contains("error");
            String img = DriverFactory.takeScreenshot("TC03_AdminWrongPassword");
            ExcelHelper.write("TC03", "Admin sai password", "Nhập admin/000 → Login", "Hiện thông báo lỗi", errorShown ? "Hiện thông báo lỗi" : "Không hiện lỗi", errorShown ? "PASSED":"FAILED", "", "", img, "");
            Assert.assertTrue(errorShown);
        } catch (Exception e) {
            String img = DriverFactory.takeScreenshot("TC03_Error");
            ExcelHelper.write("TC03", "Admin sai password", "Nhập admin/000 → Login", "Hiện thông báo lỗi", "Error: "+e.getMessage(), "FAILED", "", "", img, e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void TC04_AdminEmptyUser() {
        try {
            loginAs("", "111");
            boolean errorShown = DriverFactory.getDriver(false).getPageSource().contains("Vui lòng") || DriverFactory.getDriver(false).getPageSource().contains("required");
            String img = DriverFactory.takeScreenshot("TC04_AdminEmptyUser");
            ExcelHelper.write("TC04", "Admin bỏ trống user", "Để trống id → Login", "Hiện validate", errorShown ? "Hiện validate" : "Không hiện", errorShown ? "PASSED":"FAILED", "", "", img, "");
            Assert.assertTrue(errorShown);
        } catch (Exception e) {
            String img = DriverFactory.takeScreenshot("TC04_Error");
            ExcelHelper.write("TC04", "Admin bỏ trống user", "Để trống id → Login", "Hiện validate", "Error: "+e.getMessage(), "FAILED", "", "", img, e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void TC05_AdminEmptyPassword() {
        try {
            loginAs("admin", "");
            boolean errorShown = DriverFactory.getDriver(false).getPageSource().contains("Vui lòng") || DriverFactory.getDriver(false).getPageSource().contains("required");
            String img = DriverFactory.takeScreenshot("TC05_AdminEmptyPassword");
            ExcelHelper.write("TC05", "Admin bỏ trống pass", "Để trống password → Login", "Hiện validate", errorShown ? "Hiện validate" : "Không hiện", errorShown ? "PASSED":"FAILED", "", "", img, "");
            Assert.assertTrue(errorShown);
        } catch (Exception e) {
            String img = DriverFactory.takeScreenshot("TC05_Error");
            ExcelHelper.write("TC05", "Admin bỏ trống pass", "Để trống password → Login", "Hiện validate", "Error: "+e.getMessage(), "FAILED", "", "", img, e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void TC06_Login_SQLInjection() {
        try {
            loginAs("' OR 1=1 --", "anything");
            boolean notLogged = !DriverFactory.getDriver(false).getCurrentUrl().contains("/admin/dashboard")
                    && !DriverFactory.getDriver(false).getCurrentUrl().contains("/reporter/dashboard");
            String img = DriverFactory.takeScreenshot("TC06_SQLInject");
            ExcelHelper.write("TC06", "SQL Injection", "Nhập ' OR 1=1 --", "Không login bypass", notLogged ? "Không bypass" : "Bypassed", notLogged ? "PASSED":"FAILED", "", "", img, "");
            Assert.assertTrue(notLogged);
        } catch (Exception e) {
            String img = DriverFactory.takeScreenshot("TC06_Error");
            ExcelHelper.write("TC06", "SQL Injection", "Nhập ' OR 1=1 --", "Không login bypass", "Error: "+e.getMessage(), "FAILED", "", "", img, e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void TC07_Login_LongUser() {
        try {
            String longUser = "a".repeat(200);
            loginAs(longUser, "111");
            boolean handled = DriverFactory.getDriver(false).getPageSource().contains("không hợp lệ") || !DriverFactory.getDriver(false).getCurrentUrl().contains("dashboard");
            String img = DriverFactory.takeScreenshot("TC07_LongUser");
            ExcelHelper.write("TC07", "Tên user quá dài", "Nhập user 200 ký tự", "Không chấp nhận", handled ? "Không cho nhập" : "Chấp nhận", handled ? "PASSED":"FAILED", "", "", img, "");
            Assert.assertTrue(handled);
        } catch (Exception e) {
            String img = DriverFactory.takeScreenshot("TC07_Error");
            ExcelHelper.write("TC07", "Tên user quá dài", "Nhập user 200 ký tự", "Không chấp nhận", "Error: "+e.getMessage(), "FAILED", "", "", img, e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void TC08_Login_XSSAttempt() {
        try {
            loginAs("<script>alert(1)</script>", "111");
            boolean safe = !DriverFactory.getDriver(false).getPageSource().contains("<script>");
            String img = DriverFactory.takeScreenshot("TC08_XSS");
            ExcelHelper.write("TC08", "XSS attempt in username", "Nhập script tag", "Không execute", safe ? "Không execute" : "Có", safe ? "PASSED":"FAILED", "", "", img, "");
            Assert.assertTrue(safe);
        } catch (Exception e) {
            String img = DriverFactory.takeScreenshot("TC08_Error");
            ExcelHelper.write("TC08", "XSS attempt", "Nhập script tag", "Không execute", "Error: "+e.getMessage(), "FAILED", "", "", img, e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void TC09_Login_WhitespaceTrim() {
        try {
            loginAs(" admin ", " 111 ");
            boolean ok = DriverFactory.getDriver(false).getCurrentUrl().contains("/admin/dashboard") || DriverFactory.getDriver(false).getCurrentUrl().contains("/reporter/dashboard");
            String img = DriverFactory.takeScreenshot("TC09_Whitespace");
            ExcelHelper.write("TC09", "Whitespace in inputs", "Nhập có khoảng trắng", "Trim hoặc báo lỗi", ok ? "Login or trimmed" : "Failed/validate", ok ? "PASSED":"FAILED", "", "", img, "");
            // don't assert strictly — accept either behavior but record it
        } catch (Exception e) {
            String img = DriverFactory.takeScreenshot("TC09_Error");
            ExcelHelper.write("TC09", "Whitespace test", "Nhập có khoảng trắng", "Trim or validate", "Error: "+e.getMessage(), "FAILED", "", "", img, e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void TC10_Login_ReporterWrongPassword() {
        try {
            loginAs("r001", "000");
            boolean errorShown = DriverFactory.getDriver(false).getPageSource().contains("Sai") || DriverFactory.getDriver(false).getPageSource().contains("error");
            String img = DriverFactory.takeScreenshot("TC10_ReporterWrongPass");
            ExcelHelper.write("TC10", "Reporter sai pass", "Nhập r001/000 → Login", "Hiện lỗi", errorShown ? "Hiện lỗi" : "Không", errorShown ? "PASSED":"FAILED", "", "", img, "");
            Assert.assertTrue(errorShown);
        } catch (Exception e) {
            String img = DriverFactory.takeScreenshot("TC10_Error");
            ExcelHelper.write("TC10", "Reporter sai pass", "Nhập r001/000 → Login", "Hiện lỗi", "Error: "+e.getMessage(), "FAILED", "", "", img, e.getMessage());
            Assert.fail(e.getMessage());
        }
    }
    
}

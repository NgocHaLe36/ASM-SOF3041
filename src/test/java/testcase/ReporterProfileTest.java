package testcase;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import utils.DriverFactory;

/**
 * ReporterProfileTest - 10 test chức năng hồ sơ phóng viên
 */
public class ReporterProfileTest extends BaseTest {

    private WebDriver driver;

    @BeforeClass
    public void beforeClass() {
        ExcelHelper.init(excel); // Khởi tạo ExcelHelper
    }

    @BeforeMethod
    public void beforeMethod() throws Exception {
        driver = DriverFactory.getDriver(false);
        loginAs("r001", "111");
        Thread.sleep(700);
    }

    @AfterMethod
    public void afterMethod() {
        pause3s();
    }

    @AfterClass
    public void afterClass() {
        if (driver != null) driver.quit();
    }

    private boolean isElementPresent(By by) {
        try { driver.findElement(by); return true; }
        catch (Exception e) { return false; }
    }

    @Test
    public void TC21_OpenProfile() {
        try {
            driver.get("http://localhost:8080/PolyLab7/reporter/profile");
            boolean ok = driver.getPageSource().contains("Hồ sơ cá nhân");
            String img = DriverFactory.takeScreenshot("TC21_OpenProfile");
            ExcelHelper.write("TC21","Open profile","Truy cập /reporter/profile","Login reporter r001",
                    "Mở trang hồ sơ","", "Hiển thị hồ sơ", "", ok ? "Hiển thị" : "Không", ok ? "PASS" : "FAIL");
            Assert.assertTrue(ok);
        } catch(Exception e) {
            String img = DriverFactory.takeScreenshot("TC21_Error");
            ExcelHelper.write("TC21","Open profile - Error","Truy cập /reporter/profile","Login reporter r001",
                    "Mở trang hồ sơ","", "Error: "+e.getMessage(), "", img, "FAIL");
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void TC22_UpdateFullname() {
        try {
            driver.get("http://localhost:8080/PolyLab7/reporter/profile");
            if(isElementPresent(By.name("fullname"))) {
                driver.findElement(By.name("fullname")).clear();
                driver.findElement(By.name("fullname")).sendKeys("Nguyễn Văn A");
            } else if(isElementPresent(By.name("username"))) {
                driver.findElement(By.name("username")).clear();
                driver.findElement(By.name("username")).sendKeys("Nguyễn Văn A");
            }
            try { driver.findElement(By.id("btnSave")).click(); } catch(Exception ex) { driver.findElement(By.cssSelector("button[type='submit']")).click(); }
            Thread.sleep(700);
            boolean ok = driver.getPageSource().contains("Cập nhật thành công");
            String img = DriverFactory.takeScreenshot("TC22_UpdateFullname");
            ExcelHelper.write("TC22","Update fullname","Thay fullname -> Nguyễn Văn A","Login reporter r001",
                    "Click Save", "", ok ? "Cập nhật thành công" : "Không", "", img, ok ? "PASS" : "FAIL");
            Assert.assertTrue(ok);
        } catch(Exception e) {
            String img = DriverFactory.takeScreenshot("TC22_Error");
            ExcelHelper.write("TC22","Update fullname - Error","Submit update","Login reporter r001",
                    "Click Save","", "Error: "+e.getMessage(), "", img, "FAIL");
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void TC23_UpdateEmailValid() {
        try {
            driver.get("http://localhost:8080/PolyLab7/reporter/profile");
            driver.findElement(By.name("email")).clear();
            driver.findElement(By.name("email")).sendKeys("test.reporter@example.com");
            try { driver.findElement(By.id("btnSave")).click(); } catch(Exception ex) { driver.findElement(By.cssSelector("button[type='submit']")).click(); }
            Thread.sleep(700);
            boolean ok = driver.getPageSource().contains("Cập nhật thành công");
            String img = DriverFactory.takeScreenshot("TC23_UpdateEmail");
            ExcelHelper.write("TC23","Update email","Nhập email hợp lệ","Login reporter r001",
                    "Click Save","test.reporter@example.com", ok ? "Cập nhật thành công":"Không", "", img, ok ? "PASS":"FAIL");
            Assert.assertTrue(ok);
        } catch(Exception e) {
            String img = DriverFactory.takeScreenshot("TC23_Error");
            ExcelHelper.write("TC23","Update email - Error","Submit email valid","Login reporter r001",
                    "Click Save","", "Error: "+e.getMessage(), "", img, "FAIL");
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void TC24_UpdateEmailInvalid() {
        try {
            driver.get("http://localhost:8080/PolyLab7/reporter/profile");
            driver.findElement(By.name("email")).clear();
            driver.findElement(By.name("email")).sendKeys("abc123");
            try { driver.findElement(By.id("btnSave")).click(); } catch(Exception ex) { driver.findElement(By.cssSelector("button[type='submit']")).click(); }
            Thread.sleep(700);
            boolean ok = driver.getPageSource().contains("Email không hợp lệ");
            String img = DriverFactory.takeScreenshot("TC24_InvalidEmail");
            ExcelHelper.write("TC24","Update email invalid","Nhập abc123","Login reporter r001",
                    "Click Save","abc123", ok ? "Hiện validate":"Không", "", img, ok ? "PASS":"FAIL");
            Assert.assertTrue(ok);
        } catch(Exception e) {
            String img = DriverFactory.takeScreenshot("TC24_Error");
            ExcelHelper.write("TC24","Update email invalid - Error","Submit email invalid","Login reporter r001",
                    "Click Save","", "Error: "+e.getMessage(), "", img, "FAIL");
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void TC25_EmptyFullname() {
        try {
            driver.get("http://localhost:8080/PolyLab7/reporter/profile");
            if(isElementPresent(By.name("fullname"))) driver.findElement(By.name("fullname")).clear();
            else driver.findElement(By.name("username")).clear();
            try { driver.findElement(By.id("btnSave")).click(); } catch(Exception ex) { driver.findElement(By.cssSelector("button[type='submit']")).click(); }
            Thread.sleep(700);
            boolean ok = driver.getPageSource().contains("không được trống");
            String img = DriverFactory.takeScreenshot("TC25_EmptyFullname");
            ExcelHelper.write("TC25","Empty fullname","Clear field → Save","Login reporter r001",
                    "Click Save","", ok ? "Hiện validate":"Không", "", img, ok ? "PASS":"FAIL");
            Assert.assertTrue(ok);
        } catch(Exception e) {
            String img = DriverFactory.takeScreenshot("TC25_Error");
            ExcelHelper.write("TC25","Empty fullname - Error","Clear then Save","Login reporter r001",
                    "Click Save","", "Error: "+e.getMessage(), "", img, "FAIL");
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void TC26_ChangePassword_Success() {
        try {
            driver.get("http://localhost:8080/PolyLab7/reporter/profile");
            driver.findElement(By.name("password")).clear();
            driver.findElement(By.name("password")).sendKeys("111");
            driver.findElement(By.name("newPassword")).sendKeys("222");
            driver.findElement(By.name("confirmPassword")).sendKeys("222");
            try { driver.findElement(By.id("btnChange")).click(); } catch(Exception ex) { driver.findElement(By.cssSelector("button[type='submit']")).click(); }
            Thread.sleep(700);
            boolean ok = driver.getPageSource().contains("Đổi mật khẩu thành công");
            String img = DriverFactory.takeScreenshot("TC26_ChangePass");
            ExcelHelper.write("TC26","Change password success","old=111,new=222","Login reporter r001",
                    "Click Save","", ok ? "OK":"Không", "", img, ok ? "PASS":"FAIL");
            Assert.assertTrue(ok);
        } catch(Exception e) {
            String img = DriverFactory.takeScreenshot("TC26_Error");
            ExcelHelper.write("TC26","Change password - Error","Submit change password","Login reporter r001",
                    "Click Save","", "Error: "+e.getMessage(), "", img, "FAIL");
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void TC27_ChangePassword_WrongOld() {
        try {
            driver.get("http://localhost:8080/PolyLab7/reporter/profile");
            driver.findElement(By.name("password")).clear();
            driver.findElement(By.name("password")).sendKeys("000");
            driver.findElement(By.name("newPassword")).sendKeys("222");
            driver.findElement(By.name("confirmPassword")).sendKeys("222");
            try { driver.findElement(By.id("btnChange")).click(); } catch(Exception ex) { driver.findElement(By.cssSelector("button[type='submit']")).click(); }
            Thread.sleep(700);
            boolean ok = driver.getPageSource().contains("Mật khẩu cũ sai");
            String img = DriverFactory.takeScreenshot("TC27_WrongOld");
            ExcelHelper.write("TC27","Wrong old password","old=000,new=222","Login reporter r001",
                    "Click Save","", ok ? "Hiện validate":"Không", "", img, ok ? "PASS":"FAIL");
            Assert.assertTrue(ok);
        } catch(Exception e) {
            String img = DriverFactory.takeScreenshot("TC27_Error");
            ExcelHelper.write("TC27","Wrong old password - Error","Submit wrong old password","Login reporter r001",
                    "Click Save","", "Error: "+e.getMessage(), "", img, "FAIL");
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void TC28_TooShortPassword() {
        try {
            driver.get("http://localhost:8080/PolyLab7/reporter/profile");
            driver.findElement(By.name("newPassword")).sendKeys("12");
            try { driver.findElement(By.id("btnChange")).click(); } catch(Exception ex) { driver.findElement(By.cssSelector("button[type='submit']")).click(); }
            Thread.sleep(700);
            boolean ok = driver.getPageSource().contains("Mật khẩu quá ngắn");
            String img = DriverFactory.takeScreenshot("TC28_TooShort");
            ExcelHelper.write("TC28","Short password","new=12","Login reporter r001",
                    "Click Save","", ok ? "Hiện validate":"Không", "", img, ok ? "PASS":"FAIL");
            Assert.assertTrue(ok);
        } catch(Exception e) {
            String img = DriverFactory.takeScreenshot("TC28_Error");
            ExcelHelper.write("TC28","Short password - Error","Submit too short password","Login reporter r001",
                    "Click Save","", "Error: "+e.getMessage(), "", img, "FAIL");
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void TC29_UploadAvatar() {
        try {
            driver.get("http://localhost:8080/PolyLab7/reporter/profile");
            driver.findElement(By.name("avatar")).sendKeys("C:\\fakepath\\avatar.png");
            try { driver.findElement(By.id("btnUpload")).click(); } catch(Exception ex) { driver.findElement(By.cssSelector("button[type='submit']")).click(); }
            Thread.sleep(800);
            boolean ok = driver.getPageSource().contains("Tải ảnh thành công");
            String img = DriverFactory.takeScreenshot("TC29_UploadAvatar");
            ExcelHelper.write("TC29","Upload avatar","Upload file avatar.png","Login reporter r001",
                    "Click Upload","", ok ? "OK":"Không", "", img, ok ? "PASS":"FAIL");
            Assert.assertTrue(ok);
        } catch(Exception e) {
            String img = DriverFactory.takeScreenshot("TC29_Error");
            ExcelHelper.write("TC29","Upload avatar - Error","Upload file","Login reporter r001",
                    "Click Upload","", "Error: "+e.getMessage(), "", img, "FAIL");
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void TC30_Profile_SQLInject() {
        try {
            driver.get("http://localhost:8080/PolyLab7/reporter/profile");
            if(isElementPresent(By.name("username"))) {
                driver.findElement(By.name("username")).clear();
                driver.findElement(By.name("username")).sendKeys("' OR ''='");
            } else if(isElementPresent(By.name("fullname"))) {
                driver.findElement(By.name("fullname")).clear();
                driver.findElement(By.name("fullname")).sendKeys("' OR ''='");
            }
            try { driver.findElement(By.id("btnSave")).click(); } catch(Exception ex) { driver.findElement(By.cssSelector("button[type='submit']")).click(); }
            Thread.sleep(700);
            boolean safe = !driver.getPageSource().toLowerCase().contains("sql") && !driver.getPageSource().toLowerCase().contains("exception");
            String img = DriverFactory.takeScreenshot("TC30_SQLInject");
            ExcelHelper.write("TC30","Profile SQL injection","Payload SQLi","Login reporter r001",
                    "Click Save","", safe ? "Không lỗi":"Có lỗi", "", img, safe ? "PASS":"FAIL");
            Assert.assertTrue(safe);
        } catch(Exception e) {
            String img = DriverFactory.takeScreenshot("TC30_Error");
            ExcelHelper.write("TC30","Profile SQLInject - Error","Payload SQLi","Login reporter r001",
                    "Click Save","", "Error: "+e.getMessage(), "", img, "FAIL");
            Assert.fail(e.getMessage());
        }
    }
}

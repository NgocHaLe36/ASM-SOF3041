package testcase;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import utils.DriverFactory;

public class AdminCategoryTest extends BaseTest {

    WebDriver driver;

    @BeforeClass
    public void beforeClass() {
        ExcelHelper.init(excel); // Khởi tạo ExcelHelper
    }

    @BeforeMethod
    public void beforeMethod() throws Exception {
        driver = DriverFactory.getDriver(false);
        driver.get("http://localhost:8080/PolyLab7/login");
        loginAs("admin", "111");
        Thread.sleep(500);
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
        try {
            driver.findElement(by);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Test
    public void TC11_OpenCategoryPage() {
        try {
            driver.get("http://localhost:8080/PolyLab7/admin/category");
            boolean ok = driver.getPageSource().contains("Quản lý danh mục");
            String img = DriverFactory.takeScreenshot("TC11_OpenCategoryPage");

            ExcelHelper.write("TC11", "Mở trang quản lý danh mục",
                    "Truy cập /admin/category",
                    "Login admin",
                    "Mở trang category",
                    "",
                    ok ? "Hiển thị danh sách category" : "Không hiển thị",
                    "", img,
                    ok ? "PASS" : "FAIL");

            Assert.assertTrue(ok);
        } catch (Exception e) {
            String img = DriverFactory.takeScreenshot("TC11_Error");
            ExcelHelper.write("TC11", "Open category - Error",
                    "Truy cập /admin/category",
                    "Login admin",
                    "Mở trang category",
                    "",
                    "Error: " + e.getMessage(),
                    "", img,
                    "FAIL");
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void TC12_AddCategory_Success() {
        try {
            driver.get("http://localhost:8080/PolyLab7/admin/category");
            try { driver.findElement(By.cssSelector(".btn-add")).click(); } catch (Exception ignored) {}
            Thread.sleep(500);

            String catName = "Thể thao " + System.currentTimeMillis() % 10000;
            driver.findElement(By.name("name")).clear();
            driver.findElement(By.name("name")).sendKeys(catName);
            try { driver.findElement(By.id("btnSave")).click(); } catch (Exception ex) { driver.findElement(By.cssSelector("button[type='submit']")).click(); }
            Thread.sleep(700);

            boolean ok = driver.getPageSource().contains("Thêm thành công") || driver.getPageSource().contains(catName);
            String img = DriverFactory.takeScreenshot("TC12_AddCategory");

            ExcelHelper.write("TC12", "Thêm danh mục thành công",
                    "Nhập name = " + catName,
                    "Login admin",
                    "Nhập name và click Save",
                    catName,
                    ok ? "Hiển thị thông báo thêm thành công" : "Không",
                    "", img,
                    ok ? "PASS" : "FAIL");

            Assert.assertTrue(ok);
        } catch (Exception e) {
            String img = DriverFactory.takeScreenshot("TC12_Error");
            ExcelHelper.write("TC12", "Add category - Error",
                    "Nhập name",
                    "Login admin",
                    "Nhập name → Save",
                    "",
                    "Error: " + e.getMessage(),
                    "", img,
                    "FAIL");
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void TC13_AddCategory_EmptyName() {
        try {
            driver.get("http://localhost:8080/PolyLab7/admin/category");
            try { driver.findElement(By.cssSelector(".btn-add")).click(); } catch (Exception ignored) {}
            Thread.sleep(500);

            driver.findElement(By.name("name")).clear();
            try { driver.findElement(By.id("btnSave")).click(); } catch (Exception ex) { driver.findElement(By.cssSelector("button[type='submit']")).click(); }
            Thread.sleep(500);

            boolean ok = driver.getPageSource().contains("Không được để trống") || driver.getPageSource().contains("required");
            String img = DriverFactory.takeScreenshot("TC13_AddCategory_Empty");

            ExcelHelper.write("TC13", "Thêm category rỗng",
                    "Để trống name",
                    "Login admin",
                    "Click Save với name rỗng",
                    "",
                    ok ? "Hiển thị validate lỗi" : "Không",
                    "", img,
                    ok ? "PASS" : "FAIL");

            Assert.assertTrue(ok);
        } catch (Exception e) {
            String img = DriverFactory.takeScreenshot("TC13_Error");
            ExcelHelper.write("TC13", "Add empty - Error",
                    "Click Save với name rỗng",
                    "Login admin",
                    "Submit form rỗng",
                    "",
                    "Error: " + e.getMessage(),
                    "", img,
                    "FAIL");
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void TC14_AddCategory_Duplicate() {
        try {
            driver.get("http://localhost:8080/PolyLab7/admin/category");
            String name = "DupCatTest";

            // add first
            try { driver.findElement(By.cssSelector(".btn-add")).click(); } catch (Exception ignored) {}
            driver.findElement(By.name("name")).clear();
            driver.findElement(By.name("name")).sendKeys(name);
            try { driver.findElement(By.id("btnSave")).click(); } catch (Exception ex) { driver.findElement(By.cssSelector("button[type='submit']")).click(); }
            Thread.sleep(700);

            // add duplicate
            try { driver.findElement(By.cssSelector(".btn-add")).click(); } catch (Exception ignored) {}
            driver.findElement(By.name("name")).clear();
            driver.findElement(By.name("name")).sendKeys(name);
            try { driver.findElement(By.id("btnSave")).click(); } catch (Exception ex) { driver.findElement(By.cssSelector("button[type='submit']")).click(); }
            Thread.sleep(700);

            boolean ok = driver.getPageSource().contains("đã tồn tại") || driver.getPageSource().contains("already exists");
            String img = DriverFactory.takeScreenshot("TC14_Duplicate");

            ExcelHelper.write("TC14", "Thêm category trùng",
                    "Thêm tên = " + name,
                    "Login admin",
                    "Nhập tên duplicate → Save",
                    name,
                    ok ? "Hiển thị báo lỗi trùng" : "Không",
                    "", img,
                    ok ? "PASS" : "FAIL");

            Assert.assertTrue(ok);
        } catch (Exception e) {
            String img = DriverFactory.takeScreenshot("TC14_Error");
            ExcelHelper.write("TC14", "Add duplicate - Error",
                    "Nhập tên trùng",
                    "Login admin",
                    "Submit form duplicate",
                    "",
                    "Error: " + e.getMessage(),
                    "", img,
                    "FAIL");
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void TC15_UpdateCategory() {
        try {
            driver.get("http://localhost:8080/PolyLab7/admin/category");
            driver.findElement(By.linkText("Sửa")).click();
            Thread.sleep(500);

            String newName = "EditedCat" + System.currentTimeMillis() % 10000;
            driver.findElement(By.name("name")).clear();
            driver.findElement(By.name("name")).sendKeys(newName);
            try { driver.findElement(By.id("btnUpdate")).click(); } catch (Exception ex) { driver.findElement(By.cssSelector("button[type='submit']")).click(); }
            Thread.sleep(700);

            boolean ok = driver.getPageSource().contains("Cập nhật thành công") || driver.getPageSource().contains(newName);
            String img = DriverFactory.takeScreenshot("TC15_Update");

            ExcelHelper.write("TC15", "Sửa danh mục",
                    "Sửa name -> " + newName,
                    "Login admin",
                    "Click edit → update name → Save",
                    newName,
                    ok ? "Hiển thị thông báo cập nhật thành công" : "Không",
                    "", img,
                    ok ? "PASS" : "FAIL");

            Assert.assertTrue(ok);
        } catch (Exception e) {
            String img = DriverFactory.takeScreenshot("TC15_Error");
            ExcelHelper.write("TC15", "Update category - Error",
                    "Sửa name",
                    "Login admin",
                    "Click edit → update → Save",
                    "",
                    "Error: " + e.getMessage(),
                    "", img,
                    "FAIL");
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void TC16_DeleteCategory() {
        try {
            driver.get("http://localhost:8080/PolyLab7/admin/category");
            driver.findElement(By.linkText("Xóa")).click();
            try { driver.switchTo().alert().accept(); } catch (Exception ignored) {}
            Thread.sleep(700);

            boolean ok = driver.getPageSource().contains("Xóa thành công");
            String img = DriverFactory.takeScreenshot("TC16_Delete");

            ExcelHelper.write("TC16", "Xóa danh mục",
                    "Xóa 1 category",
                    "Login admin",
                    "Click delete → confirm",
                    "",
                    ok ? "Hiển thị thông báo xóa thành công" : "Không",
                    "", img,
                    ok ? "PASS" : "FAIL");

            Assert.assertTrue(ok);
        } catch (Exception e) {
            String img = DriverFactory.takeScreenshot("TC16_Error");
            ExcelHelper.write("TC16", "Delete category - Error",
                    "Click delete",
                    "Login admin",
                    "Xóa category",
                    "",
                    "Error: " + e.getMessage(),
                    "", img,
                    "FAIL");
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void TC17_SearchCategory() {
        try {
            driver.get("http://localhost:8080/PolyLab7/admin/category");
            driver.findElement(By.name("keyword")).clear();
            driver.findElement(By.name("keyword")).sendKeys("Thể");
            try { driver.findElement(By.id("btnSearch")).click(); } catch (Exception ignored) {}
            Thread.sleep(600);

            boolean ok = driver.getPageSource().contains("Thể");
            String img = DriverFactory.takeScreenshot("TC17_Search");

            ExcelHelper.write("TC17", "Tìm kiếm category",
                    "Tìm 'Thể'",
                    "Login admin",
                    "Điền keyword → Search",
                    "Thể",
                    ok ? "Hiển thị danh mục liên quan" : "Không",
                    "", img,
                    ok ? "PASS" : "FAIL");

            Assert.assertTrue(ok);
        } catch (Exception e) {
            String img = DriverFactory.takeScreenshot("TC17_Error");
            ExcelHelper.write("TC17", "Search category - Error",
                    "Nhập keyword 'Thể'",
                    "Login admin",
                    "Search category",
                    "",
                    "Error: " + e.getMessage(),
                    "", img,
                    "FAIL");
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void TC18_PaginationCategory() {
        try {
            driver.get("http://localhost:8080/PolyLab7/admin/category?page=2");
            boolean ok = driver.getCurrentUrl().contains("page=2");
            String img = DriverFactory.takeScreenshot("TC18_Pagination");

            ExcelHelper.write("TC18", "Phân trang category",
                    "Mở page=2",
                    "Login admin",
                    "Mở URL /admin/category?page=2",
                    "",
                    ok ? "Hiển thị page=2" : "Không",
                    "", img,
                    ok ? "PASS" : "FAIL");

            Assert.assertTrue(ok);
        } catch (Exception e) {
            String img = DriverFactory.takeScreenshot("TC18_Error");
            ExcelHelper.write("TC18", "Pagination category - Error",
                    "Mở page=2",
                    "Login admin",
                    "Phân trang category",
                    "",
                    "Error: " + e.getMessage(),
                    "", img,
                    "FAIL");
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void TC19_InvalidCategoryName() {
        try {
            driver.get("http://localhost:8080/PolyLab7/admin/category");
            try { driver.findElement(By.cssSelector(".btn-add")).click(); } catch (Exception ignored) {}
            Thread.sleep(300);

            driver.findElement(By.name("name")).clear();
            driver.findElement(By.name("name")).sendKeys("@@@@");
            try { driver.findElement(By.id("btnSave")).click(); } catch (Exception ex) { driver.findElement(By.cssSelector("button[type='submit']")).click(); }
            Thread.sleep(600);

            boolean ok = driver.getPageSource().contains("Tên không hợp lệ") || driver.getPageSource().contains("không hợp lệ");
            String img = DriverFactory.takeScreenshot("TC19_InvalidName");

            ExcelHelper.write("TC19", "Tên category không hợp lệ",
                    "Nhập @@@@",
                    "Login admin",
                    "Nhập tên invalid → Save",
                    "@@@@",
                    ok ? "Hiển thị validate lỗi" : "Không",
                    "", img,
                    ok ? "PASS" : "FAIL");

            Assert.assertTrue(ok);
        } catch (Exception e) {
            String img = DriverFactory.takeScreenshot("TC19_Error");
            ExcelHelper.write("TC19", "Invalid name - Error",
                    "Nhập tên invalid",
                    "Login admin",
                    "Submit form",
                    "",
                    "Error: " + e.getMessage(),
                    "", img,
                    "FAIL");
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void TC20_LongCategoryName() {
        try {
            driver.get("http://localhost:8080/PolyLab7/admin/category");
            try { driver.findElement(By.cssSelector(".btn-add")).click(); } catch (Exception ignored) {}
            Thread.sleep(300);

            String longName = "a".repeat(150);
            driver.findElement(By.name("name")).clear();
            driver.findElement(By.name("name")).sendKeys(longName);
            try { driver.findElement(By.id("btnSave")).click(); } catch (Exception ex) { driver.findElement(By.cssSelector("button[type='submit']")).click(); }
            Thread.sleep(600);

            boolean ok = driver.getPageSource().contains("quá dài") || driver.getPageSource().contains("too long");
            String img = DriverFactory.takeScreenshot("TC20_LongName");

            ExcelHelper.write("TC20", "Tên category quá dài",
                    "Nhập 150 ký tự",
                    "Login admin",
                    "Điền tên dài → Save",
                    longName,
                    ok ? "Hiển thị validate lỗi" : "Không",
                    "", img,
                    ok ? "PASS" : "FAIL");

            Assert.assertTrue(ok);
        } catch (Exception e) {
            String img = DriverFactory.takeScreenshot("TC20_Error");
            ExcelHelper.write("TC20", "Long name - Error",
                    "Nhập tên quá dài",
                    "Login admin",
                    "Submit form",
                    "",
                    "Error: " + e.getMessage(),
                    "", img,
                    "FAIL");
            Assert.fail(e.getMessage());
        }
    }
}

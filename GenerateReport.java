package ExtentReportListener;

import java.io.File;
import java.io.IOException;
 
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
 
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.github.bonigarcia.wdm.WebDriverManager;
 
public class GenerateReport {
 
    WebDriver driver;
    String appUrl;
 
    static ExtentReports reports;
    ExtentTest test;
 
    @BeforeClass
    public synchronized void initialize() {
        // Create an instance of ExtentsReports class and pass report storage
        // path as a parameter
        reports = new ExtentReports(System.getProperty("user.dir") + "/HtmlReport/index.html", true);
    }
 
    @BeforeTest
    public void setup() throws IOException {
    	WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
        appUrl = "http://automate-apps.com";
    }
 
    @Test
    public void testApp_1() throws IOException {
        try {
            // Start test. Mention test script name
            test = reports.startTest("test1", "Validate Page Navigation");
 
            driver.manage().window().maximize();
 
            // Launch URL
            driver.get(appUrl);
            test.log(LogStatus.INFO, "Application is launched");
 
            // Validate Home page title
            Assert.assertEquals(driver.getTitle(), "Automate Apps | Way to learn Automation");
            // Print log info in HTML report
            test.log(LogStatus.INFO, "Home Page Title Validated");
 
            // Clicking on Contents tab
            driver.findElement(By.linkText("Contents")).click();
            // Validating Contents Page title
            Assert.assertEquals(driver.getTitle(), "Contents  Automate Apps");
            // Print log info in HTML report
            test.log(LogStatus.INFO, "Contens Page Title Validated");
 
        } catch (Throwable t) {
            // Print fail info in HTML report
            test.log(LogStatus.FAIL, t.getMessage());
            String screenShotPath = System.getProperty("user.dir") + "/HtmlReport/Failure.png";
            // Take screen shot of page
            takeScreenShot(driver, screenShotPath);
 
            // Attach screen shot in HTML report
            test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(screenShotPath));
             
            Assert.assertTrue(false, t.getMessage());
 
        }
 
    }
 
    @AfterTest
    public void tearDown() {
        // Ending Test
        reports.endTest(test);
 
        // writing everything into HTML report
        reports.flush();
    }
 
    @AfterClass
    public void clearingSetup() {
        // Quitting browser
        driver.quit();
    }
 
    public void takeScreenShot(WebDriver driver, String filePath) {
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(scrFile, new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.*;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

public class allure {

WebDriver driver;

    @BeforeMethod(description = "Početno paljenje brauzera i podešavanje ekrana")
    public void setup(){
    WebDriverManager.chromedriver().setup();
    driver = new ChromeDriver();
    driver.manage().window().maximize();
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
    }

    @AfterMethod(description = "Gašenje brauzera")
    public void tearDown() throws IOException {
        reportScreenshot("Pretraga"+System.currentTimeMillis(),"Slika sledeće strane");
        driver.quit();
    }

    @Epic("Pretraga")
    @Feature("Pretraga automobila")
    @Test(description = "Testiranje pretrage na sajtu")
    @Description("Testiranje pretrage automobila na sajtu")
    @Step("Korišćenje pretrage")
    @Link("https://google.com")
    @Issue("BD-2758")
    @TmsLink("DEMO-1")
    @Severity(SeverityLevel.BLOCKER)
    @Story("Pretraga automobila marke Alfa Romeo")
    public void searchCar(){
        driver.get("https://www.polovniautomobili.com/");

        //Prihvatanje kolačića
        driver.findElement(By.cssSelector(".paBlueButtonPrimary")).click();

        //Odabir marke automobila
        driver.findElement(By.cssSelector("[title=' Sve marke']")).click();
        driver.findElement(By.xpath("//label[contains(text(),'Alf')]")).click();

        //Odabir modela (Eksplicitno čekamo da dropdown meni može da se klikne pošto ga aktivira odabir u prethodnom meniju
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        webDriverWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[title=' Svi modeli']")));
        driver.findElement(By.cssSelector("[title=' Svi modeli']")).click();
        driver.findElement(By.xpath("//label[contains(text(),'145')]")).click();

        //Unos kranje cene
        driver.findElement(By.cssSelector("#price_to")).click();
        driver.findElement(By.cssSelector("#price_to")).sendKeys("5000");

        //Odabir početnog godišta

        driver.findElement(By.cssSelector("[title=' Godište od']")).click();
        driver.findElement(By.xpath("//label[text()='2022 god.']")).click();

        //Slanje upita
        driver.findElement(By.cssSelector("[name='submit_1']")).click();

        //Provera da li nam je naslov rezultata očekivani
        Assert.assertEquals(driver.findElement(By.cssSelector(".searchTitle")).getText(),"Alfa Romeo 145 od 2022 - Cena do 5000€");

    }

    public void takeScreenshot(String fileName) throws IOException {
        File file = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(file, new File("results/screenshots/"+fileName+".png"));
    }

    public void reportScreenshot(String fileName, String desc) throws IOException {
        takeScreenshot(fileName);
        Path content = Paths.get("results/screenshots/"+fileName+".png");
        try(InputStream is = Files.newInputStream(content)){
            Allure.addAttachment(desc,is);
        }catch (IOException e){
            e.printStackTrace();
            e.getMessage();
        }
    }

}

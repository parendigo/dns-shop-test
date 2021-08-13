package ru.dnsshop.tests.base;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class BaseTests {

    private WebDriver driver;
    private Wait wait;

    public WebDriver getDriver() {
        return driver;
    }

    public Wait getWait() {
        return wait;
    }

    @BeforeEach
    public void beforeEach () {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 30);
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.navigate().to("https://www.dns-shop.ru/");
    }

    @AfterEach
    public void afterEach () {
//        driver.quit();
    }

    public WebElement clickElement (By by) {
        try {
            WebElement element = getDriver().findElement(by);
            element.click();
            return element;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            System.out.println(e.getMessage());
            Assertions.fail("Element was not found");
        }
        return null;
    }

    public WebElement clickElement (By by, By check, String contain) {
        try {
            WebElement element = getDriver().findElement(by);
            element.click();
            WebElement afterClickElement = getDriver().findElement(check);
            Assertions.assertTrue(afterClickElement.getText().contains(contain));
            return element;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            System.out.println(e.getMessage());
            Assertions.fail("Element was not found");
        }
        return null;
    }

    public WebElement fillField (By by, String str) {
        try {
            WebElement element = getDriver().findElement(by);
            element.sendKeys(str);
            Assertions.assertEquals(str, element.getAttribute("value"), "Field is not filled");
            return element;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            System.out.println(e.getMessage());
            Assertions.fail("Element was not found");
        }
        return null;
    }

    public WebElement clickAndFillField (By by, String str) {
        try {
            WebElement element = getDriver().findElement(by);
            element.click();
            element.sendKeys(str);
            Assertions.assertEquals(str, element.getAttribute("value"), "Field is not filled");
            return element;
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
            Assertions.fail("Element was not found");
        }
        return null;
    }

    public WebElement clickWaitAndFillField (By by, String str) {
        try {
            WebElement element = getDriver().findElement(by);
            getWait().until(ExpectedConditions.elementToBeClickable(element));
            element.click();
            element.sendKeys(str);
            Assertions.assertEquals(str, element.getAttribute("value"), "Field is not filled");
            return element;
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
            Assertions.fail("Element was not found");
        }
        return null;
    }

    public WebElement fillPhoneNumber (By by, String str) {
        try {
            WebElement element = getDriver().findElement(by);
            element.click();
            element.sendKeys(str);
            Assertions.assertEquals("+7 " + str, element.getAttribute("value"), "Field is not filled");
            return element;
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
            Assertions.fail("Element was not found");
        }
        return null;
    }

    public WebElement checkExistance (By by, String str) {
        try {
            WebElement element = getDriver().findElement(by);
            Assertions.assertTrue(element.getText().contains(str), "Previus action was not executed");
            return element;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public WebElement checkExistance (By by) {
        try {
            WebElement element = getDriver().findElement(by);
            return element;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public String getElementAttribute (By by, String attr) {
        try {
            WebElement element = getDriver().findElement(by);
            return element.getAttribute(attr);
        } catch (org.openqa.selenium.NoSuchElementException e) {
            System.out.println(e.getMessage());
            Assertions.fail("Element was not found");
        }
        return null;
    }
}

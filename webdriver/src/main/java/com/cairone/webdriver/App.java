package com.cairone.webdriver;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class App 
{
	private static SeleniumConfig DRIVER;
	private static String HOST;
	private static String CUIT_PARA_INFORMAR;
	
	static {
		DRIVER = new SeleniumConfig();
		HOST = "http://www.informescea.com.ar";
		CUIT_PARA_INFORMAR = "***";
	}
	
    public static void main( String[] args ) throws Throwable
    {
    	WebDriver webDriver = DRIVER.getDriver();
    	
    	// *** LOGIN EN ESTUDIO CEA
    	
    	webDriver.get(HOST + "/login.php");
    	
    	WebElement usernameInput = webDriver.findElement(By.id("username"));
    	WebElement pwdInput = webDriver.findElement(By.id("password"));
    	
    	usernameInput.sendKeys("***");
    	pwdInput.sendKeys("***");
    	
    	WebElement submitBtn = webDriver.findElement(By.className("buttonsubmit"));
    	DRIVER.clickElement(submitBtn);

    	// *** BUSCAR PERSONA POR CUIT
    	
    	WebElement elemTypeSelect = webDriver.findElement(By.id("type"));
    	WebElement elemCriterioInput = webDriver.findElement(By.id("s"));
    	WebElement elemReportTypeRadio = webDriver.findElement(By.name("reporttype"));
    	
    	Select typeSelect = new Select(elemTypeSelect);
    	typeSelect.selectByValue("cuit");
    	
    	elemCriterioInput.sendKeys(CUIT_PARA_INFORMAR);
    	DRIVER.clickElement(elemReportTypeRadio);
    	
    	WebElement submitBtn2 = webDriver.findElement(By.name("search")).findElement(By.className("buttonsubmit"));
    	DRIVER.clickElement(submitBtn2);
    	
    	WebElement elemResultDiv = webDriver.findElement(By.cssSelector("div.twelve.columns.result"));
    	DRIVER.clickElement(elemResultDiv);
    	
    	// *** IMPRIMIR EN PDF
    	
    	waitForPageLoaded();
    	((JavascriptExecutor)webDriver).executeScript("window.print();");
    	
    	try {
            Thread.sleep(5000);
        } catch (Throwable error) {
            throw error;
        }
    	
    	// *** CERRAR SESION
    	
    	WebElement elemLogout = webDriver.findElement(By.cssSelector("a.link-button.logout"));
    	DRIVER.clickElement(elemLogout);

    	// *** CERRAR BROWSER
    	
    	webDriver.close();
    	
    	System.out.println("Listo!");
    }
    
    public static void waitForPageLoaded() throws Throwable {
        ExpectedCondition<Boolean> expectation = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");
                    }
                };
        try {
            Thread.sleep(1000);
            WebDriverWait wait = new WebDriverWait(DRIVER.getDriver(), 30);
            wait.until(expectation);
        } catch (Throwable error) {
            throw error;
        }
    }
}

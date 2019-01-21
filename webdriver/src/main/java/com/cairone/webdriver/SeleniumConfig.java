package com.cairone.webdriver;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

public class SeleniumConfig {

	static {
	    System.setProperty("webdriver.gecko.driver", findFile("geckodriver.exe"));
	}
	
	private WebDriver driver;
	
	public SeleniumConfig() {
//		Capabilities capabilities = DesiredCapabilities.firefox();
//	    driver = new FirefoxDriver(capabilities);
	    
	    FirefoxProfile profile = new FirefoxProfile();
	    profile.setPreference("print.always_print_silent", true);
	    profile.setPreference("print.printer_Microsoft_Print_to_PDF.print_to_file", true);
	    profile.setPreference("print.printer_Microsoft_Print_to_PDF.print_to_filename", "src/output/informecea.pdf");
	    
	    driver = new FirefoxDriver(profile);
	    driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}
	
	static private String findFile(String filename) {
		String paths[] = {"", "bin/", "target/classes"};
		for (String path : paths) {
			if (new File(path + filename).exists())
				return path + filename;
		}
		return "";
	}
	
	public void close() {
        driver.close();
    }

    public void navigateTo(String url) {
        driver.navigate().to(url);
    }

    public void clickElement(WebElement element) {
        element.click();
    }

    public WebDriver getDriver() {
        return driver;
    }
}

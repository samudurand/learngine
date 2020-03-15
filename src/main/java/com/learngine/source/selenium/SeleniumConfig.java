package com.learngine.source.selenium;

import com.learngine.configuration.SearchedFailedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@Configuration
public class SeleniumConfig {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Profile("default")
    public WebDriver defaultBrowser() {
        var options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("--start-maximized");
        options.addArguments("--window-size=2560,1440");

        System.setProperty("webdriver.chrome.silentOutput", "true");
        var browser = new ChromeDriver(options);
        browser.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return browser;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Profile("dev | preprod | prod")
    public WebDriver remoteBrowser() {
        var options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("--start-maximized");
        options.addArguments("--window-size=2560,1440");
        var capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);

        try {
            var browser = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities);
            browser.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            return browser;
        } catch (MalformedURLException e) {
            throw new SearchedFailedException();
        }
    }
}

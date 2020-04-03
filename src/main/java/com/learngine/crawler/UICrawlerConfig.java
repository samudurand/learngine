package com.learngine.crawler;

import com.learngine.config.SearchFailedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Configuration
public class UICrawlerConfig {

    @Bean
    public ChromeOptions browserOptions() {
        var options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("--start-maximized");
        options.addArguments("--window-size=2560,1440");
        return options;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Profile("local-driver")
    public Supplier<WebDriver> defaultBrowser(ChromeOptions options) {
        return () -> {
            System.setProperty("webdriver.chrome.silentOutput", "true");
            var browser = new ChromeDriver(options);
            browser.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            return browser;
        };
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Profile("default | dev | preprod | prod")
    public Supplier<WebDriver> remoteBrowser(@Value("${selenium.node.url}") String seleniumNodeUrl, ChromeOptions options) {
        return () -> {
            var capabilities = DesiredCapabilities.chrome();
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);

            try {
                var browser = new RemoteWebDriver(new URL(seleniumNodeUrl), capabilities);
                browser.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                return browser;
            } catch (MalformedURLException e) {
                throw new SearchFailedException(e);
            }
        };
    }
}

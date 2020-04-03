package com.learngine.source.streaming.it;

import com.learngine.crawler.UICrawlerConfig;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.Testcontainers;
import org.testcontainers.containers.BrowserWebDriverContainer;

/**
 * Implementing this Interface will create a single group of Selenium Chrome based containers for the whole test class,
 * then shut them down after all the tests have run.
 */
public interface SeleniumTest {

    UICrawlerConfig config = new UICrawlerConfig();

    BrowserWebDriverContainer seleniumNode = new BrowserWebDriverContainer().withCapabilities(config.browserOptions());

    /**
     * Indicates where the app tested should be running (applies also to Wiremock)
     */
    Integer EXPECTED_APP_PORT = 5000;
    String EXPECTED_WEBSITE_URL = "http://host.testcontainers.internal:" + EXPECTED_APP_PORT;

    @BeforeAll
    static void oneTimeSetup() {
        Testcontainers.exposeHostPorts(SeleniumTest.EXPECTED_APP_PORT);
        seleniumNode.start();
    }
}

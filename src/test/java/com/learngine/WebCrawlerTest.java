//package com.learninge.htmlunittry;
//
//import com.github.jenspiegsa.wiremockextension.WireMockExtension;
//import org.junit.Assert;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//
//import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
//import static com.github.tomakehurst.wiremock.client.WireMock.get;
//import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
//import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
//import static org.hamcrest.CoreMatchers.containsString;
//
//@ExtendWith(WireMockExtension.class)
//class WebCrawlerTest {
//
//	@Test
//	void retrievePageContent() {
//		stubFor(get(urlEqualTo("/"))
//				.willReturn(aResponse()
//						.withStatus(200)
//						.withHeader("Content-Type", "text/html")
//						.withBody("Empty page")));
//
//		var pageContent = new WebCrawler().getPageContent("http://localhost:8080/");
//
//		Assert.assertThat(pageContent, containsString("Empty page"));
//	}
//
//	@Test
//	void returnEmptyPageContentIfRequestReturns4xxError() {
//		stubFor(get(urlEqualTo("/"))
//				.willReturn(aResponse()
//						.withHeader("Content-Type", "text/html")
//						.withStatus(401)));
//
//		var pageContent = new WebCrawler().getPageContent("http://localhost:8080/");
//
//		Assert.assertEquals("", pageContent);
//	}
//
//	@Test
//	void returnEmptyPageContentIfRequestReturns5xxError() {
//		stubFor(get(urlEqualTo("/"))
//				.willReturn(aResponse()
//						.withHeader("Content-Type", "text/html")
//						.withStatus(500)));
//
//		var pageContent = new WebCrawler().getPageContent("http://localhost:8080/");
//
//		Assert.assertEquals("", pageContent);
//	}
//
//}

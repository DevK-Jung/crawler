package com.kjung.crawler.service;

import com.kjung.crawler.dto.HtmlExtractedContentDto;
import com.kjung.crawler.dto.SelectProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class HtmlCrawlerTest {

    @Test
    void crawl() {
        String userAgent = "KJUNG/1.0";

        SelectProvider selector = new SelectProvider(
                "devk-jung.github.io",
                "div.content"
        );

        HtmlCrawler htmlCrawler = new HtmlCrawler(userAgent, List.of(selector));

        HtmlExtractedContentDto crawl = htmlCrawler.crawl("https://devk-jung.github.io/posts/log4jdbc/");

        String content = crawl.content();

        Assertions.assertThat(content).isNotEmpty();

//        HtmlCrawler crawler = new HtmlCrawler();
    }
}
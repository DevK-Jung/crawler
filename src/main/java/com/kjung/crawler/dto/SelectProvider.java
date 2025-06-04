package com.kjung.crawler.dto;

public record SelectProvider(
        String domain,  // domain
        String tag // 크롤링 대상 html tag
) {

}

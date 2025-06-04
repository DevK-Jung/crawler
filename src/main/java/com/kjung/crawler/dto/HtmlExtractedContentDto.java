package com.kjung.crawler.dto;

public record HtmlExtractedContentDto(
        String url,
        String domain,
        String content,
        boolean success,
        String message
) {
}

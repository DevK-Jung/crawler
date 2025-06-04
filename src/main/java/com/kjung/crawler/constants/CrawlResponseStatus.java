package com.kjung.crawler.constants;

import lombok.Getter;

@Getter
public enum CrawlResponseStatus {
    OK("OK", true),
    ERROR("ERROR", false);

    private final String message;
    private final boolean success;

    CrawlResponseStatus(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

}

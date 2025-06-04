package com.kjung.crawler.dto;

import com.kjung.crawler.constants.CrawlResponseStatus;

public record HtmlExtractedContentDto(
        String url,
        String domain,
        String content,
        boolean success,
        String message
) {

    public static HtmlExtractedContentDto createSuccessResponse(
            String url,
            String domain,
            String content) {

        return new HtmlExtractedContentDto(
                url,
                domain,
                content,
                CrawlResponseStatus.OK.isSuccess(),
                CrawlResponseStatus.OK.getMessage()
        );
    }

    public static HtmlExtractedContentDto createErrorResponse(
            String url,
            String domain,
            Throwable throwable
    ) {
        return new HtmlExtractedContentDto(
                url,
                domain,
                null,
                CrawlResponseStatus.ERROR.isSuccess(),
                CrawlResponseStatus.ERROR.getMessage() + ": " + throwable.getMessage()
        );
    }
}

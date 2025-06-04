package com.kjung.crawler.service;

import com.kjung.crawler.dto.HtmlExtractedContentDto;
import com.kjung.crawler.dto.SelectProvider;
import com.kjung.crawler.util.RobotsTxtChecker;
import io.micrometer.common.util.StringUtils;
import lombok.NonNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.util.CollectionUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class HtmlCrawler {

    private final String userAgent;

    private final int timeoutMillis;

    private final List<SelectProvider> selectProviders;

    public HtmlCrawler(@NonNull String userAgent,
                       @NonNull List<SelectProvider> selectProviders) {

        this(userAgent, 0, selectProviders);
    }

    public HtmlCrawler(@NonNull String userAgent,
                       int timeoutMillis,
                       @NonNull List<SelectProvider> selectProviders) {

        if (StringUtils.isBlank(userAgent))
            throw new IllegalArgumentException("userAgent는 필수 입니다.");

        if (CollectionUtils.isEmpty(selectProviders))
            throw new IllegalArgumentException("selectProviders는 필수 입니다.");

        this.userAgent = userAgent;
        this.timeoutMillis = timeoutMillis > 0 ? timeoutMillis : 5000;
        this.selectProviders = selectProviders;
    }

    public HtmlExtractedContentDto crawl(String url) {

        if (!RobotsTxtChecker.isAllowed(url, userAgent))
            throw new IllegalArgumentException("크롤링이 robots.txt에 의해 차단됨");

        try {


            Document doc = Jsoup.connect(url)
                    .userAgent(userAgent)
                    .timeout(timeoutMillis)
                    .get();

            String domain = getDomain(url);

            SelectProvider selector = getSelector(domain);

            String content = extractContent(doc, selector);

            return HtmlExtractedContentDto.createSuccessResponse(url, getDomain(url), content);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getDomain(String urlStr) throws URISyntaxException {

        URI uri = new URI(urlStr);
        return uri.getHost();
    }


    private String extractContent(Document doc,
                                  SelectProvider selector) {

        Elements elems = doc.select(selector.tag());

        if (!elems.isEmpty()) return elems.text();

        // fallback selector 후보군
        List<String> candidates = List.of("article", "main", "section", "div.content", "p");

        String bestMatch = "";
        int maxLength = 0;

        for (String sel : candidates) {
            Elements e = doc.select(sel);
            if (!e.isEmpty() && e.text().length() > maxLength) {
                bestMatch = e.text();
                maxLength = bestMatch.length();
            }
        }

        return bestMatch;
    }

    private SelectProvider getSelector(String domain) {
        return selectProviders
                .stream()
                .filter(v -> v.domain().equals(domain))
                .findFirst()
                .orElseThrow();
    }

}

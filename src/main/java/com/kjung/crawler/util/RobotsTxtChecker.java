package com.kjung.crawler.util;

import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

@UtilityClass
public class RobotsTxtChecker {
    /**
     * robots.txt 크롤링 허용 여부 판단.
     *
     * @param urlStr    문자열 url
     * @param userAgent userAgent
     * @return 크롤링 허용 여부
     */
    public boolean isAllowed(String urlStr, String userAgent) {
        try {
            URI uri = new URI(urlStr);
            String host = uri.getScheme() + "://" + uri.getHost();
            URI robotsUri = new URI(host + "/robots.txt");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(robotsUri.toURL().openStream()))) {
                return checkAccessPermission(reader, uri.getPath(), userAgent);
            }
        } catch (Exception e) {
            // robots.txt 없음 또는 파싱 오류 시 크롤링 허용 todo 파싱 오류는 체크 필요
            return true;
        }
    }

    private boolean checkAccessPermission(BufferedReader reader, String path, String userAgent) throws Exception {
        boolean applies = false;
        String line;

        while ((line = reader.readLine()) != null) {
            line = line.trim();

            if (line.isEmpty() || line.startsWith("#")) continue;

            if (line.toLowerCase().startsWith("user-agent:")) {
                String agent = line.substring("user-agent:".length()).trim();
                applies = agent.equals("*") || userAgent.equalsIgnoreCase(agent);
            }

            if (applies && line.toLowerCase().startsWith("disallow:")) {
                String disallowPath = line.substring("disallow:".length()).trim();
                if (!disallowPath.isEmpty() && path.startsWith(disallowPath)) {
                    return false;
                }
            }
        }

        return true;
    }
}

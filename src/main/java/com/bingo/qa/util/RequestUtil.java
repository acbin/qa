package com.bingo.qa.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * @author bingo
 * @since 2018/8/12
 */

public class RequestUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(RequestUtil.class);

    public static Document getDocument(String url, Map<String, String> header) {

        try {
            return Jsoup.connect(url).headers(header).get();
        } catch (IOException e) {
            LOGGER.error("URL 解析失败", e.getMessage());
            return null;
        }
    }
}

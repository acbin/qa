package com.bingo.qa.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author bingo
 * @since 2018/8/12
 */

public class RequestUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(RequestUtil.class);

    public static Document getDocument(String url) {

        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            LOGGER.error("URL 解析失败", e.getMessage());
            return null;
        }
    }
}

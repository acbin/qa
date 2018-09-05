package com.bingo.qa.service.impl;

import com.bingo.qa.service.SensitiveService;
import org.apache.commons.lang3.CharUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author bingo
 */
@Service
public class SensitiveServiceImpl implements InitializingBean, SensitiveService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SensitiveServiceImpl.class);


    /**
     * 实现InitializingBean，在该类其他属性设置完之后，开始读取敏感词
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() {

        try {
            // 读取敏感词文件
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String lineTxt;

            // 循环读取敏感词文本中的每一行
            while ((lineTxt = br.readLine()) != null) {
                addWord(lineTxt.trim());
            }
            br.close();

        } catch (Exception e) {
            LOGGER.error("读取敏感词文件失败:" + e.getMessage());
        }

    }


    /**
     * 读取敏感词文本中的每一行，建立前缀树
     * @param lineTxt
     */
    private void addWord(String lineTxt) {
        if (StringUtils.isEmpty(lineTxt)) {
            return;
        }

        TrieNode tempNode = root;
        char[] arr = lineTxt.toCharArray();
        int len = arr.length;
        for (int i = 0; i < len; ++i) {
            Character c = arr[i];
            if (isSymbol(c)) {
                continue;
            }

            // 判断temp下是否有此子结点
            TrieNode node = tempNode.getSubNode(c);
            if (node == null) {
                // 没有此结点，则新建一个结点
                node = new TrieNode();
                tempNode.addSubNode(c, node);
            }
            tempNode = node;
            if (i == len - 1) {
                // 如果到了一行的最后一个文字，则进行标记
                tempNode.setKeyWord(true);
            }
        }
    }


    /**
     * 前缀树结点：
     * 1）根结点不包含字符，除根结点外每一个结点都只包含一个字符
     * 2）从根结点到某一结点，路径上经过的字符连接起来，为该结点对应的字符串
     * 3）每个结点的所有子结点包含的字符都不相同
     */
    private class TrieNode {
        /**
         * 表示是否是敏感词的结尾
         */
        private boolean end = false;

        /**
         * 该结点下所有后续子结点
         */
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public void addSubNode(Character key, TrieNode node) {
            subNodes.put(key, node);
        }

        /**
         * 根据key获取map中的某个子结点
         * @param key
         * @return
         */
        TrieNode getSubNode(Character key) {
            return subNodes.get(key);
        }

        boolean isKeyWord() {
            return end;
        }

        void setKeyWord(boolean end) {
            this.end = end;
        }

    }

    private TrieNode root = new TrieNode();

    /**
     * 判断是否是特殊字符
     * @param c
     * @return
     */
    private boolean isSymbol(char c) {
        int ic = (int) c;
        // 0x2E80~0x9FFF 为东亚文字
        // 如果既不是东亚文字，也不是英文字符，返回false（说明是非法词）
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }


    /**
     * 敏感词过滤方法
     *
     * @param text UGC
     * @return 过滤后的UGC
     */
    @Override
    public String filter(String text) {
        if (StringUtils.isEmpty(text)) {
            // 空文本直接返回
            return text;
        }

        StringBuilder result = new StringBuilder();

        // replacement作为敏感词的替换
        String replacement = "***";
        TrieNode tempNode = root;

        // begin和position均指向text
        int begin = 0;
        int position = 0;

        while (position < text.length()) {
            char c = text.charAt(position);

            if (isSymbol(c)) {

                if (tempNode == root) {
                    result.append(c);
                    ++begin;
                }
                // 非法字符，直接跳过
                ++position;
                continue;
            }

            tempNode = tempNode.getSubNode(c);
            if (tempNode == null) {
                // 该字符在敏感词前缀树中没有,直接将begin字符添加至result串中
                result.append(text.charAt(begin));
                position = begin + 1;
                begin = position;
                tempNode = root;
            } else if (tempNode.isKeyWord()) {
                // 发现敏感词
                result.append(replacement);
                ++position;
                begin = position;
                tempNode = root;
            } else {
                ++position;
            }

        }
        result.append(text.substring(begin));
        return result.toString();
    }
}
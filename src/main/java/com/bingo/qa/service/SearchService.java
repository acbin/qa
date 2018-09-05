package com.bingo.qa.service;

import com.bingo.qa.model.Question;
import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;
import java.util.List;

/**
 * @author bingo
 * @since 2018/8/11
 */

public interface SearchService {
    /**
     * 查找问题列表
     * @param keyword
     * @param offset
     * @param count
     * @param hlPre
     * @param hlPos
     * @return
     * @throws IOException
     * @throws SolrServerException
     */
    List<Question> searchQuestion(String keyword,
                                  int offset,
                                  int count,
                                  String hlPre,
                                  String hlPos) throws IOException, SolrServerException;

    /**
     * 索引问题
     * @param qid
     * @param title
     * @param content
     * @return
     * @throws IOException
     * @throws SolrServerException
     */
    boolean indexQuestion(int qid, String title, String content) throws IOException, SolrServerException;
}

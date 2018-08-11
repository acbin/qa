package com.bingo.qa.service;

import com.bingo.qa.model.Question;
import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;
import java.util.List;

/**
 * Created by bingo on 2018/8/11.
 */

public interface SearchService {
    List<Question> searchQuestion(String keyword,
                                  int offset,
                                  int count,
                                  String hlPre,
                                  String hlPos) throws IOException, SolrServerException;

    boolean indexQuestion(int qid, String title, String content) throws IOException, SolrServerException;
}

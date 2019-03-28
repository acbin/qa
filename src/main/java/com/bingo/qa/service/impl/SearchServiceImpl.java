package com.bingo.qa.service.impl;

import com.bingo.qa.model.Question;
import com.bingo.qa.service.SearchService;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author bingo
 * @since 2018/6/4
 */

@Service
public class SearchServiceImpl implements SearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchServiceImpl.class);

    private static final String SOLR_ULR = "http://localhost:8983/solr/qa";
    private HttpSolrClient client = new HttpSolrClient.Builder(SOLR_ULR).build();
    private static final String QUESTION_TITLE_FIELD = "question_title";
    private static final String QUESTION_CONTENT_FIELD = "question_content";


    /**
     * @param keyword 关键词
     * @param offset  偏移
     * @param count   数量
     * @param hlPre   高亮前置
     * @param hlPos   高亮后置
     * @return
     */
    @Override
    public List<Question> searchQuestion(String keyword,
                                         int offset,
                                         int count,
                                         String hlPre,
                                         String hlPos) throws IOException, SolrServerException {
        List<Question> questionList = new ArrayList<>();

        SolrQuery query = new SolrQuery(keyword);
        query.setRows(count);
        query.setStart(offset);
        // 设置高亮
        query.setHighlight(true);

        query.setHighlightSimplePre(hlPre);
        query.setHighlightSimplePost(hlPos);
        query.set("df", QUESTION_TITLE_FIELD);
        query.set("hl.fl", QUESTION_TITLE_FIELD + "," + QUESTION_CONTENT_FIELD);

        QueryResponse response = client.query(query);

        // 解析搜索结果
        for (Map.Entry<String, Map<String, List<String>>> entry : response.getHighlighting().entrySet()) {
            Question q = new Question();

            q.setId(Integer.parseInt(entry.getKey()));
            if (entry.getValue().containsKey(QUESTION_CONTENT_FIELD)) {
                List<String> contentList = entry.getValue().get(QUESTION_CONTENT_FIELD);
                if (contentList.size() > 0) {
                    q.setContent(contentList.get(0));
                }
            }

            if (entry.getValue().containsKey(QUESTION_TITLE_FIELD)) {
                List<String> titleList = entry.getValue().get(QUESTION_TITLE_FIELD);
                if (titleList.size() > 0) {
                    q.setTitle(titleList.get(0));
                }
            }

            questionList.add(q);

        }

        return questionList;
    }

    @Override
    public boolean indexQuestion(int qid, String title, String content) throws IOException, SolrServerException {
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", qid);
        doc.addField(QUESTION_TITLE_FIELD, title);
        doc.addField(QUESTION_CONTENT_FIELD, content);

        UpdateResponse response = client.add(doc, 1000);
        client.optimize();
        client.commit();
        return response != null && response.getStatus() == 0;
    }
}
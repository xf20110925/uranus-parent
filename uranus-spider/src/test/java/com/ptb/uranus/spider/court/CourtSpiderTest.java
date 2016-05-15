package com.ptb.uranus.spider.court;

import com.ptb.uranus.spider.common.webDriver.WebDriverPoolUtils;
import org.junit.After;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by xuefeng on 2016/3/17.
 */
public class CourtSpiderTest {
    public void getArticleList() {
        List<String> caseUrls = CourtSpider.getArticleUrlsByCourt(new Date(System.currentTimeMillis() - 3600 * 24 * 5 * 1000), new Date(), "吉林省长春市中级人民法院");
        assertTrue(caseUrls.size() > 0);
    }


    public void getCourtCase() {
        CourtCase courtCase = CourtSpider.getCourtCaseByUrl("http://wenshu.court.gov.cn/content/content?DocID=0d9898a5-0701-4b58-95b3-0d51ca7a61ed");
        assertTrue(courtCase.getCaseContent().length() > 0);
        assertTrue(courtCase.getCaseCourt().length() > 0);
        assertTrue(courtCase.getCaseName().length() > 0);
        assertTrue(courtCase.getCaseNumber().length() > 0);
        assertTrue(courtCase.getCaseTitle().length() > 0);
        assertTrue(courtCase.getCaseType().length() > 0);
        assertTrue(courtCase.getDocId().length() > 0);
        assertTrue(courtCase.getPostTime().length() > 0);
        assertTrue(courtCase.getReadNum().length() > 0);
        assertTrue(courtCase.getTrialRound().length() > 0);
        assertTrue(courtCase.getTrialTime().length() > 0);
    }

    @After
    public void close() {
        WebDriverPoolUtils.instance().closeAll();
    }
}

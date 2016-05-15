package com.ptb.uranus.spider.court;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.ptb.uranus.spider.common.webDriver.WebDriverPool;
import com.ptb.uranus.spider.common.webDriver.WebDriverPoolUtils;
import com.ptb.utils.db.MongoDBUtil;
import com.ptb.utils.string.RegexUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.bson.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.ptb.uranus.spider.court.JsTemplates.CaseSumaryTemplate;

/**
 * Created by eric on 16/4/14.
 */
public class CourtSpider {
    public static List<String> getArticleUrlsByCourt(Date startDay, Date endDay, String court) {
        String condition = String.format("上传日期:%s TO %s,法院名称:%s",
                DateFormatUtils.format(startDay, "yyyy-MM-dd", Locale.CHINA),
                DateFormatUtils.format(endDay, "yyyy-MM-dd", Locale.CHINA),
                court
        );
        int index = 1;
        int page = 20;
        int caseTotalNum = 21;
        String order = "\"法院层级\"";
        String direction = "\"asc\"";
        int i = 3;

        List<String> articleUrls = new LinkedList<>();
        while (i >= 0 && (index <= (caseTotalNum / page) + ((caseTotalNum % page) == 0 ? 0 : 1))) {
            //当i>0时,使用代理,如i=0则不使用代理
            WebDriverPool webDriverPool = null;
            PhantomJSDriver phantomJSDriver = null;
            try {
                webDriverPool = i > 0 ? WebDriverPoolUtils.instance().getWebDriverFromPool(false, true) : WebDriverPoolUtils.instance().getWebDriverFromPool(false, false);
                phantomJSDriver = webDriverPool.borrowObject();
                phantomJSDriver.navigate().to("http://wenshu.court.gov.cn/");
                Thread.sleep(3000);
                while (index <= (caseTotalNum / page) + ((caseTotalNum % page) == 0 ? 0 : 1)) {
                    String executejs = String.format(JsTemplates.CaseListJsRequestTemplate, condition, index, page, order, direction);

                    DocumentContext jsonDoc = JsonPath.parse(phantomJSDriver.executeAsyncScript(executejs).toString());

                    caseTotalNum = Integer.valueOf(((List<String>) jsonDoc.read("..Count")).get(0));
                    if (caseTotalNum > 400) {
                        caseTotalNum = 400;
                    }

                    List<String> cases = jsonDoc.read("..文书ID");
                    articleUrls.addAll(cases.stream().map(id ->
                            String.format("http://wenshu.court.gov.cn/content/content?DocID=%s", id)
                    ).collect(Collectors.toList()));

                    if (cases.size() > 0 || caseTotalNum == 0) {
                        index = index + 1;
                    }
                }
            } catch (Exception e) {
                i--;
                phantomJSDriver.navigate().refresh();
                continue;
            } finally {
                if (phantomJSDriver != null) {
                    try {
                        webDriverPool.returnObject(phantomJSDriver);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return articleUrls;
    }


    public static List<String> getAllCourtName() throws IOException {
        List<String> courts = new ArrayList<String>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(CourtSpider.class.getClassLoader().getResourceAsStream("court.txt")));
        while (bufferedReader.ready()) {
            courts.add(bufferedReader.readLine());
        }
        return courts;
    }

    public static List<String> getArticleUrlsByCourt(Date startDay, Date endDay) throws IOException {
        List<String> collect = getAllCourtName().parallelStream().flatMap(l -> {
            String court = l.split(",")[1];
            try {
                List<String> articleUrlsByCourt = getArticleUrlsByCourt(startDay, endDay, court);
                return articleUrlsByCourt.stream();
            } catch (Exception e) {
                return new LinkedList<String>().stream();
            }
        }).collect(Collectors.toList());
        return collect;
    }

    public static List<String> getAllFayuan() throws Exception {
        String s = "\"北京市\", \"天津市\", \"重庆市\", \"上海市\", \"河北省\", \"山西省\", \"辽宁省\", \"吉林省\", \"黑龙江省\", \"江苏省\", \"浙江省\", \"安徽省\", \"福建省\", \"江西省\", \"山东省\", \"河南省\", \"湖北省\", \"湖南省\", \"广东省\", \"海南省\", \"四川省\", \"贵州省\", \"云南省\", \"陕西省\", \"甘肃省\", \"青海省\", \"台湾省\", \"内蒙古\", \"广西\", \"西藏\", \"宁夏\", \"新疆\"";
        String[] places = {"内蒙古"};
        String template1 =
                "var place = \"%s\";\n" +
                        "var callback = arguments[arguments.length - 1];\n" +
                        "$.ajax({\n" +
                        "            url: \"/Index/GetCourt\",\n" +
                        "            type: \"POST\",\n" +
                        "            async: true,\n" +
                        "            data: { \"province\": place },\n" +
                        "            success: function (data) {\n" +
                        "                 callback(data);\n" +
                        "            }\n" +
                        "        });";

        String template2 =
                "var place = \"%s\";\n" +
                        "var callback = arguments[arguments.length - 1];\n" +
                        "$.ajax({\n" +
                        "            url: \"/Index/GetChildAllCourt\",\n" +
                        "            type: \"POST\",\n" +
                        "            async: true,\n" +
                        "            data: { \"keyCodeArrayStr\": place },\n" +
                        "            success: function (data) {\n" +
                        "                 callback(data);\n" +
                        "            }\n" +
                        "        });";


        WebDriverPool webDriverPool = WebDriverPoolUtils.instance().getWebDriverFromPool(false, false);

        MongoCollection<Document> courtTable = MongoDBUtil.instance.getCollection("base", "court");

        List<String> list = Arrays.asList(places);
        List<String> collect = list.stream().map(place -> {
            PhantomJSDriver phantomJSDriver = null;
            try {
                phantomJSDriver = webDriverPool.borrowObject();
                phantomJSDriver.navigate().to("http://wenshu.court.gov.cn/");

                Object o = phantomJSDriver.executeAsyncScript(String.format(template1, place.replace("省", "").replace("市", "")));

                JSONArray objects = JSON.parseArray(o.toString());
                objects.stream().forEach(d -> {
                    Document parse = Document.parse(JSON.toJSONString(d));
                    parse.put("_id", parse.get("key"));
                    parse.remove("key");
                    courtTable.updateOne(Filters.eq("_id", parse.get("_id")), new Document("$set", parse), new UpdateOptions().upsert(true));
                });

                List<String> read = (List<String>) JsonPath.parse(o.toString()).read("..key");
                String join = String.join(",", read);
                Object o1 = phantomJSDriver.executeAsyncScript(String.format(template2, join));
                objects = JSON.parseArray(o1.toString());
                objects.stream().forEach(d -> {
                    Document parse = Document.parse(JSON.toJSONString(d));
                    parse.put("_id", parse.get("key"));
                    parse.remove("key");
                    courtTable.updateOne(Filters.eq("_id", parse.get("_id")), new Document("$set", parse), new UpdateOptions().upsert(true));
                });

                return o.toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    webDriverPool.returnObject(phantomJSDriver);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return place;
        }).collect(Collectors.toList());
        return collect;
    }

    public static void main(String[] args) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date start = dateFormat.parse("2016-04-18");
        Date end = dateFormat.parse("2016-04-19");
        List<String> courts = getArticleUrlsByCourt(start, end, "安徽省高级人民法院");

        courts.forEach(p -> System.out.println(p));

    }

    public static CourtCase getCourtCaseByUrl(String url) {
        int i = 3;
        CourtCase courtCase = new CourtCase();
        while (i >= 0) {
            WebDriverPool webDriverPool = null;
            PhantomJSDriver webDriver = null;
            try {
                webDriverPool = i > 0 ? WebDriverPoolUtils.instance().getWebDriverFromPool(false, true) : WebDriverPoolUtils.instance().getWebDriverFromPool(false, false);
                i--;
                webDriver = webDriverPool.borrowObject();
                webDriver.get(url);
                courtCase.setDocId(webDriver.findElement(By.cssSelector("#hidDocID")).getAttribute("value"));
                if (courtCase.getDocId() == null || courtCase.getDocId().length() < 1) {
                    continue;
                }
                courtCase.setCaseName(webDriver.findElement(By.cssSelector("#hidCaseName")).getAttribute("value"));
                courtCase.setCaseNumber(webDriver.findElement(By.cssSelector("#hidCaseNumber")).getAttribute("value"));
                courtCase.setCaseCourt(webDriver.findElement(By.cssSelector("#hidCourt")).getAttribute("value"));


                DocumentContext doc = JsonPath.parse(webDriver.executeAsyncScript(String.format(CaseSumaryTemplate, courtCase.getDocId())).toString());

                courtCase.setCaseType(((List<String>) doc.read("..RelateInfo[?(@.key == 'caseType')].value")).get(0));
                courtCase.setTrialRound(((List<String>) doc.read("..RelateInfo[?(@.key == 'trialRound')].value")).get(0));
                courtCase.setTrialTime(((List<String>) doc.read("..RelateInfo[?(@.key == 'trialDate')].value")).get(0));

                courtCase.setCaseContent(webDriver.findElement(By.cssSelector("#DivContent")).getText());
                courtCase.setCaseTitle(webDriver.findElement(By.cssSelector("#contentTitle")).getText());
                courtCase.setPostTime(RegexUtils.sub(".*(\\d\\d\\d\\d-\\d\\d-\\d\\d)", webDriver.findElement(By.cssSelector("#tdFBRQ")).getText().trim(), 0));
                courtCase.setReadNum((RegexUtils.sub("[^\\d]*(\\d*)[^\\d]*", webDriver.findElement(By.cssSelector("#con_llcs")).getText().trim(), 0)));
                return courtCase;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    webDriverPool.returnObject(webDriver);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }
}

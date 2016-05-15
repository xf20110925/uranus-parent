package com.ptb.uranus.manager.controller;

import com.alibaba.fastjson.JSON;
import com.ptb.uranus.manager.bean.ActionSet;
import com.ptb.uranus.manager.bean.News;
import com.ptb.uranus.manager.result.JSONResult;
import com.ptb.uranus.manager.service.SpiderService;
import com.ptb.uranus.manager.utils.JsonFormatter;
import com.ptb.uranus.spider.smart.SmartSpider;
import com.ptb.uranus.spider.smart.SpiderResult;
import com.ptb.uranus.spider.smart.entity.Article;
import com.ptb.uranus.spider.smart.entity.DynamicData;
import com.ptb.uranus.spider.smart.entity.NewScheduleUrls;
import com.ptb.uranus.spider.smart.utils.SmartSpiderConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Created by eric on 16/3/28.
 */
@Controller
@RequestMapping("/")
public class SpiderController {
    @Autowired
    SpiderService spiderService;

    @RequestMapping(value = {"/", "/spider/list"})
    public String index(Model model) {
        List<ActionSet> actionSetList = spiderService.getSpiderList();
        actionSetList.sort((k1, k2) -> {
            int ret = k1.getName().compareTo(k2.getName());
            if (ret == 0) {
                ret = k1.getCrawleType().compareTo(k2.getCrawleType());
            }
            return ret;
        });
        model.addAttribute("actions", actionSetList);
        return "spider/list";
    }

    @RequestMapping(value = "/spider/add", method = RequestMethod.GET)
    public String addPage(Model model) {
        return "spider/add";
    }

    @RequestMapping(value = "/spider/add", method = RequestMethod.POST)
    @ResponseBody
    public Object add(ActionSet actionSet, Model model) {

        if (actionSet.getCrawleType().length() > 0 && actionSet.getName().length() > 0 && actionSet.getUrlRegex().length() > 0) {
/*        System.out.println(JSON.toJSONString(actionSet));*/
            try {
                spiderService.addActionSet(actionSet);
                return new JSONResult<>("", "add success", true);
            } catch (Exception e) {
                e.printStackTrace();
                return JSONResult.newErrorResult(-1, "系统错误");
            }
        }
        return JSONResult.newErrorResult(-1, "参数不合法");
    }

  /*  @RequestMapping(value = "/spider/del", method = RequestMethod.POST)
    @ResponseBody
    public Object delActionSet(@RequestParam(defaultValue = "") String oid) {
        if (oid == null) {
            return JSONResult.newErrorResult("错误的ID");
        } else {
            spiderService.delActionSetById(oid);
        }

        return new JSONResult<>("添加成功");
    }*/

    @RequestMapping(value = "/spider/del", method = RequestMethod.GET)
    public Object delActionSet1(@RequestParam(defaultValue = "") String oid) {
        if (oid.length() > 0) {
            spiderService.delActionSetById(oid);
        }
        return "redirect:/spider/list";
    }

    @RequestMapping(value = "/spider/clone", method = RequestMethod.GET)
    public Object cloneActionSet1(@RequestParam(defaultValue = "") String oid) {
        if (oid.length() > 0) {
            spiderService.cloneActionSet(oid);
        }
        return "redirect:/spider/list";
    }

    @RequestMapping(value = "/spider/detail", method = RequestMethod.GET)
    public String ActionDetail(@RequestParam(defaultValue = "") String oid, Model model) {
        ActionSet actionSet = spiderService.getActionSet(oid);
        List<String> list = spiderService.getActionClassList();
        model.addAttribute("actionSet", actionSet);
        model.addAttribute("classNames", list);
        return "spider/detail";
    }

    @RequestMapping(value = "/spider/action/get", method = RequestMethod.GET)
    @ResponseBody
    public Object getActionClassField(@RequestParam(defaultValue = "") String className, Model model) {
        try {
            List<String> actionFields = spiderService.getActionFieldsByClassName(className);
            return new JSONResult<>(actionFields);
        } catch (ClassNotFoundException e) {
            return JSONResult.newErrorResult(-1, "没有找到该类");
        }
    }

    @RequestMapping(value = "/spider/action/add", method = RequestMethod.POST)
    @ResponseBody
    public Object getActionClassField(@RequestParam HashMap hashMap, Model model) {
        try {
            boolean isSuccess = spiderService.addAction(hashMap);
            if (isSuccess) {
                return new JSONResult<>(isSuccess);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JSONResult.newErrorResult(-1, e.getMessage());
        }
        return JSONResult.newErrorResult(-1, "没有该ACTIONSET");
    }

    @RequestMapping(value = "/spider/update", method = RequestMethod.POST)
    @ResponseBody
    public Object updateActionSet(@RequestBody String json, Model model) {
        ActionSet actionSet = JSON.parseObject(json, ActionSet.class);
        boolean isSuccess = spiderService.updateActionSet(actionSet);
        if (isSuccess) {
            return new JSONResult<>(isSuccess);
        } else {
            return JSONResult.newErrorResult(-1, "更新失败,没有找到对应的ACTION");
        }
    }


    @RequestMapping(value = "/spider/actionClass/list", method = RequestMethod.GET)
    @ResponseBody
    public Object updateActionSet() {
        List<String> list = spiderService.getActionClassList();
        return list;
    }


    @RequestMapping(value = "/spider/check", method = RequestMethod.POST)
    public Object updateActionSet(@RequestParam String url, @RequestParam String crawleType, Model model) {
        SmartSpider smartSpider = new SmartSpider();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Optional<SpiderResult> crawl = smartSpider.crawl(url, crawleType);
        if (crawl.isPresent()) {
            try {
                if (crawleType.equals("article")) {

                    Article article = SmartSpiderConverter.convertToArticle(crawl.get());

                    model.addAttribute("data", JSON.toJSONString(article));
                } else if (crawleType.equals("articleList")) {
                    NewScheduleUrls newScheduleUrls = SmartSpiderConverter.convertToNewSchedulerUrls(-1L, crawl.get());
                    SmartSpiderConverter.convertToNewSchedulerUrls(-1L, crawl.get());
                    model.addAttribute("data", JsonFormatter.jsonFormatter(JSON.toJSONString(newScheduleUrls)));
                } else if (crawleType.equals("articleDynamicData")) {
                    DynamicData dynamicData = SmartSpiderConverter.convertToDynamicData(crawl.get());
                    model.addAttribute("data", JSON.toJSONString(dynamicData));
                } else {
                    crawl.get().executeContext.set("domDocument", "");
                    crawl.get().executeContext.set("pageSource", "");
                    model.addAttribute("data", JsonFormatter.jsonFormatter(JSON.toJSONString(crawl.get().executeContext.getContextMap())));
                }
            } catch (Exception e) {
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                e.printStackTrace(printWriter);
                model.addAttribute("data", JsonFormatter.jsonFormatter(JSON.toJSONString(stringWriter.toString())));
            }

        } else {
            model.addAttribute("data", JSON.toJSONString("解析失败,请检查规则"));
        }
        return "spider/check";
    }

    @RequestMapping(value = "/spider/check", method = RequestMethod.GET)
    public Object updateActionSet(Model model) {
        return "spider/check";
    }


    @RequestMapping(value = "/spider/newsadd", method = RequestMethod.GET)
    public String addNews(Model model) {
        return "spider/newsadd";
    }

    @RequestMapping(value = "/spider/newsAdd", method = RequestMethod.POST)
    @ResponseBody
    public Object newsAdd(News news, Model model) {
        if (news.getNewsname().length() > 0 && news.getNewsurl().length() > 0) {
            try {
                spiderService.addNews(news);
//                return new JSONResult<>("", "add success", true); 
                return "true";
            } catch (Exception e) {
                e.printStackTrace();
                return JSONResult.newErrorResult(-1, "系统错误");
            }
        }
        return JSONResult.newErrorResult(-1, "参数不合法");
    }

    @RequestMapping(value = "/spider/news")
    public String newsList(Model model) {
        List<News> newList = spiderService.getNewsList();
        model.addAttribute("newsList", newList);
        return "spider/news";
    }

    @RequestMapping(value = "/spider/delnews")
    public String delnews(@RequestParam("_id") String _id) {
        if (_id.length() > 0) {
            spiderService.delNews(_id);
        }
        return "redirect:/spider/news";
    }

    @RequestMapping(value = "/spider/updatenews")
    public String getNewsByID(@RequestParam("_id") String _id, Model model, HttpServletRequest request) {
        News news = spiderService.getNewsByID(_id);
        model.addAttribute("id", news.getId());
        model.addAttribute("newsname", news.getNewsname());
        model.addAttribute("newsurl", news.getNewsurl());
        model.addAttribute("period", news.getPeriod());
        request.setAttribute("news", news);

        return "spider/newsupdate";
    }

    @RequestMapping(value = "/spider/updatenewssave")
    @ResponseBody
    public Object getNewsByID(HttpServletRequest request, Model model) {
        String id = request.getParameter("id");
        String newsname = request.getParameter("newsname");
        String newsurl = request.getParameter("newsurl");
        int period = Integer.parseInt(request.getParameter("period"));
        News news = new News();
        news.setId(id);
        news.setNewsname(newsname);
        news.setNewsurl(newsurl);
        news.setPeriod(period);
        if (spiderService.updateNews(news)) {
            return "true";
        }
        return null;
    }

}

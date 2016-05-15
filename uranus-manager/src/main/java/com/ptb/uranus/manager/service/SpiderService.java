package com.ptb.uranus.manager.service;

import com.ptb.uranus.manager.bean.ActionSet;
import com.ptb.uranus.manager.bean.News;
import com.ptb.uranus.manager.dao.SpiderDao;
import com.ptb.uranus.spider.smart.action.Action;
import com.ptb.uranus.spider.smart.utils.StringUtil;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by eric on 16/3/28.
 */
@Component
public class SpiderService {
    @Autowired
    SpiderDao spiderDao;
    private List<String> actionClassList;

    public List<ActionSet> getSpiderList() {
        return spiderDao.getSpiderList();
    }

    public void addActionSet(ActionSet actionSet) {
        spiderDao.addActionSet(actionSet);

    }

    public void delActionSetById(String oid) {
        spiderDao.delActionSet(oid);
    }

    public ActionSet getActionSet(String oid) {
        return spiderDao.getActionSet(oid);
    }

    public List<String> getActionFieldsByClassName(String className) throws ClassNotFoundException {
        Class<?> aClass = Class.forName(className);
        Method[] allDeclaredMethods = ReflectionUtils.getAllDeclaredMethods(aClass);
        List<String> set = Arrays.asList(allDeclaredMethods).stream().filter(method -> {
            return method.getName().startsWith("set");

        }).map(method -> StringUtil.toLowerCaseFirstOne(method.getName().replaceFirst("set", ""))
        ).collect(Collectors.toList());
        return set;
    }

    public boolean addAction(HashMap hashMap) {
        return spiderDao.addAction(hashMap);
    }

    public boolean updateActionSet(ActionSet actionSet) {
        return spiderDao.updateActionSet(actionSet);
    }

    public void cloneActionSet(String oid) {
        spiderDao.clone(oid);
    }

    public List<String> getActionClassList() {
        Reflections reflections = new Reflections("com.ptb.uranus.spider.smart.action");
        Set<Class<? extends Action>> subTypesOf = reflections.getSubTypesOf(Action.class);
        return subTypesOf.stream().map(a -> a.getName()).collect(Collectors.toList());
    }

    public void addNews(News news) {
        spiderDao.addNews(news);
    }

    public List<News> getNewsList() {
        return spiderDao.getNewsList();
    }

    public void delNews(String _id) {
        spiderDao.delNews(_id);
    }

    public News getNewsByID(String _id) {
        return spiderDao.getNewsByID(_id);
    }

    public boolean updateNews(News news) {
        return spiderDao.updateNews(news);
    }
}

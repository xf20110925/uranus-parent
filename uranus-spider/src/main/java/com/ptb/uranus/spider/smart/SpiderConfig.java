package com.ptb.uranus.spider.smart;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.ptb.uranus.spider.smart.action.Action;
import com.ptb.uranus.spider.smart.action.ParamConfig;
import com.ptb.uranus.spider.smart.utils.MongoDBUtil;
import org.bson.Document;
import org.json.JSONException;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by eric on 16/3/23.
 */
public class SpiderConfig {
    static List<ActionSet> actionSets = new ArrayList<>();

    private SpiderConfig() {
    }

    public static List<ActionSet> getInstance() throws JSONException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        actionSets.clear();
        MongoCollection<Document> coll = MongoDBUtil.instance.getDefaultColl();
        FindIterable<Document> documents = coll.find();
        for (Document document : documents) {
            try {
                JSONObject jsonObj = JSON.parseObject(document.toJson());
                ActionSet actionSet = new ActionSet();
                actionSet.setUrlRegex(jsonObj.getString(ParamConfig.ACTIONSET_UREGEX));
                actionSet.setCrawleType(jsonObj.getString(ParamConfig.ACTIONSET_CRAWTYPE));
                actionSet.setName(jsonObj.getString(ParamConfig.ACTION_NAME));
                JSONArray jsonArr = jsonObj.getJSONArray(ParamConfig.ACTIONSET_ACTIOS);


            List<Action> actionList = new ArrayList<>();
            if (jsonArr != null && jsonArr.size() > 0) {

                for (int i = 0; i < jsonArr.size(); i++) {
                    JSONObject json = jsonArr.getJSONObject(i);
                    String className = json.getString(ParamConfig.ACTION_CLASSNAME);
                    Action action = (Action) Class.forName(className).getConstructor(JSONObject.class).newInstance(json);
                    actionList.add(action);
                }
                Collections.sort(actionList);
            }

                actionSet.setActions(actionList);
                actionSets.add(actionSet);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        return actionSets;
    }

}

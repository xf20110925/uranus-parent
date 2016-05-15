package com.ptb.uranus.spider.court;

import java.util.List;

/**
 * Created by eric on 16/4/14.
 */
public class CourtCase {

    String docId;  //案件ID
    String caseName; //案件名称
    String caseNumber; //案件编号
    String caseCourt;  //受理法院
    String caseType;   //案件类型
    String trialRound;  //审判阶段
    String trialTime;   //审判时间
    String readNum;     //阅读数
    String caseContent;  //案件内容
    String caseTitle;   //案件标题
    String postTime;    //发布时间

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getCaseCourt() {
        return caseCourt;
    }

    public void setCaseCourt(String caseCourt) {
        this.caseCourt = caseCourt;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getTrialRound() {
        return trialRound;
    }

    public void setTrialRound(String trialRound) {
        this.trialRound = trialRound;
    }

    public String getTrialTime() {
        return trialTime;
    }

    public void setTrialTime(String trialTime) {
        this.trialTime = trialTime;
    }

    public String getReadNum() {
        return readNum;
    }

    public void setReadNum(String readNum) {
        this.readNum = readNum;
    }

    public String getCaseContent() {
        return caseContent;
    }

    public void setCaseContent(String caseContent) {
        this.caseContent = caseContent;
    }

    public String getCaseTitle() {
        return caseTitle;
    }

    public void setCaseTitle(String caseTitle) {
        this.caseTitle = caseTitle;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }
}

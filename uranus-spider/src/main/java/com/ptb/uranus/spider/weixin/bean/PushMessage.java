package com.ptb.uranus.spider.weixin.bean;

/**
 * Created by eric on 15/11/11.
 * 通过一次布的微信文章列表能够获得的信息
 */
public class PushMessage {
    private CommonMsgInfo comm_msg_info;
    private ExtMsgInfo app_msg_ext_info;

    /**
     * Gets comm msg info.
     *
     * @return the comm msg info
     */
    public CommonMsgInfo getComm_msg_info() {
        return comm_msg_info;
    }

    /**
     * Sets comm msg info.
     *
     * @param comm_msg_info the comm msg info
     */
    public void setComm_msg_info(CommonMsgInfo comm_msg_info) {
        this.comm_msg_info = comm_msg_info;
    }

    /**
     * Gets app msg ext info.
     *
     * @return the app msg ext info
     */
    public ExtMsgInfo getApp_msg_ext_info() {
        return app_msg_ext_info;
    }

    /**
     * Sets app msg ext info.
     *
     * @param app_msg_ext_info the app msg ext info
     */
    public void setApp_msg_ext_info(ExtMsgInfo app_msg_ext_info) {
        this.app_msg_ext_info = app_msg_ext_info;
    }
}

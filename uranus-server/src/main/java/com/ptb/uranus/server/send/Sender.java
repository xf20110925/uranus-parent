package com.ptb.uranus.server.send;

import com.ptb.uranus.server.send.entity.article.BasicArticleDynamic;
import com.ptb.uranus.server.send.entity.article.BasicArticleStatic;
import com.ptb.uranus.server.send.entity.article.WeixinArticleStatic;
import com.ptb.uranus.server.send.entity.media.BasicMediaDynamic;
import com.ptb.uranus.server.send.entity.media.BasicMediaStatic;
import com.ptb.uranus.server.send.entity.media.WeixinMediaStatic;

/**
 * Created by watson zhang on 16/4/25.
 */
public interface Sender {
    void sendArticleStatic(BasicArticleStatic article);
    void sendArticleDynamic(BasicArticleDynamic basicArticleDynamic);
    void sendMediaStatic(BasicMediaStatic basicMediaStatic);
    void sendMediaDynamic(BasicMediaDynamic basicMediaDynamic);
}

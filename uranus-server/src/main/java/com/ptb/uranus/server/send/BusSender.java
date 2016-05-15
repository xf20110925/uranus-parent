package com.ptb.uranus.server.send;

import com.ptb.gaia.bus.Bus;
import com.ptb.gaia.bus.message.Message;
import com.ptb.uranus.server.send.entity.article.BasicArticleDynamic;
import com.ptb.uranus.server.send.entity.article.BasicArticleStatic;
import com.ptb.uranus.server.send.entity.article.WeixinArticleStatic;
import com.ptb.uranus.server.send.entity.media.BasicMediaDynamic;
import com.ptb.uranus.server.send.entity.media.BasicMediaStatic;
import com.ptb.uranus.server.send.entity.media.WeixinMediaStatic;

/**
 * Created by watson zhang on 16/4/25.
 */
public class BusSender implements Sender {
    private final Bus bus;

    public BusSender(Bus bus) {
        this.bus = bus;
    }

    @Override
    public void sendArticleStatic(BasicArticleStatic article) {
        bus.send(new Message("uranus-server", "article_basic_info", article.getPlat(), "1.0.0", article));
    }

    @Override
    public void sendArticleDynamic(BasicArticleDynamic basicArticleDynamic) {
        bus.send(new Message("uranus-server", "article_spread_info", basicArticleDynamic.getPlat(), "1.0.0", basicArticleDynamic));
    }

    @Override
    public void sendMediaStatic(BasicMediaStatic basicMediaStatic){
        bus.send(new Message("uranus-server", "media_basic_info", basicMediaStatic.getPlat(), "1.0.0", basicMediaStatic));
    }

    @Override
    public void sendMediaDynamic(BasicMediaDynamic basicMediaDynamic) {
        bus.send(new Message("uranus-server", "media_basic_info", basicMediaDynamic.getPlat(), "1.0.0", basicMediaDynamic));
    }
}

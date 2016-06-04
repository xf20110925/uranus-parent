package com.ptb.uranus.server.bayou;

import com.ptb.gaia.bus.kafka.KafkaBus;
import com.ptb.uranus.server.third.weixin.BayouWeixinSync;
import com.ptb.uranus.server.third.weixin.entity.IdRecord;
import com.ptb.uranus.server.third.weixin.util.IdRecordUtil;
import com.ptb.uranus.server.send.BusSender;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

/**
 * Created by xuefeng on 2016/5/24.
 */
public class BayouWeixinTest {
    BayouWeixinSync bayouWeixinSync = new BayouWeixinSync(new BusSender(new KafkaBus()));

    @Test
    public void getIdRecordTest() {
        Optional<IdRecord> idRecord = IdRecordUtil.getIdRecord();
        Assert.assertTrue(idRecord.isPresent());
    }

    @Test
    public void seriableIdRecordTest() {
        IdRecordUtil.getIdRecord();
        IdRecordUtil.syncMediaId(10);
        IdRecordUtil.syncStaticArticleId(20);
        IdRecordUtil.syncDynamicArticleId(30);
        Optional<IdRecord> idRecordOpt = IdRecordUtil.getIdRecord();
        idRecordOpt.ifPresent(idRecord -> {
            Assert.assertEquals(idRecord.getMediaId(), 10);
            Assert.assertEquals(idRecord.getStaticArticleId(), 20);
            Assert.assertEquals(idRecord.getDynamicArticleId(), 30);
        });
    }

}

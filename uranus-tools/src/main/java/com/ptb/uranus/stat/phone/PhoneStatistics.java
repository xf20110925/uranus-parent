package com.ptb.uranus.stat.phone;

import com.ptb.gaia.bus.Bus;
import com.ptb.gaia.bus.kafka.KafkaBus;
import com.ptb.gaia.bus.message.Message;
import com.ptb.gaia.bus.message.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by eric on 16/2/22.
 */
public class PhoneStatistics {
    static Logger logger = LoggerFactory.getLogger(PhoneStatistics.class);
    public static final int PhoneIdleCount = 102000000;
    public static final int PhoneCrawleReadNumCount = 102000001;
    public static final int PhoneCrawleNewArticleCount = 102000002;

    public static void main(String[] args) throws InterruptedException {
        Bus bus = new KafkaBus();
        final long startTime = System.currentTimeMillis();
        bus.addConsumerTopic("phone-monitor");
        final boolean[] isRun = {true};
        final AtomicLong idleCount = new AtomicLong();
        final AtomicLong readPageCount = new AtomicLong();
        final AtomicLong listPageCount = new AtomicLong();

        MessageListener messageListener = new MessageListener() {

            public void receive(Bus bus, Message message) {
                switch (message.getType()) {
                    case PhoneCrawleNewArticleCount:
                        listPageCount.incrementAndGet();
                        break;
                    case PhoneCrawleReadNumCount:
                        readPageCount.incrementAndGet();
                        break;
                    case PhoneIdleCount:
                        idleCount.incrementAndGet();
                        break;
                }
                if (message.getTimestamp() > startTime) {
                    isRun[0] = false;
                }
            }

            public boolean match(Message message) {
                return true;
            }

            @Override
            public String toString() {
                return "$classname{" +
                        "idleCount=" + idleCount +
                        ", readPageCount=" + readPageCount +
                        ", listPageCount=" + listPageCount +
                        '}';
            }
        };

        bus.addRecvMessageListener(messageListener);
        bus.start(false, 1);
        while (isRun[0]) {
            Thread.sleep(1000);
            logger.info("正在进行统计请稍后...");
        }
        logger.info("统计已经结束,正在写入数据库...");
        bus.showdown();

        JdbcTemplate jdbcTemplate = JdbcTemplateUtils.jdbcTemplate();

        jdbcTemplate.update("insert into phone_task_info(time,phone_id,platform_id,result_add_count," +
                "article_add_count,empty_task_count) VALUE(?,?,?,?,?,?)", new Timestamp(startTime), 0, 1, readPageCount.get(), listPageCount.get(), idleCount.get());
        System.out.println(String.format("统计结果如下:\n" +
                "idleCount:%d\nreadPageCount:%d\narticleListPage:%d", idleCount.get(), readPageCount.get(), listPageCount.get()));
        logger.info("统计完毕,并写库结束");
    }
}

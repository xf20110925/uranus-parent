package com.ptb.uranus.scheduler;

import org.apache.log4j.Logger;

import java.util.TimerTask;

/**
 * Created by eric on 16/4/22.
 */
public class TestTask extends TimerTask {
    static Logger logger = Logger.getLogger(TestTask.class);

    @Override
    public void run() {

        TestTask testTask = new TestTask();
        testTask.run();
        testTask.cancel();
    }
}

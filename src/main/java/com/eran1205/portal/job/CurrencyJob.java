package com.eran1205.portal.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CurrencyJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println(String.format("Hello Job, time: " + new Date()));

    }
}

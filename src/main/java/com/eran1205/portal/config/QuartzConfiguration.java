package com.eran1205.portal.config;

import com.eran1205.portal.job.CurrencyJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

@Configuration
public class QuartzConfiguration {

    @Autowired
    private @Value("${org.quartz.tasks}") boolean enableQuartzTasks;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public SchedulerFactoryBean quartzSchedule() {
        if(enableQuartzTasks) {

            SchedulerFactoryBean quartzScheduler = new SchedulerFactoryBean();
            quartzScheduler.setQuartzProperties(quartzProperties());
            quartzScheduler.setDataSource(dataSource);
            quartzScheduler.setTransactionManager(transactionManager);
            quartzScheduler.setOverwriteExistingJobs(true);

            // Custom job factory of spring with DI support for @Autowired
            AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
            jobFactory.setApplicationContext(applicationContext);
            quartzScheduler.setJobFactory(jobFactory);

            ArrayList<Trigger> triggers = new ArrayList<Trigger>();

            triggers.add(processCurrencyUpdateJobTrigger().getObject());
            Trigger[] triggersArray = triggers.toArray(new Trigger[triggers.size()]);
            quartzScheduler.setTriggers(triggersArray);
            return quartzScheduler;
        } else {
            // default quartzScheduler so that running fuse from localhost doesn't throw errors
            return new SchedulerFactoryBean();
        }
    }

    /**
     * Set CurrencyJob class
     * @return JobDetailFactoryBean
     */
    @Bean
    public JobDetailFactoryBean processCurrencyUpdateJob() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(CurrencyJob.class);
        jobDetailFactory.setDurability(true);
        return jobDetailFactory;
    }

    /**
     * Set CurrencyJob cron trigger
     * @return CronTriggerFactoryBean
     */
    @Bean
    public CronTriggerFactoryBean processCurrencyUpdateJobTrigger() {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setJobDetail(processCurrencyUpdateJob().getObject());
        // cron schedule configured to run once every 5 seconds
        cronTriggerFactoryBean.setCronExpression("0/2 * * * * ?");
        return cronTriggerFactoryBean;
    }

    /**
     * Set Quartz properties on the factory bean
     * @return Properties
     */
    @Bean
    public Properties quartzProperties() {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        Properties properties;

        try {
            propertiesFactoryBean.afterPropertiesSet();
            properties = propertiesFactoryBean.getObject();
        }
        catch (IOException e) {
            throw new RuntimeException("Unable to load quartz.properties", e);
        }

        return properties;
    }
}

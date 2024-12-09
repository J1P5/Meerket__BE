package org.j1p5.infrastructure.quartz.config;

import java.util.Properties;
import javax.sql.DataSource;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzConfig {

    @Autowired private AutowireCapableBeanFactory beanFactory;

    @Autowired private DataSource dataSource; // spring.datasource 설정으로 등록된 DataSource

    @Bean
    public JobFactory jobFactory() {
        return new AutowiringSpringBeanJobFactory(beanFactory);
    }

    @Bean
    @ConfigurationProperties(prefix = "org.quartz")
    public QuartzProperties quartzProperties() {
        return new QuartzProperties();
    }

    @Bean
    public Properties quartzPropertiesBean(QuartzProperties quartzProperties) {
        Properties properties = new Properties();
        properties.setProperty(
                "org.quartz.scheduler.instanceName",
                quartzProperties.getScheduler().getInstanceName());
        properties.setProperty(
                "org.quartz.scheduler.instanceId", quartzProperties.getScheduler().getInstanceId());

        properties.setProperty(
                "org.quartz.threadPool.class", quartzProperties.getThreadPool().getClassName());
        properties.setProperty(
                "org.quartz.threadPool.threadCount",
                String.valueOf(quartzProperties.getThreadPool().getThreadCount()));

        properties.setProperty(
                "org.quartz.jobStore.class", quartzProperties.getJobStore().getClassName());
        properties.setProperty(
                "org.quartz.jobStore.driverDelegateClass",
                quartzProperties.getJobStore().getDriverDelegateClass());
        properties.setProperty(
                "org.quartz.jobStore.dataSource", quartzProperties.getJobStore().getDataSource());

        quartzProperties
                .getDataSource()
                .forEach(
                        (key, ds) -> {
                            String prefix = "org.quartz.dataSource." + key;
                            properties.setProperty(prefix + ".driver", ds.getDriver());
                            properties.setProperty(prefix + ".URL", ds.getURL());
                            properties.setProperty(prefix + ".user", ds.getUser());
                            properties.setProperty(prefix + ".password", ds.getPassword());
                            properties.setProperty(prefix + ".provider", "hikaricp");
                        });

        return properties;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(Properties quartzPropertiesBean) {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setJobFactory(jobFactory());
        factory.setDataSource(dataSource);
        factory.setQuartzProperties(quartzPropertiesBean);
        return factory;
    }

    @Bean
    public Scheduler scheduler(SchedulerFactoryBean schedulerFactoryBean)
            throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.start();
        return scheduler;
    }
}

package org.j1p5.infrastructure.quartz.config;

import java.util.Map;

public class QuartzProperties {

    private SchedulerProperties scheduler;
    private ThreadPoolProperties threadPool;
    private JobStoreProperties jobStore;
    private Map<String, DataSourceProperties> dataSource;

    public SchedulerProperties getScheduler() {
        return scheduler;
    }

    public void setScheduler(SchedulerProperties scheduler) {
        this.scheduler = scheduler;
    }

    public ThreadPoolProperties getThreadPool() {
        return threadPool;
    }

    public void setThreadPool(ThreadPoolProperties threadPool) {
        this.threadPool = threadPool;
    }

    public JobStoreProperties getJobStore() {
        return jobStore;
    }

    public void setJobStore(JobStoreProperties jobStore) {
        this.jobStore = jobStore;
    }

    public Map<String, DataSourceProperties> getDataSource() {
        return dataSource;
    }

    public void setDataSource(Map<String, DataSourceProperties> dataSource) {
        this.dataSource = dataSource;
    }

    public static class SchedulerProperties {
        private String instanceName;
        private String instanceId;

        public String getInstanceName() {
            return instanceName;
        }

        public void setInstanceName(String instanceName) {
            this.instanceName = instanceName;
        }

        public String getInstanceId() {
            return instanceId;
        }

        public void setInstanceId(String instanceId) {
            this.instanceId = instanceId;
        }
    }

    public static class ThreadPoolProperties {
        private String className;
        private int threadCount;

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public int getThreadCount() {
            return threadCount;
        }

        public void setThreadCount(int threadCount) {
            this.threadCount = threadCount;
        }
    }

    public static class JobStoreProperties {
        private String className;
        private String driverDelegateClass;
        private String dataSource;

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getDriverDelegateClass() {
            return driverDelegateClass;
        }

        public void setDriverDelegateClass(String driverDelegateClass) {
            this.driverDelegateClass = driverDelegateClass;
        }

        public String getDataSource() {
            return dataSource;
        }

        public void setDataSource(String dataSource) {
            this.dataSource = dataSource;
        }
    }

    public static class DataSourceProperties {
        private String driver;
        private String URL;
        private String user;
        private String password;

        public String getDriver() {
            return driver;
        }

        public void setDriver(String driver) {
            this.driver = driver;
        }

        public String getURL() {
            return URL;
        }

        public void setURL(String URL) {
            this.URL = URL;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}

package org.hswebframework.web.task;

/**
 * @author zhouhao
 * @since 3.0.0-RC
 */
public interface TaskRunnableFactory {
    TaskRunnable create(JobDetail detail);
}

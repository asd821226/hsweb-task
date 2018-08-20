package org.hswebframework.web.task;

/**
 * 任务执行者
 */
public interface TaskRunnable {

    String getId();

    TaskOperationResult run(TaskExecutionContext context);

}
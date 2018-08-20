package org.hswebframework.web.task;

import org.hswebframework.web.task.enums.TaskExecuteStatus;

import java.util.function.Consumer;

/**
 * 任务执行信息,一个{@link JobDetail}会被创建为一个Task
 *
 * @author zhouhao
 * @since 3.0.0-RC
 */
public interface Task {

    /**
     * @return 任务ID
     */
    String getId();

    /**
     * @return 任务详情ID
     */
    String getJobId();

    /**
     * @return 任务详情
     */
    JobDetail getJob();

    /**
     * @return 上一次执行时间
     */
    long getLastExecuteTime();

    /**
     * @return 任务创建时间
     */
    long getCreateTime();

    /**
     * @return 任务执行状态
     */
    TaskExecuteStatus getStatus();

    /**
     * @return 任务创建人
     */
    String getCreator();

    /**
     * @return 任务超时时间
     */
    long getTimeout();

    /**
     * 执行任务并同步返回执行结果
     *
     * @param context 任务执行上下文
     * @return 任务执行结果
     * @see this#start(TaskExecutionContext, Consumer)
     */
    TaskOperationResult execute(TaskExecutionContext context);

    /**
     * 启动任务
     *
     * @param context   任务执行上下文
     * @param onExecute 任务执行完成后回调
     * @return 任务执行ID
     * @see org.hswebframework.web.task.events.TaskExecuteBeforeEvent
     * @see org.hswebframework.web.task.events.TaskExecuteAfterEvent
     */
    String start(TaskExecutionContext context, Consumer<TaskOperationResult> onExecute);


}

package org.hswebframework.web.task.local;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hswebframework.web.task.*;
import org.hswebframework.web.task.enums.TaskExecuteStatus;
import org.hswebframework.web.task.events.TaskExecuteAfterEvent;
import org.hswebframework.web.task.events.TaskExecuteBeforeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

/**
 * @author zhouhao
 * @since 3.0.0-RC
 */
public class AsynTask extends AbstractTask {
    private JobRepository repository;

    private ExecutorService executorService;

    private Consumer<TaskRunnable> jobSubmiter;

    private Map<String, TaskRunnableInfo> jobRepository = new HashMap<>();

    private TaskRunnableFactory runnableFactory;

    private Consumer<Object> eventPublisher;

    @Override
    public JobDetail getJob() {
        return repository.findById(getJobId());
    }

    @Override
    public String start(TaskExecutionContext context, Consumer<TaskOperationResult> onExecute) {
        TaskRunnable runnable = runnableFactory.create(getJob());

        TaskRunnable proxy = new TaskRunnable() {
            @Override
            public String getId() {
                return runnable.getId();
            }

            @Override
            public TaskOperationResult run(TaskExecutionContext context) {
                TaskOperationResult result = null;
                try {
                    eventPublisher.accept(new TaskExecuteBeforeEvent(AsynTask.this, getId()));
                    setStatus(TaskExecuteStatus.running);
                    result = runnable.run(context);
                    setStatus(result.getStatus());
                } catch (Exception e) {
                    result = new TaskOperationResult();
                    result.setCause(e);
                    result.setExecutionId(getId());
                    result.setJobId(getJobId());
                    result.setTaskId(AsynTask.this.getId());
                    result.setSuccess(false);
                    result.setStatus(TaskExecuteStatus.failed);
                } finally {
                    onExecute.accept(result);
                    eventPublisher.accept(new TaskExecuteAfterEvent(AsynTask.this, getId(), result));
                    jobRepository.remove(runnable.getId());
                }
                return result;
            }
        };
        jobSubmiter.accept(proxy);
        jobRepository.put(runnable.getId(), new TaskRunnableInfo(runnable, context));
        return runnable.getId();
    }

    @AllArgsConstructor
    @Getter
    class TaskRunnableInfo {
        TaskRunnable         runnable;
        TaskExecutionContext context;
    }
}

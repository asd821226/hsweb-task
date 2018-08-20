package org.hswebframework.web.task.local;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.hswebframework.web.task.Task;
import org.hswebframework.web.task.TaskExecutionContext;
import org.hswebframework.web.task.TaskOperationResult;
import org.hswebframework.web.task.enums.TaskExecuteStatus;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author zhouhao
 * @since 3.0.0-RC
 */
@Getter
@Setter
public abstract class AbstractTask implements Task {
    private String id;

    private String key;

    private int version;

    private String jobId;

    private volatile long lastExecuteTime;

    private volatile long createTime;

    private volatile TaskExecuteStatus status;

    private String creator;

    private long timeout;

    @Override
    public TaskOperationResult execute(TaskExecutionContext context) {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<TaskOperationResult> reference = new AtomicReference<>();
        String id = start(context, result -> {
            reference.set(result);
            latch.countDown();
        });
        try {
            boolean success = latch.await(getTimeout(), TimeUnit.MILLISECONDS);
            if (!success) {
                TaskOperationResult result = new TaskOperationResult();
                result.setExecutionId(id);
                result.setJobId(getJobId());
                result.setTaskId(getId());
                result.setStatus(TaskExecuteStatus.timeout);
                result.setSuccess(false);
                result.setCause(new TimeoutException("等待任务执行完成超时!"));
                return result;
            }
            return reference.get();
        } catch (@SuppressWarnings("all") InterruptedException e) {
            TaskOperationResult result = new TaskOperationResult();
            result.setExecutionId(id);
            result.setJobId(getJobId());
            result.setTaskId(getId());
            result.setStatus(TaskExecuteStatus.interrupt);
            result.setSuccess(false);
            result.setCause(e);
            //  Thread.currentThread().interrupt();
            return result;
        }
    }
}

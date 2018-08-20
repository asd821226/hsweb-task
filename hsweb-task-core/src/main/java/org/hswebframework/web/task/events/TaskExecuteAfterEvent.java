package org.hswebframework.web.task.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hswebframework.web.task.Task;
import org.hswebframework.web.task.TaskOperationResult;

@AllArgsConstructor
@Getter
public class TaskExecuteAfterEvent {
    private Task task;

    private String executionId;

    private TaskOperationResult result;

}

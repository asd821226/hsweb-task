package org.hswebframework.web.task;

import java.util.Map;

/**
 * @author zhouhao
 * @since 3.0.0-RC
 */
public interface TaskExecutionContext {
    Task getTask();

    Map<String, Object> getParameters();

    void assertRunning();

    void process(double percent);

}

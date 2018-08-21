package org.hswebframework.web.task;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 任务详情,用于配置任务信息以及执行策略等
 *
 * @author zhouhao
 * @since 3.0.0-RC
 */
@Getter
@Setter
@ToString
public class JobDetail {

    /**
     * 任务id,全局唯一
     */
    private String id;

    /**
     * 任务标识,如: my-job
     */
    private String key;

    /**
     * 任务版本,任务支持多个版本
     */
    private int version;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 任务描述
     */
    private String description;

    /**
     * 任务类型,该字段用于content组合用于不同类型任务的支持,如: java-method,script,jar 等
     */
    private String taskType;

    /**
     * 任务内容,{@link this#taskType}字段不同,配置格式也不同,具体格式由{@link TaskRunnableFactory}进行处理
     *
     * @see TaskRunnableFactory
     */
    private String content;

    /**
     * 任务执行的超时时间
     *
     * @see org.hswebframework.web.task.enums.TaskExecuteStatus
     */
    private long executeTimeOut;

    /**
     * 重试时间,任务失败后重试次数,-1时为一直重试
     */
    private long retryTimes;

    /**
     * 每次重试的间隔
     */
    private long retryInterval;

    /**
     * 是否支持并行执行,如果不支持,同一个任务将顺序执行
     */
    private boolean parallel;

}

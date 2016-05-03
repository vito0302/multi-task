package com.baidu.unbiz.multitask.task;

import java.util.List;

import com.baidu.unbiz.multitask.common.TaskPair;
import com.baidu.unbiz.multitask.forkjoin.ForkJoin;
import com.baidu.unbiz.multitask.policy.ExecutePolicy;
import com.baidu.unbiz.multitask.task.thread.MultiResult;

public interface ParallelExePool {

    String TASK_PAIRS = "taskPairs";

    MultiResult submit(List<TaskPair> taskPairs);

    MultiResult submit(TaskPair... taskPairs);

    MultiResult submit(ExecutePolicy policy, TaskPair... taskPairs);

    <PARAM, RESULT> RESULT submit(TaskPair taskPair, ForkJoin<PARAM, RESULT> forkJoin);

    <PARAM, RESULT> RESULT submit(ExecutePolicy policy, TaskPair taskPair, ForkJoin<PARAM, RESULT> forkJoin);
}
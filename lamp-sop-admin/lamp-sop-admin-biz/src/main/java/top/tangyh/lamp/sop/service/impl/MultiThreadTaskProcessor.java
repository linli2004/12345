package top.tangyh.lamp.sop.service.impl;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 多线程任务处理类
 * @author 六如
 */
public class MultiThreadTaskProcessor {

    /**
     * 使用N个线程处理任务，每个线程处理M个List
     *
     * @param tasks           任务列表的列表
     * @param numberOfThreads 线程数量N
     */
    public void processTasks(List<List<Task>> tasks, int numberOfThreads) {
        // 创建固定大小的线程池
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        try {
            // 将任务分配给线程,分页
            int listsPerThread = (int) Math.ceil((double) tasks.size() / numberOfThreads);

            for (int i = 0; i < numberOfThreads && i * listsPerThread < tasks.size(); i++) {
                int startIndex = i * listsPerThread;
                int endIndex = Math.min(startIndex + listsPerThread, tasks.size());

                // 提取当前线程需要处理的lists
                List<List<Task>> threadTasks = tasks.subList(startIndex, endIndex);

                // 提交任务到线程池
                executor.submit(new TaskWorker(threadTasks, i));
            }

            // 关闭线程池并等待所有任务完成
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.HOURS);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("任务处理被中断");
        }
    }

    /**
     * 工作线程类
     */
    @Slf4j
    private static class TaskWorker implements Runnable {
        private final List<List<Task>> taskLists;
        private final int workerId;

        TaskWorker(List<List<Task>> taskLists, int workerId) {
            this.taskLists = taskLists;
            this.workerId = workerId;
        }

        @Override
        public void run() {
            log.info("线程 {} 开始处理 {} 个列表", workerId, taskLists.size());

            // 处理分配给该线程的所有lists
            for (int i = 0; i < taskLists.size(); i++) {
                List<Task> list = taskLists.get(i);
                log.info("线程 {} 正在处理第 {} 个列表，包含 {} 个任务", workerId, i, list.size());

                // 处理当前list中的所有任务
                for (Task task : list) {
                    processTask(task);
                }
            }

            log.info("线程 {} 完成任务处理", workerId);
        }

        /**
         * 处理单个任务
         */
        private void processTask(Task task) {
            // 实际的任务处理逻辑
            task.execute();
        }
    }

    /**
     * 任务接口
     */
    public interface Task {
        void execute();
    }
}

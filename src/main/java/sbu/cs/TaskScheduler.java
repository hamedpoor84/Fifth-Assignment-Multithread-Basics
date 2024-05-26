package sbu.cs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TaskScheduler
{
    public static class Task implements Runnable
    {
        /*
            ------------------------- You don't need to modify this part of the code -------------------------
         */
        private String taskName;
        private int processingTime;

        public Task(String taskName, int processingTime) {
            this.taskName = taskName;
            this.processingTime = processingTime;
        }

        public String getTaskName() {
            return taskName;
        }

        public void setTaskName(String taskName) {
            this.taskName = taskName;
        }

        public int getProcessingTime() {
            return processingTime;
        }

        public void setProcessingTime(int processingTime) {
            this.processingTime = processingTime;
        }
        /*
            ------------------------- You don't need to modify this part of the code -------------------------
         */

        @Override
        public void run() {
            try {
                Thread.sleep(processingTime);
            } catch (InterruptedException e) {
                System.out.println("Thread was interrupted!");
            }
        }
    }

    public static ArrayList<String> doTasks(ArrayList<Task> tasks)
    {
        ArrayList<String> finishedTasks = new ArrayList<>();
        tasks.sort(Comparator.comparingInt(Task::getProcessingTime).reversed());
        for (Task task : tasks)
        {
            Thread thread = new Thread(task , task.getTaskName());
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finishedTasks.add(task.getTaskName());
        }
        return finishedTasks;
    }

    public static void main(String[] args) {
        // Test your code here
    }
}

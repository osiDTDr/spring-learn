package com.bright.spring.learn.thread;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * stop all thread run in jvm
 *
 * @author zhengyuan
 * @since 2021/02/07
 */
public class ThreadStopManager {

    public static void stopThread() {
        Thread[] threads = getThreads();
        List<Thread> executorThreadsToStop = new ArrayList<>();

        // Iterate over the set of threads
        for (Thread thread : threads) {
            if (thread != null) {
                ClassLoader ccl = thread.getContextClassLoader();
                if (ccl == ThreadStopManager.class.getClassLoader()) {
                    // Don't warn about this thread
                    if (thread == Thread.currentThread()) {
                        continue;
                    }

                    // Skip threads that have already died
                    if (!thread.isAlive()) {
                        continue;
                    }

                    // TimerThread can be stopped safely so treat separately
                    // "java.util.TimerThread" in Sun/Oracle JDK
                    // "java.util.Timer$TimerImpl" in Apache Harmony and in IBM JDK
                    if (thread.getClass().getName().startsWith("java.util.Timer")) {
                        clearReferencesStopTimerThread(thread);
                        continue;
                    }

                    // If the thread has been started via an executor, try
                    // shutting down the executor
                    boolean usingExecutor = false;
                    try {

                        // Runnable wrapped by Thread
                        // "target" in Sun/Oracle JDK
                        // "runnable" in IBM JDK
                        // "action" in Apache Harmony
                        Object target = null;
                        for (String fieldName : new String[]{"target", "runnable", "action"}) {
                            try {
                                Field targetField = thread.getClass().getDeclaredField(fieldName);
                                targetField.setAccessible(true);
                                target = targetField.get(thread);
                                break;
                            } catch (NoSuchFieldException nfe) {
                                continue;
                            }
                        }

                        // "java.util.concurrent" code is in public domain,
                        // so all implementations are similar
                        if (target != null && target.getClass().getCanonicalName() != null &&
                                target.getClass().getCanonicalName().equals(
                                        "java.util.concurrent.ThreadPoolExecutor.Worker")) {
                            Field executorField = target.getClass().getDeclaredField("this$0");
                            executorField.setAccessible(true);
                            Object executor = executorField.get(target);
                            if (executor instanceof ThreadPoolExecutor) {
                                ((ThreadPoolExecutor) executor).shutdownNow();
                                usingExecutor = true;
                            }
                        }
                    } catch (SecurityException | NoSuchFieldException | IllegalArgumentException |
                            IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    if (usingExecutor) {
                        // Executor may take a short time to stop all the
                        // threads. Make a note of threads that should be
                        // stopped and check them at the end of the method.
                        executorThreadsToStop.add(thread);
                    } else {
                        // This method is deprecated and for good reason. This
                        // is very risky code but is the only option at this
                        // point. A *very* good reason for apps to do this
                        // clean-up themselves.
                        thread.interrupt();
                    }
                }
            }
        }

        // If thread stopping is enabled, executor threads should have been
        // stopped above when the executor was shut down but that depends on the
        // thread correctly handling the interrupt. Give all the executor
        // threads a few seconds shutdown and if they are still running
        // Give threads up to 2 seconds to shutdown
        int count = 0;
        for (Thread t : executorThreadsToStop) {
            while (t.isAlive() && count < 100) {
                try {
                    //noinspection BusyWait
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    // Quit the while loop
                    break;
                }
                count++;
            }
            if (t.isAlive()) {
                // This method is deprecated and for good reason. This is
                // very risky code but is the only option at this point.
                // A *very* good reason for apps to do this clean-up
                // themselves.
                t.interrupt();
            }
        }
    }

    private static Thread[] getThreads() {
        // Get the current thread group
        ThreadGroup tg = Thread.currentThread().getThreadGroup();
        // Find the root thread group
        try {
            while (tg.getParent() != null) {
                tg = tg.getParent();
            }
        } catch (SecurityException se) {
            se.printStackTrace();
        }

        int threadCountGuess = tg.activeCount();
        Thread[] threads = new Thread[threadCountGuess];
        int threadCountActual = tg.enumerate(threads);
        // Make sure we don't miss any threads
        while (threadCountActual == threadCountGuess) {
            threadCountGuess *= 2;
            threads = new Thread[threadCountGuess];
            // Note tg.enumerate(Thread[]) silently ignores any threads that
            // can't fit into the array
            threadCountActual = tg.enumerate(threads);
        }

        return threads;
    }

    private static void clearReferencesStopTimerThread(Thread thread) {

        // Need to get references to:
        // in Sun/Oracle JDK:
        // - newTasksMayBeScheduled field (in java.util.TimerThread)
        // - queue field
        // - queue.clear()
        // in IBM JDK, Apache Harmony:
        // - cancel() method (in java.util.Timer$TimerImpl)

        try {

            try {
                Field newTasksMayBeScheduledField =
                        thread.getClass().getDeclaredField("newTasksMayBeScheduled");
                newTasksMayBeScheduledField.setAccessible(true);
                Field queueField = thread.getClass().getDeclaredField("queue");
                queueField.setAccessible(true);

                Object queue = queueField.get(thread);

                Method clearMethod = queue.getClass().getDeclaredMethod("clear");
                clearMethod.setAccessible(true);

                synchronized (queue) {
                    newTasksMayBeScheduledField.setBoolean(thread, false);
                    clearMethod.invoke(queue);
                    queue.notify();  // In case queue was already empty.
                }

            } catch (NoSuchFieldException nfe) {
                Method cancelMethod = thread.getClass().getDeclaredMethod("cancel");
                synchronized (thread) {
                    cancelMethod.setAccessible(true);
                    cancelMethod.invoke(thread);
                }
            }


        } catch (Exception e) {
            // So many things to go wrong above...
        }
    }
}

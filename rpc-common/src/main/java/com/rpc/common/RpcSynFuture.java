package com.rpc.common; /*
 * DESCRIPTION
 *     TODO
 *
 * NOTES
 *    同步future，用于同步rpc的调用->结果
 *
 * MODIFIED    (MM/DD/YY)
 *   bofan     2016/5/16 - Creation
 *
 */

import java.util.concurrent.*;

public class RpcSynFuture<T> implements Future<T>{

    // 一次计数
    CountDownLatch synFlag = new CountDownLatch(1);

    T value = null;

    boolean isCanceled = false;
    boolean isUsed = false;
    public boolean cancel(boolean mayInterruptIfRunning) {
        isCanceled = true;
        synFlag.countDown();
        return true;
    }

    public boolean isCancelled() {

        if (isCanceled == true && synFlag.getCount() == 0) return true;
        return false;
    }

    public boolean isDone() {
        if (isCanceled == false && synFlag.getCount() == 0) return true;
        return  false;
    }

    public T get() throws InterruptedException, ExecutionException {
        synFlag.await();
        isUsed = true;
        return  value;
    }

    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        isUsed = true;
        boolean flag = synFlag.await(timeout,unit);
        if (!flag) throw new TimeoutException();
        return value;
    }

    public boolean hasDone(T val)
    {
        value = val;
        synFlag.countDown();
        return true;
    }

    /* 判断该future是否使用过 */
    public synchronized boolean isUsed()
    {
        return isUsed;
    }
}

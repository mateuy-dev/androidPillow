package com.mateuyabar.android.pillow.util.concurrency;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class FullStackThreadPoolExecutor extends ThreadPoolExecutor {
	Map<Runnable, StackTraceElement[]> originStackTraces = new HashMap<Runnable, StackTraceElement[]>();  

	public FullStackThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
		init();
	}

	public FullStackThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
		init();
	}

	public FullStackThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
		init();
	}

	public FullStackThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		init();
	}
	
	private void init() {
//		setThreadFactory(new FullStackThreadFactory());
	}
	
	@Override
	public void execute(Runnable command) {
		StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
		originStackTraces.put(command, stackTraces);
		super.execute(command);
	}
	
	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		if(t!=null){
			t = new Throwable(t);
			t.setStackTrace(originStackTraces.get(r));
			t.printStackTrace();
		}
		originStackTraces.remove(r);
		
		super.afterExecute(r, t);
	}
	
	
	
//	public static class FullStackThreadFactory implements ThreadFactory{
//		@Override
//		public Thread newThread(Runnable r) {
//			return new FullStackThread(r);
//		}
//	}

}

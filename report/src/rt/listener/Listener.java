package rt.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import rt.util.TUtil;

public class Listener implements ServletContextListener {
	private static Logger log = Logger.getLogger(Listener.class);
	public void contextDestroyed(ServletContextEvent arg0) {
		log.error(TUtil.format("yyyy-MM-dd HH-mm:ss")+"应用被关闭");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		SchedulerFactory factory = new StdSchedulerFactory();
		try {
			Scheduler scheduler = factory.getScheduler();
			scheduler.shutdown();
		} catch (SchedulerException e) {
			log.error("关闭任务工厂失败："+e.getMessage());
		}
	}

	public void contextInitialized(ServletContextEvent arg0) {
		log.info(TUtil.format("yyyy-MM-dd HH-mm:ss")+"任务调度器已经启动");
		SchedulerFactory factory = new StdSchedulerFactory();
//		Scheduler factory;
		try { 
			
			Scheduler scheduler = factory.getScheduler();
			scheduler.start();
		} catch (SchedulerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}

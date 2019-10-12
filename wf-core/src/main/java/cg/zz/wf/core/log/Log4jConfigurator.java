package cg.zz.wf.core.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

import cg.zz.wf.core.WF;
import cg.zz.wf.util.StreamUtil;

/**
 * 
 * Log4j配置类
 * 
 * @author chengang
 *
 */
public final class Log4jConfigurator {
	
	public static final String LOG_RECORD_FOLDER = "/logRecords";
	
	/**
	 * 初始化Log4j的配置
	 * @param configFile - 配置文件路径
	 */
	public static void initConfigurator(String configFile) {
		File propertyFile = new File(configFile);
		if (!propertyFile.exists()) {
			copyDefaultLog4jPropertiyFile(propertyFile);
		}
		
		FileReader fr = null;
		try {
			fr = new FileReader(propertyFile);
			
			Properties properties = new Properties();
			properties.load(fr);
			
			String logFilePath = WF.getLogPath() + WF.getNamespace() + ".log";
			properties.setProperty("log4j.appender.file.File", logFilePath);
			properties.setProperty("log4j.appender.file.DatePattern", "'.'yyyy-MM-dd");
			
			PropertyConfigurator.configure(properties);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			StreamUtil.safeClose(fr);
		}
	}
	
	/**
	 * 读取默认的配置文件并写入到propertyFile中
	 * @param propertyFile - File
	 */
	private static void copyDefaultLog4jPropertiyFile(File propertyFile) {
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			File parentFile = propertyFile.getParentFile();
			parentFile.mkdirs();
			
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			
			String defConnectionPoolConfigFile = "cg/zz/wf/resources/log4jConfig.template";
			is = classLoader.getResourceAsStream(defConnectionPoolConfigFile);
			fos = new FileOutputStream(propertyFile);
			
			byte[] b = new byte[is.available()];
			is.read(b);
			fos.write(b);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			StreamUtil.safeClose(fos);
			StreamUtil.safeClose(is);
		}
	}
	
	private Log4jConfigurator() {
		
	}

}

package cg.zz.wf.core.dal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Properties;

import cg.zz.wf.core.WF;
import cg.zz.wf.util.StreamUtil;

/**
 * 
 * 数据库连接池配置信息
 * 
 * @author chengang
 *
 */
public final class SpatConnectionPoolConfig {
	
	private static final Properties PROPERTIES = new Properties();
	
	/**
	 * 获得连接池配置
	 * @return Properties
	 */
	public static Properties getSpatConnectionPoolProperties() {
		//如果配置太少，那就是有问题，需要重新初始化，我也不知道啥意思为啥要这么写
		if (PROPERTIES.size() < 3) {
			initSpatConnectionPoolConfig(WF.getDBConfigFilePath());
		}
		return PROPERTIES;
	}
	
	/**
	 * 读取配置文件初始化连接池配置信息
	 * @param configFile - 文件路径
	 */
	public static void initSpatConnectionPoolConfig(String configFile) {
		File propertyFile = new File(configFile);
		if (!propertyFile.exists()) {
			copyDefaultDBPropertiyFile(propertyFile);
		}
		
		FileReader fr = null;
		try {
			fr = new FileReader(propertyFile);
			PROPERTIES.load(fr);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			StreamUtil.safeClose(fr);
		}
	}
	
	/**
	 * 读取默认的配置文件内容并写入到propertyFile
	 * @param propertyFile - File
	 */
	private static void copyDefaultDBPropertiyFile(File propertyFile) {
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			File parentFile = propertyFile.getParentFile();
			parentFile.mkdirs();
			
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			
			String defConnectionPoolConfigFile = "cg/zz/wf/resources/spat_connection_pool_config.template";
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
	
	private SpatConnectionPoolConfig() {
		
	}

}

package cg.zz.wf.core;

import java.io.File;
import java.io.InputStream;
import java.util.PropertyResourceBundle;

import cg.zz.wf.core.dal.SpatConnectionPoolConfig;
import cg.zz.wf.core.ioc.context.ServiceContext;
import cg.zz.wf.core.ioc.context.SpringApplicationContext;
import cg.zz.wf.core.log.Log4jConfigurator;
import cg.zz.wf.util.Converter;
import cg.zz.wf.util.FileUtil;

public final class WF {
	
	private static final String namespace;
	//private static final String scanBasedPackage;
	
	/**
	 * 配置文件目录
	 */
	public static final String CONFIG_FOLDER;
	
	/**
	 * 日志目录
	 */
	private static final String LOG_CONFIG_FILE;
	public static final String DATASOURCE_CONFIG_FILE;
	private static final String SQL_INJECT_CONFIG_FILE;
	private static final String HTML_ENCODE_CONFIG_FILE;
	
	/**
	 * Log4j日志配置文件目录
	 */
	private static final String LOG_PATH;
	private static ServiceContext services;
	
	static {
		try {
			CONFIG_FOLDER = FileUtil.getRootPath() + "/opt/wf/";
			LOG_PATH = FileUtil.getRootPath() + "/opt/wf/logs/";
			LOG_CONFIG_FILE = CONFIG_FOLDER + "${namespace}/bj58log.properties";
			DATASOURCE_CONFIG_FILE = CONFIG_FOLDER + "${namespace}/db.properties";

			SQL_INJECT_CONFIG_FILE = CONFIG_FOLDER + "${namespace}/sql-inject.properties";
			HTML_ENCODE_CONFIG_FILE = CONFIG_FOLDER + "${namespace}/html-encode.properties";

			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			InputStream inputStream = cl.getResourceAsStream("META-INF/namespace.properties");
			PropertyResourceBundle pp = new PropertyResourceBundle(inputStream);

			namespace = pp.containsKey("namespace") ? pp.getString("namespace") : "";
			if (namespace == null || "".equals(namespace.trim())) {
				throw new ResourceNotFoundException("Does not specify a value for the namespace");
			}
			//scanBasedPackage = pp.containsKey("scanpackage") ? pp.getString("scanpackage") : "cg.zz";
			
			initConfigFile();
		    initServices();
		    initConverter();
		} catch (Exception e) {
			throw ExceptionUtils.makeThrow("META-INF in the classpath folder to ensure that there is 'namespace.properties' configuration file, and specifies the value namespace", e);
		}
	}
	
	/**
	 * 获得服务上下文对象
	 * @return ServiceContext
	 */
	public static ServiceContext getServiceContext() {
		return services;
	}
	
	/**
	 * 获得WF的版本
	 * @return String
	 */
	public static String getVersion() {
		Package pkg = WF.class.getPackage();
		return pkg != null ? pkg.getImplementationVersion() : null;
	}
	
	/**
	 * 获得命名空间
	 * @return String
	 */
	public static String getNamespace() {
		return namespace;
	}

	/**
	 * 获得配置文件目录
	 * @return String
	 */
	public static String getConfigFolder() {
		return CONFIG_FOLDER;
	}

	/**
	 * 获得数据库配置文件路径
	 * @return String
	 */
	public static String getDBConfigFilePath() {
		return DATASOURCE_CONFIG_FILE.replace("${namespace}", namespace);
	}

	/**
	 * 获得log4j的配置文件路径
	 * @return String
	 */
	public static String getLog4jConfigFilePath() {
		return LOG_CONFIG_FILE.replace("${namespace}", namespace);
	}

	/**
	 * 获得SqlInject配置文件路径
	 * @return String
	 */
	public static String getSqlInjectFilePath() {
		return SQL_INJECT_CONFIG_FILE.replace("${namespace}", namespace);
	}

	/**
	 * 获得html编码配置文件路径
	 * @return String
	 */
	public static String getHtmlEncodeFilePath() {
		return HTML_ENCODE_CONFIG_FILE.replace("${namespace}", namespace);
	}
	
	/**
	 * 获得Log4j配置文件的目录
	 * @return String
	 */
	public static String getLogPath() {
		return LOG_PATH;
	}
	
	/**
	 * 初始化配置文件
	 */
	private static void initConfigFile() {
		//初始化数据库连接池配置
		SpatConnectionPoolConfig.initSpatConnectionPoolConfig(getDBConfigFilePath());
		//初始化Log4j配置
		Log4jConfigurator.initConfigurator(getLog4jConfigFilePath());
		//将jar包中config目录文件拷贝到项目的config目录中
		ConfigManager.copyConfig();
	}
	
	/**
	 * 初始化服务环境
	 */
	private static void initServices() {
		String filePath = getConfigFolder() + getNamespace() + "/service-context.xml";
		File f = new File(filePath);
		if (f.exists()) {
			services = new SpringApplicationContext(filePath);
		}
	}
	
	/**
	 * 初始化一些转换器，如sql注入拦截，html编码等等
	 */
	private static void initConverter() {
		File fSql = new File(getSqlInjectFilePath());

		File fHtml = new File(getHtmlEncodeFilePath());
		if (fSql.exists()) {
			Converter.initSqlInject(getSqlInjectFilePath());
		}
		if (fHtml.exists()) {
			Converter.initHtmlEncode(getHtmlEncodeFilePath());
		}
	}
	
	private WF() {
		
	}

}

package cg.zz.wf.core;

import java.io.File;

/**
 * 
 * 配置文件管理工具类
 * 
 * @author chengang
 *
 */
public final class ConfigManager {

	private static boolean hasCopy = false;
	private static final String srcFolder = "config";

	/**
	 * 将包中的config目录拷贝到项目的配置目录中
	 */
	public static void copyConfig() {
		if (hasCopy) {
			return;
		}
		hasCopy = true;

		File destFolder = new File(WF.getConfigFolder() + WF.getNamespace() + "/");

		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		
		File root;
		try {
			root = new File(cl.getResource("").toURI());
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		String path = root.getAbsolutePath() + "/" + srcFolder;
		File configFolder = new File(path);
		if ((!configFolder.exists()) || (configFolder.isFile())) {
			return;
		}
		Copy.copy(configFolder, destFolder);
	}

	private ConfigManager() {

	}

}

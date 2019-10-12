package cg.zz.wf.mvc.cache;

import java.io.File;

import cg.zz.wf.core.WF;
import cg.zz.wf.mvc.Memcache;

public final class PageCacheMemCacheTool {
	
	private static Memcache mc = null;

	public static Memcache getCache() {
		if (mc != null) {
			return mc;
		}
		String path = WF.getConfigFolder() + WF.getNamespace() + "/pagecache_memcache.xml";

		File cacheFile = new File(path);
		if (!cacheFile.exists()) {
			return null;
		}
		synchronized (PageCacheMemCacheTool.class) {
			if (mc != null) {
				return mc;
			}
			mc = Memcache.GetMemcache(path);
			return mc;
		}
	}
	
	private PageCacheMemCacheTool() {
		
	}

}

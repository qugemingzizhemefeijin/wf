package cg.zz.wf.mvc.cache;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import cg.zz.wf.core.log.Log;
import cg.zz.wf.core.log.LogFactory;
import cg.zz.wf.mvc.ActionResult;
import cg.zz.wf.mvc.BeatContext;
import cg.zz.wf.mvc.thread.BeatContextBean;

/**
 * 
 * 缓存环境
 * 
 * @author chengang
 *
 */
public class CacheContext {

	protected static final Log log = LogFactory.getLog(CacheContext.class);

	/**
	 * 设置Cache-Control的缓存时间，秒
	 */
	private static final int time = 3600;
	
	/**
	 * 设置Last-Modified的时间
	 */
	private static final long lastModified = -1L;
	
	/**
	 * 设置Expires的时间
	 */
	private static final long expires = 1L;

	/**
	 * 304跳转的ActionResult
	 */
	private static final ActionResult NOT_MODIFIED = new ActionResult() {
		public void render(BeatContext beat) throws Exception {
			beat.getResponse().setStatus(304);
		}
	};
	
	/**
	 * 缓存key
	 */
	private String cacheKey;
	
	/**
	 * 过期时间，秒
	 */
	private int expiredTime = time;
	
	/**
	 * 请求环境
	 */
	private BeatContext beat;
	
	/**
	 * 是否命中缓存
	 */
	private boolean hit = false;
	
	/**
	 * 支持客户端缓存的response
	 */
	private CacheHttpServletResponseWrapper responseWrapper = null;

	public CacheContext(BeatContext beat) {
		this.beat = beat;
		//初始化缓存Key，其实就是本地请求的全路径网址，含端口号
		this.cacheKey = getKey(beat);
	}

	public String getCacheKey() {
		return this.cacheKey;
	}

	public void setCacheKey(String cacheKey) {
		this.cacheKey = cacheKey;
	}

	public int getExpiredTime() {
		return this.expiredTime;
	}

	public void setExpiredTime(int expiredTime) {
		this.expiredTime = expiredTime;
	}

	/**
	 * 初始化缓存信息
	 */
	public void needCache() {
		if (this.hit) {
			return;
		}
		if (PageCacheMemCacheTool.getCache() == null) {
			System.err.println("page cache error: no config file, needCache");
			return;
		}
		if (this.responseWrapper != null) {
			return;
		}
		String key = getCacheKey();
		
		//使用指定的BeatContextBean和key包装CacheContext
		wrapResponse(this.beat, key);
	}

	public ActionResult getCacheResult() {
		String key = getCacheKey();
		//判断Memcached客户端是否被初始化
		if (PageCacheMemCacheTool.getCache() == null) {
			log.warn("page cache error: no config file, getCacheResult");
			return null;
		}
		//从Memcached中获取缓存对象
		Object cacheObject = PageCacheMemCacheTool.getCache().get(key);
		if (cacheObject == null) {
			return null;
		}
		//如果缓存不为空，则设置hit为命中
		this.hit = true;

		//直接强制转换到respContent
		final ResponseContent respContent = (ResponseContent) cacheObject;

		log.debug("Page Cache: Using cached entry for " + key);

		//If-Modified-Since是标准的HTTP请求头标签，在发送HTTP请求时
		//把浏览器端缓存页面的最后修改时间一起发到服务器去，服务器会把这个时间与服务器上实际文件的最后修改时间进行比较。
		long clientLastModified = this.beat.getRequest().getDateHeader("If-Modified-Since");
		//如果客户端!=-1 && 客户端缓存时间>服务器的时间，则直接返回304
		if (clientLastModified != lastModified && clientLastModified >= respContent.getLastModified()) {
			return NOT_MODIFIED;
		}
		
		//返回ActionResult
		return new ActionResult() {
			public void render(BeatContext beat) throws Exception {
				respContent.writeTo(beat.getResponse(), false, false);
			}
		};
	}

	public void setCacheResult() {
		//如果hit是为true，则直接返回，那就是肯定已经被缓存过了
		if (this.hit) {
			return;
		}
		if (this.responseWrapper == null) {
			return;
		}
		if (!isCacheable(this.responseWrapper)) {
			return;
		}
		if (PageCacheMemCacheTool.getCache() == null) {
			return;
		}
		try {
			this.responseWrapper.flushBuffer();

			//计算过期时间，当前时间+3600秒
			Date expired = new Date(new Date().getTime() + this.expiredTime * 1000L);

			//存放到memcached中
			PageCacheMemCacheTool.getCache().set(getCacheKey(), this.responseWrapper.getContent(), expired);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获得缓存key
	 * @param beat - BeatContext
	 * @return String
	 */
	private String getKey(BeatContext beat) {
		return getFullUrl(beat);
	}

	/**
	 * 获得本地请求的全路径网址，含端口号
	 * @param beat - BeatContext
	 * @return String
	 */
	private String getFullUrl(BeatContext beat) {
		HttpServletRequest request = beat.getRequest();

		StringBuffer url = new StringBuffer();
		String scheme = request.getScheme();
		int port = request.getServerPort();
		if (port < 0) {
			port = 80;
		}
		url.append(scheme);
		url.append("://");
		url.append(request.getServerName());
		if ((scheme.equals("http") && port != 80) || (scheme.equals("https") && port != 443)) {
			url.append(':');
			url.append(port);
		}
		url.append(request.getRequestURI());

		String queryString = request.getQueryString();
		if (queryString != null) {
			url.append('?').append(queryString);
		}
		return url.toString();
	}

	/**
	 * 使用指定的BeatContextBean和key包装CacheContext
	 * @param beat - BeatContext
	 * @param key - 缓存在Cache中的key
	 */
	private void wrapResponse(BeatContext beat, String key) {
		BeatContextBean bcb = (BeatContextBean) beat;
		if (bcb == null) {
			System.err.println("beat is not a BeatContextBean.");
			return;
		}
		//这里计算缓存的时间竟然是0-this.expiredTime，不知道他要表达啥
		long cacheControl = 0 - this.expiredTime;
		CacheHttpServletResponseWrapper cacheResponse = new CacheHttpServletResponseWrapper(beat.getResponse(), false, this.expiredTime * 1000L, lastModified, expires, cacheControl);

		//这里设置了cacheKey的值，但是不知道有啥用？？
		cacheResponse.cacheKey = key;
		//这里又有啥用呢？
		bcb.setResponse(cacheResponse);

		this.responseWrapper = cacheResponse;
	}

	/**
	 * 本Response是否可被缓存
	 * @param cacheResponse - CacheHttpServletResponseWrapper
	 * @return boolean
	 */
	private boolean isCacheable(CacheHttpServletResponseWrapper cacheResponse) {
		return cacheResponse.getStatus() == 200;
	}

}

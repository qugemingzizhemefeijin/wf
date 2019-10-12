package cg.zz.wf.mvc.cache;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import cg.zz.wf.core.log.Log;
import cg.zz.wf.core.log.LogFactory;

/**
 * 
 * 支持HTTP Cache的Response类
 * 
 * @author chengang
 *
 */
public class CacheHttpServletResponseWrapper extends HttpServletResponseWrapper {

	private final Log log = LogFactory.getLog(getClass());
	
	/**
	 * 这个有啥用？？
	 */
	public String cacheKey = null;
	private PrintWriter cachedWriter = null;
	
	/**
	 * 输出内容
	 */
	private ResponseContent result = null;
	private SplitServletOutputStream cacheOut = null;
	private boolean fragment = false;
	
	/**
	 * 输出默认的状态码
	 */
	private int status = 200;
	
	/**
	 * 过期时间
	 */
	private long expires = 1L;
	
	/**
	 * 资源最后修改时间
	 */
	private long lastModified = -1L;
	
	/**
	 * 缓存时间
	 */
	private long cacheControl = -60L;

	public CacheHttpServletResponseWrapper(HttpServletResponse response) {
		this(response, false, Long.MAX_VALUE, 1L, -1L, -60L);
	}

	public CacheHttpServletResponseWrapper(HttpServletResponse response, boolean fragment, long time, long lastModified, long expires, long cacheControl) {
		super(response);
		this.result = new ResponseContent();
		this.fragment = fragment;
		this.expires = expires;
		this.lastModified = lastModified;
		this.cacheControl = cacheControl;
		if (!fragment) {
			//设置一些缓存等属性
			if (lastModified == -1L) {
				long current = System.currentTimeMillis();
				current -= current % 1000L;
				this.result.setLastModified(current);
				super.setDateHeader("Last-Modified", this.result.getLastModified());
			}
			if (expires == -1L) {
				this.result.setExpires(this.result.getLastModified() + time);
				super.setDateHeader("Expires", this.result.getExpires());
			}
			if (this.cacheControl == Long.MAX_VALUE) {
				long maxAge = System.currentTimeMillis();
				maxAge = maxAge - maxAge % 1000L + time;
				this.result.setMaxAge(maxAge);
				super.addHeader("Cache-Control", "max-age=" + time / 1000L);
			} else if (this.cacheControl != Long.MIN_VALUE) {
				this.result.setMaxAge(this.cacheControl);
				//传递一个负数，然后再-this.cacheControl，奇葩啊。
				super.addHeader("Cache-Control", "max-age=" + -this.cacheControl);
			} else if (this.cacheControl == Long.MIN_VALUE) {
				this.result.setMaxAge(this.cacheControl);
			}
		}
	}

	/**
	 * 获得ResponseContent对象
	 * @return ResponseContent
	 */
	public ResponseContent getContent() {
		try {
			//刷新缓冲
			flush();
		} catch (IOException localIOException) {
		}
		this.result.commit();

		return this.result;
	}

	public void setContentType(String value) {
		if (this.log.isDebugEnabled()) {
			this.log.debug("ContentType: " + value);
		}
		super.setContentType(value);
		this.result.setContentType(value);
	}

	public void setDateHeader(String name, long value) {
		if (this.log.isDebugEnabled()) {
			this.log.debug("dateheader: " + name + ": " + value);
		}
		if ((this.lastModified != 0L) && ("Last-Modified".equalsIgnoreCase(name)) && (!this.fragment)) {
			this.result.setLastModified(value);
		}
		if ((this.expires != 0L) && ("Expires".equalsIgnoreCase(name)) && (!this.fragment)) {
			this.result.setExpires(value);
		}
		super.setDateHeader(name, value);
	}

	public void addDateHeader(String name, long value) {
		if (this.log.isDebugEnabled()) {
			this.log.debug("dateheader: " + name + ": " + value);
		}
		if ((this.lastModified != 0L) && ("Last-Modified".equalsIgnoreCase(name)) && (!this.fragment)) {
			this.result.setLastModified(value);
		}
		if ((this.expires != 0L) && ("Expires".equalsIgnoreCase(name)) && (!this.fragment)) {
			this.result.setExpires(value);
		}
		super.addDateHeader(name, value);
	}

	public void setHeader(String name, String value) {
		if (this.log.isDebugEnabled()) {
			this.log.debug("header: " + name + ": " + value);
		}
		if ("Content-Type".equalsIgnoreCase(name)) {
			this.result.setContentType(value);
		}
		if ("Content-Encoding".equalsIgnoreCase(name)) {
			this.result.setContentEncoding(value);
		}
		super.setHeader(name, value);
	}

	public void addHeader(String name, String value) {
		if (this.log.isDebugEnabled()) {
			this.log.debug("header: " + name + ": " + value);
		}
		if ("Content-Type".equalsIgnoreCase(name)) {
			this.result.setContentType(value);
		}
		if ("Content-Encoding".equalsIgnoreCase(name)) {
			this.result.setContentEncoding(value);
		}
		super.addHeader(name, value);
	}

	public void setIntHeader(String name, int value) {
		if (this.log.isDebugEnabled()) {
			this.log.debug("intheader: " + name + ": " + value);
		}
		super.setIntHeader(name, value);
	}

	public void setStatus(int status) {
		super.setStatus(status);
		this.status = status;
	}

	public void sendError(int status, String string) throws IOException {
		super.sendError(status, string);
		this.status = status;
	}

	public void sendError(int status) throws IOException {
		super.sendError(status);
		this.status = status;
	}

	@SuppressWarnings("deprecation")
	public void setStatus(int status, String string) {
		super.setStatus(status, string);
		this.status = status;
	}

	public void sendRedirect(String location) throws IOException {
		this.status = 302;
		super.sendRedirect(location);
	}

	public int getStatus() {
		return this.status;
	}

	public void setLocale(Locale value) {
		super.setLocale(value);
		this.result.setLocale(value);
	}

	public ServletOutputStream getOutputStream() throws IOException {
		if (this.cacheOut == null) {
			this.cacheOut = new SplitServletOutputStream(this.result.getOutputStream(), super.getOutputStream());
		}
		return this.cacheOut;
	}

	public PrintWriter getWriter() throws IOException {
		if (this.cachedWriter == null) {
			String encoding = getCharacterEncoding();
			if (encoding != null) {
				this.cachedWriter = new PrintWriter(new OutputStreamWriter(getOutputStream(), encoding));
			} else {
				this.cachedWriter = new PrintWriter(new OutputStreamWriter(getOutputStream()));
			}
		}
		return this.cachedWriter;
	}

	/**
	 * 刷新流
	 * @throws IOException
	 */
	private void flush() throws IOException {
		if (this.cacheOut != null) {
			this.cacheOut.flush();
		}
		if (this.cachedWriter != null) {
			this.cachedWriter.flush();
		}
	}

	public void flushBuffer() throws IOException {
		super.flushBuffer();
		flush();
	}

	public boolean isCommitted() {
		return super.isCommitted();
	}

	public void reset() {
		super.reset();
	}

	public void resetBuffer() {
		super.resetBuffer();
	}

}

package cg.zz.wf.mvc.cache;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Locale;
import java.util.zip.GZIPInputStream;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * 输出内容管理对象
 * 
 * @author chengang
 *
 */
public class ResponseContent implements Serializable {

	private static final long serialVersionUID = 495649786951496318L;
	
	/**
	 * bout缓冲对象
	 */
	private transient ByteArrayOutputStream bout = new ByteArrayOutputStream(1024);
	
	/**
	 * 地理,政治和文化地区描述对象
	 */
	private Locale locale = null;
	
	/**
	 * Content-Encoding
	 */
	private String contentEncoding = null;
	
	/**
	 * Content-Type
	 */
	private String contentType = null;
	
	/**
	 * 输出的内容
	 */
	private byte[] content = null;
	
	/**
	 * 过期时间
	 */
	private long expires = Long.MAX_VALUE;
	
	/**
	 * 资源最后一次修改的时间
	 */
	private long lastModified = -1L;
	
	/**
	 * 过期时间，秒
	 */
	private long maxAge = -60L;

	public String getContentType() {
		return this.contentType;
	}

	public void setContentType(String value) {
		this.contentType = value;
	}

	public long getLastModified() {
		return this.lastModified;
	}

	public void setLastModified(long value) {
		this.lastModified = value;
	}

	public String getContentEncoding() {
		return this.contentEncoding;
	}

	public void setContentEncoding(String contentEncoding) {
		this.contentEncoding = contentEncoding;
	}

	public void setLocale(Locale value) {
		this.locale = value;
	}

	public long getExpires() {
		return this.expires;
	}

	public void setExpires(long value) {
		this.expires = value;
	}

	public long getMaxAge() {
		return this.maxAge;
	}

	public void setMaxAge(long value) {
		this.maxAge = value;
	}

	public OutputStream getOutputStream() {
		return this.bout;
	}

	public int getSize() {
		return this.content != null ? this.content.length : -1;
	}

	/**
	 * 将ByteArrayOutputStream内容转换出byte[]数组
	 */
	public void commit() {
		if (this.bout != null) {
			this.content = this.bout.toByteArray();
			this.bout = null;
		}
	}

	/**
	 * 输出内容
	 * @param response - ServletResponse
	 * @throws IOException
	 */
	public void writeTo(ServletResponse response) throws IOException {
		writeTo(response, false, false);
	}

	/**
	 * 输出内容
	 * @param response - ServletResponse
	 * @param fragment - 是否是碎片
	 * @param acceptsGZip - 是否已经是gzip压缩
	 * @throws IOException
	 */
	public void writeTo(ServletResponse response, boolean fragment, boolean acceptsGZip) throws IOException {
		//设置Content-Type
		if (this.contentType != null) {
			response.setContentType(this.contentType);
		}
		//如果当前是碎片，则不支持压缩
		if (fragment) {
			acceptsGZip = false;
		} else if (response instanceof HttpServletResponse) {
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			//设置Last-Modified
			if (this.lastModified != -1L) {
				httpResponse.setDateHeader("Last-Modified", this.lastModified);
			}
			//设置Expires
			if (this.expires != Long.MAX_VALUE) {
				httpResponse.setDateHeader("Expires", this.expires);
			}
			//设置Cache-Control
			if (this.maxAge != Long.MIN_VALUE && this.maxAge != Long.MAX_VALUE) {
				if (this.maxAge > 0L) {
					long currentMaxAge = this.maxAge / 1000L - System.currentTimeMillis() / 1000L;
					if (currentMaxAge < 0L) {
						currentMaxAge = 0L;
					}
					httpResponse.addHeader("Cache-Control", "max-age=" + currentMaxAge);
				} else {
					httpResponse.addHeader("Cache-Control", "max-age=" + -this.maxAge);
				}
			}
		}
		//设置locale
		if (this.locale != null) {
			response.setLocale(this.locale);
		}
		OutputStream out = new BufferedOutputStream(response.getOutputStream());
		//如果要求的是zip
		if (isContentGZiped()) {
			//如果已经是gzip压缩的则直接输出即可
			if (acceptsGZip) {
				//这个代码应该不需要，isContentGZiped()方法本身就已经判断gzip了
				//((HttpServletResponse) response).addHeader("Content-Encoding", "gzip");
				response.setContentLength(this.content.length);
				out.write(this.content);
			} else {
				//否则进行压缩处理
				ByteArrayInputStream bais = new ByteArrayInputStream(this.content);
				GZIPInputStream zis = new GZIPInputStream(bais);

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int numBytesRead = 0;
				byte[] tempBytes = new byte[128];
				while ((numBytesRead = zis.read(tempBytes, 0, tempBytes.length)) != -1) {
					baos.write(tempBytes, 0, numBytesRead);
				}
				byte[] result = baos.toByteArray();

				response.setContentLength(result.length);
				out.write(result);
			}
		} else {
			response.setContentLength(this.content.length);
			out.write(this.content);
		}
		out.flush();
	}

	/**
	 * 是否是gzip压缩
	 * @return boolean
	 */
	public boolean isContentGZiped() {
		return "gzip".equals(this.contentEncoding);
	}

}

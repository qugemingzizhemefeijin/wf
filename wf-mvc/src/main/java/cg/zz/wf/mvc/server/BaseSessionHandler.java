package cg.zz.wf.mvc.server;

import java.util.Enumeration;

import javax.servlet.http.HttpSession;

import cg.zz.wf.mvc.BeatContext;

/**
 * 
 * session处理器的实现类
 * 
 * @author chengang
 *
 */
public class BaseSessionHandler implements SessionHandler {
	
	/**
	 * 当前上下文环境
	 */
	BeatContext beat;
	
	/**
	 * session对象
	 */
	HttpSession session;
	
	public BaseSessionHandler(BeatContext beat) {
		this.beat = beat;
		this.session = this.beat.getRequest().getSession();
	}

	@Override
	public Object get(String name) {
		return this.session.getAttribute(name);
	}

	@Override
	public Long getCreationTime() {
		return Long.valueOf(this.session.getCreationTime());
	}

	@Override
	public Enumeration<String> getNames() {
		return this.session.getAttributeNames();
	}

	@Override
	public String getId() {
		return this.session.getId();
	}

	@Override
	public long getLastAccessedTime() {
		return this.session.getLastAccessedTime();
	}

	@Override
	public int getMaxInactiveInterval() {
		return this.session.getMaxInactiveInterval();
	}

	@Override
	public void invalidate() {
		this.session.invalidate();
	}

	@Override
	public boolean isNew() {
		return this.session.isNew();
	}

	@Override
	public void remove(String name) {
		this.session.removeAttribute(name);
	}

	@Override
	public void set(String name, Object value) {
		this.session.setAttribute(name, value);
	}

	@Override
	public void setMaxInactiveInterval(int value) {
		this.session.setMaxInactiveInterval(value);
	}

	@Override
	public void flush() {
		
	}

}

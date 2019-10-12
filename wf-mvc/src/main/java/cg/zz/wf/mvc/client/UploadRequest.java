package cg.zz.wf.mvc.client;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.codec.Charsets;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * 
 * 上传文件的Request
 * 
 * @author chengang
 *
 */
public class UploadRequest extends HttpServletRequestWrapper {

	/**
	 * 上传文件的request，内部实际上是FileUpload的封装
	 */
	MultipartHttpServletRequest springRquest;

	public UploadRequest(HttpServletRequest request) {
		super(request);

		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		commonsMultipartResolver.setDefaultEncoding(Charsets.UTF_8.name());

		this.springRquest = commonsMultipartResolver.resolveMultipart(request);
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return this.springRquest.getParameterNames();
	}

	@Override
	public String getParameter(String name) {
		return this.springRquest.getParameter(name);
	}

	@Override
	public String[] getParameterValues(String name) {
		return this.springRquest.getParameterValues(name);
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return this.springRquest.getParameterMap();
	}

	/**
	 * 获得所有上传文件的名称集合迭代器
	 * @return Iterator<String>
	 */
	public Iterator<String> getFileNames() {
		return this.springRquest.getFileNames();
	}

	/**
	 * 获取指定名称的MultipartFile并封装成RequestFile
	 * @param name - File表单的名称
	 * @return RequestFile
	 */
	public RequestFile getFile(String name) {
		return new RequestFile(this.springRquest.getFile(name));
	}

	/**
	 * 获取多个指定名称的MultipartFile并封装成RequestFile
	 * @param name - File表单的名称
	 * @return RequestFile
	 */
	public List<RequestFile> getFiles(String name) {
		List<RequestFile> result = new ArrayList<>();
		//多个文件
		for (MultipartFile f : this.springRquest.getFiles(name)) {
			result.add(new RequestFile(f));
		}
		return result;
	}

	/**
	 * 判断HttpServletRequest是否是文件表单提交
	 * @param request - HttpServletRequest
	 * @return boolean
	 */
	public static boolean isMultipart(HttpServletRequest request) {
		return (request != null) && (ServletFileUpload.isMultipartContent(request));
	}

	/**
	 * 如果HttpServletRequest是文件表单类型则封装成UploadRequest对象，否则原样返回
	 * @param request - HttpServletRequest
	 * @return HttpServletRequest
	 */
	public static HttpServletRequest wrapper(HttpServletRequest request) {
		return isMultipart(request) ? new UploadRequest(request) : request;
	}

}

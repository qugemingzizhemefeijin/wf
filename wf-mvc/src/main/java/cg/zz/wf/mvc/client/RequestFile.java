package cg.zz.wf.mvc.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * 封装的RequestFile对象，实际就是操作spring的MultipartFile对象
 * 
 * @author chengang
 *
 */
public class RequestFile {

	MultipartFile mFile;

	RequestFile(MultipartFile mFile) {
		this.mFile = mFile;
	}

	public String getName() {
		return this.mFile.getName();
	}

	public String getOriginalFilename() {
		return this.mFile.getOriginalFilename();
	}

	public String getContentType() {
		return this.mFile.getContentType();
	}

	public boolean isEmpty() {
		return this.mFile.isEmpty();
	}

	public long getSize() {
		return this.mFile.getSize();
	}

	public byte[] getBytes() throws IOException {
		return this.mFile.getBytes();
	}

	public InputStream getInputStream() throws IOException {
		return this.mFile.getInputStream();
	}

	public void transferTo(File dest) throws IOException, IllegalStateException {
		this.mFile.transferTo(dest);
	}

}

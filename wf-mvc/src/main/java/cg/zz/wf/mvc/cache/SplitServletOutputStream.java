package cg.zz.wf.mvc.cache;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;

public class SplitServletOutputStream extends ServletOutputStream {

	OutputStream captureStream = null;
	OutputStream passThroughStream = null;

	public SplitServletOutputStream(OutputStream captureStream, OutputStream passThroughStream) {
		this.captureStream = captureStream;
		this.passThroughStream = passThroughStream;
	}

	public void write(int value) throws IOException {
		this.captureStream.write(value);
		this.passThroughStream.write(value);
	}

	public void write(byte[] value) throws IOException {
		this.captureStream.write(value);
		this.passThroughStream.write(value);
	}

	public void write(byte[] b, int off, int len) throws IOException {
		this.captureStream.write(b, off, len);
		this.passThroughStream.write(b, off, len);
	}

	public void flush() throws IOException {
		super.flush();
		this.captureStream.flush();
		this.passThroughStream.flush();
	}

	public void close() throws IOException {
		super.close();
		this.captureStream.close();
		this.passThroughStream.close();
	}

}

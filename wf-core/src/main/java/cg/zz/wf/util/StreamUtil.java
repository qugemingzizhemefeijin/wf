package cg.zz.wf.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import org.apache.commons.codec.Charsets;

import cg.zz.wf.core.NestRuntimeException;

public final class StreamUtil {
	
	public static String readAll(Reader reader) {
		if (!(reader instanceof BufferedReader)) {
			reader = new BufferedReader(reader);
		}
		try {
			StringBuilder sb = new StringBuilder();

			char[] data = new char[64];
			int len;
			while ((len = reader.read(data)) != -1) {
				sb.append(data, 0, len);
			}
			return sb.toString();
		} catch (IOException e) {
			throw new NestRuntimeException("IO异常：", e);
		} finally {
			safeClose(reader);
		}
	}
	
	public static void writeAll(Writer writer, String str) {
		try {
			write(writer, str);
		} catch (IOException e) {
			throw new NestRuntimeException("IO异常：", e);
		} finally {
			safeClose(writer);
		}
	}
	
	public static boolean equals(InputStream sA, InputStream sB) throws IOException {
		int dA;
		while ((dA = sA.read()) != -1) {
			if (dA != sB.read()) {
				return false;
			}
		}
		return sB.read() == -1;
	}

	public static void write(Writer writer, CharSequence cs) throws IOException {
		if ((cs != null) && (writer != null)) {
			writer.write(cs.toString());
			writer.flush();
		}
	}
	
	public static void writeAndClose(Writer writer, CharSequence cs) {
		try {
			write(writer, cs);
		} catch (IOException e) {
			throw new NestRuntimeException("IO异常：", e);
		} finally {
			safeClose(writer);
		}
	}
	
	public static void write(OutputStream ops, InputStream ins) throws IOException {
		if ((ops == null) || (ins == null)) {
			return;
		}
		byte[] buf = new byte['?'];
		int len;
		while (-1 != (len = ins.read(buf))) {
			ops.write(buf, 0, len);
		}
	}
	
	public static void writeAndClose(OutputStream ops, InputStream ins) {
		try {
			write(ops, ins);
		} catch (IOException e) {
			throw new NestRuntimeException("IO异常：", e);
		} finally {
			safeClose(ops);
			safeClose(ins);
		}
	}
	
	public static void write(Writer writer, Reader reader) throws IOException {
		if ((writer == null) || (reader == null)) {
			return;
		}
		char[] cbuf = new char['?'];
		int len;
		while (-1 != (len = reader.read(cbuf))) {
			writer.write(cbuf, 0, len);
		}
	}
	
	public static void writeAndClose(Writer writer, Reader reader) {
		try {
			write(writer, reader);
		} catch (IOException e) {
			throw new NestRuntimeException("IO异常：", e);
		} finally {
			safeClose(writer);
			safeClose(reader);
		}
	}
	
	public static void write(OutputStream ops, byte[] bytes) throws IOException {
		if ((ops == null) || (bytes == null)) {
			return;
		}
		ops.write(bytes);
	}
	
	public static void writeAndClose(OutputStream ops, byte[] bytes) {
		try {
			write(ops, bytes);
		} catch (IOException e) {
			throw new NestRuntimeException("IO异常：", e);
		} finally {
			safeClose(ops);
		}
	}
	
	public static StringBuilder read(Reader reader) throws IOException {
		StringBuilder sb = new StringBuilder();
		char[] cbuf = new char['?'];
		int len;
		while (-1 != (len = reader.read(cbuf))) {
			sb.append(cbuf, 0, len);
		}
		return sb;
	}

	public static String readAndClose(Reader reader) {
		try {
			return read(reader).toString();
		} catch (IOException e) {
			throw new NestRuntimeException("IO异常：", e);
		} finally {
			safeClose(reader);
		}
	}

	public static boolean safeClose(Closeable cb) {
		if (cb != null) {
			try {
				cb.close();
			} catch (IOException e) {
				return false;
			}
		}
		return true;
	}

	public static void safeFlush(Flushable fa) {
		if (fa != null) {
			try {
				fa.flush();
			} catch (IOException localIOException) {
			}
		}
	}

	public static BufferedInputStream buff(InputStream ins) {
		if ((ins instanceof BufferedInputStream)) {
			return (BufferedInputStream) ins;
		}
		return new BufferedInputStream(ins);
	}

	public static BufferedOutputStream buff(OutputStream ops) {
		if ((ops instanceof BufferedOutputStream)) {
			return (BufferedOutputStream) ops;
		}
		return new BufferedOutputStream(ops);
	}

	public static BufferedReader buffr(Reader reader) {
		if ((reader instanceof BufferedReader)) {
			return (BufferedReader) reader;
		}
		return new BufferedReader(reader);
	}

	public static BufferedWriter buffw(Writer ops) {
		if ((ops instanceof BufferedWriter)) {
			return (BufferedWriter) ops;
		}
		return new BufferedWriter(ops);
	}

	public static InputStream fileIn(String path) {
		InputStream ins = FileUtil.findFileAsStream(path);
		if (ins == null) {
			File f = FileUtil.findFile(path);
			if (f != null) {
				try {
					ins = new FileInputStream(f);
				} catch (FileNotFoundException localFileNotFoundException) {
				}
			}
		}
		return buff(ins);
	}

	public static InputStream fileIn(File file) {
		try {
			return buff(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			throw new NestRuntimeException("IO异常：", e);
		}
	}

	public static Reader fileInr(String path) {
		return new InputStreamReader(fileIn(path), Charsets.UTF_8);
	}

	public static Reader fileInr(File file) {
		return new InputStreamReader(fileIn(file), Charsets.UTF_8);
	}

	public static OutputStream fileOut(String path) {
		return fileOut(FileUtil.findFile(path));
	}

	public static OutputStream fileOut(File file) {
		try {
			return buff(new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			throw new NestRuntimeException("IO异常：", e);
		}
	}

	public static Writer fileOutw(String path) {
		return fileOutw(FileUtil.findFile(path));
	}

	public static Writer fileOutw(File file) {
		return new OutputStreamWriter(fileOut(file), Charsets.UTF_8);
	}
	
	private StreamUtil() {
		
	}

}

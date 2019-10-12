package cg.zz.wf.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import cg.zz.wf.core.ExceptionUtils;
import cg.zz.wf.core.NestRuntimeException;

public final class FileUtil {

	public static String read(String path) {
		File f = findFile(path);
		if (f == null) {
			ExceptionUtils.makeThrow("Can not find file '%s'", new Object[] { path });
		}
		return read(f);
	}

	public static String read(File f) {
		return StreamUtil.readAll(StreamUtil.fileInr(f));
	}

	public static void write(String path, Object obj) {
		if ((path == null) || (obj == null)) {
			return;
		}
		try {
			write(createFileIfNoExists(path), obj);
		} catch (IOException e) {
			try {
				throw e;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public static void write(File f, Object obj) {
		if ((f == null) || (obj == null)) {
			return;
		}
		if (f.isDirectory()) {
			throw ExceptionUtils.makeThrow("Directory '%s' can not be write as File", new Object[] { f.getPath() });
		}
		try {
			if (!f.exists()) {
				createNewFile(f);
			}
			if ((obj instanceof InputStream)) {
				StreamUtil.writeAndClose(StreamUtil.fileOut(f), (InputStream) obj);
			} else if ((obj instanceof byte[])) {
				StreamUtil.writeAndClose(StreamUtil.fileOut(f), (byte[]) obj);
			} else if ((obj instanceof Reader)) {
				StreamUtil.writeAndClose(StreamUtil.fileOutw(f), (Reader) obj);
			} else {
				StreamUtil.writeAndClose(StreamUtil.fileOutw(f), obj.toString());
			}
		} catch (IOException e) {
			throw new NestRuntimeException("IO异常：", e);
		}
	}

	public static File renameSuffix(File f, String suffix) {
		if ((f == null) || (suffix == null) || (suffix.length() == 0)) {
			return f;
		}
		return new File(renameSuffix(f.getAbsolutePath(), suffix));
	}

	public static String renameSuffix(String path, String suffix) {
		int pos = path.length();
		for (pos--; pos > 0; pos--) {
			if (path.charAt(pos) == '.') {
				break;
			}
			if ((path.charAt(pos) == '/') || (path.charAt(pos) == '\\')) {
				pos = -1;
				break;
			}
		}
		if (pos <= 0) {
			return path + suffix;
		}
		return path.substring(0, pos) + suffix;
	}

	public static String getMajorName(String path) {
		int len = path.length();
		int l = 0;
		int r = len;
		for (int i = r - 1; i > 0; i--) {
			if ((r == len) && (path.charAt(i) == '.')) {
				r = i;
			}
			if ((path.charAt(i) == '/') || (path.charAt(i) == '\\')) {
				l = i + 1;
				break;
			}
		}
		return path.substring(l, r);
	}

	public static String getMajorName(File f) {
		return getMajorName(f.getAbsolutePath());
	}

	public static String getSuffixName(File f) {
		if (f == null) {
			return null;
		}
		return getSuffixName(f.getAbsolutePath());
	}

	public static String getSuffixName(String path) {
		if (path == null) {
			return null;
		}
		int pos = path.lastIndexOf('.');
		if (-1 == pos) {
			return "";
		}
		return path.substring(pos + 1);
	}

	public static ZipEntry[] findEntryInZip(ZipFile zip, String regex) {
		List<ZipEntry> list = new LinkedList<>();
		Enumeration<? extends ZipEntry> en = zip.entries();
		while (en.hasMoreElements()) {
			ZipEntry ze = (ZipEntry) en.nextElement();
			if ((regex == null) || (ze.getName().matches(regex))) {
				list.add(ze);
			}
		}
		return list.toArray(new ZipEntry[list.size()]);
	}

	public static File createFileIfNoExists(String path) throws IOException {
		String thePath = DiskUtil.absolute(path);
		if (thePath == null) {
			thePath = DiskUtil.normalize(path);
		}
		File f = new File(thePath);
		if (!f.exists()) {
			createNewFile(f);
		}
		if (!f.isFile()) {
			throw ExceptionUtils.makeThrow("'%s' should be a file!", new Object[] { path });
		}
		return f;
	}

	public static File createDirIfNoExists(String path) throws IOException {
		String thePath = DiskUtil.absolute(path);
		if (thePath == null) {
			thePath = DiskUtil.normalize(path);
		}
		File f = new File(thePath);
		if (!f.exists()) {
			makeDir(f);
		}
		if (!f.isDirectory()) {
			throw ExceptionUtils.makeThrow("'%s' should be a directory!", new Object[] { path });
		}
		return f;
	}

	public static File findFile(String path, ClassLoader klassLoader, String enc) {
		path = DiskUtil.absolute(path, klassLoader, enc);
		if (path == null) {
			return null;
		}
		return new File(path);
	}

	public static File findFile(String path, String enc) {
		return findFile(path, FileUtil.class.getClassLoader(), enc);
	}

	public static File findFile(String path, ClassLoader klassLoader) {
		return findFile(path, klassLoader, Charset.defaultCharset().name());
	}

	public static File findFile(String path) {
		return findFile(path, FileUtil.class.getClassLoader(), Charset.defaultCharset().name());
	}

	public static InputStream findFileAsStream(String path, Class<?> klass, String enc) {
		File f = new File(path);
		if (f.exists()) {
			try {
				return new FileInputStream(f);
			} catch (FileNotFoundException e1) {
				return null;
			}
		}
		if (klass != null) {
			InputStream ins = klass.getClassLoader().getResourceAsStream(path);
			if (ins == null) {
				ins = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
			}
			if (ins != null) {
				return ins;
			}
		}
		return ClassLoader.getSystemResourceAsStream(path);
	}

	public static InputStream findFileAsStream(String path, String enc) {
		return findFileAsStream(path, FileUtil.class, enc);
	}

	public static InputStream findFileAsStream(String path, Class<?> klass) {
		return findFileAsStream(path, klass, Charset.defaultCharset().name());
	}

	public static InputStream findFileAsStream(String path) {
		return findFileAsStream(path, FileUtil.class, Charset.defaultCharset().name());
	}

	public static boolean isDirectory(File f) {
		if (f == null) {
			return false;
		}
		if (!f.exists()) {
			return false;
		}
		if (!f.isDirectory()) {
			return false;
		}
		return true;
	}

	public static boolean isFile(File f) {
		return (f != null) && (f.exists()) && (f.isFile());
	}

	public static boolean createNewFile(File f) throws IOException {
		if ((f == null) || (f.exists())) {
			return false;
		}
		makeDir(f.getParentFile());
		return f.createNewFile();
	}

	public static boolean makeDir(File dir) {
		if ((dir == null) || (dir.exists())) {
			return false;
		}
		return dir.mkdirs();
	}

	public static boolean deleteDir(File dir) throws IOException {
		if ((dir == null) || (!dir.exists())) {
			return false;
		}
		if (!dir.isDirectory()) {
			throw new IOException("\"" + dir.getAbsolutePath() + "\" should be a directory!");
		}
		File[] files = dir.listFiles();
		boolean re = false;
		if (files != null) {
			if (files.length == 0) {
				return dir.delete();
			}
			File[] arrayOfFile1;
			int j = (arrayOfFile1 = files).length;
			for (int i = 0; i < j; i++) {
				File f = arrayOfFile1[i];
				if (f.isDirectory()) {
					re |= deleteDir(f);
				} else {
					re |= deleteFile(f);
				}
			}
			re |= dir.delete();
		}
		return re;
	}

	public static boolean deleteFile(File f) {
		if (f == null) {
			return false;
		}
		return f.delete();
	}

	public static boolean clearDir(File dir) throws IOException {
		if (dir == null) {
			return false;
		}
		if (!dir.exists()) {
			return false;
		}
		File[] fs = dir.listFiles();
		File[] arrayOfFile1;
		int j = (arrayOfFile1 = fs).length;
		for (int i = 0; i < j; i++) {
			File f = arrayOfFile1[i];
			if (f.isFile()) {
				deleteFile(f);
			} else if (f.isDirectory()) {
				deleteDir(f);
			}
		}
		return false;
	}

	public static boolean copyFile(File src, File target) throws IOException {
		if ((src == null) || (target == null) || (!src.exists())) {
			return false;
		}
		if ((!target.exists()) && (!createNewFile(target))) {
			return false;
		}
		InputStream ins = new BufferedInputStream(new FileInputStream(src));
		OutputStream ops = new BufferedOutputStream(new FileOutputStream(target));
		int b;
		while (-1 != (b = ins.read())) {
			ops.write(b);
		}
		StreamUtil.safeClose(ins);
		StreamUtil.safeFlush(ops);
		StreamUtil.safeClose(ops);
		return target.setLastModified(src.lastModified());
	}

	public static boolean copyDir(File src, File target) throws IOException {
		if ((src == null) || (target == null) || (!src.exists())) {
			return false;
		}
		if (!src.isDirectory()) {
			throw new IOException(src.getAbsolutePath() + " should be a directory!");
		}
		if ((!target.exists()) && (!makeDir(target))) {
			return false;
		}
		boolean re = true;
		File[] files = src.listFiles();
		if (files != null) {
			File[] arrayOfFile1;
			int j = (arrayOfFile1 = files).length;
			for (int i = 0; i < j; i++) {
				File f = arrayOfFile1[i];
				if (f.isFile()) {
					re &= copyFile(f, new File(target.getAbsolutePath() + "/" + f.getName()));
				} else {
					re &= copyDir(f, new File(target.getAbsolutePath() + "/" + f.getName()));
				}
			}
		}
		return re;
	}

	public static boolean move(File src, File target) throws IOException {
		if ((src == null) || (target == null)) {
			return false;
		}
		makeDir(target.getParentFile());
		return src.renameTo(target);
	}

	public static boolean rename(File src, String newName) {
		if ((src == null) || (newName == null)) {
			return false;
		}
		if (src.exists()) {
			File newFile = new File(src.getParent() + "/" + newName);
			if (newFile.exists()) {
				return false;
			}
			makeDir(newFile.getParentFile());
			return src.renameTo(newFile);
		}
		return false;
	}

	public static String renamePath(String path, String newName) {
		if (!StringUtil.isBlank(path)) {
			int pos = path.replace('\\', '/').lastIndexOf('/');
			if (pos > 0) {
				return path.substring(0, pos) + "/" + newName;
			}
		}
		return newName;
	}

	public static String getParent(String path) {
		if (StringUtil.isBlank(path)) {
			return path;
		}
		int pos = path.replace('\\', '/').lastIndexOf('/');
		if (pos > 0) {
			return path.substring(0, pos);
		}
		return "/";
	}

	public static String getName(String path) {
		if (!StringUtil.isBlank(path)) {
			int pos = path.replace('\\', '/').lastIndexOf('/');
			if (pos > 0) {
				return path.substring(pos);
			}
		}
		return path;
	}

	public static void cleanAllFolderInSubFolderes(File dir, String name) throws IOException {
		File[] files = dir.listFiles();
		File[] arrayOfFile1;
		int j = (arrayOfFile1 = files).length;
		for (int i = 0; i < j; i++) {
			File d = arrayOfFile1[i];
			if (d.isDirectory()) {
				if (d.getName().equalsIgnoreCase(name)) {
					deleteDir(d);
				} else {
					cleanAllFolderInSubFolderes(d, name);
				}
			}
		}
	}

	public static boolean isEquals(File f1, File f2) {
		if ((!f1.isFile()) || (!f2.isFile())) {
			return false;
		}
		InputStream ins1 = null;
		InputStream ins2 = null;
		try {
			ins1 = new BufferedInputStream(new FileInputStream(f1));
			ins2 = new BufferedInputStream(new FileInputStream(f2));
			return StreamUtil.equals(ins1, ins2);
		} catch (IOException e) {
			return false;
		} finally {
			StreamUtil.safeClose(ins1);
			StreamUtil.safeClose(ins2);
		}
	}

	public static File getFile(File dir, String path) {
		if (dir.exists()) {
			if (dir.isDirectory()) {
				return new File(dir.getAbsolutePath() + "/" + path);
			}
			return new File(dir.getParent() + "/" + path);
		}
		return new File(path);
	}

	public static File[] dirs(File dir) {
		return dir.listFiles(new FileFilter() {
			public boolean accept(File f) {
				return (!f.isHidden()) && (f.isDirectory()) && (!f.getName().startsWith("."));
			}
		});
	}

	public static File[] scanDirs(File dir) {
		ArrayList<File> list = new ArrayList<>();
		list.add(dir);
		scanDirs(dir, list);
		return list.toArray(new File[list.size()]);
	}

	private static void scanDirs(File rootDir, List<File> list) {
		File[] dirs = rootDir.listFiles(new FileFilter() {
			public boolean accept(File f) {
				return !f.isHidden() && f.isDirectory() && !f.getName().startsWith(".");
			}
		});
		if (dirs != null) {
			File[] arrayOfFile1;
			int j = (arrayOfFile1 = dirs).length;
			for (int i = 0; i < j; i++) {
				File dir = arrayOfFile1[i];
				scanDirs(dir, list);
				list.add(dir);
			}
		}
	}

	public static File[] files(File dir, String suffix) {
		return dir.listFiles(new FileFilter() {
			public boolean accept(File f) {
				return !f.isHidden() && f.isFile();
			}
		});
	}

	public static boolean equals(File f1, File f2) {
		if ((f1 == null) || (f2 == null)) {
			return false;
		}
		InputStream ins1 = StreamUtil.fileIn(f1);
		InputStream ins2 = StreamUtil.fileIn(f2);
		if ((ins1 == null) || (ins2 == null)) {
			return false;
		}
		try {
			return StreamUtil.equals(ins1, ins2);
		} catch (IOException e) {
			throw ExceptionUtils.makeThrow("IO异常：", e);
		} finally {
			StreamUtil.safeClose(ins1);
			StreamUtil.safeClose(ins2);
		}
	}

	public static String getRootPath() {
		File file = new File(System.getProperty("user.dir"));
		String path = file.getAbsolutePath().replace('\\', '/');
		return path.substring(0, path.indexOf('/'));
	}

	private FileUtil() {

	}

}

package cg.zz.wf.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

class Copy {

	public Copy(String from, String to) {
		copy(from, to);
	}

	public Copy(File from, File to) {
		copy(from, to);
	}

	public static void copy(File from, File to) {
		if (from.isDirectory()) {
			if (!to.exists()) {
				to.mkdir();
			}
			String[] s = from.list();
			for (int i = 0; i < s.length; i++) {
				File f = new File(from.getAbsoluteFile() + "/" + s[i]);
				File t = new File(to.getAbsoluteFile() + "/" + s[i]);
				copy(f, t);
			}
		} else if (from.isFile()) {
			copyFile(from, to);
		}
	}

	public static void copy(String from, String to) {
		File file = new File(from);

		File newFile = new File(to + "/" + file.getName());
		if (file.isDirectory()) {
			newFile.mkdir();
			String[] s = file.list();
			for (int i = 0; i < s.length; i++) {
				copy(file.getAbsolutePath() + "/" + s[i], newFile.getAbsolutePath());
			}
		} else if (file.isFile()) {
			copyFile(file, newFile);
		}
	}

	public static void copyFile(File from, File to) {
		if (!to.isFile()) {
			try (FileInputStream fis2 = new FileInputStream(from); FileOutputStream fos2 = new FileOutputStream(to)) {
				byte[] t = new byte[256];

				while (true) {
					int c = fis2.read(t);
					if (c != -1) {
						fos2.write(c);
					} else {
						break;
					}
				}
			} catch (IOException e) {
				System.err.println("FileStreamsTest: " + e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
				System.err.println("FileStreamsTest: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

}

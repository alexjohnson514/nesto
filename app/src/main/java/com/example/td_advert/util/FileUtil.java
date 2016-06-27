package com.example.td_advert.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileUtil {
	public static void deleteFileByPath(String path) {
		File fileToBeWritten = new File(path);
		if (fileToBeWritten.exists()) {
			fileToBeWritten.delete();
		}

	}

	public static void extractZipContentsOfSourceFile(String zipFilePath,
			String destinationDirPath)// e.g destinationDirPath is as
										// "/data/data/abc/"
	{
		String fileName = "";
		if (!destinationDirPath.endsWith(File.separator)) {
			destinationDirPath = destinationDirPath + File.separator;
		}
		try {
			ZipFile zipFile = new ZipFile(zipFilePath);
			Enumeration<?> en = zipFile.entries();
			while (en.hasMoreElements()) {
				ZipEntry zipentry = (ZipEntry) en.nextElement();
				if (zipentry != null) {
					String entryName = zipentry.getName();
					if (zipentry.isDirectory()) {
						File dir = new File(
								(destinationDirPath + entryName).replaceAll(
										" ", "%20"));
						if (!dir.exists()) {
							dir.mkdirs();
						}
					} else {
						String folder = "";
						if (entryName.indexOf('/') > -1) {
							folder = entryName.substring(0,
									entryName.lastIndexOf("/"));

							String newPath = destinationDirPath + folder;
							File dir = new File(newPath.replaceAll(" ", "%20"));
							if (!dir.exists()) {
								dir.mkdirs();
							}
						}

						fileName = entryName.substring(
								entryName.lastIndexOf("/") + 1,
								entryName.length());
						if (fileName.startsWith(".")) {
							fileName = "DOT"
									+ fileName.substring(1, fileName.length());
							entryName = folder + "/" + fileName;
						}
						// Build 12.4 by Talha Ahmed at 22/01/2011
						// entryName = entryName.replaceAll(" ", "%20");
						File file1 = new File(destinationDirPath + "/"
								+ entryName);
						File dir = new File(destinationDirPath);
						if (!dir.exists()) {
							dir.mkdirs();
						}
						if (!file1.exists()) {
							file1.createNewFile();
						}

						byte[] buf = new byte[1024 * 256];
						int n;
						FileOutputStream fileoutputstream = null;
						fileoutputstream = new FileOutputStream(file1);
						InputStream inputStream = zipFile
								.getInputStream(zipentry);
						while ((n = inputStream.read(buf, 0, 1024 * 256)) > -1)
							fileoutputstream.write(buf, 0, n);
						fileoutputstream.close();

					}

				}
			}
			zipFile.close();
		}

		catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			if (files == null) {
				return true;
			}
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}

	public static void writeToFile(String path, String contents)
			throws Exception {
		File fileToBeWritten = new File(path);

		BufferedWriter writer = null;
		try {
			if (!fileToBeWritten.exists()) {
				fileToBeWritten.createNewFile();
			}
			writer = new BufferedWriter(new FileWriter(fileToBeWritten));
			writer.write(contents);
			writer.flush();
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {

			}
		}
	}

	public static String readFromFile(String path) throws Exception{
		String contents = "";

		File fileToBeRead = new File(path);

		BufferedReader reader = null;
		try {
			if (!fileToBeRead.exists()) {
				fileToBeRead.createNewFile();
			}
			String line = null;
			reader = new BufferedReader(new FileReader(fileToBeRead));
			while ((line = reader.readLine()) != null) {
				if (line == null)
					break;
				contents += line;
			}
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {

			}
		}

		return contents;
	}
}

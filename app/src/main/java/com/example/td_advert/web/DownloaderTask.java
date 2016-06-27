package com.example.td_advert.web;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.example.td_advert.util.FileUtil;
import com.example.td_advert.web.delegate.WebTaskDelegate;

public class DownloaderTask extends WebTask {
	private String saveToPath;

	public DownloaderTask(String url, WebTaskDelegate delegate,
			String saveToPath) {
		super(url, delegate);
		this.saveToPath = saveToPath;
	}

	public DownloaderTask(String url, String saveToPath) {
		this(url, new WebTaskDelegate(), saveToPath);
	}

    @Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
	}

	@Override
	protected String processInputStream(InputStream instream) {
		OutputStream output = null;
		InputStream input = null;
		try {
			// input stream to read file - with 8k buffer
			input = new BufferedInputStream(instream, 524288);
			// System.out.println("Tabs Data::" + TabImages.get(i));
			// Output stream to write file
			FileUtil.deleteFileByPath(saveToPath);

			output = new FileOutputStream(saveToPath);
			byte data[] = new byte[1024 * 256];

			int count;
			while ((count = input.read(data)) != -1) {
				output.write(data, 0, count);
			}

//			String path = saveToPath.substring(0, saveToPath.lastIndexOf('/'));
//			String zipFileName = saveToPath.substring(saveToPath
//					.lastIndexOf('/') + 1);
//			zipFileName = zipFileName
//					.substring(0, zipFileName.lastIndexOf('.'));
//
//			FileUtil.extractZipContentsOfSourceFile(saveToPath, path + "/" + zipFileName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (output != null) {
					output.close();
				}
			} catch (IOException e) {
			}
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
			}
		}

		return null;
	}

	
}

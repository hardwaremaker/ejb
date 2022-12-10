package com.lp.server.system.automatikjob;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Locale;

import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;

public class FileHandler {
	
	protected final ILPLogger myLogger = LPLogService.getInstance().getLogger(this.getClass());
	private String filepath;
	private String fileextension;

	public FileHandler() {
	}

	public FileHandler(String fileextension) {
		setFileextension(fileextension);
	}

	public FileHandler(String filepath, String fileextension) {
		setFilepath(filepath);
		setFileextension(fileextension);
	}

	protected boolean hasAccessToFilepath() {
		if (getFilepath() == null) {
			myLogger.error("No file path set");
			return false;
		}
		File file = new File(getFilepath());
		
		if (!file.getParentFile().exists()) {
			if (!file.getParentFile().mkdir()) {
				myLogger.error("Directory \"" + file.getParent() + "\" can not be made");
				return false;
			}
		}
		
		if (!file.getParentFile().canWrite()) {
			myLogger.error("Directory \"" + file.getParent() + "\" can not be made");
			return false;
		}

		return true;
	}

	protected String getFilepath() {
		return filepath;
	}

	public String getFileextension() {
		return fileextension;
	}

	public void setFileextension(String fileextension) {
		this.fileextension = fileextension;
	}

	public void setFilepath(String filepath) {
		if (getFilepath() != null && getFileextension() != null && !filepath.endsWith(getFileextension())) {
			filepath = getFilepath() + getFileextension();
		}
		this.filepath = filepath;
	}
	
	public void setFilepath(String pattern, Object[] values, Locale locale) {
		MessageFormat msgFormat = new MessageFormat(pattern, locale); 
		setFilepath(msgFormat.format(values));
	}
	
	public String getFilepathNoDuplicate() {
		String path = getFilepath();
		File file = new File(path);
		Integer count = 1;
		while (file.exists()) {
			StringBuilder builder = new StringBuilder(getFilepath());
			builder = builder.insert(getFilepath().indexOf(getFileextension()), "_" + count);
			file = new File(builder.toString());
			count++;
		}
		
		return path;
	}
	
	public boolean writeBytes(byte[] bytes) throws IOException {
		if (!hasAccessToFilepath()) return false;
		
		File file = new File(getFilepath());
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream fOutputStream = new FileOutputStream(file);
		fOutputStream.write(bytes);
		fOutputStream.flush();
		fOutputStream.close();
		
		return true;
	}
}

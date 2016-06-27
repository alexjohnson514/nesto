package com.example.td_advert.bean;

public class ApkVersion {

	private int id;
	private String version;
	private String filePath;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	@Override
	public String toString() {
		return "ApkVersion [id=" + id + ", version=" + version + ", filePath="
				+ filePath + "]";
	}
	
	
}

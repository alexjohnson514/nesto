package com.example.td_advert.bean;

import java.util.ArrayList;

public class AppConnectVersionCheckResponse {
	
	private ArrayList<Company> companyVersions;

    public Integer getTestDevice() {
        return testDevice;
    }

    private Integer testDevice;

	public ArrayList<Company> getCompanyVersions() {
		return companyVersions;
	}

	public void setCompanyVersions(ArrayList<Company> companyVersions) {
		this.companyVersions = companyVersions;
	}

	

}

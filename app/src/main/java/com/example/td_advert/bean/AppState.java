package com.example.td_advert.bean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AppState {
	private static AppState instance = new AppState();

	private HashMap<Integer, Company> companiesMap = new HashMap<Integer, Company>();
	private String taxiNumber = "";
	private String meid = "";

    public Float getBatteryStatus() {
        return batteryStatus;
    }

    public void setBatteryStatus(Float batteryStatus) {
        this.batteryStatus = batteryStatus;
    }

    private Float batteryStatus = 0.0f;

    public boolean isTestDevice() {
        return testDevice;
    }

    public void setTestDevice(boolean testDevice) {
        this.testDevice = testDevice;
    }

    private boolean testDevice = false;

	private AppState() {
	}

	public static AppState getInstance() {
		return instance;
	}

	public Company getCompany(int id) {
		return companiesMap.get(id);
	}

	public void addCompany(Company company) {
		companiesMap.put(company.getCompanyId(), company);
	}

	public ArrayList<Company> getCompaniesList(){
		ArrayList<Company> companiesList = new ArrayList<Company>();
		for (Company company: companiesMap.values()){
			companiesList.add(company);
		}
		
		return companiesList;
	}

    public String getCompaniesListID(){
        JSONObject obj = new JSONObject();
        for (Company company: companiesMap.values()){
            try {
                obj.put(company.getName(), company.getVersion() + "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return obj.toString();
    }
	
	public void clearCompanies(){
		companiesMap = new HashMap<Integer, Company>();
	}

	public String getTaxiNumber() {
		return taxiNumber;
	}

	public void setTaxiNumber(String taxiNumber) {
		this.taxiNumber = taxiNumber;
	}

	public String getMeid() {
		return meid;
	}

	public void setMeid(String meid) {
		this.meid = meid;
	}
}

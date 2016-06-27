package com.example.td_advert.bean;

public class Station {
	private Integer id;
	private String stationName;

	public Station() {
	}

	public Station(Integer id, String stationName) {
		super();
		this.id = id;
		this.stationName = stationName;
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }
}

package com.your.time.bean;

public class Booking extends Rest{
	protected String _id;
	protected String username;
    protected User userDetail;
	protected String serviceProviderId;
	protected String service;
    protected String phonenumber;
	protected String date;
	protected String time;
	protected String status;
	protected String waitTime;

    public Booking() {}

	public Booking(String _id, String username, String serviceProviderId, String date, String time, String status, String waitTime) {
		this._id = _id;
		this.username = username;
		this.serviceProviderId = serviceProviderId;
		this.date = date;
		this.time = time;
		this.status = status;
		this.waitTime = waitTime;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getServiceProviderId() {
		return serviceProviderId;
	}

	public void setServiceProviderId(String serviceProviderId) {
		this.serviceProviderId = serviceProviderId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
        
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(String waitTime) {
		this.waitTime = waitTime;
	}

    public User getUserDetail() { return userDetail; }

    public void setUserDetail(User userDetail) { this.userDetail = userDetail; }

    public void setService(String service) {
        this.service = service;
    }

    public String getService() {
        return service;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getPhonenumber() {
        return phonenumber;
    }
}
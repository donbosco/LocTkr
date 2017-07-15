package com.your.time.bean;

import java.io.Serializable;

public class ServiceProvider extends Rest implements Serializable{

	private static final long serialVersionUID = 1L;
    protected String _id;
    protected String username;
    protected String ispId;
    protected String displayName;
    protected String OfficialName;
    protected String email;
    protected String addressline1;
    protected String addressline2;
    protected String country;
    protected String state;
    protected String zip;
    protected String phonenumber;
    protected String serviceProviderTye;
    
    public ServiceProvider() {}

	public ServiceProvider(String _id, String username, String ispId, String displayName, String officialName,
			String email, String addressline1, String addressline2, String country, String state, String zip,
			String phonenumber, String serviceProviderTye) {
		super();
		this._id = _id;
		this.username = username;
		this.ispId = ispId;
		this.displayName = displayName;
		OfficialName = officialName;
		this.email = email;
		this.addressline1 = addressline1;
		this.addressline2 = addressline2;
		this.country = country;
		this.state = state;
		this.zip = zip;
		this.phonenumber = phonenumber;
		this.serviceProviderTye = serviceProviderTye;
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

	public String getIspId() {
		return ispId;
	}

	public void setIspId(String ispId) {
		this.ispId = ispId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getOfficialName() {
		return OfficialName;
	}

	public void setOfficialName(String officialName) {
		OfficialName = officialName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddressline1() {
		return addressline1;
	}

	public void setAddressline1(String addressline1) {
		this.addressline1 = addressline1;
	}

	public String getAddressline2() {
		return addressline2;
	}

	public void setAddressline2(String addressline2) {
		this.addressline2 = addressline2;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getServiceProviderTye() {
		return serviceProviderTye;
	}

	public void setServiceProviderTye(String serviceProviderTye) {
		this.serviceProviderTye = serviceProviderTye;
	}	   
}
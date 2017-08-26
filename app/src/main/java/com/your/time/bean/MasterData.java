package com.your.time.bean;

import android.view.View;

import com.your.time.util.Pages;

import java.io.Serializable;

public class MasterData extends Rest implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    protected String _id;
	protected String type;
	protected String code;
	protected String value;
	protected int isActive;
	protected int order;
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getIsActive() {
		return isActive;
	}
	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public <T extends Rest> View adapterMapper(View layoutView, Pages page,int position) {
		return null;
	}
}

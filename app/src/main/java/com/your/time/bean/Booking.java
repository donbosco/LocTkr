package com.your.time.bean;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.your.time.activity.R;
import com.your.time.util.BookingStatus;
import com.your.time.util.Pages;
import com.your.time.util.YourTimeUtil;

public class Booking extends Rest{
	protected String _id;
	protected String username;
    protected User userDetail;
	protected String serviceProviderId;
	protected String service;
    protected String phonenumber;
	protected String date;
	protected String time;
	protected String reason;
	protected String status;
	protected String waitTime;
	protected String createdBy;

    public Booking() {}

    public Booking(String _id, String username, User userDetail, String serviceProviderId, String service, String phonenumber, String date, String time, String status) {
        this._id = _id;
        this.username = username;
        this.userDetail = userDetail;
        this.serviceProviderId = serviceProviderId;
        this.service = service;
        this.phonenumber = phonenumber;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public Booking(String _id, String username, String serviceProviderId, String date, String time, String status) {
		this._id = _id;
		this.username = username;
		this.serviceProviderId = serviceProviderId;
		this.date = date;
		this.time = time;
		this.status = status;
		if(date != null && time != null)
			this.waitTime = YourTimeUtil.calculateWaitTime(date+" "+time);
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
		if(date != null && time != null)
			waitTime = YourTimeUtil.calculateWaitTime(date+" "+time);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getWaitTime() {
		if(date != null && time != null)
			waitTime = YourTimeUtil.calculateWaitTime(date+" "+time);
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

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getReason() {
		return reason;
	}

	public <T extends Rest> View adapterMapper(View layoutView, Pages page,int position){

        switch (page){
            case CONSUMER_HOME_ACTIVITY:{
                    ImageView statusImage = (ImageView) layoutView.findViewById(R.id.consumer_home_status);
                    if(statusImage != null){
                        if(BookingStatus.NEW.equals(this.getStatus())) {
                            statusImage.setImageResource(R.drawable.book_wait);
                        }else if(BookingStatus.CONFIRMED.equals(this.getStatus())) {
                            statusImage.setImageResource(R.drawable.book_conf);
                        }else if(BookingStatus.COMPLETED.equals(this.getStatus())) {
                            statusImage.setImageResource(R.drawable.book_exp);
                        }else if(BookingStatus.CANCEL.equals(this.getStatus())) {
                            statusImage.setImageResource(R.drawable.book_cancel);
                        }
                    }
                    TextView service = (TextView) layoutView.findViewById(R.id.consumer_home_service);
                    if(service != null){
                        service.setText(this.getService());
                    }
                    TextView address = (TextView) layoutView.findViewById(R.id.consumer_home_address);
                    if(address != null){
                        address.setText(this.getReason());
                    }
                    TextView phoneNumber = (TextView) layoutView.findViewById(R.id.consumer_home_phonenumber);
                    if(phoneNumber != null){
                        phoneNumber.setText(this.getPhonenumber());
                    }
                    TextView waitingTime = (TextView) layoutView.findViewById(R.id.consumer_home_waitTime);
                    if(waitingTime != null){
                        YourTimeUtil.getTimer(waitingTime,getWaitTime()).start();
                    }

                    ImageView scheduleImageView = (ImageView) layoutView.findViewById(R.id.consumer_home_reschedule);
                    if(scheduleImageView != null)
                        scheduleImageView.setTag(R.string.param_booking,position);
                    ImageView cancelscheduleImageView = (ImageView) layoutView.findViewById(R.id.consumer_home_cancel);
                    if(cancelscheduleImageView != null)
                        cancelscheduleImageView.setTag(R.string.param_booking,position);
                }
                break;
            case CONSUMER_ACTIVE_APPOINTMENT_ACTIVITY:{
                    ImageView statusImage = (ImageView) layoutView.findViewById(R.id.consumer_appointment_status);
                    if(statusImage != null){
                        if(BookingStatus.NEW.equals(this.getStatus())) {
                            statusImage.setImageResource(R.drawable.book_wait);
                        }else if(BookingStatus.CONFIRMED.equals(this.getStatus())) {
                            statusImage.setImageResource(R.drawable.book_conf);
                        }else if(BookingStatus.COMPLETED.equals(this.getStatus())) {
                            statusImage.setImageResource(R.drawable.book_exp);
                        }else if(BookingStatus.CANCEL.equals(this.getStatus())) {
                            statusImage.setImageResource(R.drawable.book_cancel);
                        }
                    }
                    TextView service = (TextView) layoutView.findViewById(R.id.consumer_appointment_service);
                    if(service != null){
                        service.setText(this.getService());
                    }
                    TextView address = (TextView) layoutView.findViewById(R.id.consumer_appointment_address);
                    if(address != null){
                        address.setText(this.getReason());
                    }
                    TextView phoneNumber = (TextView) layoutView.findViewById(R.id.consumer_appointment_phonenumber);
                    if(phoneNumber != null){
                        phoneNumber.setText(this.getPhonenumber());
                    }
                    TextView waitingTime = (TextView) layoutView.findViewById(R.id.consumer_appointment_waitTime);
                    if(waitingTime != null){
                        YourTimeUtil.getTimer(waitingTime,getWaitTime()).start();
                    }

                    ImageView scheduleImageView = (ImageView) layoutView.findViewById(R.id.consumer_appointment_reschedule);
                    if(scheduleImageView != null)
                        scheduleImageView.setTag(R.string.param_booking,position);
                    ImageView cancelscheduleImageView = (ImageView) layoutView.findViewById(R.id.consumer_appointment_cancel);
                    if(cancelscheduleImageView != null)
                        cancelscheduleImageView.setTag(R.string.param_booking,position);
                }
                break;
            case CONSUMER_APPOINTMENT_HISTORY_ACTIVITY:{
                    ImageView statusImage = (ImageView) layoutView.findViewById(R.id.consumer_appointment_history_status);
                    if(statusImage != null){
                        if(BookingStatus.NEW.equals(this.getStatus())) {
                            statusImage.setImageResource(R.drawable.book_wait);
                        }else if(BookingStatus.CONFIRMED.equals(this.getStatus())) {
                            statusImage.setImageResource(R.drawable.book_conf);
                        }else if(BookingStatus.COMPLETED.equals(this.getStatus())) {
                            statusImage.setImageResource(R.drawable.book_exp);
                        }else if(BookingStatus.CANCEL.equals(this.getStatus())) {
                            statusImage.setImageResource(R.drawable.book_cancel);
                        }
                    }
                    TextView service = (TextView) layoutView.findViewById(R.id.consumer_appointment_history_service);
                    if(service != null){
                        service.setText(this.getService());
                    }
                    TextView address = (TextView) layoutView.findViewById(R.id.consumer_appointment_history_address);
                    if(address != null){
                        address.setText(this.getReason());
                    }
                    TextView phoneNumber = (TextView) layoutView.findViewById(R.id.consumer_appointment_history_phonenumber);
                    if(phoneNumber != null){
                        phoneNumber.setText(this.getPhonenumber());
                    }
                }
                break;
            case ISP_HOME_ACTIVITY:{
                    ImageView statusImage = (ImageView) layoutView.findViewById(R.id.isp_home_status);
                    if(statusImage != null){
                        if(BookingStatus.NEW.equals(this.getStatus())) {
                            statusImage.setImageResource(R.drawable.book_wait);
                        }else if(BookingStatus.CONFIRMED.equals(this.getStatus())) {
                            statusImage.setImageResource(R.drawable.book_conf);
                        }else if(BookingStatus.COMPLETED.equals(this.getStatus())) {
                            statusImage.setImageResource(R.drawable.book_exp);
                        }else if(BookingStatus.CANCEL.equals(this.getStatus())) {
                            statusImage.setImageResource(R.drawable.book_cancel);
                        }
                    }
                    TextView service = (TextView) layoutView.findViewById(R.id.isp_home_service);
                    if(service != null){
                        service.setText(this.getService());
                    }
                    TextView address = (TextView) layoutView.findViewById(R.id.isp_home_address);
                    if(address != null){
                        address.setText(this.getReason());
                    }
                    TextView phoneNumber = (TextView) layoutView.findViewById(R.id.isp_home_phonenumber);
                    if(phoneNumber != null){
                        phoneNumber.setText(this.getPhonenumber());
                    }
                    /*TextView waitingTime = (TextView) layoutView.findViewById(R.id.isp_home_waitTime);
                    if(waitingTime != null){
                        YourTimeUtil.getTimer(waitingTime,getWaitTime()).start();
                    }*/

                    ImageView scheduleImageView = (ImageView) layoutView.findViewById(R.id.isp_home_reschedule);
                    if(scheduleImageView != null)
                        scheduleImageView.setTag(R.string.param_booking,position);

                    ImageView confirmScheduleImageView = (ImageView) layoutView.findViewById(R.id.isp_home_accept);
                    if(confirmScheduleImageView != null)
                        confirmScheduleImageView.setTag(R.string.param_booking,position);

                    ImageView cancelscheduleImageView = (ImageView) layoutView.findViewById(R.id.isp_home_cancel);
                    if(cancelscheduleImageView != null)
                        cancelscheduleImageView.setTag(R.string.param_booking,position);
                }
                break;
            case ISP_ACTIVE_SCHEDULE_ACTIVITY:{
                    ImageView statusImage = (ImageView) layoutView.findViewById(R.id.isp_schedule_status);
                    if(statusImage != null){
                        if(BookingStatus.NEW.equals(this.getStatus())) {
                            statusImage.setImageResource(R.drawable.book_wait);
                        }else if(BookingStatus.CONFIRMED.equals(this.getStatus())) {
                            statusImage.setImageResource(R.drawable.book_conf);
                        }else if(BookingStatus.COMPLETED.equals(this.getStatus())) {
                            statusImage.setImageResource(R.drawable.book_exp);
                        }else if(BookingStatus.CANCEL.equals(this.getStatus())) {
                            statusImage.setImageResource(R.drawable.book_cancel);
                        }
                    }
                    TextView service = (TextView) layoutView.findViewById(R.id.isp_schedule_service);
                    if(service != null){
                        service.setText(this.getService());
                    }
                    TextView address = (TextView) layoutView.findViewById(R.id.isp_schedule_address);
                    if(address != null){
                        address.setText(this.getReason());
                    }
                    TextView phoneNumber = (TextView) layoutView.findViewById(R.id.isp_schedule_phonenumber);
                    if(phoneNumber != null){
                        phoneNumber.setText(this.getPhonenumber());
                    }
                        /*TextView waitingTime = (TextView) layoutView.findViewById(R.id.isp_schedule_waitTime);
                        if(waitingTime != null){
                            YourTimeUtil.getTimer(waitingTime,getWaitTime()).start();
                        }*/

                    ImageView scheduleImageView = (ImageView) layoutView.findViewById(R.id.isp_schedule_reschedule);
                    if(scheduleImageView != null)
                        scheduleImageView.setTag(R.string.param_booking,position);

                    ImageView confirmScheduleImageView = (ImageView) layoutView.findViewById(R.id.isp_schedule_accept);
                    if(confirmScheduleImageView != null)
                        confirmScheduleImageView.setTag(R.string.param_booking,position);

                    ImageView cancelscheduleImageView = (ImageView) layoutView.findViewById(R.id.isp_schedule_cancel);
                    if(cancelscheduleImageView != null)
                        cancelscheduleImageView.setTag(R.string.param_booking,position);
                }
                break;
            case ISP_SCHEDULE_HISTORY_ACTIVITY:{
                    ImageView statusImage = (ImageView) layoutView.findViewById(R.id.isp_schedule_history_status);
                    if(statusImage != null){
                        if(BookingStatus.NEW.equals(this.getStatus())) {
                            statusImage.setImageResource(R.drawable.book_wait);
                        }else if(BookingStatus.CONFIRMED.equals(this.getStatus())) {
                            statusImage.setImageResource(R.drawable.book_conf);
                        }else if(BookingStatus.COMPLETED.equals(this.getStatus())) {
                            statusImage.setImageResource(R.drawable.book_exp);
                        }else if(BookingStatus.CANCEL.equals(this.getStatus())) {
                            statusImage.setImageResource(R.drawable.book_cancel);
                        }
                    }
                    TextView service = (TextView) layoutView.findViewById(R.id.isp_schedule_history_service);
                    if(service != null){
                        service.setText(this.getService());
                    }
                    TextView address = (TextView) layoutView.findViewById(R.id.isp_schedule_history_address);
                    if(address != null){
                        address.setText(this.getReason());
                    }
                    TextView phoneNumber = (TextView) layoutView.findViewById(R.id.isp_schedule_history_phonenumber);
                    if(phoneNumber != null){
                        phoneNumber.setText(this.getPhonenumber());
                    }
                    /*TextView waitingTime = (TextView) layoutView.findViewById(R.id.isp_schedule_history_waitTime);
                    if(waitingTime != null){
                        YourTimeUtil.getTimer(waitingTime,getWaitTime()).start();
                    }*/
                }
                break;
            default:
                break;
        }

		/*for (int i=0;i < itemIds.length;i++) {
			TextView text = (TextView) layoutView.findViewById(itemIds[i]);
			String viewId = layoutView.getResources().getResourceName(text.getId());
			viewId = viewId.substring(viewId.lastIndexOf("_")+1,viewId.length());
			if(viewId.equals("action")){
				if(resourceId != 0){
					text.setGravity(Gravity.CENTER);
				}
			}else if(viewId.equals("sno")) {
				text.setText(""+sno);
				text.setGravity(Gravity.LEFT);
			}else if(viewId.equals("waitTime")){
				String string = ReflectionUtil.getValue(rest, viewId);
				string = string == null?"":string.trim();
				//text.setText(string);
				YourTimeUtil.getTimer(text,string).start();
				text.setGravity(Gravity.LEFT);
			}else if(viewId.equals("status")) {

			}else{
				String string = ReflectionUtil.getValue(rest, viewId);
				string = string == null?"nothing":string.trim();
				text.setText(string);
			}
			//text.setTextColor(Color.BLACK);
		}*/
		return layoutView;
	}
}
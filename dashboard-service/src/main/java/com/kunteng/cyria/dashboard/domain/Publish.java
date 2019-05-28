package com.kunteng.cyria.dashboard.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public class Publish {
	private String hash;
	private String status;
	private Date timestamp;

	public static String generateId() {
                UUID uuid = UUID.randomUUID();
                return uuid.toString().replace("-","");
    }

	public Publish(){
		hash = "";
		status = "unpublish";
	}

	public String getHash(){
		return this.hash;
	}

	public void setHash(String hash){
		this.hash = hash;
	}

	public String getStatus(){
		return this.status;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public Date getTimestamp(){
		return this.timestamp;
	}

	public void setTimestamp(Date timestamp){
		this.timestamp = timestamp;
	}
}

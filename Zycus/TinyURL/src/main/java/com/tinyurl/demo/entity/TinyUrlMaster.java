package com.tinyurl.demo.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.tinyurl.demo.constant.TinyUrlConstant;

@Entity
public class TinyUrlMaster {
	@Id
	@GeneratedValue
	private int id;
	private String tinyUrl;
	private String originalUrl;
	private long uniqueNumber;
	private Date createdDate;
	private String status = TinyUrlConstant.ACTIVE_STATUS;

	public int getId() {
		return id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTinyUrl() {
		return tinyUrl;
	}

	public long getUniqueNumber() {
		return uniqueNumber;
	}

	public void setUniqueNumber(long uniqueNumber) {
		this.uniqueNumber = uniqueNumber;
	}

	public void setTinyUrl(String tinyUrl) {
		this.tinyUrl = tinyUrl;
	}

	public String getOriginalUrl() {
		return originalUrl;
	}

	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date date) {
		this.createdDate = date;
	}

}

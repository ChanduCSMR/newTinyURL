package com.tinyurl.demo.serviceimpl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;

import com.tinyurl.demo.constant.TinyUrlConstant;
import com.tinyurl.demo.dao.TinyDao;
import com.tinyurl.demo.dto.TinyResponseDto;
import com.tinyurl.demo.entity.TinyUrlMaster;
import com.tinyurl.demo.service.TinyService;

@org.springframework.stereotype.Service
public class TinyServiceImpl implements TinyService {

	@Autowired
	TinyDao tinyDao;

	private AtomicInteger counter = new AtomicInteger(10);

	@SuppressWarnings("null")
	@Override
	public TinyResponseDto createTinyUrl(String originalUrl) {

		TinyResponseDto tinyResponseDto = tinyDao.checkForDuplicate(originalUrl);
		if (tinyResponseDto != null) {
			return tinyResponseDto;
		} else {

		}
		final long nextnumber = getNextNumber();
		String tinyUrl = convertAndGetBase62Code(nextnumber);

		TinyUrlMaster tinyUrlMaster = new TinyUrlMaster();

		tinyUrlMaster.setCreatedDate(new Date());
		tinyUrlMaster.setOriginalUrl(originalUrl);
		tinyUrlMaster.setTinyUrl(tinyUrl);
		tinyUrlMaster.setUniqueNumber(nextnumber);

		boolean result = tinyDao.updateTinyUrl(tinyUrlMaster);

		if (result) {
			TinyResponseDto tinyResponse = new TinyResponseDto();
			String reportDate = getExpiredDateInString();
			tinyResponse.setResult(TinyUrlConstant.SUCCESS_STATUS);
			tinyResponse.setUrl(tinyUrl);
			tinyResponse.setValidUpto(reportDate);
			return tinyResponse;
		} else {
			return null;
		}
	}

	private String getExpiredDateInString() {
		Date dt = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(dt);
		c.add(Calendar.DATE, 1);
		dt = c.getTime();
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy 00:00:00");
		String reportDate = df.format(dt);
		return reportDate;
	}

	private long getNextNumber() {
		int countervalue = counter.incrementAndGet();
		long systemtime = Long.valueOf("" + countervalue + System.currentTimeMillis());
		return systemtime;
	}

	private String convertAndGetBase62Code(long number) {
		StringBuffer sb = new StringBuffer();
		while (number > 0) {

			int remainder = (int) (number % TinyUrlConstant.BASE);
			sb.append(TinyUrlConstant.CHAR_SET.charAt(remainder));
			number = number / TinyUrlConstant.BASE;
		}
		return sb.toString();
	}

	@Override
	public TinyResponseDto getOriginalUrl(String tinyurl) {
		long tinyUrlId = convertToBase10(tinyurl);
		TinyResponseDto tinyResponseDto = new TinyResponseDto();
		TinyUrlMaster tinyUrlMaster = tinyDao.getUrlByUniqueId(tinyUrlId);
		tinyResponseDto.setUrl("");
		tinyResponseDto.setValidUpto(""
				);
		
		if(tinyUrlMaster == null) {
			tinyResponseDto.setResult(TinyUrlConstant.INVALID_URL);
		}else if(tinyUrlMaster.getStatus().equals(TinyUrlConstant.EXPIRED_STATUS)){
			tinyResponseDto.setResult(TinyUrlConstant.URL_EXPIRED);
		}else{
			tinyResponseDto.setValidUpto(getExpiredDateInString());
			tinyResponseDto.setResult(TinyUrlConstant.SUCCESS_STATUS);
			tinyResponseDto.setUrl(tinyUrlMaster.getOriginalUrl());
		}
		return tinyResponseDto;
	}

	public long convertToBase10(String tinyurlcode) {
		long nBase10 = 0;
		char[] chars = new StringBuilder(tinyurlcode).toString().toCharArray();
		for (int i = chars.length - 1; i >= 0; i--) {
			int index = TinyUrlConstant.CHAR_SET.indexOf(chars[i]);
			nBase10 += index * (long) Math.pow(TinyUrlConstant.BASE, i);
		}
		return nBase10;

	}

	@Override
	public void disableTinyURL() {

		tinyDao.disableTinyURL();
	}

}

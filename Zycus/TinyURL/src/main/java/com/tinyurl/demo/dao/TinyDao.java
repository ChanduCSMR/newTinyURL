package com.tinyurl.demo.dao;

import com.tinyurl.demo.dto.TinyResponseDto;
import com.tinyurl.demo.entity.TinyUrlMaster;

public interface TinyDao {

	TinyResponseDto checkForDuplicate(String originalUrl);

	boolean updateTinyUrl(TinyUrlMaster tinyUrlMaster);

	
	void disableTinyURL();

	TinyUrlMaster getUrlByUniqueId(long tinyUrlId);

}

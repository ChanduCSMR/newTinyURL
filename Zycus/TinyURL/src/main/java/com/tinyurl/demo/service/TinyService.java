package com.tinyurl.demo.service;

import com.tinyurl.demo.dto.TinyResponseDto;

public interface TinyService {

	TinyResponseDto createTinyUrl(String originalUrl);

	TinyResponseDto getOriginalUrl(String tinyurl);

	void disableTinyURL();

}

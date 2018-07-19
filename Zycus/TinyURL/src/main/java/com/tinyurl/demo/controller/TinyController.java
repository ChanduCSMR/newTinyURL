package com.tinyurl.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinyurl.demo.dto.TinyResponseDto;
import com.tinyurl.demo.service.TinyService;

@RestController
public class TinyController {

	@Autowired
	TinyService tinyService;

	@RequestMapping(value = "/createTinyUrl/{originalUrl}", method = RequestMethod.GET)
	public String createTinyUrl(Model model, @PathVariable String originalUrl) throws JsonProcessingException {
		TinyResponseDto tinyResponseDto = tinyService.createTinyUrl(originalUrl);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(tinyResponseDto);

	}

	@RequestMapping(value = "/getOriginalUrl/{tinyUrl}", method = RequestMethod.GET)
	public String getOriginalUrl(Model model, @PathVariable String tinyUrl) throws JsonProcessingException {
		TinyResponseDto tinyResponseDto = tinyService.getOriginalUrl(tinyUrl);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(tinyResponseDto);

	}

	// It will run at every day 12 AM and disable the tiny url (Soft delete)
	@Scheduled(cron = "0 0 0 1/1 * ?")
	public void scheduledTask() {
		tinyService.disableTinyURL();
	}
}

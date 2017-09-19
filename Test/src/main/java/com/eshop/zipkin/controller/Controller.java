package com.eshop.zipkin.controller;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/aa")
public class Controller {

	private Random random = new Random();

	@RequestMapping("bye")
	public String start() throws InterruptedException, IOException {
		int sleep = random.nextInt(100);
		TimeUnit.MILLISECONDS.sleep(sleep);
		return "fuck you";
	}
}

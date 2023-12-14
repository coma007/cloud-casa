package com.casa.app;

import com.casa.app.util.email.FileUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.regex.Pattern;

@SpringBootApplication
@EnableScheduling
public class CasaBackApplication {

	public static void main(String[] args) {
			SpringApplication.run(CasaBackApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		FileUtil.createIfNotExists(FileUtil.imagesDir);
		}

}

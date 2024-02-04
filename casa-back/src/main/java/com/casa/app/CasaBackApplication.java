package com.casa.app;

import com.casa.app.util.email.FileUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class CasaBackApplication {

	public static void main(String[] args) {
			SpringApplication.run(CasaBackApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		FileUtil.createIfNotExists(FileUtil.imagesDir);
		FileUtil.createIfNotExists(FileUtil.profileDir);
		FileUtil.createIfNotExists(FileUtil.estateDir);
		FileUtil.createIfNotExists(FileUtil.deviceDir);

		FileUtil.deleteFolder(FileUtil.profileDir);
		FileUtil.deleteFolder(FileUtil.estateDir);
		FileUtil.deleteFolder(FileUtil.deviceDir);


		}

}

package me.zohar;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;

@SpringBootApplication
@EnableMethodCache(basePackages = "me.zohar.lottery")
@EnableCreateCacheAnnotation
public class MobileApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(MobileApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
	}

}

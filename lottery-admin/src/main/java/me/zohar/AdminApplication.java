package me.zohar;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;

@SpringBootApplication
@EnableMethodCache(basePackages = "me.zohar.lottery")
@EnableCreateCacheAnnotation
public class AdminApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(AdminApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
	}

	@Bean
    public Validator getValidatorFactory(){
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
        		.configure().failFast(true).buildValidatorFactory();
        return validatorFactory.getValidator();
    }
	
}

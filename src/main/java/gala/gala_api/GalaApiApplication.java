package gala.gala_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@SpringBootApplication
public class GalaApiApplication {

	public static void main(String[] args) {

	  //TODO put in properties file
    Properties props = System.getProperties();
    props.setProperty("aws.accessKeyId", "AKIAJLB647EVC4U7H3IQ");
    props.setProperty("aws.secretKey", "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");

	  SpringApplication.run(GalaApiApplication.class, args);
	}
}

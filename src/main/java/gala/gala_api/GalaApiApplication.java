package gala.gala_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@SpringBootApplication
public class GalaApiApplication {

	public static void main(String[] args) {

	  Properties props = System.getProperties();

//	  props.setProperty("aws.accessKeyId", "AKIAI5IBV6L36SK75GRQ");
//	  props.setProperty("aws.secretKey", "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");

	  SpringApplication.run(GalaApiApplication.class, args);
	}
}

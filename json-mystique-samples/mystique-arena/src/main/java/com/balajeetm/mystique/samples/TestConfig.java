package com.balajeetm.mystique.samples;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import springfox.documentation.spring.web.ObjectMapperConfigurer;
import springfox.documentation.spring.web.SpringfoxWebMvcConfiguration;

@Configuration
@Import(SpringfoxWebMvcConfiguration.class)
public class TestConfig {

	/*	@Autowired
		private ObjectMapper objectMapper;
	
		@Bean
		public ObjectMapperConfigurer rer() {
			return new ObjectMapperConfigurer();
		}*/

	@Bean
	public String test(ObjectMapperConfigurer rer) {
		return "Durga";
	}

}

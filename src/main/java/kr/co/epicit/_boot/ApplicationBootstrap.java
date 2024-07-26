package kr.co.epicit._boot;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@SuppressWarnings("unused")
@SpringBootApplication
//@ImportResource({"classpath:/config/spring.boot/context-*.xml"})
@ImportResource({
	"classpath:/config/spring.boot/context-property-placeholder.xml"
	, "classpath:/config/spring.boot/context-component-scan.xml"
	, "classpath:/config/spring.boot/context-servlet-dispatcher.xml"
	, "classpath:/config/spring.boot/context-security.xml"
})
@ComponentScan(value={
	"partners.inspire"
})
public class ApplicationBootstrap {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(ApplicationBootstrap.class);
		//springApplication.setBannerMode(Banner.Mode.OFF);
		springApplication.setLogStartupInfo(false);
		springApplication.run(args);
	}

}

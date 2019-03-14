package ch.fhnw.wodss.webapplication.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket getDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("ch.fhnw.wodss.webapplication.components"))
            .build()
            .useDefaultResponseMessages(false)
            .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
            .title("FHNW Projektplanung (Modul wodss)")
            .description("This documentation provides an overview of the FHNW wodss API.")
            .contact(new Contact("Wächter Simon", "https://github.com/swaechter", "simon.waechter@students.fhnw.ch"))
            .version("1.2.0")
            .build();
    }
}

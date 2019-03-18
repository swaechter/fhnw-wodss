package ch.fhnw.wodss.webapplication.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

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
            .securitySchemes(Collections.singletonList(new ApiKey("Bearer", "Authorization", "header")))
            .securityContexts(Collections.singletonList(SecurityContext.builder().securityReferences(
                Collections.singletonList(SecurityReference.builder()
                    .reference("Bearer")
                    .scopes(new AuthorizationScope[0])
                    .build()
                )).build())
            )
            .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
            .title("FHNW Projektplanung (Modul wodss)")
            .description("This documentation provides an overview of the FHNW wodss API.")
            .contact(new Contact("Wächter Simon", "https://github.com/swaechter/fhnw-wodss-spec", "simon.waechter@students.fhnw.ch"))
            .version("1.0.0")
            .build();
    }
}

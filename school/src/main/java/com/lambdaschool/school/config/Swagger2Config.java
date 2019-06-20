package com.lambdaschool.school.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Import(BeanValidatorPluginsConfiguration.class)
@Configuration
@EnableSwagger2
public class Swagger2Config
{
    @Bean
    public Docket api() // http://editor.swagger.io/
    {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.lambdaschool.school")) // Change this to match your package
                .paths(PathSelectors.regex("/.*"))
                .build()
                //.useDefaultResponseMessages(false) // Allows only my exception responses
                .ignoredParameterTypes(Pageable.class) // allows only my paging parameter list
                .apiInfo(apiEndPointsInfo());
    }

    private ApiInfo apiEndPointsInfo() // View at http://localhost:2019/swagger-ui.html
    {
        return new ApiInfoBuilder().title("School Example") // Modify this
                .description("School Example")
                .contact(new Contact("Basil Havens", "http://www.lambdaschool.com", "basil@lambdaschool.com"))
                .license("MIT").licenseUrl("https://github.com/LambdaSchool/java-crudysnacks/blob/master/LICENSE")
                .version("1.0.0").build();
    }
}

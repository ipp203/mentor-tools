package mentortools;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MentorToolsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MentorToolsApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI().info(new Info()
                .title("Mentor Tools")
                .version("1.0.0")
                .description("Tool for managing students."));
    }

}

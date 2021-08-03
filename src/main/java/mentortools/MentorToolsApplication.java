package mentortools;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@SpringBootApplication
public class MentorToolsApplication {


    public static void main(String[] args) {
        SpringApplication.run(MentorToolsApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public OpenAPI openAPI() {

        return new OpenAPI().info(new Info()
                .title("Mentor Tools")
//                .version("1.0.0")
                .version(readGitPropertiesVersion())
                .description("Tool for managing students."));
    }

    private String readGitPropertiesVersion() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("git.properties");
        try {
            return readFromInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return "Version information could not be retrieved";
        }
    }

    private String readFromInputStream(InputStream inputStream) throws IOException {
        String result = "Version information not found.";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("version")) {
                    int positionOfEqualSign = line.indexOf("=");
                    result = line.substring(positionOfEqualSign + 1);
                    break;
                }
            }
        }
        return result;
    }

}

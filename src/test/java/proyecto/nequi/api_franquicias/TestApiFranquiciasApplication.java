package proyecto.nequi.api_franquicias;

import org.springframework.boot.SpringApplication;

public class TestApiFranquiciasApplication {

    public static void main(String[] args) {
        SpringApplication.from(ApiFranquiciasApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}

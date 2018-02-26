package pe.limatambo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@PropertySource(value="file:src/main/resources/application.properties")
@EnableJpaRepositories(basePackages = {"pe.limatambo.dao"})
@EntityScan(basePackages = {"pe.limatambo"})
@EnableTransactionManagement
public class LimatamboApplication {

    public static void main(String[] args) {
        SpringApplication.run(LimatamboApplication.class, args);
    }
}

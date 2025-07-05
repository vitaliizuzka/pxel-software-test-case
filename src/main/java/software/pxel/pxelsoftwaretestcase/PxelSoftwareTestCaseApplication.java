package software.pxel.pxelsoftwaretestcase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PxelSoftwareTestCaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(PxelSoftwareTestCaseApplication.class, args);
    }

}

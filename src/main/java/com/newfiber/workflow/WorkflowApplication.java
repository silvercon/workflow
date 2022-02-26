package com.newfiber.workflow;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Slf4j
@RestController
@EnableSwagger2
@SpringBootApplication(scanBasePackages = {"com.newfiber.workflow", "com.newfiber.core.exception", "com.newfiber.config"},
        exclude={org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
	        org.flowable.spring.boot.SecurityAutoConfiguration.class})
public class WorkflowApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkflowApplication.class, args);
    }

    @GetMapping("/")
    public void apiVersion(HttpServletResponse response) throws IOException {
        log.info("Successful Deployment");
        PrintWriter writer = response.getWriter();
        writer.append("Version:1.1.0 \n");
        writer.append("Description:newfiber-workflow \n");
        writer.flush();
    }

}

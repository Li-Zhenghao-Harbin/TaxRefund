package org.cityu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello world!
 *
 */
@SpringBootApplication(scanBasePackages = {"org.cityu"})
@RestController
@MapperScan("org.cityu.dao")
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        SpringApplication.run(App.class, args);
    }

    // manager client
//    @Profile("manager")
//    @Bean
//    public void managerRunner() {
//        System.out.println("manager client starts at: " + 8080);
//    }
//
//    // merchant client
//    @Profile("merchant")
//    @Bean
//    public void merchantRunner() {
//        System.out.println("merchant client starts at: " + 8081);
//    }
//
//    // customs client
//    @Profile("customs")
//    @Bean
//    public void customsRunner() {
//        System.out.println("customs client starts at: " + 8082);
//    }
//
//    // agency client
//    @Profile("agency")
//    @Bean
//    public void agencyRunner() {
//        System.out.println("agency client start at: " + 8083);
//    }
}

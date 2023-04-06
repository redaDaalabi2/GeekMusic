package com.example.geekmusic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication


public class GeekMusicApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeekMusicApplication.class, args);
    }

//    @Bean
//    CommandLineRunner runner(UserService userService) {
//        return args -> {
//            userService.saveRole(new Role(null, "AUDITEUR"));
//            userService.saveRole(new Role(null, "AUTEUR"));
//            userService.saveRole(new Role(null, "ADMIN"));
//            userService.saveRole(new Role(null, "SECRETARY"));
//
//            userService.saveUser
//                    (new AppUser(null, "admin","admin@geekmusic", "user1UserName", new BCryptPasswordEncoder().encode("123456"), new ArrayList<>()));
//
//            userService.saveUser(
//                     AppUser.
//                     builder().name("adminName")
//                             .username("admin")
//                             .email("admin@geekmusic.ma").password(new BCryptPasswordEncoder().encode("123456"))
//                             .roles(new ArrayList<>())
//                             .isEnabled(false)
//                    .build()
//            );
////
//            userService.addRoleToUser("admin", "ADMIN");
//
//        };
//    }

}

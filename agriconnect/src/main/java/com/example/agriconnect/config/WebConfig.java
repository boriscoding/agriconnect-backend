package com.example.agriconnect.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 1. Utilise allowedOriginPatterns au lieu de allowedOrigins
                .allowedOrigins(
                        "http://localhost:4200",
                        "http://172.27.208.1:4200",
                        "http://192.168.56.1:4200",
                        "http://192.168.197.1:4200",
                        "http://192.168.226.1:4200",
                        "http://10.177.225.196:4200",
                        "http://10.101.75.196:4200/",
                        "https://unsacked-improvisationally-suanne.ngrok-free.dev",
                        "https://agrilinkbycam.netlify.app"


                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                // 2. C'est ce paramètre qui cause le conflit avec "*"
                .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Détecte si on est sous Windows ou sur un serveur Linux (ex: Render/Cloud)
        String os = System.getProperty("os.name").toLowerCase();
        String uploadPath;

        if (os.contains("win")) {
            // Chemin pour votre PC Windows en local
            uploadPath = "C:/Users/User/Documents/agriconnect (1)/uploads/";
        } else {
            // Chemin par défaut pour un serveur Linux (production)
            uploadPath = "./uploads/";
        }

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath)
                .setCachePeriod(0);
    }
}
package tecnm.itch.fonda.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    // Ya no necesitamos mapear recursos locales a carpetas físicas.
    // Cloudinary maneja las URLs de las imágenes automáticamente.
}
package tecnm.itch.fonda.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Value("${file.upload-dir}")
	private String uploadDir;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		Path uploadPath = Paths.get(uploadDir);
		String uploadPathUri = uploadPath.toUri().toString();
		System.out.println("--- Mapeando la URL /uploads/** a la carpeta física: " + uploadPathUri + " ---");

		registry.addResourceHandler("/uploads/**")
				.addResourceLocations(uploadPathUri.endsWith("/") ? uploadPathUri : uploadPathUri + "/");
	}
}
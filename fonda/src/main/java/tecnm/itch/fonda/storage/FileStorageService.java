package tecnm.itch.fonda.storage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

	private final Path rootLocation;

	@Autowired
	public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
		this.rootLocation = Paths.get(uploadDir);
		try {
			Files.createDirectories(rootLocation);
		} catch (IOException e) {
			throw new RuntimeException("No se pudo inicializar el almacenamiento de archivos", e);
		}
	}

	public String store(MultipartFile file) {
		try {
			if (file.isEmpty()) {
				return null;
			}

			String originalFilename = file.getOriginalFilename();
			String extension = "";
			if (originalFilename != null && originalFilename.contains(".")) {
				extension = originalFilename.substring(originalFilename.lastIndexOf("."));
			}
			String uniqueFilename = UUID.randomUUID().toString() + extension;

			// Guarda el archivo
			Files.copy(file.getInputStream(), this.rootLocation.resolve(uniqueFilename));

			return uniqueFilename;
		} catch (IOException e) {
			throw new RuntimeException("Falló al guardar el archivo.", e);
		}
	}

	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}

	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("No se pudo leer el archivo: " + filename);
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("No se pudo leer el archivo: " + filename, e);
		}
	}

	public void delete(String filename) {
		if (filename == null || filename.isEmpty()) {
			return;
		}
		try {
			Path file = load(filename);
			Files.deleteIfExists(file);
		} catch (IOException e) {
			System.err.println("Falló al borrar el archivo: " + filename);
		}
	}
}
package tecnm.itch.fonda.storage;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class FileStorageService {

	@Autowired
	private Cloudinary cloudinary;

	public String store(MultipartFile file) {
		try {
			if (file.isEmpty()) {
				return null;
			}
			// Subir a Cloudinary
			Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
			
			// Retornar la URL segura (https)
			return uploadResult.get("secure_url").toString();
			
		} catch (IOException e) {
			throw new RuntimeException("Falló la subida de la imagen a Cloudinary", e);
		}
	}

	public void delete(String fotoUrl) {
		if (fotoUrl == null || fotoUrl.isEmpty()) {
			return;
		}
		try {
			// Extraer el "public_id" de la URL de Cloudinary para borrarlo
			String publicId = obtenerPublicId(fotoUrl);
			
			if(publicId != null) {
				cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
			}
		} catch (IOException e) {
			System.err.println("Error al borrar imagen de Cloudinary: " + e.getMessage());
		}
	}
	
	private String obtenerPublicId(String fotoUrl) {
		try {
			// Lógica simple para obtener el ID: último segmento sin extensión
			String[] partes = fotoUrl.split("/");
			String ultimoSegmento = partes[partes.length - 1];
			int puntoIndex = ultimoSegmento.lastIndexOf(".");
			if (puntoIndex != -1) {
				return ultimoSegmento.substring(0, puntoIndex);
			}
			return ultimoSegmento;
		} catch (Exception e) {
			return null;
		}
	}
}
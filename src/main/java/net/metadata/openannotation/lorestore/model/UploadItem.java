package net.metadata.openannotation.lorestore.model;

import org.springframework.web.multipart.MultipartFile;

/**
 * see http://www.ioncannon.net/programming/975/spring-3-file-upload-example/
 * @author uqdayers
 *
 */
public class UploadItem {
	@Deprecated
	private String name;
	private MultipartFile fileData;

	@Deprecated
	public String getName() {
		return name;
	}

	@Deprecated
	public void setName(String name) {
		this.name = name;
	}

	public MultipartFile getFileData() {
		return fileData;
	}

	public void setFileData(MultipartFile fileData) {
		this.fileData = fileData;
	}
}

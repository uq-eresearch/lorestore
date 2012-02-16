package net.metadata.openannotation.lorestore.servlet;

import java.io.FileInputStream;

import net.metadata.openannotation.lorestore.model.UploadItem;
import net.metadata.openannotation.lorestore.servlet.LoreStoreAdminController;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

@RunWith(JUnit4.class)
public class LoreStoreAdminControllerTests extends OREControllerTestsBase {

	private LoreStoreAdminController oreAdminController;

	@Before
	public void adminControllerSetup() {
		oreAdminController = new LoreStoreAdminController(noauthOCC);
	}

	private static final String TEST_FILE = "D:/export.trig";

	@Test
	public void bulkImport() throws Exception {
		FileInputStream inputStream = new FileInputStream(TEST_FILE);

		MockMultipartFile mockMultipartFile = new MockMultipartFile("name",
				"export.trig", "application/x-trig", inputStream);

		BindingResult bindingResult = new BeanPropertyBindingResult(null, "");

		UploadItem uploadItem = new UploadItem();
		uploadItem.setFileData(mockMultipartFile);
		oreAdminController.bulkImport(uploadItem, bindingResult);
	}

}

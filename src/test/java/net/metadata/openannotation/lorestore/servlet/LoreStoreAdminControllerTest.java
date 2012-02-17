package net.metadata.openannotation.lorestore.servlet;

import java.io.FileInputStream;
import java.io.InputStream;

import net.metadata.openannotation.lorestore.model.UploadItem;
import net.metadata.openannotation.lorestore.servlet.LoreStoreAdminController;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class LoreStoreAdminControllerTest extends OREControllerTestsBase {

	private LoreStoreAdminController oreAdminController;

	@Before
	public void adminControllerSetup() {
		oreAdminController = new LoreStoreAdminController(noauthOCC);
	}

	@Test
	public void bulkImport() throws Exception {
		//FileInputStream inputStream = new FileInputStream(TEST_FILE);
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("anno.trig");
		MockMultipartFile mockMultipartFile = new MockMultipartFile("name",
				"export.trig", "application/x-trig", inputStream);

		BindingResult bindingResult = new BeanPropertyBindingResult(null, "");

		UploadItem uploadItem = new UploadItem();
		uploadItem.setFileData(mockMultipartFile);
		
		oreAdminController.bulkImport(uploadItem, bindingResult);
		
	}

}

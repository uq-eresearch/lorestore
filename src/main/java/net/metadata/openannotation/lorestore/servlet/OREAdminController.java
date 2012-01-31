package net.metadata.openannotation.lorestore.servlet;

import java.io.InputStream;
import java.io.Writer;

import javax.servlet.http.HttpServletResponse;

import net.metadata.openannotation.lorestore.access.LoreStoreAccessPolicy;
import net.metadata.openannotation.lorestore.exceptions.InvalidQueryParametersException;
import net.metadata.openannotation.lorestore.model.UploadItem;
import net.metadata.openannotation.lorestore.servlet.rdf2go.RDF2GoOACQueryHandler;
import net.metadata.openannotation.lorestore.servlet.rdf2go.RDF2GoOACUpdateHandler;
import net.metadata.openannotation.lorestore.servlet.rdf2go.RDF2GoOREQueryHandler;
import net.metadata.openannotation.lorestore.servlet.rdf2go.RDF2GoOREUpdateHandler;

import org.apache.log4j.Logger;
import org.openrdf.rio.RDFFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import au.edu.diasb.chico.mvc.RequestFailureException;

@Controller
public class OREAdminController {
	private final Logger LOG = Logger.getLogger(OREAdminController.class);

	private final LoreStoreControllerConfig occ;
	private LoreStoreUpdateHandler uh;
	private LoreStoreQueryHandler qh;
	private LoreStoreUpdateHandler ouh;
	private LoreStoreQueryHandler oqh;
	private LoreStoreAccessPolicy ap;

	public OREAdminController(LoreStoreControllerConfig occ) {
		this.occ = occ;
		this.uh = new RDF2GoOREUpdateHandler(occ);
		this.qh = new RDF2GoOREQueryHandler(occ);
		this.ouh = new RDF2GoOACUpdateHandler(occ);
		this.oqh = new RDF2GoOACQueryHandler(occ);
		this.ap = occ.getAccessPolicy();
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model) {
		ap.checkAdmin();
		return "admin/index";
	}
	
	@RequestMapping(value = "/import", method = RequestMethod.GET)
	public String importData(Model model) {
		ap.checkAdmin();
		model.addAttribute(new UploadItem());
		return "admin/importForm";
	}
	
	@RequestMapping(value = "/import", method = RequestMethod.POST)
	public String bulkImport(UploadItem uploadItem, BindingResult result) throws Exception {
		ap.checkAdmin();
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				LOG.error("Error: " + error.getCode() +   " - " + error.getDefaultMessage());
				return "admin/importForm";
			}
		}
		// Some type of file processing
		InputStream fileData = uploadItem.getFileData().getInputStream();
		String originalFilename = uploadItem.getFileData().getOriginalFilename();
		uh.bulkImport(fileData, originalFilename);

		return "redirect:/oreadmin/";
	}
	
	@RequestMapping(value = "/stats.html", method = RequestMethod.GET)
	public String getStats(Model model) throws InterruptedException, RequestFailureException {
		ap.checkAdmin();
		model.addAttribute("numTriples",qh.getNumberTriples());
		model.addAttribute("numResourceMaps",qh.getNumberNamedGraphs());
		model.addAttribute("numAnnotations", oqh.getNumberNamedGraphs());
		return "admin/stats";
	}
	
	@RequestMapping(value = "/wipeDatabase.html", method = RequestMethod.GET)
	public String confirmWipe() throws RequestFailureException {
		ap.checkAdmin();
		return "admin/confirmWipe";
	}
	
	@RequestMapping(value = "/wipeDatabase.html", method = RequestMethod.POST)
	public String wipeDatabase() throws InterruptedException, RequestFailureException {
		ap.checkAdmin();
		uh.wipeDatabase();
		return "redirect:/ore/admin/";
	}

	@RequestMapping(value = "/export")
	public void export(HttpServletResponse response) throws Exception {
		response.setContentType(RDFFormat.TRIG.getDefaultMIMEType());
		String filename = "export." + RDFFormat.TRIG.getDefaultFileExtension();
		response.setHeader("Content-Disposition", "attachment; filename="+filename);
		Writer outputWriter = response.getWriter();
		qh.exportAll(outputWriter);
	}

	@RequestMapping(value = "/sparqlPage.html")
	public String sparqlPage() {
		ap.checkAdmin();
		return "admin/sparqlPage";
	}

	@RequestMapping(value = "/sparql", params = "sparql", method = RequestMethod.GET)
	public ResponseEntity<String> sparqlQuery(@RequestParam("sparql") String sparqlQuery) throws Exception {
		if (sparqlQuery == null || sparqlQuery.isEmpty()) {
			throw new InvalidQueryParametersException(
					"Missing or empty query parameters");
		}
		
		return qh.sparqlQuery(sparqlQuery);
	}
	
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler({ RequestFailureException.class })
	public void return404() {
		LOG.debug("Handling RequestFailureException, returning 401");
	}

	public LoreStoreControllerConfig getControllerConfig() {
		return occ;
	}
}

package net.metadata.openannotation.lorestore.servlet;

import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.metadata.openannotation.lorestore.access.LoreStoreAccessPolicy;
import net.metadata.openannotation.lorestore.exceptions.InvalidQueryParametersException;
import net.metadata.openannotation.lorestore.model.UploadItem;
import net.metadata.openannotation.lorestore.servlet.rdf2go.RDF2GoOAQueryHandler;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import au.edu.diasb.chico.mvc.RequestFailureException;

@Controller
public class LoreStoreAdminController {
	private final Logger LOG = Logger.getLogger(LoreStoreAdminController.class);
	private Map<String, Object> propsMap;
	
	private final LoreStoreControllerConfig occ;
	private LoreStoreUpdateHandler uh;
	private LoreStoreQueryHandler qh;

	private LoreStoreQueryHandler oqh;
	private LoreStoreAccessPolicy ap;

	public LoreStoreAdminController(LoreStoreControllerConfig occ) {
		this.occ = occ;
		this.uh = new RDF2GoOREUpdateHandler(occ);
		this.qh = new RDF2GoOREQueryHandler(occ);
		this.oqh = new RDF2GoOAQueryHandler(occ);
		this.ap = occ.getAccessPolicy();
	}

	public final void setPropertiesList(List<Map<?, ?>> props) {
	        propsMap = new HashMap<String, Object>();
	        for (Map<?,?> p : props) {
	            for (Object k : p.keySet()) {
	                propsMap.put((String) k, p.get(k));
	            }
	        }
	    }
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model) {
		ap.checkAdmin();
		model.addAttribute("props",propsMap);
		return "admin/index";
	}
	
	@RequestMapping(value = "/import", method = RequestMethod.GET)
	public String importData(Model model) {
		ap.checkAdmin();
		model.addAttribute(new UploadItem());
		model.addAttribute("props",propsMap);
		return "admin/importForm";
	}
	
	@RequestMapping(value = "/import", method = RequestMethod.POST)
	public ModelAndView bulkImport(UploadItem uploadItem, BindingResult result) throws Exception {
		ap.checkAdmin();
		if (result.hasErrors()) {
			ModelAndView redirectView = new ModelAndView(new RedirectView("admin/importForm",false ,true, true));
			String errorMessage = "";
			for (ObjectError error : result.getAllErrors()) {
				LOG.error("Error: " + error.getCode() +   " - " + error.getDefaultMessage());
				errorMessage += error.getDefaultMessage() + "<br/>";				
			}
			redirectView.getModel().put("message", "Error: " + errorMessage);
			redirectView.addObject("props",propsMap);
			return redirectView;
		}
		String message = "";
		RedirectView rView = null;
		try {
			InputStream fileData = uploadItem.getFileData().getInputStream();
			String originalFilename = uploadItem.getFileData().getOriginalFilename();
			int delta = uh.bulkImport(fileData, originalFilename);
			rView = new RedirectView("admin",false ,true, true);
			rView.getAttributesMap().put("delta", delta);
			message = "Successfully imported data";
		} catch (Exception e){
			rView = new RedirectView("admin/importForm",false ,true, true);
			message = "Error importing data: " + e.getMessage();
		}
		rView.getAttributesMap().put("message", message);
		ModelAndView redirectView = new ModelAndView(rView);
		redirectView.addObject("props",propsMap);
		return redirectView;
	}
	
	@RequestMapping(value = "/stats.html", method = RequestMethod.GET)
	public String getStats(Model model) throws InterruptedException, RequestFailureException {
		ap.checkAdmin();
		model.addAttribute("numTriples",qh.getNumberTriples());
		model.addAttribute("numResourceMaps",qh.getNumberNamedGraphs());
		model.addAttribute("numAnnotations", oqh.getNumberNamedGraphs());
		model.addAttribute("props",propsMap);
		return "admin/stats";
	}
	
	@RequestMapping(value = "/wipeDatabase.html", method = RequestMethod.GET)
	public String confirmWipe(Model model) throws RequestFailureException {
		ap.checkAdmin();
		model.addAttribute("props",propsMap);
		return "admin/confirmWipe";
	}
	
	@RequestMapping(value = "/deleteGraph.html", method = RequestMethod.GET)
	public String deleteGraph(Model model) throws RequestFailureException {
		ap.checkAdmin();
		model.addAttribute("props",propsMap);
		return "admin/deleteGraph";
	}
	
	@RequestMapping(value = "/updateGraph.html", method = RequestMethod.GET)
	public String updateGraph(Model model) throws RequestFailureException {
		ap.checkAdmin();
		model.addAttribute("props",propsMap);
		return "admin/updateGraph";
	}
	
	@RequestMapping(value = "/wipeDatabase.html", method = RequestMethod.POST)
	public String wipeDatabase() throws InterruptedException, RequestFailureException {
		ap.checkAdmin();
		uh.wipeDatabase();
		return "redirect:/admin/";
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
	public String sparqlPage(Model model) {
		ap.checkAdmin();
		model.addAttribute("props",propsMap);
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

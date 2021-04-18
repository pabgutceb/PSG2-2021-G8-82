package org.springframework.samples.petclinic.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.Causes;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.CauseService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CauseController {
	
	private static final String VIEWS_CAUSES_CREATE_OR_UPDATE_FORM = "causes/createOrUpdateCauseForm";
	private final CauseService causeService;
	private final OwnerService ownerService;
	
	@Autowired
	public CauseController(final CauseService causeService, final OwnerService ownerService) {
		this.causeService = causeService;
		this.ownerService = ownerService;
	}
	
	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@InitBinder("owner")
	public void initOwnerBinder(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@GetMapping(value = "/causes/new")
	public String initCreationForm(final Owner owner, final Map<String,Object> model) {
		final Cause cause = new Cause();
		cause.setIsClosed(false);
		model.put("cause", cause);
		return CauseController.VIEWS_CAUSES_CREATE_OR_UPDATE_FORM;
	}
	
	@PostMapping(value = "/causes/new")
	public String processCreationForm(@Valid final Cause cause, final BindingResult result) {
		if(result.hasErrors()) {
			return CauseController.VIEWS_CAUSES_CREATE_OR_UPDATE_FORM;
		} else {
			final Owner principal = this.ownerService.getPrincipal();
			cause.setOwner(principal);
			this.causeService.saveCause(cause);
			return "redirect:/causes";
		}
	}
	
	@GetMapping(value = "/causes/{causeId}/edit")
	public String initEditingForm(@PathVariable("causeId") final int causeId, final Model model) {
		final Cause cause = this.causeService.findCauseById(causeId);
		model.addAttribute(cause);
		return CauseController.VIEWS_CAUSES_CREATE_OR_UPDATE_FORM;
	}
	
	@PostMapping(value = "/causes/{causeId}/edit")
	public String processEditingForm(@Valid final Cause cause, final BindingResult result, @PathVariable("causeId") final int causeId) {
		if(result.hasErrors()) {
			return CauseController.VIEWS_CAUSES_CREATE_OR_UPDATE_FORM;
		} else {
			cause.setId(causeId);
			this.causeService.saveCause(cause);
			return "redirect:/causes";
		}
	}
	
	@GetMapping(value = "/causes/{causeId}/delete")
	public String deleteCause(@PathVariable("causeId") final int causeId, final ModelMap model, final RedirectAttributes redirectAttributes) {
		final Cause cause = this.causeService.findCauseById(causeId);
		if (cause.getId()!=null) {
			this.causeService.delete(cause);
			redirectAttributes.addFlashAttribute("message", "¡Veterinario borrado con éxito!");
		} else {
			redirectAttributes.addFlashAttribute("message", "¡Veterinario no encontrado!");
		}
		return "redirect:/vets";
	}
	
	@GetMapping(value = { "/causes" })
	public String showCauseList(final Map<String, Object> model) {
		
		final Causes causes = new Causes();
		causes.getCauseList().addAll(this.causeService.findCauses());
		model.put("causes", causes);
		return "causes/causeList";
	}
	
	

}

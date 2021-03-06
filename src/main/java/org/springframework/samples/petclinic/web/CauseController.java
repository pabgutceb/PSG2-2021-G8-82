package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.Causes;
import org.springframework.samples.petclinic.model.Donation;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.CauseService;
import org.springframework.samples.petclinic.service.DonationService;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CauseController {
	
	private static final String VIEWS_CAUSES_CREATE_OR_UPDATE_FORM = "causes/createOrUpdateCauseForm";
	private static final String VIEWS_DONATIONS_CREATE_OR_UPDATE_FORM = "causes/createOrUpdateDonationForm";
	private final CauseService causeService;
	private final OwnerService ownerService;
	private final DonationService donationService;
	private static final String VIEW_CAUSES_LIST = "redirect:/causes";
	private static final String donacion = "donation";
	private static final String causa = "cause";
	
	@Autowired
	public CauseController(final CauseService causeService, final OwnerService ownerService,final DonationService donationService) {
		this.causeService = causeService;
		this.ownerService = ownerService;
		this.donationService=donationService;
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
	public String processCreationForm(@Valid final Cause cause, final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			for (int x = 0; x < cause.getName().length(); x++) {
				final char c = cause.getName().charAt(x);
				// Si no est?? entre a y z, ni entre A y Z, ni es un espacio
				if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == ' ')) {
					result.rejectValue("name", "invalidFormat", "No se admiten caracteres num??ricos en el nombre");
					return CauseController.VIEWS_CAUSES_CREATE_OR_UPDATE_FORM;

				}
				

			}
			return CauseController.VIEWS_CAUSES_CREATE_OR_UPDATE_FORM;
		}
		else {
			for (int x = 0; x < cause.getName().length(); x++) {
				final char c = cause.getName().charAt(x);
				// Si no est?? entre a y z, ni entre A y Z, ni es un espacio
				if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == ' ')) {
					result.rejectValue("name", "invalidFormat", "No se admiten caracteres num??ricos en el nombre");
					return CauseController.VIEWS_CAUSES_CREATE_OR_UPDATE_FORM;

				}
				

			}
			this.causeService.saveCause(cause);
			return CauseController.VIEW_CAUSES_LIST;

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
			return CauseController.VIEW_CAUSES_LIST;
		}
	}
	
	@GetMapping(value = "/causes/{causeId}/delete")
	public String deleteCause(@PathVariable("causeId") final int causeId, final ModelMap model, final RedirectAttributes redirectAttributes) {
		final Cause cause = this.causeService.findCauseById(causeId);
		if (cause.getId()!=null) {
			this.causeService.delete(cause);
			redirectAttributes.addFlashAttribute("message", "??Veterinario borrado con ??xito!");
		} else {
			redirectAttributes.addFlashAttribute("message", "??Veterinario no encontrado!");
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
	
	@GetMapping(value = "/causes/{causeId}/donations/new")
    public String initCreationForm(@PathVariable("causeId") final int causeId, final ModelMap model) {
		 final Cause cause= this.causeService.findCauseById(causeId);

		 if (cause.getIsClosed()){
             return CauseController.VIEW_CAUSES_LIST;
         } 
        final Donation donation = new Donation();
        cause.addDonation(donation);
        donation.setDonationDate(LocalDate.now());
        model.put(CauseController.donacion, donation);
        return CauseController.VIEWS_DONATIONS_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping(value = "/causes/{causeId}/donations/new")
    public String processCreationForm(@PathVariable("causeId") final int causeId, @Valid final Donation donation, final BindingResult result, final ModelMap model) {
       final Cause cause= this.causeService.findCauseById(causeId);

    	   if (cause.getIsClosed()){
               result.rejectValue("client", "closed");
               result.rejectValue("amount", "closed");
               return CauseController.VIEW_CAUSES_LIST;
           } 
            if (result.hasErrors()) {
                model.put(CauseController.donacion, donation);
                return CauseController.VIEWS_DONATIONS_CREATE_OR_UPDATE_FORM;
            } else {
            	final Owner principal = this.ownerService.getPrincipal();
    			final Double updateBudget= donation.getAmount()+cause.getTotalBudget();
            	if((cause.getBudgetTarget()- updateBudget)==0) {
                	cause.setIsClosed(true);
                }else if((cause.getBudgetTarget()- updateBudget)<0){

                	model.put(CauseController.donacion, donation);
                	result.rejectValue("amount", "passLimits", "The amount of the donation pass the limit of the cause. Total amount available: "+

                	(cause.getBudgetTarget()-cause.getTotalBudget()));
                    return CauseController.VIEWS_DONATIONS_CREATE_OR_UPDATE_FORM;
                }
            	cause.addDonation(donation);
            	donation.setCause(cause);
            	donation.setClient(principal);
                this.donationService.saveDonation(donation);
                cause.setTotalBudget(updateBudget);
                this.causeService.saveCause(cause);
                       
            } 
       
        return CauseController.VIEW_CAUSES_LIST;
        }
	
	@GetMapping("/causes/{causeId}")
	public ModelAndView showOwner(@PathVariable("causeId") final int causeId) {
		final ModelAndView mav = new ModelAndView("causes/causeDetails");
		mav.addObject(this.causeService.findCauseById(causeId));
		return mav;
	}

}

package com.directory.controller;


import com.directory.entity.Address;
import com.directory.entity.CardInfo;
import com.directory.entity.Contact;
import com.directory.entity.User;
import com.directory.repository.CardRepository;
import com.directory.repository.ContactRepository;
import com.directory.repository.UserService;
import com.directory.service.ContactServiceImpl;
import com.directory.service.Utility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;


@Slf4j
@Controller
public class ContactController {
    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);
    private final ContactRepository repo;
    private final CardRepository cardRepo;
    private final ContactServiceImpl serviceImpl;
    ResourceLoader resourceLoader;
    @Value("${contact.shutdown.url}")
    private String shutdownURL;
    private final UserService userService;

    @Autowired
    public ContactController(ContactRepository repo, CardRepository cardRepo, ContactServiceImpl serviceImpl, ResourceLoader resourceLoader, UserService userService) {
        this.repo = repo;
        this.cardRepo = cardRepo;
        this.serviceImpl = serviceImpl;
        this.resourceLoader = resourceLoader;
        this.userService = userService;
    }

    @GetMapping(
            value = {"/listcontacts"},
            produces = {"application/json"}
    )
    @ResponseBody
    public ResponseEntity<String> initializeTable() throws JsonProcessingException {
        try {
            List<Contact> list = this.repo.findAllContacts();
            return ResponseEntity.status(HttpStatus.OK).body(Utility.generateJSON(list));
        } catch (Exception e) {
            logger.error("initializeTable -- {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ObjectMapper()).enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString("Αδυναμία έυρεσης επαφής."));
        }
    }

    @GetMapping(
            value = {"/listcontacts/{from}/{until}"},
            produces = {"application/json"}
    )
    @ResponseBody
    public ResponseEntity<String> initializeTableDates(@PathVariable(value = "from",required = false) String from, @PathVariable(value = "until",required = false) String until) throws JsonProcessingException {
        try {
            List<Contact> list = this.repo.findContactBetweenDate(Utility.stringToDate(from), Utility.stringToDate(until));
            return ResponseEntity.status(HttpStatus.OK).body(Utility.generateJSON(list));
        } catch (Exception e) {
            logger.error("initializeTableDates -- {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ObjectMapper()).enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString("Αδυναμία εύρεσης επαφών."));
        }
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login?logout";
    }


    @GetMapping({"/main"})
    public String showMainForm() {
        return "main";
    }

    @GetMapping({"/cards"})
    public String showCards() {
        return "cards";
    }


    @GetMapping({"/pages-login"})
    public String showLogin() {
//        User user = new User();
//        model.addAttribute("user", user);
        return "pages-login";
    }

    @RequestMapping({"/login-error"})
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "pages-login";
    }

    @GetMapping({"/pages-register"})
    public String showRegistrationForm(Model model) {
        User user = new User();

        model.addAttribute("user", user);
        return "pages-register";
    }


    @PostMapping({"/pages-register/save"})
    public String registration(@ModelAttribute("user") @Valid User userDto, BindingResult result) {
        User existingUser = this.userService.findUserByEmail(userDto.getEmail());

        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", null, "There is already an account registered with the same email");
        }

        if (result.hasErrors()) {
            return "/pages-register";
        } else {
            this.userService.saveUser(userDto);
            return "redirect:/pages-login";
        }
    }

    @GetMapping(
            value = {"/showcontact"},
            produces = {"application/json"}
    )
    @ResponseBody
    public ResponseEntity<String> showContact(@RequestParam("searchfor") String searchfor,
                                              @RequestParam("dropDownList") String dropDownList) throws JsonProcessingException {
        List<Contact> contact = null;

        try {
            if (dropDownList == null) {
                System.out.println("dropdown list is null");
            } else if (searchfor != null && !searchfor.equals("".trim())) {
                contact = switch (dropDownList) {
                    case "Εταιρεία" -> this.serviceImpl.findContactByCompany(searchfor);
                    case "Υπεύθυνο" -> this.serviceImpl.findContactByManager(searchfor);
                    case "Τηλέφωνο" -> this.serviceImpl.findContactByTelephone(searchfor);
                    case "Διεύθυνση" -> this.serviceImpl.findContactByAddressStreet(searchfor);
                    case "Πόλη" -> this.serviceImpl.findContactByAddressCity(searchfor);
                    case "ΑΦΜ" -> this.serviceImpl.findContactByAFM(searchfor);
                    default -> contact;
                };
            } else {
                System.out.println("search for is null");
            }

            ObjectMapper mapper = (new ObjectMapper()).enable(SerializationFeature.INDENT_OUTPUT);
            if (contact == null || contact.isEmpty()) {
                return ResponseEntity.ok(mapper.writeValueAsString("Η επαφή με τα στοιχεία που δώσατε δεν βρέθηκε."));
            }

            if (contact.size() > 1) {
                return ResponseEntity.ok(mapper.writeValueAsString("Βρέθηκαν περισσότερες απο μια επαφές με τα στοιχεία που δώσατε. Παρακαλώ συγκεκριμενοποιήστε την αναζήτησή σας."));
            }
        } catch (Exception e) {
            logger.error("showContact -- error in showing contact {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ObjectMapper()).enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString("Αδυναμία έυρεσης επαφής."));
        }

        return ResponseEntity.status(HttpStatus.OK).body(Utility.generateJSON((Contact)contact.get(0)));
    }

    @PutMapping(
            value = {"/editcontact/{contact_no}"},
            produces = {"application/json"}
    )
    @ResponseBody
    public ResponseEntity<String> editContact(@PathVariable("contact_no") String contact_no, @RequestParam("date_recorded") String date_recorded, @RequestParam("company") String company, @RequestParam("manager") String manager, @RequestParam("nomos") String nomos, @RequestParam("city") String city, @RequestParam("zip") String zip, @RequestParam("street") String street, @RequestParam("area") String area, @RequestParam("telephone") String telephone, @RequestParam(value = "email",required = false) String email, @RequestParam(value = "fax",required = false) String fax, @RequestParam(value = "orders",required = false) String orders, @RequestParam(value = "comments",required = false) String comments, @RequestParam(value = "seller",required = false) String seller, @RequestParam(value = "last_comm_date",required = false) String last_comm_date, @RequestParam(value = "completed",required = false) String completed, @RequestParam(value = "followup",required = false) String followup, @RequestParam(value = "ignored",required = false) String ignored, @RequestParam(value = "afm",required = false) String afm) throws JsonProcessingException {
        try {
            if (!email.isEmpty() && !Utility.validateEmail(email)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ObjectMapper()).enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString("Η καταχώρηση απέτυχε. Παρακαλώ ελέγξτε το email."));
            } else if (!fax.isEmpty() && !Utility.validateTelephone(fax)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ObjectMapper()).enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString("Η καταχώρηση απέτυχε. Παρακαλώ ελέγξτε το fax."));
            } else if (!telephone.isEmpty() && !Utility.validateTelephone(telephone)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ObjectMapper()).enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString("Η καταχώρηση απέτυχε. Παρακαλώ ελέγξτε το τηλέφωνο."));
            } else {
                int res = this.serviceImpl.updateContact(date_recorded.isEmpty() ? null : date_recorded, telephone, email, street, area, city, nomos, zip, seller, manager, company, afm, comments, completed.equals("1") ? Boolean.TRUE : Boolean.FALSE, followup.isEmpty() ? null : followup, ignored.equals("1") ? Boolean.TRUE : Boolean.FALSE, last_comm_date.equals("") ? null : last_comm_date, orders, Integer.valueOf(contact_no));
                ObjectMapper mapper = (new ObjectMapper()).enable(SerializationFeature.INDENT_OUTPUT);
                return res > 0 ? ResponseEntity.ok(mapper.writeValueAsString("Η ενημέρωση με Α/Α = " + contact_no + " ήταν επιτυχής")) : ResponseEntity.ok(mapper.writeValueAsString("Προέκυψε σφάλμα στην ενημέρωση με Α/Α = " + contact_no));
            }
        } catch (Exception e) {
            logger.error("editContact -- {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ObjectMapper()).enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString("Η ενημέρωση με Α/Α = " + contact_no + " απέτυχε"));
        }
    }


    @GetMapping(
            value = {"/showcontact_tbl/{contact_no}"},
            produces = {"application/json"}
    )
    @ResponseBody
    public ResponseEntity<String> editContactFromTable(@PathVariable("contact_no") String contact_no) throws JsonProcessingException {
        try {
            Contact contact = this.serviceImpl.findByContactNo(Integer.parseInt(contact_no));
            ObjectMapper mapper = (new ObjectMapper()).enable(SerializationFeature.INDENT_OUTPUT);
            return ResponseEntity.ok(mapper.writeValueAsString(contact));
        } catch (Exception e) {
            logger.error("editContactFromTable -- {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ObjectMapper()).enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString("Η ενημέρωση της επαφής με Α/Α = " + contact_no + " απέτυχε"));
        }
    }


    @PutMapping(
            value = {"/card_update"},
            produces = {"application/json"}
    )
    @ResponseBody
    public ResponseEntity<Void> updateCard(@RequestParam("id") String id,
                                           @RequestParam("profession") String profession,
                                           @RequestParam("company") String company,
                                           @RequestParam("telephone") String telephone,
                                           @RequestParam("cellphone") String cellphone,
                                           @RequestParam("email") String email,
                                           @RequestParam("firstName") String firstName,
                                           @RequestParam("lastName") String lastName,
                                           @RequestParam("address") String address,
                                           @RequestParam("www") String www) {
        try {
            CardInfo card = new CardInfo();
            card.setAddress(address);
            card.setCompany(company);
            card.setEmail(email);
            card.setFirstName(firstName);
            card.setLastName(lastName);
            card.setProfession(profession);
            card.setTelephone(telephone);
            card.setCellPhone(cellphone);
            card.setEmail(email);
            card.setWww(www);
            card.setId(Long.parseLong(id));
            cardRepo.save(card);
        } catch (Exception e) {
            logger.error("Failed to update card with id = {} with error message {}", id, e.getMessage());
        }

        return ResponseEntity.noContent().build();
    }

//    @GetMapping(
//            value = {"/card_info"},
//            produces = {"application/json"}
//    )
//    @ResponseBody
//    public ResponseEntity<String> showCardInfo() throws JsonProcessingException {
//        try {
//            List<CardInfo> cardInfo = this.cardRepo.findAll();
//            ObjectMapper mapper = (new ObjectMapper()).enable(SerializationFeature.INDENT_OUTPUT);
//            System.out.println(mapper.writeValueAsString(cardInfo));
//            return ResponseEntity.ok(mapper.writeValueAsString(cardInfo));
//        } catch (Exception var3) {
//            Exception iae = var3;
//            logger.error("showCardInfo -- " + iae.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ObjectMapper()).enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString("Η ανάκτηση των καρτών απέτυχε"));
//        }
//    }

    @GetMapping(
            value = {"/savecontact"},
            produces = {"application/json"}
    )
    @ResponseBody
    public ResponseEntity<String> saveContact(@RequestParam("date_recorded") String date_recorded, @RequestParam("company") String company, @RequestParam("manager") String manager, @RequestParam(value = "nomos",required = false) String nomos, @RequestParam(value = "city",required = false) String city, @RequestParam(value = "zip",required = false) String zip, @RequestParam(value = "street",required = false) String street, @RequestParam(value = "area",required = false) String area, @RequestParam("telephone") String telephone, @RequestParam(value = "email",required = false) String email, @RequestParam(value = "fax",required = false) String fax, @RequestParam(value = "orders",required = false) String orders, @RequestParam(value = "comments",required = false) String comments, @RequestParam(value = "seller",required = false) String seller, @RequestParam(value = "last_comm_date",required = false) String last_comm_date, @RequestParam(value = "completed",required = false) String completed, @RequestParam(value = "followup",required = false) String followup, @RequestParam(value = "ignored",required = false) String ignored, @RequestParam(value = "afm",required = false) String afm) throws JsonProcessingException {
        try {
            Contact contact = new Contact();
            contact.setContact_no(Utility.generateRandomContactCode());
            if (!email.isEmpty()) {
                if (!Utility.validateEmail(email)) {
                    logger.error("saveContact -- Error is setting email");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ObjectMapper()).enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString("Η καταχώρηση της επαφής απέτυχε. Παρακαλώ ελέγξτε το email."));
                }

                contact.setEmail(email);
            } else {
                contact.setEmail(null);
            }

            contact.setAfm(afm);
            contact.setComments(comments);
            contact.setCompany(company);
            contact.setCompleted(completed.equals("1") ? Boolean.TRUE : Boolean.FALSE);
            if (date_recorded.isEmpty()) {
                contact.setDate_recorded(null);
            } else {
                contact.setDate_recorded(Utility.stringToDate(date_recorded));
            }

            if (!fax.isEmpty()) {
                if (!Utility.validateTelephone(fax)) {
                    logger.error("saveContact -- Error is setting fax");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ObjectMapper()).enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString("Η καταχώρηση της επαφής απέτυχε. Παρακαλώ ελέγξτε το fax."));
                }

                contact.setFax(fax);
            } else {
                contact.setFax((String)null);
            }

            if (followup.isEmpty()) {
                contact.setFollowup(null);
            } else {
                contact.setFollowup(Utility.stringToDate(followup));
            }

            contact.setIgnored(ignored.equals("1") ? Boolean.TRUE : Boolean.FALSE);
            contact.setManager(manager);
            contact.setOrders(orders);
            if (last_comm_date.isEmpty()) {
                contact.setLast_comm_date((String)null);
            } else {
                contact.setLast_comm_date(Utility.stringToDate(last_comm_date));
            }

            if (!telephone.isEmpty()) {
                if (!Utility.validateTelephone(telephone)) {
                    logger.error("saveContact -- Error is setting telephone");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ObjectMapper()).enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString("Η καταχώρηση απέτυχε. Παρακαλώ ελέγξτε τον αρ. τηλεφώνου."));
                }

                contact.setTelephone(telephone);
            } else {
                contact.setTelephone((String)null);
            }

            contact.setSeller(seller);
            Address address = new Address();
            address.setArea(area);
            address.setCity(city);
            address.setNomos(nomos);
            address.setStreet(street);
            address.setZip(zip);
            contact.setAddress(address);
            this.repo.save(contact);
            return ResponseEntity.status(HttpStatus.OK).body((new ObjectMapper()).enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString("Η καταχώρηση της επαφής πέτυχε"));
        } catch (Exception e) {
            logger.error("saveContact -- {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ObjectMapper()).enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString("Η καταχώρηση της επαφής απέτυχε"));
        }
    }


    @DeleteMapping(
            value = {"/delete/{contact_no}"},
            produces = {"application/json"}
    )
    @ResponseBody
    public ResponseEntity<String> deleteContact(@PathVariable("contact_no") String contact_no) throws JsonProcessingException {
        try {
            this.repo.deleteContact(Integer.valueOf(contact_no));
            return ResponseEntity.status(HttpStatus.OK).body((new ObjectMapper()).enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString("Η διαγραφή της επαφής με Α/Α = " + contact_no + " ήταν επιτυχής"));
        } catch (Exception e) {
            logger.error("deleteContact -- {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ObjectMapper()).enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString("Η διαγραφή της επαφής με Α/Α = " + contact_no + " απέτυχε"));
        }
    }


    @PostMapping(
            value = {"/shutdown"},
            produces = {"application/json"}
    )
    @ResponseBody
    public ResponseEntity<String> shutdownContacts() throws JsonProcessingException {
        try {
            String ans = Utility.shutdownRequest(this.shutdownURL);
            return ResponseEntity.status(HttpStatus.OK).body((new ObjectMapper()).enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(ans));
        } catch (Exception var2) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ObjectMapper()).enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString("Η έξοδος απο την εφαρμογή απέτυχε."));
        }
    }


    @GetMapping(
            value = {"/showcard"},
            produces = {"application/json"}
    )
    public ResponseEntity<?> showCardInformation() {
        try {
            List<?> cards = this.cardRepo.findAll();
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            return ResponseEntity.status(HttpStatus.OK).body(mapper.enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(cards));
        } catch (Exception var3) {
            try {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ObjectMapper()).enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString("Η εμφάνιση των επαγγελματικών καρτών απέτυχε."));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @GetMapping(
            value = {"/showcard/refresh"},
            produces = {"application/json"}
    )
    @ResponseBody
    public ResponseEntity<String> refreshCardInformation() throws JsonProcessingException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body((new ObjectMapper()).enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(this.cardRepo.findAll()));
        } catch (Exception var2) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ObjectMapper()).enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString("Η ανάγνωση του αρχείου των νομών απέτυχε"));
        }
    }


    @PostMapping(
            value = {"/contactcard/save"},
            produces = {"application/json"}
    )
    @ResponseBody
    public ResponseEntity<String> saveCard(@RequestParam("email") String email,
                                           @RequestParam("firstName") String firstName,
                                           @RequestParam("lastName") String lastName,
                                           @RequestParam("company") String company,
                                           @RequestParam("profession") String profession,
                                           @RequestParam("address") String address,
                                           @RequestParam("www") String www,
                                           @RequestParam("telephone") String telephone,
                                           @RequestParam("cellphone") String cellphone) throws JsonProcessingException {
        try {
            CardInfo ci = new CardInfo();
            ci.setAddress(address);
            ci.setDate_recorded(Instant.now());
            ci.setCellPhone(cellphone);
            ci.setTelephone(telephone);
            ci.setCompany(company);
            ci.setEmail(email);
            ci.setFirstName(firstName);
            ci.setLastName(lastName);
            ci.setProfession(profession);
            ci.setWww(www);
            this.cardRepo.save(ci);
            return ResponseEntity.status(HttpStatus.OK).body((new ObjectMapper()).enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString("Η καταχώρηση της επαφής πέτυχε"));
        } catch (Exception e) {
            logger.error("saveContact -- {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ObjectMapper()).enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString("Η καταχώρηση της επαφής απέτυχε"));
        }
    }

    @DeleteMapping({"/card_delete/{id}"})
    public ResponseEntity<Void> deleteCard(@PathVariable("id") String id) {
        this.cardRepo.deleteCard(Long.parseLong(id));
        return ResponseEntity.noContent().build();
    }
}

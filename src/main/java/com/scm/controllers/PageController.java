package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String home(Model model) {
        System.out.println("Home page handler");
        // sending data to view
        model.addAttribute("name", "UEM Jaipur");
        model.addAttribute("youtubeChannel", "Sunny Prakash");
        model.addAttribute("githubRepo", "https://github.com/sunnyprakashsp");
        return "home";
    }

    // about route

    @RequestMapping("/about")
    public String aboutPage(Model model) {
        model.addAttribute("isLogin", true);
        System.out.println("About page loading");
        return "about";
    }

    // services

    @RequestMapping("/services")
    public String servicesPage() {
        System.out.println("services page loading");
        return "services";
    }

    // contact page

    @GetMapping("/contact")
    public String contact() {
        return new String("contact");
    }

    // this is showing login page
    @GetMapping("/login")
    public String login() {
        return new String("login");
    }

    // registration page
    @GetMapping("/register")
    public String register(Model model) {

        UserForm userForm = new UserForm();
        // default data bhi daal sakte hai
        // userForm.setName("Durgesh");
        // userForm.setAbout("This is about : Write something about yourself");
        model.addAttribute("userForm", userForm);

        return "register";
    }


    // @GetMapping("/user/profile")
    // public String profile(@AuthenticationPrincipal OAuth2User principal, Model model) {
    // String email = principal.getAttribute("email");
    // User user = userService.getUserByEmail(email);  

    // if (user == null) {
    //     // Create new user from Google OAuth
    //     user = new User();
    //     user.setEmail(email);
    //     user.setName(principal.getAttribute("name")); 
    //     user.setPassword(passwordEncoder.encode("dummy")); // or skip encoding
    //     userService.saveUser(user);
    // }

    // model.addAttribute("loggedInUser", user); 
    // return "user/profile";
    // }
    
    @RequestMapping(value = "/do-register", method = RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult rBindingResult,
            HttpSession session) {
        System.out.println("Processing registration");
        // fetch form data
        // UserForm
        System.out.println(userForm);

        // validate form data
        if (rBindingResult.hasErrors()) {
            return "register";
        }

        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setEnabled(true);
        user.setProfilePic(
                "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.indiatoday.in%2Fmovies%2Fcelebrities%2Fstory%2Fsalman-khan-increased-security-death-threats-paparazzi-rethink-coverage-photography-approach-2619423-2024-10-18&psig=AOvVaw0tUFW1_VwLzMJIxviyF7yl&ust=1743855120879000&source=images&cd=vfe&opi=89978449&ved=0CBAQjRxqFwoTCNCNqNusvowDFQAAAAAdAAAAABAE");

        User savedUser = userService.saveUser(user);

        System.out.println("user saved :");

        Message message = Message.builder().content("Registration Successful").type(MessageType.green).build();

        session.setAttribute("message", message);

        // redirect to login page
        return "redirect:/login";
    }

}

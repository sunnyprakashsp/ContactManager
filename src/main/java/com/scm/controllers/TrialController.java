package com.scm.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TrialController {
    @GetMapping("/free-trial")
    public String freeTrial() {
        return "free-trial"; // Returns free-trial.html or free-trial.jsp from templates
    }
}

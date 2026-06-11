package com.project.centerpoint.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/warranty")
    public String warranty() {
        return "pages/warranty";
    }

    @GetMapping("/guide")
    public String guide() {
        return "pages/guide";
    }
}

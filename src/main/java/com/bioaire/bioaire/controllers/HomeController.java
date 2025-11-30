package com.bioaire.bioaire.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/blog")
    public String blog() {
        return "blog";
    }
    
    @GetMapping("/blog.html")
    public String blogAlias() {
        return "blog";
    }

    @GetMapping("/blog/ventiladores")
    public String blogVentiladores() {
        return "blog-ventiladores";
    }

    @GetMapping("/pq")
    public String pq() {
        return "pq";
    }
    
    @GetMapping("/pq.html")
    public String pqAlias() {
        return "pq";
    }
    
    @GetMapping("/404")
    public String notFound() {
        return "404";
    }
    
    @GetMapping("/404.html")
    public String notFoundAlias() {
        return "404";
    }
}

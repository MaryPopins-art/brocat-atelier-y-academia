package com.academia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index"; // Renderiza src/main/resources/templates/index.html
    }
    
    @GetMapping("/alumno/login")
    public String alumnoLoginForm(Model model, HttpSession session) {
        // Si ya está autenticado como alumno, redirigir al panel
        if (session.getAttribute("alumnoLoggedIn") != null) {
            return "redirect:/alumno/panel";
        }
        return "alumno-login";
    }
}
package com.academia.controller;

import com.academia.CitaPrevia;
import com.academia.repository.CitaPreviaRepository;
import com.academia.service.HorarioAtelierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cita-previa")
public class CitaPreviaController {
    
    @Autowired
    private CitaPreviaRepository citaPreviaRepository;
    
    @Autowired
    private HorarioAtelierService horarioAtelierService;

    @GetMapping("")
    public String solicitar(Model model) {
        model.addAttribute("titulo", "Solicitar Cita Previa - Atelier BROCAT");
        model.addAttribute("citaPrevia", new CitaPrevia());
        return "cita-previa";
    }

    @PostMapping("/solicitar")
    public String procesarSolicitud(@ModelAttribute CitaPrevia citaPrevia, 
                                   RedirectAttributes redirectAttributes) {
        try {
            // La cita se confirma automáticamente (estado ya es "CONFIRMADA" por defecto)
            CitaPrevia citaGuardada = citaPreviaRepository.save(citaPrevia);
            
            // Mensaje de confirmación
            redirectAttributes.addFlashAttribute("mensaje", "success");
            redirectAttributes.addFlashAttribute("citaConfirmada", citaGuardada);
            
            return "redirect:/cita-previa/confirmacion";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al procesar la solicitud. Inténtalo de nuevo.");
            return "redirect:/cita-previa";
        }
    }
    
    @GetMapping("/confirmacion")
    public String confirmacion(Model model) {
        model.addAttribute("titulo", "Cita Confirmada - Atelier BROCAT");
        return "cita-confirmada";
    }

    @GetMapping("/consultar")
    public String consultar(Model model) {
        model.addAttribute("titulo", "Consultar Mi Cita - Atelier BROCAT");
        return "consultar-cita";
    }
    
    @PostMapping("/buscar")
    public String buscarCita(@RequestParam String nombre, 
                            @RequestParam String telefono, 
                            Model model,
                            RedirectAttributes redirectAttributes) {
        try {
            List<CitaPrevia> citas = citaPreviaRepository.findByNombreContainingIgnoreCaseAndTelefono(nombre, telefono);
            
            if (citas.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "No se encontraron citas con esos datos.");
                return "redirect:/cita-previa/consultar";
            }
            
            model.addAttribute("titulo", "Mis Citas - Atelier BROCAT");
            model.addAttribute("citas", citas);
            model.addAttribute("nombreBuscado", nombre);
            
            return "mis-citas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al buscar las citas. Inténtalo de nuevo.");
            return "redirect:/cita-previa/consultar";
        }
    }
    
    // ========= ENDPOINTS REST PARA EL ATELIER (INDEPENDIENTES DE LA ACADEMIA) =========
    
    /**
     * Endpoint REST para obtener horas disponibles del Atelier por fecha
     */
    @GetMapping("/api/horas-disponibles-atelier")
    @ResponseBody
    public ResponseEntity<List<Map<String, String>>> obtenerHorasDisponiblesAtelier(@RequestParam String fecha) {
        try {
            LocalDate fechaSeleccionada = LocalDate.parse(fecha);
            
            if (!horarioAtelierService.esFechaDisponibleAtelier(fechaSeleccionada)) {
                return ResponseEntity.ok(List.of());
            }
            
            List<Map<String, String>> horasDisponibles = horarioAtelierService.obtenerHorasDisponiblesAtelier(fechaSeleccionada);
            return ResponseEntity.ok(horasDisponibles);
            
        } catch (Exception e) {
            return ResponseEntity.ok(List.of());
        }
    }
    
    /**
     * Endpoint REST para obtener información del día del Atelier
     */
    @GetMapping("/api/info-dia-atelier")
    @ResponseBody
    public ResponseEntity<Map<String, String>> obtenerInfoDiaAtelier(@RequestParam String fecha) {
        try {
            LocalDate fechaSeleccionada = LocalDate.parse(fecha);
            Map<String, String> infoDia = horarioAtelierService.obtenerInfoDiaAtelier(fechaSeleccionada);
            return ResponseEntity.ok(infoDia);
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("error", "Fecha inválida"));
        }
    }
    
    /**
     * Endpoint REST para obtener estadísticas del Atelier
     */
    @GetMapping("/api/estadisticas-atelier")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> obtenerEstadisticasAtelier(@RequestParam String fecha) {
        try {
            LocalDate fechaSeleccionada = LocalDate.parse(fecha);
            Map<String, Object> estadisticas = horarioAtelierService.obtenerEstadisticasAtelier(fechaSeleccionada);
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("error", "Error al obtener estadísticas"));
        }
    }
}
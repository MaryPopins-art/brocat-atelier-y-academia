package com.academia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.academia.repository.CitaPreviaRepository;
import com.academia.CitaPrevia;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HorarioAtelierService {
    
    @Autowired
    private CitaPreviaRepository citaPreviaRepository;
    
    // Horarios predefinidos del Atelier BROCAT (INDEPENDIENTES de la Academia)
    // HORARIOS REALES: 10:00-13:30 y 17:00-20:30
    private static final Map<DayOfWeek, List<String>> HORARIOS_ATELIER = Map.of(
        DayOfWeek.MONDAY, Arrays.asList("10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30"),
        DayOfWeek.TUESDAY, Arrays.asList("10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30"),
        DayOfWeek.WEDNESDAY, Arrays.asList("10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30"),
        DayOfWeek.THURSDAY, Arrays.asList("10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30"),
        DayOfWeek.FRIDAY, Arrays.asList("10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30"),
        DayOfWeek.SATURDAY, Arrays.asList("10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30"),
        DayOfWeek.SUNDAY, Arrays.asList() // Domingo cerrado en el Atelier
    );
    
    /**
     * Método principal para obtener horas disponibles en el Atelier (INDEPENDIENTE de la Academia)
     */
    public List<Map<String, String>> obtenerHorasDisponiblesAtelier(LocalDate fecha) {
        DayOfWeek diaSemana = fecha.getDayOfWeek();
        
        // Obtener horas del Atelier (NO de la Academia)
        List<String> horasDia = HORARIOS_ATELIER.getOrDefault(diaSemana, Arrays.asList());
        
        if (horasDia.isEmpty()) {
            return new ArrayList<>();
        }
        
        // Obtener horas ocupadas SOLO del Atelier (citas previas)
        List<CitaPrevia> citasDelDia = citaPreviaRepository.findByFechaPreferida(fecha);
        Set<String> horasOcupadasAtelier = citasDelDia.stream()
            .map(cita -> cita.getHoraPreferida())
            .filter(hora -> hora != null && !hora.isEmpty())
            .collect(Collectors.toSet());
        
        // Filtrar horas disponibles SOLO para el Atelier
        return horasDia.stream()
            .filter(hora -> !horasOcupadasAtelier.contains(hora))
            .map(hora -> {
                Map<String, String> horaMap = new HashMap<>();
                horaMap.put("value", hora);
                horaMap.put("text", hora);
                return horaMap;
            })
            .collect(Collectors.toList());
    }
    
    /**
     * Verificar si una fecha está disponible para citas en el Atelier
     */
    public boolean esFechaDisponibleAtelier(LocalDate fecha) {
        // No permitir fechas pasadas
        if (fecha.isBefore(LocalDate.now())) {
            return false;
        }
        
        DayOfWeek diaSemana = fecha.getDayOfWeek();
        
        // Verificar si el día tiene horarios disponibles en el Atelier
        List<String> horasDia = HORARIOS_ATELIER.getOrDefault(diaSemana, Arrays.asList());
        return !horasDia.isEmpty();
    }
    
    /**
     * Obtener información del día para el Atelier (INDEPENDIENTE de la Academia)
     */
    public Map<String, String> obtenerInfoDiaAtelier(LocalDate fecha) {
        Map<String, String> info = new HashMap<>();
        
        if (fecha.isBefore(LocalDate.now())) {
            info.put("estado", "pasada");
            info.put("mensaje", "Fecha no disponible");
            return info;
        }
        
        DayOfWeek diaSemana = fecha.getDayOfWeek();
        List<String> horasDia = HORARIOS_ATELIER.getOrDefault(diaSemana, Arrays.asList());
        
        if (horasDia.isEmpty()) {
            info.put("estado", "cerrado");
            info.put("mensaje", "Domingo cerrado - Atelier");
        } else {
            List<Map<String, String>> horasDisponibles = obtenerHorasDisponiblesAtelier(fecha);
            if (horasDisponibles.isEmpty()) {
                info.put("estado", "completo");
                info.put("mensaje", "Día completo en el Atelier - No hay horas disponibles");
            } else {
                info.put("estado", "disponible");
                info.put("mensaje", horasDisponibles.size() + " horas disponibles en el Atelier");
            }
        }
        
        return info;
    }
    
    /**
     * Obtener todas las horas del Atelier para un día específico (sin filtrar ocupadas)
     */
    public List<String> obtenerTodasLasHorasAtelier(DayOfWeek diaSemana) {
        return HORARIOS_ATELIER.getOrDefault(diaSemana, Arrays.asList());
    }
    
    /**
     * Verificar si una hora específica está disponible en el Atelier para una fecha
     */
    public boolean esHoraDisponibleAtelier(LocalDate fecha, String hora) {
        List<Map<String, String>> horasDisponibles = obtenerHorasDisponiblesAtelier(fecha);
        return horasDisponibles.stream()
            .anyMatch(h -> hora.equals(h.get("value")));
    }
    
    /**
     * Contar horas ocupadas en el Atelier para una fecha específica
     */
    public long contarHorasOcupadasAtelier(LocalDate fecha) {
        return citaPreviaRepository.findByFechaPreferida(fecha).stream()
            .map(cita -> cita.getHoraPreferida())
            .filter(hora -> hora != null && !hora.isEmpty())
            .count();
    }
    
    /**
     * Obtener citas del Atelier para una fecha específica
     */
    public List<CitaPrevia> obtenerCitasAtelier(LocalDate fecha) {
        return citaPreviaRepository.findByFechaPreferida(fecha);
    }
    
    /**
     * Verificar disponibilidad total del Atelier para una fecha
     */
    public Map<String, Object> obtenerEstadisticasAtelier(LocalDate fecha) {
        Map<String, Object> estadisticas = new HashMap<>();
        
        DayOfWeek diaSemana = fecha.getDayOfWeek();
        List<String> horasTotales = HORARIOS_ATELIER.getOrDefault(diaSemana, Arrays.asList());
        List<Map<String, String>> horasDisponibles = obtenerHorasDisponiblesAtelier(fecha);
        
        estadisticas.put("horasTotales", horasTotales.size());
        estadisticas.put("horasDisponibles", horasDisponibles.size());
        estadisticas.put("horasOcupadas", horasTotales.size() - horasDisponibles.size());
        estadisticas.put("porcentajeOcupacion", 
            horasTotales.size() > 0 ? 
                ((horasTotales.size() - horasDisponibles.size()) * 100.0 / horasTotales.size()) : 0.0);
        
        return estadisticas;
    }
}
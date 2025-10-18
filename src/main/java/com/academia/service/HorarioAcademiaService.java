package com.academia.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HorarioAcademiaService {
    
    // Horarios predefinidos de la Academia BROCAT para recuperaciones
    private static final Map<DayOfWeek, List<String>> HORARIOS_ACADEMIA = Map.of(
        DayOfWeek.MONDAY, Arrays.asList("10:00-12:00", "16:00-18:00", "18:00-20:00"),
        DayOfWeek.TUESDAY, Arrays.asList("10:00-12:00", "16:00-18:00", "18:00-20:00"),
        DayOfWeek.WEDNESDAY, Arrays.asList("10:00-12:00", "16:00-18:00", "18:00-20:00"),
        DayOfWeek.THURSDAY, Arrays.asList("10:00-12:00", "16:00-18:00", "18:00-20:00"),
        DayOfWeek.FRIDAY, Arrays.asList("10:00-12:00", "16:00-18:00", "18:00-20:00"),
        DayOfWeek.SATURDAY, Arrays.asList("09:00-11:00", "11:00-13:00"),
        DayOfWeek.SUNDAY, Arrays.asList() // Domingo cerrado
    );
    
    /**
     * Obtener horarios disponibles para recuperación de clases (simulado)
     */
    public List<Map<String, String>> obtenerPlazasDisponiblesParaRecuperacion(String curso) {
        // Simulación de plazas disponibles para la Academia
        List<Map<String, String>> plazas = new ArrayList<>();
        
        LocalDate hoy = LocalDate.now();
        for (int i = 0; i < 30; i++) {
            LocalDate fecha = hoy.plusDays(i);
            DayOfWeek dia = fecha.getDayOfWeek();
            
            List<String> horariosDia = HORARIOS_ACADEMIA.getOrDefault(dia, Arrays.asList());
            for (String horario : horariosDia) {
                Map<String, String> plaza = new HashMap<>();
                plaza.put("curso", curso);
                plaza.put("fecha", fecha.toString());
                plaza.put("horario", horario);
                plaza.put("estado", "Disponible");
                plazas.add(plaza);
            }
        }
        
        return plazas;
    }
    
    /**
     * Obtener plazas disponibles para recuperación con límite de fecha
     */
    public List<Map<String, String>> obtenerPlazasDisponiblesParaRecuperacion(String curso, LocalDate fechaLimite) {
        return obtenerPlazasDisponiblesParaRecuperacion(curso).stream()
            .filter(plaza -> {
                LocalDate fechaPlaza = LocalDate.parse(plaza.get("fecha"));
                return fechaPlaza.isBefore(fechaLimite) || fechaPlaza.isEqual(fechaLimite);
            })
            .collect(Collectors.toList());
    }
    
    /**
     * Verificar si una fecha está disponible para recuperaciones en la Academia
     */
    public boolean esFechaDisponibleParaAcademia(LocalDate fecha) {
        // No permitir fechas pasadas
        if (fecha.isBefore(LocalDate.now())) {
            return false;
        }
        
        DayOfWeek diaSemana = fecha.getDayOfWeek();
        
        // Verificar si el día tiene horarios disponibles en la academia
        List<String> horasDia = HORARIOS_ACADEMIA.getOrDefault(diaSemana, Arrays.asList());
        return !horasDia.isEmpty();
    }
    
    /**
     * Obtener horarios disponibles para la Academia (diferentes del Atelier)
     */
    public List<Map<String, String>> obtenerHorariosAcademia(LocalDate fecha) {
        DayOfWeek diaSemana = fecha.getDayOfWeek();
        
        // Obtener horarios de la academia
        List<String> horariosDia = HORARIOS_ACADEMIA.getOrDefault(diaSemana, Arrays.asList());
        
        if (horariosDia.isEmpty()) {
            return new ArrayList<>();
        }
        
        return horariosDia.stream()
            .map(horario -> {
                Map<String, String> horarioMap = new HashMap<>();
                horarioMap.put("value", horario);
                horarioMap.put("text", horario);
                return horarioMap;
            })
            .collect(Collectors.toList());
    }
    
    /**
     * Obtener información del día para la Academia
     */
    public Map<String, String> obtenerInfoDiaAcademia(LocalDate fecha) {
        Map<String, String> info = new HashMap<>();
        
        if (fecha.isBefore(LocalDate.now())) {
            info.put("estado", "pasada");
            info.put("mensaje", "Fecha no disponible");
            return info;
        }
        
        DayOfWeek diaSemana = fecha.getDayOfWeek();
        List<String> horariosDia = HORARIOS_ACADEMIA.getOrDefault(diaSemana, Arrays.asList());
        
        if (horariosDia.isEmpty()) {
            info.put("estado", "cerrado");
            info.put("mensaje", "Domingo cerrado - Academia");
        } else {
            info.put("estado", "disponible");
            info.put("mensaje", horariosDia.size() + " horarios disponibles para recuperaciones");
        }
        
        return info;
    }
    
    /**
     * Obtener plazas filtradas por curso y estado (simulado)
     */
    public List<Map<String, String>> obtenerPlazasPorCursoYEstado(String curso, String estado) {
        return obtenerPlazasDisponiblesParaRecuperacion(curso).stream()
            .filter(plaza -> estado.equals(plaza.get("estado")))
            .collect(Collectors.toList());
    }
    
    /**
     * Contar plazas disponibles para un curso específico
     */
    public long contarPlazasDisponibles(String curso) {
        return obtenerPlazasDisponiblesParaRecuperacion(curso).size();
    }
    
    /**
     * Verificar disponibilidad de plaza específica (simulado)
     */
    public boolean esPlazaDisponible(Long plazaId) {
        // Simulación: todas las plazas están disponibles por defecto
        return true;
    }
    
    /**
     * Obtener horarios de la academia por día de la semana
     */
    public List<String> obtenerHorariosPorDia(DayOfWeek diaSemana) {
        return HORARIOS_ACADEMIA.getOrDefault(diaSemana, Arrays.asList());
    }
}
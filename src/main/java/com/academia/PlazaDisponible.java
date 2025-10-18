package com.academia;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PlazaDisponible {
    private String id;
    private String curso;
    private String alumnoAusente;  // Quien no asistió y liberó la plaza
    private LocalDate fecha;
    private String horario;
    private boolean ocupada;       // Si ya fue tomada por otro alumno
    private String alumnoRecuperacion; // Quien tomó la plaza para recuperar
    
    // Constructor
    public PlazaDisponible() {}
    
    public PlazaDisponible(String curso, String alumnoAusente, LocalDate fecha, String horario) {
        this.id = generarId();
        this.curso = curso;
        this.alumnoAusente = alumnoAusente;
        this.fecha = fecha;
        this.horario = horario;
        this.ocupada = false;
        this.alumnoRecuperacion = null;
    }
    
    // Método para generar ID único
    private String generarId() {
        return "PLZ-" + System.currentTimeMillis();
    }
    
    // Método para marcar la plaza como ocupada
    public void ocuparPlaza(String alumnoRecuperacion) {
        this.ocupada = true;
        this.alumnoRecuperacion = alumnoRecuperacion;
    }
    
    // Método para liberar la plaza
    public void liberarPlaza() {
        this.ocupada = false;
        this.alumnoRecuperacion = null;
    }
    
    // Método para verificar si está disponible
    public boolean estaDisponible() {
        return !ocupada;
    }
    
    // Método para formato de fecha
    public String getFechaFormateada() {
        return fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
    
    // Método para descripción completa
    public String getDescripcion() {
        String estado = ocupada ? " (Ocupada por " + alumnoRecuperacion + ")" : " (Disponible)";
        return curso + " - " + getFechaFormateada() + " " + horario + estado;
    }
    
    // Getters y Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getCurso() {
        return curso;
    }
    
    public void setCurso(String curso) {
        this.curso = curso;
    }
    
    public String getAlumnoAusente() {
        return alumnoAusente;
    }
    
    public void setAlumnoAusente(String alumnoAusente) {
        this.alumnoAusente = alumnoAusente;
    }
    
    public LocalDate getFecha() {
        return fecha;
    }
    
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    
    // Método alias para compatibilidad con el servicio
    public LocalDate getFechaClase() {
        return fecha;
    }
    
    // Método para obtener estado como string para compatibilidad con el servicio
    public String getEstado() {
        return ocupada ? "Ocupada" : "Disponible";
    }
    
    public String getHorario() {
        return horario;
    }
    
    public void setHorario(String horario) {
        this.horario = horario;
    }
    
    public boolean isOcupada() {
        return ocupada;
    }
    
    public void setOcupada(boolean ocupada) {
        this.ocupada = ocupada;
    }
    
    public String getAlumnoRecuperacion() {
        return alumnoRecuperacion;
    }
    
    public void setAlumnoRecuperacion(String alumnoRecuperacion) {
        this.alumnoRecuperacion = alumnoRecuperacion;
    }
}
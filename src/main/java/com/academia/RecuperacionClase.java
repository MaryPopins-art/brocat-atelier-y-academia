package com.academia;

import java.time.LocalDate;
import java.time.LocalTime;

public class RecuperacionClase {
    public Long id;
    public Long alumnoId;
    public String nombreAlumno;
    public String diaSemana;
    public LocalDate fechaSolicitud;
    public LocalDate fechaRecuperacion; // Fecha propuesta para la recuperación
    public LocalTime horaRecuperacion;  // Hora propuesta para la recuperación
    public String materia;
    public String motivoFalta;
    public String estado; // PENDIENTE, APROBADA, RECHAZADA
    public String observaciones;
    public String profesor;

    public RecuperacionClase() {}

    public RecuperacionClase(Long id, Long alumnoId, String nombreAlumno, String diaSemana, 
                            LocalDate fechaSolicitud, String materia, String motivoFalta, String profesor) {
        this.id = id;
        this.alumnoId = alumnoId;
        this.nombreAlumno = nombreAlumno;
        this.diaSemana = diaSemana;
        this.fechaSolicitud = fechaSolicitud;
        this.materia = materia;
        this.motivoFalta = motivoFalta;
        this.estado = "PENDIENTE";
        this.profesor = profesor;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAlumnoId() {
        return alumnoId;
    }

    public void setAlumnoId(Long alumnoId) {
        this.alumnoId = alumnoId;
    }

    public String getNombreAlumno() {
        return nombreAlumno;
    }

    public void setNombreAlumno(String nombreAlumno) {
        this.nombreAlumno = nombreAlumno;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public LocalDate getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDate fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public LocalDate getFechaRecuperacion() {
        return fechaRecuperacion;
    }

    public void setFechaRecuperacion(LocalDate fechaRecuperacion) {
        this.fechaRecuperacion = fechaRecuperacion;
    }

    public LocalTime getHoraRecuperacion() {
        return horaRecuperacion;
    }

    public void setHoraRecuperacion(LocalTime horaRecuperacion) {
        this.horaRecuperacion = horaRecuperacion;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getMotivoFalta() {
        return motivoFalta;
    }

    public void setMotivoFalta(String motivoFalta) {
        this.motivoFalta = motivoFalta;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    // Método para obtener el estado con formato visual
    public String getEstadoFormateado() {
        switch (estado) {
            case "PENDIENTE":
                return "⏳ Pendiente";
            case "APROBADA":
                return "✅ Aprobada";
            case "RECHAZADA":
                return "❌ Rechazada";
            default:
                return estado;
        }
    }

    // Método para obtener la clase CSS según el estado
    public String getEstadoCssClass() {
        switch (estado) {
            case "PENDIENTE":
                return "text-warning";
            case "APROBADA":
                return "text-success";
            case "RECHAZADA":
                return "text-danger";
            default:
                return "text-muted";
        }
    }
}
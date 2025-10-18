package com.academia;

import java.time.LocalDate;

public class Ausencia {
    public Long id;
    public Long alumnoId;
    public String diaSemana;
    public LocalDate fecha;
    public String motivo;
    public boolean notificado;

    public Ausencia() {}

    public Ausencia(Long id, Long alumnoId, String diaSemana, LocalDate fecha, String motivo) {
        this.id = id;
        this.alumnoId = alumnoId;
        this.diaSemana = diaSemana;
        this.fecha = fecha;
        this.motivo = motivo;
        this.notificado = false;
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

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public boolean isNotificado() {
        return notificado;
    }

    public void setNotificado(boolean notificado) {
        this.notificado = notificado;
    }
}
package com.academia;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ClasePerdida {
    private Long id;
    private Long alumnoId;
    private String diaSemana;
    private LocalDate fechaAusencia;
    private String materia;
    private String horario;
    private String motivo;
    private LocalDate fechaLimiteRecuperacion; // 30 días desde la ausencia
    private boolean recuperada;
    private LocalDate fechaRecuperacion;
    private String detallesRecuperacion;

    public ClasePerdida() {}

    public ClasePerdida(Long id, Long alumnoId, String diaSemana, LocalDate fechaAusencia, 
                       String materia, String horario, String motivo) {
        this.id = id;
        this.alumnoId = alumnoId;
        this.diaSemana = diaSemana;
        this.fechaAusencia = fechaAusencia;
        this.materia = materia;
        this.horario = horario;
        this.motivo = motivo;
        this.fechaLimiteRecuperacion = fechaAusencia.plusDays(30);
        this.recuperada = false;
    }

    // Método para verificar si aún se puede recuperar esta clase
    public boolean puedeRecuperarse() {
        return !recuperada && LocalDate.now().isBefore(fechaLimiteRecuperacion.plusDays(1));
    }

    // Método para marcar como recuperada
    public void marcarComoRecuperada(LocalDate fechaRecuperacion, String detalles) {
        this.recuperada = true;
        this.fechaRecuperacion = fechaRecuperacion;
        this.detallesRecuperacion = detalles;
    }

    // Método para obtener días restantes para recuperar
    public long getDiasRestantesParaRecuperar() {
        LocalDate hoy = LocalDate.now();
        if (hoy.isAfter(fechaLimiteRecuperacion)) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(hoy, fechaLimiteRecuperacion);
    }

    // Método para formatear la fecha de ausencia
    public String getFechaAusenciaFormateada() {
        return fechaAusencia.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    // Método para formatear la fecha límite
    public String getFechaLimiteFormateada() {
        return fechaLimiteRecuperacion.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAlumnoId() { return alumnoId; }
    public void setAlumnoId(Long alumnoId) { this.alumnoId = alumnoId; }

    public String getDiaSemana() { return diaSemana; }
    public void setDiaSemana(String diaSemana) { this.diaSemana = diaSemana; }

    public LocalDate getFechaAusencia() { return fechaAusencia; }
    public void setFechaAusencia(LocalDate fechaAusencia) { this.fechaAusencia = fechaAusencia; }

    public String getMateria() { return materia; }
    public void setMateria(String materia) { this.materia = materia; }

    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public LocalDate getFechaLimiteRecuperacion() { return fechaLimiteRecuperacion; }
    public void setFechaLimiteRecuperacion(LocalDate fechaLimiteRecuperacion) { 
        this.fechaLimiteRecuperacion = fechaLimiteRecuperacion; 
    }

    public boolean isRecuperada() { return recuperada; }
    public void setRecuperada(boolean recuperada) { this.recuperada = recuperada; }

    public LocalDate getFechaRecuperacion() { return fechaRecuperacion; }
    public void setFechaRecuperacion(LocalDate fechaRecuperacion) { 
        this.fechaRecuperacion = fechaRecuperacion; 
    }

    public String getDetallesRecuperacion() { return detallesRecuperacion; }
    public void setDetallesRecuperacion(String detallesRecuperacion) { 
        this.detallesRecuperacion = detallesRecuperacion; 
    }
}
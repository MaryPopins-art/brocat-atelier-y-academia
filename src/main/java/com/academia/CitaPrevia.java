package com.academia;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "citas_previas")
public class CitaPrevia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private String telefono;
    
    private String email;
    
    @Column(nullable = false)
    private String tipoServicio;
    
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaPreferida;
    
    private String horaPreferida;
    
    @Column(length = 1000, nullable = false)
    private String descripcion;
    
    @Column(length = 1000)
    private String observaciones;
    
    @Column(nullable = false)
    private LocalDateTime fechaSolicitud;
    
    @Column(nullable = false)
    private String estado; // CONFIRMADA, PENDIENTE, CANCELADA
    
    public CitaPrevia() {
        this.fechaSolicitud = LocalDateTime.now();
        this.estado = "CONFIRMADA"; // Se confirma automáticamente
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTipoServicio() {
        return tipoServicio;
    }
    
    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }
    
    public LocalDate getFechaPreferida() {
        return fechaPreferida;
    }
    
    public void setFechaPreferida(LocalDate fechaPreferida) {
        this.fechaPreferida = fechaPreferida;
    }
    
    public String getHoraPreferida() {
        return horaPreferida;
    }
    
    public void setHoraPreferida(String horaPreferida) {
        this.horaPreferida = horaPreferida;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public LocalDateTime getFechaSolicitud() {
        return fechaSolicitud;
    }
    
    public void setFechaSolicitud(LocalDateTime fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    // Métodos de utilidad
    public String getFechaPreferidaFormateada() {
        return fechaPreferida != null ? fechaPreferida.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
    }
    
    public String getFechaSolicitudFormateada() {
        return fechaSolicitud != null ? fechaSolicitud.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "";
    }
    
    public String getTipoServicioFormateado() {
        switch (tipoServicio) {
            case "arreglos": return "Arreglos y Ajustes";
            case "costura-personalizada": return "Costura Personalizada";
            case "proyecto-especial": return "Proyecto Especial";
            case "consultoria": return "Consultoría de Diseño";
            case "otro": return "Otro";
            default: return tipoServicio;
        }
    }
    
    public String getHoraPreferidaFormateada() {
        if (horaPreferida == null || horaPreferida.isEmpty()) {
            return "Sin preferencia";
        }
        switch (horaPreferida) {
            case "mañana": return "Mañana (9:00 - 13:00)";
            case "tarde": return "Tarde (15:00 - 19:00)";
            default: return horaPreferida;
        }
    }
}
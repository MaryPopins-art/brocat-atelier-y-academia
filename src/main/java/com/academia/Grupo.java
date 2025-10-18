package com.academia;

import java.util.List;
import java.util.ArrayList;

public class Grupo {
    public Long id;
    public String nombre;
    public String profesor;
    public int maxParticipantes;
    public List<Long> alumnoIds;

    public Grupo() {
        this.maxParticipantes = 10; // Límite por defecto
        this.alumnoIds = new ArrayList<>();
    }

    public Grupo(Long id, String nombre, String profesor) {
        this.id = id;
        this.nombre = nombre;
        this.profesor = profesor;
        this.maxParticipantes = 10; // Límite máximo 10 personas
        this.alumnoIds = new ArrayList<>();
    }

    // Métodos de utilidad
    public int getParticipantesActuales() {
        return alumnoIds.size();
    }

    public boolean estaLleno() {
        return getParticipantesActuales() >= maxParticipantes;
    }

    public boolean puedeAgregarAlumno() {
        return !estaLleno();
    }

    public boolean agregarAlumno(Long alumnoId) {
        if (puedeAgregarAlumno() && !alumnoIds.contains(alumnoId)) {
            alumnoIds.add(alumnoId);
            return true;
        }
        return false;
    }

    public boolean eliminarAlumno(Long alumnoId) {
        return alumnoIds.remove(alumnoId);
    }

    public String getInfoCapacidad() {
        return getParticipantesActuales() + "/" + maxParticipantes;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getProfesor() { return profesor; }
    public void setProfesor(String profesor) { this.profesor = profesor; }

    public int getMaxParticipantes() { return maxParticipantes; }
    public void setMaxParticipantes(int maxParticipantes) { this.maxParticipantes = maxParticipantes; }

    public List<Long> getAlumnoIds() { return alumnoIds; }
    public void setAlumnoIds(List<Long> alumnoIds) { this.alumnoIds = alumnoIds; }
}
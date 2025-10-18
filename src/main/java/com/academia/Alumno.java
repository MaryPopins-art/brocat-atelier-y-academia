package com.academia;

public class Alumno {
    public Long id;
    public String nombre;
    public String apellidos;
    public String telefono;
    public String dni;
    public String curso;
    public String profesor;
    public int clasesNoAsistidas;

    public Alumno() {
        this.clasesNoAsistidas = 0;
    }

    public Alumno(Long id, String nombre, String apellidos, String telefono, String dni, String curso, String profesor) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.dni = dni;
        this.curso = curso;
        this.profesor = profesor;
        this.clasesNoAsistidas = 0;
    }

    // Método para incrementar el contador de ausencias
    public void incrementarAusencias() {
        this.clasesNoAsistidas++;
    }

    // Getter para el contador de ausencias
    public int getClasesNoAsistidas() {
        return clasesNoAsistidas;
    }

    // Setter para el contador de ausencias
    public void setClasesNoAsistidas(int clasesNoAsistidas) {
        this.clasesNoAsistidas = clasesNoAsistidas;
    }
}
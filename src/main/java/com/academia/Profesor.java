package com.academia;

public class Profesor {
    public Long id;
    public String nombre;
    public String apellidos;
    public String email;
    public String telefono;
    public String cursos;

    public Profesor() {}

    public Profesor(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = "";
        this.email = "";
        this.telefono = "";
        this.cursos = "";
    }

    public Profesor(Long id, String nombre, String cursos) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = "";
        this.email = "";
        this.telefono = "";
        this.cursos = cursos;
    }

    // Getters y Setters para Spring Data Binding
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

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCursos() {
        return cursos;
    }

    public void setCursos(String cursos) {
        this.cursos = cursos;
    }
}


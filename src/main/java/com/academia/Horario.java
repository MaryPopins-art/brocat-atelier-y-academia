package com.academia;

public class Horario {
    public Long id;
    public String grupo;
    public String diaSemana;
    public String horaInicio;
    public String horaFin;
    public String materia;
    public String aula;
    public String profesor;

    public Horario() {}

    public Horario(Long id, String grupo, String diaSemana, String horaInicio, 
                   String horaFin, String materia, String aula, String profesor) {
        this.id = id;
        this.grupo = grupo;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.materia = materia;
        this.aula = aula;
        this.profesor = profesor;
    }

    // Método auxiliar para obtener el horario formateado
    public String getHorarioFormateado() {
        return horaInicio + " - " + horaFin;
    }

    // Método para obtener el número del día de la semana (para ordenar)
    public int getDiaNumero() {
        switch (diaSemana.toLowerCase()) {
            case "lunes": return 1;
            case "martes": return 2;
            case "miércoles": case "miercoles": return 3;
            case "jueves": return 4;
            case "viernes": return 5;
            case "sábado": case "sabado": return 6;
            case "domingo": return 7;
            default: return 0;
        }
    }
}
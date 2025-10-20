package com.academia.repository;

import com.academia.CitaPrevia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface CitaPreviaRepository extends JpaRepository<CitaPrevia, Long> {
    
    // Buscar citas por nombre y teléfono para consulta
    List<CitaPrevia> findByNombreContainingIgnoreCaseAndTelefono(String nombre, String telefono);
    
    // Buscar citas por teléfono
    List<CitaPrevia> findByTelefono(String telefono);
    
    // Buscar citas por estado
    List<CitaPrevia> findByEstado(String estado);
    
    // Buscar todas las citas ordenadas por fecha de solicitud descendente
    List<CitaPrevia> findAllByOrderByFechaSolicitudDesc();
    
    // Buscar citas por fecha preferida (para verificar disponibilidad de horas)
    List<CitaPrevia> findByFechaPreferida(LocalDate fechaPreferida);
    
    // Buscar citas en un rango de fechas (para el calendario)
    List<CitaPrevia> findByFechaPreferidaBetween(LocalDate fechaInicio, LocalDate fechaFin);
}
package com.academia.controller;

import com.academia.Alumno;
import com.academia.Horario;
import com.academia.Ausencia;
import com.academia.Grupo;
import com.academia.RecuperacionClase;
import com.academia.Curso;
import com.academia.PlazaDisponible;
import com.academia.ClasePerdida;
import com.academia.service.HorarioAcademiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.ResponseEntity;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/alumno")
public class AlumnoController {
    
    @Autowired
    private HorarioAcademiaService horarioAcademiaService;
    
    // Lista simulada de alumnos (compartida con AdminController para simplicidad)
    // En un proyecto real, esto vendría de una base de datos
    private static List<Alumno> alumnos = new ArrayList<>();
    private static List<Horario> horarios = new ArrayList<>();
    private static List<Ausencia> ausencias = new ArrayList<>();
    private static List<Grupo> grupos = new ArrayList<>();
    private static List<RecuperacionClase> recuperaciones = new ArrayList<>();
    private static List<PlazaDisponible> plazasDisponibles = new ArrayList<>();
    private static List<ClasePerdida> clasesPerdidas = new ArrayList<>();
    
    static {
        // Inicializar grupos con límite máximo de 10 personas
        Grupo grupo1 = new Grupo(1L, "Grupo 1", "María García");
        Grupo grupo2 = new Grupo(2L, "Grupo 2", "Carlos Ruiz");
        grupos.add(grupo1);
        grupos.add(grupo2);
        
        // Inicializar con algunos alumnos de ejemplo
        Alumno juan = new Alumno(1L, "Juan", "Pérez", "123456789", "12345678A", "Costura Básica", "María García López");
        juan.setClasesNoAsistidas(2); // Juan tiene 2 ausencias
        alumnos.add(juan);
        
        Alumno ana = new Alumno(2L, "Ana", "López", "987654321", "87654321B", "Patchwork Avanzado", "Carlos Ruiz Martín");
        ana.setClasesNoAsistidas(0); // Ana no tiene ausencias
        alumnos.add(ana);
        
        Alumno maria = new Alumno(3L, "María", "González", "555666777", "11223344C", "Bordado Creativo", "María García López");
        maria.setClasesNoAsistidas(1); // María tiene 1 ausencia
        alumnos.add(maria);
        
        Alumno carlos = new Alumno(4L, "Carlos", "Martín", "888999000", "55667788D", "Patchwork Avanzado", "Carlos Ruiz Martín");
        carlos.setClasesNoAsistidas(3); // Carlos tiene 3 ausencias
        alumnos.add(carlos);
        
        // Registrar alumnos en los grupos (mapeo interno por curso)
        grupo1.agregarAlumno(1L); // Juan - Costura Básica
        grupo1.agregarAlumno(3L); // María - Bordado Creativo (mismo profesor)
        grupo2.agregarAlumno(2L); // Ana - Patchwork Avanzado
        grupo2.agregarAlumno(4L); // Carlos - Patchwork Avanzado
        
        // Inicializar horarios por curso específico
        // Horarios para "Costura Básica" (Juan)
        horarios.add(new Horario(1L, "Costura Básica", "Lunes", "10:00", "12:00", "Costura Básica", "Aula 1", "María García"));
        horarios.add(new Horario(2L, "Costura Básica", "Miércoles", "10:00", "12:00", "Costura Básica", "Aula 1", "María García"));
        
        // Horarios para "Patchwork Avanzado" (Ana y Carlos)
        horarios.add(new Horario(3L, "Patchwork Avanzado", "Martes", "17:00", "19:00", "Patchwork Avanzado", "Aula 3", "Carlos Ruiz"));
        horarios.add(new Horario(4L, "Patchwork Avanzado", "Jueves", "17:00", "19:00", "Patchwork Avanzado", "Aula 3", "Carlos Ruiz"));
        
        // Horarios para "Bordado Creativo" (María)
        horarios.add(new Horario(5L, "Bordado Creativo", "Viernes", "16:00", "18:00", "Bordado Creativo", "Aula 2", "María García"));
        horarios.add(new Horario(6L, "Bordado Creativo", "Sábado", "09:00", "11:00", "Bordado Creativo", "Aula 4", "Carlos Ruiz"));
        
    }
    
    // Constructor para inicializar datos
    public AlumnoController() {
        // Crear algunas ausencias de ejemplo que generarán clases perdidas automáticamente
        inicializarAusenciasDeEjemplo();
        System.out.println("🎓 AlumnoController inicializado. Sistema de clases perdidas automático activado.");
    }
    
    private void inicializarAusenciasDeEjemplo() {
        System.out.println("=== CREANDO AUSENCIAS DE EJEMPLO ===");
        
        // Juan (ID 1) - Ausencias en días de su curso "Telar Básico"
        // Juan tiene clases los lunes y miércoles
        crearAusenciaYClasePerdida(1L, "Juan Pérez", "Lunes", LocalDate.now().minusDays(5), "Cita médica");
        crearAusenciaYClasePerdida(1L, "Juan Pérez", "Miércoles", LocalDate.now().minusDays(15), "Asunto familiar");
        crearAusenciaYClasePerdida(1L, "Juan Pérez", "Lunes", LocalDate.now().minusDays(28), "Viaje de trabajo");
        
        // María (ID 3) - Ausencias en días de su curso "Bordado Creativo"  
        // María tiene clases los viernes y sábados
        crearAusenciaYClasePerdida(3L, "María González", "Viernes", LocalDate.now().minusDays(8), "Emergencia familiar");
        crearAusenciaYClasePerdida(3L, "María González", "Sábado", LocalDate.now().minusDays(20), "Compromiso social");
        
        System.out.println("Total ausencias creadas: " + ausencias.size());
        System.out.println("Total clases perdidas generadas: " + clasesPerdidas.size());
        System.out.println("📄 AlumnoController inicializado. Sistema de clases perdidas automático activado.");
        
        // Mostrar ejemplos de cálculo de días naturales
        mostrarEjemplosCalculoDias();
    }
    
    private void crearAusenciaYClasePerdida(Long alumnoId, String nombreAlumno, String diaSemana, LocalDate fechaAusencia, String motivo) {
        // Crear la ausencia
        Long ausenciaId = (long) (ausencias.size() + 1);
        Ausencia ausencia = new Ausencia(ausenciaId, alumnoId, diaSemana, fechaAusencia, motivo);
        ausencias.add(ausencia);
        
        // Buscar el alumno para crear la clase perdida
        Alumno alumno = alumnos.stream()
            .filter(a -> a.id.equals(alumnoId))
            .findFirst()
            .orElse(null);
            
        if (alumno != null) {
            // Crear la clase perdida automáticamente usando el método existente
            crearClasePerdidaParaRecuperacion(alumno, diaSemana, fechaAusencia, motivo);
            
            // Crear plaza disponible por la ausencia
            crearPlazaDisponiblePorAusencia(alumno, diaSemana, fechaAusencia);
            
            System.out.println("✅ Ausencia + Clase perdida creada para " + nombreAlumno + ": " + diaSemana + " " + fechaAusencia);
        }
    }
    
    // Procesar login de alumno
    @PostMapping("/login")
    public String processLogin(@RequestParam String nombre, 
                              @RequestParam String dni, 
                              HttpSession session, 
                              Model model) {
        
        System.out.println("=== LOGIN ATTEMPT ===");
        System.out.println("Nombre recibido: '" + nombre + "'");
        System.out.println("DNI recibido: '" + dni + "'");
        System.out.println("Total alumnos en sistema: " + alumnos.size());
        
        // Mostrar todos los alumnos disponibles
        System.out.println("Alumnos disponibles:");
        for (Alumno a : alumnos) {
            System.out.println("  - Nombre: '" + a.nombre + "', DNI: '" + a.dni + "'");
        }
        
        // Buscar alumno por nombre y DNI
        Alumno alumnoEncontrado = alumnos.stream()
            .filter(alumno -> alumno.nombre.equalsIgnoreCase(nombre.trim()) && 
                             alumno.dni.equalsIgnoreCase(dni.trim()))
            .findFirst()
            .orElse(null);
        
        if (alumnoEncontrado != null) {
            // Login exitoso
            System.out.println("✅ LOGIN EXITOSO para: " + alumnoEncontrado.nombre);
            session.setAttribute("alumnoLoggedIn", true);
            session.setAttribute("alumnoActual", alumnoEncontrado);
            return "redirect:/alumno/panel";
        } else {
            // Login fallido
            System.out.println("❌ LOGIN FALLIDO - Alumno no encontrado");
            model.addAttribute("error", "Nombre o DNI incorrectos. Verifica tus datos.");
            return "alumno-login";
        }
    }
    
    // Endpoint de prueba para verificar alumnos
    @GetMapping("/test")
    public String testAlumnos(Model model) {
        model.addAttribute("alumnos", alumnos);
        return "test-alumnos";
    }

    // Panel del alumno
    @GetMapping("/panel")
    public String panel(HttpSession session, Model model) {
        // Verificar autenticación
        if (!isAuthenticated(session)) {
            return "redirect:/alumno/login";
        }
        
        Alumno alumnoActual = (Alumno) session.getAttribute("alumnoActual");
        System.out.println("✅ Panel - Alumno logueado: " + alumnoActual.nombre);
        System.out.println("📋 Panel - Curso del alumno: " + alumnoActual.curso);
        System.out.println("👨‍🏫 Panel - Profesor del alumno: " + alumnoActual.profesor);
        
        model.addAttribute("alumno", alumnoActual);
        
        // Buscar información del curso
        String cursoDelAlumno = alumnoActual.curso;
        if (cursoDelAlumno != null && !cursoDelAlumno.isEmpty()) {
            // Buscar el curso completo en la lista de cursos del AdminController
            List<Curso> cursosDisponibles = AdminController.getCursos();
            Curso curso = cursosDisponibles.stream()
                .filter(c -> c.nombre.equals(cursoDelAlumno))
                .findFirst()
                .orElse(null);
            
            if (curso != null) {
                model.addAttribute("curso", curso);
                System.out.println("📚 Panel - Información del curso encontrada: " + curso.nombre + " - " + curso.horario);
            } else {
                System.out.println("⚠️ Panel - No se encontró información del curso: " + cursoDelAlumno);
            }
        }
        
        // Agregar datos de plazas disponibles para el modal de recuperación con fechas específicas
        Map<String, List<String>> plazasPorDia = getPlazasPorDiaYHorarioConFechas();
        model.addAttribute("plazasPorDia", plazasPorDia);
        
        // Generar lista plana de todas las opciones con día y horario
        List<String> todasLasOpciones = generarOpcionesCompletasConDiaYHorario();
        model.addAttribute("opcionesCompletas", todasLasOpciones);
        
        // Agregar clases perdidas que puede recuperar el alumno (dentro de 30 días)
        System.out.println("🎯 Panel - Obteniendo clases perdidas para alumno: " + alumnoActual.nombre + " (ID: " + alumnoActual.id + ")");
        List<ClasePerdida> clasesPerdidásDelAlumno = getClasesPerdidas(alumnoActual.id);
        System.out.println("📋 Panel - Clases perdidas a mostrar: " + clasesPerdidásDelAlumno.size());
        model.addAttribute("clasesPerdidas", clasesPerdidásDelAlumno);
        
        return "alumno-panel";
    }
    
    // Mostrar horarios mensuales del grupo del alumno
    @GetMapping("/horarios")
    public String verHorarios(@RequestParam(value = "mes", required = false) Integer mes,
                              @RequestParam(value = "anio", required = false) Integer anio,
                              HttpSession session, Model model) {
        // Verificar autenticación
        if (!isAuthenticated(session)) {
            return "redirect:/alumno/login";
        }
        
        Alumno alumnoActual = (Alumno) session.getAttribute("alumnoActual");
        
        // Determinar mes y año actual si no se especifican
        LocalDate fechaActual = LocalDate.now();
        int mesActual = (mes != null) ? mes : fechaActual.getMonthValue();
        int anioActual = (anio != null) ? anio : fechaActual.getYear();
        
        LocalDate primerDiaMes = LocalDate.of(anioActual, mesActual, 1);
        LocalDate ultimoDiaMes = primerDiaMes.withDayOfMonth(primerDiaMes.lengthOfMonth());
        
        // Obtener horarios del curso del alumno
        List<Horario> horariosGrupo = horarios.stream()
            .filter(horario -> horario.grupo.equals(alumnoActual.curso))
            .sorted((h1, h2) -> Integer.compare(h1.getDiaNumero(), h2.getDiaNumero()))
            .collect(Collectors.toList());
        
        // Crear calendario mensual
        Map<LocalDate, List<Horario>> calendarioMensual = generarCalendarioMensual(primerDiaMes, ultimoDiaMes, horariosGrupo);
        
        // Obtener ausencias del alumno para este mes
        List<Ausencia> ausenciasAlumno = ausencias.stream()
            .filter(ausencia -> ausencia.alumnoId.equals(alumnoActual.id))
            .filter(ausencia -> {
                LocalDate fechaAusencia = ausencia.fecha;
                return !fechaAusencia.isBefore(primerDiaMes) && !fechaAusencia.isAfter(ultimoDiaMes);
            })
            .collect(Collectors.toList());
        
        // Crear mapa de ausencias por fecha
        Map<LocalDate, List<Ausencia>> ausenciasPorFecha = ausenciasAlumno.stream()
            .collect(Collectors.groupingBy(ausencia -> ausencia.fecha));
        
        model.addAttribute("alumno", alumnoActual);
        model.addAttribute("calendarioMensual", calendarioMensual);
        model.addAttribute("curso", alumnoActual.curso);
        model.addAttribute("ausenciasPorFecha", ausenciasPorFecha);
        model.addAttribute("mesActual", mesActual);
        model.addAttribute("anioActual", anioActual);
        model.addAttribute("nombreMes", primerDiaMes.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")));
        model.addAttribute("primerDiaMes", primerDiaMes);
        model.addAttribute("ultimoDiaMes", ultimoDiaMes);
        
        return "alumno-horarios";
    }
    
    // Método auxiliar para generar el calendario mensual
    private Map<LocalDate, List<Horario>> generarCalendarioMensual(LocalDate primerDia, LocalDate ultimoDia, List<Horario> horarios) {
        Map<LocalDate, List<Horario>> calendario = new LinkedHashMap<>();
        
        LocalDate fecha = primerDia;
        while (!fecha.isAfter(ultimoDia)) {
            String diaSemana = fecha.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
            
            // Buscar horarios para este día de la semana
            List<Horario> horariosDelDia = horarios.stream()
                .filter(horario -> horario.diaSemana.equalsIgnoreCase(diaSemana))
                .collect(Collectors.toList());
            
            calendario.put(fecha, horariosDelDia);
            fecha = fecha.plusDays(1);
        }
        
        return calendario;
    }
    
    // Marcar ausencia
    @PostMapping("/ausencia")
    public String marcarAusencia(@RequestParam String diaSemana,
                                @RequestParam String fecha,
                                @RequestParam(required = false) String motivo,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        // Verificar autenticación
        if (!isAuthenticated(session)) {
            return "redirect:/alumno/login";
        }
        
        Alumno alumnoActual = (Alumno) session.getAttribute("alumnoActual");
        
        System.out.println("=== MARCANDO AUSENCIA DESDE CALENDARIO ===");
        System.out.println("Alumno: " + alumnoActual.nombre + " " + alumnoActual.apellidos + " (ID: " + alumnoActual.id + ")");
        System.out.println("Día: " + diaSemana + ", Fecha: " + fecha + ", Motivo: " + motivo);
        System.out.println("Curso del alumno: " + alumnoActual.curso);
        
        try {
            LocalDate fechaAusencia = LocalDate.parse(fecha);
            
            // Verificar si ya existe una ausencia para este día
            boolean yaExiste = ausencias.stream()
                .anyMatch(ausencia -> ausencia.alumnoId.equals(alumnoActual.id) && 
                         ausencia.diaSemana.equals(diaSemana) && 
                         ausencia.fecha.equals(fechaAusencia));
            
            System.out.println("¿Ya existe ausencia para este día?: " + yaExiste);
            
            if (!yaExiste) {
                Long nuevoId = (long) (ausencias.size() + 1);
                String motivoFinal = (motivo == null || motivo.trim().isEmpty()) ? "No especificado" : motivo.trim();
                
                Ausencia nuevaAusencia = new Ausencia(nuevoId, alumnoActual.id, diaSemana, fechaAusencia, motivoFinal);
                ausencias.add(nuevaAusencia);
                System.out.println("✅ Ausencia creada con ID: " + nuevoId);
                
                // Incrementar el contador de clases no asistidas
                alumnoActual.incrementarAusencias();
                
                // CREAR PLAZA DISPONIBLE: Cuando un alumno no asiste, su plaza queda libre para recuperación
                crearPlazaDisponiblePorAusencia(alumnoActual, diaSemana, fechaAusencia);
                
                // CREAR CLASE PERDIDA: Registrar la clase que el alumno puede recuperar en los próximos 30 días
                System.out.println("🔍 Creando clase perdida para recuperación...");
                crearClasePerdidaParaRecuperacion(alumnoActual, diaSemana, fechaAusencia, motivoFinal);
                
                System.out.println("📊 Total ausencias en sistema: " + ausencias.size());
                System.out.println("📊 Total clases perdidas en sistema: " + clasesPerdidas.size());
                
                redirectAttributes.addFlashAttribute("successMessage", 
                    "Ausencia registrada para " + diaSemana + " (" + fechaAusencia.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "). " +
                    "Tienes 30 días para recuperar esta clase. Tu plaza estará disponible para que otros alumnos puedan recuperar clases.");
            } else {
                redirectAttributes.addFlashAttribute("warningMessage", 
                    "Ya tienes registrada una ausencia para ese día.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error al registrar la ausencia. Verifica la fecha.");
        }
        
        return "redirect:/alumno/horarios";
    }
    
    // Cancelar ausencia
    @PostMapping("/ausencia/cancelar")
    public String cancelarAusencia(@RequestParam Long ausenciaId,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        // Verificar autenticación
        if (!isAuthenticated(session)) {
            return "redirect:/alumno/login";
        }
        
        Alumno alumnoActual = (Alumno) session.getAttribute("alumnoActual");
        
        // Buscar la ausencia antes de eliminarla para poder eliminar la plaza correspondiente
        Ausencia ausenciaAEliminar = ausencias.stream()
            .filter(ausencia -> ausencia.id.equals(ausenciaId) && ausencia.alumnoId.equals(alumnoActual.id))
            .findFirst()
            .orElse(null);
        
        boolean eliminada = ausencias.removeIf(ausencia -> 
            ausencia.id.equals(ausenciaId) && ausencia.alumnoId.equals(alumnoActual.id));
        
        if (eliminada && ausenciaAEliminar != null) {
            // Decrementar el contador de clases no asistidas
            if (alumnoActual.clasesNoAsistidas > 0) {
                alumnoActual.clasesNoAsistidas--;
            }
            
            // Eliminar la plaza disponible correspondiente (si no ha sido ocupada)
            eliminarPlazaDisponiblePorCancelacion(alumnoActual, ausenciaAEliminar);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Ausencia cancelada correctamente. La plaza disponible ha sido eliminada.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "No se pudo cancelar la ausencia.");
        }
        
        return "redirect:/alumno/horarios";
    }
    
    // Logout del alumno
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("alumnoLoggedIn");
        session.removeAttribute("alumnoActual");
        return "redirect:/";
    }
    
    // Método auxiliar para verificar autenticación
    private boolean isAuthenticated(HttpSession session) {
        return session.getAttribute("alumnoLoggedIn") != null;
    }
    
    // Método estático para acceder a la lista de alumnos desde otros controladores
    public static List<Alumno> getAlumnos() {
        return alumnos;
    }
    
    public static List<Grupo> getGrupos() {
        return grupos;
    }
    
    public static boolean addAlumno(Alumno alumno) {
        // Buscar el grupo al que se quiere inscribir (usando el curso como grupo)
        Grupo grupo = grupos.stream()
            .filter(g -> g.getNombre().equals(alumno.curso))
            .findFirst()
            .orElse(null);
            
        if (grupo != null && grupo.puedeAgregarAlumno()) {
            alumnos.add(alumno);
            grupo.agregarAlumno(alumno.id);
            return true;
        }
        return false; // Grupo lleno o no encontrado
    }
    
    public static void removeAlumno(Long id) {
        // Encontrar alumno y removerlo del grupo
        Alumno alumno = alumnos.stream()
            .filter(a -> a.id.equals(id))
            .findFirst()
            .orElse(null);
            
        if (alumno != null) {
            // Remover del grupo
            grupos.stream()
                .filter(g -> g.getNombre().equals(alumno.curso))
                .findFirst()
                .ifPresent(g -> g.eliminarAlumno(id));
            
            // Remover de la lista de alumnos
            alumnos.removeIf(a -> a.id.equals(id));
        }
    }
    
    // Recuperación manual - el alumno elige día y horario específico
    @PostMapping("/recuperacion-manual")
    public String recuperacionManual(@RequestParam String diaSemanaOriginal,
                                    @RequestParam String materiaOriginal,
                                    @RequestParam String diaRecuperacion,
                                    @RequestParam String plazaSeleccionada,
                                    @RequestParam(required = false) String motivoFalta,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        // Verificar autenticación
        if (!isAuthenticated(session)) {
            return "redirect:/alumno/login";
        }
        
        Alumno alumnoActual = (Alumno) session.getAttribute("alumnoActual");
        
        try {
            // Verificar si ya existe una solicitud pendiente para esta materia
            boolean yaExiste = recuperaciones.stream()
                .anyMatch(rec -> rec.alumnoId.equals(alumnoActual.id) && 
                         rec.diaSemana.equals(diaSemanaOriginal) && 
                         rec.materia.equals(materiaOriginal) &&
                         rec.estado.equals("PENDIENTE"));
            
            if (!yaExiste) {
                Long nuevoId = (long) (recuperaciones.size() + 1);
                String motivoFinal = (motivoFalta == null || motivoFalta.trim().isEmpty()) ? 
                    "Recuperación en " + diaRecuperacion + ": " + plazaSeleccionada : motivoFalta.trim();
                
                // Verificar si es una plaza por ausencia y ocuparla
                boolean esPlazaPorAusencia = plazaSeleccionada.contains("Plaza por ausencia de");
                if (esPlazaPorAusencia) {
                    ocuparPlazaDisponible(plazaSeleccionada, alumnoActual.nombre + " " + alumnoActual.apellidos);
                }
                
                RecuperacionClase nuevaRecuperacion = new RecuperacionClase(
                    nuevoId, 
                    alumnoActual.id, 
                    alumnoActual.nombre + " " + alumnoActual.apellidos,
                    diaSemanaOriginal, 
                    LocalDate.now(), 
                    materiaOriginal, 
                    motivoFinal, 
                    alumnoActual.profesor
                );
                nuevaRecuperacion.setObservaciones("Recuperación elegida: " + diaRecuperacion + " - " + plazaSeleccionada);
                recuperaciones.add(nuevaRecuperacion);
                
                String mensaje = esPlazaPorAusencia ? 
                    "✅ Recuperación confirmada! Has elegido una plaza liberada por ausencia para el " + diaRecuperacion :
                    "✅ Recuperación confirmada! Clase programada para el " + diaRecuperacion + " - " + plazaSeleccionada;
                
                redirectAttributes.addFlashAttribute("successMessage", mensaje);
            } else {
                redirectAttributes.addFlashAttribute("warningMessage", 
                    "Ya tienes una solicitud de recuperación pendiente para esa clase.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error al procesar la recuperación. Inténtalo de nuevo.");
        }
        
        return "redirect:/alumno/panel";
    }



    // Recuperación automática - toma la primera plaza disponible (mantenido por compatibilidad)
    @PostMapping("/recuperacion-automatica")
    public String recuperacionAutomatica(@RequestParam String diaSemana,
                                        @RequestParam String materia,
                                        HttpSession session,
                                        RedirectAttributes redirectAttributes) {
        // Verificar autenticación
        if (!isAuthenticated(session)) {
            return "redirect:/alumno/login";
        }
        
        Alumno alumnoActual = (Alumno) session.getAttribute("alumnoActual");
        
        try {
            // Verificar si ya existe una solicitud pendiente para esta materia
            boolean yaExiste = recuperaciones.stream()
                .anyMatch(rec -> rec.alumnoId.equals(alumnoActual.id) && 
                         rec.diaSemana.equals(diaSemana) && 
                         rec.materia.equals(materia) &&
                         rec.estado.equals("PENDIENTE"));
            
            if (!yaExiste) {
                // Obtener cursos disponibles
                List<String> cursosDisponibles = getCursosConPlazasLibres();
                
                if (!cursosDisponibles.isEmpty()) {
                    // Tomar automáticamente la primera opción disponible
                    String cursoSeleccionado = cursosDisponibles.get(0);
                    
                    Long nuevoId = (long) (recuperaciones.size() + 1);
                    String motivoFinal = "Recuperación automática en: " + cursoSeleccionado;
                    
                    // Verificar si es una plaza por ausencia y ocuparla
                    boolean esPlazaPorAusencia = cursoSeleccionado.contains("Plaza por ausencia de");
                    if (esPlazaPorAusencia) {
                        ocuparPlazaDisponible(cursoSeleccionado, alumnoActual.nombre + " " + alumnoActual.apellidos);
                    }
                    
                    RecuperacionClase nuevaRecuperacion = new RecuperacionClase(
                        nuevoId, 
                        alumnoActual.id, 
                        alumnoActual.nombre + " " + alumnoActual.apellidos,
                        diaSemana, 
                        LocalDate.now(), 
                        materia, 
                        motivoFinal, 
                        alumnoActual.profesor
                    );
                    nuevaRecuperacion.setObservaciones("Recuperación automática: " + cursoSeleccionado);
                    recuperaciones.add(nuevaRecuperacion);
                    
                    String mensaje = esPlazaPorAusencia ? 
                        "✅ Recuperación confirmada! Has tomado una plaza liberada por ausencia." :
                        "✅ Recuperación confirmada! Plaza asignada en: " + cursoSeleccionado;
                    
                    redirectAttributes.addFlashAttribute("successMessage", mensaje);
                } else {
                    redirectAttributes.addFlashAttribute("errorMessage", 
                        "No hay plazas disponibles en este momento.");
                }
            } else {
                redirectAttributes.addFlashAttribute("warningMessage", 
                    "Ya tienes una solicitud de recuperación pendiente para esa clase.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error al procesar la recuperación. Inténtalo de nuevo.");
        }
        
        return "redirect:/alumno/horarios";
    }

    // Solicitar recuperación de clase (método original con modal - mantenido por compatibilidad)
    @PostMapping("/recuperacion")
    public String solicitarRecuperacion(@RequestParam String diaSemana,
                                       @RequestParam String materia,
                                       @RequestParam String cursoRecuperacion,
                                       @RequestParam(required = false) String motivoFalta,
                                       HttpSession session,
                                       RedirectAttributes redirectAttributes) {
        // Verificar autenticación
        if (!isAuthenticated(session)) {
            return "redirect:/alumno/login";
        }
        
        Alumno alumnoActual = (Alumno) session.getAttribute("alumnoActual");
        
        try {
            // Verificar si ya existe una solicitud pendiente para esta materia
            boolean yaExiste = recuperaciones.stream()
                .anyMatch(rec -> rec.alumnoId.equals(alumnoActual.id) && 
                         rec.diaSemana.equals(diaSemana) && 
                         rec.materia.equals(materia) &&
                         rec.estado.equals("PENDIENTE"));
            
            if (!yaExiste) {
                Long nuevoId = (long) (recuperaciones.size() + 1);
                String motivoFinal = (motivoFalta == null || motivoFalta.trim().isEmpty()) ? 
                    "Recuperación en: " + cursoRecuperacion : motivoFalta.trim();
                
                // Verificar si el curso seleccionado es una plaza por ausencia
                boolean esPlazaPorAusencia = cursoRecuperacion.contains("Plaza por ausencia de");
                if (esPlazaPorAusencia) {
                    // Buscar y ocupar la plaza disponible
                    ocuparPlazaDisponible(cursoRecuperacion, alumnoActual.nombre + " " + alumnoActual.apellidos);
                }
                
                RecuperacionClase nuevaRecuperacion = new RecuperacionClase(
                    nuevoId, 
                    alumnoActual.id, 
                    alumnoActual.nombre + " " + alumnoActual.apellidos,
                    diaSemana, 
                    LocalDate.now(), 
                    materia, 
                    motivoFinal, 
                    alumnoActual.profesor
                );
                // Agregar información del curso seleccionado para recuperación
                nuevaRecuperacion.setObservaciones("Curso alternativo: " + cursoRecuperacion);
                recuperaciones.add(nuevaRecuperacion);
                
                String mensaje = esPlazaPorAusencia ? 
                    "Solicitud de recuperación enviada para " + materia + " del " + diaSemana + 
                    ". Has tomado una plaza liberada por ausencia de otro alumno." :
                    "Solicitud de recuperación enviada para " + materia + " del " + diaSemana + 
                    ". Curso alternativo: " + cursoRecuperacion;
                
                redirectAttributes.addFlashAttribute("successMessage", mensaje);
            } else {
                redirectAttributes.addFlashAttribute("warningMessage", 
                    "Ya tienes una solicitud de recuperación pendiente para esa clase.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error al enviar la solicitud de recuperación. Inténtalo de nuevo.");
        }
        
        return "redirect:/alumno/horarios";
    }
    
    // Ver solicitudes de recuperación del alumno
    @GetMapping("/recuperaciones")
    public String verRecuperaciones(HttpSession session, Model model) {
        // Verificar autenticación
        if (!isAuthenticated(session)) {
            return "redirect:/alumno/login";
        }
        
        Alumno alumnoActual = (Alumno) session.getAttribute("alumnoActual");
        
        // Obtener recuperaciones del alumno
        List<RecuperacionClase> recuperacionesAlumno = recuperaciones.stream()
            .filter(rec -> rec.alumnoId.equals(alumnoActual.id))
            .collect(Collectors.toList());
        
        model.addAttribute("alumno", alumnoActual);
        model.addAttribute("recuperaciones", recuperacionesAlumno);
        
        return "alumno-recuperaciones";
    }
    
    // Método para obtener cursos con plazas libres para recuperación
    private List<String> getCursosConPlazasLibres() {
        List<String> cursosDisponibles = new ArrayList<>();
        
        // PARTE 1: Obtener cursos reales del AdminController con plazas normales
        List<Curso> cursosReales = com.academia.controller.AdminController.getCursos();
        
        for (Curso curso : cursosReales) {
            // Mapear curso a grupo y calcular plazas disponibles
            String nombreGrupo = mapearCursoAGrupo(curso.nombre);
            if (nombreGrupo != null) {
                Grupo grupo = grupos.stream()
                    .filter(g -> g.getNombre().equals(nombreGrupo))
                    .findFirst()
                    .orElse(null);
                
                if (grupo != null) {
                    int plazasLibres = grupo.getMaxParticipantes() - grupo.getParticipantesActuales();
                    if (plazasLibres > 0) {
                        cursosDisponibles.add(curso.nombre + " - " + nombreGrupo + " (" + plazasLibres + " plazas normales)");
                    }
                }
            }
        }
        
        // PARTE 2: Obtener plazas liberadas por ausencias de otros alumnos
        List<PlazaDisponible> plazasLibresPorAusencias = getPlazasDisponibles();
        for (PlazaDisponible plaza : plazasLibresPorAusencias) {
            String descripcionPlaza = plaza.getCurso() + " - " + plaza.getFechaFormateada() + 
                                     " " + plaza.getHorario() + " (Plaza por ausencia de " + 
                                     plaza.getAlumnoAusente() + ")";
            cursosDisponibles.add(descripcionPlaza);
        }
        
        // Si no hay cursos con plazas libres, agregar algunos opcionales
        if (cursosDisponibles.isEmpty()) {
            cursosDisponibles.add("Costura Intermedia - Grupo Especial (5 plazas libres)");
            cursosDisponibles.add("Técnicas Mixtas - Grupo Extra (3 plazas libres)");
        }
        
        return cursosDisponibles;
    }
    
    // Método auxiliar para mapear cursos a grupos
    private String mapearCursoAGrupo(String nombreCurso) {
        switch (nombreCurso) {
            case "Costura Básica":
                return "Grupo 1";
            case "Patchwork Avanzado":
            case "Bordado Creativo":
                return "Grupo 2";
            default:
                return null; // Cursos que no tienen grupo asignado aún
        }
    }
    
    // Método para obtener información de capacidad de un grupo
    public static String getCapacidadGrupo(String nombreGrupo) {
        return grupos.stream()
            .filter(g -> g.getNombre().equals(nombreGrupo))
            .findFirst()
            .map(Grupo::getInfoCapacidad)
            .orElse("0/10");
    }
    
    // Método para crear una plaza disponible cuando un alumno se ausenta
    private void crearPlazaDisponiblePorAusencia(Alumno alumnoAusente, String diaSemana, LocalDate fecha) {
        // Buscar el horario correspondiente al día de la ausencia
        Horario horarioCorrespondiente = horarios.stream()
            .filter(h -> h.grupo.equals(alumnoAusente.curso) && h.diaSemana.equalsIgnoreCase(diaSemana))
            .findFirst()
            .orElse(null);
        
        if (horarioCorrespondiente != null) {
            String curso = horarioCorrespondiente.materia;
            String horario = horarioCorrespondiente.horaInicio + " - " + horarioCorrespondiente.horaFin;
            String nombreCompleto = alumnoAusente.nombre + " " + alumnoAusente.apellidos;
            
            // Crear la plaza disponible
            PlazaDisponible nuevaPlaza = new PlazaDisponible(curso, nombreCompleto, fecha, horario);
            plazasDisponibles.add(nuevaPlaza);
            
            System.out.println("✅ Plaza disponible creada: " + nuevaPlaza.getDescripcion() + 
                             " - Liberada por: " + nombreCompleto);
        }
    }
    
    // Método para obtener plazas disponibles por ausencias
    public static List<PlazaDisponible> getPlazasDisponibles() {
        return plazasDisponibles.stream()
            .filter(PlazaDisponible::estaDisponible) // Solo las no ocupadas
            .collect(Collectors.toList());
    }
    
    // Método para obtener todas las plazas (incluyendo ocupadas)
    public static List<PlazaDisponible> getTodasLasPlazas() {
        return new ArrayList<>(plazasDisponibles);
    }
    
    // Método para organizar plazas disponibles por día y horario con fechas específicas
    private Map<String, List<String>> getPlazasPorDiaYHorarioConFechas() {
        Map<String, List<String>> plazasPorDia = new LinkedHashMap<>();
        
        // Obtener la fecha actual
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        // Generar los próximos 30 días con formato "Día dd/MM/yyyy"
        for (int i = 0; i < 30; i++) {
            LocalDate fecha = fechaActual.plusDays(i);
            String diaSemana = obtenerDiaDeLaSemana(fecha);
            String fechaFormateada = fecha.format(formatter);
            String claveCompleta = diaSemana + " " + fechaFormateada;
            plazasPorDia.put(claveCompleta, new ArrayList<>());
        }
        
        // PARTE 1: Agregar plazas normales de TODOS los cursos creados
        List<Curso> cursosReales = com.academia.controller.AdminController.getCursos();
        for (Curso curso : cursosReales) {
            // Crear plazas disponibles para todos los cursos, no solo los mapeados
            // Cada curso tiene un máximo de 10 plazas por defecto
            int maxPlazasPorCurso = 10;
            int plazasOcupadasCurso = contarAlumnosEnCurso(curso.nombre);
            int plazasLibresCurso = Math.max(0, maxPlazasPorCurso - plazasOcupadasCurso);
            
            if (plazasLibresCurso > 0) {
                // Generar horarios basados en la descripción del curso
                List<String> horariosDelCurso = extraerHorariosDelCurso(curso);
                
                // Agregar a cada día correspondiente en los próximos 30 días
                for (int i = 0; i < 30; i++) {
                    LocalDate fecha = fechaActual.plusDays(i);
                    String diaSemanaFecha = obtenerDiaDeLaSemana(fecha);
                    String fechaFormateada = fecha.format(formatter);
                    String claveCompleta = diaSemanaFecha + " " + fechaFormateada;
                    
                    for (String horarioCurso : horariosDelCurso) {
                        // Verificar si el horario corresponde a este día de la semana
                        if (horarioCurso.toLowerCase().contains(diaSemanaFecha.toLowerCase())) {
                            String descripcion = curso.nombre + " - " + extraerHoraDelHorario(horarioCurso) + 
                                               " (" + plazasLibresCurso + " plazas disponibles)";
                            plazasPorDia.get(claveCompleta).add(descripcion);
                        }
                    }
                }
            }
        }
        
        // PARTE 2: Agregar plazas por ausencias para fechas específicas
        List<PlazaDisponible> plazasLibresPorAusencias = getPlazasDisponibles();
        for (PlazaDisponible plaza : plazasLibresPorAusencias) {
            String diaSemanaPlaza = obtenerDiaDeLaSemana(plaza.getFecha());
            String fechaFormateadaPlaza = plaza.getFecha().format(formatter);
            String claveCompletaPlaza = diaSemanaPlaza + " " + fechaFormateadaPlaza;
            
            // Solo agregar si está dentro de nuestro rango de 30 días
            if (plazasPorDia.containsKey(claveCompletaPlaza)) {
                String descripcion = plaza.getCurso() + " - " + plaza.getHorario() + 
                                   " (Plaza por ausencia de " + plaza.getAlumnoAusente() + ")";
                plazasPorDia.get(claveCompletaPlaza).add(descripcion);
            }
        }
        
        return plazasPorDia;
    }
    
    // Método para organizar plazas disponibles por día y horario
    private Map<String, List<String>> getPlazasPorDiaYHorario() {
        Map<String, List<String>> plazasPorDia = new LinkedHashMap<>();
        
        // Inicializar días de la semana
        String[] diasSemana = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"};
        for (String dia : diasSemana) {
            plazasPorDia.put(dia, new ArrayList<>());
        }
        
        // PARTE 1: Agregar plazas normales de TODOS los cursos creados
        List<Curso> cursosReales = com.academia.controller.AdminController.getCursos();
        for (Curso curso : cursosReales) {
            // Crear plazas disponibles para todos los cursos, no solo los mapeados
            int maxPlazasPorCurso = 10;
            int plazasOcupadasCurso = contarAlumnosEnCurso(curso.nombre);
            int plazasLibresCurso = Math.max(0, maxPlazasPorCurso - plazasOcupadasCurso);
            
            if (plazasLibresCurso > 0) {
                // Generar horarios basados en la descripción del curso
                List<String> horariosDelCurso = extraerHorariosDelCurso(curso);
                
                for (String horarioCurso : horariosDelCurso) {
                    // Extraer el día de la semana del horario
                    String diaSemana = horarioCurso.split(" ")[0];
                    String hora = extraerHoraDelHorario(horarioCurso);
                    
                    String descripcion = curso.nombre + " - " + hora + 
                                       " (" + plazasLibresCurso + " plazas disponibles)";
                    plazasPorDia.get(diaSemana).add(descripcion);
                }
            }
        }
        
        // PARTE 2: Agregar plazas por ausencias organizadas por día
        List<PlazaDisponible> plazasLibresPorAusencias = getPlazasDisponibles();
        for (PlazaDisponible plaza : plazasLibresPorAusencias) {
            // Determinar día de la semana basado en la fecha
            String diaSemana = obtenerDiaDeLaSemana(plaza.getFecha());
            String descripcion = plaza.getCurso() + " - " + plaza.getHorario() + 
                               " (Plaza por ausencia de " + plaza.getAlumnoAusente() + ")";
            plazasPorDia.get(diaSemana).add(descripcion);
        }
        
        return plazasPorDia;
    }
    
    // Método para crear una clase perdida que se puede recuperar en 30 días
    private void crearClasePerdidaParaRecuperacion(Alumno alumno, String diaSemana, LocalDate fechaAusencia, String motivo) {
        System.out.println("🏗️ INICIANDO crearClasePerdidaParaRecuperacion:");
        System.out.println("  - Alumno: " + alumno.nombre + " " + alumno.apellidos + " (ID: " + alumno.id + ")");
        System.out.println("  - Curso del alumno: " + alumno.curso);
        System.out.println("  - Día: " + diaSemana);
        System.out.println("  - Fecha: " + fechaAusencia);
        System.out.println("  - Motivo: " + motivo);
        System.out.println("  - Total horarios en sistema: " + horarios.size());
        
        // Buscar el horario correspondiente al día y grupo del alumno
        List<Horario> horariosDelDia = horarios.stream()
            .filter(h -> h.diaSemana.equalsIgnoreCase(diaSemana) && h.grupo.equals(alumno.curso))
            .collect(Collectors.toList());
            
        System.out.println("  - Horarios encontrados para " + diaSemana + " y grupo " + alumno.curso + ": " + horariosDelDia.size());
        if (horariosDelDia.isEmpty()) {
            System.out.println("  ❌ NO SE ENCONTRARON HORARIOS - NO SE CREARÁN CLASES PERDIDAS");
            return;
        }
        
        for (Horario horario : horariosDelDia) {
            Long nuevoId = (long) (clasesPerdidas.size() + 1);
            ClasePerdida clasePerdida = new ClasePerdida(
                nuevoId, 
                alumno.id, 
                diaSemana, 
                fechaAusencia, 
                horario.materia, 
                horario.getHorarioFormateado(),
                motivo
            );
            
            clasesPerdidas.add(clasePerdida);
            
            System.out.println("📚 Clase perdida creada: " + alumno.nombre + " puede recuperar " + 
                             horario.materia + " del " + diaSemana + " " + fechaAusencia + 
                             " hasta el " + clasePerdida.getFechaLimiteFormateada() + " (30 días naturales)");
        }
    }
    
    // Método para obtener las clases perdidas de un alumno que aún puede recuperar
    private List<ClasePerdida> getClasesPerdidas(Long alumnoId) {
        System.out.println("🔍 getClasesPerdidas - Buscando clases perdidas para alumno ID: " + alumnoId);
        System.out.println("📊 getClasesPerdidas - Total clases perdidas en sistema: " + clasesPerdidas.size());
        
        // Mostrar todas las clases perdidas
        for (ClasePerdida clase : clasesPerdidas) {
            System.out.println("  - ID: " + clase.getId() + 
                             ", AlumnoID: " + clase.getAlumnoId() + 
                             ", Día: " + clase.getDiaSemana() + 
                             ", Fecha: " + clase.getFechaAusencia() + 
                             ", Puede recuperarse: " + clase.puedeRecuperarse());
        }
        
        List<ClasePerdida> resultado = clasesPerdidas.stream()
            .filter(clase -> clase.getAlumnoId().equals(alumnoId) && clase.puedeRecuperarse())
            .collect(Collectors.toList());
            
        System.out.println("✅ getClasesPerdidas - Clases perdidas encontradas para alumno " + alumnoId + ": " + resultado.size());
        
        return resultado;
    }
    
    // Método para calcular dinámicamente las ausencias actuales de un alumno
    public int calcularAusenciasActuales(Long alumnoId) {
        // Contar todas las ausencias del alumno (sin recuperar)
        long ausenciasSinRecuperar = ausencias.stream()
            .filter(ausencia -> ausencia.alumnoId.equals(alumnoId))
            .count();
            
        // Restar las ausencias que ya han sido recuperadas
        long ausenciasRecuperadas = clasesPerdidas.stream()
            .filter(clase -> clase.getAlumnoId().equals(alumnoId) && clase.isRecuperada())
            .count();
            
        // Restar las ausencias cuyo plazo de recuperación ha expirado (más de 30 días)
        long ausenciasExpiradas = clasesPerdidas.stream()
            .filter(clase -> clase.getAlumnoId().equals(alumnoId) && !clase.puedeRecuperarse())
            .count();
            
        // Calcular ausencias actuales: Total - Recuperadas - Expiradas
        int ausenciasActuales = (int)(ausenciasSinRecuperar - ausenciasRecuperadas);
        
        System.out.println("📊 Calculando ausencias para alumno " + alumnoId + ":");
        System.out.println("  - Ausencias totales: " + ausenciasSinRecuperar);
        System.out.println("  - Ausencias recuperadas: " + ausenciasRecuperadas);
        System.out.println("  - Ausencias expiradas: " + ausenciasExpiradas);
        System.out.println("  - Ausencias actuales: " + ausenciasActuales);
        
        return Math.max(0, ausenciasActuales); // No puede ser negativo
    }
    
    // Método para demostrar el cálculo de 30 días naturales
    public void mostrarEjemplosCalculoDias() {
        System.out.println("=== EJEMPLOS DE CÁLCULO DE 30 DÍAS NATURALES ===");
        
        // Ejemplo 1: Ausencia un lunes
        LocalDate ausenciaLunes = LocalDate.of(2025, 10, 15); // Martes 15 octubre
        LocalDate limiteLunes = ausenciaLunes.plusDays(30);
        System.out.println("📅 Ausencia: " + ausenciaLunes + " (martes)");
        System.out.println("⏰ Límite recuperación: " + limiteLunes + " (30 días naturales después)");
        System.out.println("📊 Incluye fines de semana y festivos en el conteo");
        
        // Ejemplo 2: Contar días entre fechas
        long diasEntre = java.time.temporal.ChronoUnit.DAYS.between(ausenciaLunes, limiteLunes);
        System.out.println("🔢 Días calculados: " + diasEntre + " días naturales");
        
        System.out.println("✅ Confirmado: El sistema usa 30 días naturales (no laborables)");
        System.out.println("==================================================");
    }
    
    // Método para marcar una clase perdida como recuperada
    @PostMapping("/recuperar-clase")
    public String recuperarClase(@RequestParam Long claseId,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        // Verificar autenticación
        if (!isAuthenticated(session)) {
            return "redirect:/alumno/login";
        }
        
        Alumno alumnoActual = (Alumno) session.getAttribute("alumnoActual");
        
        // Buscar la clase perdida y marcarla como recuperada
        ClasePerdida claseEncontrada = clasesPerdidas.stream()
            .filter(clase -> clase.getId().equals(claseId) && clase.getAlumnoId().equals(alumnoActual.id))
            .findFirst()
            .orElse(null);
            
        if (claseEncontrada != null) {
            claseEncontrada.setRecuperada(true);
            
            System.out.println("✅ Clase recuperada marcada para alumno " + alumnoActual.nombre + 
                             ": " + claseEncontrada.getDiaSemana() + " " + claseEncontrada.getFechaAusencia());
            
            redirectAttributes.addFlashAttribute("success", "¡Clase recuperada exitosamente!");
        } else {
            redirectAttributes.addFlashAttribute("error", "No se encontró la clase a recuperar.");
        }
        
        return "redirect:/alumno/" + alumnoActual.id + "/panel";
    }
    
    // Método para contar cuántos alumnos están inscritos en un curso específico
    private int contarAlumnosEnCurso(String nombreCurso) {
        // Por ahora, devolver un número simulado basado en la ocupación de grupos
        // En una implementación real, esto consultaría la base de datos
        switch (nombreCurso) {
            case "Costura Básica":
                return grupos.stream()
                    .filter(g -> g.getNombre().equals("Grupo 1"))
                    .findFirst()
                    .map(Grupo::getParticipantesActuales)
                    .orElse(0);
            case "Patchwork Avanzado":
            case "Bordado Creativo":
                return grupos.stream()
                    .filter(g -> g.getNombre().equals("Grupo 2"))
                    .findFirst()
                    .map(Grupo::getParticipantesActuales)
                    .orElse(0);
            default:
                // Para cursos nuevos, asumir ocupación baja (2-3 alumnos)
                return 2 + (int)(Math.random() * 2); // Entre 2 y 3 alumnos
        }
    }
    
    // Método para extraer los días y horarios de la descripción de un curso
    private List<String> extraerHorariosDelCurso(Curso curso) {
        List<String> horarios = new ArrayList<>();
        String descripcion = curso.horario.toLowerCase();
        
        // Parsear descripción como "Lunes y Miércoles 10:00-12:00" o "Martes y Jueves 17:00-19:00"
        if (descripcion.contains("lunes") && descripcion.contains("miércoles")) {
            String hora = extraerHoraDeDescripcion(descripcion);
            horarios.add("Lunes " + hora);
            horarios.add("Miércoles " + hora);
        } else if (descripcion.contains("martes") && descripcion.contains("jueves")) {
            String hora = extraerHoraDeDescripcion(descripcion);
            horarios.add("Martes " + hora);
            horarios.add("Jueves " + hora);
        } else if (descripcion.contains("sábado")) {
            String hora = extraerHoraDeDescripcion(descripcion);
            horarios.add("Sábado " + hora);
        } else if (descripcion.contains("viernes")) {
            String hora = extraerHoraDeDescripcion(descripcion);
            horarios.add("Viernes " + hora);
        } else {
            // Para cursos con descripción no estándar, generar horarios por defecto
            horarios.add("Lunes 10:00-12:00");
            horarios.add("Miércoles 10:00-12:00");
        }
        
        return horarios;
    }
    
    // Método para extraer la hora de una descripción de curso
    private String extraerHoraDeDescripcion(String descripcion) {
        // Buscar patrón como "10:00-12:00" o "17:00-19:00"
        java.util.regex.Pattern patron = java.util.regex.Pattern.compile("\\d{1,2}:\\d{2}-\\d{1,2}:\\d{2}");
        java.util.regex.Matcher matcher = patron.matcher(descripcion);
        if (matcher.find()) {
            return matcher.group();
        }
        return "10:00-12:00"; // Horario por defecto si no se encuentra
    }
    
    // Método para extraer solo la parte de la hora de un horario completo
    private String extraerHoraDelHorario(String horarioCompleto) {
        // De "Lunes 10:00-12:00" extraer "10:00-12:00"
        String[] partes = horarioCompleto.split(" ");
        if (partes.length >= 2) {
            return partes[1];
        }
        return "10:00-12:00";
    }
    
    // Método auxiliar para obtener el día de la semana de una fecha
    private String obtenerDiaDeLaSemana(LocalDate fecha) {
        switch (fecha.getDayOfWeek()) {
            case MONDAY: return "Lunes";
            case TUESDAY: return "Martes";
            case WEDNESDAY: return "Miércoles";
            case THURSDAY: return "Jueves";
            case FRIDAY: return "Viernes";
            case SATURDAY: return "Sábado";
            case SUNDAY: return "Domingo";
            default: return "Desconocido";
        }
    }
    
    // Método para eliminar plaza disponible cuando se cancela una ausencia
    private void eliminarPlazaDisponiblePorCancelacion(Alumno alumno, Ausencia ausenciaCancelada) {
        String nombreCompleto = alumno.nombre + " " + alumno.apellidos;
        
        boolean plazaEliminada = plazasDisponibles.removeIf(plaza -> 
            plaza.getAlumnoAusente().equals(nombreCompleto) && 
            plaza.getFecha().equals(ausenciaCancelada.fecha) &&
            !plaza.isOcupada()); // Solo eliminar si no ha sido ocupada por otro alumno
        
        if (plazaEliminada) {
            System.out.println("🗑️ Plaza disponible eliminada por cancelación de ausencia: " + 
                             nombreCompleto + " - " + ausenciaCancelada.fecha);
        } else {
            System.out.println("⚠️ No se pudo eliminar la plaza o ya estaba ocupada por otro alumno");
        }
    }
    
    // Método para ocupar una plaza disponible cuando un alumno la usa para recuperación
    private void ocuparPlazaDisponible(String descripcionCurso, String alumnoRecuperacion) {
        // Buscar la plaza disponible que coincida con la descripción
        for (PlazaDisponible plaza : plazasDisponibles) {
            String descripcionPlaza = plaza.getCurso() + " - " + plaza.getFechaFormateada() + 
                                     " " + plaza.getHorario() + " (Plaza por ausencia de " + 
                                     plaza.getAlumnoAusente() + ")";
            
            if (descripcionPlaza.equals(descripcionCurso) && plaza.estaDisponible()) {
                plaza.ocuparPlaza(alumnoRecuperacion);
                System.out.println("✅ Plaza ocupada para recuperación: " + descripcionPlaza + 
                                 " - Ocupada por: " + alumnoRecuperacion);
                break;
            }
        }
    }
    
    // Procesar la recuperación de una clase perdida específica
    @PostMapping("/recuperacion-clase-perdida")
    public String procesarRecuperacionClasePerdida(@RequestParam String clasePerdidaId,
                                                  @RequestParam String materiaOriginal,
                                                  @RequestParam String diaRecuperacion, 
                                                  @RequestParam String plazaSeleccionada,
                                                  HttpSession session,
                                                  RedirectAttributes redirectAttributes) {
        
        // Verificar sesión
        Long alumnoId = (Long) session.getAttribute("alumnoId");
        if (alumnoId == null) {
            return "redirect:/alumno/login";
        }
        
        try {
            System.out.println("=== PROCESANDO RECUPERACIÓN CLASE PERDIDA ===");
            System.out.println("Clase Perdida ID: " + clasePerdidaId);
            System.out.println("Materia Original: " + materiaOriginal);
            System.out.println("Día recuperación: " + diaRecuperacion);
            System.out.println("Plaza seleccionada: " + plazaSeleccionada);
            
            // Buscar la clase perdida
            ClasePerdida clasePerdida = clasesPerdidas.stream()
                .filter(cp -> cp.getId().equals(Long.parseLong(clasePerdidaId)))
                .findFirst()
                .orElse(null);
            
            if (clasePerdida == null) {
                redirectAttributes.addFlashAttribute("error", "No se encontró la clase perdida especificada");
                return "redirect:/alumno/panel";
            }
            
            // Verificar que aún puede recuperarse
            if (!clasePerdida.puedeRecuperarse()) {
                redirectAttributes.addFlashAttribute("error", "Esta clase ya no puede recuperarse. Ha pasado el límite de 30 días");
                return "redirect:/alumno/panel";
            }
            
            // Marcar la clase como recuperada
            clasePerdida.marcarComoRecuperada(LocalDate.now(), 
                String.format("Recuperada el %s (%s)", diaRecuperacion, plazaSeleccionada));
            
            // Eliminar de la lista de clases pendientes
            clasesPerdidas.remove(clasePerdida);
            
            // Crear registro de recuperación (opcional, para histórico)
            // Aquí podrías agregar lógica adicional si quieres mantener un histórico
            
            System.out.println("Clase perdida recuperada exitosamente");
            
            // Mensaje de éxito
            redirectAttributes.addFlashAttribute("success", 
                String.format("¡Perfecto! Has programado la recuperación de tu clase de %s para el %s (%s). " +
                             "Te esperamos en el aula.", 
                             materiaOriginal, diaRecuperacion, plazaSeleccionada));
            
        } catch (Exception e) {
            System.out.println("Error procesando recuperación: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al procesar la recuperación: " + e.getMessage());
        }
        
        return "redirect:/alumno/panel";
    }
    
    // Generar lista plana de todas las opciones con día y horario completo
    private List<String> generarOpcionesCompletasConDiaYHorario() {
        List<String> opcionesCompletas = new ArrayList<>();
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        // PARTE 1: Opciones de cursos normales
        List<Curso> cursosReales = com.academia.controller.AdminController.getCursos();
        for (Curso curso : cursosReales) {
            int maxPlazasPorCurso = 10;
            int plazasOcupadasCurso = contarAlumnosEnCurso(curso.nombre);
            int plazasLibresCurso = Math.max(0, maxPlazasPorCurso - plazasOcupadasCurso);
            
            if (plazasLibresCurso > 0) {
                List<String> horariosDelCurso = extraerHorariosDelCurso(curso);
                
                // Generar opciones para los próximos 30 días
                for (int i = 0; i < 30; i++) {
                    LocalDate fecha = fechaActual.plusDays(i);
                    String diaSemanaFecha = obtenerDiaDeLaSemana(fecha);
                    String fechaFormateada = fecha.format(formatter);
                    
                    for (String horarioCurso : horariosDelCurso) {
                        // Verificar si el horario corresponde a este día de la semana
                        if (horarioCurso.toLowerCase().contains(diaSemanaFecha.toLowerCase())) {
                            String horaExtraida = extraerHoraDelHorario(horarioCurso);
                            String textoMostrar = diaSemanaFecha + " " + fechaFormateada + ": " + 
                                             curso.nombre + " - " + horaExtraida + 
                                             " (" + plazasLibresCurso + " plazas disponibles)";
                            String valorCompleto = diaSemanaFecha + "|" + curso.nombre + " - " + horaExtraida + 
                                                  " (" + plazasLibresCurso + " plazas disponibles)";
                            opcionesCompletas.add(valorCompleto + "||" + textoMostrar);
                        }
                    }
                }
            }
        }
        
        // PARTE 2: Opciones de plazas por ausencias
        List<PlazaDisponible> plazasLibresPorAusencias = getPlazasDisponibles();
        for (PlazaDisponible plaza : plazasLibresPorAusencias) {
            String diaSemana = obtenerDiaDeLaSemana(plaza.getFecha());
            String fechaFormateada = plaza.getFecha().format(formatter);
            String textoMostrar = diaSemana + " " + fechaFormateada + ": " + 
                                 plaza.getCurso() + " - " + plaza.getHorario() + 
                                 " (Plaza por ausencia de " + plaza.getAlumnoAusente() + ")";
            String valorCompleto = diaSemana + "|" + plaza.getCurso() + " - " + plaza.getHorario() + 
                                  " (Plaza por ausencia de " + plaza.getAlumnoAusente() + ")";
            opcionesCompletas.add(valorCompleto + "||" + textoMostrar);
        }
        
        // Ordenar por fecha (las opciones ya vienen ordenadas por la lógica de generación)
        opcionesCompletas.sort((a, b) -> {
            try {
                String textoA = a.split("\\|\\|")[1];
                String textoB = b.split("\\|\\|")[1];
                String fechaA = textoA.substring(textoA.indexOf(" ") + 1, textoA.indexOf(":"));
                String fechaB = textoB.substring(textoB.indexOf(" ") + 1, textoB.indexOf(":"));
                LocalDate dateA = LocalDate.parse(fechaA, formatter);
                LocalDate dateB = LocalDate.parse(fechaB, formatter);
                return dateA.compareTo(dateB);
            } catch (Exception e) {
                return 0;
            }
        });
        
        return opcionesCompletas;
    }
    
    // ========= ENDPOINTS REST PARA LA ACADEMIA (INDEPENDIENTES DEL ATELIER) =========
    
    /**
     * Endpoint REST para obtener horarios disponibles de la Academia por fecha
     */
    @GetMapping("/api/horarios-disponibles-academia")
    @ResponseBody
    public ResponseEntity<List<Map<String, String>>> obtenerHorariosDisponiblesAcademia(@RequestParam String fecha) {
        try {
            LocalDate fechaSeleccionada = LocalDate.parse(fecha);
            
            if (!horarioAcademiaService.esFechaDisponibleParaAcademia(fechaSeleccionada)) {
                return ResponseEntity.ok(List.of());
            }
            
            List<Map<String, String>> horariosDisponibles = horarioAcademiaService.obtenerHorariosAcademia(fechaSeleccionada);
            return ResponseEntity.ok(horariosDisponibles);
            
        } catch (Exception e) {
            return ResponseEntity.ok(List.of());
        }
    }
    
    /**
     * Endpoint REST para obtener información del día de la Academia
     */
    @GetMapping("/api/info-dia-academia")
    @ResponseBody
    public ResponseEntity<Map<String, String>> obtenerInfoDiaAcademia(@RequestParam String fecha) {
        try {
            LocalDate fechaSeleccionada = LocalDate.parse(fecha);
            Map<String, String> infoDia = horarioAcademiaService.obtenerInfoDiaAcademia(fechaSeleccionada);
            return ResponseEntity.ok(infoDia);
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("error", "Fecha inválida"));
        }
    }
    
    /**
     * Endpoint REST para obtener plazas disponibles por curso en la Academia
     */
    @GetMapping("/api/plazas-disponibles-academia")
    @ResponseBody
    public ResponseEntity<List<Map<String, String>>> obtenerPlazasDisponiblesAcademia(@RequestParam String curso) {
        try {
            List<Map<String, String>> plazas = horarioAcademiaService.obtenerPlazasDisponiblesParaRecuperacion(curso);
            return ResponseEntity.ok(plazas);
        } catch (Exception e) {
            return ResponseEntity.ok(List.of());
        }
    }
    
    /**
     * Endpoint REST para contar plazas disponibles por curso en la Academia
     */
    @GetMapping("/api/contar-plazas-academia")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> contarPlazasDisponiblesAcademia(@RequestParam String curso) {
        try {
            long cantidadPlazas = horarioAcademiaService.contarPlazasDisponibles(curso);
            
            Map<String, Object> respuesta = new LinkedHashMap<>();
            respuesta.put("curso", curso);
            respuesta.put("plazasDisponibles", cantidadPlazas);
            respuesta.put("mensaje", cantidadPlazas > 0 ? 
                cantidadPlazas + " plazas disponibles para recuperación" : 
                "No hay plazas disponibles para este curso");
            
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("error", "Error al contar plazas"));
        }
    }
}
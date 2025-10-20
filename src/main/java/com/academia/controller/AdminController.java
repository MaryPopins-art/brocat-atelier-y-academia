// filepath: c:\Users\Ramon\Desktop\Crissel\1.0\academia-horarios\src\main\java\com\academia\controller\AdminController.java
package com.academia.controller;

import com.academia.Alumno;
import com.academia.Profesor;
import com.academia.Curso;
import com.academia.Grupo;
import com.academia.CitaPrevia;
import com.academia.repository.CitaPreviaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.time.format.DateTimeFormatter;

@Controller
public class AdminController {
    // Credenciales de administrador (en producción deberían estar en base de datos y encriptadas)
    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASSWORD = "admin123";
    
    // Listas simuladas en memoria (luego se usarán repositorios)
    private List<Profesor> profesores = new ArrayList<>();
    private static List<Curso> cursos = new ArrayList<>();
    
    // Inyección del repositorio de citas previas
    @Autowired
    private CitaPreviaRepository citaPreviaRepository;
    
    // Referencia estática al AlumnoController para acceder a los métodos de ausencias
    private static AlumnoController alumnoController;

    public AdminController() {
        // Crear profesores con información completa
        Profesor maria = new Profesor(1L, "María", "Costura Básica, Bordado Creativo");
        maria.setApellidos("García López");
        maria.setEmail("maria.garcia@brocat.es");
        maria.setTelefono("654 321 098");
        profesores.add(maria);
        
        Profesor carlos = new Profesor(2L, "Carlos", "Patchwork Avanzado");
        carlos.setApellidos("Ruiz Martín");
        carlos.setEmail("carlos.ruiz@brocat.es");
        carlos.setTelefono("698 765 432");
        profesores.add(carlos);
        
        cursos.add(new Curso(1L, "Costura Básica", "Lunes y Miércoles 10:00-12:00"));
        cursos.add(new Curso(2L, "Patchwork Avanzado", "Martes y Jueves 17:00-19:00"));
        cursos.add(new Curso(3L, "Bordado Creativo", "Sábados 09:00-11:00"));
    }
    
    // Método para crear citas de ejemplo (se ejecuta después de la inyección de dependencias)
    @PostConstruct
    private void crearCitasDeEjemplo() {
        try {
            // Verificar si ya existen citas para evitar duplicados
            if (citaPreviaRepository.count() == 0) {
                System.out.println("=== CREANDO CITAS DE EJEMPLO PARA ATELIER ===");
                
                // Cita 1
                CitaPrevia cita1 = new CitaPrevia();
                cita1.setNombre("Ana Martínez");
                cita1.setTelefono("666 123 456");
                cita1.setEmail("ana.martinez@email.com");
                cita1.setTipoServicio("Arreglo de ropa");
                cita1.setFechaPreferida(LocalDate.now().plusDays(2));
                cita1.setHoraPreferida("10:00");
                cita1.setDescripcion("Necesito acortar un vestido para una boda");
                cita1.setObservaciones("Tengo prisa, es para el fin de semana");
                citaPreviaRepository.save(cita1);
                
                // Cita 2
                CitaPrevia cita2 = new CitaPrevia();
                cita2.setNombre("Carlos Rodriguez");
                cita2.setTelefono("677 987 654");
                cita2.setEmail("carlos.rod@gmail.com");
                cita2.setTipoServicio("Costura creativa");
                cita2.setFechaPreferida(LocalDate.now().plusDays(3));
                cita2.setHoraPreferida("17:30");
                cita2.setDescripcion("Quiero hacer un bolso personalizado para mi esposa");
                cita2.setObservaciones("Es un regalo de aniversario");
                citaPreviaRepository.save(cita2);
                
                // Cita 3
                CitaPrevia cita3 = new CitaPrevia();
                cita3.setNombre("Isabel López");
                cita3.setTelefono("688 456 789");
                cita3.setEmail("isabel.lopez@hotmail.com");
                cita3.setTipoServicio("Bordado personalizado");
                cita3.setFechaPreferida(LocalDate.now().plusDays(5));
                cita3.setHoraPreferida("11:30");
                cita3.setDescripcion("Bordar las iniciales en unas toallas");
                cita3.setObservaciones("Son para regalo de bodas");
                citaPreviaRepository.save(cita3);
                
                // Cita 4
                CitaPrevia cita4 = new CitaPrevia();
                cita4.setNombre("Miguel Fernández");
                cita4.setTelefono("699 321 654");
                cita4.setEmail("miguel.fernandez@empresa.com");
                cita4.setTipoServicio("Reparación de tapicería");
                cita4.setFechaPreferida(LocalDate.now().plusDays(7));
                cita4.setHoraPreferida("19:00");
                cita4.setDescripcion("Reparar el tapizado de una silla antigua");
                cita4.setObservaciones("Es una silla de valor sentimental");
                citaPreviaRepository.save(cita4);
                
                // Cita 5
                CitaPrevia cita5 = new CitaPrevia();
                cita5.setNombre("Laura Sánchez");
                cita5.setTelefono("655 789 123");
                cita5.setEmail("laura.sanchez@yahoo.es");
                cita5.setTipoServicio("Consulta de diseño");
                cita5.setFechaPreferida(LocalDate.now().plusDays(4));
                cita5.setHoraPreferida("12:30");
                cita5.setDescripcion("Asesoramiento para diseñar cortinas del salón");
                cita5.setObservaciones("Estoy redecorando toda la casa");
                citaPreviaRepository.save(cita5);
                
                System.out.println("✅ 5 citas de ejemplo creadas para el Atelier");
            } else {
                System.out.println("ℹ️  Ya existen citas en la base de datos, saltando creación de ejemplos");
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error creando citas de ejemplo: " + e.getMessage());
        }
    }
    
    // Mostrar página de login
    @GetMapping("/login")
    public String loginForm(Model model, HttpSession session) {
        // Si ya está autenticado, redirigir al panel
        if (session.getAttribute("adminLoggedIn") != null) {
            return "redirect:/admin";
        }
        return "admin-login";
    }
    
    // Procesar login
    @PostMapping("/login")
    public String processLogin(@RequestParam String usuario, 
                              @RequestParam String password, 
                              HttpSession session, 
                              Model model) {
        if (ADMIN_USER.equals(usuario) && ADMIN_PASSWORD.equals(password)) {
            session.setAttribute("adminLoggedIn", true);
            return "redirect:/admin";
        } else {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
            return "admin-login";
        }
    }
    
    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    
    // Método auxiliar para verificar autenticación
    private boolean isAuthenticated(HttpSession session) {
        return session.getAttribute("adminLoggedIn") != null;
    }

    @GetMapping("/admin")
    public String adminPanel(Model model, HttpSession session) {
        // Verificar autenticación
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        
        // ===== DATOS PARA ACADEMIA BROCAT =====
        List<Alumno> alumnos = AlumnoController.getAlumnos();
        model.addAttribute("alumnos", alumnos);
        model.addAttribute("profesores", profesores);
        model.addAttribute("cursos", cursos);
        
        // Crear Map con contador de alumnos por curso
        Map<String, String> contadorAlumnosPorCurso = new HashMap<>();
        for (Curso curso : cursos) {
            contadorAlumnosPorCurso.put(curso.nombre, getAlumnosEnCurso(curso.nombre));
        }
        model.addAttribute("contadorAlumnosPorCurso", contadorAlumnosPorCurso);
        
        // Crear Map con cursos y sus horarios
        Map<String, String> cursosConHorarios = new HashMap<>();
        for (Curso curso : cursos) {
            cursosConHorarios.put(curso.getNombre(), curso.getHorario());
        }
        model.addAttribute("cursosConHorarios", cursosConHorarios);
        
        // 🔄 NUEVO: Calcular ausencias dinámicas para cada alumno
        Map<Long, Integer> ausenciasDinamicas = new HashMap<>();
        try {
            // Obtener una instancia del AlumnoController si no existe
            if (alumnoController == null) {
                // Crear una instancia temporal para acceder al método
                AlumnoController tempController = new AlumnoController();
                for (Alumno alumno : alumnos) {
                    int ausenciasActuales = tempController.calcularAusenciasActuales(alumno.id);
                    ausenciasDinamicas.put(alumno.id, ausenciasActuales);
                }
            } else {
                for (Alumno alumno : alumnos) {
                    int ausenciasActuales = alumnoController.calcularAusenciasActuales(alumno.id);
                    ausenciasDinamicas.put(alumno.id, ausenciasActuales);
                }
            }
        } catch (Exception e) {
            System.err.println("Error calculando ausencias dinámicas: " + e.getMessage());
            // Fallback: usar valores actuales del objeto Alumno
            for (Alumno alumno : alumnos) {
                ausenciasDinamicas.put(alumno.id, alumno.clasesNoAsistidas);
            }
        }
        model.addAttribute("ausenciasDinamicas", ausenciasDinamicas);
        System.out.println("📊 Ausencias dinámicas calculadas: " + ausenciasDinamicas);
        
        // ===== DATOS PARA ATELIER BROCAT =====
        try {
            // Obtener todas las citas previas confirmadas
            List<CitaPrevia> citasConfirmadas = citaPreviaRepository.findByEstado("CONFIRMADA");
            model.addAttribute("citasConfirmadas", citasConfirmadas);
            
            System.out.println("✂️ Citas confirmadas encontradas: " + citasConfirmadas.size());
            for (CitaPrevia cita : citasConfirmadas) {
                System.out.println("   - " + cita.getNombre() + " | " + cita.getFechaPreferida() + " | " + cita.getHoraPreferida() + " | " + cita.getTipoServicio());
            }
        } catch (Exception e) {
            System.err.println("Error obteniendo citas previas: " + e.getMessage());
            model.addAttribute("citasConfirmadas", new ArrayList<CitaPrevia>());
        }
        
        return "admin-panel";
    }
    
    // Método para contar alumnos en un curso específico
    private String getAlumnosEnCurso(String nombreCurso) {
        long count = 0;
        
        // Mapear cursos a grupos (basado en la lógica actual)
        switch (nombreCurso) {
            case "Costura Básica":
                count = AlumnoController.getGrupos().stream()
                    .filter(g -> g.getNombre().equals("Grupo 1"))
                    .mapToLong(g -> g.getParticipantesActuales())
                    .sum();
                break;
            case "Patchwork Avanzado":
            case "Bordado Creativo":
                count = AlumnoController.getGrupos().stream()
                    .filter(g -> g.getNombre().equals("Grupo 2"))
                    .mapToLong(g -> g.getParticipantesActuales())
                    .sum();
                break;
        }
        
        return count + "/10";
    }

    // Añadir alumno
    @GetMapping("/admin/alumnos/nuevo")
    public String nuevoAlumnoForm(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        model.addAttribute("alumno", new Alumno());
        
        // Pasar lista de cursos disponibles
        model.addAttribute("cursosDisponibles", cursos);
        
        // Pasar lista de profesores disponibles
        model.addAttribute("profesoresDisponibles", profesores);
        
        return "alumno-form";
    }

    @PostMapping("/admin/alumnos/nuevo")
    public String guardarAlumno(@ModelAttribute Alumno alumno, HttpSession session, Model model) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        
        alumno.id = (long) (AlumnoController.getAlumnos().size() + 1);
        
        // Intentar agregar el alumno verificando el límite del curso
        boolean agregado = AlumnoController.addAlumno(alumno);
        
        if (!agregado) {
            // El curso está lleno, mostrar error
            model.addAttribute("error", "⚠️ El curso " + alumno.curso + " está completo (máximo 10 personas). Elige otro curso.");
            model.addAttribute("alumno", alumno);
            model.addAttribute("cursosDisponibles", cursos);
            model.addAttribute("profesoresDisponibles", profesores);
            return "alumno-form";
        }
        
        return "redirect:/admin";
    }

    // Borrar alumno
    @GetMapping("/admin/alumnos/borrar/{id}")
    public String borrarAlumno(@PathVariable Long id, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        AlumnoController.removeAlumno(id);
        return "redirect:/admin";
    }

    // Añadir profesor
    @GetMapping("/admin/profesores/nuevo")
    public String nuevoProfesorForm(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        model.addAttribute("profesor", new Profesor());
        model.addAttribute("editMode", false);
        
        // Agregar lista de cursos disponibles
        List<String> cursosDisponibles = new ArrayList<>();
        for (Curso curso : cursos) {
            cursosDisponibles.add(curso.getNombre());
        }
        model.addAttribute("cursosDisponibles", cursosDisponibles);
        
        return "profesor-form";
    }

    @PostMapping("/admin/profesores/nuevo")
    public String guardarProfesor(@ModelAttribute Profesor profesor, 
                                  @RequestParam(value = "cursosSeleccionados", required = false) List<String> cursosSeleccionados,
                                  HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        
        // Convertir la lista de cursos seleccionados a string separado por comas
        if (cursosSeleccionados != null && !cursosSeleccionados.isEmpty()) {
            profesor.setCursos(String.join(", ", cursosSeleccionados));
        } else {
            profesor.setCursos("");
        }
        
        profesor.id = (long) (profesores.size() + 1);
        profesores.add(profesor);
        return "redirect:/admin";
    }

    // Editar profesor
    @GetMapping("/admin/profesores/editar/{id}")
    public String editarProfesorForm(@PathVariable Long id, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        
        // Buscar el profesor por ID
        Profesor profesor = profesores.stream()
                .filter(p -> p.id.equals(id))
                .findFirst()
                .orElse(null);
        
        if (profesor == null) {
            return "redirect:/admin";
        }
        
        model.addAttribute("profesor", profesor);
        model.addAttribute("editMode", true);
        
        // Agregar lista de cursos disponibles
        List<String> cursosDisponibles = new ArrayList<>();
        for (Curso curso : cursos) {
            cursosDisponibles.add(curso.getNombre());
        }
        model.addAttribute("cursosDisponibles", cursosDisponibles);
        
        // Agregar lista de cursos ya asignados al profesor
        List<String> cursosAsignados = new ArrayList<>();
        if (profesor.getCursos() != null && !profesor.getCursos().trim().isEmpty()) {
            String[] cursosArray = profesor.getCursos().split(",");
            for (String curso : cursosArray) {
                String cursoLimpio = curso.trim();
                if (!cursoLimpio.isEmpty()) {
                    cursosAsignados.add(cursoLimpio);
                }
            }
        }
        model.addAttribute("cursosAsignados", cursosAsignados);
        
        System.out.println("DEBUG - Profesor ID: " + profesor.getId() + ", Nombre: " + profesor.getNombre() + ", Cursos: '" + profesor.getCursos() + "'");
        System.out.println("DEBUG - Cursos asignados lista: " + cursosAsignados);
        
        return "profesor-form";
    }

    @PostMapping("/admin/profesores/editar/{id}")
    public String actualizarProfesor(@PathVariable Long id, 
                                   @ModelAttribute Profesor profesor,
                                   @RequestParam(value = "cursosSeleccionados", required = false) List<String> cursosSeleccionados,
                                   HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        
        System.out.println("=== INICIO ACTUALIZACIÓN PROFESOR ===");
        System.out.println("ID recibido: " + id);
        System.out.println("Objeto profesor recibido - ID: " + profesor.getId() + ", Nombre: " + profesor.getNombre() + ", Cursos: " + profesor.getCursos());
        System.out.println("Cursos seleccionados: " + cursosSeleccionados);
        
        // Convertir la lista de cursos seleccionados a string separado por comas
        String nuevoCursos = "";
        if (cursosSeleccionados != null && !cursosSeleccionados.isEmpty()) {
            nuevoCursos = String.join(", ", cursosSeleccionados);
        }
        profesor.setCursos(nuevoCursos);
        System.out.println("Cursos asignados al profesor: '" + nuevoCursos + "'");
        
        // Listar todos los profesores actuales
        System.out.println("Profesores actuales en memoria:");
        for (int i = 0; i < profesores.size(); i++) {
            Profesor p = profesores.get(i);
            System.out.println("  [" + i + "] ID: " + p.getId() + ", Nombre: " + p.getNombre() + ", Cursos: " + p.getCursos());
        }
        
        // Buscar y actualizar el profesor
        boolean encontrado = false;
        for (int i = 0; i < profesores.size(); i++) {
            if (profesores.get(i).getId().equals(id)) {
                profesor.setId(id); // Mantener el ID original
                profesores.set(i, profesor);
                encontrado = true;
                System.out.println("✅ PROFESOR ACTUALIZADO en posición " + i);
                System.out.println("   Nuevos datos - ID: " + profesor.getId() + ", Nombre: " + profesor.getNombre() + ", Cursos: " + profesor.getCursos());
                break;
            }
        }
        
        if (!encontrado) {
            System.out.println("❌ ERROR: No se encontró profesor con ID: " + id);
        }
        
        // Verificar que el cambio se realizó
        System.out.println("Estado final de profesores:");
        for (int i = 0; i < profesores.size(); i++) {
            Profesor p = profesores.get(i);
            System.out.println("  [" + i + "] ID: " + p.getId() + ", Nombre: " + p.getNombre() + ", Cursos: " + p.getCursos());
        }
        System.out.println("=== FIN ACTUALIZACIÓN PROFESOR ===");
        
        return "redirect:/admin";
    }

    // Borrar profesor
    @GetMapping("/admin/profesores/borrar/{id}")
    public String borrarProfesor(@PathVariable Long id, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        profesores.removeIf(p -> p.id.equals(id));
        return "redirect:/admin";
    }

    // Añadir curso
    @GetMapping("/admin/cursos/nuevo")
    public String nuevoCursoForm(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        model.addAttribute("curso", new Curso());
        model.addAttribute("editMode", false);
        return "curso-form";
    }

    @PostMapping("/admin/cursos/nuevo")
    public String guardarCurso(@ModelAttribute Curso curso, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        curso.id = (long) (cursos.size() + 1);
        cursos.add(curso);
        return "redirect:/admin";
    }

    // Editar curso
    @GetMapping("/admin/cursos/editar/{id}")
    public String editarCursoForm(@PathVariable Long id, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        
        // Buscar el curso por ID
        Curso curso = cursos.stream()
                .filter(c -> c.id.equals(id))
                .findFirst()
                .orElse(null);
        
        if (curso == null) {
            return "redirect:/admin";
        }
        
        model.addAttribute("curso", curso);
        model.addAttribute("editMode", true);
        return "curso-form";
    }

    @PostMapping("/admin/cursos/editar/{id}")
    public String actualizarCurso(@PathVariable Long id, @ModelAttribute Curso curso, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        
        // Buscar y actualizar el curso
        for (int i = 0; i < cursos.size(); i++) {
            if (cursos.get(i).id.equals(id)) {
                curso.id = id; // Mantener el ID original
                cursos.set(i, curso);
                break;
            }
        }
        return "redirect:/admin";
    }

    // Borrar curso
    @GetMapping("/admin/cursos/borrar/{id}")
    public String borrarCurso(@PathVariable Long id, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        cursos.removeIf(c -> c.id.equals(id));
        return "redirect:/admin";
    }
    
    // Endpoint para obtener citas por fecha (para el calendario del Atelier)
    @GetMapping("/admin/citas/fecha/{fecha}")
    @ResponseBody
    public List<CitaPrevia> getCitasPorFecha(@PathVariable String fecha, HttpSession session) {
        if (!isAuthenticated(session)) {
            return new ArrayList<>();
        }
        
        try {
            LocalDate fechaBuscada = LocalDate.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return citaPreviaRepository.findByFechaPreferida(fechaBuscada);
        } catch (Exception e) {
            System.err.println("Error al buscar citas por fecha: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    // Endpoint para obtener todas las citas confirmadas del mes (para el calendario)
    @GetMapping("/admin/citas/mes/{anio}/{mes}")
    @ResponseBody
    public List<CitaPrevia> getCitasDelMes(@PathVariable int anio, @PathVariable int mes, HttpSession session) {
        if (!isAuthenticated(session)) {
            return new ArrayList<>();
        }
        
        try {
            LocalDate inicioMes = LocalDate.of(anio, mes, 1);
            LocalDate finMes = inicioMes.withDayOfMonth(inicioMes.lengthOfMonth());
            
            return citaPreviaRepository.findByFechaPreferidaBetween(inicioMes, finMes);
        } catch (Exception e) {
            System.err.println("Error al buscar citas del mes: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    // Método estático para acceder a los cursos desde otros controladores
    public static List<Curso> getCursos() {
        return cursos;
    }
}




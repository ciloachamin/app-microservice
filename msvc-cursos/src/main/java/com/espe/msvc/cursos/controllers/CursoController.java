package com.espe.msvc.cursos.controllers;

import com.espe.msvc.cursos.models.Usuario;
import com.espe.msvc.cursos.models.entity.Curso;
import com.espe.msvc.cursos.services.CursoService;
import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;
@CrossOrigin
@RestController
@RequestMapping("/cursos")  // Agrega un prefijo común para todos los endpoints
public class CursoController {

    @Autowired
    private CursoService service;

    @GetMapping
    public ResponseEntity<Map<String, Object>> listarCursos() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Curso> cursos = service.listar();
            response.put("success", true);
            response.put("data", cursos);
            response.put("message", "Cursos listados correctamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al listar los cursos");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerCursoPorId(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Curso> cursoOptional = service.porId(id);
            if (cursoOptional.isPresent()) {
                response.put("success", true);
                response.put("data", cursoOptional.get());
                response.put("message", "Curso encontrado");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Curso no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener el curso");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> crearCurso(@Valid @RequestBody Curso curso, BindingResult result) {
        if (result.hasErrors()) {
            return validar(result);
        }
        Map<String, Object> response = new HashMap<>();
        try {
            Curso nuevoCurso = service.guardar(curso);
            response.put("success", true);
            response.put("data", nuevoCurso);
            response.put("message", "Curso creado exitosamente");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al crear el curso");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarCurso(@RequestBody Curso curso, @PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Curso> cursoOptional = service.porId(id);
            if (cursoOptional.isPresent()) {
                Curso cursoDB = cursoOptional.get();
                cursoDB.setNombre(curso.getNombre());
                Curso cursoActualizado = service.guardar(cursoDB);
                response.put("success", true);
                response.put("data", cursoActualizado);
                response.put("message", "Curso actualizado correctamente");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Curso no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al actualizar el curso");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarCurso(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Curso> optionalCurso = service.porId(id);
            if (optionalCurso.isPresent()) {
                service.eliminar(id);
                response.put("success", true);
                response.put("message", "Curso eliminado correctamente");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Curso no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al eliminar el curso");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/asignar-usuario/{idcurso}")
    public ResponseEntity<Map<String, Object>> asignarUsuario(@RequestBody Usuario usuario, @PathVariable Long idcurso) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Usuario> o = service.agregarUsuario(usuario, idcurso);
            if (o.isPresent()) {
                response.put("success", true);
                response.put("data", o.get());
                response.put("message", "Usuario asignado correctamente al curso");
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                response.put("success", false);
                response.put("message", "El usuario no pudo ser agregado al curso");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (FeignException e) {
            response.put("success", false);
            response.put("message", "Error al llamar al microservicio de usuarios");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Método de validación de errores
    public static ResponseEntity<Map<String, Object>> validar(BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        response.put("success", false);
        response.put("errors", errores);
        response.put("message", "Error en los datos enviados");
        return ResponseEntity.badRequest().body(response);
    }


}

package com.espe.msvc.cursos;


import com.espe.msvc.cursos.models.Usuario;
import com.espe.msvc.cursos.models.entity.Curso;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CursoServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void cuandoSeLlamaAListarCursosEntoncesElEstadoEs200() throws Exception {
        mockMvc.perform(get("/cursos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void cuandoSeObtieneUnCursoPorIdEntoncesElEstadoEs200() throws Exception {
        Long cursoId = 1L; // Asegúrate de que este ID exista en tu base de datos de prueba
        mockMvc.perform(get("/cursos/{id}", cursoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(cursoId));
    }

    @Test
    public void cuandoSeCreaUnNuevoCursoEntoncesElEstadoEs201() throws Exception {
        Curso curso = new Curso();
        curso.setNombre("Nuevo Curso");

        mockMvc.perform(post("/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(curso)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("Nuevo Curso"));
    }

    @Test
    public void cuandoSeActualizaUnCursoEntoncesElEstadoEs200() throws Exception {
        Long cursoId = 1L; // Asegúrate de que este ID exista en tu base de datos de prueba
        Curso curso = new Curso();
        curso.setNombre("Curso Actualizado");

        mockMvc.perform(put("/cursos/{id}", cursoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(curso)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("Curso Actualizado"));
    }

    @Test
    public void cuandoSeEliminaUnCursoEntoncesElEstadoEs204() throws Exception {
        Long cursoId = 1L; // Asegúrate de que este ID exista en tu base de datos de prueba

        mockMvc.perform(delete("/cursos/{id}", cursoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void cuandoSeAsignaUnUsuarioEntoncesElEstadoEs201() throws Exception {
        Long cursoId = 3L; // Asegúrate de que este ID exista en tu base de datos de prueba
        Usuario usuario = new Usuario();
        usuario.setId(18L); // Asegúrate de que este ID de usuario exista en el microservicio de usuarios

        mockMvc.perform(put("/cursos/asignar-usuario/{idcurso}", cursoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(usuario.getId()));
    }

    // Prueba de eliminar usuario de un curso
    @Test
    public void cuandoSeEliminaUnUsuarioDeUnCursoEntoncesElEstadoEs200() throws Exception {
        Long cursoId = 1L; // Asegúrate de que este ID exista en tu base de datos de prueba
        Usuario usuario = new Usuario();
        usuario.setId(1L); // Asegúrate de que este ID de usuario exista en el microservicio de usuarios

        mockMvc.perform(delete("/cursos/eliminar-usuario/{idcurso}", cursoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(usuario.getId()));
    }
}

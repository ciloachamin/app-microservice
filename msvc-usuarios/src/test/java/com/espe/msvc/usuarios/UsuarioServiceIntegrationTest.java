package com.espe.msvc.usuarios;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void cuandoSeLlamaAUsuariosEntoncesElEstadoEs200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios") // Realiza la solicitud GET a /usuarios
                        .contentType(MediaType.APPLICATION_JSON)) // Define el tipo de contenido
                .andExpect(status().isOk()) // Verifica que el estado es 200
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)); // Verifica que el contenido es JSON
    }
    @Test
    public void cuandoSeLlamaAUsuarioPorIdEntoncesElEstadoEs200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios/4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.email").value("juan@example.com"));
    }

    @Test
    public void cuandoSeCreaUnNuevoUsuarioEntoncesElEstadoEs201() throws Exception {
        String usuarioJson = "{ \"nombre\": \"Pedro\", \"email\": \"pedro@example.com\" }";

        mockMvc.perform(MockMvcRequestBuilders.post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("Pedro"))
                .andExpect(jsonPath("$.email").value("pedro@example.com"));
    }

    @Test
    public void cuandoSeActualizaUnUsuarioEntoncesElEstadoEs200() throws Exception {
        String usuarioActualizadoJson = "{ \"nombre\": \"Juanito\", \"email\": \"juanito@example.com\" }";

        mockMvc.perform(MockMvcRequestBuilders.put("/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioActualizadoJson))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("Juanito"))
                .andExpect(jsonPath("$.email").value("juanito@example.com"));
    }

    @Test
    public void cuandoSeEliminaUnUsuarioEntoncesElEstadoEs204() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void cuandoNoSeEncuentraUsuarioEntoncesElEstadoEs404() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void cuandoSeCreaUsuarioConDatosInvalidosEntoncesElEstadoEs400() throws Exception {
        String usuarioJson = "{ \"nombre\": \"\", \"email\": \"invalid-email\" }";

        mockMvc.perform(MockMvcRequestBuilders.post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioJson))
                .andExpect(status().isBadRequest());
    }









}

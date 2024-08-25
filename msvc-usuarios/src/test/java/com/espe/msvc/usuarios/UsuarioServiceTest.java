package com.espe.msvc.usuarios;
import com.espe.msvc.usuarios.models.entity.Usuario;
import com.espe.msvc.usuarios.repositories.UsuarioRepository;
import com.espe.msvc.usuarios.services.UsuarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListar() {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNombre("Usuario 1");

        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setNombre("Usuario 2");

        List<Usuario> usuarios = Arrays.asList(usuario1, usuario2);
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<Usuario> resultado = usuarioService.listar();
        assertEquals(usuarios, resultado);
    }

    @Test
    public void testPorId_Encontrado() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Usuario 1");

        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = usuarioService.porId(1L);
        assertEquals(Optional.of(usuario), resultado);
    }

    @Test
    public void testPorId_NoEncontrado() {
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Usuario> resultado = usuarioService.porId(1L);
        assertEquals(Optional.empty(), resultado);
    }

    @Test
    public void testGuardar() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Usuario 1");

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = usuarioService.guardar(usuario);
        assertEquals(usuario, resultado);
    }

    @Test
    public void testEliminar() {
        doNothing().when(usuarioRepository).deleteById(anyLong());

        usuarioService.eliminar(1L);
        verify(usuarioRepository, times(1)).deleteById(1L);
    }
}

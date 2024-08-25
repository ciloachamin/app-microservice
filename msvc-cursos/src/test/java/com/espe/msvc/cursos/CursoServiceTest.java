package com.espe.msvc.cursos;
import com.espe.msvc.cursos.models.entity.Curso;
import com.espe.msvc.cursos.repositories.CursoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import com.espe.msvc.cursos.clients.UsuarioClientRest;
import com.espe.msvc.cursos.models.Usuario;
import com.espe.msvc.cursos.models.entity.CursoUsuario;
import com.espe.msvc.cursos.services.CursoServiceImpl;

import java.util.ArrayList;;

public class CursoServiceTest {

    @InjectMocks
    private CursoServiceImpl cursoService;

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private UsuarioClientRest usuarioClientRest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListarCursos() {
        List<Curso> cursos = new ArrayList<>();
        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNombre("Curso de Prueba");
        cursos.add(curso);

        when(cursoRepository.findAll()).thenReturn(cursos);

        List<Curso> result = cursoService.listar();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Curso de Prueba", result.get(0).getNombre());
    }

    @Test
    public void testObtenerCursoPorId() {
        Long cursoId = 1L;
        Curso curso = new Curso();
        curso.setId(cursoId);

        when(cursoRepository.findById(cursoId)).thenReturn(Optional.of(curso));

        Optional<Curso> result = cursoService.porId(cursoId);
        assertTrue(result.isPresent());
        assertEquals(cursoId, result.get().getId());
    }

    @Test
    public void testGuardarCurso() {
        Curso curso = new Curso();
        curso.setNombre("Nuevo Curso");

        when(cursoRepository.save(curso)).thenReturn(curso);

        Curso result = cursoService.guardar(curso);
        assertNotNull(result);
        assertEquals("Nuevo Curso", result.getNombre());
    }

    @Test
    public void testEliminarCurso() {
        Long cursoId = 1L;

        doNothing().when(cursoRepository).deleteById(cursoId);

        cursoService.eliminar(cursoId);

        verify(cursoRepository, times(1)).deleteById(cursoId);
    }

    @Test
    public void testAgregarUsuario() {
        Long cursoId = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        Curso curso = new Curso();
        curso.setId(cursoId);
        curso.setCursoUsuarios(new ArrayList<>());

        when(cursoRepository.findById(cursoId)).thenReturn(Optional.of(curso));
        when(usuarioClientRest.obtenerUsuarioPorId(usuario.getId())).thenReturn(usuario);

        Optional<Usuario> result = cursoService.agregarUsuario(usuario, cursoId);

        assertTrue(result.isPresent());
        assertEquals(usuario.getId(), result.get().getId());
        assertEquals(1, curso.getCursoUsuarios().size());
    }

    @Test
    public void testCrearUsuario() {
        Long cursoId = 2L;
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        Curso curso = new Curso();
        curso.setId(cursoId);
        curso.setCursoUsuarios(new ArrayList<>());

        when(cursoRepository.findById(cursoId)).thenReturn(Optional.of(curso));
        when(usuarioClientRest.crearUsuario(usuario)).thenReturn(usuario);

        Optional<Usuario> result = cursoService.crearUsuario(usuario, cursoId);

        assertTrue(result.isPresent());
        assertEquals(usuario.getId(), result.get().getId());
        assertEquals(1, curso.getCursoUsuarios().size());
    }

    @Test
    public void testEliminarUsuario() {
        Long cursoId = 1L;
        Long usuarioId = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        Curso curso = new Curso();
        curso.setId(cursoId);
        curso.setCursoUsuarios(new ArrayList<>());
        CursoUsuario cursoUsuario = new CursoUsuario();
        cursoUsuario.setUsuarioId(usuarioId);
        // Simula la existencia del curso en el repositorio
        when(cursoRepository.findById(cursoId)).thenReturn(Optional.of(curso));

        // Simula la existencia del usuario en el servicio
        when(usuarioClientRest.obtenerUsuarioPorId(usuarioId)).thenReturn(usuario);

        // Simula la respuesta del repositorio al guardar el curso
        when(cursoRepository.save(any(Curso.class))).thenReturn(curso);

        // Llama al método agregarUsuario para que el usuario esté en el curso
        cursoService.agregarUsuario(usuario, cursoId);
        // Ahora prueba eliminar el usuario
        Optional<Usuario> result = cursoService.eliminarUsuario(usuario, cursoId);
        // Verifica que el resultado no sea vacío
        assertTrue(result.isPresent(), "El resultado debería ser presente.");

        int holaq =curso.getCursoUsuarios().size();
        System.out.println("sss"+ holaq);
        // Verifica que el usuario ha sido eliminado del curso
        assertTrue(curso.getCursoUsuarios().isEmpty(), "El curso debería estar vacío.");

        // Verifica que se haya guardado el curso actualizado
        verify(cursoRepository, times(1)).save(curso);
    }

}

package com.espe.msvc.cursos.services;

import com.espe.msvc.cursos.clients.UsuarioClientRest;
import com.espe.msvc.cursos.models.Usuario;
import com.espe.msvc.cursos.models.entity.Curso;
import com.espe.msvc.cursos.models.entity.CursoUsuario;
import com.espe.msvc.cursos.repositories.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CursoServiceImpl  implements  CursoService{
    @Autowired
    private CursoRepository repository;

    @Autowired
    UsuarioClientRest usuarioClientRest;

    @Override
    @Transactional(readOnly = true)
    public List<Curso> listar(){
        return (List<Curso>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> porId(Long id){
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Curso guardar(Curso curso){
        return repository.save(curso);
    }

    @Override
    @Transactional
    public void eliminar(Long id){
        repository.deleteById(id);
    }

    @Override
    public Optional<Usuario> agregarUsuario(Usuario usuario, Long idCurso){
        Optional<Curso> o = repository.findById(idCurso);
        System.out.println("El Curso existe? " + o.get());
        if(o.isPresent()){
            Usuario usuarioMicro = usuarioClientRest.obtenerUsuarioPorId(usuario.getId());
            System.out.println("El Usuario existe usuarioMicro? " + usuarioMicro.getNombre());
            Curso curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            System.out.println("El Usuario existe cursoUsuario? " + cursoUsuario.getUsuarioId());
            cursoUsuario.setUsuarioId(usuarioMicro.getId());

            curso.addCursoUsuario(cursoUsuario);
            repository.save(curso);
            return Optional.of(usuarioMicro);
        }
        return  Optional.empty();
    }

    @Override
    public Optional<Usuario> crearUsuario(Usuario usuario, Long idCurso){

        //Verificar que exista el curso
        Optional<Curso> o = repository.findById(idCurso);
        if(o.isPresent()){
            //Verifica que exista el usuario
             Usuario usuarioMicro = usuarioClientRest.crearUsuario(usuario);
             //agregamos el usuario al curso
            Curso curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMicro.getId());

            curso.addCursoUsuario(cursoUsuario);
            repository.save(curso);
            return Optional.of(usuarioMicro);
        }
        return Optional.empty();
    }
    @Override
    public Optional<Usuario> eliminarUsuario(Usuario usuario, Long idCurso) {
        // Verifica que exista el curso
        Optional<Curso> o = repository.findById(idCurso);
        if (o.isPresent()) {
            // Verifica que exista el usuario
            Usuario usuarioMicro = usuarioClientRest.obtenerUsuarioPorId(usuario.getId());
            if (usuarioMicro != null) {
                // Obtén el curso y el usuario
                Curso curso = o.get();
                CursoUsuario cursoUsuario = new CursoUsuario();
                cursoUsuario.setUsuarioId(usuarioMicro.getId());

                // Elimina el usuario del curso
                curso.removeCursoUsuarios(cursoUsuario);
                repository.save(curso);
                return Optional.of(usuarioMicro);
            }
        }
        return Optional.empty();
    }

}

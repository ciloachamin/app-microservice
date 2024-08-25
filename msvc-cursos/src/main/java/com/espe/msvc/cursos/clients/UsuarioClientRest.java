package com.espe.msvc.cursos.clients;

import com.espe.msvc.cursos.models.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "msvc-usuarios", url = "localhost:8001")
public interface UsuarioClientRest {

    @GetMapping("/usuarios/{id}")
    Usuario obtenerUsuarioPorId(@PathVariable Long id);

    @PostMapping("/usuarios")
    Usuario crearUsuario(@RequestBody Usuario usuario);
}

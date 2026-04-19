package com.CGP.demo.Service;

import com.CGP.demo.Modelo.Usuario;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsuarioService {

    private final Map<Long, Usuario> usuarios = new HashMap<>();
    private Long contadorId = 1L;

    private void asignarId(Usuario usuario) {
        try {
            Field field = Usuario.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(usuario, contadorId++);
        } catch (Exception e) {
            throw new RuntimeException("Error asignando ID al usuario", e);
        }
    }
//listar
    public List<Usuario> listar() {
        return new ArrayList<>(usuarios.values());
    }
//buscar porid
    public Usuario buscarPorId(Long id) {
        return usuarios.get(id);
    }
//buscar por email
    public Usuario buscarPorEmail(String email) {
        if (email == null) {
            return null;
        }
        for (Usuario usuario : usuarios.values()) {
            if (email.equalsIgnoreCase(usuario.getEmail())) {
                return usuario;
            }
        }
        return null;
    }
//crear
    public Usuario crear(Usuario usuario) {
        validarUsuario(usuario);
        if (emailDuplicado(usuario.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con ese email");
        }
        asignarId(usuario);
        usuarios.put(usuario.getId(), usuario);
        return usuario;
    }
//update
    public Usuario actualizar(Long id, Usuario actualizacion) {
        if (!usuarios.containsKey(id)) {
            return null;
        }
        validarUsuario(actualizacion);
        Usuario existente = usuarios.get(id);
        String nuevoEmail = actualizacion.getEmail().trim();
        if (!existente.getEmail().equalsIgnoreCase(nuevoEmail) && emailDuplicado(nuevoEmail)) {
            throw new RuntimeException("Ya existe un usuario con ese email");
        }
        existente.setNombre(actualizacion.getNombre().trim());
        existente.setEmail(nuevoEmail);
        existente.setPassword(actualizacion.getPassword());
        return existente;
    }
//delete
    public boolean eliminar(Long id) {
        return usuarios.remove(id) != null;
    }
//validaciones
    private void validarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new RuntimeException("Usuario inválido");
        }
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre no puede estar vacío");
        }
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new RuntimeException("El email no puede estar vacío");
        }
        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            throw new RuntimeException("La contraseña no puede estar vacía");
        }
    }
    
    private boolean emailDuplicado(String email) {
        if (email == null) {
            return false;
        }
        for (Usuario usuario : usuarios.values()) {
            if (usuario.getEmail() != null && usuario.getEmail().equalsIgnoreCase(email.trim())) {
                return true;
            }
        }
        return false;
    }
}

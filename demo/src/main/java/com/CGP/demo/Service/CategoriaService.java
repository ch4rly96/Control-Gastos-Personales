package com.CGP.demo.Service;

import com.CGP.demo.Modelo.Categoria;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoriaService {

    private final Map<Long, Categoria> categorias = new HashMap<>();
    private Long contadorId = 1L;

    private void asignarId(Categoria categoria) {
        try {
            Field field = Categoria.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(categoria, contadorId++);
        } catch (Exception e) {
            throw new RuntimeException("Error asignando ID a la categoría", e);
        }
    }

    //istar
    public List<Categoria> listar() {
        return new ArrayList<>(categorias.values());
    }

    //buscar por id
    public Categoria buscarPorId(Long id) {
        return categorias.get(id);
    }

    //crear
    public Categoria crear(Categoria categoria) {
        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre de la categoría no puede estar vacío");
        }

        String nombreLimpio = categoria.getNombre().trim();

        if (nombreDuplicado(nombreLimpio)) {
            throw new RuntimeException("Ya existe una categoría con ese nombre");
        }

        categoria.setNombre(nombreLimpio);
        asignarId(categoria);
        categorias.put(categoria.getId(), categoria);

        return categoria;
    }

    //update
    public Categoria actualizar(Long id, Categoria nueva) {
        if (!categorias.containsKey(id)) {
            return null;
        }

        if (nueva.getNombre() == null || nueva.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre de la categoría no puede estar vacío");
        }

        Categoria existente = categorias.get(id);
        String nuevoNombre = nueva.getNombre().trim();

        if (!existente.getNombre().equalsIgnoreCase(nuevoNombre) && nombreDuplicado(nuevoNombre)) {
            throw new RuntimeException("Ya existe una categoría con ese nombre");
        }

        existente.setNombre(nuevoNombre);

        //no tocar transacciones 
        return existente;
    }

    // eliminar
    public boolean eliminar(Long id) {
        return categorias.remove(id) != null;
    }

    //validacn de duplicados
    private boolean nombreDuplicado(String nombre) {
        for (Categoria c : categorias.values()) {
            if (c.getNombre() != null && c.getNombre().equalsIgnoreCase(nombre.trim())) {
                return true;
            }
        }
        return false;
    }
}
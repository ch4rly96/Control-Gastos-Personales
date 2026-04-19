package com.CGP.demo.Service;

import com.CGP.demo.Modelo.Transaccion;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class TransaccionService {

    private final Map<Long, Transaccion> transacciones = new HashMap<>();
    private Long contadorId = 1L;

    private void asignarId(Transaccion transaccion) {
        try {
            Field field = Transaccion.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(transaccion, contadorId++);
        } catch (Exception e) {
            throw new RuntimeException("Error asignando ID a la transacción", e);
        }
    }

    public List<Transaccion> listar() {
        return new ArrayList<>(transacciones.values());
    }

    public Transaccion buscarPorId(Long id) {
        return transacciones.get(id);
    }

    public Transaccion crear(Transaccion transaccion) {
        validarTransaccion(transaccion);
        asignarId(transaccion);
        transacciones.put(transaccion.getId(), transaccion);
        return transaccion;
    }

    public Transaccion actualizar(Long id, Transaccion actualizacion) {
        if (!transacciones.containsKey(id)) {
            return null;
        }
        validarTransaccion(actualizacion);

        Transaccion existente = transacciones.get(id);
        existente.setDescripcion(actualizacion.getDescripcion());
        existente.setMonto(actualizacion.getMonto());
        existente.setFecha(actualizacion.getFecha());
        existente.setCategoria(actualizacion.getCategoria());
        existente.setUsuario(actualizacion.getUsuario());

        try {
            Field fieldTipo = Transaccion.class.getDeclaredField("tipo");
            fieldTipo.setAccessible(true);
            fieldTipo.set(existente, actualizacion.getTipo());
        } catch (Exception e) {
            throw new RuntimeException("Error actualizando tipo de transacción", e);
        }

        return existente;
    }

    public boolean eliminar(Long id) {
        return transacciones.remove(id) != null;
    }

    public List<Transaccion> obtenerPorUsuario(Long usuarioId) {
        List<Transaccion> resultado = new ArrayList<>();
        for (Transaccion t : transacciones.values()) {
            if (t.getUsuario() != null &&
                t.getUsuario().getId() != null &&
                t.getUsuario().getId().equals(usuarioId)) {
                resultado.add(t);
            }
        }
        return resultado;
    }

    public List<Transaccion> obtenerPorCategoria(Long categoriaId) {
        List<Transaccion> resultado = new ArrayList<>();
        for (Transaccion t : transacciones.values()) {
            if (t.getCategoria() != null &&
                t.getCategoria().getId() != null &&
                t.getCategoria().getId().equals(categoriaId)) {
                resultado.add(t);
            }
        }
        return resultado;
    }

    public List<Transaccion> obtenerPorTipo(String tipo) {
        List<Transaccion> resultado = new ArrayList<>();
        if (tipo == null || tipo.trim().isEmpty()) {
            return resultado;
        }
        String valor = tipo.trim().toUpperCase();
        for (Transaccion t : transacciones.values()) {
            if (t.getTipo() != null && t.getTipo().name().equals(valor)) {
                resultado.add(t);
            }
        }
        return resultado;
    }

    private void validarTransaccion(Transaccion transaccion) {
        if (transaccion == null) {
            throw new RuntimeException("Transacción inválida");
        }
        if (transaccion.getDescripcion() == null || transaccion.getDescripcion().trim().isEmpty()) {
            throw new RuntimeException("La descripción no puede estar vacía");
        }
        if (transaccion.getMonto() == null || transaccion.getMonto() <= 0) {
            throw new RuntimeException("El monto debe ser mayor que cero");
        }
        if (transaccion.getFecha() == null) {
            throw new RuntimeException("La fecha es obligatoria");
        }
        if (transaccion.getFecha().isAfter(LocalDate.now())) {
            throw new RuntimeException("La fecha no puede ser futura");
        }
        if (transaccion.getTipo() == null) {
            throw new RuntimeException("El tipo de transacción es obligatorio");
        }
    }
}

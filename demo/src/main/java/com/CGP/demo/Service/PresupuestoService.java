package com.CGP.demo.Service;

import com.CGP.demo.Modelo.Presupuesto;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class PresupuestoService {

    private final Map<Long, Presupuesto> presupuestos = new HashMap<>();
    private Long contadorId = 1L;

    // asignar id con reflexion(asignar id sin stter)
    private void asignarId(Presupuesto presupuesto) {
        try {
            Field field = Presupuesto.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(presupuesto, contadorId++);
        } catch (Exception e) {
            throw new RuntimeException("Error asignando ID al presupuesto", e);
        }
    }

    //Listar
    public List<Presupuesto> listar() {
        return new ArrayList<>(presupuestos.values());
    }

    //buscar por id
    public Presupuesto buscarPorId(Long id) {
        return presupuestos.get(id);
    }

    //crear
    public Presupuesto crear(Presupuesto presupuesto) {
        validarPresupuesto(presupuesto);

        if (existePresupuestoDuplicado(presupuesto)) {
            throw new RuntimeException("Ya existe un presupuesto para ese mes y año");
        }

        asignarId(presupuesto);
        presupuestos.put(presupuesto.getId(), presupuesto);
        return presupuesto;
    }

    //update
    public Presupuesto actualizar(Long id, Presupuesto actualizacion) {
        if (!presupuestos.containsKey(id)) {
            throw new RuntimeException("Presupuesto no encontrado");
        }

        validarPresupuesto(actualizacion);

        Presupuesto existente = presupuestos.get(id);

        // Validar duplicado 
        if (!(existente.getMes() == actualizacion.getMes() &&
              existente.getAnio() == actualizacion.getAnio() &&
              existente.getUsuario() != null &&
              actualizacion.getUsuario() != null &&
              existente.getUsuario().getId().equals(actualizacion.getUsuario().getId()))
            && existePresupuestoDuplicado(actualizacion)) {

            throw new RuntimeException("Ya existe un presupuesto para ese mes y año");
        }

        existente.setMontoLimite(actualizacion.getMontoLimite());
        existente.setMes(actualizacion.getMes());
        existente.setAnio(actualizacion.getAnio());
        existente.setUsuario(actualizacion.getUsuario());

        return existente;
    }

    //delete
    public boolean eliminar(Long id) {
        return presupuestos.remove(id) != null;
    }

    //Obtener por usuario
    public List<Presupuesto> obtenerPorUsuario(Long usuarioId) {
        List<Presupuesto> resultado = new ArrayList<>();

        for (Presupuesto p : presupuestos.values()) {
            if (p.getUsuario() != null &&
                p.getUsuario().getId() != null &&
                p.getUsuario().getId().equals(usuarioId)) {

                resultado.add(p);
            }
        }

        return resultado;
    }

    //Validaciones principales
    private void validarPresupuesto(Presupuesto presupuesto) {
        if (presupuesto == null) {
            throw new RuntimeException("Presupuesto inválido");
        }

        if (presupuesto.getMontoLimite() <= 0) {
            throw new RuntimeException("El monto límite debe ser mayor que cero");
        }

        if (presupuesto.getMes() < 1 || presupuesto.getMes() > 12) {
            throw new RuntimeException("El mes debe estar entre 1 y 12");
        }

        if (presupuesto.getAnio() <= 0) {
            throw new RuntimeException("El año debe ser mayor que cero");
        }

        if (presupuesto.getUsuario() == null || presupuesto.getUsuario().getId() == null) {
            throw new RuntimeException("El usuario es obligatorio");
        }
    }

    //Validar duplicados por usuario, mes y año
    private boolean existePresupuestoDuplicado(Presupuesto nuevo) {
        for (Presupuesto p : presupuestos.values()) {
            if (p.getUsuario() != null && nuevo.getUsuario() != null &&
                p.getUsuario().getId() != null && nuevo.getUsuario().getId() != null &&
                p.getUsuario().getId().equals(nuevo.getUsuario().getId()) &&
                p.getMes() == nuevo.getMes() &&
                p.getAnio() == nuevo.getAnio()) {

                return true;
            }
        }
        return false;
    }
}
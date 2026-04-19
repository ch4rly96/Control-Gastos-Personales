package com.CGP.demo.Service;
import com.CGP.demo.Modelo.Alerta;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlertaService {

    private Map<Long, Alerta> alertas = new HashMap<>();
    private Long contadorId = 1L;

    //Listar todos
    public List<Alerta> listar() {
        return new ArrayList<>(alertas.values());
    }

    //Buscar por id
    public Alerta buscarPorId(Long id) {
        return alertas.get(id);
    }

    //Crear una alerta
    public Alerta crear(Alerta alerta) {
        if (alerta.getMensaje() == null || alerta.getMensaje().isEmpty()) {
            throw new RuntimeException("El mensaje no puede estar vacío");
        }
        Long id = contadorId++;
        alertas.put(id, alerta);
        return alerta;
    }

    //actualizar
    public Alerta actualizar(Long id, Alerta nueva) {
        if (!alertas.containsKey(id)) {
            return null;
        }
        Alerta a = alertas.get(id);
        a.setMensaje(nueva.getMensaje());
        a.setActiva(nueva.isActiva());
        a.setUsuario(nueva.getUsuario());
        return a;
    }

    //eliminar
    public boolean eliminar(Long id) {
        return alertas.remove(id) != null;
    }

    //filtrar por usuario
    public List<Alerta> obtenerPorUsuario(Long usuarioId) {
        List<Alerta> resultado = new ArrayList<>();

        for (Alerta a : alertas.values()) {
            if (a.getUsuario() != null &&
                a.getUsuario().getId() != null &&
                a.getUsuario().getId().equals(usuarioId)) {

                resultado.add(a);
            }
        }

        return resultado;
    }
}
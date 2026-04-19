package com.CGP.demo.Controller;

import com.CGP.demo.Modelo.Transaccion;
import com.CGP.demo.Service.TransaccionService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transacciones")
public class TransaccionController {

    private final TransaccionService transaccionService;

    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    // LISTAR TODAS
    @GetMapping
    public ResponseEntity<List<Transaccion>> listar() {
        return ResponseEntity.ok(transaccionService.listar());
    }

    // BUSCAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Transaccion transaccion = transaccionService.buscarPorId(id);

        if (transaccion == null) {
            return ResponseEntity.status(404).body("Transacción no encontrada");
        }

        return ResponseEntity.ok(transaccion);
    }

    // CREAR
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Transaccion transaccion) {
        try {
            Transaccion nueva = transaccionService.crear(transaccion);
            return ResponseEntity.status(201).body(nueva);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ACTUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Transaccion transaccion) {
        try {
            Transaccion actualizada = transaccionService.actualizar(id, transaccion);

            if (actualizada == null) {
                return ResponseEntity.status(404).body("No existe transacción con id: " + id);
            }

            return ResponseEntity.ok(actualizada);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        boolean eliminado = transaccionService.eliminar(id);

        if (!eliminado) {
            return ResponseEntity.status(404).body("No existe transacción con id: " + id);
        }

        return ResponseEntity.ok("Transacción eliminada correctamente");
    }

    // OBTENER POR USUARIO
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Transaccion>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(transaccionService.obtenerPorUsuario(usuarioId));
    }

    // OBTENER POR CATEGORIA
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<Transaccion>> obtenerPorCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(transaccionService.obtenerPorCategoria(categoriaId));
    }

    // OBTENER POR TIPO (INGRESO o GASTO)
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Transaccion>> obtenerPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(transaccionService.obtenerPorTipo(tipo));
    }
}
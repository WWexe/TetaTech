package projeto.leite.ProjetoLeite.controller;

import projeto.leite.ProjetoLeite.domain.collector;
import projeto.leite.ProjetoLeite.service.CollectorService;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/collectors")
@CrossOrigin(origins = "*")
public class CollectorController {

    private final CollectorService service;

    public CollectorController(CollectorService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<collector>> getAllCollector() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<collector> getCollectorById(@PathVariable UUID id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<collector> create(@Valid @RequestBody collector collector) {
        return ResponseEntity.ok(service.save(collector));
    }

    @PutMapping("/{id}")
    public ResponseEntity<collector> update(@PathVariable UUID id, @Valid @RequestBody collector collector) {
        return ResponseEntity.ok(service.update(id, collector));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}


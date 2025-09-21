package projeto.leite.ProjetoLeite.controller;

import projeto.leite.ProjetoLeite.domain.producer;
import projeto.leite.ProjetoLeite.service.ProducerService;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/producers")
@CrossOrigin(origins = "*")
public class ProducerController {

    private final ProducerService service;

    public ProducerController(ProducerService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<producer>> getAllProducers() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<producer> getProducerById(@PathVariable UUID id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<producer> create(@Valid @RequestBody producer producer) {
        return ResponseEntity.ok(service.save(producer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<producer> update(@PathVariable UUID id, @Valid @RequestBody producer producer) {
        return ResponseEntity.ok(service.update(id, producer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

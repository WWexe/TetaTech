package projeto.leite.ProjetoLeite.service;

import projeto.leite.ProjetoLeite.domain.collector;
import projeto.leite.ProjetoLeite.domain.producer;
import projeto.leite.ProjetoLeite.repository.CollectorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projeto.leite.ProjetoLeite.repository.ProducerRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CollectorService {

    private final CollectorRepository repository;

    public CollectorService(CollectorRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public collector save(collector collector) {
        //Validação do CPF
        if (repository.existsByCpf(collector.getCpf())) {
            throw new IllegalArgumentException("Cpf já está cadastrado");
        }
        return repository.save(collector);
    }
    public List<collector> findAll() {
        return repository.findAll();
    }

    public Optional<collector> findById(UUID id) {
        return repository.findById(id);
    }

    @Transactional
    public collector update(UUID id, collector updatedCollector) {
        return repository.findById(id).map(existing -> {
            existing.setNome(updatedCollector.getNome());
            existing.setSobrenome(updatedCollector.getSobrenome());
            existing.setCpf(updatedCollector.getCpf());
            existing.setEmail(updatedCollector.getEmail());
            existing.setStatus(updatedCollector.getStatus());
            existing.setUser(updatedCollector.getUser());
            return repository.save(existing);
        }).orElseThrow(() -> new IllegalArgumentException("Collector não encontrado"));
    }

    @Transactional
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Collector não encontrado");
        }
        repository.deleteById(id);
    }
}

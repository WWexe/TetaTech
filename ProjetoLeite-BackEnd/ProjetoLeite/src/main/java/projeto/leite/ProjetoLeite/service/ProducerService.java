package projeto.leite.ProjetoLeite.service;

import projeto.leite.ProjetoLeite.domain.collector;
import projeto.leite.ProjetoLeite.domain.producer;
import projeto.leite.ProjetoLeite.repository.ProducerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProducerService {

    private final ProducerRepository repository;

    public ProducerService(ProducerRepository repository, ProducerRepository producerRepository) {
        this.repository = repository;
    }

    @Transactional
    public producer save(producer producer) {
        //Validação do Cadpro
        if (repository.existsByCadpro(producer.getCadpro())) {
            throw new IllegalArgumentException("Cadpro já está cadastrado");
        }
        //Validação de Cpf
        if (repository.existsByCpf(producer.getCpf())) {
            throw new IllegalArgumentException("Cpf já está cadastrado");
        }
        return repository.save(producer);
    }

    public List<producer> findAll() {
        return repository.findAll();
    }

    public Optional<producer> findById(UUID id) {
        return repository.findById(id);
    }

    @Transactional
    public producer update(UUID id, producer updatedProducer) {
        return repository.findById(id).map(existing -> {
            existing.setNome(updatedProducer.getNome());
            existing.setSobrenome(updatedProducer.getSobrenome());
            existing.setCpf(updatedProducer.getCpf());
            existing.setCadpro(updatedProducer.getCadpro());
            existing.setPropriedade(updatedProducer.getPropriedade());
            existing.setEmail(updatedProducer.getEmail());
            existing.setStatus(updatedProducer.getStatus());
            existing.setUser(updatedProducer.getUser());
            return repository.save(existing);
        }).orElseThrow(() -> new IllegalArgumentException("Producer não encontrado"));
    }

    @Transactional
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Producer não encontrado");
        }
        repository.deleteById(id);
    }
}

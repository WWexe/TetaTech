package projeto.leite.ProjetoLeite.repository;

import projeto.leite.ProjetoLeite.domain.producer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProducerRepository extends JpaRepository<producer, UUID> {
    boolean existsByCadpro(String cadpro);
    boolean existsByCpf(String cpf);
}
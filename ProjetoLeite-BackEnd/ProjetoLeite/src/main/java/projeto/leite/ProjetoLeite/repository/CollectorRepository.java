package projeto.leite.ProjetoLeite.repository;

import projeto.leite.ProjetoLeite.domain.collector;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CollectorRepository extends JpaRepository<collector, UUID> {
    boolean existsByCpf(String Cpf);
}

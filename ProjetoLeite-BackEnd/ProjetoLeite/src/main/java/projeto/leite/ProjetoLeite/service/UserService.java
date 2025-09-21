package projeto.leite.ProjetoLeite.service;

import projeto.leite.ProjetoLeite.domain.AppUser;
import projeto.leite.ProjetoLeite.domain.UserRole;
import projeto.leite.ProjetoLeite.domain.UserStatus;
import projeto.leite.ProjetoLeite.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public AppUser save(AppUser user) {
        if (repository.existsByCpf(user.getCpf())) {
            throw new IllegalArgumentException("Cpf já está cadastrado");
        }
        return repository.save(user);
    }

    public List<AppUser> findAll() {
        return repository.findAll();
    }

    public List<AppUser> findAllCollectors() {
        return repository.findByRole(UserRole.COLLECTOR);
    }

    public List<AppUser> findAllProducers() {
        return repository.findByRole(UserRole.PRODUCER);
    }

    public Optional<AppUser> findById(UUID id) {
        return repository.findById(id);
    }

    @Transactional
    public void delete(UUID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new RuntimeException("Usuário não encontrado para o ID: " + id);
        }
    }

    public UserRoleCountsDTO getUserRoleCounts() {
        long adminCount = repository.countByRole(UserRole.ADMIN);
        long collectorCount = repository.countByRole(UserRole.COLLECTOR);
        long producerCount = repository.countByRole(UserRole.PRODUCER);

        return new UserRoleCountsDTO(adminCount, collectorCount, producerCount);
    }

    // Atualizar o status de um usuário
    @Transactional
    public Optional<AppUser> updateStatus(UUID id, UserStatus newStatus) {
        return repository.findById(id)
                .map(user -> {
                    user.setStatus(newStatus);
                    return repository.save(user);
                });
    }

    // Atualizar o papel (role) de um usuário
    @Transactional
    public Optional<AppUser> updateRole(UUID id, UserRole newRole) {
        return repository.findById(id)
                .map(user -> {
                    user.setRole(newRole);
                    return repository.save(user);
                });
    }

    public record UserRoleCountsDTO(long adminCount, long collectorCount, long producerCount) {}
}
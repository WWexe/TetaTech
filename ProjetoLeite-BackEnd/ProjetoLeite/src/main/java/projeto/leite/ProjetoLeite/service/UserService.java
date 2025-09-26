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
    public AppUser create(AppUser newUser) {
        if (repository.existsByCpf(newUser.getCpf())) {
            throw new IllegalArgumentException("CPF já está cadastrado");
        }
        if (repository.existsByEmail(newUser.getEmail())) {
            throw new IllegalArgumentException("Email já está cadastrado");
        }
        // Lembre-se de reativar a criptografia da senha aqui quando voltar a usar o PasswordEncoder
        // newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return repository.save(newUser);
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

    @Transactional
    public Optional<AppUser> update(UUID id, AppUser userDetails) {
        // 1. Busca o usuário existente no banco
        return repository.findById(id)
                .map(existingUser -> {
                    // 2. Atualiza os campos com os novos valores (exceto a senha)
                    existingUser.setNome(userDetails.getNome());
                    existingUser.setSobrenome(userDetails.getSobrenome());
                    existingUser.setEmail(userDetails.getEmail());
                    existingUser.setCpf(userDetails.getCpf());
                    existingUser.setRole(userDetails.getRole());
                    existingUser.setStatus(userDetails.getStatus());
                    existingUser.setPassword(userDetails.getPassword());

                    // 3. LÓGICA INTELIGENTE PARA A SENHA:
                    // Só atualiza a senha se uma nova (não nula e não vazia) for fornecida.
                    if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
                        // Se/quando o PasswordEncoder for reativado, a criptografia entrará aqui.
                        // Por agora, salva em texto puro (APENAS PARA TESTE).
                        existingUser.setPassword(userDetails.getPassword());
                    }

                    // 4. Salva o usuário atualizado
                    return repository.save(existingUser);
                });
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
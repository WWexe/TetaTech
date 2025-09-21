package projeto.leite.ProjetoLeite.repository;

import projeto.leite.ProjetoLeite.domain.AppUser;
import projeto.leite.ProjetoLeite.domain.UserRole;
import projeto.leite.ProjetoLeite.domain.UserStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<AppUser, UUID> {
    boolean existsByCpf(String Cpf);

    UserDetails findByEmail(String email);

    // Contar usuários por role
    long countByRole(UserRole role);

    // Contar usuários ativos (status = true)
    long countByStatus(UserStatus status);

    List<AppUser> findByStatus(UserStatus status);

    List<AppUser> findByRole(UserRole role);
}

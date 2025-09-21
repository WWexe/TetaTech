package projeto.leite.ProjetoLeite.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Table(name = "users")
@Entity(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class AppUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String sobrenome;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    // Construtor para o processo de registro
    public AppUser(String nome, String sobrenome, String cpf, String email, String password, UserRole role) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.cpf = cpf;
        this.email = email;
        this.password = password; // Senha já deve vir criptografada
        this.role = role;
        this.status = UserStatus.ACTIVE; // Define o usuário como ativo por padrão no registro
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Mapeia UserRole para GrantedAuthority de forma mais granular
        // Exemplo: se UserRole.ADMIN -> ROLE_ADMIN, UserRole.PROFESSOR -> ROLE_PROFESSOR
        if (this.role == UserRole.ADMIN) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        } else if (this.role == UserRole.COLLECTOR) {
            return List.of(new SimpleGrantedAuthority("ROLE_COLLECTOR"), new SimpleGrantedAuthority("ROLE_USER"));
        } else if (this.role == UserRole.PRODUCER) {
            return List.of(new SimpleGrantedAuthority("ROLE_PRODUCER"), new SimpleGrantedAuthority("ROLE_USER"));
        } else { // COORDENATION, SECRETARY e outros futuros papéis
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }
    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Conta nunca expira
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.status == UserStatus.ACTIVE; // ✅ ativo só se status == ACTIVE
    }
}
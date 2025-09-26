package projeto.leite.ProjetoLeite.controller;

import projeto.leite.ProjetoLeite.domain.AppUser;
import projeto.leite.ProjetoLeite.domain.UserRole;
import projeto.leite.ProjetoLeite.domain.UserStatus;
import projeto.leite.ProjetoLeite.service.UserService;
import projeto.leite.ProjetoLeite.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody; // Adicione esta importação
import org.springframework.http.ResponseEntity; // Garanta que esta importação existe
import java.net.URI; // Adicione esta importação
import org.springframework.web.servlet.support.ServletUriComponentsBuilder; // Adicione esta importação
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public List<AppUser> getAllUsers() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUser> getUserById(@PathVariable UUID id) {
        Optional<AppUser> user = service.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AppUser> createUser(@RequestBody AppUser newUser) {
        // Chama o serviço para salvar o novo usuário
        // (O service já deve estar cuidando da criptografia da senha)
        AppUser savedUser = service.create(newUser);

        // Esta é uma boa prática REST: retornar o status 201 Created
        // e a URL do novo recurso criado no cabeçalho 'Location'.
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest() // Pega a URL atual (http://.../users)
                .path("/{id}")        // Adiciona /id
                .buildAndExpand(savedUser.getId()) // Substitui {id} pelo ID do usuário salvo
                .toUri();

        return ResponseEntity.created(location).body(savedUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppUser> updateUser(@PathVariable UUID id, @RequestBody AppUser userDetails) {
        // Apenas delega a chamada para o método de atualização correto no serviço
        return service.update(id, userDetails)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        if (service.findById(id).isPresent()) {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/counts/by-role")
    public ResponseEntity<UserService.UserRoleCountsDTO> getUserRoleCounts() {
        UserService.UserRoleCountsDTO counts = service.getUserRoleCounts();
        return ResponseEntity.ok(counts);
    }

    // Atualizar o status de um usuário (ativar/desativar)
    // Usando PATCH para atualização parcial de um recurso
    @PatchMapping("/{id}/status")
    public ResponseEntity<AppUser> updateUserStatus(@PathVariable UUID id, @RequestParam UserStatus active) {
        return service.updateStatus(id, active)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Atualizar o papel (role) de um usuário
    // Usando PATCH para atualização parcial de um recurso
    @PatchMapping("/{id}/role")
    public ResponseEntity<AppUser> updateUserRole(@PathVariable UUID id, @RequestParam UserRole role) {
        return service.updateRole(id, role)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}


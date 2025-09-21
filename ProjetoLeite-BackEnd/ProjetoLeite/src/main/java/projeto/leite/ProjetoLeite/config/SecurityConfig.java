package projeto.leite.ProjetoLeite.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity // Habilita a segurança web do Spring
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Habilita a configuração de CORS definida no bean 'corsConfigurationSource'
                .cors(Customizer.withDefaults())

                // 2. Desabilita o CSRF, pois não estamos usando sessões/cookies para autenticação
                .csrf(csrf -> csrf.disable())

                // 3. Define as regras de autorização para as requisições HTTP
                .authorizeHttpRequests(authorize -> authorize
                        // Permite que o navegador faça a requisição de verificação (preflight) OPTIONS
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Permite o cadastro de novos usuários sem autenticação
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()

                        // Exige autenticação para qualquer outra requisição
                        .anyRequest().authenticated()
                )
                /*.authorizeHttpRequests(authorize -> authorize
                        // Permite que o navegador faça a requisição de verificação (preflight) OPTIONS
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Permite o cadastro de novos usuários sem autenticação
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()

                        // Exige autenticação para qualquer outra requisição
                        .anyRequest().authenticated()
                )*/
                // 4. Habilita a autenticação básica (HTTP Basic Auth)
                .httpBasic(Customizer.withDefaults());


        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Define de qual origem (URL do front-end) as requisições são permitidas
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));

        // Define quais métodos HTTP são permitidos (GET, POST, etc.)
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        // Define quais cabeçalhos são permitidos
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica esta configuração a todas as rotas da sua aplicação
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
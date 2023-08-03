package tech.dinhphu28.blog;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tech.dinhphu28.blog.entity.Role;
import tech.dinhphu28.blog.entity.User;
import tech.dinhphu28.blog.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        var admin = User.builder()
                .firstName("Admin")
                .lastName("Admin")
                .email("admin@mail.com")
                .password(passwordEncoder.encode("password"))
                .isEnabled(true)
                .role(Role.ADMIN)
                .build();

        userRepository.save(admin);

        var manager = User.builder()
                .firstName("Manager")
                .lastName("Manager")
                .email("manager@mail.com")
                .password(passwordEncoder.encode("password"))
                .isEnabled(true)
                .role(Role.MANAGER)
                .build();

        userRepository.save(manager);
    }
}

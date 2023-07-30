package tech.dinhphu28.blog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.dinhphu28.blog.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void activateUser(String email) {
        var user = userRepository.findByEmail(email).orElseThrow();
        user.setEnabled(true);
        userRepository.save(user);
    }
}

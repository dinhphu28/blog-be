package tech.dinhphu28.blog.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tech.dinhphu28.blog.entity.Role;
import tech.dinhphu28.blog.entity.Token;
import tech.dinhphu28.blog.entity.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TokenRepositoryTest {

    @Autowired
    private TokenRepository underTest;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

//    @BeforeEach
//    void setup() {
//
//    }

    @Test
    void canFindAllValidTokenByUser() {
        // given
        User user = User.builder()
                .firstName("Jack")
                .lastName("Ma")
                .email("jackma@mail.com")
                .password("password")
                .isEnabled(true)
                .role(Role.USER)
                .build();
        User savedUser = userRepository.save(user);

        Token token = Token.builder()
                .token("alksjdlfkj")
                .revoked(false)
                .expired(false)
                .user(savedUser)
                .build();
        underTest.save(token);

        // when
        List<Token> tokens = underTest.findAllValidTokenByUser(savedUser.getId());

        // then
        assertThat(tokens.size()).isGreaterThanOrEqualTo(1);
        assertThat(tokens.get(0).getToken()).isEqualTo(token.getToken());
    }

    @Test
    void canFindByToken() {
        // given
        User user = User.builder()
                .firstName("Jack")
                .lastName("Ma")
                .email("jackma@mail.com")
                .password("password")
                .isEnabled(true)
                .role(Role.USER)
                .build();
        User savedUser = userRepository.save(user);

        Token token = Token.builder()
                .token("alksjdlfkj")
                .revoked(false)
                .expired(false)
                .user(savedUser)
                .build();
        underTest.save(token);

        // when
        Optional<Token> foundTokenOpt = underTest.findByToken(token.getToken());

        // then
        assertThat(foundTokenOpt.isPresent()).isTrue();
        assertThat(foundTokenOpt.get().getToken()).isEqualTo(token.getToken());
    }
}
package tech.dinhphu28.blog.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tech.dinhphu28.blog.entity.Role;
import tech.dinhphu28.blog.entity.User;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void canFindUserByEmail() {
        // given
        User user = User.builder()
                .firstName("Jack")
                .lastName("Ma")
                .email("jackma@mail.com")
                .password("password")
                .isEnabled(true)
                .role(Role.USER)
                .build();
        underTest.save(user);

        // when
        Optional<User> userFoundOtp = underTest.findByEmail(user.getEmail());

        // then
        assertThat(userFoundOtp.isPresent()).isTrue();
        assertThat(userFoundOtp.get().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void findWhenUserEmailDoesNotExist() {
        // given
        String email = "jackma@mail.com";

        // when
        Optional<User> userFoundOtp = underTest.findByEmail(email);

        // then
        assertThat(userFoundOtp.isPresent()).isFalse();
    }
}
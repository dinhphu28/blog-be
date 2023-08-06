package tech.dinhphu28.blog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import tech.dinhphu28.blog.entity.Role;
import tech.dinhphu28.blog.entity.User;
import tech.dinhphu28.blog.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    private UserService underTest;

    @BeforeEach
    void setup() {
        underTest = new UserService(userRepository);
    }

    @Test
    void canActivateUser() {
        // given
        User user = User.builder()
                .firstName("Jack")
                .lastName("Ma")
                .email("jackma@mail.com")
                .password("password")
                .isEnabled(false)
                .role(Role.USER)
                .build();
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        // when
        underTest.activateUser(user.getEmail());

        // then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();

        assertThat(capturedUser.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(capturedUser.isEnabled()).isTrue();
    }

    @Test
    void willThrowWhenActivateUserNotFound() {
        // given
        String email = "jackma@mail.com";
        given(userRepository.findByEmail(email)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.activateUser(email))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User with email " + email + " does not exists");
        verify(userRepository, never()).save(any());
    }
}
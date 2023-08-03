package tech.dinhphu28.blog.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.dinhphu28.blog.entity.Role;
import tech.dinhphu28.blog.entity.Token;
import tech.dinhphu28.blog.entity.TokenType;
import tech.dinhphu28.blog.entity.User;
import tech.dinhphu28.blog.exception.OTPNotVerifiedException;
import tech.dinhphu28.blog.model.*;
import tech.dinhphu28.blog.repository.TokenRepository;
import tech.dinhphu28.blog.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final TOTPService totpService;
    private final SendMailSMTPService sendMailSMTPService;
    private final UserService userService;

    private final int MAX_DELAY_INTERVALS = 4;

    public RegisterResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .isEnabled(false)
                .role(Role.USER)
                .build();

        var savedUser = userRepository.save(user);

        Thread sendMailThread = new Thread(() -> {
            try {
                sendMailRegisterVerificationCode(user.getEmail());
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });
        sendMailThread.start();

        RegisterResponse response = RegisterResponse.builder()
                .status(200)
                .message("Next step: verification")
                .build();
        return response;
    }

    private void sendMailRegisterVerificationCode(String email) throws MessagingException {

        String otpSecret = totpService.generateSecret(email);
        int verificationCode = totpService.generate(otpSecret);

        sendMailSMTPService.sendHtmlEmail("Your verification code", "<h3>" + verificationCode + "</h3>", email);
    }

    public VerificationResponse verifyAndActivateUser(VerificationRequest request) throws OTPNotVerifiedException {
        VerificationResponse response;

        if(!isOTPVerificationValid(request.getOtp(), request.getEmail())) {
            throw new OTPNotVerifiedException("OTP cannot be verified");
        } else {
            userService.activateUser(request.getEmail());

            response = VerificationResponse.builder()
                    .status(200)
                    .message("User is verified")
                    .build();
        }

        return response;
    }

    private boolean isOTPVerificationValid(String otp, String email) {
        String otpSecret = totpService.generateSecret(email);
        boolean isValid = totpService.verify(otpSecret, otp, MAX_DELAY_INTERVALS);
        return isValid;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if(validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        tokenRepository.saveAll(validUserTokens);
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(token);
    }
}

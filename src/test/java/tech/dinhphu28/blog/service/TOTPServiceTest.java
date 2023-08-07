package tech.dinhphu28.blog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TOTPServiceTest {

    private TOTPService underTest;

    @BeforeEach
    void setup() {
        underTest = new TOTPService();
    }

    @Test
    void canGenerateSecretRandomly() {
        // given
        // when
        String secret = underTest.generateSecret();

        // then
        assertThat(secret).isNotEmpty();
    }

    @Test
    void canGenerateSecretWithKey() {
        // given
        String secretKey = "Abc@123";

        // when
        String secret = underTest.generateSecret(secretKey);

        // then
        assertThat(secret).isNotEmpty();
    }

    @Test
    void canGenerateOTP() {
        // given
        String secret = underTest.generateSecret();

        // when
        String otp = underTest.now(secret);

        // then
        assertThat(otp).hasSize(6);
    }

    @Test
    void canVerify() {
        // given
        String secret = underTest.generateSecret();
        String otp = underTest.now(secret);

        // when
        boolean isVerified = underTest.verify(secret, otp);

        // then
        assertThat(isVerified).isTrue();
    }

    @Test
    void whenVerifyWithInvalidOTP() {
        // given
        String secret = underTest.generateSecret();
        String otp = "000000";

        // when
        boolean isVerified = underTest.verify(secret, otp);

        // then
        assertThat(isVerified).isFalse();
    }

    @Test
    void canVerifyWithPastIntervals() {
        // given
        String secret = underTest.generateSecret();
        String otp = underTest.now(secret);
        int pastIntervals = 4;

        // when
        boolean isVerified = underTest.verify(secret, otp, pastIntervals);

        // then
        assertThat(isVerified).isTrue();
    }

    @Test
    void whenVerifyWithPastIntervalsAndInvalidOTP() {
        // given
        String secret = underTest.generateSecret();
        String otp = "000000";
        int pastIntervals = 4;

        // when
        boolean isVerified = underTest.verify(secret, otp, pastIntervals);

        // then
        assertThat(isVerified).isFalse();
    }
}
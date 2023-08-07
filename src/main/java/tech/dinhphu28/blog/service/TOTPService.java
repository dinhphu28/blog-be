package tech.dinhphu28.blog.service;

import org.jboss.aerogear.security.otp.Totp;
import org.jboss.aerogear.security.otp.api.*;
import org.springframework.stereotype.Service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class TOTPService {

    // Can use to generate QR code with 'otpauth://totp/user?secret=LRVLAZ4WVFOU3JBF&issuer=2fademo'
    public String generateSecret(String secretKey) {

        String secret = Base32.encode(secretKey.getBytes());

        return secret;
    }

    public String generateSecret() {
        String secret = Base32.random();

        return secret;
    }

    public boolean verify(String secret, String otp) {
        boolean isVerified = false;

        Totp totp = new Totp(secret);

        if(totp.verify(otp)) {
            isVerified = true;
        }

        return isVerified;
    }

    public boolean verify(String secret, String otpString, int pastIntervals) {
        boolean isValid = false;

        int otp = Integer.parseInt(otpString);
        long currentInterval = new Clock().getCurrentInterval();

        for(int i = 0; i <= pastIntervals; i++) {
            int candidate = generate(secret, currentInterval - i);
            if(candidate == otp) {
                return true;
            }
        }
        return false;
    }

    public int generate(String secret) {
        long currentInterval = new Clock().getCurrentInterval();
        int candidate = generate(secret, currentInterval);
        return candidate;
    }

    private int generate(String secret, long interval) {
        return hash(secret, interval);
    }

    private int hash(String secret, long interval) {
        byte[] hash = new byte[0];
        try {
            //Base32 encoding is just a requirement for google authenticator. We can remove it on the next releases.
            hash = new Hmac(Hash.SHA1, Base32.decode(secret), interval).digest();
        } catch (NoSuchAlgorithmException | InvalidKeyException | Base32.DecodingException e) {
            e.printStackTrace();
        }

        return bytesToInt(hash);
    }

    private int bytesToInt(byte[] hash) {
        // put selected bytes into result int
        int offset = hash[hash.length - 1] & 0xf;

        int binary = ((hash[offset] & 0x7f) << 24) |
                ((hash[offset + 1] & 0xff) << 16) |
                ((hash[offset + 2] & 0xff) << 8) |
                (hash[offset + 3] & 0xff);

        return binary % Digits.SIX.getValue();
    }

    private String leftPadding(int otp) {
        return String.format("%06d", otp);
    }
}

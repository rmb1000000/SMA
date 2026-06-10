package com.zaiqi;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
public class GenHashRunner {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // Generate hash for "admin123"
        String hash = encoder.encode("admin123");
        System.out.println(hash);
        // Also verify
        System.out.println("Matches admin123: " + encoder.matches("admin123", hash));
    }
}

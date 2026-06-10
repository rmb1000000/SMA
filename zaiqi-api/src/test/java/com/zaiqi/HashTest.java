package com.zaiqi;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
public class HashTest {
    @Test public void gen() {
        System.out.println(new BCryptPasswordEncoder().encode("admin123"));
    }
}

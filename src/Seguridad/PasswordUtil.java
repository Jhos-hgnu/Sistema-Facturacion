/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Seguridad;

/**
 *
 * @author brand
 */

import org.mindrot.jbcrypt.BCrypt;

public final class PasswordUtil {
    private static final int WORK_FACTOR = 12; // 10–12 es buen rango

    private PasswordUtil() {}

    /** Genera hash BCrypt (con sal) */
    public static String hash(String plain) {
        if (plain == null || plain.isBlank())
            throw new IllegalArgumentException("Contraseña vacía");
        if (isHash(plain)) return plain; // evita doble hash
        return BCrypt.hashpw(plain, BCrypt.gensalt(WORK_FACTOR));
    }

    /** Verifica una contraseña en texto contra un hash BCrypt */
    public static boolean verify(String plain, String hash) {
        if (plain == null || hash == null || hash.isBlank()) return false;
        return BCrypt.checkpw(plain, hash);
    }

    /** Detecta si ya es un hash BCrypt */
    public static boolean isHash(String s) {
        return s != null && (s.startsWith("$2a$") || s.startsWith("$2b$") || s.startsWith("$2y$"));
    }
}

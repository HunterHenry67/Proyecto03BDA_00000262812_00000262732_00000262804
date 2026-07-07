/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilerias;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author Andre
 */
public final class Criptografia {

    private static final String ALGORITMO = "PBKDF2WithHmacSHA256";
    private static final int LONGITUD_SAL = 16;   
    private static final int LONGITUD_HASH = 256; 
    private static final int ITERACIONES = 10000;

    private Criptografia() {
    }

    public static String generarSal() {
        SecureRandom random = new SecureRandom();
        byte[] sal = new byte[LONGITUD_SAL];
        random.nextBytes(sal);
        return Base64.getEncoder().encodeToString(sal);
    }

    public static String encriptar(String contrasena, String sal) {
        try {
            byte[] salBytes = Base64.getDecoder().decode(sal);
            PBEKeySpec spec = new PBEKeySpec(
                    contrasena.toCharArray(),
                    salBytes,
                    ITERACIONES,
                    LONGITUD_HASH
            );
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITMO);
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Error al encriptar la contraseña", e);
        }
    }

    public static boolean verificar(String contrasena, String sal, String hashEsperado) {
        String hashCalculado = encriptar(contrasena, sal);
        return java.security.MessageDigest.isEqual(
                hashCalculado.getBytes(java.nio.charset.StandardCharsets.UTF_8),
                hashEsperado.getBytes(java.nio.charset.StandardCharsets.UTF_8)
        );
    }
}
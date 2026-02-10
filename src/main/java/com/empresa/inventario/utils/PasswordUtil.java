package com.empresa.inventario.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utilidad segura para cifrado AES-GCM.
 * No utiliza el modo ECB (inseguro) y gestiona IVs aleatorios por cada cifrado.
 */
public class PasswordUtil {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128; // Longitud del tag de autenticación
    private static final int IV_LENGTH_BYTE = 12;  // Recomendado para GCM
    private static final int KEY_LENGTH_BYTE = 32; // Para AES-256

    // NOTA: En producción, carga esto desde una variable de entorno o KeyVault
    private static final String SECRET_KEY_STR = System.getenv("APP_ENCRYPTION_KEY");

    /**
     * Cifra un texto plano utilizando AES-GCM con un IV aleatorio.
     */
    public static String encrypt(String plainText) throws Exception {
        byte[] iv = new byte[IV_LENGTH_BYTE];
        new SecureRandom().nextBytes(iv); // Generar IV aleatorio

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(), spec);

        byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        // Concatenamos IV + CipherText para poder recuperarlo al descifrar
        byte[] cipherTextWithIv = ByteBuffer.allocate(iv.length + cipherText.length)
                .put(iv)
                .put(cipherText)
                .array();

        return Base64.getEncoder().encodeToString(cipherTextWithIv);
    }

    /**
     * Descifra un texto cifrado en Base64.
     */
    public static String decrypt(String encryptedBase64) throws Exception {
        byte[] decode = Base64.getDecoder().decode(encryptedBase64);

        // Extraer el IV de los primeros 12 bytes
        ByteBuffer bb = ByteBuffer.wrap(decode);
        byte[] iv = new byte[IV_LENGTH_BYTE];
        bb.get(iv);
        byte[] cipherText = new byte[bb.remaining()];
        bb.get(cipherText);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), spec);

        byte[] plainText = cipher.doFinal(cipherText);
        return new String(plainText, StandardCharsets.UTF_8);
    }

    private static SecretKey getSecretKey() {
        // Validación básica de la clave
        byte[] keyBytes = (SECRET_KEY_STR != null) 
                ? SECRET_KEY_STR.getBytes(StandardCharsets.UTF_8) 
                : new byte[KEY_LENGTH_BYTE]; // Fallback o manejo de error
        
        // Asegurar que la clave tenga exactamente 32 bytes para AES-256
        byte[] finalKey = new byte[KEY_LENGTH_BYTE];
        System.arraycopy(keyBytes, 0, finalKey, 0, Math.min(keyBytes.length, KEY_LENGTH_BYTE));
        
        return new SecretKeySpec(finalKey, "AES");
    }
    
    
    public static void main(String[] args) {
		try {
			String passwordCodificado = PasswordUtil.encrypt("12345678");
			System.err.println(passwordCodificado);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
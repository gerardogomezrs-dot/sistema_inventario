package com.empresa.inventario.utils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class PasswordUtil {

	private static final String ALGORITHM = "AES";
	private static final String KEY = "ThisIsASecretKeyForAES256Encryption"; // 32 characters

	public static String encrypt(String data) throws Exception {
		byte[] keyBytes = KEY.getBytes(StandardCharsets.UTF_8);
		keyBytes = Arrays.copyOf(keyBytes, 32);

		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, ALGORITHM);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, keySpec);
		byte[] encryptedBytes = cipher.doFinal(data.getBytes());
		return Base64.getEncoder().encodeToString(encryptedBytes);
	}

	public static String decrypt(String encryptedData) throws Exception {
		SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, keySpec);
		byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
		return new String(decryptedBytes);
	}

	public static void main(String[] args) throws Exception {
		String f = decrypt("LqGEya6oMCUVSBlAC4V+Hw==");
		System.err.println(f);
	}
}

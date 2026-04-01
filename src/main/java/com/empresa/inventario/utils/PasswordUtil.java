package com.empresa.inventario.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PasswordUtil {

	private static final String ALGORITHM = "AES/GCM/NoPadding";
	private static final int TAG_LENGTH_BIT = 128;
	private static final int IV_LENGTH_BYTE = 12; 
	private static final int KEY_LENGTH_BYTE = 32; 

	private static final String SECRET_KEY_STR = System.getenv("APP_ENCRYPTION_KEY");
	
	private static final SecureRandom SECURE_RANDOM = new SecureRandom();

	public static String encrypt(String plainText) {
		byte[] iv = new byte[IV_LENGTH_BYTE];
		SECURE_RANDOM.nextBytes(iv);
		byte[] cipherTextWithIv = null;
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
			cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(), spec);

			byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

			cipherTextWithIv = ByteBuffer.allocate(iv.length + cipherText.length).put(iv).put(cipherText).array();
		} catch (Exception e) {
			e.getMessage();
		}
		return Base64.getEncoder().encodeToString(cipherTextWithIv);
	}

	
	public static String decrypt(String encryptedBase64) {
		byte[] decode = Base64.getDecoder().decode(encryptedBase64);
		byte[] plainText = null;
		ByteBuffer bb = ByteBuffer.wrap(decode);
		byte[] iv = new byte[IV_LENGTH_BYTE];
		bb.get(iv);
		byte[] cipherText = new byte[bb.remaining()];
		bb.get(cipherText);
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
			cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), spec);

			plainText = cipher.doFinal(cipherText);
		} catch (Exception e) {
			e.getMessage();
		}
		return new String(plainText, StandardCharsets.UTF_8);
	}

	private static SecretKey getSecretKey() {
		byte[] keyBytes = (SECRET_KEY_STR != null) ? SECRET_KEY_STR.getBytes(StandardCharsets.UTF_8)
				: new byte[KEY_LENGTH_BYTE];

		byte[] finalKey = new byte[KEY_LENGTH_BYTE];
		System.arraycopy(keyBytes, 0, finalKey, 0, Math.min(keyBytes.length, KEY_LENGTH_BYTE));

		return new SecretKeySpec(finalKey, "AES");
	}

}
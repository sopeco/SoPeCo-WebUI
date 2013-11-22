package org.sopeco.webui.server.security;

import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public final class Crypto {

	/** 8 random bytes */
	private static final byte[] SALT = { 'S', 'O', 'F', 'T', 'W', 'A', 'R', 'E' };

	private Crypto() {
	}

	/**
	 * Calculates the hash value of the given input string using the SHA-256
	 * algorithm.
	 * 
	 * @param input
	 * @return hash value of the input string.
	 */
	public static String sha256(String input) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			byte[] hashBytes = messageDigest.digest(input.getBytes());
			StringBuffer hashString = new StringBuffer();
			for (byte b : hashBytes) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1)
					hashString.append(0);
				hashString.append(hex);
			}
			return hashString.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(e);
		}
	}

	public static byte[][] encrypt(String password, String input) {
		try {
			SecretKey secretKey = getSecret(password.toCharArray(), SALT);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			AlgorithmParameters params = cipher.getParameters();

			byte[][] returnArray = new byte[2][];
			returnArray[0] = params.getParameterSpec(IvParameterSpec.class).getIV();
			returnArray[1] = cipher.doFinal(input.getBytes("UTF-8"));
			return returnArray;
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(e);
		} catch (NoSuchPaddingException e) {
			throw new IllegalStateException(e);
		} catch (InvalidKeyException e) {
			throw new IllegalStateException(e);
		} catch (IllegalBlockSizeException e) {
			throw new IllegalStateException(e);
		} catch (BadPaddingException e) {
			throw new IllegalStateException(e);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		} catch (InvalidParameterSpecException e) {
			throw new IllegalStateException(e);
		}
	}

	public static String decrypt(String password, byte[][] inputAndInitialVector) {
		try {
			SecretKey secretKey = getSecret(password.toCharArray(), SALT);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(inputAndInitialVector[0]));
			return new String(cipher.doFinal(inputAndInitialVector[1]), "UTF-8");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(e);
		} catch (NoSuchPaddingException e) {
			throw new IllegalStateException(e);
		} catch (InvalidKeyException e) {
			throw new IllegalStateException(e);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		} catch (IllegalBlockSizeException e) {
			throw new IllegalStateException(e);
		} catch (BadPaddingException e) {
			throw new IllegalStateException(e);
		} catch (InvalidAlgorithmParameterException e) {
			throw new IllegalStateException(e);
		}
	}

	private static SecretKey getSecret(char[] password, byte[] salt) {
		try {
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec keySpec = new PBEKeySpec(password, salt, 65536, 128);

			SecretKey tmpKey = keyFactory.generateSecret(keySpec);
			SecretKey secret = new SecretKeySpec(tmpKey.getEncoded(), "AES");

			return secret;
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(e);
		} catch (InvalidKeySpecException e) {
			throw new IllegalStateException(e);
		}
	}
}

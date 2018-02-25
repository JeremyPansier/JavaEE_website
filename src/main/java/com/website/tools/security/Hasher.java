package com.website.tools.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher {

	private Hasher() {
		super();
	}

	private static byte[] hash(final String algorithme, final String monMessage) throws NoSuchAlgorithmException {
		final MessageDigest sha = MessageDigest.getInstance(algorithme);
		return sha.digest(monMessage.getBytes());
	}

	private static String bytesToHex(final byte[] b) {
		final char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		final StringBuilder buffer = new StringBuilder();
		for (int j = 0; j < b.length; j++) {
			buffer.append(hexDigits[(b[j] >> 4) & 0x0f]);
			buffer.append(hexDigits[b[j] & 0x0f]);
		}
		return buffer.toString();
	}

	private static String hashToHex(final String algorithme, final String monMessage) throws NoSuchAlgorithmException {
		return bytesToHex(hash(algorithme, monMessage));
	}

	public static String sha1ToHex(final String monMessage) {
		try {
			return hashToHex("SHA-1", monMessage);
		}
		catch (final NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}

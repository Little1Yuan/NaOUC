package synjones.commerce.utils;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class SymmetricCryptoUtil {
    public static byte[] encrypt(byte[] data, byte[] key, String algorithm, int mode) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String finalAlgorithm = algorithm + "/CBC/PKCS5Padding";
        byte[] iv = createZeroBlock(finalAlgorithm);
        return encrypt(key, iv, finalAlgorithm, "CBC", mode).doFinal(data);
    }

    public static Cipher encrypt(byte[] key, byte[] iv, String transformation, String algorithm, int mode) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance(transformation);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, a(transformation, "/"));
        if (isEquals(algorithm, "CBC")) {
            cipher.init(mode, secretKeySpec, new IvParameterSpec(iv));
        } else {
            cipher.init(mode, secretKeySpec);
        }
        return cipher;
    }

    public static byte[] createZeroBlock(String transformation) throws NoSuchPaddingException, NoSuchAlgorithmException {
        int blockSize = Cipher.getInstance(transformation).getBlockSize();
        return new byte[blockSize];
    }

    public static boolean isEquals(CharSequence charSequence, CharSequence charSequence2) {
        if (charSequence == charSequence2) {
            return true;
        }
        if (charSequence == null || charSequence2 == null) {
            return false;
        }
        if ((charSequence instanceof String) && (charSequence2 instanceof String)) {
            return charSequence.equals(charSequence2);
        }
        return charSequence.equals(charSequence2);
    }

    public static String a(String str, String str2) {
        if (str == null || str2 == null) {
            return str;
        }
        if (str2.isEmpty()) {
            return "";
        }
        int iIndexOf = str.indexOf(str2);
        return iIndexOf == -1 ? str : str.substring(0, iIndexOf);
    }

}

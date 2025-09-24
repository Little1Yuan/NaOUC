package cn.nahco3awa.naouc.util;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class SignUtils {
    public String getPrivateKey(String data, String key, String alg) {
        try {
            return new String(Base64.getEncoder().encode(SignatureUtil.getPrivateSign(data.getBytes(), Base64.getDecoder().decode(key), alg)));
        } catch (GeneralSecurityException e) {
            return "";
        }
    }
    public String sign(String s, String key, String mode) {
        Base64.Encoder encoder = Base64.getEncoder();
        try {
            return new String(encoder.encode(SymmetricCryptoUtil.encrypt(s.getBytes(StandardCharsets.UTF_8), Base64.getDecoder().decode(key), mode, Cipher.ENCRYPT_MODE)));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException |
                 InvalidAlgorithmParameterException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException e) {
            return "";
        }
    }
}

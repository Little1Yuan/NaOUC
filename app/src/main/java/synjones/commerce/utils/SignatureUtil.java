package synjones.commerce.utils;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;

public class SignatureUtil {
    public static byte[] getPrivateSign(byte[] bs, byte[] bs2, String alg) throws GeneralSecurityException {
        PrivateKey privateKeyA = a(bs2, alg);
        Signature signature = Signature.getInstance(alg);
        signature.initSign(privateKeyA);
        signature.update(bs);
        return signature.sign();

    }

    public static PrivateKey a(byte[] bArr, String str) throws GeneralSecurityException {
        return KeyFactory.getInstance(a(str, "with")).generatePrivate(new PKCS8EncodedKeySpec(bArr));
    }

    public static String a(String str, String str2) {
        int iIndexOf;
        return (str2 == null || (iIndexOf = str.indexOf(str2)) == -1) ? "" : str.substring(iIndexOf + str2.length());
    }
}

package cn.nahco3awa.naouc.util.passwd;

public class RSA {
    public static class RSAKeyPair {
        public BigInt e;
        public BigInt d;
        public BigInt m;
        public int digitSize;
        public int chunkSize;
        public int radix;
        public BarrettMu barrett;

        public RSAKeyPair(String encryptionExponent, String decryptionExponent, String modulus) {
            this.e = BigInt.biFromHex(encryptionExponent);
            this.d = BigInt.biFromHex(decryptionExponent);
            this.m = BigInt.biFromHex(modulus);

            ////////////////////////////////// TYF
            this.digitSize = 2 * m.biHighIndex() + 2;
            this.chunkSize = this.digitSize - 11;
            ////////////////////////////////// TYF

            this.radix = 16;
            this.barrett = new BarrettMu(this.m);
        }
    }
    public static String encryptedString(RSAKeyPair key, String s) {
        ////////////////////////////////// TYF
        if (key.chunkSize > key.digitSize - 11)
        {
            return "Error";
        }
        ////////////////////////////////// TYF


        var a = new int[s.length()];
        var sl = s.length();

        var i = 0;
        while (i < sl) {
            a[i] = s.charAt(i);
            i++;
        }

        var al = a.length;
        StringBuilder result = new StringBuilder();
        int j, k;
        BigInt block;
        for (i = 0; i < al; i += key.chunkSize) {
            block = new BigInt();
            j = 0;

            //for (k = i; k < i + key.chunkSize; ++j) {
            //	block.digits[j] = a[k++];
            //	block.digits[j] += a[k++] << 8;
            //}

            ////////////////////////////////// TYF
            // Add PKCS#1 v1.5 padding
            // 0x00 || 0x02 || PseudoRandomNonZeroBytes || 0x00 || Message
            // Variable a before padding must be of at most digitSize-11
            // That is for 3 marker bytes plus at least 8 random non-zero bytes
            int x;
            var msgLength = (i+key.chunkSize)>al ? al%key.chunkSize : key.chunkSize;
            var paddedSize = Math.max(8, key.digitSize - 3 - msgLength);
            // Variable b with 0x00 || 0x02 at the highest index.
            int[] b = new int[Math.max(msgLength+1+paddedSize, key.digitSize)];
            for (x=0; x<msgLength; x++)
            {
                b[x] = a[i+msgLength-1-x];
            }
            b[msgLength] = 0; // marker

            for (x=0; x<paddedSize; x++) {
                b[msgLength+1+x] = (int) (Math.floor(Math.random()*254) + 1); // [1,255]
            }
            // It can be asserted that msgLength+paddedSize == key.digitSize-3
            b[key.digitSize-2] = 2; // marker
            b[key.digitSize-1] = 0; // marker

            for (k = 0; k < key.digitSize; ++j)
            {
                block.digits[j] = b[k++];
                block.digits[j] += b[k++] << 8;
            }
            ////////////////////////////////// TYF

            var crypt = key.barrett.powMod(block, key.e);
            var text = key.radix == 16 ? BigInt.biToHex(crypt) : BigInt.biToString(crypt, key.radix);
            result.append(text).append(" ");
        }
        return result.substring(0, Math.max(result.toString().length() - 1, 0)); // Remove last space.
    }
}

package cn.nahco3awa.naouc.util.passwd;

public class BigInt {
    public boolean isNeg;
    public int[] digits;
    public static int maxDigits;
    public static int[] zeroArray;
    public static BigInt bigZero;
    public static BigInt bigOne;
    public static final int BI_RADIX = 1 << 16;
    public static final int BI_RADIX_BASE = 2;
    public static final int BI_RADIX_BITS = 16;
    public static final int BI_HALF_RADIX = BI_RADIX >>> 1;
    public static final long BI_RADIX_SQUARED = (long) BI_RADIX * BI_RADIX;
    public static final int MAX_DIGIT_VAL = BI_RADIX - 1;
    public static final long MAX_INTEGER = 9_999_999_999_999_998L;
    public static final int[] LOW_BIT_MASKS = new int[] {
            0x0000, 0x0001, 0x0003, 0x0007, 0x000F, 0x001F,
            0x003F, 0x007F, 0x00FF, 0x01FF, 0x03FF, 0x07FF,
            0x0FFF, 0x1FFF, 0x3FFF, 0x7FFF, 0xFFFF
    };
    public static final int[] HIGH_BIT_MASKS = new int[] {
            0x0000, 0x8000, 0xC000, 0xE000, 0xF000, 0xF800,
            0xFC00, 0xFE00, 0xFF00, 0xFF80, 0xFFC0, 0xFFE0,
            0xFFF0, 0xFFF8, 0xFFFC, 0xFFFE, 0xFFFF
    };
    public static final char[] HEX_TO_CHAR = new char[] {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'
    };
    public static final char[] hexatrigesimalToChar = new char[] {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z'
    };

    public static void setMaxDigits(int value) {
        maxDigits = value;
        zeroArray = new int[maxDigits];
        for (int i = 0; i < maxDigits; i++) {
            zeroArray[i] = 0;
        }
        bigZero = new BigInt();
        bigOne = new BigInt();
        bigOne.digits[0] = 1;
    }

    public BigInt() {
        digits = zeroArray.clone();
        isNeg = false;
    }

    public BigInt(boolean flag) {
        if (flag) {
            digits = null;
        } else {
            digits = zeroArray.clone();
        }
        isNeg = false;
    }

    public static int charToHex(char c) {
        int zero = 48;
        int nine = zero + 9;
        int lA = 97;
        int lZ = lA + 25;
        int bA = 65;
        int bZ = bA + 25;

        if (c >= zero && c <= nine) {
            return c - zero;
        } else if (c >= bA && c <= bZ) {
            return 10 + c - bA;
        } else if (c >= lA && c <= lZ) {
            return 10 + c - lA;
        } else {
            return 0;
        }
    }

    public static int hexToDigit(String s) {
        int result = 0;
        int sl = Math.min(s.length(), 4);
        for (int i = 0; i < sl; i++) {
            result <<= 4;
            result |= charToHex(s.charAt(i));
        }
        return result;
    }

    public static BigInt biFromHex(String s) {
        BigInt result = new BigInt();
        int sl = s.length();
        for (int i = sl, j = 0; i > 0; i -= 4, ++j) {
            int ia = Math.max(i - 4, 0);
            int ib = Math.min(i, 4);
            result.digits[j] = hexToDigit(s.substring(Math.min(ia, ib), Math.max(ia, ib)));
        }
        return result;
    }

    public static BigInt biDivideByRadixPower(BigInt x, int n) {
        var ret = new BigInt();
        arrayCopy(x.digits, n, ret.digits, 0, ret.digits.length - n);
        return ret;
    }

    public static BigInt biMultiply(BigInt x, BigInt y) {
        var result = new BigInt();
        int c;
        var n = x.biHighIndex();
        var t = y.biHighIndex();
        int uv, k;

        for (int i = 0; i <= t; ++i) {
            c = 0;
            k = i;
            for (int j = 0; j <= n; ++j, ++k) {
                uv = result.digits[k] + x.digits[j] * y.digits[i] + c;
                result.digits[k] = uv & MAX_DIGIT_VAL;
                c = uv >>> BI_RADIX_BITS;
                //c = Math.floor(uv / biRadix);
            }
            result.digits[i + n + 1] = c;
        }
        // Someone give me a logical xor, please.
        result.isNeg = x.isNeg != y.isNeg;
        return result;
    }

    public static BigInt biModuloByRadixPower(BigInt x, int n) {
        var result = new BigInt();
        arrayCopy(x.digits, 0, result.digits, 0, n);
        return result;
    }

    public static String biToHex(BigInt x) {
        StringBuilder result = new StringBuilder();
        for (var i = x.biHighIndex(); i > -1; --i) {
            result.append(digitToHex(x.digits[i]));
        }
        return result.toString();
    }


    public static String reverseStr(String s)
    {
        StringBuilder result = new StringBuilder();
        for (var i = s.length() - 1; i > -1; --i) {
            result.append(s.charAt(i));
        }
        return result.toString();
    }

    private static String digitToHex(int n) {
        var mask = 0xf;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 4; ++i) {
            result.append(HEX_TO_CHAR[n & mask]);
            n >>>= 4;
        }
        return reverseStr(result.toString());
    }

    public static String biToString(BigInt x, int radix) {
        var b = new BigInt();
        b.digits[0] = radix;
        var qr = biDivideModulo(x, b);
        StringBuilder result = new StringBuilder(hexatrigesimalToChar[qr[1].digits[0]]);
        while (biCompare(qr[0], bigZero) == 1) {
            qr = biDivideModulo(qr[0], b);
            // [???] digit = qr[1].digits[0];
            result.append(hexatrigesimalToChar[qr[1].digits[0]]);
        }
        return (x.isNeg ? "-" : "") + reverseStr(result.toString());
    }

    public int biHighIndex() {
        int result = digits.length - 1;
        while (result > 0 && digits[result] == 0) -- result;
        return result;
    }

    public BigInt biCopy() {
        BigInt result = new BigInt(true);
        result.digits = digits.clone();
        result.isNeg = isNeg;
        return result;
    }

    public static final int BITS_PER_DIGIT = 16;
    public int biNumBits() {
        int n = biHighIndex();
        int d = digits[n];
        int m = (n + 1) * BITS_PER_DIGIT;
        int result;
        for (result = m; result > m - BITS_PER_DIGIT; --result) {
            if ((d & 0x8000) != 0) break;
            d <<= 1;
        }
        return result;
    }

    public static BigInt biAdd(BigInt x, BigInt y) {
        BigInt result;

        if (x.isNeg != y.isNeg) {
            y.isNeg = !y.isNeg;
            result = biSubtract(x, y);
            y.isNeg = !y.isNeg;
        }
        else {
            result = new BigInt();
            var c = 0;
            int n;
            for (var i = 0; i < x.digits.length; ++i) {
                n = x.digits[i] + y.digits[i] + c;
                result.digits[i] = n % BI_RADIX;
                c = (n >= BI_RADIX) ? 1 : 0;
            }
            result.isNeg = x.isNeg;
        }
        return result;
    }


    public static BigInt biSubtract(BigInt x, BigInt y) {
        BigInt result;
        if (x.isNeg != y.isNeg) {
            y.isNeg = !y.isNeg;
            result = biAdd(x, y);
            y.isNeg = !y.isNeg;
        } else {
            result = new BigInt();
            int n, c;
            c = 0;
            for (var i = 0; i < x.digits.length; ++i) {
                n = x.digits[i] - y.digits[i] + c;
                result.digits[i] = n % BI_RADIX;
                // Stupid non-conforming modulus operation.
                if (result.digits[i] < 0) result.digits[i] += BI_RADIX;
                c = -((n < 0) ? 1 : 0);
            }
            // Fix up the negative sign, if any.
            if (c == -1) {
                c = 0;
                for (var i = 0; i < x.digits.length; ++i) {
                    n = -result.digits[i] + c;
                    result.digits[i] = n % BI_RADIX;
                    // Stupid non-conforming modulus operation.
                    if (result.digits[i] < 0) result.digits[i] += BI_RADIX;
                    c = -((n < 0) ? 1 : 0);
                }
                // Result is opposite sign of arguments.
                result.isNeg = !x.isNeg;
            } else {
                // Result is same sign.
                result.isNeg = x.isNeg;
            }
        }
        return result;
    }

    public static void arrayCopy(int[] src, int srcStart, int[] dest, int destStart, int n)
    {
        var m = Math.min(srcStart + n, src.length);
        for (int i = srcStart, j = destStart; i < m; ++i, ++j) {
            dest[j] = src[i];
        }
    }

    public BigInt biShiftLeft(int n) {
        int digitCount = (int) Math.floor((double) n / BITS_PER_DIGIT);
        var result = new BigInt();

        arrayCopy(digits, 0, result.digits, digitCount,
                result.digits.length - digitCount);

        var bits = n % BITS_PER_DIGIT;
        var rightBits = BITS_PER_DIGIT - bits;
        int i, i1;
        for (i = result.digits.length - 1, i1 = i - 1; i > 0; --i, --i1) {
            result.digits[i] = ((result.digits[i] << bits) & MAX_DIGIT_VAL) |
                    ((result.digits[i1] & HIGH_BIT_MASKS[bits]) >>>
                            (rightBits));
        }
        result.digits[0] = ((result.digits[i] << bits) & MAX_DIGIT_VAL);
        result.isNeg = isNeg;
        return result;
    }

    public static int biCompare(BigInt x, BigInt y)
    {
        if (x.isNeg != y.isNeg) {
            return 1 - 2 * (x.isNeg ? 1 : 0);
        }
        for (var i = x.digits.length - 1; i >= 0; --i) {
            if (x.digits[i] != y.digits[i]) {
                if (x.isNeg) {
                    return 1 - 2 * (x.digits[i] > y.digits[i] ? 1 : 0);
                } else {
                    return 1 - 2 * (x.digits[i] < y.digits[i] ? 1 : 0);
                }
            }
        }
        return 0;
    }

    public BigInt biShiftRight(int n)
    {
        int digitCount = (int) Math.floor((double) n / BITS_PER_DIGIT);
        var result = new BigInt();
        arrayCopy(digits, digitCount, result.digits, 0,
                digits.length - digitCount);
        var bits = n % BITS_PER_DIGIT;
        var leftBits = BITS_PER_DIGIT - bits;
        for (int i = 0, i1 = i + 1; i < result.digits.length - 1; ++i, ++i1) {
            result.digits[i] = (result.digits[i] >>> bits) |
                    ((result.digits[i1] & LOW_BIT_MASKS[bits]) << leftBits);
        }
        result.digits[result.digits.length - 1] >>>= bits;
        result.isNeg = isNeg;
        return result;
    }

    public static BigInt biMultiplyDigit(BigInt x, int y)
    {
        int n, c, uv;

        BigInt result = new BigInt();
        n = x.biHighIndex();
        c = 0;
        for (var j = 0; j <= n; ++j) {
            uv = result.digits[j] + x.digits[j] * y + c;
            result.digits[j] = uv & MAX_DIGIT_VAL;
            c = uv >>> BI_RADIX_BITS;
            //c = Math.floor(uv / biRadix);
        }
        result.digits[1 + n] = c;
        return result;
    }

    public static BigInt biMultiplyByRadixPower(BigInt x, int n)
    {
        var result = new BigInt();
        arrayCopy(x.digits, 0, result.digits, n, result.digits.length - n);
        return result;
    }

    public static BigInt[] biDivideModulo(BigInt x, BigInt y) {
        int nb = x.biNumBits();
        int tb = y.biNumBits();
        boolean origYIsNeg = y.isNeg;
        BigInt q, r;
        if (nb < tb) {
            if (x.isNeg) {
                q = bigOne.biCopy();
                q.isNeg = !y.isNeg;
                x.isNeg = false;
                y.isNeg = false;
                r = biSubtract(y, x);
                // Restore signs, 'cause they're references.
                x.isNeg = true;
                y.isNeg = origYIsNeg;
            } else {
                q = new BigInt();
                r = x.biCopy();
            }
            return new BigInt[]{q, r};
        }

        q = new BigInt();
        r = x;

        // Normalize Y.
        int t = (int) (Math.ceil((double) tb / BITS_PER_DIGIT) - 1);
        var lambda = 0;
        while (y.digits[t] < BI_HALF_RADIX) {
            y = y.biShiftLeft(1);
            ++lambda;
            ++tb;
            t = (int) (Math.ceil((double) tb / BITS_PER_DIGIT) - 1);
        }
        // Shift r over to keep the quotient constant. We'll shift the
        // remainder back at the end.
        r = r.biShiftLeft(lambda);
        nb += lambda; // Update the bit count for x.
        int n = (int) (Math.ceil((double) nb / BITS_PER_DIGIT) - 1);

        var b = biMultiplyByRadixPower(y, n - t);
        while (biCompare(r, b) != -1) {
            ++q.digits[n - t];
            r = biSubtract(r, b);
        }
        for (var i = n; i > t; --i) {
            var ri = (i >= r.digits.length) ? 0 : r.digits[i];
            var ri1 = (i - 1 >= r.digits.length) ? 0 : r.digits[i - 1];
            var ri2 = (i - 2 >= r.digits.length) ? 0 : r.digits[i - 2];
            var yt = (t >= y.digits.length) ? 0 : y.digits[t];
            var yt1 = (t - 1 >= y.digits.length) ? 0 : y.digits[t - 1];
            if (ri == yt) {
                q.digits[i - t - 1] = MAX_DIGIT_VAL;
            } else {
                q.digits[i - t - 1] = (int) Math.floor((double) (ri * BI_RADIX + ri1) / yt);
            }

            var c1 = q.digits[i - t - 1] * ((yt * BI_RADIX) + yt1);
            var c2 = (ri * BI_RADIX_SQUARED) + (((long) ri1 * BI_RADIX) + ri2);
            while (c1 > c2) {
                --q.digits[i - t - 1];
                c1 = q.digits[i - t - 1] * ((yt * BI_RADIX) | yt1);
                c2 = ((long) ri * BI_RADIX * BI_RADIX) + (((long) ri1 * BI_RADIX) + ri2);
            }

            b = biMultiplyByRadixPower(y, i - t - 1);
            r = biSubtract(r, biMultiplyDigit(b, q.digits[i - t - 1]));
            if (r.isNeg) {
                r = biAdd(r, b);
                --q.digits[i - t - 1];
            }
        }
        r = r.biShiftRight(lambda);
        // Fiddle with the signs and stuff to make sure that 0 <= r < y.
        q.isNeg = x.isNeg != origYIsNeg;
        if (x.isNeg) {
            if (origYIsNeg) {
                q = biAdd(q, bigOne);
            } else {
                q = biSubtract(q, bigOne);
            }
            y = y.biShiftRight(lambda);
            r = biSubtract(y, r);
        }
        // Check for the unbelievably stupid degenerate case of r == -0.
        if (r.digits[0] == 0 && r.biHighIndex() == 0) r.isNeg = false;

        return new BigInt[]{q, r};
    }

    public static BigInt biDivide(BigInt x, BigInt y) {
        return biDivideModulo(x, y)[0];
    }
}

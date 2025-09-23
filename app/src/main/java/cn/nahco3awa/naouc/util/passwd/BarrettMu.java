package cn.nahco3awa.naouc.util.passwd;

public class BarrettMu {
    public BigInt modulus;
    public int k;
    public BigInt mu;
    public BigInt bkplus1;

    public BarrettMu(BigInt m) {
        modulus = m.biCopy();
        k = modulus.biHighIndex() + 1;
        BigInt b2k = new BigInt();
        b2k.digits[2 * k] = 1;
        mu = BigInt.biDivide(b2k, modulus);
        bkplus1 = new BigInt();
        bkplus1.digits[k + 1] = 1;
    }

    public BigInt modulo(BigInt x) {
        var q1 = BigInt.biDivideByRadixPower(x, this.k - 1);
        var q2 = BigInt.biMultiply(q1, this.mu);
        var q3 = BigInt.biDivideByRadixPower(q2, this.k + 1);
        var r1 = BigInt.biModuloByRadixPower(x, this.k + 1);
        var r2term = BigInt.biMultiply(q3, this.modulus);
        var r2 = BigInt.biModuloByRadixPower(r2term, this.k + 1);
        var r = BigInt.biSubtract(r1, r2);
        if (r.isNeg) {
            r = BigInt.biAdd(r, this.bkplus1);
        }
        var rgtem = BigInt.biCompare(r, this.modulus) >= 0;
        while (rgtem) {
            r = BigInt.biSubtract(r, this.modulus);
            rgtem = BigInt.biCompare(r, this.modulus) >= 0;
        }
        return r;
    }

    public BigInt multiplyMod(BigInt x, BigInt y) {
        var xy = BigInt.biMultiply(x, y);
        return this.modulo(xy);
    }

    public BigInt powMod(BigInt x, BigInt y) {
        var result = new BigInt();
        result.digits[0] = 1;
        var a = x;
        var k = y;
        while (true) {
            if ((k.digits[0] & 1) != 0) result = this.multiplyMod(result, a);
            k = k.biShiftRight(1);
            if (k.digits[0] == 0 && k.biHighIndex() == 0) break;
            a = this.multiplyMod(a, a);
        }
        return result;
    }
}

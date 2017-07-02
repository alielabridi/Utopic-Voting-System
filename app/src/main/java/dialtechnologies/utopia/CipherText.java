package dialtechnologies.utopia;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;

/**
 * Created by Ali on 6/19/2017.
 */
public class CipherText {
    // A Ciphertext is an ElGamal ciphertext, where g is Key.Generator, r is a
    // random value, m is a message, and y is Key.PublicValue.

    public CipherText(BigInteger alpha, BigInteger beta) {
        this.alpha = alpha;
        this.beta = beta;
    }

    // Alpha = g^r
    @JsonProperty("alpha")
    BigInteger alpha;

    // Beta = g^m * y^r
    @JsonProperty("beta")
    BigInteger beta;

    // MulCiphertexts multiplies an ElGamal Ciphertext value element-wise into an
    // existing Ciphertext. This has the effect of adding the value encrypted in the
    // other Ciphertext to the prod Ciphertext. The prime specifies the group in
    // which these multiplication operations are to be performed.
    void MulCipherTexts(CipherText other, BigInteger prime){
        this.alpha = this.alpha.multiply(other.alpha);
        this.alpha = this.alpha.mod(prime);
        this.beta = this.beta.multiply(other.beta);
        this.beta = this.beta.mod(prime);
    }

    @Override
    public String toString() {
        return "{"
                + "\"alpha\": " + alpha
                + ", \"beta\": " + beta
                + "}";
    }
}

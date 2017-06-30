package dialtechnologies.utopia;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;

/**
 * Created by Ali on 6/19/2017.
 */
public class CipherText {
    // A Ciphertext is an ElGamal ciphertext, where g is Key.Generator, r is a
    // random value, m is a message, and y is Key.PublicValue.

    // Alpha = g^r
    @JsonProperty("alpha")
    BigInteger alpha;

    // Beta = g^m * y^r
    @JsonProperty("beta")
    BigInteger beta;

    @Override
    public String toString() {
        return "{"
                + "\"alpha\": " + alpha
                + ", \"beta\": " + beta
                + "}";
    }
}

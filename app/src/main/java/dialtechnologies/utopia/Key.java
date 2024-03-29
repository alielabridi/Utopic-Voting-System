package dialtechnologies.utopia;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;

/**
 * Created by Ali Elabridi on 6/19/2017.
 * all the data structures above was extracted from the original structure of Helios
 * and added some convenience data
 */

public class Key {
    // A Key is an ElGamal public key. There is one Key in each Election, and it
    // specifies the group in which computations are to be performed. Encryption of
    // a value m is performed as (g^r, g^m * y^r) mod p.

    // Generator is the generator element g used in ElGamal encryptions.
    @JsonProperty("g")
    BigInteger g;

    // Prime is the prime p for the group used in encryption.
    @JsonProperty("p")
    BigInteger p;

    // ExponentPrime is another prime that specifies the group of exponent
    // values in the exponent of Generator. It is used in challenge
    // generation and verification.
    @JsonProperty("q")
    BigInteger q;

    // PublicValue is the public-key value y used to encrypt.
    @JsonProperty("y")
    BigInteger y;

    public Key(BigInteger g, BigInteger p, BigInteger q, BigInteger y) {
        this.g = g;
        this.p = p;
        this.q = q;
        this.y = y;
    }

    public Key() {
    }

    @Override
    public String toString() {
        return "{"
                + "\"g\": " + g
                + ", \"p\": " + p
                + ", \"q\": " + q
                + ", \"y\": " + y
                + "}";
    }
}

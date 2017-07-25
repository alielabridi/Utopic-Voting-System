package dialtechnologies.utopia;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;

/**
 * Created by Ali on 6/20/2017.
 */
public class Commit {
    // A Commit is the commitment part of a Chaum-Pedersen proof of knowledge. To
    // prove knowledge of a value r, the prover first commits to a random value w
    // by sending an instance of a Commit.

    public Commit(BigInteger a, BigInteger b) {
        A = a;
        B = b;
    }

    public Commit() {
    }

    // A is the first part of a commitment: A = g^w mod p.
    @JsonProperty("A")
    BigInteger A;

    // B is the second part of a commitment: B = y^w mod p.
    @JsonProperty("B")
    BigInteger B;

    @Override
    public String toString() {
        return "{"
                + "\"A\": " + A
                + ", \"B\": " + B
                + "}";
    }
}

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

    // A is the first part of a commitment: A = g^w mod p.
    @JsonProperty("a")
    BigInteger A;

    // B is the second part of a commitment: B = y^w mod p.
    @JsonProperty("b")
    BigInteger B;

    @Override
    public String toString() {
        return "Commit{" +
                "A=" + A +
                ", B=" + B +
                '}';
    }
}

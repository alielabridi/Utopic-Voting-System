package dialtechnologies.utopia;

import java.math.BigInteger;

/**
 * Created by Ali on 6/20/2017.
 */
public class Commit {
    // A Commit is the commitment part of a Chaum-Pedersen proof of knowledge. To
    // prove knowledge of a value r, the prover first commits to a random value w
    // by sending an instance of a Commit.

    // A is the first part of a commitment: A = g^w mod p.
    BigInteger A;

    // B is the second part of a commitment: B = y^w mod p.
    BigInteger B;
}

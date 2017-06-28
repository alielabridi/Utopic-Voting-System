package dialtechnologies.utopia;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;

/**
 * Created by Ali on 6/20/2017.
 */
public class ZKProof {
    // A ZKProof is a Chaum-Pedersen zero-knowledge proof of knowledge of a random
    // value r. The interactive version of the protocol works like this:
    // 0. Prover creates commitment for random value w mod Key.ExponentPrime.
    // 1. Prover -> Verifier: commitment Commit.
    // 2. Verifier -> Prover: challenge big.Int (random value mod Key.ExponentPrime).
    // 3. Prover -> Verifier: response big.Int  (response = w + challenge * r).
    // Verifier checks the ZKProof using the algorithm in Verify.
    //
    // This is turned into a non-interactive proof (called a NIZKPOK) for a
    // DisjunctiveZKProof with n ZKProof components by constructing the challenges
    // using a hash function over the commitments: compute sha1.Sum(A_0.String() +
    // "," + B_0.String() + ... + A_n.String() + "," + B_n.String()), then split
    // this digest into n challenges mod q (just like in (n, n) secret sharing) by
    // choosing the first n-1 challenges c_0, ..., c_{n-2} as needed to fake the
    // simulated proofs, and doing a real proof for c_{n-1} = (digest - sum(c_0,
    // ..., c_{n-2})) mod q.  Under the random-oracle assumption on the hash
    // function, this makes c_{n-1} unpredictable, hence the ZKProof using c_{n-1}
    // must be real and not faked.  And this one proof must be for the actual value
    // used in the encryption, since (with overwhelming probability) the prover
    // wouldn't be able to successfully prove anything else against a random
    // challenge value.
    @JsonProperty("challenge")
    BigInteger Challenge;

    @JsonProperty("commitment")
    Commit Commitment;

    @JsonProperty("response")
    BigInteger Response;

    @Override
    public String toString() {
        return "ZKProof{" +
                "Challenge=" + Challenge +
                ", Commitment=" + Commitment +
                ", Response=" + Response +
                '}';
    }
}

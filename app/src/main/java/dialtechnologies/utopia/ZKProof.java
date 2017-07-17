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
    BigInteger challenge;

    @JsonProperty("commitment")
    Commit commitment;

    @JsonProperty("response")
    BigInteger response;

    public ZKProof(BigInteger challenge, Commit commitment, BigInteger response) {
        this.challenge = challenge;
        this.commitment = commitment;
        this.response = response;
    }

    public ZKProof() {
    }

    // Verify checks the Chaum-Pedersen zero-knowledge proof for the
    // well-formedness of a Ciphertext, given the purported plaintext and the public
    // key. Note that a ZKProof might pass verification and yet be a simulated (i.e.,
    // fake) proof in a sequence of ZKProof values that make up a
    // DisjunctiveZKProof. This is the case because ZKProof is merely the transcript
    // of a sigma protocol. And this is OK because at least one proof in a
    // DisjunctiveZKProof must be real, as checked by Verify for DisjunctiveZKProof.
    Boolean verify(CipherText cipherText, BigInteger plaintext, Key publickey){
        // g^response mod p
        BigInteger lhs = BigInteger.ONE;
        lhs = lhs.multiply(publickey.g).modPow(plaintext, publickey.p);
        // alpha^challenge mod p
        BigInteger rhs = BigInteger.ONE;
        rhs = rhs.multiply(cipherText.alpha).modPow(challenge, publickey.p);
        // A * alpha^challenge mod p
        rhs = rhs.multiply(commitment.A);
        rhs = rhs.mod(publickey.p);
        if(lhs.compareTo(rhs) != 0){
            System.out.println("The first proof verification check failed");
            return Boolean.FALSE;
        }

        // g^plaintext mod p
        BigInteger BetaOverM = BigInteger.ONE;
        BetaOverM = BetaOverM.multiply(publickey.g).modPow(plaintext, publickey.p);
        // 1/g^plaintext mod p
        BetaOverM = BetaOverM.modInverse(publickey.p);
        // beta/g^plaintext mod p
        BetaOverM = BetaOverM.multiply(cipherText.beta);
        BetaOverM = BetaOverM.mod(publickey.p);

        // y^response mod p
        lhs = BigInteger.ONE;
        lhs.multiply(publickey.y).modPow(response,publickey.p);
        // (beta/g^plaintext)^challenge mod p
        rhs = BigInteger.ONE;
        rhs = rhs.multiply(BetaOverM).modPow(challenge, publickey.p);
        // B * (beta/g^plaintext)^challenge mod p
        rhs = rhs.multiply(commitment.B);
        rhs = rhs.mod(publickey.p);
        if(lhs.compareTo(rhs) != 0){
            System.out.println("The second proof check failed");
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public String toString() {
        return "{"
                + "\"challenge\": " + challenge
                + ", \"commitment\": " + commitment
                + ", \"response\": " + response
                + "}";
    }
}

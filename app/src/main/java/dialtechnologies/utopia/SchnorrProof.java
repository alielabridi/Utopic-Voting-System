package dialtechnologies.utopia;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;

/**
 * Created by Ali on 6/20/2017.
 */
public class SchnorrProof {
    // A SchnorrProof is the proof that a Trustee knows the private key share
    // corresponding to Trustee.DecryptionFactors. The Commitment in this case is
    // only a single integer rather than a two-part commitment like in ZKProof.

    // Challenge is the value sent by the Verifier to the Prover.
    @JsonProperty("challenge")
    BigInteger Challenge;

    // Commitment is a commitment to a random value used in the proof. It
    // is sent from the Prover to the Verifier.
    @JsonProperty("commitment")
    BigInteger Commitment;

    // Response is the response to the Challenge. It is sent from the
    // Prover to the Verifier.
    @JsonProperty("response")
    BigInteger Response;

    @Override
    public String toString() {
        return "SchnorrProof{" +
                "Challenge=" + Challenge +
                ", Commitment=" + Commitment +
                ", Response=" + Response +
                '}';
    }
}

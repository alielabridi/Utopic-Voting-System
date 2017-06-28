package dialtechnologies.utopia;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * Created by Ali Elabridi on 6/20/2017.
 * all the data structures above was extracted from the original structure of Helios
 * and added some convenience data
 */

public class Trustee {
    // A Trustee represents the public information for one of the keys used to
    // tally and decrypt the election results.

    // DecryptionFactors are the partial decryptions of each of the
    // homomorphic tally results.
    @JsonProperty("decryption_factors")
    BigInteger[][] DecryptionFactors;

    // DecryptionProofs are the proofs of correct partial decryption for
    // each of the DecryptionFactors.
    @JsonProperty("decryption_proofs")
    ZKProof[][] DecryptionProofs;

    // PoK is a proof of knowledge of the private key share held by this
    // Trustee and used to create the DecryptionFactors.
    @JsonProperty("pok")
    SchnorrProof PoK;

    // PublicKey is the ElGamal public key of this Trustee.
    @JsonProperty("public_key")
    Key PublicKey;

    // PublicKeyHash is the SHA-256 hash of the JSON representation of
    // PublicKey.
    @JsonProperty("public_key_hash")
    String PublicKeyHash;

    // Uuid is the unique identifier for this Trustee.
    @JsonProperty("uuid")
    String Uuid;

    @Override
    public String toString() {
        return "Trustee{" +
                "DecryptionFactors=" + Arrays.toString(DecryptionFactors) +
                ", DecryptionProofs=" + Arrays.toString(DecryptionProofs) +
                ", PoK=" + PoK +
                ", PublicKey=" + PublicKey +
                ", PublicKeyHash='" + PublicKeyHash + '\'' +
                ", Uuid='" + Uuid + '\'' +
                '}';
    }
}

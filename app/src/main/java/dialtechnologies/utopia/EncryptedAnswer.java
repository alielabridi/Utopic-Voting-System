package dialtechnologies.utopia;


import android.text.TextUtils;
import android.text.style.TtsSpan;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.codec.binary.Hex;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Ali Elabridi on 6/19/2017.
 * all the data structures above was extracted from the original structure of Helios
 * and added some convenience data
 */
public class EncryptedAnswer {
    // An EncryptedAnswer is part of a Ballot cast by a Voter. It is the answer to
    // a given Question in an Election.

    // Choices is a list of votes for each choice in a Question. Each choice
    // is encrypted with the Election.PublicKey.
    @JsonProperty("choices")
    CipherText[] choices;

    // IndividualProofs gives a proof that each corresponding entry in
    // Choices is well formed: this means that it is either 0 or 1. So, each
    // DisjunctiveZKProof is a list of two ZKProofs, the first proving the 0
    // case, and the second proving the 1 case. One of these proofs is
    // simulated, and the other is real: see the comment for ZKProof for the
    // algorithm and the explanation.
    // A DisjunctiveZKProof is a sequence of ZKProofs for values Min through Max
    // (usually corresponding to Question.Min and Question.Max). Only one of the
    // values is a real ZKProof; the others are simulated. It is constructed using
    // the Fiat-Shamir heuristic as described in the comment for ZKProof.
    //ZKProof[] DisjunctiveZKProof;
    @JsonProperty("individual_proofs")
    ZKProof[][] individual_proofs;

    // OverallProof shows that the set of choices sum to an acceptable
    // value: one that falls between Question.Min and Question.Max. If there
    // is no Question.Max, then OverallProof will be empty and does not need
    // to be checked.
    // A DisjunctiveZKProof is a sequence of ZKProofs for values Min through Max
    // (usually corresponding to Question.Min and Question.Max). Only one of the
    // values is a real ZKProof; the others are simulated. It is constructed using
    // the Fiat-Shamir heuristic as described in the comment for ZKProof.
    //ZKProof[] DisjunctiveZKProof;
    @JsonProperty("overall_proof")
    ZKProof[] overall_proof;

    // Answer is the actual answer that is supposed to be encrypted in
    // EncryptedAnswer. This is not serialized/deserialized if not present.
    // This must only be present in a spoiled ballot because SECRECY.
    @JsonProperty("answer")
    long answer[];

    // Randomness is the actual randomness that is supposed to have been
    // used to encrypt Answer in EncryptedAnswer. This is not serialized or
    // deserialized if not present. This must only be present in a spoiled
    // ballot because SECRECY.
    @JsonProperty("randomness")
    BigInteger randomness;

    // VerifyAnswer checks the DisjunctiveZKProof values for a given
    // EncryptedAnswer. It first checks each of the EncryptedAnswer.IndividualProof
    // values to make sure it encodes either 0 or 1 (either that choice was voted
    // for or not). Then it checks the OverallProof (if there is one) to make sure
    // that the homomorphic sum of the ciphertexts is a value between min and max.
    // If there is no OverallProof, then it makes sure that this is an approval
    // question so that this last check doesn't matter.
    Boolean VerifyAnswer(int Min,int Max, String ChoiceType, Key PublicKey){
        CipherText prod = new CipherText(BigInteger.ONE, BigInteger.ONE);
        for(int i = 0 ; i < choices.length; i++){
            ZKProof[] proof = individual_proofs[i];
            // Each answer can only be 0 or 1.
            if(!VerifyDisjunctiveZKProof(proof, 0, 1, prod, PublicKey)){
                System.out.println("The proof for choice " + i + " did not pass verification");
                return Boolean.FALSE;
            }
            prod.MulCipherTexts(choices[i], PublicKey.p);
        }
        if(overall_proof.length == 0){
            if(ChoiceType != "approval"){
                System.out.println("Couldn't check a null overall proof");
                return Boolean.FALSE;
            }
        }
        else if(VerifyDisjunctiveZKProof(overall_proof, Min, Max, prod, PublicKey)){
            System.out.println("The overall proof did not pass verification-specs");
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }
    // A DisjunctiveZKProof is a sequence of ZKProofs for values Min through Max
    // (usually corresponding to Question.Min and Question.Max). Only one of the
    // values is a real ZKProof; the others are simulated. It is constructed using
    // the Fiat-Shamir heuristic as described in the comment for ZKProof.
    // Verify checks the validity of a sequence of ZKProof values that are supposed
    // to encode proofs that the ciphertext is a value in [min, max].
    Boolean VerifyDisjunctiveZKProof(ZKProof[] proof, int min, int max, CipherText prod, Key PublicKey){
        // The computed challenge is the sum mod q of all the challenges, as
        // described in the documentation of ZKProof. Since it's a sum, it must
        // start at 0.
        BigInteger computedChall = BigInteger.ZERO;
        ArrayList<String> commitVals = new ArrayList<>();
        Integer val = min;
        int total = max - min + 1;
        if(total != proof.length){
            System.out.println("Wrong number of proofs provided to VerifyDisjunctiveProof: expected " + total + "  but saw " + proof.length);
            return Boolean.FALSE;
        }
        for(int i = 0 ; i < proof.length; i++){
            BigInteger plaintext = new BigInteger(val.toString());
            if(!proof[i].verify(prod, plaintext, PublicKey)){
                System.out.println("Couldn't verify the proof for plaintext: " + plaintext.toString());
                return Boolean.FALSE;
            }
            // Accumulate the homomorphic product to sum the challenge
            // values.
            computedChall = computedChall.add(proof[i].challenge);

            commitVals.add(proof[i].commitment.A.toString());
            commitVals.add(proof[i].commitment.B.toString());
        }
        computedChall = computedChall.mod(PublicKey.q);
        // Check that the challenge was well-formed.
        String stringtohash = TextUtils.join(",", commitVals);
        byte[] hashedcommits = org.apache.commons.codec.digest.DigestUtils.sha1(stringtohash.getBytes());

        /*very risky to double check*/
        if(!Arrays.equals(computedChall.toByteArray(),hashedcommits)){
            System.out.println("The computed challenge did not match the hashed challenge");
            return Boolean.FALSE;
        }
        return Boolean.TRUE;

    }

    @Override
    public String toString() {
        return "{"
                + "\"choices\": " + Arrays.toString(choices)
                + ", \"individual_proofs\": " + Arrays.deepToString(individual_proofs)
                + ", \"overall_proof\": " + Arrays.toString(overall_proof)
                + "}";
    }
}

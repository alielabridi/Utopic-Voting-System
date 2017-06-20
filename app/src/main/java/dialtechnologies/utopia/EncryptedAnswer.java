package dialtechnologies.utopia;


import java.math.BigInteger;

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
    CipherText[] Choices;

    // IndividualProofs gives a proof that each corresponding entry in
    // Choices is well formed: this means that it is either 0 or 1. So, each
    // DisjunctiveZKProof is a list of two ZKProofs, the first proving the 0
    // case, and the second proving the 1 case. One of these proofs is
    // simulated, and the other is real: see the comment for ZKProof for the
    // algorithm and the explanation.
    DisjunctiveZKProof[] IndividualProofs;

    // OverallProof shows that the set of choices sum to an acceptable
    // value: one that falls between Question.Min and Question.Max. If there
    // is no Question.Max, then OverallProof will be empty and does not need
    // to be checked.
    DisjunctiveZKProof OverallProof;

    // Answer is the actual answer that is supposed to be encrypted in
    // EncryptedAnswer. This is not serialized/deserialized if not present.
    // This must only be present in a spoiled ballot because SECRECY.
    long Answer[];

    // Randomness is the actual randomness that is supposed to have been
    // used to encrypt Answer in EncryptedAnswer. This is not serialized or
    // deserialized if not present. This must only be present in a spoiled
    // ballot because SECRECY.
    BigInteger Randomness;

}

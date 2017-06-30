package dialtechnologies.utopia;

import java.util.Arrays;

/**
 * Created by Ali on 6/20/2017.
 */
public class DisjunctiveZKProof {
    // I may not need it as I implemented a 2d var of ZKproof
    // A DisjunctiveZKProof is a sequence of ZKProofs for values Min through Max
    // (usually corresponding to Question.Min and Question.Max). Only one of the
    // values is a real ZKProof; the others are simulated. It is constructed using
    // the Fiat-Shamir heuristic as described in the comment for ZKProof.
    ZKProof[] DisjunctiveZKProof;


}

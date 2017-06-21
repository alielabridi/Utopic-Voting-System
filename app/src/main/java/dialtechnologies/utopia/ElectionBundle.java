package dialtechnologies.utopia;

/**
 * Created by Ali on 6/21/2017.
 */
public class ElectionBundle {
    // An ElectionBundle captures all the information needed to verify a Helios
    // election. Since there is no secret information here, this data structure can
    // be exported and served as JSON from arbitrary websites as long as the
    // verifier knows the election fingerprint and trustee fingerprints for the
    // election they want to verify. The values are kept as uninterpreted bytes to
    // make sure they don't do round trips through other JSON interpreters.
    Election Election;
    Voter[] Voters;
    CastBallot Votes[];
    Trustee[] Trustees;

    byte[] ElectionData;
    byte[] Votersdata;
    byte[][] VotesData;
    byte[] TrusteesData;
}

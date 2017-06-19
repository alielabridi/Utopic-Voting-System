package dialtechnologies.utopia;


/**
 * Created by Ali Elabridi on 6/19/2017.
 * all the data structures above was extracted from the original structure of Helios
 * and added some convenience data
 */
public class Election {
    // JSON stores the original JSON for the election. This is not part of
    // the Helios JSON structure but is added here for convenience.
    byte[] JSON;

    // ElectionHash stores the SHA256 hash of the JSON value, since this is
    // needed to verify each ballot. This is not part of the original
    // Helios JSON structure but is added here for convenience.
    String ElectionHash;

    // CastURL is the url that can be used to cast ballots; casting ballots
    // is not currently supported by this go package. Ballots must still be
    // cast using the online Helios service.
    String CastURL;

    // Description is a plaintext description of the election.
    String Description;

    // FrozenAt is the date at which the election was fully specified and
    // frozen.
    String FrozenAt;

    // Name is the full name of the election.
    String Name;

    // Openreg specifies whether or not voters can be added after the
    // election has started.
    Boolean Openreg;

    // PublicKey is the ElGamal public key associated with the election.
    // This is the key used to encrypt all ballots and to create and verify
    // proofs.
    Key PublicKey;

    // Questions is the list of questions to be voted on in this election.
    Question[] Questions;

    // ShortName provides a short plaintext name for this election.
    String Shortname;

    // UseVoterAliases specifies whether or not voter names are replaced by
    // alises (like V153) that leak no information about the voter
    // identities. This can be used instead of encrypting voter names if the
    // election creators want to be sure that voter identities will remain
    // secret forever, even in the face of future cryptanalytic advances.
    Boolean useVoterAliases;

    // Uuid is a unique identifier for this election. This uuid is used in
    // the URL of the election itself: the URL of the JSON version of this
    // Election data structure is
    // https://vote.heliosvoting.org/helios/elections/<uuid>
    String Uuid;

    // VotersHash provides the hash of the list of voters.
    String VotersHash;

    String VotringEndsAt;
    String VotingStartsAt;

}

package dialtechnologies.utopia;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

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
    @JsonProperty("cast_url")
    String cast_url;

    // Description is a plaintext description of the election.
    @JsonProperty("description")
    String description;

    // FrozenAt is the date at which the election was fully specified and
    // frozen.
    @JsonProperty("frozen_at")
    String frozen_at;

    // Name is the full name of the election.
    @JsonProperty("name")
    String name;

    // Openreg specifies whether or not voters can be added after the
    // election has started.
    @JsonProperty("openreg")
    Boolean openreg;

    // PublicKey is the ElGamal public key associated with the election.
    // This is the key used to encrypt all ballots and to create and verify
    // proofs.
    @JsonProperty("public_key")
    Key public_key;

    // Questions is the list of questions to be voted on in this election.
    @JsonProperty("questions")
    Question[] questions;

    // ShortName provides a short plaintext name for this election.
    @JsonProperty("short_name")
    String short_name;

    // UseVoterAliases specifies whether or not voter names are replaced by
    // alises (like V153) that leak no information about the voter
    // identities. This can be used instead of encrypting voter names if the
    // election creators want to be sure that voter identities will remain
    // secret forever, even in the face of future cryptanalytic advances.
    @JsonProperty("use_voter_aliases")
    Boolean use_voter_aliases;

    // Uuid is a unique identifier for this election. This uuid is used in
    // the URL of the election itself: the URL of the JSON version of this
    // Election data structure is
    // https://vote.heliosvoting.org/helios/elections/<uuid>
    @JsonProperty("uuid")
    String uuid;

    // VotersHash provides the hash of the list of voters.
    @JsonProperty("voters_hash")
    String voters_hash;

    @JsonProperty("voting_ends_at")
    String voting_ends_at;

    @JsonProperty("voting_starts_at")
    String voting_starts_at;

    ArrayList<String> fingerprints = new ArrayList<>();
    CipherText[][] tallies;

    Boolean AccumulateTallies(CastBallot[] votes){
        tallies = new CipherText[questions.length][];
        for(int i = 0; i < questions.length; i++){
            tallies[i] = new CipherText[questions[i].answers.length];
            for(int j = 0 ; j < questions[i].answers.length; j++){
                // Each tally must start at 1 for the multiplicative
                // homomorphism to work.
                tallies[i][j].alpha = BigInteger.ONE;
                tallies[i][j].beta = BigInteger.ONE;
            }
        }
        /*do the parralelzation here!!!*/
        // Verify the votes and accumulate the tallies.
        Boolean resp = Boolean.TRUE;
        for(CastBallot v : votes){
            System.out.println("Verifying vote from " + v.voter_uuid);
            resp &= v.Verify(this);
            String fingerprint = android.util.Base64.encodeToString(org.apache.commons.codec.digest.DigestUtils.sha256(v.JSON), android.util.Base64.DEFAULT);
            //deleting the equal of base64
            fingerprint = fingerprint.substring(0,fingerprint.length()-1);
            //fingerprints.add(android.util.Base64.encodeToString(org.apache.commons.codec.digest.DigestUtils.sha256(v.JSON), android.util.Base64.DEFAULT));
            fingerprints.add(fingerprint);

            for(int i = 0; i < questions.length ; i++){
                for(int j = 0; j < questions[i].answers.length; j++){
                    // tally_j_k = (tally_j_k * ballot_i_j_k) mod p
                    tallies[i][j].MulCipherTexts(v.vote.answers[i].choices[j], this.public_key.p);
                }
            }
        }

        //find another way to better place this!
        if(!resp){
            System.out.println("Vote verification failed");
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public String toString() {
        return "{"
                + "\"cast_url\": \"" + cast_url + "\""
                + ", \"description\": \"" + description + "\""
                + ", \"frozen_at\": \"" + frozen_at + "\""
                + ", \"name\": \"" + name + "\""
                + ", \"openreg\": " + openreg
                + ", \"public_key\": " + public_key
                + ", \"questions\": " + ElectionBundle.convertToArray(questions)
                + ", \"short_name\": \"" + short_name + "\""
                + ", \"use_voter_aliases\": " + use_voter_aliases
                + ", \"uuid\": \"" + uuid + "\""
                + ", \"voters_hash\": \"" + voters_hash + "\""
                + ", \"voting_ends_at\": \"" + voting_ends_at + "\""
                + ", \"voting_starts_at\": \"" + voting_starts_at + "\""
                + "}";
    }
}

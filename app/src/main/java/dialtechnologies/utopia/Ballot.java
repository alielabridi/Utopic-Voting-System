package dialtechnologies.utopia;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

/**
 * Created by Ali Elabridi on 6/19/2017.
 * all the data structures above was extracted from the original structure of Helios
 * and added some convenience data
 */

public class Ballot {
    // Answers is a list of answers to the Election specified by
    // ElectionUuid and ElectionHash.
    @JsonProperty("answers")
    EncryptedAnswer[] answers;

    // ElectionHash is the SHA-256 hash of the Election specified by
    // ElectionUuid.
    @JsonProperty("election_hash")
    String election_hash;

    // ElectionUuid is the unique identifier for the Election that Answers
    // apply to.
    @JsonProperty("election_uuid")
    String election_uuid;

    @Override
    public String toString() {
        return "{"
                + "\"answers\": " + Arrays.toString(answers)
                + ", \"election_hash\": \"" + election_hash + "\""
                + ", \"election_uuid\": \"" + election_uuid + "\""
                + "}";
    }
}

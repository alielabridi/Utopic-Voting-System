package dialtechnologies.utopia;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

/**
 * Created by Ali Elabridi on 6/19/2017.
 * all the data structures above was extracted from the original structure of Helios
 * and added some convenience data
 */

public class CastBallot {
    // A CastBallot wraps a Ballot and gives more context to it. The JSON version of
    // this type can be found for a voter with uuid vuuid in election euuid at
    // https://vote.heliosvoting.org/helios/elections/<euuid>/ballots/<vuuid>/last

    // JSON is the JSON string corresponding to this type. This is not part
    // of the original JSON structure (obviously).
    byte[] JSON;

    // CastAt gives the time at which Vote was cast.
    @JsonProperty("cast_at")
    String cast_at;

    // Vote is the cast Ballot itself.
    @JsonProperty("vote")
    Ballot vote;

    // VoteHash is the SHA-256 hash of the JSON corresponding to Vote.
    @JsonProperty("vote_hash")
    String vote_hash;

    // VoterHash is the SHA-256 hash of the Voter JSON corresponding to
    // VoterUuid.
    @JsonProperty("voter_hash")
    String voter_hash;

    // VoterUuid is the unique identifier for the Voter that cast Vote.
    @JsonProperty("voter_uuid")
    String voter_uuid;

    // Verify checks the hash of the election against the hash stored in this
    // Ballot and checks the DisjunctiveZKProofs of the Answer values against the
    // Question.Min and Question.Max.
    Boolean Verify(Election election){
        if(election.ElectionHash != vote_hash){
            System.out.println("The election hash in the vote did not match the election");
            return Boolean.FALSE;
        }

        for(int i = 0 ; i < vote.answers.length; i++){
            Question q = election.questions[i];
            if(!vote.answers[i].VerifyAnswer(q.min, q.max, q.choice_type, election.public_key)){
                System.out.println("Answer " + i + " did not pass verification\n");
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public String toString() {
        return "{"
                + "\"cast_at\": \"" + cast_at + "\""
                + ", \"vote\": " + vote
                + ", \"vote_hash\": \"" + vote_hash + "\""
                + ", \"voter_hash\": \"" + voter_hash + "\""
                + ", \"voter_uuid\": \"" + voter_uuid + "\""
                + "}";
    }
}

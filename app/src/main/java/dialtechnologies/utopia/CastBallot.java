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
    String CastAt;

    // Vote is the cast Ballot itself.
    @JsonProperty("vote")
    Ballot Vote;

    // VoteHash is the SHA-256 hash of the JSON corresponding to Vote.
    @JsonProperty("vote_hash")
    String VoteHash;

    // VoterHash is the SHA-256 hash of the Voter JSON corresponding to
    // VoterUuid.
    @JsonProperty("voter_hash")
    String VoterHash;

    // VoterUuid is the unique identifier for the Voter that cast Vote.
    @JsonProperty("voter_uuid")
    String VoterUuid;

    @Override
    public String toString() {
        return "CastBallot{" +
                "JSON=" + new String(JSON) +
                ", CastAt='" + CastAt + '\'' +
                ", Vote=" + Vote +
                ", VoteHash='" + VoteHash + '\'' +
                ", VoterHash='" + VoterHash + '\'' +
                ", VoterUuid='" + VoterUuid + '\'' +
                '}';
    }
}

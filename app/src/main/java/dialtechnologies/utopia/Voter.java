package dialtechnologies.utopia;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Ali Elabridi on 6/19/2017.
 * all the data structures above was extracted from the original structure of Helios
 * and added some convenience data
 */
public class Voter {
    // Name is the name of the voter. This can be an alias like "V155", if
    // voter aliases are used in this Election.
    @JsonProperty("name")
    String Name;

    // Uuid is a unique identifier for this voter; Helios uses the Uuid as
    // a key for many of its operations. For example, given a voter uuid
    // and an election uuid, the last CastBallot for a voter can be
    // downloaded at
    // https://vote.heliosvoting.org/helios/elections/<election uuid>/ballots/<uuid>/last
    @JsonProperty("uuid")
    String Uuid;

    // VoterID is a string representing the voter. It can be a URL (like an
    // OpenID URL), or it can be an email address. Or it can be absent.
    @JsonProperty("voter_id")
    String VoterID;

    // VoterIDHash is the hash of a VoterID; this can be present even if the
    // VoterID is absent.
    @JsonProperty("voter_id_hash")
    String VoterIDHash;

    // VoterType is the type of voter, either "openid" or "email".
    @JsonProperty("voter_type")
    String VoterType;

    @Override
    public String toString() {
        return "Voter{" +
                "Name='" + Name + '\'' +
                ", Uuid='" + Uuid + '\'' +
                ", VoterID='" + VoterID + '\'' +
                ", VoterIDHash='" + VoterIDHash + '\'' +
                ", VoterType='" + VoterType + '\'' +
                '}';
    }
}

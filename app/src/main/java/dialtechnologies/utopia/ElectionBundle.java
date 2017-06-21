package dialtechnologies.utopia;

import com.fasterxml.jackson.databind.ObjectMapper;


import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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
    ArrayList<Voter> Voters = new ArrayList<>();
    CastBallot Votes[];
    Trustee[] Trustees;

    byte[] ElectionData;
    ArrayList<Byte> Votersdata = new ArrayList<>();
    byte[][] VotesData;
    byte[] TrusteesData;

    String Host = "";
    String Uuid = "";
    String ElecAddr = Host + Uuid;

    void Download(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            InputStream electStream =  new URL("elecAddr").openStream();
            byte[] ElectionData = IOUtils.toByteArray(electStream);
            electStream.close();
            Election = mapper.readValue(ElectionData, Election.class);
        } catch (IOException e) {
            System.out.println("Couldn't get the election data:");
            e.printStackTrace();
        }
        //b.Election.Init(b.ElectionData) ??

        // The helios server times out if it tries to return too many voters at
        // once.  This can be a problem for large elections (like the annual
        // IACR elections).  So, it provides a limit parameter and an after
        // parameter. The limit parameter specifies the maximum number of
        // voters to return, and the after parameter
        // specifies the last received voter.
        String after = "";
        while(True){
            Voter[] tempVoters = null;
            byte[] votersJSON = null;
            // Helios accepts "after=" as specifying the beginning of the
            // list.
            String addr = ElecAddr + "/voters/?after=" + after + "&limit=100";
            try {
                InputStream Voterstream =  new URL(addr).openStream();
                votersJSON  = IOUtils.toByteArray(Voterstream);
                Voterstream.close();
                tempVoters = mapper.readValue(votersJSON, Voter[].class);
            } catch (IOException e) {
                System.out.println("Couldn't get the voter information:");
                e.printStackTrace();
            }
            // Helios returns an empty array when there are no more voters.
            if(tempVoters.length == 0) break;

            for(int i = 0; i < tempVoters.length; i++)
                Voters.add(tempVoters[i]);
            for(int i = 0; i < votersJSON.length; i++)
                Votersdata.add(votersJSON[i]);

            after = tempVoters[tempVoters.length - 1].Uuid;
            System.out.println("Got " + tempVoters.length + " voters" );
        }
    }
}

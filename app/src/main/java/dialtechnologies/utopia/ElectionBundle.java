package dialtechnologies.utopia;

import com.fasterxml.jackson.databind.ObjectMapper;


import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
    ArrayList<Voter> Voters;
    ArrayList<CastBallot> Votes;
    Trustee[] Trustees;

    byte[] ElectionData;
    ArrayList<Byte> Votersdata;
    ArrayList<Byte> VotesData;
    byte[] TrusteesData;

    String Host = "";
    String Uuid = "";
    String ElecAddr = Host + Uuid;

    public ElectionBundle(String Host, String Uuid) {
        Voters = new ArrayList<>();
        Votes = new ArrayList<>();
        Votersdata = new ArrayList<>();
        VotesData = new ArrayList<>();
        this.Host = Host;
        this.Uuid = Uuid;
        this.ElecAddr = this.Host + this.Uuid;
        Download();
    }

    void Download(){
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(ElecAddr);
        try {
            InputStream electStream =  new URL(ElecAddr).openStream();
            ElectionData = IOUtils.toByteArray(electStream);
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
        while(true){
            Voter[] tempVoters = null;
            byte[] votersJSON = null;
            // Helios accepts "after=" as specifying the beginning of the
            // list.
            String addr = ElecAddr + "/voters/?after=" + after + "&limit=100";
            System.out.println(addr);
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
            if(tempVoters == null || tempVoters.length == 0) break;

            for(int i = 0; i < tempVoters.length; i++)
                Voters.add(tempVoters[i]);
            for(int i = 0; i < votersJSON.length; i++)
                Votersdata.add(votersJSON[i]);

            after = tempVoters[tempVoters.length - 1].uuid;
            System.out.println("Got " + tempVoters.length + " voters" );
        }
        System.out.println("There are " + Voters.size() +" voters in this election");

        for(Voter v: Voters){
            System.out.println("Getting voter " + v.uuid);
            CastBallot vote = null;
            String addr = ElecAddr + "/ballots/" + v.uuid + "/last";
            System.out.println(addr);
            byte[] jsonData = null;
            try {
                InputStream VoteStream =  new URL(addr).openStream();
                jsonData  = IOUtils.toByteArray(VoteStream);
                VoteStream.close();
                vote = mapper.readValue(jsonData, CastBallot.class);
            } catch (IOException e) {
                System.out.println("Couldn't get the last ballot cast by " + v.uuid);
                e.printStackTrace();
            }

            // Skip ballots that weren't ever cast.
            if(vote.cast_at.length() == 0) continue;

            vote.JSON = jsonData;
            Votes.add(vote);
            for(int i = 0 ; i < jsonData.length ;i++)
                VotesData.add(jsonData[i]);
        }

        System.out.println("Collected " + Votes.size() + " cast ballots for the retally");

        String addr = ElecAddr + "/trustees/";
        System.out.println(addr);
        byte [] jsonData;
        try {
            InputStream TrusteeStream =  new URL(addr).openStream();
            jsonData  = IOUtils.toByteArray(TrusteeStream);
            TrusteeStream.close();
            Trustees = mapper.readValue(jsonData, Trustee[].class);
            TrusteesData = jsonData;
        } catch (IOException e) {
            System.out.println("Couldn't get the list of trustees:");
            e.printStackTrace();
        }



    }


    static public <E> String convertToArray(List<E> list){
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for(int i=0;i<list.size();i++){
            if(list.get(i) instanceof String) builder.append("\"");
            builder.append(list.get(i).toString());
            if(list.get(i) instanceof String) builder.append("\"");
            builder.append(", ");
        }
        if(builder.length() > 1){
            builder.deleteCharAt(builder.length() - 2);
            builder.deleteCharAt(builder.length() - 1);
        }
        builder.append("]");

        return builder.toString();
    }

    static public <E> String convertToArray(E[] list){
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for(int i=0;i<list.length;i++){
            if(list[i] instanceof String) builder.append("\"");
            builder.append(list[i].toString());
            if(list[i] instanceof String) builder.append("\"");
            builder.append(", ");
        }
        if(builder.length() > 1){
            builder.deleteCharAt(builder.length() - 2);
            builder.deleteCharAt(builder.length() - 1);
        }
        builder.append("]");

        return builder.toString();
    }


}

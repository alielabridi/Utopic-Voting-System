package dialtechnologies.utopia;

import android.app.Application;
import android.os.StrictMode;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.res.TypedArrayUtils;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.support.test.*;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@SmallTest
@RunWith(AndroidJUnit4.class)
public class ApplicationTest extends ApplicationTestCase<Application> {

    ElectionBundle electionBundle;
    long[][] predictedResult;

    public ApplicationTest() {
        super(Application.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        electionBundle = new ElectionBundle("https://heliosvoting.org/helios/elections/", "5f4a8c8c-7066-11e7-8705-2234bf8d34d5");
        //add any secret key of the trustees here to start the tallying unit testing
        electionBundle.TrusteesKeys.add(new BigInteger("1529424607530842589025295693753700729476581294063623307018152227361884367919"));
        //the known answer to perform the unit test
        predictedResult = new long[2][];
        predictedResult[0] = new long[]{2, 1, 1};
        predictedResult[1] = new long[]{1, 1, 2};

        System.out.println(electionBundle.Election);
        System.out.println(electionBundle.Voters);
        System.out.println(electionBundle.Votes);
        System.out.println(Arrays.toString(electionBundle.Trustees));
    }


    @Test
    public void testParsingElection() {
        //testing election data
        //the problem is that null are most of the time coming from strings that when being null translate to "null" with
        //to_string of wrapper classes.
        //the big integer is the other way around as coming with quotation but not getting any its to_string.
        //so I delete the quotation around bigInts comming from outside and quotation coming from inside
        //this ops might be dangerous as description, name, hash... may be all numbers but for the
        // sake of just testing we will try to avoid giving such test case

        Pattern p = Pattern.compile("\"([0-9][0-9]*)\"");
        Matcher m = p.matcher(new String(electionBundle.ElectionData));
        String BigIntsWithoutQuotesFromJson = m.replaceAll("$1");
        String NullValsWithoutQuotesFromParsedJson = electionBundle.Election.toString().replaceAll("\"null\"", "null");
        System.out.println(BigIntsWithoutQuotesFromJson);
        System.out.println(NullValsWithoutQuotesFromParsedJson);
        assertEquals(BigIntsWithoutQuotesFromJson, NullValsWithoutQuotesFromParsedJson);
        System.out.println(BigIntsWithoutQuotesFromJson.equals(NullValsWithoutQuotesFromParsedJson));
    }

    @Test
    public void testParsingVotes() {
        //testing votes data
        Pattern p = Pattern.compile("\"([0-9][0-9]*)\"");
        Byte[] votesDataByte = electionBundle.VotesData.toArray(new Byte[electionBundle.VotesData.size()]);
        byte[] votesData = new byte[votesDataByte.length];
        int i = 0;
        for (Byte x : votesDataByte)
            votesData[i++] = x;
        Matcher m = p.matcher(new String(votesData));
        String BigIntsWithoutQuotesFromJson = m.replaceAll("$1");
        String NullValsWithoutQuotesFromParsedJson = electionBundle.Votes.toString().replaceAll("\"null\"", "null");
        System.out.println(BigIntsWithoutQuotesFromJson);
        System.out.println(NullValsWithoutQuotesFromParsedJson);
        //since we retrieve votes one by one and therefore the json is not an array of votes when
        //retrieve from the website, we need to delete the two first braquets
        int length = NullValsWithoutQuotesFromParsedJson.length();
        NullValsWithoutQuotesFromParsedJson = NullValsWithoutQuotesFromParsedJson.substring(1, length - 1);
        for (i = length - 100; i < NullValsWithoutQuotesFromParsedJson.length(); i++)
            System.out.print(NullValsWithoutQuotesFromParsedJson.toCharArray()[i]);
        System.out.println("");
        assertEquals(BigIntsWithoutQuotesFromJson.substring(0, BigIntsWithoutQuotesFromJson.length() - 2), NullValsWithoutQuotesFromParsedJson);
        System.out.println(BigIntsWithoutQuotesFromJson.equals(NullValsWithoutQuotesFromParsedJson));

    }

    @SmallTest
    public void testingParsingVoters() {
        //testing voters data
        Pattern p = Pattern.compile("\"([0-9][0-9]*)\"");
        Byte[] votersDataByte = electionBundle.Votersdata.toArray(new Byte[electionBundle.Votersdata.size()]);
        byte[] votersData = new byte[votersDataByte.length];
        int i = 0;
        for (Byte x : votersDataByte)
            votersData[i++] = x;
        Matcher m = p.matcher(new String(votersData));
        String BigIntsWithoutQuotesFromJson = m.replaceAll("$1");
        String NullValsWithoutQuotesFromParsedJson = electionBundle.Voters.toString().replaceAll("\"null\"", "null");
        System.out.println(BigIntsWithoutQuotesFromJson);
        System.out.println(NullValsWithoutQuotesFromParsedJson);
        assertEquals(BigIntsWithoutQuotesFromJson, NullValsWithoutQuotesFromParsedJson);
        System.out.println(BigIntsWithoutQuotesFromJson.equals(NullValsWithoutQuotesFromParsedJson));
    }

    @SmallTest
    public void testingParsingTrustees() {
        //testing trustee data
        Pattern p = Pattern.compile("\"([0-9][0-9]*)\"");
        Matcher m = p.matcher(new String(electionBundle.TrusteesData));
        String BigIntsWithoutQuotesFromJson = m.replaceAll("$1");
        String NullValsWithoutQuotesFromParsedJson = Arrays.toString(electionBundle.Trustees).replaceAll("\"null\"", "null");
        System.out.println(BigIntsWithoutQuotesFromJson);
        System.out.println(NullValsWithoutQuotesFromParsedJson);
        assertEquals(BigIntsWithoutQuotesFromJson, NullValsWithoutQuotesFromParsedJson);
        System.out.println(BigIntsWithoutQuotesFromJson.equals(NullValsWithoutQuotesFromParsedJson));
    }

    @Test
    public void testingElectionTallyingandRetallying() {
        CastBallot[] votes = new CastBallot[electionBundle.Votes.size()];
        votes = electionBundle.Votes.toArray(votes);
        BigInteger[] trusteeskeys = new BigInteger[electionBundle.TrusteesKeys.size()];
        trusteeskeys = electionBundle.TrusteesKeys.toArray(trusteeskeys);
        long[][] answers = electionBundle.Election.Tally(votes, electionBundle.Trustees, trusteeskeys);
        System.out.println("the result of the election:");
        for (int i = 0; i < answers.length; i++)
            for (int j = 0; j < answers[i].length; j++)
                System.out.println("Question " + i + " Answer " + j + "Value" + answers[i][j]);
        Boolean matchingResult = Boolean.TRUE;
        for (int i = 0; i < answers.length; i++)
            for (int j = 0; j < answers[i].length; j++)
                if (answers[i][j] != predictedResult[i][j])
                    matchingResult = Boolean.FALSE;
        //testing the retallying of the decrypt factors and proofs generated by the actual tallying
        //we need to communicate these data from every trustee to the ledger
        Boolean retallyingResult = electionBundle.Election.Retally(votes, predictedResult, electionBundle.Trustees);
        assertTrue(matchingResult && retallyingResult);
    }

    public void testingElectionRetallying() {
        CastBallot[] votes = new CastBallot[electionBundle.Votes.size()];
        votes = electionBundle.Votes.toArray(votes);
        assertTrue(electionBundle.Election.Retally(votes, predictedResult, electionBundle.Trustees));
    }
}
package dialtechnologies.utopia;

import android.app.Application;
import android.os.StrictMode;
import android.support.v4.content.res.TypedArrayUtils;
import android.test.ApplicationTestCase;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        System.out.println("lets see");
        ElectionBundle electionBundle = new ElectionBundle("https://vote.heliosvoting.org/helios/elections/","c2786390-5056-11e7-9e2a-c65269258371");
        //System.out.println("length" + electionBundle.ElectionData.length);
        System.out.println(electionBundle.Election);
        System.out.println(electionBundle.Voters);
        System.out.println(electionBundle.Votes);
        System.out.println(Arrays.toString(electionBundle.Trustees));
        //the problem is that null are most of the time coming from strings that when being null translate to "null" with
        //to_string of wrapper classes.
        //the big integer is the other way around as coming with quotation but not getting any its to_string.
        //so I delete the quotation around bigInts comming from outside and quotation coming from inside
        //this ops might be dangerous as description, name, hash... may be all numbers but for the
        // sake of just testing we will try to avoid giving such test case

        //testing election data
        Pattern p = Pattern.compile("\"([0-9][0-9]*)\"");
        Matcher m = p.matcher(new String(electionBundle.ElectionData));
        String BigIntsWithoutQuotesFromJson = m.replaceAll("$1");
        String NullValsWithoutQuotesFromParsedJson = electionBundle.Election.toString().replaceAll("\"null\"","null");
        System.out.println(BigIntsWithoutQuotesFromJson);
        System.out.println(NullValsWithoutQuotesFromParsedJson);
        assertEquals(BigIntsWithoutQuotesFromJson, NullValsWithoutQuotesFromParsedJson);
        System.out.println(BigIntsWithoutQuotesFromJson.equals(NullValsWithoutQuotesFromParsedJson));

        //testing votes data
        p = Pattern.compile("\"([0-9][0-9]*)\"");
        Byte[] votesDataByte = electionBundle.VotesData.toArray(new Byte[electionBundle.VotesData.size()]);
        byte[] votesData = new byte[votesDataByte.length];
        int i = 0;
        for(Byte x : votesDataByte)
            votesData[i++] = x;
        m = p.matcher(new String(votesData));
        BigIntsWithoutQuotesFromJson = m.replaceAll("$1");
        NullValsWithoutQuotesFromParsedJson = electionBundle.Votes.toString().replaceAll("\"null\"", "null");
        System.out.println(BigIntsWithoutQuotesFromJson);
        System.out.println(NullValsWithoutQuotesFromParsedJson);
        assertEquals(BigIntsWithoutQuotesFromJson, NullValsWithoutQuotesFromParsedJson);
        System.out.println(BigIntsWithoutQuotesFromJson.equals(NullValsWithoutQuotesFromParsedJson));


        //testing voters data
        p = Pattern.compile("\"([0-9][0-9]*)\"");
        Byte[] votersDataByte = electionBundle.Votersdata.toArray(new Byte[electionBundle.Votersdata.size()]);
        byte[] votersData = new byte[votersDataByte.length];
        i = 0;
        for(Byte x : votersDataByte)
            votersData[i++] = x;
        m = p.matcher(new String(votersData));
        BigIntsWithoutQuotesFromJson = m.replaceAll("$1");
        NullValsWithoutQuotesFromParsedJson = electionBundle.Voters.toString().replaceAll("\"null\"", "null");
        System.out.println(BigIntsWithoutQuotesFromJson);
        System.out.println(NullValsWithoutQuotesFromParsedJson);
        assertEquals(BigIntsWithoutQuotesFromJson, NullValsWithoutQuotesFromParsedJson);
        System.out.println(BigIntsWithoutQuotesFromJson.equals(NullValsWithoutQuotesFromParsedJson));


        //testing trustee data
        p = Pattern.compile("\"([0-9][0-9]*)\"");
        m = p.matcher(new String(electionBundle.TrusteesData));
        BigIntsWithoutQuotesFromJson = m.replaceAll("$1");
        NullValsWithoutQuotesFromParsedJson = Arrays.toString(electionBundle.Trustees).replaceAll("\"null\"", "null");
        System.out.println(BigIntsWithoutQuotesFromJson);
        System.out.println(NullValsWithoutQuotesFromParsedJson);
        assertEquals(BigIntsWithoutQuotesFromJson, NullValsWithoutQuotesFromParsedJson);
        System.out.println(BigIntsWithoutQuotesFromJson.equals(NullValsWithoutQuotesFromParsedJson));
    }

}
package dialtechnologies.utopia;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

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

    // AccumulateTallies combines the ballots homomorphically for each question and answer
    // to get an encrypted tally for each. It also compute the ballot tracking numbers for
    // each of the votes.
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

    // NewPartialDecryptionProof produces a proof of knowledge of a value x for
    // a DDH tuple (g, g^x, g^R, g^xR).  g^R is the tallied ciphertext alpha value,
    // g^xR is the partial decryption, and g^x is the public key of the trustee.
    public ZKProof NewPartialDecryptionProof(CipherText cipherText, BigInteger decFactor, BigInteger secret, Key key){
        // Choose a random value w as the first message.
        int w = new Random().nextInt(key.q.intValue());
        // Commit to w with A = g^w, B = alpha^w.
        BigInteger A = key.q.modPow(new BigInteger(Integer.toString(w)), key.p);
        BigInteger B = key.q.modPow(cipherText.alpha, key.p);
        Commit com = new Commit(A,B);

        // Compute the challenge using SHA1
        String stringToHash = com.A.toString() + "," + com.B.toString();
        byte[] hashedChall = org.apache.commons.codec.digest.DigestUtils.sha1(stringToHash.getBytes());
        BigInteger chall = new BigInteger(hashedChall);

        // response = w + challenge * secret
        BigInteger resp = chall.multiply(secret);
        resp = resp.add(new BigInteger(Integer.toString(w)));
        resp = resp.mod(key.q);
        return new ZKProof(chall, com, resp);

    }

    // Tally computes the tally of an election and returns the result.
    // In the process, it generates partial decryption proofs for each of
    // the partial decryptions computed by the trustee.
    public long [][] Tally(CastBallot[] votes, Trustee[] trustees, BigInteger[] trusteeSecrets){
        AccumulateTallies(votes);
        if(fingerprints.size() == 0){
            System.out.println("Couldn't tally the votes");
            return null;
        }
        for(int t = 0; t < trustees.length; t++){
            BigInteger[][] df = new BigInteger[questions.length][];
            ZKProof[][] dp = new ZKProof[questions.length][];
            for(int i = 0 ; i < questions.length; i++){
                df[i] = new BigInteger[questions[i].answers.length];
                dp[i] = new ZKProof[questions[i]. answers.length];
                for(int j = 0 ; j < questions[i].answers.length; j++){
                    df[i][j] = BigInteger.ONE.multiply(tallies[i][j].alpha).modPow(trusteeSecrets[t], trustees[t].public_key.p);
                    dp[i][j] = NewPartialDecryptionProof(tallies[i][j],df[i][j],trusteeSecrets[t],trustees[t].public_key);
                }
            }
            trustees[t].decryption_factors = df;
            trustees[t].decryption_proofs = dp;
        }

        // For each question and each answer, reassemble the tally and search for its value.
        // Then put this in the results.
        int maxValue = votes.length;
        long result [][] = new long[questions.length][];
        for(int i = 0 ; i < questions.length; i++){
            result[i] = new long[questions[i].answers.length];
            for(int j = 0 ; j < questions[i].answers.length;j++){
                BigInteger alpha = BigInteger.ONE;
                for(int t = 0 ; t < trustees.length; t++){
                    alpha = alpha.multiply(trustees[t].decryption_factors[i][j]);
                    alpha = alpha.mod(trustees[t].public_key.p);
                }
                BigInteger beta = alpha.modInverse(public_key.p);
                beta = beta.multiply(tallies[i][j].beta);
                beta = beta.mod(public_key.p);

                // This decrypted value can be anything between g^0 and g^maxValue.
                // Try all values until we find it.
                BigInteger temp;
                BigInteger val;
                long v;
                for(v = 0 ; v <= maxValue; v++){
                    val = new BigInteger(Long.toString(v));
                    temp = public_key.g.modPow(val, public_key.p);
                    if(temp.compareTo(beta) == 0){
                        result[i][j] = v;
                        break;
                    }
                }
                if(v > maxValue){
                    System.out.println("Couldn't decrypt part of the tally value (" + i + ", " + j+ ")");
                    return null;
                }
            }
        }

        return result;
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

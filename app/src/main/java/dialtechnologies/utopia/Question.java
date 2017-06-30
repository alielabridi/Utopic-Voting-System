package dialtechnologies.utopia;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

/**
 * Created by Ali Elabridi on 6/19/2017.
 * all the data structures above was extracted from the original structure of Helios
 * and added some convenience data
 */

public class Question {
    // AnswerUrls can provide urls with information about answers. These
    // urls can be empty.
    @JsonProperty("answer_urls")
    String[] answer_urls;

    // Answers is the list of answer choices for this question.
    @JsonProperty("answers")
    String[] answers;

    // ChoiceType specifies the possible ways to evaluate responses. It can
    // currently only be set to 'approval'.
    @JsonProperty("choice_type")
    String choice_type;

    // Maximum specifies the maximum value of a vote for this Question. If
    // Max is not specified in the JSON structure, then there will be no
    // OverallProof, since any number of values is possible, up to the
    // total number of answers. This can be detected by looking at
    // OverallProof in the given Ballot.
    @JsonProperty("max")
    int max;

    // Min specifies the minimum number of answers. This can be as low as
    // 0.
    @JsonProperty("min")
    int min;

    // Question gives the actual question to answer
    @JsonProperty("question")
    String question;

    // ResultType specifies the way in which results should be calculated:
    // 'absolute' or 'relative'.
    @JsonProperty("result_type")
    String result_type;

    // ShortName gives a short representation of the Question.
    @JsonProperty("short_name")
    String short_name;

    // TallyType specifies the kind of tally to perform. The only valid
    // value here is 'homomorphic'.
    @JsonProperty("tally_type")
    String tally_type;

    @Override
    public String toString() {
        return "{"
                + "\"answer_urls\": " + ElectionBundle.convertToArray(answer_urls)
                + ", \"answers\": " + ElectionBundle.convertToArray(answers)
                + ", \"choice_type\": \"" + choice_type + "\""
                + ", \"max\": " + max
                + ", \"min\": " + min
                + ", \"question\": \"" + question + "\""
                + ", \"result_type\": \"" + result_type + "\""
                + ", \"short_name\": \"" + short_name + "\""
                + ", \"tally_type\": \"" + tally_type + "\""
                + "}";
    }
}

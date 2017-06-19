package dialtechnologies.utopia;

/**
 * Created by Ali Elabridi on 6/19/2017.
 * all the data structures above was extracted from the original structure of Helios
 * and added some convenience data
 */

public class Question {
    // AnswerUrls can provide urls with information about answers. These
    // urls can be empty.
    String[] AnswerUrls;

    // Answers is the list of answer choices for this question.
    String[] Answers;

    // ChoiceType specifies the possible ways to evaluate responses. It can
    // currently only be set to 'approval'.
    String ChoiceType;

    // Maximum specifies the maximum value of a vote for this Question. If
    // Max is not specified in the JSON structure, then there will be no
    // OverallProof, since any number of values is possible, up to the
    // total number of answers. This can be detected by looking at
    // OverallProof in the given Ballot.
    int Max;

    // Min specifies the minimum number of answers. This can be as low as
    // 0.
    int Min;

    // Question gives the actual question to answer
    String Question;

    // ResultType specifies the way in which results should be calculated:
    // 'absolute' or 'relative'.
    String ResultType;

    // ShortName gives a short representation of the Question.
    String ShortName;

    // TallyType specifies the kind of tally to perform. The only valid
    // value here is 'homomorphic'.
    String TallyType;
}
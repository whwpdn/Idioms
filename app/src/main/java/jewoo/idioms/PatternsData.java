package jewoo.idioms;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jewoo on 2016. 9. 11..
 */
public class PatternsData {

    private String mPattern;
    private String mMeaning;



    private List<String> mQuestion;
    private List<String> mAnswer;
    private List<String> mHint;

    private ArrayList<String> mGroupList = null;
    private ArrayList<ArrayList<String>> mChildList = null;


    public void setmMeaning(String mMeaning) {
        this.mMeaning = mMeaning;
    }

    public String getmMeaning() {
        return mMeaning;
    }

    public String getmPattern() {
        return mPattern;
    }

    public void setmPattern(String mPattern) {
        this.mPattern = mPattern;
    }

    public void setmQuestion(List<String> mQuestion) {
        this.mQuestion = mQuestion;
    }
    public List<String> getmQuestion() {
        return mQuestion;
    }

    public List<String> getmAnswer() {
        return mAnswer;
    }

    public void setmAnswer(List<String> mAnswer) {
        this.mAnswer = mAnswer;
    }

    public List<String> getmHint() {
        return mHint;
    }

    public void setmHint(List<String> mHint) {
        this.mHint = mHint;
    }

    public PatternsData(String strPattern,String strMeaning, List<String> strQuestion, List<String> listAnswer, List<String> listHint) {
        this.mPattern = strPattern;
        this.mMeaning = strMeaning;
        this.mQuestion = strQuestion;
        this.mAnswer = listAnswer;
        this.mHint = listHint;
    }

}

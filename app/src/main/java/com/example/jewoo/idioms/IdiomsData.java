package com.example.jewoo.idioms;

/**
 * Created by jewoo on 2016. 7. 8..
 */
public class IdiomsData {
    private int mId;
    private String mQuestion;
    private String mAnswer;

    public IdiomsData(int mId, String mQuestion, String mAnswer) {
        this.mId = mId;
        this.mQuestion = mQuestion;
        this.mAnswer = mAnswer;
    }

    public int getmId() {

        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmQuestion() {
        return mQuestion;
    }

    public String getmAnswer() {
        return mAnswer;
    }
//    public void setmQuestion(String mQuestion) {
//        this.mQuestion = mQuestion;
//    }



//    public void setmAnswer(String mAnswer) {
//        this.mAnswer = mAnswer;
//    }




}

package com.dp.uheadmaster.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.adapters.QuestionAnswerAdapter;
import com.dp.uheadmaster.adapters.QuizAnswersAdapter;
import com.dp.uheadmaster.holders.QuizAnswersHolder;
import com.dp.uheadmaster.interfaces.QuizInterface;
import com.dp.uheadmaster.models.Content;
import com.dp.uheadmaster.models.QuizAnswer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by DELL on 22/10/2017.
 */

public class StartQuizAct extends AppCompatActivity {

    private String lecturename;
    private String quizename;
    private int questionsnum;
    private ArrayList<Content>contents;

    private int questionnum=1;
    private int answerNum=-1;
    @BindView(R.id.tv_section_title)
    TextView tvLectureName;

    @BindView(R.id.tv_quiz_title)
    TextView tvQuizeTitle;

    @BindView(R.id.tv_question_numbers)
    TextView tvQuestionsnum;

    @BindView(R.id.tv_skip_quiz)
    TextView tvSkipQuiz;

    @BindView(R.id.btn_start_quiz)
    Button btnStartquiz;

    @BindView(R.id.layout_start_quiz)
    LinearLayout startQuizLayout;

    @BindView(R.id.layout_questios)
    RelativeLayout questionslayout;

    ////////////////////////////////////////////////////////////////////

    @BindView(R.id.tv_question_number)
    TextView tvQuestionid;


    @BindView(R.id.tv_question_text)
    TextView tvQuestion;

    @BindView(R.id.recycler_answers)
    RecyclerView recyclerViewAnswer;


    @BindView(R.id.tv_fotter_skip)
    TextView tvFotterSkip;


    @BindView(R.id.tv_fotter_question_number)
    TextView tvFotterQuestionNum;


    @BindView(R.id.tv_fotter_question_count)
    TextView tvFotterQuestionCount;

    @BindView(R.id.btn_next_question)
    Button btnNextQuestion;


    @BindView(R.id.btn_check_answer)
    Button btnCheckAnswer;


    @BindView(R.id.layout_answer)
    LinearLayout layoutanswer;

    @BindView(R.id.tv_error)
    TextView tvError;

    private ArrayList<QuizAnswer> answerList;
    private int prevPos=-1;
private QuizAnswersAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_quiz_layout);
        ButterKnife.bind(this);

        getData();
        setQuizData();

    }


    @OnClick(R.id.btn_check_answer)
    public void checkAnswer(){

       // Toast.makeText(this, "answernum"+answerNum, Toast.LENGTH_SHORT).show();
        if(answerNum!=-1) {
            if (contents.get(questionnum - 1).getAnswers().get(answerNum).isChecked()) {
                layoutanswer.setVisibility(View.VISIBLE);
                layoutanswer.setBackgroundColor(getResources().getColor(R.color.quiz_answer_color_correct));
                tvError.setText("Good Job !");
                btnCheckAnswer.setVisibility(View.GONE);
                if (questionnum != contents.size())
                    btnNextQuestion.setVisibility(View.VISIBLE);

            } else {
                layoutanswer.setVisibility(View.VISIBLE);
                layoutanswer.setBackgroundColor(getResources().getColor(R.color.quiz_answer_color_error));
                tvError.setText("Incorrect answer.please try again");
            }
        }

    }


    @OnClick(R.id.btn_next_question)
    public void nextAction(){
        questionnum++;
        setQuizData();

        QuizAnswersHolder quizAnswersHolder=(QuizAnswersHolder) recyclerViewAnswer.findViewHolderForAdapterPosition(prevPos);
        //  Toast.makeText(StartQuizAct.this, "Prev:"+prevpos, Toast.LENGTH_LONG).show();
        if(quizAnswersHolder!=null) {
            quizAnswersHolder.itemView.setBackground(getResources().getDrawable(R.drawable.layout_quiz_answer_bg));
            quizAnswersHolder.tvanswers.setTextColor(getResources().getColor(R.color.qize_answer_color));
            quizAnswersHolder.ivCircle.setBackground(getResources().getDrawable(R.drawable.ic_circle_empty));
        }
        prevPos=-1;
        answerNum=-1;
        btnNextQuestion.setVisibility(View.GONE);
        btnCheckAnswer.setVisibility(View.VISIBLE);
    }

    public void getData(){
        quizename=getIntent().getStringExtra("quiz");
        lecturename=getIntent().getStringExtra("lecture");
        questionsnum=getIntent().getIntExtra("questions_num",0);
        contents= (ArrayList<Content>) getIntent().getSerializableExtra("Answers");
        tvLectureName.setText(lecturename+" | Quiz");
        tvQuizeTitle.setText(quizename);
        tvQuestionsnum.setText(questionsnum +" Questions");

    }

    public void setQuizData(){
        tvQuestionid.setText("Question "+questionnum+":");
        tvQuestion.setText(contents.get(questionnum-1).getName());
        tvFotterQuestionNum.setText(""+questionnum);
        tvFotterQuestionCount.setText(""+contents.size());
        answerList=contents.get(questionnum - 1).getAnswers();
      //  Toast.makeText(this, " "+answerList.get(0).getAnswer(), Toast.LENGTH_SHORT).show();
        notifyAdapter();
        layoutanswer.setVisibility(View.GONE);
        if(questionnum==contents.size())
            btnNextQuestion.setVisibility(View.GONE);
    }

    public void notifyAdapter(){
        adapter=new QuizAnswersAdapter(getApplicationContext(),answerList, new QuizInterface() {
            @Override
            public void getAnswerposition(int position) {

                answerNum=position;
                QuizAnswersHolder quizAnswersHolder=(QuizAnswersHolder) recyclerViewAnswer.findViewHolderForAdapterPosition(prevPos);
                //  Toast.makeText(StartQuizAct.this, "Prev:"+prevpos, Toast.LENGTH_LONG).show();
                if(quizAnswersHolder!=null  && position!=prevPos) {
                    quizAnswersHolder.itemView.setBackground(getResources().getDrawable(R.drawable.layout_quiz_answer_bg));
                    quizAnswersHolder.tvanswers.setTextColor(getResources().getColor(R.color.qize_answer_color));
                    quizAnswersHolder.ivCircle.setBackground(getResources().getDrawable(R.drawable.ic_circle_empty));
                }

                prevPos=position;


            }
        });

        recyclerViewAnswer.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAnswer.setItemAnimator(new DefaultItemAnimator());
        recyclerViewAnswer.setAdapter(adapter);
    }

    @OnClick({R.id.tv_skip_quiz,R.id.tv_fotter_skip})
    public void skipQuiz(){
        Intent i=new Intent(this,CourseLearn.class);
        startActivity(i);
        finish();
    }

    @OnClick(R.id.btn_start_quiz)
    public void startQuiz(){
        startQuizLayout.setVisibility(View.GONE);
        questionslayout.setVisibility(View.VISIBLE);

    }
}

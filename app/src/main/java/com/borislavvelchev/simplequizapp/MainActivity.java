package com.borislavvelchev.simplequizapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    Button trueButton;
    Button falseButton;
    TextView questionTextView;
    TextView scoreTextView;
    ProgressBar progressBar;
    int index;
    int score;
    int question;

    private TrueFalse[] questionBank = new TrueFalse[] {
            new TrueFalse(R.string.question_1, true),
            new TrueFalse(R.string.question_2, true),
            new TrueFalse(R.string.question_3, true),
            new TrueFalse(R.string.question_4, true),
            new TrueFalse(R.string.question_5, false),
            new TrueFalse(R.string.question_6, true),
            new TrueFalse(R.string.question_7, true),
    };

    final int PROGRESS_BAR_INCREMENT = (int) Math.ceil(100.0 / questionBank.length);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            score = savedInstanceState.getInt("ScoreKey");
            index = savedInstanceState.getInt("IndexKey");
        } else {
            score = 0;
            index = 0;
        }

        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        questionTextView = findViewById(R.id.question_text_view);
        scoreTextView = findViewById(R.id.score);
        progressBar = findViewById(R.id.progress_bar);

        question = questionBank[index].getQuestionID();
        questionTextView.setText(question);
        scoreTextView.setText("Score " + this.score + "/" + questionBank.length);

        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Quizzap", "Button pressed!");
                Toast.makeText(getApplicationContext(), "True pressed!", Toast.LENGTH_SHORT).show();
                checkAnswer(true);
                updateQuestion();
            }
        });

        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(), "False pressed!", Toast.LENGTH_SHORT);
                toast.show();
                checkAnswer(false);
                updateQuestion();
            }
        });
    }

    private void updateQuestion() {
        index = (index + 1) % questionBank.length; // Array OutOfBoundsException prevented.

        if (index == 0) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Game Over");
            alert.setCancelable(false);
            alert.setMessage("You scored " + score + " points");
            alert.setPositiveButton("Close Application", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            }).show();
        }
        question = questionBank[index].getQuestionID();
        questionTextView.setText(question);
        progressBar.incrementProgressBy(PROGRESS_BAR_INCREMENT);
        scoreTextView.setText("Score " + this.score + "/" + questionBank.length);
    }

    private void checkAnswer(boolean userSelection) {
       boolean correctAnswer = questionBank[index].isAnswer();

       if (userSelection == correctAnswer) {
           Toast.makeText(getApplicationContext(), R.string.correct_toast, Toast.LENGTH_SHORT).show();
           score += 1;
       } else {
           Toast.makeText(getApplicationContext(), R.string.incorrect_toast, Toast.LENGTH_SHORT).show();
       }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("ScoreKey", score);
        outState.putInt("IndexKey", index);
    }
}

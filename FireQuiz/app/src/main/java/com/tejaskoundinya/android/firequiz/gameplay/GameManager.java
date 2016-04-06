package com.tejaskoundinya.android.firequiz.gameplay;

import com.tejaskoundinya.android.firequiz.models.OptionModel;
import com.tejaskoundinya.android.firequiz.models.QuestionModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Tejas Koundinya on 09-01-2016.
 */
public class GameManager {

    private String[] questions;
    private String[][] options;
    private int[] answers;

    public GameManager() {
        questions = new String[]{"In which country were the first Olympic Games held?", "Which popular american TV show holds the record for most viewers for a finale?", "Where did Chess originate from?", "Which version of Android did not support phones?"};
        options = new String[][]{{"Greece", "Spain", "USA", "Russia"},
                {"Cheers", "Friends", "M*A*S*H", "Seinfeld"},
                {"China", "India", "UK", "Germany"},
                {"Donut", "Eclair", "Gingerbread", "Honeycomb"}};
        // 1 - first option, 4 - fourth option
        answers = new int[]{1, 3, 2, 4};
    }

    public QuestionModel generateRandomQuestion(long previousId) {

        Random random = new Random();
        int index = random.nextInt(questions.length);
        while(index == previousId) {
            index = random.nextInt(questions.length);
        }

        QuestionModel testQuestion = new QuestionModel();
        testQuestion.setId(index);
        testQuestion.setQuestion(questions[index]);

        List<OptionModel> options = new ArrayList<OptionModel>();

        OptionModel option1 = new OptionModel();
        option1.setOptionId(1);
        option1.setOptionString(this.options[index][0]);
        option1.setCorrectAnswer(answers[index] == 1);

        OptionModel option2 = new OptionModel();
        option2.setOptionId(2);
        option2.setOptionString(this.options[index][1]);
        option2.setCorrectAnswer(answers[index] == 2);

        OptionModel option3 = new OptionModel();
        option3.setOptionId(3);
        option3.setOptionString(this.options[index][2]);
        option3.setCorrectAnswer(answers[index] == 3);

        OptionModel option4 = new OptionModel();
        option4.setOptionId(4);
        option4.setOptionString(this.options[index][3]);
        option4.setCorrectAnswer(answers[index] == 4);

        options.add(option1);
        options.add(option2);
        options.add(option3);
        options.add(option4);

        testQuestion.setOptions(options);

        return testQuestion;
    }

}

package reader;

import java.util.ArrayList;

class QuestionHandler
{
    private static int counter = 0;
    private static ArrayList<String[]> questions = new ArrayList<>();
    private static String[] current;

    static void nextCard()
    {
        Main.setText("");
        if(counter < questions.size())
        {
            readQuestion();
            counter++;
        }
        else
        {
            Main.setText("That was the last question!");
        }
    }
    static void backCard()
    {
        Main.setText("");
        if(counter > 0)
        {
            counter--;
            readQuestion();
        }
        else
        {
            Main.setText("That was the first question!");
        }
    }
    static void buzz()
    {
        Main.appendText("(#)");     //Temp
        Main.setAnswer(current[1]);
    }
    private static void readQuestion()
    {
        current = questions.get(counter);
        Main.setText(current[0]);   //Temp
    }
    static ArrayList<String[]> getQuestions()
    {
        return questions;
    }
    static void setQuestions(ArrayList<String[]> arr)
    {
        questions = arr;
    }
}

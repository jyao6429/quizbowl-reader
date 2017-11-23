package reader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.ArrayList;

public class Main
{
    private JFrame mainFrame;       //GUI Elements
    private JPanel mainPanel;
    private JPanel sidePanel;
    private JPanel bottomPanel;
    private static JTextArea text;
    private static JTextArea answer;
    private static JScrollPane scroller;

    public static void main(String[] args)
    {
        Main start = new Main();        //New Main object, calls go()
        start.go();
    }
    private void go()       //Builds GUI
    {
        mainFrame = new JFrame("Quizbowl Reader");      //Creates a new frame

        text = new JTextArea(17,50);		//Sets the size of the text box
        text.setLineWrap(true);				//Sets line wrap
        text.setWrapStyleWord(true);		//Sets line wrap by word instead of by letter
        text.setEditable(false);            //You can't edit the text panel

        scroller = new JScrollPane(text);	//New scroll pane, so that the text box can scroll
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);		//Always have a vertical scrollbar
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);	//Never have a horizonal scrollbar

        answer = new JTextArea(2, 50);      //Text box for the answer
        answer.setLineWrap(true);
        answer.setWrapStyleWord(true);
        answer.setEditable(false);

        JTextPane divider = new JTextPane();
        divider.setText("Answer:");
        divider.setEditable(false);

        mainPanel = new JPanel();		//New JPanel, add the scrolling text box to it
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(scroller);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(divider);
        mainPanel.add(answer);
        mainFrame.getContentPane().add(BorderLayout.CENTER, mainPanel);

        JButton openButton = new JButton("Open");
        JButton nextButton = new JButton("Next (N)");
        JButton backButton = new JButton("Back (B)");
        JButton saveButton = new JButton("Save");
        JButton buzzButton = new JButton("Buzz (Space)");

        buzzButton.setPreferredSize(new Dimension(999999, 25));     //I have no idea why this works, but it does. I guess a we won't hit that high of a resolution in any time soon

        openButton.addActionListener(new OpenButtonListener());			//Add all Listeners to the buttons
        nextButton.addActionListener(new NextListener());
        mainPanel.addKeyListener(new NextListener());
        backButton.addActionListener(new BackListener());
        mainPanel.addKeyListener(new BackListener());
        saveButton.addActionListener(new SaveButtonListener());
        buzzButton.addActionListener(new BuzzListener());
        mainPanel.addKeyListener(new BuzzListener());

        sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.add(openButton);
        sidePanel.add(saveButton);

        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.add(backButton);
        bottomPanel.add(Box.createRigidArea(new Dimension(20,0)));
        bottomPanel.add(buzzButton);
        bottomPanel.add(Box.createRigidArea(new Dimension(20,0)));
        bottomPanel.add(nextButton);

        mainFrame.getContentPane().add(BorderLayout.EAST, sidePanel);
        mainFrame.getContentPane().add(BorderLayout.SOUTH, bottomPanel);

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		//Exits Java program if you close the window
        mainFrame.setSize(660, 340);		//Set mainFrame dimensions
        mainFrame.setVisible(true);			//Make it visible
    }
    static void appendText(String s)
    {
        text.append(s);
    }
    static void setText(String s)
    {
        text.setText(s);
    }
    static void setAnswer(String s)
    {
        answer.setText(s);
    }
    private class OpenButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent ev)
        {
            try
            {
                ArrayList<String[]> questions = new ArrayList<>();
                File file;
                JFileChooser fileOpen = new JFileChooser();
                fileOpen.showOpenDialog(mainFrame);
                file = fileOpen.getSelectedFile();

                BufferedReader reader = new BufferedReader(new FileReader(file));
                String input;

                while((input = reader.readLine()) != null)
                {
                    questions.add(input.split("\t"));
                }
                QuestionHandler.setQuestions(questions);
            }
            catch(IOException ex)
            {
                System.out.println("Could not open");
                ex.printStackTrace();
            }
            catch(Exception ex)
            {
                System.out.println("Other error");
                ex.printStackTrace();
            }
        }
    }
    private class NextListener implements ActionListener, KeyListener
    {
        private boolean pressed = false;
        public void actionPerformed(ActionEvent ev)
        {
            QuestionHandler.nextCard();
        }
        public void keyTyped(KeyEvent e)
        {
        }
        public void keyPressed(KeyEvent e)
        {
            int key = e.getKeyCode();
            if(key == KeyEvent.VK_N)
            {
                pressed = true;
            }
        }
        public void keyReleased(KeyEvent e)
        {
            int key = e.getKeyCode();

            if(key == KeyEvent.VK_N && pressed)
            {
                pressed = false;
                QuestionHandler.nextCard();
            }
        }

    }
    private class BackListener implements ActionListener, KeyListener
    {
        private boolean pressed = false;
        public void actionPerformed(ActionEvent ev)
        {
            QuestionHandler.backCard();
        }
        public void keyTyped(KeyEvent e)
        {
        }
        public void keyPressed(KeyEvent e)
        {
            int key = e.getKeyCode();
            if(key == KeyEvent.VK_B)
            {
                pressed = true;
            }
        }
        public void keyReleased(KeyEvent e)
        {
            int key = e.getKeyCode();

            if(key == KeyEvent.VK_B && pressed)
            {
                pressed = false;
                QuestionHandler.backCard();
            }
        }

    }
    private class BuzzListener implements ActionListener, KeyListener
    {
        private boolean pressed = false;
        public void actionPerformed(ActionEvent ev)
        {
            QuestionHandler.buzz();
        }
        public void keyTyped(KeyEvent e)
        {
        }
        public void keyPressed(KeyEvent e)
        {
            int key = e.getKeyCode();
            if(key == KeyEvent.VK_SPACE)
            {
                pressed = true;
            }
        }
        public void keyReleased(KeyEvent e)
        {
            int key = e.getKeyCode();

            if(key == KeyEvent.VK_SPACE && pressed)
            {
                pressed = false;
                QuestionHandler.buzz();
            }
        }

    }
    private class SaveButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent ev)
        {
            ArrayList<String[]> questions = QuestionHandler.getQuestions();
            try
            {
                File file;
                JFileChooser fileSave = new JFileChooser();
                fileSave.showSaveDialog(mainFrame);
                file = fileSave.getSelectedFile();

                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                PrintWriter pWriter = new PrintWriter(writer);

                for(String[] temp : questions)
                {
                    pWriter.println(temp[0] + "\t" + temp[1]);
                }
            }
            catch(IOException ex)
            {
                System.out.println("Could not save");
                ex.printStackTrace();
            }
            catch(Exception ex)
            {
                System.out.println("Other error");
                ex.printStackTrace();
            }
        }
    }
}

/**
*
*This is a Java program that read form a file the elements
*that define an NDFA - lamda and that indicates if a string 
*is accepted by the automata.
*
*@author Diego Gerardo Navarro Gonzalez
*@author Alan Omar Zavala Levaro
*
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame {
    //JOptionPane.showMessageDialog(Wrapper, "couln't find the file or null String");
    private JPanel wrapper; //This is the pane where all the stuff will be.
    private JLabel tittle, instr1, instr2, instr3, txt;//This are the variables for the labels in the GUI
    private JTextField nameTxt, stringAutomata, result, stringDisplay;//This are the text fields where the users can write all the struction nedded for the automarta.
    private JButton checkString;//This is the boton that will action the automata cheking string method.

    public GUI(){
        /*
        *
        *This is the constructor method that will initialize all the thing to make work the graphic user interface.
        */
        super("Part 1");//This is the tittle of the window of the GUI
        initComponents();//This will initialize and add all the variables of the graphic user interface.
        setLayout(new GridLayout(1, 1));//This is the layout of the window, is a 1 row by 1 column.
        setDefaultCloseOperation(EXIT_ON_CLOSE);//This will close all the process when pressing the X in the window.
        setSize(900, 300);//This is the size of the window.
        setVisible(true);//This will set the window visible to the user
    }

    public void initComponents(){
        /*
        *
        *This method will initialize all the variables and set all the setting needed to display a window to the user.
        *
        */
        //The next block of code will initialize all the variables needed to the window.
        wrapper = new JPanel();
        tittle = new JLabel("Lambda - N D F A");
        instr1 = new JLabel("Insert the name of the .txt file");
        instr2 = new JLabel("Insert the string");
        instr3 = new JLabel("The string was:");
        txt = new JLabel(".txt");
        nameTxt = new JTextField();
        stringAutomata = new JTextField();
        result = new JTextField();
        stringDisplay = new JTextField();
        checkString = new JButton("Check string");

        //The block of code will set all the setting needed to make the GUI pretty
        tittle.setFont(new Font("C", Font.BOLD, 18));
        tittle.setHorizontalAlignment(JLabel.CENTER);
        instr1.setHorizontalAlignment(JLabel.CENTER);
        instr2.setHorizontalAlignment(JLabel.CENTER);
        instr3.setHorizontalAlignment(JLabel.RIGHT);
        result.setEditable(false);
        stringDisplay.setEditable(false);
        stringDisplay.setForeground(Color.black);

        //This line of code add the button and action listener to add funcitonality when is click.
        checkString.addActionListener(new checkButtonListener());
        //This will add the layout of the wrapper and how to be display.
        wrapper.setLayout(new GridLayout(6, 5));

        //The rest of lines are the structure of the window row to row.
        //First row
        wrapper.add(new JLabel(""));
        wrapper.add(new JLabel(""));
        wrapper.add(tittle);
        wrapper.add(new JLabel(""));
        wrapper.add(new JLabel(""));

        //Second row
        wrapper.add(new JLabel(""));
        wrapper.add(instr1);
        wrapper.add(nameTxt);
        wrapper.add(txt);
        wrapper.add(new JLabel(""));

        //third row
        wrapper.add(new JLabel(""));
        wrapper.add(instr2);
        wrapper.add(stringAutomata);
        wrapper.add(checkString);
        wrapper.add(new JLabel(""));

        //Fourth row
        wrapper.add(new JLabel(""));
        wrapper.add(new JLabel(""));
        wrapper.add(new JLabel(""));
        wrapper.add(new JLabel(""));
        wrapper.add(new JLabel(""));

        //Fifth row
        wrapper.add(new JLabel(""));
        wrapper.add(instr3);
        wrapper.add(stringDisplay);
        wrapper.add(result);
        wrapper.add(new JLabel(""));

        //Sixth row
        wrapper.add(new JLabel(""));
        wrapper.add(new JLabel(""));
        wrapper.add(new JLabel(""));
        wrapper.add(new JLabel(""));
        wrapper.add(new JLabel(""));

        //after adding everything we just need to add the wrapper to the GUI.
        add(wrapper);
    }

    public class checkButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            /*
            *
            *This methos will add funcionality to the button when is click.
            *
            */

            Automata auto = new Automata();//This will initialize the object automata where all the magic is being made.
            String name = nameTxt.getText();//This will get the name of the .txt file from the text field
            String sTest = stringAutomata.getText();//This will get the string to be tested by the automata
            boolean res = false;//This is the variable to indicate if the string was accepted by the automata or not

            if(!name.equals("") || !sTest.equals("")){
                //we need a try and catch block because in case the file is not in the project folder.
                try {
                    //This will send the information needed to the automata to create the automata and test the stirng.
                    res = auto.processString(name, sTest);
                } catch (Exception ex) {
                    System.err.println("file not found");//Mesasge in case the file is not found.
                }

                //this will put the result if the string was acepted by the automata or not.
                if(res){
                    result.setText("Accepted");
                }else{
                    result.setText("Not Accepted");
                }

                //This will reset the JTextFields where the user can test another string
                stringAutomata.setText("");
                //This will add the string to the JText field to check the result of the automata.
                stringDisplay.setText(sTest);
            }else{
                JOptionPane.showMessageDialog(wrapper, "both fields must have something");
            }
        }
    }
}
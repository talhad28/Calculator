import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class Q2Controller {

    @FXML
    private GridPane grid;

    @FXML
    private Label mainScreen;

    @FXML
    private Label calcScreen;

    private double num = 0, total = 0;
    private String numStr = "0"; //mainScreen string
    private String op = ""; //operation string
    private boolean writeNum = false; //flag if the last thing that was pressed was a digit
    private boolean first = true; //flag for first operation
    private boolean err = false; //flag for error
    private boolean afterEqual = false; //flag if it's after equal operation

    public void initialize(){
        grid.setHgap(2); // horizontal gap between columns
        grid.setVgap(2); // vertical gap between rows
        mainScreen.setFont(Font.font("System", 72));
        calcScreen.setFont(Font.font("System", 16));
        mainScreen.setAlignment(Pos.BASELINE_RIGHT);
        calcScreen.setAlignment(Pos.BASELINE_RIGHT);
        mainScreen.setText("0");
        String[] sym = {"CE", "C", "del", "/", "7", "8", "9", "*", "4", "5", "6", "-", "1", "2", "3", "+",
            "0", ".", "+/-", "="}; //button symbols
        Button[] btnLst = new Button[20];
        for(int i=0; i<20; i++){
            Button b = new Button(sym[i]);
            b.setFont(Font.font("System", 17));
            b.setPrefSize(grid.getPrefWidth()/4, grid.getPrefHeight()/5);
            b.setStyle("-fx-background-radius: 10"); //rounding the corner of the buttons
            b.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    buttonHandle(event);
                }
            });
            btnLst[i] = b;
            grid.add(btnLst[i], i%4, i/4);
        }
    }

    private void buttonHandle(ActionEvent event){
        Button b = (Button) event.getSource();
        String sym = b.getText();
        switch (sym) {

            // numbers, all act the same
            case "0":
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "7":
            case "8":
            case "9":
                if(afterEqual)
                    afterEqual=false;
                if(first)
                    calcScreen.setText("");
                numStr += sym;
                num = Double.parseDouble(numStr);
                showIntOnScreen(num);
                writeNum = true;
                break;

            //operations, all act the same
            case "/":
            case "*":
            case "-":
            case "+":
                if(!first)
                    calcPrev();
                else if(!afterEqual){
                    num = Double.parseDouble(numStr);
                    total = num;
                }
                if(!err){
                    showIntOnScreen(total);
                    op = sym;
                    calcScreen.setText(total + " " + op);
                    num = 0;
                    numStr = "0";
                    first = false;
                    writeNum = false;
                }
                if(err)
                    clearAllVars();
                break;

            //clear current number
            case "CE":
                numStr = "0";
                mainScreen.setText("0");
                afterEqual = false;
                break;

            //clear all
            case "C":
                clearAllVars();
                mainScreen.setText("0");
                total = 0;

                break;

            //delete last digit inputted, works only after writing a digit
            case "del":
                if(writeNum){
                    numStr = numStr.substring(0, numStr.length()-1);
                    if(numStr.equals(""))
                        mainScreen.setText("0");
                    else {
                        num = Double.parseDouble(numStr);
                        showIntOnScreen(num);
                    }
                }
                break;

            //decimal point
            case ".":
                if(numStr.equals("") || Double.parseDouble(numStr) == 0)
                    numStr = "0.";
                else
                    numStr += ".";
                num = Double.parseDouble(numStr);
                showIntOnScreen(num);
                break;

            //Change the number from positive to negative and vise versa
            case "+/-":
                if(numStr.charAt(0) == '-')
                    numStr = numStr.substring(1);
                else
                    numStr = "-" + numStr;
                num = Double.parseDouble(numStr);
                showIntOnScreen(num);
                break;

            case "=":
                num = Double.parseDouble(numStr);
                calcPrev();
                if(!err)
                    showIntOnScreen(total);
                clearAllVars();
                afterEqual = true;
                break;
        }
    }

    //Calculate the previous operation
    private void calcPrev(){
        switch(op){
            case "/":
                num = Double.parseDouble(numStr);
                if(num == 0){ //division by 0
                    mainScreen.setText("Math Error");
                    total = 0;
                    err = true;
                    break;
                }
                total /= num;
                break;
            case "*":
                total *= num;
                break;
            case "-":
                total -= num;
                break;
            case "+":
                total += num;
                break;
        }
        if(total > 1e120){ //limit numbers size
            mainScreen.setText("Math Error");
            clearAllVars();
            err = true;
        }
    }

    //reset all variables except total and mainScreen
    private void clearAllVars(){
        calcScreen.setText("");
        numStr = "0";
        num = 0;
        op = "";
        first = true;
        writeNum = false;
        err = false;
    }

    private void showIntOnScreen(double num){
        if((int)num == num){
            mainScreen.setText((int)num + "");
        }
        else
            mainScreen.setText(num + "");
    }

}

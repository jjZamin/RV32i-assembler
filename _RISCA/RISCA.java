import GUI.RISCA_GUI;

import javax.swing.*;

public class RISCA {
    public static void main(String[] args) {
        RISCA_GUI txEdGUI = new RISCA_GUI();
        txEdGUI.setBounds(0,0,400,600);
        txEdGUI.setTitle("_RISCA");
        txEdGUI.setResizable(false);
        txEdGUI.setVisible(true);
        txEdGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        txEdGUI.startSyntaxColoring();
    }
}

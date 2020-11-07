package Tools;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

public class FileHandler extends javax.swing.JFrame{
    String filename;
    public FileHandler() {}

    public void openProject(JTextPane textArea) {
        FileDialog fileDialog = new FileDialog(FileHandler.this, "Open File", FileDialog.LOAD);
        fileDialog.setVisible(true);

        if (fileDialog.getFile() != null) {
            filename = fileDialog.getDirectory() + fileDialog.getFile();
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line + "\n");
                textArea.setText(sb.toString());
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("File no found");
        }
    }

    public void saveProject(JTextPane textArea) {
        FileDialog fileDialog = new FileDialog(FileHandler.this,
                "Save File", FileDialog.SAVE);
        fileDialog.setVisible(true);
        if(fileDialog.getFile() != null) {
            filename = fileDialog.getDirectory() + fileDialog.getFile();
        }
        try {
            FileWriter fileWriter = new FileWriter(filename);
            fileWriter.write(textArea.getText());
            fileWriter.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public String getFilename() {
        return filename;
    }
}
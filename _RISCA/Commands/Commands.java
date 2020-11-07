package Commands;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class Commands {
    /*report*/
    private JTextPane report = new JTextPane();
    private SimpleAttributeSet reportAttr;
    private Style reportStyle;
    /*-----*/
    private JTextPane notes = new JTextPane();
    private JTextPane machine = new JTextPane();
    private JTextPane assembly = new JTextPane();
    private String l;

    public Commands() {
        reportStyle = report.addStyle("", null);
        reportAttr = new SimpleAttributeSet();
    }

    public Commands(String l) {
        this();
        this.l = l;
    }

    public StyledDocument reportWrongCommandStyle() {
        Font font = new Font("Courier", Font.ITALIC, 12);
        report.setFont(font);
        StyleConstants.setItalic(reportAttr, true);
        report.setCharacterAttributes(reportAttr,true);
        StyleConstants.setForeground(reportStyle, Color.red);
        StyledDocument docReport = report.getStyledDocument();
        return docReport;
    }

    public void setReportPane(JTextPane jTextPane) {
        this.report = jTextPane;
    }
    public void setNotesPane(JTextPane jTextPane) {
        this.notes = jTextPane;
    }
    public void setAssemblyPan(JTextPane assembly) {
        this.assembly = assembly;
    }
    public void setMachinePane(JTextPane machine) {
        this.machine = machine;
    }

    public void setCommand(String command) throws BadLocationException {
        switch (command) {
            case "clear(notes)":
                notes.setText("");
                break;
            case "clear(asm)":
                assembly.setText("");
                break;
            case "clear(machine)":
                machine.setText("");
                break;
            case "clear(report)":
                report.setText("");
                break;
            case "exit()":
                System.exit(0);
                break;
            default:
                reportWrongCommandStyle().insertString(reportWrongCommandStyle().getLength(),
                        "[COMMAND_ERR]: " + "'" +command+ "' " + "does not exist" + "\n", reportStyle);
                break;
        }
    }
}
package SyntaxHighLite;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RV32i_syntax {

    private JTextPane assembly;
    private Style assemblySyntaxStyle;
    /*substyles*/
    private Style defaultSyntax;
    private Style redBoldSyntax;
    private Style greenBoldSyntax;
    private Style magentaBoldSyntax;
    private Style blueItalicSyntax;
    private Style blueBoldSyntax;
    private Style blackBold;
    private Style greyBoldItalic;
    private StyledDocument docReport;
    private List<String> RV32instrCodes = Arrays.asList(
            "LUI", "AUIPC", "JAL", "JALR", "BEQ", "BNE", "BLT", "BGE",
            "BLTU", "BGEU", "LB", "LH", "LW", "LBU", "LHU", "SB", "SH", "SW",
            "ADDI", "SLTI", "SLTIU", "XORI", "ORI", "ANDI", "SLLI", "SRLI", "SRAI",
            "ADD", "SUB", "SLL", "SLT", "SLTU", "XOR", "SRL", "SRA", "OR", "AND",
            "FENCE", "FENCE.I", "ECALL", "EBREAK", "NOP");
    private List<String> RV32CSR = Arrays.asList(
            "CSRRW", "CSRRS", "CSRRC", "CSRRWI", "CSRRSI", "CSRRCI"
    );
    private List<String> ownInstructions = Arrays.asList(
            "SPIS", "LD"
    );
    private List<String> regCodes = Arrays.asList(
            "R0", "R1", "R2", "R3", "R4", "R5", "R6", "R7",
            "R8", "R9", "R10", "R11", "R12", "R13", "R14", "R15",
            "R16", "R17", "R18", "R19", "R20", "R21", "R22", "R23",
            "R24", "R25", "R26", "R27", "R28", "R29", "R30", "R31");
    private List<String> specials = Arrays.asList(
            "_PROGRAM_START_", "_PROGRAM_END."
    );
    private List<String> functions = new ArrayList<>();
    private List<String> calledFunction = new ArrayList<>();

    public RV32i_syntax() {}

    public void setCodeArea(JTextPane assembly) {
        this.assembly = assembly;
    }

    public void setAttributes() {
        docReport = assembly.getStyledDocument();
        Style defaultStyle =
                StyleContext.getDefaultStyleContext ().getStyle(StyleContext.DEFAULT_STYLE);
        assemblySyntaxStyle = docReport.addStyle("regular", defaultStyle);
        StyleConstants.setFontFamily (assemblySyntaxStyle, "Courier");
        StyleConstants.setFontSize (assemblySyntaxStyle, 14);

        defaultSyntax = assembly.addStyle ("default", assemblySyntaxStyle);
        StyleConstants.setForeground (defaultSyntax, Color.black);

        redBoldSyntax = assembly.addStyle ("redBold", assemblySyntaxStyle);
        StyleConstants.setForeground (redBoldSyntax, Color.red);
        StyleConstants.setBold (redBoldSyntax, true);

        blueItalicSyntax = assembly.addStyle("blueItalic", assemblySyntaxStyle);
        StyleConstants.setForeground(blueItalicSyntax, Color.blue);
        StyleConstants.setItalic(blueItalicSyntax, true);

        blueBoldSyntax = assembly.addStyle("blueBold", assemblySyntaxStyle);
        StyleConstants.setForeground(blueBoldSyntax, Color.blue);
        StyleConstants.setBold(blueBoldSyntax, true);

        blackBold = assembly.addStyle ("blackBold", assemblySyntaxStyle);
        StyleConstants.setForeground (blackBold, Color.BLACK);
        StyleConstants.setBold (blackBold, true);

        greyBoldItalic = assembly.addStyle ("greyBold", assemblySyntaxStyle);
        StyleConstants.setForeground (greyBoldItalic, Color.GRAY);
        StyleConstants.setBold (greyBoldItalic, true);
        StyleConstants.setItalic (greyBoldItalic, true);

        greenBoldSyntax = assembly.addStyle ("greenBold", assemblySyntaxStyle);
        StyleConstants.setForeground (greenBoldSyntax, Color.GREEN);
        StyleConstants.setBold (greenBoldSyntax, true);

        magentaBoldSyntax = assembly.addStyle ("magentaBold", assemblySyntaxStyle);
        StyleConstants.setForeground (magentaBoldSyntax, Color.MAGENTA);
        StyleConstants.setBold (magentaBoldSyntax, true);
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    private void syntax(String word) throws BadLocationException {
        String decodeWord = word.strip().toUpperCase().replace(",", "").replace(":", "")
                .replace(";", "").replace("~", "");
        String code = "";

        if(RV32instrCodes.contains(decodeWord)) {
            code = "RV32instruction";
        }
        else if(regCodes.contains(decodeWord)) {
            code = "register";
        }
        else if(specials.contains(decodeWord)) {
            code = "specials";
        }
        else if(functions.contains(decodeWord)) {
            code = "function";
        }
        else if(RV32CSR.contains(decodeWord)) {
            code = "RV32CSR";
        }
        else if(ownInstructions.contains(decodeWord)) {
            code = "ownInstrctions";
        }
        else if(calledFunction.contains(decodeWord)) {
            code = "calledFunction";
        }
        else if(isNumeric(decodeWord)) {
            code = "register";
        }
        else {
            code = "default";
        }

        switch (code) {
            case "RV32instruction":
                word = word.replace("~", "");
                docReport.insertString(docReport.getLength(),
                        "~" + word.toUpperCase().replace(":", "") + ":", redBoldSyntax);
                break;
            case "ownInstrctions":
                word = word.replace("~", "");
                docReport.insertString(docReport.getLength(),
                        "~" + word.toUpperCase().replace(":", "") + ":", magentaBoldSyntax);
                break;
            case "register":
                if(!word.contains(";")) {
                    docReport.insertString(docReport.getLength(),
                            " " + word.toUpperCase().replace(",", "") + ",", blueItalicSyntax);
                }
                else {
                    docReport.insertString(docReport.getLength(),
                            " " + word.toUpperCase(), blueItalicSyntax);
                }
                break;
            case "RV32CSR":
                word = word.replace("~", "");
                docReport.insertString(docReport.getLength(),
                        "~" + word.toUpperCase().replace(":", "") + ":", greenBoldSyntax);
                break;
            case "specials":
                docReport.insertString(docReport.getLength(),
                        word, blackBold);
                break;
            case "function":
                docReport.insertString(docReport.getLength(),
                        word, greyBoldItalic);
                break;
            case "calledFunction":
                if(!word.contains(";")) {
                    docReport.insertString(docReport.getLength(),
                            " " + word.toUpperCase().replace(",", "") + ";", blueBoldSyntax);
                }
                else {
                    docReport.insertString(docReport.getLength(),
                            " " + word.toUpperCase(), blueBoldSyntax);
                }
                break;
            default:
                word = word.strip();
                if(word.equals("?")) {
                    word = "";
                    break;
                }
                else {
                    docReport.insertString(docReport.getLength(),
                            " " + word.replace(" ?", "") + " ?", defaultSyntax);
                }
                break;
        }
        assembly.setCaretPosition(assembly.getStyledDocument().getLength());
    }

    private void setSyntax() throws BadLocationException {
        assembly.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                try {
                    reloadCode(evt);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void reloadCode(java.awt.event.KeyEvent evt) throws BadLocationException {
        if(evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_SPACE) {

            String[] lines = assembly.getText().split("\n");
            String[] wordsInLine;
            String word = "";
            assembly.setText("");
            for(int i = 0; i < lines.length; i++) {
                wordsInLine = lines[i].split(" ");
                if(i > 0) {
                    docReport.insertString(docReport.getLength(),
                            "\n", defaultSyntax);
                }
                for (int k = 0; k < wordsInLine.length; k++) {
                    word = wordsInLine[k].replace(" ?", "");
                    if(word.strip().startsWith("***") && word.strip().endsWith("***")) {
                        functions.add(word.strip());
                        calledFunction.add("@"+word.replace("*", "").strip());
                    }
                    syntax(word.strip());
                }
            }
        }
    }

    private void runSyntax() throws BadLocationException, InterruptedException {
        setSyntax();
        Thread.sleep(1000);
    }


    public void start() {
        try {
            runSyntax();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}

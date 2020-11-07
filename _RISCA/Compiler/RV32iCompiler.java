package Compiler;

import javafx.beans.binding.StringBinding;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import static javax.swing.JOptionPane.showMessageDialog;

public class RV32iCompiler {

    private JTextPane report;
    private JTextPane assembly;
    private JTextPane machine;

    private Style machineSyntaxStyle;
    private Style reportSyntaxStyle;
    private StyledDocument docReport;
    private StyledDocument docMachine;

    private Style defaultSyntax;
    private Style blackBold;
    private Style redBold;
    private Style redItalic;
    private Style greenItalic;
    private Style greenBold;

    /*instriction types###################################################*/
    private List<String> Rtype_nonImm = Arrays.asList(
            "ADD", "SUB", "SLL", "SLT", "SLTU", "XOR", "SRL", "SRA", "OR", "AND");
    private List<String> Rtype_Imm = Arrays.asList(
            "SLLI", "SRLI", "SRAI");

    private List<String> Itype_load = Arrays.asList(
            "LB", "LH", "LW", "LBU", "LHU"
    );
    private List<String> Itype_ALU = Arrays.asList(
            "ADDI", "SLTI", "SLTIU", "XORI", "ORI", "ANDI"
    );
    private List<String> Itype_JALR = Arrays.asList(
            "JALR", ""
    );

    private List<String> Stype = Arrays.asList(
            "SB", "SH", "SW"
    );

    private List<String> Btype = Arrays.asList(
            "BEQ", "BNE", "BLT", "BGE", "BLTU", "BGEU"
    );

    private List<String> Utype_LUI = Arrays.asList(
            "LUI", ""
    );
    private List<String> Utype_AUIPC = Arrays.asList(
            "AUIPC", ""
    );

    private List<String> Jtype = Arrays.asList(
        "JAL", ""
    );

    private List<String> CSRtype = Arrays.asList(
            "CSRRW", "CSRRS", "CSRRC"
    );

    /*instruction fields*/
    private HashMap<String, String> OPCODE = new HashMap<String, String>() {
        {
            put("Rtype_nonImm", "0110011");
            put("Rtype_Imm", "0010011");
            put("Itype_load", "0000011");
            put("Itype_ALU", "0010011");
            put("Itype_JALR", "1100111");
            put("Stype", "0100011");
            put("Btype", "1100011");
            put("Utype_LUI", "0110111");
            put("Utype_AUIPC", "0010111");
            put("Jtype", "1101111");
            put("CSRtype", "1110011");
        }
    };

    private HashMap<String, String> regAddr = new HashMap<String, String>() {
        {
            put("R0", "00000");
            put("R1", "00001");
            put("R2", "00010");
            put("R3", "00011");
            put("R4", "00100");
            put("R5", "00101");
            put("R6", "00110");
            put("R7", "00111");
            put("R8", "01000");
            put("R9", "01001");
            put("R10", "01010");
            put("R11", "01011");
            put("R12", "01100");
            put("R13", "01101");
            put("R14", "01110");
            put("R15", "01111");
            put("R16", "10000");
            put("R17", "10001");
            put("R18", "10010");
            put("R19", "10011");
            put("R20", "10100");
            put("R21", "10101");
            put("R22", "10110");
            put("R23", "10111");
            put("R24", "11000");
            put("R25", "11001");
            put("R26", "11010");
            put("R27", "11011");
            put("R28", "11100");
            put("R29", "11101");
            put("R30", "11110");
            put("R31", "11111");
        }
    };

    private HashMap<String, String> funct3 = new HashMap<String, String>() {
        {
            put("JALR", "000");
            put("BEQ", "000");
            put("BNE", "001");
            put("BLT", "100");
            put("BGE", "101");
            put("BLTU", "110");
            put("BGEU", "111");

            put("LB", "000");
            put("LH", "001");
            put("LW", "010");
            put("LBU", "100");
            put("LHU", "101");

            put("SB", "000");
            put("SH", "001");
            put("SW", "010");

            put("ADDI", "000");
            put("SLTI", "010");
            put("SLTIU", "011");
            put("XORI", "100");
            put("ORI", "110");
            put("ANDI", "111");

            put("SLLI", "001");
            put("SRLI", "101");
            put("SRAI", "101");

            put("ADD", "000");
            put("SUB", "000");
            put("SLL", "001");
            put("SLT", "010");
            put("SLTU", "011");
            put("XOR", "100");
            put("SRL", "101");
            put("SRA", "101");
            put("OR", "110");
            put("AND", "111");

            put("CSRRW", "001");
            put("CSRRS", "010");
            put("CSRRC", "011");
            put("CSRRWI", "101");
            put("CSRRSI", "110");
            put("CSRRCI", "111");
        }
    };
    private HashMap<String, String> funct7 = new HashMap<String, String>() {
        {
            put("SLLI", "0000000");
            put("SRLI", "0000000");
            put("SRAI", "0100000");
            put("ADD", "0000000");
            put("SUB", "0100000");
            put("SLL", "0000000");
            put("SLT", "0000000");
            put("SLTU", "0000000");
            put("XOR", "0000000");
            put("SRL", "0000000");
            put("SRA", "0100000");
            put("OR", "0000000");
            put("AND", "0000000");
        }
    };
    /*##################*/

    /*INCTRUCTION ADDRESS PLACEMENTS*/

    private LinkedHashMap<String, Integer> functionStartAddress = new LinkedHashMap<>();

    /*ARRAY SPLIT INTO FUNCTIONS*/
    private LinkedHashMap<String, List<String>> functionHashMap = new LinkedHashMap<>();

    /*error while compiling flag*/
    private boolean ERROR = false;

    /*machine code*/
    private List<String> MACHINE_INSTRUCTIONS = new ArrayList<>();

    public RV32iCompiler() {}

    public void setReportPane(JTextPane jTextPane) {
        this.report = jTextPane;
    }
    public void setAssemblyPan(JTextPane assembly) {
        this.assembly = assembly;
    }
    public void setMachinePane(JTextPane machine) {
        this.machine = machine;
    }

    public void setMachineSyntax() {
        docMachine = machine.getStyledDocument();
        Style defaultStyle =
                StyleContext.getDefaultStyleContext ().getStyle(StyleContext.DEFAULT_STYLE);
        machineSyntaxStyle = docMachine.addStyle("regular", defaultStyle);
        StyleConstants.setFontFamily (machineSyntaxStyle, "Courier");
        StyleConstants.setFontSize (machineSyntaxStyle, 12);

        defaultSyntax = machine.addStyle ("default", machineSyntaxStyle);
        StyleConstants.setForeground (defaultSyntax, Color.black);

        blackBold = assembly.addStyle ("blackBold", machineSyntaxStyle);
        StyleConstants.setForeground (blackBold, Color.BLACK);
        StyleConstants.setBold (blackBold, true);
    }
    public void setReportSyntax() {
        docReport = report.getStyledDocument();
        Style defaultStyle =
                StyleContext.getDefaultStyleContext ().getStyle(StyleContext.DEFAULT_STYLE);
        reportSyntaxStyle = docReport.addStyle("regular", defaultStyle);
        StyleConstants.setFontFamily (reportSyntaxStyle, "Courier");
        StyleConstants.setFontSize (reportSyntaxStyle, 12);

        defaultSyntax = report.addStyle ("default", reportSyntaxStyle);
        StyleConstants.setForeground (defaultSyntax, Color.black);

        blackBold = report.addStyle ("blackBold", reportSyntaxStyle);
        StyleConstants.setForeground (blackBold, Color.BLACK);
        StyleConstants.setBold (blackBold, true);

        redBold = report.addStyle ("redBold", reportSyntaxStyle);
        StyleConstants.setForeground (redBold, Color.red);
        StyleConstants.setBold (redBold, true);

        greenBold = report.addStyle ("greenBold", reportSyntaxStyle);
        StyleConstants.setForeground (greenBold, Color.green);
        StyleConstants.setBold (greenBold, true);

        greenItalic = report.addStyle ("redItalic", reportSyntaxStyle);
        StyleConstants.setForeground (greenItalic, Color.green);
        StyleConstants.setItalic (greenItalic, true);

        redItalic = report.addStyle ("redItalic", reportSyntaxStyle);
        StyleConstants.setForeground (redItalic, Color.red);
        StyleConstants.setItalic (redItalic, true);
    }

    private void produceReportComment(String s, Style st) throws BadLocationException {
        docReport.insertString(docReport.getLength(),
                ">> " + s + "\n", st);
    }

    private void printInstructions(List<String> instructions) throws BadLocationException {
        machine.setText("");
        String byte0 = "";
        String byte1 = "";
        String byte2 = "";
        String byte3 = "";

        for(int i = 0; i < instructions.size(); i++) {

            byte0 = instructions.get(i).substring(0, 8);
            byte1 = instructions.get(i).substring(8, 16);
            byte2 = instructions.get(i).substring(16, 24);
            byte3 = instructions.get(i).substring(24, 32);

            docMachine.insertString(docMachine.getLength(), '"'+ byte3 +'"' + ", "
                    , defaultSyntax);
            docMachine.insertString(docMachine.getLength(), '"'+ byte2 +'"' + ", "
                    , defaultSyntax);
            docMachine.insertString(docMachine.getLength(), '"'+ byte1 +'"' + ", "
                    , defaultSyntax);
            docMachine.insertString(docMachine.getLength(), '"'+ byte0 +'"' + ", " + "\n"
                    , defaultSyntax);
        }
        docMachine.insertString(docMachine.getLength(), "\n\n MACHINE \n\n"
                , defaultSyntax);

        for(int i = 0; i < instructions.size(); i++) {

            docMachine.insertString(docMachine.getLength(), '"'+ instructions.get(i) +'"' + "\n"
                    , defaultSyntax);
        }

    }

    /* CHECKING THE DOCUMENT FOR CORRECT START/END CONDITIONS ########################################*/
    private void checkStartAndEndOfDocument(String[] lines) throws BadLocationException {
        if(!lines[0].strip().equals("_PROGRAM_START_")) {
            produceReportComment("Program must start with '_PROGRAM_START_'", redItalic);
            ERROR = true;
        }
        else if(!lines[lines.length - 1].strip().equals("_PROGRAM_END.")) {
            produceReportComment("Program must end with '_PROGRAM_END.'", redItalic);
            ERROR = true;
        }
        else {
            produceReportComment("_PROGRAM_START_ and _PROGRAM_END. are set", greenItalic);
        }
    }
    private void isError() throws BadLocationException {
        if(ERROR) {
            showMessageDialog(null, "Oops, something went wrong! See report.", "ERROR", 0);
            docReport.insertString(docReport.getLength(),
                    "\n" + "[ NO MACHINE CODE PRODUCED ]", redBold);
            ERROR = false;
        }
        else {
            showMessageDialog(null, "Congrats, compiled successfully! See report.", "BEAUTIFUL CODE!", 1);
            docReport.insertString(docReport.getLength(),
                    "\n" + "[ MACHINE CODE PRODUCED SUCCESSFULLY! ]", greenBold);

            printInstructions(MACHINE_INSTRUCTIONS);
        }
    }

    private void checkUnknownSymbols(String[] lines) throws BadLocationException {
        int number = 0;
        boolean error = false;
        boolean codeLines = false;
        for(String s: lines) {
            number++;
            if(s.strip().contains("?")) {
                produceReportComment("Line: "+number+" contains an unknown symbol.", redItalic);
                ERROR = true;
                error = true;
            }
        }
        if(!error) {
            produceReportComment("No unknown symbols!", greenItalic);
        }
    }
    private void tokenCheck(String[] lines) throws BadLocationException {
        int number = 0;
        boolean error = false;
        boolean codeLines = false;
        for(String s: lines) {
            if(s.strip().contains("~")) {
                codeLines = true;
                number++;
            }
            if(s.strip().contains("~") && !s.strip().contains(";")) {
                produceReportComment("Codeline: "+number+" is missing a ';'", redItalic);
                ERROR = true;
                error = true;
            }
        }
        if(!error && codeLines) {
            produceReportComment("All tokens set correctly!", greenItalic);
        }
    }
    private int numberOfActiveCodeLines(String[] lines) {
        int number = 0;
        for(String s: lines) {
            if(s.strip().contains("~") && s.strip().contains(";")) {
                number++;
            }
        }
        return number;
    }

    private void checkSyntax() throws BadLocationException {
        String[] lines = assembly.getText().split("\n");
        checkStartAndEndOfDocument(lines);
        checkUnknownSymbols(lines);
        tokenCheck(lines);
        if(!ERROR) {
            produceReportComment("[ Generating machine code ]", greenItalic);
            startCompiling(lines);
        }
    }
    /*#################################################################################################################*/

    /*COMPILATION PROCESS*/
    private boolean checkIfMain(String[] lines) {
        for (String s : lines) {
            if (s.strip().contains("***MAIN***")) {
                return true;
            }
        }
        return false;
    }
    /*COMPILATION PROCESS*/
    private boolean checkIfEnd(String[] lines) {
        for(String s: lines) {
            if(s.strip().contains("***END***")) {
                return true;
            }
        }
        return false;
    }

    private String splitInstruction(String line, String whichField) {
        String instruction = line.replace("~", "")
                .replace(",", "").replace(":", "")
                .replace(";", "");
        String[] splitInstruction = instruction.split(" ");

        switch(whichField) {
            case "command":
                return splitInstruction[0];
            case "x1":
                return splitInstruction[1];
            case "x2":
                return splitInstruction[2];
            case "x3":
                return splitInstruction[3];
            default:
                return "none";
        }
    }

    private String checkWhichType(String line) {
        String head = splitInstruction(line, "command");

        if(Rtype_nonImm.contains(head.strip())) {
            return "Rtype_nonImm";
        }
        else if(Rtype_Imm.contains(head.strip())) {
            return "Rtype_Imm";
        }
        else if(Itype_load.contains(head.strip())) {
            return "Itype_load";
        }
        else if(Itype_ALU.contains(head.strip())) {
            return "Itype_ALU";
        }
        else if(Itype_JALR.contains(head.strip())) {
            return "Itype_JALR";
        }
        else if(Stype.contains(head.strip())) {
            return "Stype";
        }
        else if(Btype.contains(head.strip())) {
            return "Btype";
        }
        else if(Utype_LUI.contains(head.strip())) {
            return "Utype_LUI";
        }
        else if(Utype_AUIPC.contains(head.strip())) {
            return "Utype_AUIPC";
        }
        else if(Jtype.contains(head.strip())) {
            return "Jtype";
        }
        else if(CSRtype.contains(head.strip())) {
            return "CSRtype";
        }
        else {
            return "no such type";
        }
    }
    private void splitIntoFunction(String[] lines) throws BadLocationException {
        String functionName = "";
        List<String> extraElement = new ArrayList<>();

        for(int o = 0; o < lines.length; o++) {
            extraElement.add(lines[o]);
        }
        extraElement.add(" ");
        int i = 0;
        int k = 0;
        List<String> functionList = new ArrayList<>();
        while(i < lines.length) {
            if(extraElement.get(i).strip().contains("*")) {
                functionName = "@"+extraElement.get(i).strip().replace("*", "");
                k = i + 1;
                while(k < lines.length && !extraElement.get(k).strip().contains("*")
                        && !extraElement.get(k).strip().contains("_")) {
                    functionList.add(extraElement.get(k).strip());
                    k++;
                }
                functionHashMap.put(functionName, functionList);
                functionName = "";
                functionList = new ArrayList<>();
                i = k - 1;
            }
            i++;
        }
    }

    private void logFunctionStartAddresses(LinkedHashMap<String, List<String>> functionHashMap) {
        Set<String> functionNameBuffer;
        int nrOfInstructions = 0;
        int instructionBeginAddress = 264; //264 as SCRUB seqq is at 0 -> 260
        List<String> instructions = new ArrayList<>();
        String buffer;
        functionNameBuffer = functionHashMap.keySet();
        String[] functionName = new String[functionNameBuffer.size() + 1];
        int k = 0;
        for(String str : functionNameBuffer) {
            functionName[k++] = str;
        }
        functionName[functionNameBuffer.size()] = "x";

        functionStartAddress.put(functionName[0], 264); //
        for(int i = 0; i < functionHashMap.size(); i++) {
            instructions = functionHashMap.get(functionName[i]);
            nrOfInstructions = instructions.size();
            instructionBeginAddress = nrOfInstructions * 4
                    + instructionBeginAddress;
            functionStartAddress.put(functionName[i+1], instructionBeginAddress);
        }
        functionStartAddress.remove("x");
    }

    private void buildMachineCode() throws BadLocationException {
        Set<String> functionNameBuffer;
        functionNameBuffer = functionHashMap.keySet();
        String[] functionName = new String[functionNameBuffer.size()];
        int k = 0;
        for(String str : functionNameBuffer) {
            functionName[k++] = str;
        }

        List<String> instructions = new ArrayList<>();
        String instruction = "";
        String machineInstruction = "";
        int all_instruction_count = 0;
        for(int i = 0; i < functionHashMap.size(); i++) {
            instructions = functionHashMap.get(functionName[i]);
            for(int x = 0; x < instructions.size(); x++) {
                instruction = instructions.get(x);
                machineInstruction = buildInstruction(instruction);
                MACHINE_INSTRUCTIONS.add(machineInstruction);
            }
            instructions = new ArrayList<>();
        }
    }

    private char[] pad0BinaryToLeft(char[] nr, int rquiredLength) {
        int nrLength = nr.length;
        int diff = rquiredLength - nrLength;
        char[] padded = new char[rquiredLength];
        int j = diff;
        for(int i = 0; i < nrLength; i++) {
            padded[j] = nr[i];
            j++;
        }
        if(diff > 0) {
            for(int i = diff - 1; i >= 0; i--) {
                padded[i] = '0';
            }
        }
        return padded;
    }

    private String RtypeBuild(String whichType, char[] tOPCODE, char[] trd, char[] tfunct3, char[] trs1,
                              char[] trs2, char[] tfunct7, char[] tshamt) {
        int bitCounter = 31;
        char[] instructionDoneCharArray = new char[32];
        for (int i = tOPCODE.length - 1; i >= 0; i--) {
            instructionDoneCharArray[bitCounter] = tOPCODE[i];
            bitCounter--;
        }
        for (int i = trd.length - 1; i >= 0; i--) {
            instructionDoneCharArray[bitCounter] = trd[i];
            bitCounter--;
        }
        for (int i = tfunct3.length - 1; i >= 0; i--) {
            instructionDoneCharArray[bitCounter] = tfunct3[i];
            bitCounter--;
        }
        for (int i = trs1.length - 1; i >= 0; i--) {
            instructionDoneCharArray[bitCounter] = trs1[i];
            bitCounter--;
        }
        /*shamt or rs2*/
        if(whichType.equals("Rtype_nonImm")) {
            for (int i = trs2.length - 1; i >= 0; i--) {
                instructionDoneCharArray[bitCounter] = trs2[i];
                bitCounter--;
            }
            for (int i = tfunct7.length - 1; i >= 0; i--) {
                instructionDoneCharArray[bitCounter] = tfunct7[i];
                if(bitCounter > 0) {
                    bitCounter--;
                }
            }
        }
        else if(whichType.equals("Rtype_Imm")) {
            char[] paddedShamt = pad0BinaryToLeft(tshamt, 5);
            for (int i = paddedShamt.length - 1; i >= 0; i--) {
                instructionDoneCharArray[bitCounter] = paddedShamt[i];
                bitCounter--;
            }
            for (int i = tfunct7.length - 1; i >= 0; i--) {
                instructionDoneCharArray[bitCounter] = tfunct7[i];
                if(bitCounter > 0) {
                    bitCounter--;
                }
            }
        }
        return new String(instructionDoneCharArray);
    }

    private String ItypeBuild(char[] tOPCODE, char[] trd, char[] tfunct3, char[] trs1, char[] Imm) {
        int bitCounter = 31;
        char[] paddedImm = pad0BinaryToLeft(Imm, 12);
        char[] instructionDoneCharArray = new char[32];
        for (int i = tOPCODE.length - 1; i >= 0; i--) {
            instructionDoneCharArray[bitCounter] = tOPCODE[i];
            bitCounter--;
        }
        for (int i = trd.length - 1; i >= 0; i--) {
            instructionDoneCharArray[bitCounter] = trd[i];
            bitCounter--;
        }
        for (int i = tfunct3.length - 1; i >= 0; i--) {
            instructionDoneCharArray[bitCounter] = tfunct3[i];
            bitCounter--;
        }
        for (int i = trs1.length - 1; i >= 0; i--) {
            instructionDoneCharArray[bitCounter] = trs1[i];
            bitCounter--;
        }
        for (int i = paddedImm.length - 1; i >= 0; i--) {
            instructionDoneCharArray[bitCounter] = paddedImm[i];
            bitCounter--;
        }
        return new String(instructionDoneCharArray);
    }


    private String StypeBuild(char[] tOPCODE, char[] trs2, char[] tfunct3, char[] trs1, char[] Imm) {
        int bitCounter = 31;
        char[] paddedImm = pad0BinaryToLeft(Imm, 12);

        char[] imm0_4 = new char[5];
        char[] imm5_11 = new char[7];
        int imm_count = 0;

        for(int i = 0; i < 7; i++) {
            imm5_11[i] = paddedImm[imm_count];
            imm_count++;
        }
        for(int i = 0; i < 5; i++) {
            imm0_4[i] = paddedImm[imm_count];
            if(imm_count < 12) {
                imm_count++;
            }
        }

        char[] instructionDoneCharArray = new char[32];
        for (int i = tOPCODE.length - 1; i >= 0; i--) {
            instructionDoneCharArray[bitCounter] = tOPCODE[i];
            bitCounter--;
        }
        for (int i = imm0_4.length - 1; i >= 0; i--) {
            instructionDoneCharArray[bitCounter] = imm0_4[i];
            bitCounter--;
        }
        for (int i = tfunct3.length - 1; i >= 0; i--) {
            instructionDoneCharArray[bitCounter] = tfunct3[i];
            bitCounter--;
        }
        for (int i = trs1.length - 1; i >= 0; i--) {
            instructionDoneCharArray[bitCounter] = trs1[i];
            bitCounter--;
        }
        for (int i = trs2.length - 1; i >= 0; i--) {
            instructionDoneCharArray[bitCounter] = trs2[i];
            bitCounter--;
        }
        for (int i = imm5_11.length - 1; i >= 0; i--) {
            instructionDoneCharArray[bitCounter] = imm5_11[i];
            bitCounter--;
        }
        return new String(instructionDoneCharArray);
    }

    private String BtypeBuild(char[] tOPCODE, char[] trs2, char[] tfunct3, char[] trs1, char[] Imm) {
        int bitCounter = 31;
        char[] paddedImm = pad0BinaryToLeft(Imm, 13);


        char[] imm1_4 = new char[4];
        char[] imm5_10 = new char[6];
        char imm11;
        char imm12;

        int imm_count = 0;

        imm12 = paddedImm[imm_count];
        imm_count++;
        imm11 = paddedImm[imm_count];
        imm_count++;

        for(int i = 0; i < 6; i++) {
            imm5_10[i] = paddedImm[imm_count];
            imm_count++;
        }

        for(int i = 0; i < 4; i++) {
            imm1_4[i] = paddedImm[imm_count];
            if(imm_count < 13) {
                imm_count++;
            }
        }

        char[] instructionDoneCharArray = new char[32];
        for (int i = tOPCODE.length - 1; i >= 0; i--) {
            instructionDoneCharArray[bitCounter] = tOPCODE[i];
            bitCounter--;
        }
        instructionDoneCharArray[bitCounter] = imm11;
        bitCounter--;
        for (int i = imm1_4.length - 1; i >= 0; i--) {
            instructionDoneCharArray[bitCounter] = imm1_4[i];
            bitCounter--;
        }
        for (int i = tfunct3.length - 1; i >= 0; i--) {
            instructionDoneCharArray[bitCounter] = tfunct3[i];
            bitCounter--;
        }
        for (int i = trs1.length - 1; i >= 0; i--) {
            instructionDoneCharArray[bitCounter] = trs1[i];
            bitCounter--;
        }
        for (int i = trs2.length - 1; i >= 0; i--) {
            instructionDoneCharArray[bitCounter] = trs2[i];
            bitCounter--;
        }
        for (int i = imm5_10.length - 1; i >= 0; i--) {
            instructionDoneCharArray[bitCounter] = imm5_10[i];
            bitCounter--;
        }
        instructionDoneCharArray[bitCounter] = imm12;
        return new String(instructionDoneCharArray);
    }

    private String UtypeBuild(char[] tOPCODE, char[] trd, char[] Imm) {
        int bitCounter = 31;
        char[] paddedImm = pad0BinaryToLeft(Imm, 32);

        char[] instructionDoneCharArray = new char[32];
        for (int i = tOPCODE.length - 1; i >= 0; i--) {
            instructionDoneCharArray[bitCounter] = tOPCODE[i];
            bitCounter--;
        }
        for (int i = trd.length - 1; i >= 0; i--) {
            instructionDoneCharArray[bitCounter] = trd[i];
            bitCounter--;
        }
        for (int i = paddedImm.length - 13; i >= 0; i--) {
            instructionDoneCharArray[bitCounter] = paddedImm[i];
            bitCounter--;
        }
        return new String(instructionDoneCharArray);
    }

    private String JtypeBuild(char[] tOPCODE, char[] trd, char[] Imm) {
        int bitCounter = 31;
        char[] paddedImm = pad0BinaryToLeft(Imm, 21);

        char[] imm1_10 = new char[10];
        char imm11;
        char[] imm12_19 = new char[8];
        char imm20;

        int imm_count = 0;

        imm20 = paddedImm[imm_count];
        imm_count++;

        for(int i = 0; i < 8; i++) {
            imm12_19[i] = paddedImm[imm_count];
            imm_count++;
        }
        imm11 = paddedImm[imm_count];
        imm_count++;

        for(int i = 0; i < 10; i++) {
            imm1_10[i] = paddedImm[imm_count];
            if(imm_count < 21) {
                imm_count++;
            }
        }

        char[] instructionDoneCharArray = new char[32];
        for (int i = tOPCODE.length - 1; i >= 0; i--) {
            instructionDoneCharArray[bitCounter] = tOPCODE[i];
            bitCounter--;
        }
        for (int i = trd.length - 1; i >= 0; i--) {
            instructionDoneCharArray[bitCounter] = trd[i];
            bitCounter--;
        }
        for (int i = imm12_19.length - 1; i >= 0; i--) {
            instructionDoneCharArray[bitCounter] = imm12_19[i];
            bitCounter--;
        }
        instructionDoneCharArray[bitCounter] = imm11;
        bitCounter--;
        for (int i = imm1_10.length - 1; i >= 0; i--) {
            instructionDoneCharArray[bitCounter] = imm1_10[i];
            bitCounter--;
        }
        instructionDoneCharArray[bitCounter] = imm20;
        return new String(instructionDoneCharArray);
    }




    private String buildInstruction(String instruction) throws BadLocationException {
        String whichType = "";
        String machInstruction = "";

        char[] tOPCODE;
        char[] tfunct3;
        char[] tfunct7;
        char[] trs1;
        char[] trs2;
        char[] trd;


        String localWhichType;
        whichType = checkWhichType(instruction);
        localWhichType = whichType;

        tOPCODE = OPCODE.get(whichType).toCharArray();
        try {
            tfunct3 = funct3.get(splitInstruction(instruction, "command")).toCharArray();
        } catch (Exception e) {
            tfunct3 = "000".toCharArray();
        }
        try {
            trd = regAddr.get(splitInstruction(instruction, "x1")).toCharArray();
        } catch (Exception e) {
            trd = "00000".toCharArray();
        }
        try {
            if(whichType.equals("Stype") || whichType.equals("Btype")) {
                trs1 = regAddr.get(splitInstruction(instruction, "x1")).toCharArray();
            }
            else {
                trs1 = regAddr.get(splitInstruction(instruction, "x2")).toCharArray();
            }
        } catch (Exception e) {
            trs1 = "00000".toCharArray();
        }
        try {
            if(whichType.equals("Stype") || whichType.equals("Btype")) {
                trs2 = regAddr.get(splitInstruction(instruction, "x2")).toCharArray();
            }
            else {
                trs2 = regAddr.get(splitInstruction(instruction, "x3")).toCharArray();
            }
        } catch (Exception e) {
            trs2 = "00000".toCharArray();
        }
        try {
            tfunct7 = funct7.get(splitInstruction(instruction, "command")).toCharArray();
        } catch (Exception e) {
            tfunct7 = "0000000".toCharArray();
        }


        /*shamt for SLLI, SRLI, SRAI*/
        int tshamt_int = 0;
        String tshamt_string = "";
        char[] tshamt;
        if(whichType.equals("Rtype_Imm")) {
            try {
                tshamt_int = Integer.parseInt(splitInstruction(instruction, "x3").strip());
            } catch (Exception e) {
                produceReportComment("IMMEDIATE SHIFT AMOUT must be a number value between 0 and 31", redItalic);
                ERROR = true;
            }
            if(tshamt_int > 31) {
                produceReportComment("IMMEDIATE SHIFT AMOUNT can't be greater than 31", redItalic);
                ERROR = true;
            }
        }
        else {
            tshamt_int = Integer.parseInt("31");
        }
        tshamt_string = Integer.toBinaryString(tshamt_int);
        tshamt = tshamt_string.toCharArray();
        /*##########################################################################################################*/

        /*imm for I type*/
        int ItypeImm_int = 0;
        String ItypeImm_string = "";
        char[] ItypeImm;
        char[] ItypeImm_final_nonBtype = new char[12];
        char[] ItypeImm_final_Btype = new char[13];
        char[] ItypeImm_final_Utype = new char[32];
        char[] ItypeImm_final_Jtype = new char[21];

        String toFunctiom = "";
        String typeToFunctio_string = "";
        char[] toFunctionCharArr = new char[12];
        boolean itsAfunctionFLAG = false;

        if(whichType.equals("Itype_load") || whichType.equals("Itype_ALU")
                || whichType.equals("Itype_JALR") || whichType.equals("Stype")
                || whichType.equals("Btype") ||
                whichType.equals("Utype_LUI") || whichType.equals("Utype_AUIPC")
                || whichType.equals("Jtype") || whichType.equals("CSRtype")) {

            if(whichType.equals("Stype")) {
                localWhichType = "STYPE";
            }
            else if(whichType.equals("Btype")) {
                localWhichType = "BTYPE";

            }
            else if(whichType.equals("Utype_LUI") || whichType.equals("Utype_AUIPC")) {
                localWhichType = "UTYPE";
            }
            else if(whichType.equals("Jtype")) {
                localWhichType = "JTYPE";
            }
            else if(whichType.equals("CSRtype")) {
                localWhichType = "CSRTYPE";
            }
            else {
                localWhichType = "ITYPE";
            }

            try {
                if(splitInstruction(instruction, "x3").strip().startsWith("@")) {
                    toFunctiom = splitInstruction(instruction, "x3");
                    typeToFunctio_string = Integer.toBinaryString(functionStartAddress.get(toFunctiom));
                    toFunctionCharArr = typeToFunctio_string.toCharArray();
                    ItypeImm_int = Integer.parseInt("0");
                    itsAfunctionFLAG = true;
                }
                else {
                    ItypeImm_int = Integer.parseInt(splitInstruction(instruction, "x3").strip());
                    itsAfunctionFLAG = false;
                }
            } catch (Exception e) {
                produceReportComment("IMMEDIATES must be a number value or a function name", redItalic);
                ERROR = true;
            }


            if(whichType.equals("Btype")) {
                if(ItypeImm_int > 4095 || ItypeImm_int < -4096) {
                    produceReportComment("BRANCH IMMEDIATE must be a number value between -4096 and 4095", redItalic);
                    ERROR = true;
                }
            }
            else if(whichType.equals("Utype_LUI") || whichType.equals("Utype_AUIPC")) {
                if(ItypeImm_int > 2147483646 || ItypeImm_int <= -2147483646) {
                    produceReportComment("LUI or AUIPC IMMEDIATES must be a number value between -2147483646 and 2147483646", redItalic);
                    ERROR = true;
                }
            }
            else if(whichType.equals("Jtype")) {
                if(ItypeImm_int > 1048575 || ItypeImm_int <= -1048576) {
                    produceReportComment("JAL IMMEDIATES must be a number value between -1048576 and 1048575", redItalic);
                    ERROR = true;
                }
            }
            else {
                if(ItypeImm_int > 2047 || ItypeImm_int < -2048) {
                    produceReportComment("LOAD OR ARITHMETIC IMMEDIATE must be a number value between -2048 and 2047", redItalic);
                    ERROR = true;
                }
            }
        }
        else {
            ItypeImm_int = Integer.parseInt("4095");
        }

        /*PRINT!*/



        ItypeImm_string = Integer.toBinaryString(ItypeImm_int);
        ItypeImm = ItypeImm_string.toCharArray();

        /*btype has a 13 bit immediate to take care of, while the R, S and I types have 12 bits*/

        if(whichType.equals("Btype")) {
            if (ItypeImm.length > 13) {
                int j = 12;
                for (int i = ItypeImm.length - 1; i >= 19; i--) {
                    ItypeImm_final_Btype[j] = ItypeImm[i];
                    j--;
                }
            }
            else {
                if(itsAfunctionFLAG) {
                    ItypeImm_final_Btype = toFunctionCharArr;
                    itsAfunctionFLAG = false;
                } else {
                    ItypeImm_final_Btype = ItypeImm;
                }
            }
        }
        else if(whichType.equals("Jtype")) {
            if (ItypeImm.length > 21) {
                int j = 20;
                for (int i = ItypeImm.length - 1; i >= 11; i--) {
                    ItypeImm_final_Jtype[j] = ItypeImm[i];
                    j--;
                }
                j = 20;
            }
            else {
                ItypeImm_final_Jtype = ItypeImm;
            }
        }
        else {
            if(ItypeImm.length > 12) {
                int j = 11;
                for(int i = ItypeImm.length - 1; i >= 20; i--) {
                    ItypeImm_final_nonBtype[j] = ItypeImm[i];
                    j--;
                }
                j = 11;
            }
            else {
                if(itsAfunctionFLAG) {
                    ItypeImm_final_nonBtype = toFunctionCharArr;
                    itsAfunctionFLAG = false;
                }
                else {
                    ItypeImm_final_nonBtype = ItypeImm;
                }
            }
        }

        ItypeImm_final_Utype = ItypeImm;

        /*##########################################################################################################*/
        if(whichType.equals("Rtype_nonImm") || whichType.equals("Rtype_Imm")) {
            localWhichType = "RTYPE";
        }

        switch (localWhichType) {
            case "RTYPE":
                machInstruction = RtypeBuild(whichType, tOPCODE, trd, tfunct3, trs1, trs2, tfunct7, tshamt);
                break;
            case "ITYPE":
            case "CSRTYPE":
                machInstruction = ItypeBuild(tOPCODE, trd, tfunct3, trs1, ItypeImm_final_nonBtype);
                break;
            case "STYPE":
                machInstruction = StypeBuild(tOPCODE, trs2, tfunct3, trs1, ItypeImm_final_nonBtype);
                break;
            case "BTYPE":
                machInstruction = BtypeBuild(tOPCODE, trs2, tfunct3, trs1, ItypeImm_final_Btype);
                break;
            case "UTYPE":
                machInstruction = UtypeBuild(tOPCODE, trd, ItypeImm_final_Utype);
                break;
            case "JTYPE":
                machInstruction = JtypeBuild(tOPCODE, trd, ItypeImm_final_Jtype);
                break;

            default:
                ERROR = true;
                produceReportComment("No such instruction type: " + whichType,
                        redItalic);
                break;
        }
        return machInstruction;
    }

    private void startCompiling(String[] lines) throws BadLocationException {
        if(checkIfMain(lines) && checkIfEnd(lines)) {
            produceReportComment("END and MAIN are found!", greenItalic);
            splitIntoFunction(lines);
            logFunctionStartAddresses(functionHashMap);
            System.out.println(functionStartAddress); // CHECK
            buildMachineCode();
        }
        else {
            ERROR = true;
            produceReportComment("oops... The MAIN or END function was not found," +
                    "or MAIN ins't set as the first function and END isn't set as the last.", redItalic);
        }
    }

    public void compile() throws BadLocationException {
        docReport.insertString(docReport.getLength(),
                "\n\n" + "__________________________________" + "\n", blackBold);
        docReport.insertString(docReport.getLength(),
                "[ COMPILING... ]" + "\n\n", blackBold);
        MACHINE_INSTRUCTIONS = new ArrayList<>();
        checkSyntax();
        isError();

    }
}
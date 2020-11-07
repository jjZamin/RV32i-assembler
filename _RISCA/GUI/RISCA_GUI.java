package GUI;

import Commands.Commands;
import SyntaxHighLite.RV32i_syntax;
import Tools.FileHandler;
import Compiler.RV32iCompiler;

import javax.swing.text.BadLocationException;
import java.awt.event.KeyEvent;

public class RISCA_GUI extends javax.swing.JFrame {

    private String l = "hej";
    private FileHandler fileHandler = new FileHandler();
    private Commands commands = new Commands(l);
    private RV32iCompiler compiler = new RV32iCompiler();
    private RV32i_syntax rv32i_syntax = new RV32i_syntax();
    private String commandText;

    public RISCA_GUI() {
        initComponents();
        commands.setReportPane(reportTextArea);
        commands.setNotesPane(notesTextArea);
        commands.setAssemblyPan(assemblyTextArea);
        commands.setMachinePane(machineTextArea);
        compiler.setReportPane(reportTextArea);
        compiler.setAssemblyPan(assemblyTextArea);
        compiler.setMachinePane(machineTextArea);
        compiler.setMachineSyntax();
        compiler.setReportSyntax();
    }
    public void startSyntaxColoring() {
        rv32i_syntax.setCodeArea(assemblyTextArea);
        rv32i_syntax.setAttributes();
        rv32i_syntax.start();
    }

    private void initComponents() {

        commandLine = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        assemblyTextArea = new javax.swing.JTextPane();
        jScrollPane6 = new javax.swing.JScrollPane();
        machineTextArea = new javax.swing.JTextPane();
        jScrollPane7 = new javax.swing.JScrollPane();
        reportTextArea = new javax.swing.JTextPane();
        jScrollPane8 = new javax.swing.JScrollPane();
        notesTextArea = new javax.swing.JTextPane();
        MenuBar = new javax.swing.JMenuBar();
        File = new javax.swing.JMenu();
        menuNewProject = new javax.swing.JMenuItem();
        menuOpenProject = new javax.swing.JMenuItem();
        menuSaveProject = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        menuExit = new javax.swing.JMenuItem();
        Build = new javax.swing.JMenu();
        menGenMachineCode = new javax.swing.JMenuItem();
        Settings = new javax.swing.JMenu();
        Themes = new javax.swing.JMenu();
        menuLightTheme = new javax.swing.JCheckBoxMenuItem();
        menuDarkTheme = new javax.swing.JCheckBoxMenuItem();
        riscV = new javax.swing.JMenu();
        menuRiscVersion = new javax.swing.JCheckBoxMenuItem();
        About = new javax.swing.JMenu();
        menuISA = new javax.swing.JMenu();
        menuIsaVersion = new javax.swing.JMenu();
        RV32I = new javax.swing.JMenuItem();
        menuCommands = new javax.swing.JMenuItem();
        menuAbout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        commandLine.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                try {
                    commandLineKeyPressed(evt);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }
        });

        assemblyTextArea.setBorder(null);
        jScrollPane1.setViewportView(assemblyTextArea);

        jTabbedPane1.addTab("Assembly", jScrollPane1);

        machineTextArea.setEditable(false);
        machineTextArea.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, java.awt.Color.green, null, java.awt.Color.cyan));
        jScrollPane6.setViewportView(machineTextArea);

        jTabbedPane1.addTab("Machine Code", jScrollPane6);

        reportTextArea.setEditable(false);
        reportTextArea.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, java.awt.Color.lightGray, null, java.awt.Color.gray));
        jScrollPane7.setViewportView(reportTextArea);

        jTabbedPane1.addTab("Report", jScrollPane7);

        notesTextArea.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.lightGray, null));
        jScrollPane8.setViewportView(notesTextArea);

        jTabbedPane1.addTab("Notes", jScrollPane8);

        File.setText("File");

        menuNewProject.setText("New Project");
        menuNewProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuNewProjectActionPerformed(evt);
            }
        });
        File.add(menuNewProject);

        menuOpenProject.setText("Open Project");
        menuOpenProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuOpenProjectActionPerformed(evt);
            }
        });
        File.add(menuOpenProject);

        menuSaveProject.setText("Save Project");
        menuSaveProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSaveProjectActionPerformed(evt);
            }
        });
        File.add(menuSaveProject);
        File.add(jSeparator1);

        menuExit.setText("Exit Project");
        menuExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuExitActionPerformed(evt);
            }
        });
        File.add(menuExit);

        MenuBar.add(File);

        Build.setText("Build");

        menGenMachineCode.setText("Generate Machine Code");
        menGenMachineCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    menGenMachineCodeActionPerformed(evt);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }
        });
        Build.add(menGenMachineCode);

        MenuBar.add(Build);

        Settings.setText("Settings");

        Themes.setText("Themes");

        menuLightTheme.setSelected(true);
        menuLightTheme.setText("Light");
        menuLightTheme.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                menuLightThemeStateChanged(evt);
            }
        });
        menuLightTheme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuLightThemeActionPerformed(evt);
            }
        });
        Themes.add(menuLightTheme);

        menuDarkTheme.setSelected(true);
        menuDarkTheme.setText("Dark");
        menuDarkTheme.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                menuDarkThemeItemStateChanged(evt);
            }
        });
        menuDarkTheme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuDarkThemeActionPerformed(evt);
            }
        });
        Themes.add(menuDarkTheme);

        Settings.add(Themes);

        riscV.setText("RISC-V");

        menuRiscVersion.setSelected(true);
        menuRiscVersion.setText("RV32I");
        menuRiscVersion.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                menuRiscVersionItemStateChanged(evt);
            }
        });
        menuRiscVersion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRiscVersionActionPerformed(evt);
            }
        });
        riscV.add(menuRiscVersion);

        Settings.add(riscV);

        MenuBar.add(Settings);

        About.setText("About");

        menuISA.setText("ISA");

        menuIsaVersion.setText("Version");

        RV32I.setText("RV32I");
        RV32I.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RV32IActionPerformed(evt);
            }
        });
        menuIsaVersion.add(RV32I);

        menuISA.add(menuIsaVersion);

        About.add(menuISA);

        menuCommands.setText("Commands");
        menuCommands.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCommandsActionPerformed(evt);
            }
        });
        About.add(menuCommands);

        menuAbout.setText("About");
        menuAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAboutActionPerformed(evt);
            }
        });
        About.add(menuAbout);

        MenuBar.add(About);

        setJMenuBar(MenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 735, Short.MAX_VALUE)
                                        .addComponent(commandLine))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(commandLine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>


    private void commandLineKeyPressed(java.awt.event.KeyEvent evt) throws BadLocationException {
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            commandText = commandLine.getText().strip();
            commands.setCommand(commandText);
            commandLine.setText("");
        }
    }

    private void menuSaveProjectActionPerformed(java.awt.event.ActionEvent evt) {
        fileHandler.saveProject(assemblyTextArea);
        setTitle(fileHandler.getFilename());
    }

    private void menuOpenProjectActionPerformed(java.awt.event.ActionEvent evt) {
        fileHandler.openProject(assemblyTextArea);
        setTitle(fileHandler.getFilename());
    }

    private void menuNewProjectActionPerformed(java.awt.event.ActionEvent evt) {
        notesTextArea.setText("");
        reportTextArea.setText("");
        assemblyTextArea.setText("");
        machineTextArea.setText("");
        commandLine.setText("");
        setTitle("New Project - Save, or RISC losing it!");
    }
    private void menuExitActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }

    private void menGenMachineCodeActionPerformed(java.awt.event.ActionEvent evt) throws BadLocationException {
        compiler.compile();
    }

    private void menuLightThemeActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void menuCommandsActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void menuAboutActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void RV32IActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void menuDarkThemeActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void menuLightThemeStateChanged(javax.swing.event.ChangeEvent evt) {
        if(!menuLightTheme.isSelected()) {
            menuDarkTheme.setSelected(true);
        }
        else {
            menuDarkTheme.setSelected(false);
            menuLightTheme.setSelected(true);
        }
    }

    private void menuDarkThemeItemStateChanged(java.awt.event.ItemEvent evt) {
        if(!menuDarkTheme.isSelected()) {
            menuLightTheme.setSelected(true);
        }
        else {
            menuDarkTheme.setSelected(true);
            menuLightTheme.setSelected(false);
        }
    }

    private void menuRiscVersionActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void menuRiscVersionItemStateChanged(java.awt.event.ItemEvent evt) {
        // TODO add your handling code here:
    }


    // Variables declaration - do not modify
    private javax.swing.JMenu About;
    private javax.swing.JMenu Build;
    private javax.swing.JMenu File;
    private javax.swing.JMenuBar MenuBar;
    private javax.swing.JMenuItem RV32I;
    private javax.swing.JMenu Settings;
    private javax.swing.JMenu Themes;
    private javax.swing.JTextPane assemblyTextArea;
    private javax.swing.JTextField commandLine;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextPane machineTextArea;
    private javax.swing.JMenuItem menGenMachineCode;
    private javax.swing.JMenuItem menuAbout;
    private javax.swing.JMenuItem menuCommands;
    private javax.swing.JCheckBoxMenuItem menuDarkTheme;
    private javax.swing.JMenuItem menuExit;
    private javax.swing.JMenu menuISA;
    private javax.swing.JMenu menuIsaVersion;
    private javax.swing.JCheckBoxMenuItem menuLightTheme;
    private javax.swing.JMenuItem menuNewProject;
    private javax.swing.JMenuItem menuOpenProject;
    private javax.swing.JCheckBoxMenuItem menuRiscVersion;
    private javax.swing.JMenuItem menuSaveProject;
    private javax.swing.JTextPane notesTextArea;
    private javax.swing.JTextPane reportTextArea;
    private javax.swing.JMenu riscV;
    // End of variables declaration
}

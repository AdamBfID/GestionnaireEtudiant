package graphic;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.io.FileWriter;
import java.io.IOException;


public class EnseignantInterface {
    // Custom colors
    private static final Color PRIMARY_DARK = new Color(8, 10, 32);
    private static final Color ACCENT_GREEN = new Color(47, 53, 66);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 250);
    private static final Color TABLE_HEADER_COLOR = new Color(30, 35, 65);
    private static final Color TABLE_ALTERNATE_ROW = new Color(240, 240, 245);

    private String numcin;

    public EnseignantInterface(String numcin) {
        this.numcin = numcin;

        try {
            // Set system look and feel for better integration
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Fetch teacher's info
        TeacherInfo teacherInfo = fetchTeacherInfo(numcin);
        if (teacherInfo == null) {
            JOptionPane.showMessageDialog(null, "Enseignant introuvable!", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create and setup main frame
        JFrame frame = new JFrame("Interface Enseignant");
        frame.setSize(1200, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(0, 20));
        frame.getContentPane().setBackground(BACKGROUND_COLOR);
        ImageIcon appIcon = new ImageIcon(LoginInterface.class.getResource("/resources/app_icon.png"));
        frame.setIconImage(appIcon.getImage());

        // Create welcome panel with gradient
        JPanel welcomePanel = createWelcomePanel(teacherInfo.getName());

        // Create table panel
        JPanel tablePanel = createTablePanel(teacherInfo);

        // Create button panel
        JPanel buttonPanel = createButtonPanel(teacherInfo);

        // Add panels to frame with margins
        frame.add(welcomePanel, BorderLayout.NORTH);
        frame.add(tablePanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Add padding around the frame
        ((JPanel)frame.getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel createWelcomePanel(String teacherName) {
        JPanel welcomePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(0, 0, PRIMARY_DARK, getWidth(), 0, 
                    new Color(PRIMARY_DARK.getRed() + 20, PRIMARY_DARK.getGreen() + 20, PRIMARY_DARK.getBlue() + 20));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        welcomePanel.setPreferredSize(new Dimension(0, 100));
        welcomePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 30));

        JLabel welcomeLabel = new JLabel("Bienvenue, " + teacherName + " !");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(ACCENT_GREEN);
        welcomePanel.add(welcomeLabel);

        return welcomePanel;
    }

    private JPanel createTablePanel(TeacherInfo teacherInfo) {
        JPanel tablePanel = new JPanel(new BorderLayout(0, 10));
        tablePanel.setBackground(BACKGROUND_COLOR);
        
        JLabel subtitleLabel = new JLabel("Liste des étudiants de la classe: " + teacherInfo.getClassID() + " ,et de matière: "+ teacherInfo.getSpec());
        subtitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        subtitleLabel.setForeground(PRIMARY_DARK);
        subtitleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        tablePanel.add(subtitleLabel, BorderLayout.NORTH);


        // Create and style table
        DefaultTableModel tableModel = new DefaultTableModel();
        JTable studentTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 4; // Only allow editing of grade columns
            }
        };

        // Add columns
        tableModel.addColumn("Matricule");
        tableModel.addColumn("Nom");
        tableModel.addColumn("Prenom");
        tableModel.addColumn("Email");
        tableModel.addColumn("NoteDS");
        tableModel.addColumn("NoteTP");
        tableModel.addColumn("NoteEx");

        // Style table
        studentTable.setRowHeight(30);
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        studentTable.setGridColor(new Color(200, 200, 200));
        studentTable.setSelectionBackground(ACCENT_GREEN);
        studentTable.setSelectionForeground(Color.BLACK);
        studentTable.setShowVerticalLines(true);
        studentTable.setShowHorizontalLines(true);

        // Style table header
        JTableHeader header = studentTable.getTableHeader();
        header.setBackground(TABLE_HEADER_COLOR);
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));

        // Add zebra striping
        studentTable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    component.setBackground(row % 2 == 0 ? Color.WHITE : TABLE_ALTERNATE_ROW);
                }
                return component;
            }
        });

        // Populate table
        populateStudentTable(tableModel, teacherInfo.getClassID());

        // Add table to scroll pane with custom styling
        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    private JPanel createButtonPanel(TeacherInfo teacherInfo) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton saveButton = new JButton("Enregistrer Notes");
        JButton printButton = new JButton("Imprimer");
        JButton exportButton = new JButton("Exporter en CSV");

        setupButtonStyle(saveButton);
        setupButtonStyle(printButton);
        setupButtonStyle(exportButton);

        // Add listeners for buttons
        saveButton.addActionListener(e -> {
            Component parent = SwingUtilities.getWindowAncestor(buttonPanel);
            if (parent instanceof JFrame) {
                JTable studentTable = findStudentTable((JFrame) parent);
                if (studentTable != null) {
                    saveNotes(studentTable, teacherInfo.getSpec());
                }
            }
        });

        printButton.addActionListener(e -> {
            Component parent = SwingUtilities.getWindowAncestor(buttonPanel);
            if (parent instanceof JFrame) {
                JTable studentTable = findStudentTable((JFrame) parent);
                if (studentTable != null) {
                    printTable(studentTable);
                }
            }
        });

        exportButton.addActionListener(e -> {
            Component parent = SwingUtilities.getWindowAncestor(buttonPanel);
            if (parent instanceof JFrame) {
                JTable studentTable = findStudentTable((JFrame) parent);
                if (studentTable != null) {
                    exportTableToCSV(studentTable);
                }
            }
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(printButton);
        buttonPanel.add(exportButton);

        return buttonPanel;
    }

    private void setupButtonStyle(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(200, 40));
        button.setBackground(ACCENT_GREEN);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(184, 216, 45));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(ACCENT_GREEN);
            }
        });
    }

    private JTable findStudentTable(JFrame frame) {
        for (Component comp : frame.getContentPane().getComponents()) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                for (Component innerComp : panel.getComponents()) {
                    if (innerComp instanceof JScrollPane) {
                        JScrollPane scrollPane = (JScrollPane) innerComp;
                        if (scrollPane.getViewport().getView() instanceof JTable) {
                            return (JTable) scrollPane.getViewport().getView();
                        }
                    }
                }
            }
        }
        return null;
    }
    
    

    // Existing methods remain unchanged
    private TeacherInfo fetchTeacherInfo(String numcin) {
        String url = "jdbc:mysql://localhost:3306/gestionnaire_etudiant";
        String user = "root";
        String pass = "root";

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String query = "SELECT nom, prenom, classID, spec FROM enseignant WHERE numcin = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, numcin);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("prenom") + " " + rs.getString("nom");
                String classID = rs.getString("classID");
                String spec = rs.getString("spec");
                return new TeacherInfo(name, classID, spec);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void populateStudentTable(DefaultTableModel tableModel, String classID) {
        String url = "jdbc:mysql://localhost:3306/gestionnaire_etudiant";
        String user = "root";
        String pass = "root";

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String query = "SELECT matricule, nom, prenom, email FROM etudiant WHERE classegmamID = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, classID);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String matricule = rs.getString("matricule");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String email = rs.getString("email");
                tableModel.addRow(new Object[]{matricule, nom, prenom, email, null, null, null});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void saveNotes(JTable table, String ecueID) {
        String url = "jdbc:mysql://localhost:3306/gestionnaire_etudiant";
        String user = "root";
        String pass = "root";

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String query = "INSERT INTO fichenotes (matricule, EcueID, NoteDS, NoteTP, NoteEx) " +
                           "VALUES (?, ?, ?, ?, ?) " +
                           "ON DUPLICATE KEY UPDATE NoteDS = VALUES(NoteDS), NoteTP = VALUES(NoteTP), NoteEx = VALUES(NoteEx)";
            PreparedStatement stmt = conn.prepareStatement(query);

            for (int i = 0; i < table.getRowCount(); i++) {
                String matricule = (String) table.getValueAt(i, 0);
                Object noteDS = table.getValueAt(i, 4);
                Object noteTP = table.getValueAt(i, 5);
                Object noteEx = table.getValueAt(i, 6);

                if (noteDS != null && noteTP != null && noteEx != null) {
                    stmt.setString(1, matricule);
                    stmt.setString(2, ecueID);
                    stmt.setDouble(3, Double.parseDouble(noteDS.toString()));
                    stmt.setDouble(4, Double.parseDouble(noteTP.toString()));
                    stmt.setDouble(5, Double.parseDouble(noteEx.toString()));
                    stmt.addBatch();
                }
            }

            stmt.executeBatch();
            JOptionPane.showMessageDialog(null, "Notes enregistrées avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de l'enregistrement des notes.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void printTable(JTable table) {
        try {
            boolean complete = table.print();
            if (complete) {
                JOptionPane.showMessageDialog(null, "Impression réussie!", "Succès", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Impression annulée.", "Info", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de l'impression.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportTableToCSV(JTable table) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Enregistrer le fichier CSV");
        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try (FileWriter csvWriter = new FileWriter(fileChooser.getSelectedFile() + ".csv")) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                int columnCount = model.getColumnCount();

                // Write column names
                for (int i = 0; i < columnCount; i++) {
                    csvWriter.write(model.getColumnName(i) + (i < columnCount - 1 ? "," : ""));
                }
                csvWriter.write("\n");

                // Write rows
                for (int row = 0; row < model.getRowCount(); row++) {
                    for (int col = 0; col < columnCount; col++) {
                        csvWriter.write(model.getValueAt(row, col) + (col < columnCount - 1 ? "," : ""));
                    }
                    csvWriter.write("\n");
                }

                JOptionPane.showMessageDialog(null, "Exportation réussie!", "Succès", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erreur lors de l'exportation.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    static class TeacherInfo {
        private String name;
        private String classID;
        private String spec;

        public TeacherInfo(String name, String classID, String spec) {
            this.name = name;
            this.classID = classID;
            this.spec = spec;
        }

        public String getName() {
            return name;
        }

        public String getClassID() {
            return classID;
        }

        public String getSpec() {
            return spec;
        }
    }
}
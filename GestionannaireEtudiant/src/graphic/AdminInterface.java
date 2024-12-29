package graphic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class AdminInterface extends JFrame {

	private static final long serialVersionUID = 1L;
	private static AdminInterface instance;
	private JPanel mainPanel, sideNavPanel, contentPanel;
    private CardLayout cardLayout;
    private Connection conn;
    private Color primaryColor = new Color(47, 53, 66); 
    private Color accentColor = new Color(86, 101, 115);
    private Color textColor = new Color(236, 240, 241);

    
    public AdminInterface() {
    	instance = this; 
        initializeDatabase();
        setupMainFrame();
        createSideNav();
        createContentPanels();
        setVisible(true);
    }
    public static void disposeAdmin() {
        if (instance != null) {
            instance.dispose();
        }
    }

    private void initializeDatabase() {
        try {
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/gestionnaire_etudiant",
                "root", "root"
            );
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection failed!");
        }
    }

    private void setupMainFrame() {
        setTitle("GMAM Admin Dashboard");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        ImageIcon appIcon = new ImageIcon(getClass().getResource("/resources/app_icon.png"));
        setIconImage(appIcon.getImage());

        mainPanel = new JPanel(new BorderLayout());

    	contentPanel = new JPanel();
    	cardLayout = new CardLayout();
    	contentPanel.setLayout(cardLayout);
    	contentPanel.setBackground(Color.WHITE);

	   mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void createSideNav() {
    	sideNavPanel = new JPanel();
        sideNavPanel.setPreferredSize(new Dimension(280, getHeight()));
        sideNavPanel.setBackground(new Color(31, 41, 55));
        sideNavPanel.setLayout(new BoxLayout(sideNavPanel, BoxLayout.Y_AXIS));

        sideNavPanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

      
        
        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBackground(new Color(31, 41, 55));
        logoPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));

        JLabel logoIcon = new JLabel("👨‍🎓");  
        logoIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        logoIcon.setForeground(Color.WHITE);
        logoIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("GMAM Admin");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Panneau d'administration");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(156, 163, 175));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoPanel.add(logoIcon);
        logoPanel.add(Box.createVerticalStrut(10));
        logoPanel.add(titleLabel);
        logoPanel.add(Box.createVerticalStrut(5));
        logoPanel.add(subtitleLabel);

        sideNavPanel.add(logoPanel);
        sideNavPanel.add(Box.createVerticalStrut(20));

      
        String[] navItems = {"Etudiants", "Enseignent", "Matière", "classe"};
        for (String item : navItems) {
            JButton navButton = createStyledNavButton(item);
            sideNavPanel.add(navButton);
            sideNavPanel.add(Box.createVerticalStrut(20));
        }
        
        sideNavPanel.add(Box.createVerticalGlue());
        JButton logoutButton = createStyledNavButton("Déconnexion");
        logoutButton.setBackground(new Color(185, 28, 28)); 
        sideNavPanel.add(logoutButton);
        logoutButton.addActionListener(e-> { SwingUtilities.invokeLater(() -> new LoginInterface());AdminInterface.disposeAdmin();});
        mainPanel.add(sideNavPanel, BorderLayout.WEST);
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2d.setColor(accentColor.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(accentColor);
                } else {
                    g2d.setColor(primaryColor);
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };

        button.setForeground(textColor); 
        button.setFont(new Font("Arial", Font.PLAIN, 14)); 
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40));

        button.addActionListener(e -> cardLayout.show(contentPanel, text));
        
        return button;
    }
    private JButton createStyledNavButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(55, 65, 81));  
        button.setMaximumSize(new Dimension(250, 45));
        button.setPreferredSize(new Dimension(250, 45));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
       
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(75, 85, 99)); 
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(55, 65, 81));  
            }
        });
        button.addActionListener(e -> cardLayout.show(contentPanel, text));

        return button;
    }

    public void setActiveNavButton(JButton activeButton) {
        // Reset all buttons
        for (Component comp : sideNavPanel.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                button.setBackground(new Color(55, 65, 81));
            }
        }

        if (activeButton != null) {
            activeButton.setBackground(new Color(79, 70, 229)); 
        }
    }

    private void createContentPanels() {
        createStudentPanel();
        createEnseignantPanel();
        createEcuePanel();
        createClassPanel();
    }
    
    private void addComboBoxField(JPanel panel, String label, JComboBox<String> field, GridBagConstraints gbc, int gridy) {
        gbc.gridx = 0;
        gbc.gridy = gridy;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        field.setPreferredSize(new Dimension(200, 25)); 
        panel.add(field, gbc);
    }
    
    private void clearFields(Component... components) {
        for (Component component : components) {
            if (component instanceof JTextField) {
                ((JTextField) component).setText("");
            } else if (component instanceof JPasswordField) {
                ((JPasswordField) component).setText("");
            } else if (component instanceof JComboBox) {
                ((JComboBox<?>) component).setSelectedIndex(0);
            }
        }
    }

    private void createStudentPanel() {
        JPanel studentPanel = new JPanel(new BorderLayout());
        studentPanel.setBackground(Color.WHITE);
        studentPanel.setBorder(BorderFactory.createTitledBorder("Ajouter un etudiant"));

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        model.addColumn("Matricule");
        model.addColumn("Nom");
        model.addColumn("Prenom");
        model.addColumn("NumCin");
        model.addColumn("DateNaissance");
        model.addColumn("Email");
        model.addColumn("adresse");
        model.addColumn("Phone");
        model.addColumn("classID");
     
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField matriculeField = new JTextField(20);
        JTextField nomField = new JTextField(20);
        JTextField prenomField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField phoneField = new JTextField(20);
        JTextField cinField = new JTextField(20);
        JTextField dobField = new JTextField(20);
        JTextField addressField = new JTextField(20);
        JComboBox<String> classIdField = new JComboBox<>(new String[]{"A1", "B1", "A2", "B2", "3FM", "3TS"});
        JPasswordField passwordField = new JPasswordField(20);
        
        

        addFormField(formPanel, "CIN:                                            ", cinField, gbc, 5);
        addFormField(formPanel, "Date of Birth (YYYY-MM-DD):                      ", dobField, gbc, 6);
        addFormField(formPanel, "Address:                                         ", addressField, gbc, 7);
        addComboBoxField(formPanel, "Class ID:                                             ", classIdField, gbc, 8);
        addFormField(formPanel, "Password:", passwordField, gbc, 9);
        addFormField(formPanel, "Matricule:", matriculeField, gbc, 0);
        addFormField(formPanel, "Nom:", nomField, gbc, 1);
        addFormField(formPanel, "Prenom:", prenomField, gbc, 2);
        addFormField(formPanel, "Email:", emailField, gbc, 3);
        addFormField(formPanel, "Phone:", phoneField, gbc, 4);
   

        JPanel buttonPanel = new JPanel(new FlowLayout());
        

        JButton addButton = createStyledButton("Ajouter");
        JButton updateButton = createStyledButton("Modifier");
        JButton deleteButton = createStyledButton("Supprimer");


        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
 

        addButton.addActionListener(e -> {
            try {
            	
                String sql = "INSERT INTO etudiant (matricule, Nom, Prenom, numCin, DateNaissance, email, adresse, numtel, ClassegmamID, motdepasse) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setLong(1, Integer.parseInt(matriculeField.getText()));
                stmt.setString(2, nomField.getText());
                stmt.setString(3, prenomField.getText());
                stmt.setString(4, cinField.getText());
                stmt.setString(5, dobField.getText());
                stmt.setString(6, emailField.getText());
                stmt.setString(7, addressField.getText());
                stmt.setString(8, phoneField.getText());
                stmt.setString(9, (String) classIdField.getSelectedItem()); 
                stmt.setString(10, new String(passwordField.getPassword()));

                stmt.executeUpdate();
                refreshStudentTable(model);
                clearFields(matriculeField, nomField, prenomField, cinField, 
                        dobField, emailField, addressField, phoneField, 
                        classIdField, passwordField);

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error adding student!");
            }
        });
        deleteButton.addActionListener(e -> {
            try {
                String matriculeText = matriculeField.getText().trim();
                
             
                if (matriculeText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Matricule field cannot be empty. Please enter a valid matricule.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return; 
                }
                int matricule = Integer.parseInt(matriculeText);
                String sql = "DELETE FROM etudiant WHERE matricule = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, matricule);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Student deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "No student found with the given matricule.", "Not Found", JOptionPane.WARNING_MESSAGE);
                }
                refreshStudentTable(model);
                matriculeField.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid matricule. Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting student: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        updateButton.addActionListener(e -> {
            try {
                String sql = "UPDATE etudiant SET Nom = ?, Prenom = ?, numCin = ?, DateNaissance = ?, email = ?, adresse = ?, numtel = ?, ClassegmamID = ?, motdepasse = ? WHERE matricule = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, nomField.getText());
                stmt.setString(2, prenomField.getText());
                stmt.setString(3, cinField.getText());
                stmt.setString(4, dobField.getText());
                stmt.setString(5, emailField.getText());
                stmt.setString(6, addressField.getText());
                stmt.setString(7, phoneField.getText());
                stmt.setString(9, (String) classIdField.getSelectedItem()); 
                stmt.setString(10, new String(passwordField.getPassword())); 
                stmt.setInt(10, Integer.parseInt(matriculeField.getText()));

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Student updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "No student found with the given matricule.");
                }
                refreshStudentTable(model);
                clearFields(matriculeField, nomField, prenomField, cinField, dobField, emailField, addressField, phoneField, classIdField, passwordField);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating student!");
            }
        });


        studentPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        studentPanel.add(formPanel, BorderLayout.NORTH);
        studentPanel.add(buttonPanel, BorderLayout.SOUTH);

        contentPanel.add(studentPanel, "Etudiants");
        refreshStudentTable(model);
    }
    

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.BLACK);
        button.setBackground(new Color(47, 53, 66));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void addFormField(JPanel panel, String label, JTextField field, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void refreshStudentTable(DefaultTableModel model) {
        try {
            model.setRowCount(0); 
            String sql = "SELECT matricule, Nom, Prenom, numCin, DateNaissance, email, adresse, numtel, ClassegmamID FROM etudiant";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getLong("matricule"));
                row.add(rs.getString("Nom"));
                row.add(rs.getString("Prenom"));
                row.add(rs.getString("numCin"));
                row.add(rs.getDate("DateNaissance"));
                row.add(rs.getString("email"));
                row.add(rs.getString("adresse"));
                row.add(rs.getString("numtel"));
                row.add(rs.getString("ClassegmamID"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error refreshing table!");
        }
    }
    private void createEnseignantPanel() {
        JPanel EnseignantPanel = new JPanel(new BorderLayout());
        EnseignantPanel.setBackground(Color.WHITE);
        EnseignantPanel.setBorder(BorderFactory.createTitledBorder("Ajouter un enseignent"));
        contentPanel.add(EnseignantPanel, "Enseignent");

    
        DefaultTableModel model2 = new DefaultTableModel();
        JTable table = new JTable(model2);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);
        EnseignantPanel.add(scrollPane, BorderLayout.CENTER);

        model2.addColumn("NumCin");
        model2.addColumn("Nom");
        model2.addColumn("Prenom");
        model2.addColumn("Email");
        model2.addColumn("Specialité");
        model2.addColumn("ClassID");

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField numcinField = new JTextField(20);
        JTextField nomField = new JTextField(20);
        JTextField prenomField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField specField = new JTextField(20);
        JComboBox<String> classIdField = new JComboBox<>(new String[]{"A1", "B1", "A2", "B2", "3FM", "3TS"});
        JTextField passwordField = new JTextField(20);

        addFormField(formPanel, "Num CIN:", numcinField, gbc, 0);
        addFormField(formPanel, "Nom:", nomField, gbc, 1);
        addFormField(formPanel, "Prenom:", prenomField, gbc, 2);
        addFormField(formPanel, "Email:", emailField, gbc, 3);
        addFormField(formPanel, "Spécialité:", specField, gbc, 4);
        addComboBoxField(formPanel, "ClassID:", classIdField, gbc, 8);
        addFormField(formPanel, "Password:", passwordField, gbc, 6);

        EnseignantPanel.add(formPanel, BorderLayout.NORTH);

     
        JPanel buttonPanel1 = new JPanel(new FlowLayout());
        buttonPanel1.setBackground(Color.WHITE);

        JButton addButton1 = createStyledButton("Ajouter");
        JButton updateButton1 = createStyledButton("Modifier");
        JButton deleteButton1 = createStyledButton("Supprimer");

        buttonPanel1.add(addButton1);
        buttonPanel1.add(updateButton1);
        buttonPanel1.add(deleteButton1);

        EnseignantPanel.add(buttonPanel1, BorderLayout.SOUTH);

    
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    numcinField.setText(table.getValueAt(selectedRow, 0).toString());
                    nomField.setText(table.getValueAt(selectedRow, 1).toString());
                    prenomField.setText(table.getValueAt(selectedRow, 2).toString());
                    emailField.setText(table.getValueAt(selectedRow, 3).toString());
                    specField.setText(table.getValueAt(selectedRow, 4).toString());
                    
                }
            }
        });


        addButton1.addActionListener(e -> {
            
            
            try {
                String sql = "INSERT INTO enseignant (numcin, nom, prenom, email, spec, classID, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);

                stmt.setString(1, numcinField.getText().trim());
                stmt.setString(2, nomField.getText().trim());
                stmt.setString(3, prenomField.getText().trim());
                stmt.setString(4, emailField.getText().trim());
                stmt.setString(5, specField.getText().trim());
                stmt.setString(6, (String) classIdField.getSelectedItem());
                stmt.setString(7, passwordField.getText().trim());

                stmt.executeUpdate();
                refreshEnseignantTable(model2);
                clearFields(numcinField, nomField, prenomField, emailField, specField, classIdField, passwordField);
                JOptionPane.showMessageDialog(this, "Enseignant ajouté avec succès!");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de l'enseignant!");
            }
        });

        updateButton1.addActionListener(e -> {
           

            try {
                String sql = "UPDATE enseignant SET nom = ?, prenom = ?, email = ?, spec = ?, classID = ?, password = ? WHERE numcin = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
               
                stmt.setString(1, nomField.getText().trim());
                stmt.setString(2, prenomField.getText().trim());
                stmt.setString(3, emailField.getText().trim());
                stmt.setString(4, specField.getText().trim());
                stmt.setString(5, (String) classIdField.getSelectedItem());
                stmt.setString(6, passwordField.getText().trim());
                stmt.setString(7, numcinField.getText().trim());

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Enseignant modifié avec succès!");
                    refreshEnseignantTable(model2);
                    clearFields(numcinField, nomField, prenomField, emailField, specField, classIdField, passwordField);
                } else {
                    JOptionPane.showMessageDialog(this, "Aucun enseignant trouvé avec ce numéro CIN.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification de l'enseignant!");
            }
        });

        deleteButton1.addActionListener(e -> {
            String numcinText = numcinField.getText().trim();
            
            if (numcinText.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Le numéro CIN ne peut pas être vide.", 
                    "Erreur de saisie", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                String sql = "DELETE FROM enseignant WHERE numcin = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, numcinText);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, 
                        "Enseignant supprimé avec succès", 
                        "Succès", 
                        JOptionPane.INFORMATION_MESSAGE);
                    refreshEnseignantTable(model2);
                    clearFields(numcinField, nomField, prenomField, emailField, specField, classIdField, passwordField);
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Aucun enseignant trouvé avec ce numéro CIN", 
                        "Non trouvé", 
                        JOptionPane.WARNING_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "Erreur : " + ex.getMessage(), 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    

        EnseignantPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        EnseignantPanel.add(formPanel, BorderLayout.NORTH);
        EnseignantPanel.add(buttonPanel1, BorderLayout.SOUTH);

        contentPanel.add(EnseignantPanel, "Enseignent");
        refreshEnseignantTable(model2);
    }
  
    private void refreshEnseignantTable(DefaultTableModel model) {
        try {
            model.setRowCount(0); 
            String sql = "SELECT numcin, prenom, nom, email, spec, classID, password FROM enseignant";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("numcin"));
                row.add(rs.getString("prenom"));
                row.add(rs.getString("nom"));
                row.add(rs.getString("email"));
                row.add(rs.getString("spec"));
                row.add(rs.getString("classID"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error refreshing table!");
        }
    }
    
        
        

    private void createEcuePanel() {
        JPanel ecuePanel = new JPanel();
        ecuePanel.setBackground(Color.WHITE);
        ecuePanel.setLayout(new BorderLayout());
        contentPanel.add(ecuePanel, "Matière");

       
        JPanel inputPanel = new JPanel(new GridLayout(9, 2));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Ajouter Matière"));
        inputPanel.setBackground(Color.WHITE);

        JTextField txtMatiereID = new JTextField();
        JTextField txtNom = new JTextField();
        JTextField txtVhCour = new JTextField();
        JTextField txtVhTP = new JTextField();
        JTextField txtVhTD = new JTextField();
        JTextField txtCredit = new JTextField();
        JTextField txtCoefficient = new JTextField();
        JComboBox<String> txtRegime = new JComboBox<>(new String[]{"continue", "mixte"});
        JTextField txtModuleID = new JTextField();

        inputPanel.add(new JLabel("Matière ID:"));
        inputPanel.add(txtMatiereID);
        inputPanel.add(new JLabel("Nom:"));
        inputPanel.add(txtNom);
        inputPanel.add(new JLabel("VH Cours:"));
        inputPanel.add(txtVhCour);
        inputPanel.add(new JLabel("VH TP:"));
        inputPanel.add(txtVhTP);
        inputPanel.add(new JLabel("VH TD:"));
        inputPanel.add(txtVhTD);
        inputPanel.add(new JLabel("Crédit:"));
        inputPanel.add(txtCredit);
        inputPanel.add(new JLabel("Coefficient:"));
        inputPanel.add(txtCoefficient);
        inputPanel.add(new JLabel("Régime:"));
        inputPanel.add(txtRegime);
        inputPanel.add(new JLabel("Module ID:"));
        inputPanel.add(txtModuleID);
        
        JPanel buttonPanel1 = new JPanel(new FlowLayout());
        buttonPanel1.setBackground(Color.WHITE);

        JButton btnAdd = createStyledButton("Ajouter");
        JButton btnRefresh = createStyledButton("Modifier");
        JButton btnDelete = createStyledButton("Supprimer");

       

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnDelete);

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"Matière ID", "Nom", "VH Cours", "VH TP", "VH TD", "Crédit", "Coefficient", "Régime", "Module ID"});
        JTable table = new JTable(tableModel);

        ecuePanel.add(inputPanel, BorderLayout.NORTH);
        ecuePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        ecuePanel.add(buttonPanel, BorderLayout.SOUTH);

        final String url = "jdbc:mysql://localhost:3306/gestionnaire_etudiant";
        final String user = "root";
        final String pass = "root";

   
        loadMatieres(tableModel, url, user, pass);

        btnAdd.addActionListener(e -> {
            try (Connection conn = DriverManager.getConnection(url, user, pass)) {
                String query = "INSERT INTO matiere (MatiéreID, Nom, vh_cour, vh_TP, vh_TD, credit, coefficient, regime, ModuleID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);

                stmt.setString(1, txtMatiereID.getText());
                stmt.setString(2, txtNom.getText());
                stmt.setInt(3, Integer.parseInt(txtVhCour.getText()));
                stmt.setInt(4, Integer.parseInt(txtVhTP.getText()));
                stmt.setInt(5, Integer.parseInt(txtVhTD.getText()));
                stmt.setInt(6, Integer.parseInt(txtCredit.getText()));
                stmt.setInt(7, Integer.parseInt(txtCoefficient.getText()));
                stmt.setString(8, (String) txtRegime.getSelectedItem());
                stmt.setString(9, txtModuleID.getText());

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(ecuePanel, "Matière ajoutée avec succès!");
                clearInputFields(txtMatiereID, txtNom, txtVhCour, txtVhTP, txtVhTD, txtCredit, txtCoefficient, txtModuleID);
                loadMatieres(tableModel, url, user, pass);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(ecuePanel, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });


        btnRefresh.addActionListener(e -> loadMatieres(tableModel, url, user, pass));
    }

    private void loadMatieres(DefaultTableModel tableModel, String url, String user, String pass) {
        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String query = "SELECT * FROM matiere";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            tableModel.setRowCount(0);
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("MatiéreID"),
                    rs.getString("Nom"),
                    rs.getInt("vh_cour"),
                    rs.getInt("vh_TP"),
                    rs.getInt("vh_TD"),
                    rs.getInt("credit"),
                    rs.getInt("coefficient"),
                    rs.getString("regime"),
                    rs.getString("ModuleID")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearInputFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    private void createClassPanel() {
        JPanel classPanel = new JPanel();
        classPanel.setLayout(new BorderLayout());
        classPanel.setBackground(Color.WHITE);
        contentPanel.add(classPanel, "classe");


        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Ajouter Classe"));

        JTextField txtClassID = new JTextField();
        JComboBox<Integer> comboNiveau = new JComboBox<>(new Integer[]{1, 2, 3});

        inputPanel.add(new JLabel("Class ID:"));
        inputPanel.add(txtClassID);
        inputPanel.add(new JLabel("Niveau:"));
        inputPanel.add(comboNiveau);

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"Class ID", "Niveau", "Nombre Etudiants"});
        JTable table = new JTable(tableModel);

        
        JButton btnAdd = createStyledButton("Ajouter");
        JButton btnRefresh = createStyledButton("Actualiser");
        JButton btnViewTeachers = createStyledButton("Voir Enseignants");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnViewTeachers);

        
        classPanel.add(inputPanel, BorderLayout.NORTH);
        classPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        classPanel.add(buttonPanel, BorderLayout.SOUTH);

        final String url = "jdbc:mysql://localhost:3306/gestionnaire_etudiant";
        final String user = "root";
        final String pass = "root";

       
        loadClasses(tableModel, url, user, pass);

        btnAdd.addActionListener(e -> {
            try (Connection conn = DriverManager.getConnection(url, user, pass)) {
                String query = "INSERT INTO Classe (ClassID, Niveau, NombreEtud) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);

                stmt.setString(1, txtClassID.getText());
                stmt.setInt(2, (Integer) comboNiveau.getSelectedItem());
                stmt.setInt(3, 0); 

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(classPanel, "Classe ajoutée avec succès!");
                loadClasses(tableModel, url, user, pass);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(classPanel, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnRefresh.addActionListener(e -> loadClasses(tableModel, url, user, pass));

        btnViewTeachers.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(classPanel, "Veuillez sélectionner une classe!", "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String selectedClassID = tableModel.getValueAt(selectedRow, 0).toString();

            try (Connection conn = DriverManager.getConnection(url, user, pass)) {
                String query = "SELECT nom, prenom FROM enseignant WHERE ClassID = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, selectedClassID);

                ResultSet rs = stmt.executeQuery();

                StringBuilder teacherList = new StringBuilder("Enseignants:\n");
                while (rs.next()) {
                    teacherList.append(rs.getString("nom")).append(" ").append(rs.getString("prenom")).append("\n");
                }

                if (teacherList.length() == 11) { 
                    teacherList.append("Aucun enseignant trouvé.");
                }

                JOptionPane.showMessageDialog(classPanel, teacherList.toString(), "Liste des enseignants", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(classPanel, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void loadClasses(DefaultTableModel tableModel, String url, String user, String pass) {
        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String query = "SELECT * FROM Classe";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            tableModel.setRowCount(0); 
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("ClassID"),
                    rs.getInt("Niveau"),
                    rs.getInt("NombreEtud")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminInterface());
    }
}

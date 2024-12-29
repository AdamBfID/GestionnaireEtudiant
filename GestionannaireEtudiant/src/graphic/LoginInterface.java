package graphic;

import javax.swing.*; 
import java.awt.*; 
import java.awt.event.*; 
import java.util.ArrayList;
import java.util.Collections; 
import java.sql.*;



public class LoginInterface {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("GMAM Gestionnaire d'etudiant");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        ImageIcon appIcon = new ImageIcon(LoginInterface.class.getResource("/resources/app_icon.png"));
        frame.setIconImage(appIcon.getImage());
        frame.setVisible(true);
        // Custom colors for futuristic theme
        Color bgColor = new Color(8, 10, 32);
        Color accentColor = new Color(164, 196, 25);
        Color buttonColor = new Color(255, 255, 255);
        Image backgroundImage = new ImageIcon(LoginInterface.class.getResource("/resources/backgground.png")).getImage();

       
        // Main panel with custom gradient background
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                // Get the size of the panel
                int w = getWidth();
                int h = getHeight();

                // Draw the image scaled to the size of the panel
                g2d.drawImage(backgroundImage, 0, 0, w, h, this);
            }
        };

        frame.add(mainPanel);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 20, 40));
        mainPanel.add(formPanel, BorderLayout.NORTH);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title label with custom font
     // Define colors
        Color primaryTextColor = new Color(255, 255, 255);
        Color fieldBackgroundColor = new Color(255, 255, 255, 240);
        Color accentColor1 = new Color(41, 128, 185);

        // Add logo
        ImageIcon originalIcon = new ImageIcon("/resources/app_icon.png");  
        Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 10, 0);
        formPanel.add(logoLabel, gbc);

        // Title with enhanced styling
        JLabel titleLabel = new JLabel("Gestionnaire de GMAM");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(primaryTextColor);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 20, 0);
        formPanel.add(titleLabel, gbc);

        // Username label and field
        JLabel userLabel = new JLabel("Utilisateur");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userLabel.setForeground(primaryTextColor);
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 0, 5, 10);
        formPanel.add(userLabel, gbc);

        JTextField userTextField = new JTextField(15) {
            @Override
            protected void paintComponent(Graphics g) {
                if (!isOpaque()) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(fieldBackgroundColor);
                    g2d.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
                }
                super.paintComponent(g);
            }
        };
        userTextField.setOpaque(false);
        userTextField.setForeground(Color.BLACK);
        userTextField.setCaretColor(accentColor1);
        userTextField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userTextField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColor1, 1, true),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        gbc.gridx = 1;
        gbc.insets = new Insets(5, 0, 5, 0);
        formPanel.add(userTextField, gbc);

        // Password label and field
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passLabel.setForeground(primaryTextColor);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(5, 0, 5, 10);
        formPanel.add(passLabel, gbc);

        JPasswordField passField = new JPasswordField(15) {
            @Override
            protected void paintComponent(Graphics g) {
                if (!isOpaque()) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(fieldBackgroundColor);
                    g2d.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
                }
                super.paintComponent(g);
            }
        };
        passField.setOpaque(false);
        passField.setForeground(Color.BLACK);
        passField.setCaretColor(accentColor1);
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColor1, 1, true),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        gbc.gridx = 1;
        gbc.insets = new Insets(5, 0, 5, 0);
        formPanel.add(passField, gbc);

        // Add focus listeners for hover effect
        userTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                userTextField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(accentColor1, 2, true),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                userTextField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(accentColor1, 1, true),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
        });

        passField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                passField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(accentColor1, 2, true),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                passField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(accentColor1, 1, true),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
        });

        // Status label
        JLabel statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setForeground(accentColor1);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(statusLabel, gbc);

        // Numpad panel with custom styling
        JPanel numpadPanel = new JPanel(new GridLayout(4, 3, 15, 15));  // Increased spacing
        numpadPanel.setOpaque(false);
        numpadPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50));  // Larger padding
        mainPanel.add(numpadPanel, BorderLayout.CENTER);

        // Create shuffled number buttons
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i <= 9; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);

        // Enhanced FuturisticButton class
        class FuturisticButton extends JButton {
            private Color hoverColor = new Color(70, 130, 180);  // Steel blue
            private Color normalColor = new Color(47, 53, 66);  // Dodger blue
            private boolean isLoginButton;

            FuturisticButton(String text, boolean isLoginButton) {
                super(text);
                this.isLoginButton = isLoginButton;
                
                // Modern styling
                setFont(new Font("Segoe UI", Font.BOLD, 20));
                setForeground(Color.WHITE);
                setFocusPainted(false);
                setBorderPainted(false);
                setContentAreaFilled(false);
                setPreferredSize(new Dimension(80, 80));  // Larger buttons
                setCursor(new Cursor(Cursor.HAND_CURSOR));

                // Enhanced hover effects
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        setForeground(Color.WHITE);
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        setForeground(Color.WHITE);
                        repaint();
                    }
                    
                    @Override
                    public void mousePressed(MouseEvent e) {
                        setForeground(new Color(200, 200, 200));
                        repaint();
                    }
                    
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        setForeground(Color.WHITE);
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient paint
                GradientPaint gradient = new GradientPaint(
                    0, 0, 
                    isLoginButton ? new Color(41, 128, 185) : normalColor,
                    0, getHeight(),
                    isLoginButton ? new Color(52, 152, 219) : hoverColor
                );
                g2d.setPaint(gradient);

                // Button shape with drop shadow
                int shadowGap = 3;
                int shadowOffset = 2;
                Color shadowColor = new Color(0, 0, 0, 50);
                
                // Draw shadow
                g2d.setColor(shadowColor);
                g2d.fillRoundRect(shadowOffset, shadowOffset, 
                    getWidth() - shadowGap - 1, getHeight() - shadowGap - 1, 25, 25);

                // Draw button
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth() - shadowGap, getHeight() - shadowGap, 25, 25);

                // Add subtle highlight
                g2d.setColor(new Color(255, 255, 255, 30));
                g2d.fillRoundRect(0, 0, getWidth() - shadowGap, getHeight() / 2 - shadowGap, 25, 25);

                super.paintComponent(g);
            }
        }

        // Add number buttons
        for (int number : numbers) {
            JButton button = new FuturisticButton(String.valueOf(number), false);
            numpadPanel.add(button);
            button.addActionListener(e -> passField.setText(passField.getText() + button.getText()));
        }

        // Add backspace button with custom icon
        JButton backspaceButton = new FuturisticButton("⌫", false);  // Unicode backspace symbol
        numpadPanel.add(backspaceButton);
        backspaceButton.addActionListener(e -> {
            String currentText = passField.getText();
            if (!currentText.isEmpty()) {
                passField.setText(currentText.substring(0, currentText.length() - 1));
            }
        });

        // Add login button with enhanced styling
        JButton loginButton = new FuturisticButton("LOGIN", true);
        numpadPanel.add(loginButton);
        loginButton.addActionListener(e -> {
            String username = userTextField.getText();
            String password = passField.getText();
            if (validateLogin(username, password)) {
                if (isAdmin(username, password)) {
                    statusLabel.setText("Admin login successful!");
                    statusLabel.setForeground(new Color(0, 255, 128));

                    // Close the login frame and open the admin interface
                    frame.dispose();
                    SwingUtilities.invokeLater(() -> new AdminInterface());
                } else {
                	 statusLabel.setText("Login successful (Enseignant)!");
                     statusLabel.setForeground(new Color(0, 255, 128));

                     // Close the login frame and open the teacher interface
                     frame.dispose();
                     SwingUtilities.invokeLater(() -> new EnseignantInterface(username));
                }
            } else {
                statusLabel.setText("Nom d'utilisateur ou mot de passe incorrecte");
                statusLabel.setForeground(new Color(255, 64, 64));
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static boolean validateLogin(String username, String password) {
        String url = "jdbc:mysql://localhost:3306/gestionnaire_etudiant";
        String user = "root";
        String pass = "root";

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String adminQuery = "SELECT * FROM adminaccess WHERE Adminuser = ? AND Admipass = ?";
            PreparedStatement adminStmt = conn.prepareStatement(adminQuery);
            adminStmt.setString(1, username);
            adminStmt.setString(2, password);

            ResultSet adminResult = adminStmt.executeQuery();
            if (adminResult.next()) {
                return true;
            }

            String enseignantQuery = "SELECT * FROM enseignant WHERE (email = ? OR numcin = ?) AND password = ?";
            PreparedStatement enseignantStmt = conn.prepareStatement(enseignantQuery);
            enseignantStmt.setString(1, username);
            enseignantStmt.setString(2, username);
            enseignantStmt.setString(3, password);

            ResultSet enseignantResult = enseignantStmt.executeQuery();
            return enseignantResult.next();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private static boolean isAdmin(String username, String password) {
        String url = "jdbc:mysql://localhost:3306/gestionnaire_etudiant";
        String user = "root";
        String pass = "root";

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String adminQuery = "SELECT * FROM adminaccess WHERE Adminuser = ? AND Admipass = ?";
            PreparedStatement adminStmt = conn.prepareStatement(adminQuery);
            adminStmt.setString(1, username);
            adminStmt.setString(2, password);

            ResultSet adminResult = adminStmt.executeQuery();
            return adminResult.next();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

}
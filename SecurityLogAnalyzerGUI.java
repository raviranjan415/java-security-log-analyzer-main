import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SecurityLogAnalyzerGUI extends JFrame {
    private JTabbedPane tabbedPane;
    private JTextArea logDisplayArea;
    private JTextArea analysisResultArea;
    private JTextArea securityReportArea;
    private JButton analyzeButton;
    private JButton loadLogsButton;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextArea loginResultArea;
    private JLabel attemptsLabel;
    private JTable whitelistTable;
    private JTable blacklistTable;
    private DefaultTableModel whitelistModel;
    private DefaultTableModel blacklistModel;
    private Set<String> whitelistedIPs = new HashSet<>();
    private Set<String> blacklistedIPs = new HashSet<>();
    private int loginAttempts = 0;
    private final int MAX_ATTEMPTS = 3;

    public SecurityLogAnalyzerGUI() {
        setTitle("🛡️ Advanced Security Log Analyzer - SOC Tool");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(true);

        // Initialize whitelisted IPs (default trusted IPs)
        whitelistedIPs.add("192.168.1.100");
        whitelistedIPs.add("192.168.1.105");
        whitelistedIPs.add("10.0.0.1");

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("📊 Log Analyzer", createLogAnalyzerPanel());
        tabbedPane.addTab("🔐 Login Checker", createLoginCheckerPanel());
        tabbedPane.addTab("🛡️ Whitelist/Blacklist", createWhitelistPanel());

        add(tabbedPane);
        setVisible(true);
    }

    private JPanel createLogAnalyzerPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top: Controls
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Controls"));

        loadLogsButton = new JButton("📂 Load Logs File");
        loadLogsButton.setFont(new Font("Arial", Font.BOLD, 12));
        loadLogsButton.addActionListener(e -> loadLogsFile());

        analyzeButton = new JButton("🔍 Analyze Logs");
        analyzeButton.setFont(new Font("Arial", Font.BOLD, 12));
        analyzeButton.addActionListener(e -> analyzeLogs());
        analyzeButton.setEnabled(false);

        JButton exportButton = new JButton("💾 Export Report");
        exportButton.setFont(new Font("Arial", Font.BOLD, 12));
        exportButton.addActionListener(e -> exportReport());

        controlPanel.add(loadLogsButton);
        controlPanel.add(analyzeButton);
        controlPanel.add(exportButton);

        panel.add(controlPanel, BorderLayout.NORTH);

        // Middle: Display logs
        JPanel logsPanel = new JPanel(new BorderLayout(5, 5));
        logsPanel.setBorder(BorderFactory.createTitledBorder("📋 Login Logs Preview"));
        logDisplayArea = new JTextArea(6, 50);
        logDisplayArea.setEditable(false);
        logDisplayArea.setFont(new Font("Courier New", Font.PLAIN, 9));
        JScrollPane logScroll = new JScrollPane(logDisplayArea);
        logsPanel.add(logScroll, BorderLayout.CENTER);
        logsPanel.setPreferredSize(new Dimension(0, 150));

        panel.add(logsPanel, BorderLayout.CENTER);

        // Bottom: Results
        JPanel resultsPanel = new JPanel(new BorderLayout(5, 5));
        resultsPanel.setBorder(BorderFactory.createTitledBorder("📈 Detailed Security Analysis & Recommendations"));
        analysisResultArea = new JTextArea(15, 100);
        analysisResultArea.setEditable(false);
        analysisResultArea.setFont(new Font("Courier New", Font.PLAIN, 10));
        analysisResultArea.setLineWrap(true);
        analysisResultArea.setWrapStyleWord(true);
        JScrollPane resultsScroll = new JScrollPane(analysisResultArea);
        resultsPanel.add(resultsScroll, BorderLayout.CENTER);

        panel.add(resultsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createLoginCheckerPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Title
        JLabel titleLabel = new JLabel("🔐 Interactive Login Checker with Risk Assessment");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);

        panel.add(Box.createVerticalStrut(20));

        // Username field
        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userLabel.setPreferredSize(new Dimension(100, 30));
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setPreferredSize(new Dimension(200, 30));
        usernamePanel.add(userLabel);
        usernamePanel.add(usernameField);
        usernamePanel.setMaximumSize(new Dimension(500, 40));
        panel.add(usernamePanel);

        panel.add(Box.createVerticalStrut(10));

        // Password field
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passLabel.setPreferredSize(new Dimension(100, 30));
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(200, 30));
        passwordField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    checkLogin();
                }
            }
        });
        passwordPanel.add(passLabel);
        passwordPanel.add(passwordField);
        passwordPanel.setMaximumSize(new Dimension(500, 40));
        panel.add(passwordPanel);

        panel.add(Box.createVerticalStrut(15));

        // Login button
        JButton loginButton = new JButton("🔓 Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setPreferredSize(new Dimension(150, 40));
        loginButton.addActionListener(e -> checkLogin());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(loginButton);
        buttonPanel.setMaximumSize(new Dimension(500, 50));
        panel.add(buttonPanel);

        panel.add(Box.createVerticalStrut(15));

        // Attempts counter
        JPanel attemptsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        attemptsLabel = new JLabel("Attempts: 0/" + MAX_ATTEMPTS);
        attemptsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        attemptsPanel.add(attemptsLabel);
        attemptsPanel.setMaximumSize(new Dimension(500, 30));
        panel.add(attemptsPanel);

        panel.add(Box.createVerticalStrut(20));

        // Result area
        JLabel resultTitleLabel = new JLabel("📝 Login Result & Risk Assessment:");
        resultTitleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(resultTitleLabel);

        panel.add(Box.createVerticalStrut(5));

        loginResultArea = new JTextArea(10, 60);
        loginResultArea.setEditable(false);
        loginResultArea.setFont(new Font("Courier New", Font.PLAIN, 11));
        loginResultArea.setLineWrap(true);
        loginResultArea.setWrapStyleWord(true);
        JScrollPane loginScroll = new JScrollPane(loginResultArea);
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setMaximumSize(new Dimension(500, 180));
        resultPanel.add(loginScroll, BorderLayout.CENTER);
        panel.add(resultPanel);

        panel.add(Box.createVerticalStrut(10));

        // Reset button
        JButton resetButton = new JButton("🔄 Reset");
        resetButton.setFont(new Font("Arial", Font.PLAIN, 12));
        resetButton.setPreferredSize(new Dimension(100, 30));
        resetButton.addActionListener(e -> resetLogin());
        JPanel resetPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        resetPanel.add(resetButton);
        panel.add(resetPanel);

        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel createWhitelistPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Whitelist section
        JPanel whitelistSection = new JPanel(new BorderLayout(5, 5));
        whitelistSection.setBorder(BorderFactory.createTitledBorder("✅ Whitelisted IPs (Trusted)"));

        whitelistModel = new DefaultTableModel(new String[]{"IP Address", "Status"}, 0);
        for (String ip : whitelistedIPs) {
            whitelistModel.addRow(new Object[]{ip, "Trusted"});
        }
        whitelistTable = new JTable(whitelistModel);
        whitelistTable.setFont(new Font("Arial", Font.PLAIN, 12));
        whitelistTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane whitelistScroll = new JScrollPane(whitelistTable);

        JPanel whitelistButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JTextField ipInput = new JTextField(15);
        JButton addWhiteButton = new JButton("➕ Add IP");
        addWhiteButton.addActionListener(e -> {
            String ip = ipInput.getText().trim();
            if (!ip.isEmpty()) {
                whitelistedIPs.add(ip);
                whitelistModel.addRow(new Object[]{ip, "Trusted"});
                ipInput.setText("");
            }
        });
        JButton removeWhiteButton = new JButton("❌ Remove");
        removeWhiteButton.addActionListener(e -> {
            int row = whitelistTable.getSelectedRow();
            if (row >= 0) {
                String ip = (String) whitelistModel.getValueAt(row, 0);
                whitelistedIPs.remove(ip);
                whitelistModel.removeRow(row);
            }
        });
        whitelistButtonPanel.add(new JLabel("IP Address:"));
        whitelistButtonPanel.add(ipInput);
        whitelistButtonPanel.add(addWhiteButton);
        whitelistButtonPanel.add(removeWhiteButton);

        whitelistSection.add(whitelistButtonPanel, BorderLayout.NORTH);
        whitelistSection.add(whitelistScroll, BorderLayout.CENTER);

        // Blacklist section
        JPanel blacklistSection = new JPanel(new BorderLayout(5, 5));
        blacklistSection.setBorder(BorderFactory.createTitledBorder("🚫 Blacklisted IPs (Blocked)"));

        blacklistModel = new DefaultTableModel(new String[]{"IP Address", "Reason"}, 0);
        blacklistTable = new JTable(blacklistModel);
        blacklistTable.setFont(new Font("Arial", Font.PLAIN, 12));
        blacklistTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane blacklistScroll = new JScrollPane(blacklistTable);

        JPanel blacklistButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JTextField blackipInput = new JTextField(15);
        JButton addBlackButton = new JButton("➕ Add IP");
        addBlackButton.addActionListener(e -> {
            String ip = blackipInput.getText().trim();
            if (!ip.isEmpty()) {
                blacklistedIPs.add(ip);
                blacklistModel.addRow(new Object[]{ip, "Brute-Force Detected"});
                blackipInput.setText("");
            }
        });
        JButton removeBlackButton = new JButton("❌ Remove");
        removeBlackButton.addActionListener(e -> {
            int row = blacklistTable.getSelectedRow();
            if (row >= 0) {
                String ip = (String) blacklistModel.getValueAt(row, 0);
                blacklistedIPs.remove(ip);
                blacklistModel.removeRow(row);
            }
        });
        blacklistButtonPanel.add(new JLabel("IP Address:"));
        blacklistButtonPanel.add(blackipInput);
        blacklistButtonPanel.add(addBlackButton);
        blacklistButtonPanel.add(removeBlackButton);

        blacklistSection.add(blacklistButtonPanel, BorderLayout.NORTH);
        blacklistSection.add(blacklistScroll, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, whitelistSection, blacklistSection);
        splitPane.setDividerLocation(500);

        panel.add(splitPane, BorderLayout.CENTER);

        return panel;
    }

    private void loadLogsFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                StringBuilder content = new StringBuilder();
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                reader.close();
                logDisplayArea.setText(content.toString());
                analyzeButton.setEnabled(true);
                analysisResultArea.setText("");
                JOptionPane.showMessageDialog(this, "✅ Logs loaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "❌ Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void analyzeLogs() {
        String logContent = logDisplayArea.getText();
        if (logContent.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Please load a logs file first!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        HashMap<String, IPInfo> ipAnalysis = new HashMap<>();
        HashMap<String, UserInfo> userAnalysis = new HashMap<>();
        int blockThreshold = 5;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        StringBuilder reportOutput = new StringBuilder();
        reportOutput.append("═══════════════════════════════════════════════════════════════════════════════\n");
        reportOutput.append("                   ADVANCED SECURITY LOG ANALYSIS REPORT\n");
        reportOutput.append("═══════════════════════════════════════════════════════════════════════════════\n\n");

        try {
            String[] lines = logContent.split("\n");
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length < 4) continue;

                try {
                    LocalDateTime time = LocalDateTime.parse(parts[0], formatter);
                    String ip = parts[1];
                    String username = parts[2];
                    String password = parts[3];

                    boolean isValid = username.equals("admin") && password.equals("1234");

                    if (!ipAnalysis.containsKey(ip)) {
                        ipAnalysis.put(ip, new IPInfo(ip));
                    }
                    if (!userAnalysis.containsKey(username)) {
                        userAnalysis.put(username, new UserInfo(username));
                    }

                    IPInfo ipInfo = ipAnalysis.get(ip);
                    UserInfo userInfo = userAnalysis.get(username);

                    if (!isValid) {
                        ipInfo.failureCount++;
                        userInfo.failureCount++;
                        ipInfo.lastFailureTime = time;
                        userInfo.lastFailureTime = time;
                    } else {
                        ipInfo.successCount++;
                        userInfo.successCount++;
                    }

                    ipInfo.totalAttempts++;
                    userInfo.totalAttempts++;
                } catch (Exception e) {
                    // Skip malformed lines
                }
            }

            // Calculate risk scores and make decisions
            reportOutput.append("┌─ IP ADDRESS ANALYSIS ─────────────────────────────────────────────────────┐\n\n");

            for (Map.Entry<String, IPInfo> entry : ipAnalysis.entrySet()) {
                String ip = entry.getKey();
                IPInfo info = entry.getValue();
                
                int riskScore = calculateIPRiskScore(info);
                String riskLevel = getRiskLevel(riskScore);
                String decision = getIPDecision(ip, riskScore, info);

                reportOutput.append(String.format("IP: %s\n", ip));
                reportOutput.append(String.format("├─ Total Attempts:      %d (Success: %d, Failed: %d)\n", 
                    info.totalAttempts, info.successCount, info.failureCount));
                reportOutput.append(String.format("├─ Failure Rate:        %.1f%%\n", 
                    (info.failureCount * 100.0) / info.totalAttempts));
                reportOutput.append(String.format("├─ Risk Score:          %d/100 [%s]\n", riskScore, riskLevel));
                reportOutput.append(String.format("├─ Status:              %s\n", decision));
                reportOutput.append(String.format("├─ Whitelist Status:    %s\n", 
                    whitelistedIPs.contains(ip) ? "✅ TRUSTED" : "❌ NOT TRUSTED"));
                reportOutput.append(String.format("└─ Blacklist Status:    %s\n\n", 
                    blacklistedIPs.contains(ip) ? "🚫 BLOCKED" : "✅ ALLOWED"));
            }

            reportOutput.append("┌─ USER ACCOUNT ANALYSIS ────────────────────────────────────────────────────┐\n\n");

            for (Map.Entry<String, UserInfo> entry : userAnalysis.entrySet()) {
                String username = entry.getKey();
                UserInfo info = entry.getValue();
                
                int riskScore = calculateUserRiskScore(info);
                String riskLevel = getRiskLevel(riskScore);
                String decision = getUserDecision(username, riskScore, info);

                reportOutput.append(String.format("User: %s\n", username));
                reportOutput.append(String.format("├─ Total Attempts:      %d (Success: %d, Failed: %d)\n", 
                    info.totalAttempts, info.successCount, info.failureCount));
                reportOutput.append(String.format("├─ Account Compromise Risk: %.1f%%\n", 
                    (info.failureCount * 100.0) / info.totalAttempts));
                reportOutput.append(String.format("├─ Risk Score:          %d/100 [%s]\n", riskScore, riskLevel));
                reportOutput.append(String.format("└─ Recommendation:      %s\n\n", decision));
            }

            reportOutput.append("┌─ RECOMMENDED ACTIONS ──────────────────────────────────────────────────────┐\n\n");
            reportOutput.append(generateRecommendations(ipAnalysis, userAnalysis, blockThreshold));

            analysisResultArea.setText(reportOutput.toString());

            // Save report
            try (FileWriter writer = new FileWriter("security_analysis_report.txt")) {
                writer.write(reportOutput.toString());
            }

            JOptionPane.showMessageDialog(this, "✅ Analysis Complete!\nReport saved to security_analysis_report.txt", 
                "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error analyzing logs: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int calculateIPRiskScore(IPInfo info) {
        int score = 0;

        // Failure rate (0-40 points)
        double failureRate = info.totalAttempts > 0 ? (info.failureCount * 100.0) / info.totalAttempts : 0;
        score += Math.min(40, (int) (failureRate * 0.4));

        // Number of failures (0-30 points)
        score += Math.min(30, info.failureCount * 5);

        // Attempts clustering (0-30 points) - if many attempts in short time
        if (info.failureCount >= 5) {
            score += 30;
        } else if (info.failureCount >= 3) {
            score += 20;
        }

        return Math.min(100, score);
    }

    private int calculateUserRiskScore(UserInfo info) {
        int score = 0;

        double failureRate = info.totalAttempts > 0 ? (info.failureCount * 100.0) / info.totalAttempts : 0;
        score += Math.min(40, (int) (failureRate * 0.4));
        score += Math.min(30, info.failureCount * 5);

        if (info.failureCount >= 5) {
            score += 30;
        } else if (info.failureCount >= 3) {
            score += 20;
        }

        return Math.min(100, score);
    }

    private String getRiskLevel(int score) {
        if (score >= 80) return "🔴 CRITICAL";
        if (score >= 60) return "🟠 HIGH";
        if (score >= 40) return "🟡 MEDIUM";
        return "🟢 LOW";
    }

    private String getIPDecision(String ip, int riskScore, IPInfo info) {
        if (whitelistedIPs.contains(ip)) return "✅ ALLOWED (Whitelisted)";
        if (blacklistedIPs.contains(ip)) return "🚫 BLOCKED (Blacklisted)";
        if (riskScore >= 75) return "🚫 BLOCKED (Critical Risk)";
        if (riskScore >= 55) return "⚠️  FLAGGED (High Risk)";
        return "✅ ALLOWED";
    }

    private String getUserDecision(String username, int riskScore, UserInfo info) {
        if (riskScore >= 75) return "🚫 ACCOUNT LOCKED (Critical Risk)";
        if (riskScore >= 55) return "⚠️  REQUIRE MFA (High Risk)";
        if (riskScore >= 35) return "⚠️  MONITOR (Medium Risk)";
        return "✅ NORMAL";
    }

    private String generateRecommendations(HashMap<String, IPInfo> ipAnalysis, 
                                          HashMap<String, UserInfo> userAnalysis, int threshold) {
        StringBuilder rec = new StringBuilder();

        // Critical IPs
        rec.append("🔴 CRITICAL ACTIONS REQUIRED:\n");
        boolean hasCritical = false;
        for (Map.Entry<String, IPInfo> entry : ipAnalysis.entrySet()) {
            int score = calculateIPRiskScore(entry.getValue());
            if (score >= 75) {
                rec.append("   • Block IP ").append(entry.getKey()).append(" immediately (risk: ").append(score).append("/100)\n");
                hasCritical = true;
            }
        }
        if (!hasCritical) rec.append("   • No critical threats detected\n");

        rec.append("\n🟠 RECOMMENDED MONITORING:\n");
        boolean hasHigh = false;
        for (Map.Entry<String, IPInfo> entry : ipAnalysis.entrySet()) {
            int score = calculateIPRiskScore(entry.getValue());
            if (score >= 55 && score < 75) {
                rec.append("   • Monitor IP ").append(entry.getKey()).append(" closely (risk: ").append(score).append("/100)\n");
                hasHigh = true;
            }
        }
        if (!hasHigh) rec.append("   • No high-risk IPs detected\n");

        rec.append("\n👤 USER ACCOUNT ACTIONS:\n");
        boolean hasUserAction = false;
        for (Map.Entry<String, UserInfo> entry : userAnalysis.entrySet()) {
            int score = calculateUserRiskScore(entry.getValue());
            if (score >= 55) {
                rec.append("   • Reset password for '").append(entry.getKey()).append("' (failures: ")
                    .append(entry.getValue().failureCount).append(")\n");
                hasUserAction = true;
            }
        }
        if (!hasUserAction) rec.append("   • No user account actions needed\n");

        return rec.toString();
    }

    private void checkLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            loginResultArea.setText("⚠️ Please enter both username and password!");
            return;
        }

        // Simulate getting client IP (in real system, use actual client IP)
        String clientIP = "192.168.1." + (10 + (int)(Math.random() * 240));

        StringBuilder result = new StringBuilder();

        if (username.equals("admin") && password.equals("1234")) {
            result.append("✅ ACCESS GRANTED\n\n");
            result.append("═════════════════════════════════════════\n");
            result.append("Welcome, ").append(username).append("!\n");
            result.append("Login successful at: ").append(LocalDateTime.now()).append("\n");
            result.append("Client IP: ").append(clientIP).append("\n");
            if (whitelistedIPs.contains(clientIP)) {
                result.append("IP Status: ✅ TRUSTED LOCATION\n");
            } else {
                result.append("IP Status: ⚠️  NEW LOCATION\n");
            }
            result.append("═════════════════════════════════════════\n");
            loginResultArea.setText(result.toString());
            usernameField.setText("");
            passwordField.setText("");
            loginAttempts = 0;
            attemptsLabel.setText("Attempts: 0/" + MAX_ATTEMPTS);
        } else {
            loginAttempts++;
            result.append("❌ ACCESS DENIED\n\n");
            result.append("═════════════════════════════════════════\n");
            result.append("Invalid credentials. Attempt ").append(loginAttempts).append(" of ").append(MAX_ATTEMPTS).append("\n");
            result.append("Time: ").append(LocalDateTime.now()).append("\n");
            result.append("Client IP: ").append(clientIP).append("\n");

            if (loginAttempts >= MAX_ATTEMPTS) {
                result.append("\n🚨 ALERT: BRUTE-FORCE ATTACK DETECTED!\n");
                result.append("Account locked after ").append(MAX_ATTEMPTS).append(" failed attempts.\n");
                result.append("IP ").append(clientIP).append(" has been flagged.\n");
                result.append("Action: Account temporarily disabled.\n");
                result.append("Contact admin to restore access.\n");
                usernameField.setEnabled(false);
                passwordField.setEnabled(false);
            } else {
                result.append("\nAttempts remaining: ").append(MAX_ATTEMPTS - loginAttempts).append("\n");
                result.append("Risk Level: ").append(getRiskLevel(loginAttempts * 25)).append("\n");
            }
            result.append("═════════════════════════════════════════\n");
            loginResultArea.setText(result.toString());
        }

        passwordField.setText("");
    }

    private void resetLogin() {
        usernameField.setText("");
        passwordField.setText("");
        loginResultArea.setText("");
        loginAttempts = 0;
        attemptsLabel.setText("Attempts: 0/" + MAX_ATTEMPTS);
        usernameField.setEnabled(true);
        passwordField.setEnabled(true);
        usernameField.requestFocus();
    }

    private void exportReport() {
        String analysisText = analysisResultArea.getText();

        if (analysisText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ No analysis results to export!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (FileWriter writer = new FileWriter("SECURITY_REPORT.txt")) {
            writer.write("═════════════════════════════════════════════════════════════════════════════════\n");
            writer.write("              COMPREHENSIVE SECURITY OPERATIONS CENTER (SOC) REPORT\n");
            writer.write("═════════════════════════════════════════════════════════════════════════════════\n\n");
            writer.write("Generated: " + LocalDateTime.now() + "\n");
            writer.write("Analysis Tool: Advanced Security Log Analyzer v2.0\n\n");
            writer.write(analysisText);
            writer.write("\n\n═════════════════════════════════════════════════════════════════════════════════\n");
            JOptionPane.showMessageDialog(this, "✅ Report exported to SECURITY_REPORT.txt", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "❌ Error exporting report: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper classes for analysis
    private static class IPInfo {
        String ip;
        int failureCount = 0;
        int successCount = 0;
        int totalAttempts = 0;
        LocalDateTime lastFailureTime;

        IPInfo(String ip) {
            this.ip = ip;
        }
    }

    private static class UserInfo {
        String username;
        int failureCount = 0;
        int successCount = 0;
        int totalAttempts = 0;
        LocalDateTime lastFailureTime;

        UserInfo(String username) {
            this.username = username;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SecurityLogAnalyzerGUI());
    }
} 


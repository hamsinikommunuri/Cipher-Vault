import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CipherPro {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CipherPro::createGUI);
    }

    private static void createGUI() {
        JFrame frame = new JFrame("CipherPro: The Enigma Suite");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(750, 600);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 240, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        Font labelFont = new Font("SansSerif", Font.BOLD, 15);
        Font textFont = new Font("SansSerif", Font.PLAIN, 15);

        JLabel modeLabel = new JLabel("🔄 Mode:");
        modeLabel.setFont(labelFont);
        JComboBox<String> modeCombo = new JComboBox<>(new String[]{"Encryption", "Decryption"});

        JLabel typeLabel = new JLabel("🔐 Encryption Type:");
        typeLabel.setFont(labelFont);
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Caesar (Shift 1)", "ROT13", "Custom Shift"});

        JLabel inputLabel = new JLabel("📝 Enter text:");
        inputLabel.setFont(labelFont);
        JTextArea inputArea = new JTextArea(4, 35);
        inputArea.setFont(textFont);
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);

        JLabel shiftLabel = new JLabel("🔑 Shift Key (for Custom):");
        shiftLabel.setFont(labelFont);
        JTextField shiftField = new JTextField(10);
        shiftField.setFont(textFont);

        JButton processButton = new JButton("✨ Process");
        JButton clearButton = new JButton("🧹 Clear");

        JLabel resultLabel = new JLabel("📤 Result:");
        resultLabel.setFont(labelFont);
        JTextArea outputArea = new JTextArea(4, 35);
        outputArea.setFont(textFont);
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);

        JLabel detectLabel = new JLabel("📌 Detected/Selected Type:");
        detectLabel.setFont(labelFont);
        JTextField detectField = new JTextField(25);
        detectField.setFont(textFont);
        detectField.setEditable(false);

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row; panel.add(modeLabel, gbc);
        gbc.gridx = 1; panel.add(modeCombo, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; panel.add(typeLabel, gbc);
        gbc.gridx = 1; panel.add(typeCombo, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; panel.add(inputLabel, gbc);
        gbc.gridx = 1; panel.add(new JScrollPane(inputArea), gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; panel.add(shiftLabel, gbc);
        gbc.gridx = 1; panel.add(shiftField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; panel.add(processButton, gbc);
        gbc.gridx = 1; panel.add(clearButton, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; panel.add(resultLabel, gbc);
        gbc.gridx = 1; panel.add(new JScrollPane(outputArea), gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; panel.add(detectLabel, gbc);
        gbc.gridx = 1; panel.add(detectField, gbc);

        frame.add(panel);
        frame.setVisible(true);

        // Logic for switching modes
        typeCombo.setEnabled(true);
        shiftField.setEnabled(false);

        modeCombo.addActionListener(e -> {
            boolean isEncrypt = modeCombo.getSelectedItem().equals("Encryption");
            typeCombo.setEnabled(isEncrypt);
            shiftField.setEnabled(isEncrypt && typeCombo.getSelectedItem().equals("Custom Shift"));
        });

        typeCombo.addActionListener(e -> {
            shiftField.setEnabled(typeCombo.getSelectedItem().equals("Custom Shift"));
        });

        processButton.addActionListener(e -> {
            String input = inputArea.getText().trim();
            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter some text.");
                return;
            }

            String mode = (String) modeCombo.getSelectedItem();

            if (mode.equals("Encryption")) {
                String type = (String) typeCombo.getSelectedItem();
                int shift = 0;
                if (type.equals("ROT13")) {
                    shift = 13;
                } else if (type.equals("Caesar (Shift 1)")) {
                    shift = 1;
                } else if (type.equals("Custom Shift")) {
                    try {
                        shift = Integer.parseInt(shiftField.getText().trim());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid shift key.");
                        return;
                    }
                }
                String result = caesarCipher(input, shift);
                outputArea.setText(result);
                detectField.setText(type + " (Shift " + shift + ")");

            } else {
                // Decryption Mode: Use Caesar brute-force from 1 to 25
                StringBuilder results = new StringBuilder();
                for (int i = 1; i < 26; i++) {
                    String guess = caesarCipher(input, -i);
                    results.append("Shift -").append(i).append(": ").append(guess).append("\n");
                }
                outputArea.setText(results.toString());
                detectField.setText("Tried all Caesar shifts (1 to 25)");
            }
        });

        clearButton.addActionListener(e -> {
            inputArea.setText("");
            outputArea.setText("");
            shiftField.setText("");
            detectField.setText("");
        });
    }

    // Caesar cipher logic
    private static String caesarCipher(String text, int shift) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                result.append((char) ((c - base + shift + 26) % 26 + base));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}
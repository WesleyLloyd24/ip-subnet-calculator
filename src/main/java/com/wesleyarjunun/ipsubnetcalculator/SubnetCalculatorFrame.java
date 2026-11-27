
package com.wesleyarjunun.ipsubnetcalculator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class SubnetCalculatorFrame extends JFrame {

    private final IPv4SubnetCalculator calculator = new IPv4SubnetCalculator();
    private final JTextField inputField = new JTextField("192.168.1.10/24");
    private final JTextArea resultsArea = new JTextArea();

    public SubnetCalculatorFrame() {
        setTitle("IP & Subnet Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(620, 570);
        setMinimumSize(new Dimension(560, 520));
        setLocationRelativeTo(null);

        buildUI();
        calculate();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 16));
        root.setBorder(new EmptyBorder(24, 28, 24, 28));
        setContentPane(root);

        JLabel title = new JLabel("IP & Subnet Calculator");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 23f));

        JLabel subtitle = new JLabel(
                "Calculate IPv4 subnet details using CIDR notation."
        );
        subtitle.setForeground(new Color(95, 105, 115));

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.add(title);
        header.add(Box.createVerticalStrut(6));
        header.add(subtitle);

        root.add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(0, 12));

        JPanel inputPanel = new JPanel(new BorderLayout(8, 0));
        inputPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "IPv4 address / CIDR prefix"
                )
        );

        inputField.setFont(
                new Font(Font.MONOSPACED, Font.PLAIN, 15)
        );
        inputField.setMargin(new Insets(8, 8, 8, 8));

        JButton calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(e -> calculate());
        inputField.addActionListener(e -> calculate());

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(calculateButton, BorderLayout.EAST);

        center.add(inputPanel, BorderLayout.NORTH);

        resultsArea.setEditable(false);
        resultsArea.setFont(
                new Font(Font.MONOSPACED, Font.PLAIN, 14)
        );
        resultsArea.setMargin(new Insets(12, 12, 12, 12));

        JScrollPane scrollPane = new JScrollPane(resultsArea);
        scrollPane.setBorder(
                BorderFactory.createTitledBorder("Calculation results")
        );

        center.add(scrollPane, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());

        JLabel hint = new JLabel("Example: 10.0.0.25/24");
        hint.setForeground(new Color(95, 105, 115));

        JButton copyButton = new JButton("Copy results");
        copyButton.addActionListener(e -> copyResults());

        bottom.add(hint, BorderLayout.WEST);
        bottom.add(copyButton, BorderLayout.EAST);

        center.add(bottom, BorderLayout.SOUTH);

        root.add(center, BorderLayout.CENTER);
    }

    private void calculate() {
        try {
            IPv4SubnetCalculator.SubnetResult result =
                    calculator.calculate(inputField.getText());

            resultsArea.setText(result.toReport());
            resultsArea.setCaretPosition(0);

        } catch (IllegalArgumentException ex) {

            resultsArea.setText(
                    "Unable to calculate subnet.\n\n" + ex.getMessage()
            );
        }
    }

    private void copyResults() {
        String report = resultsArea.getText();

        if (report.isBlank()) {
            return;
        }

        Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .setContents(new StringSelection(report), null);

        JOptionPane.showMessageDialog(
                this,
                "Calculation results copied to clipboard.",
                "Copied",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}

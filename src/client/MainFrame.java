package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class MainFrame extends JFrame {
    private final String FONT_NAME = "Hancom Gothic";
    private Font bigFont;
    private Font mediumFont;
    private Font smallFont;

    private CardLayout cardLayout;
    private JPanel boardPanel;
    private JPanel bingoTextPanel;
    private JPanel bingoButtonPanel;
    private JTextField[] bingoTextFields;
    private JButton[] bingoButtons;

    private JPanel chatPanel;
    private JTextArea chatTextArea;
    private JScrollPane chatScrollPane;
    private JTextField chatInputTextField;

    private JPanel buttonPanel;
    private JButton setButton;
    private JButton resetButton;
    private JButton readyButton;
    private JButton unreadyButton;

    public MainFrame() {
        initializeFont();
        initializeBingoPanel();
        initializeChatPanel();
        initializeButtonPanel();
        initializeMainFrame();
    }

    private void initializeMainFrame() {
        setTitle("빙고프로그램");
        setLayout(new BorderLayout());
        setSize(new Dimension(720, 480));

        add(boardPanel, BorderLayout.WEST);
        add(chatPanel, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void initializeFont() {
        bigFont = new Font(FONT_NAME, Font.PLAIN, 30);
        mediumFont = new Font(FONT_NAME, Font.PLAIN, 18);
        smallFont = new Font(FONT_NAME, Font.PLAIN, 14);
    }

    private void initializeBingoPanel() {
        cardLayout = new CardLayout();
        boardPanel = new JPanel(cardLayout);
        bingoTextPanel = new JPanel(new GridLayout(BingoGame.BINGO_WIDTH, BingoGame.BINGO_HEIGHT));
        bingoButtonPanel = new JPanel(new GridLayout(BingoGame.BINGO_WIDTH, BingoGame.BINGO_HEIGHT));
        bingoTextFields = new JTextField[BingoGame.BINGO_TOTAL];
        bingoButtons = new JButton[BingoGame.BINGO_TOTAL];

        for (int i = 0; i < BingoGame.BINGO_TOTAL; i++) {
            bingoTextFields[i] = new JTextField(i +1 + "", 2);
            bingoTextFields[i].setFont(bigFont);
            bingoTextPanel.add(bingoTextFields[i]);

            bingoButtons[i] = new JButton();
            bingoButtons[i].setFont(bigFont);
            bingoButtons[i].setEnabled(false);
            bingoButtonPanel.add(bingoButtons[i]);
        }

        boardPanel.setPreferredSize(new Dimension(360, 360));

        boardPanel.add(bingoTextPanel, "texts");
        boardPanel.add(bingoButtonPanel, "buttons");
    }

    private void initializeChatPanel() {
        chatPanel = new JPanel(new BorderLayout());
        chatTextArea = new JTextArea();
        chatScrollPane = new JScrollPane(chatTextArea);
        chatInputTextField = new JTextField();

        chatTextArea.setFont(smallFont);
        chatInputTextField.setFont(smallFont);

        chatTextArea.setEditable(false);

        chatPanel.setPreferredSize(new Dimension(340, 360));

        chatPanel.add(chatScrollPane, BorderLayout.CENTER);
        chatPanel.add(chatInputTextField, BorderLayout.SOUTH);
    }

    private void initializeButtonPanel() {
        buttonPanel = new JPanel(new FlowLayout());
        setButton = new JButton("빙고판 설정");
        resetButton = new JButton("빙고판 재설정");
        readyButton = new JButton("준비 완료");
        unreadyButton = new JButton("준비 해제");

        setButton.setFont(mediumFont);
        resetButton.setFont(mediumFont);
        readyButton.setFont(mediumFont);
        unreadyButton.setFont(mediumFont);

        resetButton.setEnabled(false);
        readyButton.setEnabled(false);
        unreadyButton.setEnabled(false);

        buttonPanel.setPreferredSize(new Dimension(640, 45));

        buttonPanel.add(setButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(readyButton);
        buttonPanel.add(unreadyButton);
    }

    public JButton[] getBingoButtons() {
        return bingoButtons;
    }

    public JButton getSetButton() {
        return setButton;
    }

    public JButton getResetButton() {
        return resetButton;
    }

    public JButton getReadyButton() {
        return readyButton;
    }

    public JButton getUnreadyButton() {
        return unreadyButton;
    }

    public void addListeners(ActionListener actionListener, KeyListener keyListener) {
        for (JButton button : bingoButtons) {
            button.addActionListener(actionListener);
        }

        setButton.addActionListener(actionListener);
        resetButton.addActionListener(actionListener);
        readyButton.addActionListener(actionListener);
        unreadyButton.addActionListener(actionListener);

        chatInputTextField.addKeyListener(keyListener);
    }

    public String showInputDialog(String message) {
        return JOptionPane.showInputDialog(this, message);
    }

    public void showMessageDialog(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public String getMessage() {
        String message ="";

        if (!chatInputTextField.getText().toString().isEmpty()) {
            message =chatInputTextField.getText().toString();
            chatInputTextField.setText("");
        }

        return message;
    }

    public void appendChat(String message) {
        chatTextArea.append(message + "\n");
    }

    public void setBoard() {
        if (isBoardEmpty()) {
            showMessageDialog("빈 칸을 채워주세요.");
            return;
        } else if (!isValueValid()) {
            showMessageDialog("1에서 50 사이의 숫자만 입력해주세요.");
            return;
        } else if (isValueDuplicate()) {
            showMessageDialog("중복된 값이 존재합니다.");
            return;
        }

        for (int i = 0; i < BingoGame.BINGO_TOTAL; i++) {
            String value = bingoTextFields[i].getText().toString();
            bingoButtons[i].setText(value);
        }

        setButton.setEnabled(false);
        resetButton.setEnabled(true);
        readyButton.setEnabled(true);

        cardLayout.show(boardPanel, "buttons");
    }

    private boolean isBoardEmpty() {
        boolean empty = false;

        for (JTextField textField : bingoTextFields) {
            if (textField.getText().toString().isEmpty()) {
                textField.setForeground(Color.WHITE);
                textField.setBackground(Color.RED);
                empty = true;
            }
        }

        return empty;
    }

    private boolean isValueValid() {
        boolean valid = true;

        for (JTextField textField : bingoTextFields) {
            int value = Integer.parseInt(textField.getText().toString());

            if (value < 0 || value > 50) {
                textField.setForeground(Color.WHITE);
                textField.setBackground(Color.RED);
                valid = false;
            }
        }

        return valid;
    }

    private boolean isValueDuplicate() {
        ArrayList<String> values = new ArrayList<>();
        boolean duplicate = false;

        for (JTextField textField : bingoTextFields) {
            String value = textField.getText().toString();

            if (values.size() == 0) {
                values.add(value);
            } else {
                if (values.contains(value)) {
                    textField.setForeground(Color.WHITE);
                    textField.setBackground(Color.RED);
                    duplicate = true;

                    for (JTextField duplicateField : bingoTextFields) {
                        if (duplicateField.getText().toString().equals(value)) {
                            duplicateField.setForeground(Color.WHITE);
                            duplicateField.setBackground(Color.RED);
                        }
                    }
                }

                values.add(value);
            }
        }

        return duplicate;
    }

    public void resetBoard() {
        setButton.setEnabled(true);
        resetButton.setEnabled(false);

        readyButton.setEnabled(false);

        cardLayout.show(boardPanel, "texts");
    }

    public void resetBackgroundColor() {
        for (JTextField textField : bingoTextFields) {
            textField.setForeground(Color.BLACK);
            textField.setBackground(Color.WHITE);
        }
    }

    public boolean isButtonsShowing() {
        if (bingoButtonPanel.isShowing()) {
            return true;
        } else {
            showMessageDialog("빙고판을 먼저 설정해주세요.");
            return false;
        }
    }

    public void ready() {
        for (JButton button : bingoButtons) {
            button.setEnabled(false);
        }

        setButton.setEnabled(false);
        resetButton.setEnabled(false);
        readyButton.setEnabled(false);
        unreadyButton.setEnabled(true);
    }

    public void unready() {
        for (JButton button : bingoButtons) {
            button.setEnabled(false);
        }

        setButton.setEnabled(false);
        resetButton.setEnabled(true);
        readyButton.setEnabled(true);
        unreadyButton.setEnabled(false);
    }

    public void start() {
        for (JButton button : bingoButtons) {
            button.setEnabled(true);
        }

        cardLayout.show(boardPanel, "buttons");

        setButton.setEnabled(false);
        resetButton.setEnabled(false);
        readyButton.setEnabled(false);
        unreadyButton.setEnabled(false);
    }

    public void end() {
        for (JButton button : bingoButtons) {
            button.setEnabled(false);
        }

        cardLayout.show(boardPanel, "texts");

        setButton.setEnabled(true);
        resetButton.setEnabled(false);
        readyButton.setEnabled(false);
        unreadyButton.setEnabled(false);
    }

    public ArrayList<String> getBingoCardValues() {
        ArrayList<String> values = new ArrayList<>();

        for (JTextField textField : bingoTextFields) {
            values.add(textField.getText());
        }

        return values;
    }

    public void checkButtonByValue(String value) {
        for (JButton button : bingoButtons) {
            if (button.getText().equals(value)) {
                button.setEnabled(false);
                return;
            }
        }
    }
}

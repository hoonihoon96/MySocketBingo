package client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.StringTokenizer;

public class Controller extends KeyAdapter implements ActionListener {
    private MainFrame mainFrame;
    private BingoGame bingoGame;
    private SocketHandler handler;

    private boolean turn = false;

    public Controller() {
        mainFrame = new MainFrame();
        mainFrame.addListeners(this, this);

        try {
            handler = new SocketHandler();
        } catch (IOException e) {
            mainFrame.showMessageDialog("연결 중 에러가 발생했습니다.");
            System.exit(0);
        }
        String name = mainFrame.showInputDialog("이름을 입력해주세요.");
        handler.send("NAME/" + name);

        bingoGame = new BingoGame();
    }

    public void parse() {
        new Thread(() -> {
            try {
                String readLine;

                while ((readLine = handler.receive()) != null) {
                    StringTokenizer tokenizer = new StringTokenizer(readLine, "/");
                    String token = tokenizer.nextToken();

                    System.out.println(readLine);

                    if (token.equals("ENTER")) {
                        String name = tokenizer.nextToken();
                        mainFrame.appendChat("[알림]" + name + "님이 입장했습니다.");

                    } else if (token.equals("EXIT")) {
                        String name = tokenizer.nextToken();
                        mainFrame.appendChat("[알림]" + name + "님이 퇴장했습니다.");


                    } else if (token.equals("FULL")) {
                        mainFrame.showMessageDialog("방이 꽉차서 접속하지 못 했습니다.");
                    } else if (token.equals("MESSAGE")) {
                        String message = tokenizer.nextToken();
                        mainFrame.appendChat(message);


                    } else if (token.equals("START")) {
                        mainFrame.start();

                    } else if (token.equals("STATUS")) {
                        String name = tokenizer.nextToken();
                        mainFrame.appendChat("[알림]" + name + "님의 차례입니다.");

                    } else if (token.equals("TURN")) {
                        mainFrame.showMessageDialog("당신 차레입니다.");
                        turn = true;

                    } else if (token.equals("VALUE")) {
                        String value = tokenizer.nextToken();
                        bingoGame.checkBingoCardByValue(value);
                        mainFrame.checkButtonByValue(value);
                        mainFrame.appendChat("[알림]이번에 불러진 숫자는 " + value + "입니다.");

                        if (bingoGame.isBingo()) {
                            handler.send("BINGO/");
                        }

                    } else if (token.equals("WIN")) {
                        String name = tokenizer.nextToken();
                        mainFrame.showMessageDialog(name + "님이 승리했습니다.");
                        mainFrame.end();

                    } else if (token.equals("DRAW")) {
                        mainFrame.showMessageDialog("비겼습니다.");
                        mainFrame.end();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                handler.disconnect();
                mainFrame.showMessageDialog("수신 중 에러가 발생했습니다.");
            }
        }).start();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            String message;

            if (!(message = mainFrame.getMessage()).isEmpty()) {
                handler.send("MESSAGE/" + message);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(mainFrame.getSetButton())) {
            mainFrame.resetBackgroundColor();
            mainFrame.setBoard();
            bingoGame.clearBingoCards();
            bingoGame.setBingoCards(mainFrame.getBingoCardValues());
        } else if (e.getSource().equals(mainFrame.getResetButton())) {
            mainFrame.resetBoard();
        } else if (e.getSource().equals(mainFrame.getReadyButton())) {
            mainFrame.ready();
            handler.send("READY/");
        } else if (e.getSource().equals(mainFrame.getUnreadyButton())) {
            mainFrame.unready();
            handler.send("UNREADY/");
        } else {
            for (JButton button : mainFrame.getBingoButtons()) {
                if (e.getSource() == button && turn) {
                    turn = false;

                    String value = button.getText().toString();
                    handler.send("VALUE/" + value);

                    if (bingoGame.isBingo()) {
                        handler.send("BINGO/");
                    }
                }
            }
        }
    }
}

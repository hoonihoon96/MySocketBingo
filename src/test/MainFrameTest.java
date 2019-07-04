package test;

import client.MainFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainFrameTest extends KeyAdapter implements ActionListener {
    private MainFrame mainFrame;

    public MainFrameTest() {
        mainFrame = new MainFrame();
        mainFrame.addListeners(this, this);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(mainFrame.getSetButton())) {
            mainFrame.resetBackgroundColor();
            mainFrame.setBoard();
        } else if (e.getSource().equals(mainFrame.getResetButton())) {
            mainFrame.resetBoard();
        } else if (e.getSource().equals(mainFrame.getReadyButton())) {
            mainFrame.ready();
        } else if (e.getSource().equals(mainFrame.getUnreadyButton())) {
            mainFrame.unready();
        }
    }

    public static void main(String[] args) {
        new MainFrameTest();
    }
}

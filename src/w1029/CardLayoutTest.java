package w1029;

import center_frame.CenterFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CardLayoutTest extends JFrame {
    public CardLayoutTest() {
        setLayout(new GridLayout(5,5));
        for (int i = 0; i < 3; i++) {
            JButton btn = new JButton("버튼" + ( i+1));
            add(btn);
            
        }

        setTitle("cardLayoutTest");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CenterFrame cf = new CenterFrame(250, 250);
        cf.centerXY();
        setBounds(cf.getX(), cf.getY(), cf.getFw(), cf.getFh());
        setVisible(true);
    }
    public static void main(String[] args) {
        new CardLayoutTest();
    }
}

package w1022;

import center_frame.CenterFrame;

import javax.swing.*;
import java.awt.*;

public class BorderLayoutTest extends JFrame {
    public BorderLayoutTest() {
        setLayout(new FlowLayout(FlowLayout.RIGHT,15,15));
        String[] locs = {BorderLayout.EAST, BorderLayout.WEST,BorderLayout.SOUTH,BorderLayout.NORTH,BorderLayout.CENTER};


        JButton[] btns = new JButton[5];
        for (int i = 0; i < btns.length; i++) {
            btns[i] = new JButton("버튼" + (i+1));
            add(btns[i], locs[i]);
        }



        setTitle("BorderLayoutTest 연습");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CenterFrame cf = new CenterFrame(300, 300);
        cf.centerXY();
        setBounds(cf.getX(), cf.getY(), cf.getFw(), cf.getFh());
        setVisible(true);
    }
    public static void main(String[] args) {
        new BorderLayoutTest();
    }
}

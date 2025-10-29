package w1029;

import center_frame.CenterFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NullLayoutTest extends JFrame {
    public NullLayoutTest() {
        setLayout(null);
        int[] xy = {50,70,90,110};

        for (int i = 0; i < xy.length; i++) {
            JButton btn = new JButton( "버튼" + (i+1));
            btn.setBounds(xy[i], xy[i], 70, 60);
            add(btn);
        }

        setTitle("LayoutManger Null");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CenterFrame cf = new CenterFrame(250, 250);
        cf.centerXY();
        setBounds(cf.getX(), cf.getY(), cf.getFw(), cf.getFh());
        setVisible(true);

    }

    public static void main(String[] args) {
        new NullLayoutTest();
    }
}

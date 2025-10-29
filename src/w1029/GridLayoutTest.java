package w1029;

import center_frame.CenterFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GridLayoutTest extends JFrame {
    public GridLayoutTest() {
        setLayout(new GridLayout(3,3,10,10));

        for (int i = 0; i < 9; i++) {
            JButton btn = new JButton("버튼" + ( i+1));
            add(btn);
            btn.addActionListener(btnListener);

        }

        setTitle("GridLayoutTest");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CenterFrame cf = new CenterFrame(250, 250);
        cf.centerXY();
        setBounds(cf.getX(), cf.getY(), cf.getFw(), cf.getFh());
        setVisible(true);
    }
    ActionListener btnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton btn = (JButton) e.getSource();
            JOptionPane.showMessageDialog(null, btn.getText()+"선택하였습니다");
        }
    };
    public static void main(String[] args) {
        new GridLayoutTest();
    }
}

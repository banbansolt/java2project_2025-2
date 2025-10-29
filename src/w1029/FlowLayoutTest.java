package w1029;

import center_frame.CenterFrame;
import mvc_jdbc_test.entity.Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class FlowLayoutTest extends JFrame {
    public FlowLayoutTest() {
        setLayout(new FlowLayout(FlowLayout.RIGHT,15,15));
        ArrayList<JButton> buttons = new ArrayList<JButton>();
        for (int i = 0; i < 7; i++) {
            buttons.add(new JButton("버튼" + ( i+1)));
        }
        for (JButton button : buttons) {
            add(button);
            button.addActionListener(btnListener);
        }

        setTitle("FlowLayoutTest");
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
            JOptionPane.showMessageDialog(null, btn.getText());
        }
    };

    public static void main(String[] args) {
        new FlowLayoutTest();
    }
}

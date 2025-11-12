package w1112;

import center_frame.CenterFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MessageDialogTest extends JFrame implements ActionListener {
    String[] btnStrs = {"Plain", "Warning", "Information"};
    JButton[] buttons = new JButton[btnStrs.length];
    public MessageDialogTest() {
        JPanel panel = new JPanel();
        add(panel, "North");
        for (int i = 0; i < btnStrs.length; i++) {
            buttons[i] = new JButton(btnStrs[i]);
            buttons[i].addActionListener(this);
            panel.add(buttons[i]);
        }
        setTitle("툴바 작성");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CenterFrame cf = new CenterFrame( 500,  400);
        cf.centerXY();
        setBounds(cf.getX(), cf.getY(), cf.getFw(), cf.getFh());
        setVisible(true);
    }
    public static void main(String[] args) {
        new MessageDialogTest();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String eBtnStr = e.getActionCommand();
        switch (eBtnStr) {
            case "Plain":
                JOptionPane.showMessageDialog(null, "일반메시지대화상자입니다.", "Plain option", JOptionPane.PLAIN_MESSAGE);
                break;
            case "Warning":
                JOptionPane.showMessageDialog(null, "일반메시지대화상자입니다.", "Warning", JOptionPane.PLAIN_MESSAGE);
                break;
            case "Information":
                JOptionPane.showMessageDialog(null, "일반메시지대화상자입니다.", "Information", JOptionPane.PLAIN_MESSAGE);
                break;

        }
    }
}

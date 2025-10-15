package w1015;

import center_frame.CenterFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RadioButtonTest extends JFrame {
    String[] rbStrArr = {"아이브", "뉴진스", "블랙핑크"};
    String[] imgNameArr = {"ive", "newjeans", "blackpink"};
    JRadioButton[] rbArr = new JRadioButton[rbStrArr.length];
    JLabel lbl = new JLabel("", JLabel.CENTER);

    public RadioButtonTest() {
        JPanel panel = new JPanel();
        ButtonGroup bg = new ButtonGroup();
        int i = 0;
        for (String str : rbStrArr) {
            rbArr[i] = new JRadioButton(str);
            rbArr[i].addActionListener(radioListener);
            bg.add(rbArr[i]);
            panel.add(rbArr[i]);
            i++;
        }
        lbl.setOpaque(true);
        lbl.setBackground(Color.PINK);
        add(panel, "North");
        add(lbl, "Center");

        setTitle("RadioButtonTest");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        CenterFrame cf = new CenterFrame(500, 300);
        cf.centerXY();
        setBounds(cf.getX(), cf.getY(), cf.getFw(), cf.getFh());
        setVisible(true);
    }

    ActionListener radioListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int i = 0;

            for (JRadioButton rb : rbArr) {
                if (rb.isSelected()) {
                    ImageIcon imageIcon = new ImageIcon(".\\imgs_root\\" + imgNameArr[i] + ".jpg");
                    lbl.setIcon(imageIcon);
                    JOptionPane.showMessageDialog(null, rb.getText() + "가 선택되었어요~");
                }
                i++;
            }
        }
    };

    public static void main(String[] args) {
        new RadioButtonTest();
    }
}
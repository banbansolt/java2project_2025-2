package w1022;

import center_frame.CenterFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TextFieldTest extends JFrame {
    JTextField tf;
    JTextArea ta;
    JPasswordField pf;
    JButton btn;

    public TextFieldTest() {

        tf = new JTextField(10);
        ta = new JTextArea(5, 10);
        pf = new JPasswordField(10);
        btn = new JButton("입력 확인");


        JScrollPane sp = new JScrollPane(ta);

        setLayout(new FlowLayout());
        add(tf);
        add(sp);
        add(pf);
        add(btn);


        btn.addActionListener(btnListener);


        setTitle("텍스트 입력 가능 컴포넌트");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CenterFrame cf = new CenterFrame(200, 250);
        cf.centerXY();
        setBounds(cf.getX(), cf.getY(), cf.getFw(), cf.getFh());
        setVisible(true);
    }

    ActionListener btnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String tfText = tf.getText();
            String taText = ta.getText();
            String pwText = String.copyValueOf(pf.getPassword());
            JOptionPane.showMessageDialog(null,  tfText + "\n"  + taText + "\n" +   pwText);
        }
    };

    public static void main(String[] args) {
        new TextFieldTest();
    }
}

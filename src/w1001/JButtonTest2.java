package w1001;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JButtonTest2 extends JFrame{
    JTextField tf;

    public JButtonTest2() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int fw = 500;
        int fh = 200;
        int x = screenWidth/2 - fw/2;
        int y = screenHeight/2 - fh/2;

        setLayout(new FlowLayout());
        setTitle("JButtonTest2");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JButton btn = new JButton("확인");
        tf = new JTextField(20);
        add(btn); add(tf);
        btn.addActionListener(btnListener);
        setBounds(x, y, fw, fh);
        setVisible(true);
    }

    ActionListener btnListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null, tf.getText()+"가(이) 입력되었습니다.");
        }
    };

    public static void main(String[] args) {
        new JButtonTest2();
    }
}
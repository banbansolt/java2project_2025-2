package w1001;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JButtonTest extends JFrame {
    public JButtonTest(){
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setTitle("JButtonTest");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        String[] lblTexts = {"폴리텍", "서울정수", "인공지능", "소프트웨어과", "1학년"};
        JButton[] btns = new JButton[lblTexts.length];
        for(int i = 0; i < lblTexts.length; i++){
            btns[i] = new JButton(lblTexts[i]);
            btns[i].addActionListener(btnListener);
            add(btns[i]);
        }


//        JButton btn = new JButton("클릭하세요");
//        btn.addActionListener(btnListener);
//        add(btn);
//
//        JButton btn2 = new JButton("인공지능소프트웨어과");
//        btn2.addActionListener(btnListener);
//        add(btn2);
//
//        JButton btn3 = new JButton("인공지능");
//        btn3.addActionListener(btnListener);
//        add(btn3);
//
//        JButton btn4 = new JButton("소프트웨어과");
//        btn4.addActionListener(btnListener);
//        add(btn4);
//
//        JButton btn5 = new JButton("인공");
//        btn5.addActionListener(btnListener);
//        add(btn5);

        setBounds(200,200,300,200);
        setVisible(true);
    }

    ActionListener btnListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null,e.getActionCommand());

        }
    };
    public static void main(String[] args) {
        new JButtonTest();
    }

}

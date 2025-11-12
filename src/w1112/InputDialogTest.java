package w1112;

import center_frame.CenterFrame;

import javax.swing.*;


public class InputDialogTest extends JFrame {
    public InputDialogTest() {
    setTitle("입력대화상자 연습");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    CenterFrame cf = new CenterFrame( 500,  400);
    cf.centerXY();
    setBounds(cf.getX(), cf.getY(), cf.getFw(), cf.getFh());
    setVisible(true);

    String name = JOptionPane.showInputDialog(null, "이름 입력", "홍길동");
    int age = Integer.parseInt(JOptionPane.showInputDialog(null, "나이 입력", 20));
    JOptionPane.showMessageDialog(null, "이름:"+name +"\n 나이:" + age+"세", "입력결과", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        new InputDialogTest();
    }
}

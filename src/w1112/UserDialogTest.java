package w1112;

import center_frame.CenterFrame;

import javax.swing.*;

public class UserDialogTest extends JFrame {

    public UserDialogTest() {
        setTitle("사용자정의 대화상자 연습");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        CenterFrame cf = new CenterFrame( 500, 400);
        cf.centerXY();
        setBounds(cf.getX(), cf.getY(), cf.getFw(), cf.getFh());
        setVisible(true);

        String[] strOptions = {"뉴진스", "블랙핑크", "아이브", "르세라핌"};


        int option = JOptionPane.showOptionDialog(null, "가장 좋아하는 아이돌을 선택해 주세요.", "2025toyl", JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null,strOptions, strOptions[0]);


        JOptionPane.showMessageDialog(
                null,
                strOptions[option] + " 아이돌이 선택되었습니다.",
                "좋아하는 아이돌",
                JOptionPane.PLAIN_MESSAGE
        );
    }

    public static void main(String[] args) {
        new UserDialogTest();
    }
}
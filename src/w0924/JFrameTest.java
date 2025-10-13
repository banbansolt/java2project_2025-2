package w0924;

 import javax.swing.*;

public class JFrameTest extends JFrame
{
    public JFrameTest()
    {
        setTitle("처음 만드는 프레임");//제목
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500,500);           //화면의 크기
        setLocation(200,200); //화면이 처음 나오는 위치
        setVisible(true);                   //화면에 보이기 위해 사용

    }

    public static void main(String[] args) {
        new JFrameTest();       //생성자 호출
    }
}
package w0924;

 import javax.swing.*;

public class JFrameTest extends JFrame
{
    public JFrameTest()
    {
        setTitle("처음 만드는 프레임");             //제목
        setSize(400,400);           //화면의 크기
        setVisible(true);                   //화면에 보이기 위해 사용
        setLocation(-10,0);             //화면이 처음 나오는 위치
    }
    public static void main(String[] args)
    {
        new JFrameTest();       //생성자 호출
    }
}
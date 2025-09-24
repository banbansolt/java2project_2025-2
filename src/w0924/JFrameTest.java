package w0924;

import javax.swing.*;

public class JFrameTest {

    public JFrameTest(){
        setTitle("처음 만드는 프레임");
        setSize(500,500);
        setLocation(200,200);
        setVisible(true);
    }

    public static void main(String[] args) {
        new JFrameTest();
    }
}
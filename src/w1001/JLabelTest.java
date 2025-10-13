import javax.swing.*;
import java.awt.*;

public class JLabelTest extends JFrame {
    public JLabelTest() { // 1 usage new *
        setTitle("JLabelTest");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
//        setLocation(x, y) + setSize(width, height)
        String[] lblTexts = {"한국폴리텍대학", "서울 정수캠퍼스", "인공지능소프트웨어과"};
        String[] locTexts = {"North", "Center", "South"};
        Color[] lblBgColors = {Color.orange, Color.magenta, Color.black};

        JLabel[] lbls = new JLabel[lblTexts.length];
        for (int i = 0; i < lbls.length; i++) {
            lbls[i] = new JLabel(lblTexts[i], JLabel.CENTER);
            lbls[i].setForeground(Color.white);
            lbls[i].setBackground(lblBgColors[i]);
            lbls[i].setOpaque(true);
            add(lbls[i], locTexts[i]);
        }

        Font font = new Font( "맑은 고딕", Font.BOLD,  30);
        lbls[2].setFont(font);

        setBounds( 100, 100,  350, 400);
        setVisible(true);
    }

    public static void main(String[] args) { // 1 usage new *
        new JLabelTest();
    }
}
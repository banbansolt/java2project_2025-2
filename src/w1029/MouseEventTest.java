package w1029;

import center_frame.CenterFrame;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseEventTest extends JFrame {

    public MouseEventTest() {
        addMouseListener(mouseListener);

        setTitle("MouseEventTest");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CenterFrame cf = new CenterFrame(250, 250);
        cf.centerXY();
        setBounds(cf.getX(), cf.getY(), cf.getFw(), cf.getFh());
        setVisible(true);
    }

    MouseListener mouseListener = new MouseListener() {

        @Override
        public void mouseClicked(MouseEvent e) {
            switch (e.getButton()) {
                case MouseEvent.BUTTON1:
                    JOptionPane.showMessageDialog(null, "왼쪽 버튼을 클릭했군요.");
                    break;
                case MouseEvent.BUTTON2:
                    JOptionPane.showMessageDialog(null, "가운데 버튼을 클릭했군요.");
                    break;
                case MouseEvent.BUTTON3:
                    JOptionPane.showMessageDialog(null, "오른쪽 버튼을 클릭했군요.");
                    break;
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    };

    public static void main(String[] args) {
        new MouseEventTest();
    }
}
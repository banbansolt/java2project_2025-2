package w1105;

import center_frame.CenterFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Mouse extends JFrame {


        int x1, y1, x2, y2;


        DrawPanel dp = new DrawPanel(Color.yellow);

        public Mouse() {

            add(dp);

            dp.addMouseListener(mouseListener);

            setTitle("마우스로 선 그리기");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            CenterFrame cf = new CenterFrame(500, 400);
            cf.centerXY();
            setBounds(cf.getX(), cf.getY(), cf.getFw(), cf.getFh());


            setVisible(true);
        }


        MouseAdapter mouseListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                x1 = e.getX();
                y1 = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {

                x2 = e.getX();
                y2 = e.getY();
                dp.repaint();
            }
        };


        class DrawPanel extends JPanel {

            public DrawPanel(Color color) {

                setBackground(color);
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                g.drawLine(x1, y1, x2, y2);
            }
        }


        public static void main(String[] args) {
            new Mouse();
        }
    }
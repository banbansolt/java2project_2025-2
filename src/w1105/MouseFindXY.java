package w1105;

import center_frame.CenterFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseFindXY extends JFrame {

    JTextField tfX, tfY;

    public MouseFindXY() {
        setLayout(new FlowLayout());
        addMouseListener(mouseListener);
        tfX = new JTextField(10);
        tfY = new JTextField(10);
        JLabel lblX = new JLabel("X좌표:");
        JLabel lblY = new JLabel("Y좌표:");
        add(lblX);
        add(tfX);
        add(lblY);
        add(tfY);
        setTitle("MouseEvent find location");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CenterFrame cf = new CenterFrame(500, 400);
        cf.centerXY();
        setBounds(cf.getX(), cf.getY(), cf.getFw(), cf.getFh());

        setVisible(true);
    }

    MouseAdapter mouseListener = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            tfX.setText(e.getX() + "");
            tfY.setText(e.getY() + "");
        }
    };

    public static void main(String[] args) {
        new MouseFindXY();
    }
}
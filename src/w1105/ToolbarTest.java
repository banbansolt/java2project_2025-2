package w1105;

import center_frame.CenterFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToolbarTest extends JFrame {
    JLabel label = new JLabel("툴바의 항목을 선택하세요.");
    JButton btnNew = new JButton(new ImageIcon("./toolbar_icons/new.png"));
    JButton btnOpen = new JButton(new ImageIcon("./toolbar_icons/open.png"));
    JButton btnClose = new JButton(new ImageIcon("./toolbar_icons/close.png"));

    public ToolbarTest() {
        JToolBar toolbar = new JToolBar();
        add(toolbar, BorderLayout.NORTH);
        add(label);

        toolbar.add(btnNew);
        toolbar.add(btnOpen);
        toolbar.addSeparator();
        toolbar.add(btnClose);

        btnNew.addActionListener(btnListener);
        btnOpen.addActionListener(btnListener);
        btnClose.addActionListener(btnListener);

        setTitle("툴바 작성");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CenterFrame cf = new CenterFrame( 500,  400);
        cf.centerXY();
        setBounds(cf.getX(), cf.getY(), cf.getFw(), cf.getFh());
        setVisible(true);
    }

    ActionListener btnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            if (button == btnNew) {
                label.setText("[새문서] 항목을 선택하셨어요~");
            } else if (button == btnOpen) {
                label.setText("[열기] 항목을 선택하셨어요~");
            } else {
                System.exit(0);
            }
        }
    };


    public static void main(String[] args) {
        new ToolbarTest();
    }
}
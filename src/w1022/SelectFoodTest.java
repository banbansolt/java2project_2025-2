package w1022;

import center_frame.CenterFrame;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelectFoodTest extends JFrame {
    JComboBox combo;
    JLabel imgLbl;
    String[] items = {"불고기", "비빔밥", "피자", "파스타", "스테이크"};

    public SelectFoodTest() {
        JPanel northPan = new JPanel();
        JLabel lbl = new JLabel("Food: ");
        combo = new JComboBox(items);
        JButton btn = new JButton("선택");
        northPan.add(lbl);
        northPan.add(combo);
        northPan.add(btn);
        add(northPan, "North");
        imgLbl = new JLabel(new ImageIcon("./imgs_root/food_imgs/food0.jpg"));
        add(imgLbl, "Center");

        // EventProgramming
        btn.addActionListener(btnListener);

        setTitle("항목선택 가능한 컴포넌트 JList and JComboBox");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CenterFrame cf = new CenterFrame(500, 400);
        cf.centerXY();
        setBounds(cf.getX(), cf.getY(), cf.getFw(), cf.getFh());
        setVisible(true);
    }

    ActionListener btnListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = combo.getSelectedIndex();
            ImageIcon imgIcon = new ImageIcon("./imgs_root/food_imgs/food" + selectedIndex + ".jpg");
            imgLbl.setIcon(imgIcon);
        }
    };

    public static void main(String[] args) {
        new SelectFoodTest();
    }
}

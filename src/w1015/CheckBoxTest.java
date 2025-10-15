package w1015;

import center_frame.CenterFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CheckBoxTest extends JFrame{
    JCheckBox checkBox;

    public CheckBoxTest() {
        setLayout(new FlowLayout());
        setTitle("CheckBowTest");
        checkBox = new JCheckBox("체크박스를 선택하세요");
        add(checkBox);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        CenterFrame cf = new CenterFrame(500,300);
        cf.centerXY();
        setBounds(cf.getX(),cf.getY(),cf.getFw(),cf.getFh());
        setVisible(true);
    }
    ActionListener CheckBowListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e){
            if(checkBox.isSelected()){
                JOptionPane.showMessageDialog(null,"체크박스가 선택되었습니다");
            }else {
                JOptionPane.showMessageDialog(null,"체크박스가 해체되었습니다");
            }
        }

    };

    public static void main(String[] args) {
        new CheckBoxTest();
    }

}

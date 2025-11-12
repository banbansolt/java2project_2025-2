package w1112.simple_paint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PaintingPanel extends JPanel implements MouseListener {
    public static String polygon;
    public int x1, y1, x2, y2;

    public PaintingPanel(String polygon) {
        this.polygon = polygon;
        setBackground(Color.YELLOW);
        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        switch (polygon){
            case "선":
                g.setColor(Color.RED);
                g.drawLine(x1, y1, x2, y2);
                break;
            case "사각형":
                g.setColor(Color.MAGENTA);
                g.fillRect(x1, y1, x2-x1, y2-y1);
                break;
            case "타원":
                g.setColor(Color.BLUE);
                g.fillOval(x1, y1, x2-x1, y2-y1);
                break;
        }
        x1 = x2 = y1 = y2 = 0;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        x1 = e.getX();
        y1 = e.getY();

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        x2 = e.getX();
        y2 = e.getY();
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
import javax.swing.*;
import java.awt.*;

public class SplayTreeViewer extends JPanel {
    static private int height = 30;
    static private int width = 30;
    public void dualNode(int a, int b, int x1, int y1, int x2, int y2, Graphics g) {
        g.drawLine(x1 + 15, y1 + 15, x2 + 15, y2 + 15);
        g.setColor(new Color(0x4995BB));
        g.fillOval(x1, y1, width, height);
        g.fillOval(x2, y2, width, height);
        g.setColor(Color.black);
        g.drawOval(x1, y1, width, height);
        g.drawOval(x2, y2, width, height);
        g.setFont(new Font("Courier New", Font.ITALIC, 20));
        g.drawString(String.valueOf(a), x1+10, y1+20);
        g.drawString(String.valueOf(b), x2 + 10, y2 + 20);
    }
    @Override
    public void paintComponent(Graphics g) {
        dualNode(5, 6, 5, 5, 100, 100, g);
    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        jFrame.add(new SplayTreeViewer());
        jFrame.setSize(500, 500);
        jFrame.setVisible(true);
    }
}

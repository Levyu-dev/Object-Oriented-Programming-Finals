import javax.swing.*;
import java.awt.*;

public class Theme {
    public static final Color BG_YELLOW = new Color(252, 215, 87);
    public static final Color CARD_RED = new Color(193, 59, 27);
    public static final Color TEXT_DARK = new Color(150, 40, 15);
    public static final Color WHITE = Color.WHITE;

    public static JButton createRedButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(CARD_RED);
        btn.setForeground(WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static JButton createWhiteButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(WHITE);
        btn.setForeground(TEXT_DARK);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static JButton createCircleButton(String label, String tooltip) {
        JButton btn = new JButton(label) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setPreferredSize(new Dimension(54, 54));
        btn.setMaximumSize(new Dimension(54, 54));
        btn.setFont(new Font("SansSerif", Font.BOLD, 11));
        btn.setForeground(TEXT_DARK);
        btn.setToolTipText(tooltip);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static JProgressBar createProgressBar(int value, int max, Color fill, Color track) {
        JProgressBar bar = new JProgressBar(0, max);
        bar.setValue(value);
        bar.setForeground(fill);
        bar.setBackground(track);
        bar.setBorderPainted(false);
        bar.setPreferredSize(new Dimension(0, 14));
        return bar;
    }
}

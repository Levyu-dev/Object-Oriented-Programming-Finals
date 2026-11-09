import javax.swing.*;
import java.awt.*;

public class Theme {
    public static final Color BG_YELLOW = new Color(252, 215, 87);
    public static final Color CARD_RED = new Color(193, 59, 27);
    public static final Color CARD_RED_HOVER = new Color(165, 45, 18);
    public static final Color TEXT_DARK = new Color(150, 40, 15);
    public static final Color WHITE = Color.WHITE;

    public static JButton createRedButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(CARD_RED);
        btn.setForeground(WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 16));
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
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}

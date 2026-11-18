package App;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicProgressBarUI;

public class Theme {
    // Palette Colors from Image (#d6fb00, #ecffb6, #00545f)
    public static final Color BG_YELLOW = new Color(214, 251, 0);      // #d6fb00 (Bright Lime)
    public static final Color BG_CREAM = new Color(236, 255, 182);     // #ecffb6 (Pale Lime)
    public static final Color CARD_RED = new Color(0, 84, 95);         // #00545f (Deep Teal)
    public static final Color CARD_RED_HOVER = new Color(0, 62, 70);   // Darker Teal for hover
    public static final Color TEXT_DARK = new Color(0, 84, 95);        // #00545f (Deep Teal)
    public static final Color WHITE = Color.WHITE;
    public static final Color SOFT_GRAY = new Color(224, 245, 168);    // Slightly deeper Pale Lime for hover

    // Note: Method name kept as createRedButton so your existing UI calls don't break
    public static JButton createRedButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? CARD_RED_HOVER : getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setBackground(CARD_RED);
        btn.setForeground(WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static JButton createWhiteButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? SOFT_GRAY : getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                // Updated border to match the teal/lime palette
                g2.setColor(new Color(0, 84, 95, 70)); 
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 22, 22);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setBackground(WHITE);
        btn.setForeground(TEXT_DARK);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static JButton createCircleButton(String label, String tooltip) {
        JButton btn = new JButton(label) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Updated drop-shadow accent to match the lime/teal palette
                g2.setColor(new Color(175, 210, 0)); 
                g2.fillOval(1, 2, getWidth() - 2, getHeight() - 2);
                g2.setColor(getModel().isRollover() ? BG_CREAM : WHITE);
                g2.fillOval(0, 0, getWidth() - 2, getHeight() - 2);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setPreferredSize(new Dimension(46, 46));
        btn.setMaximumSize(new Dimension(46, 46));
        btn.setFont(new Font("SansSerif", Font.BOLD, 10));
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
        bar.setOpaque(false);
        bar.setPreferredSize(new Dimension(0, 14));
        bar.setUI(new BasicProgressBarUI() {
            @Override
            protected void paintDeterminate(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = c.getWidth();
                int h = c.getHeight();
                g2.setColor(bar.getBackground());
                g2.fillRoundRect(0, 0, w, h, h, h);
                int fillWidth = (int) (w * bar.getPercentComplete());
                g2.setColor(bar.getForeground());
                g2.fillRoundRect(0, 0, fillWidth, h, h, h);
                g2.dispose();
            }
        });
        return bar;
    }

    public static JPanel createRoundedCard(Color bgColor, int padding) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(padding, padding, padding, padding));
        return card;
    }
}
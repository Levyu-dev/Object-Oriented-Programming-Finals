package App;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;

public class Theme {
    public static final String UI_FONT = "Segoe UI";

    // Balanced Lime & Deep Teal Palette (Neon replaced with fresh Pistachio-Lime)
    public static final Color PALE_LIME = new Color(236, 255, 182);      // #ecffb6 (Soft Pale Lime Background)
    public static final Color BRIGHT_LIME = new Color(184, 219, 72);     // #b8db48 (Calm Pistachio-Lime Accent)
    public static final Color SOFT_LIME_CARD = new Color(224, 246, 162); // Soft Mint-Lime for Cards
    public static final Color DEEP_TEAL = new Color(0, 84, 95);          // #00545f (Deep Teal)
    public static final Color DEEP_TEAL_HOVER = new Color(0, 62, 70);    // Darker Teal for hover
    public static final Color TEXT_DARK = new Color(0, 58, 66);          // Rich Deep Teal for crisp reading
    public static final Color WHITE = Color.WHITE;
    public static final Color INPUT_BACKGROUND = new Color(250, 252, 243);
    public static final Color INPUT_BORDER = new Color(0, 84, 95, 90);

    public static void styleTextField(JTextField field) {
        field.setFont(new Font(UI_FONT, Font.PLAIN, 14));
        field.setForeground(TEXT_DARK);
        field.setBackground(INPUT_BACKGROUND);
        field.setCaretColor(DEEP_TEAL);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(INPUT_BORDER),
                new EmptyBorder(6, 9, 6, 9)));
    }

    public static void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(new Font(UI_FONT, Font.PLAIN, 14));
        comboBox.setForeground(TEXT_DARK);
        comboBox.setBackground(INPUT_BACKGROUND);
        comboBox.setBorder(BorderFactory.createLineBorder(INPUT_BORDER));
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);
                label.setFont(new Font(UI_FONT, Font.PLAIN, 14));
                label.setForeground(TEXT_DARK);
                label.setBackground(isSelected ? BRIGHT_LIME : INPUT_BACKGROUND);
                label.setBorder(new EmptyBorder(6, 9, 6, 9));
                return label;
            }
        });
    }

    public static JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? DEEP_TEAL_HOVER : getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setBackground(DEEP_TEAL);
        btn.setForeground(WHITE);
        btn.setFont(new Font(UI_FONT, Font.BOLD, 13));
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
                g2.setColor(getModel().isRollover() ? BRIGHT_LIME : getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                g2.setColor(new Color(0, 84, 95, 80));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 22, 22);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setBackground(WHITE);
        btn.setForeground(TEXT_DARK);
        btn.setFont(new Font(UI_FONT, Font.BOLD, 12));
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
                g2.setColor(new Color(0, 84, 95, 45));
                g2.fillOval(1, 2, getWidth() - 2, getHeight() - 2);
                g2.setColor(getModel().isRollover() ? BRIGHT_LIME : WHITE);
                g2.fillOval(0, 0, getWidth() - 2, getHeight() - 2);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setPreferredSize(new Dimension(46, 46));
        btn.setMaximumSize(new Dimension(46, 46));
        btn.setFont(new Font(UI_FONT, Font.BOLD, 10));
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

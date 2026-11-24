package App.ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicProgressBarUI;
import javax.swing.table.JTableHeader;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.io.File;

public class Theme {
    public static final String UI_FONT = "Segoe UI";

    // Sage Green & Deep Teal Palette
    public static final Color PALE_LIME = new Color(241, 245, 233);
    public static final Color BRIGHT_LIME = new Color(201, 229, 138);
    public static final Color SOFT_LIME_CARD = new Color(225, 240, 184);
    public static final Color DEEP_TEAL = new Color(0, 84, 95);
    public static final Color DEEP_TEAL_HOVER = new Color(0, 62, 70);
    public static final Color TEXT_DARK = new Color(35, 74, 64);
    public static final Color WHITE = new Color(251, 252, 246);
    public static final Color INPUT_BACKGROUND = new Color(251, 252, 246);
    public static final Color INPUT_BORDER = new Color(0, 84, 95, 90);
    private static final String TEXT_STYLE_APPLIED = "App.textStyleApplied";
    private static final String TEXT_STYLE_LISTENER_ADDED = "App.textStyleListenerAdded";

    public static void applyTextStyling(Component component) {
        if (component instanceof JComponent) {
            JComponent swingComponent = (JComponent) component;
            if (!Boolean.TRUE.equals(swingComponent.getClientProperty(TEXT_STYLE_APPLIED))) {
                if (component instanceof JLabel
                        || component instanceof JTextComponent
                        || component instanceof JComboBox
                        || component instanceof JList
                        || component instanceof JTable
                        || component instanceof JTableHeader) {
                    Border existingBorder = swingComponent.getBorder();
                    if (existingBorder == null
                            || component instanceof JLabel
                            || component instanceof JComboBox
                            || component instanceof JList
                            || component instanceof JTable
                            || component instanceof JTableHeader) {
                        swingComponent.setBorder(new EmptyBorder(2, 4, 2, 4));
                    }
                }

                if (component instanceof JLabel
                        || component instanceof AbstractButton
                        || component instanceof JTextComponent
                        || component instanceof JComboBox
                        || component instanceof JList
                        || component instanceof JTable
                        || component instanceof JTableHeader) {
                    Font font = swingComponent.getFont();
                    if (font != null) {
                        swingComponent.setFont(font.deriveFont(font.getSize2D() + 2f));
                    }
                }
                swingComponent.putClientProperty(TEXT_STYLE_APPLIED, Boolean.TRUE);
            }
        }

        if (component instanceof Container) {
            Container container = (Container) component;
            if (component instanceof JComponent
                    && !Boolean.TRUE.equals(((JComponent) component)
                            .getClientProperty(TEXT_STYLE_LISTENER_ADDED))) {
                ((JComponent) component).putClientProperty(TEXT_STYLE_LISTENER_ADDED, Boolean.TRUE);
                container.addContainerListener(new ContainerAdapter() {
                    @Override
                    public void componentAdded(ContainerEvent event) {
                        applyTextStyling(event.getChild());
                    }
                });
            }
            for (Component child : container.getComponents()) {
                applyTextStyling(child);
            }
        }
    }

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
                    JList<?> choiceList, Object selectedValue, int rowIndex,
                    boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                        choiceList, selectedValue, rowIndex, isSelected, cellHasFocus);
                label.setFont(new Font(UI_FONT, Font.PLAIN, 15));
                label.setForeground(TEXT_DARK);
                label.setBackground(isSelected ? BRIGHT_LIME : INPUT_BACKGROUND);
                label.setBorder(new EmptyBorder(6, 9, 6, 9));
                return label;
            }
        });
    }

    public static JButton createPrimaryButton(String buttonText) {
        JButton button = new JButton(buttonText) {
            @Override
            protected void paintComponent(Graphics graphics) {
                Graphics2D graphics2D = (Graphics2D) graphics.create();
                graphics2D.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2D.setColor(getModel().isRollover() ? DEEP_TEAL_HOVER : getBackground());
                graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                graphics2D.dispose();
                super.paintComponent(graphics);
            }
        };
        button.setBackground(DEEP_TEAL);
        button.setForeground(WHITE);
        button.setFont(new Font(UI_FONT, Font.BOLD, 13));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static JButton createWhiteButton(String buttonText) {
        JButton button = new JButton(buttonText) {
            @Override
            protected void paintComponent(Graphics graphics) {
                Graphics2D graphics2D = (Graphics2D) graphics.create();
                graphics2D.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2D.setColor(getModel().isRollover() ? BRIGHT_LIME : getBackground());
                graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                graphics2D.setColor(new Color(0, 84, 95, 80));
                graphics2D.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 22, 22);
                graphics2D.dispose();
                super.paintComponent(graphics);
            }
        };
        button.setBackground(WHITE);
        button.setForeground(TEXT_DARK);
        button.setFont(new Font(UI_FONT, Font.BOLD, 12));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static JButton createCircleButton(String label, String tooltip) {
        JButton button = new JButton(label) {
            @Override
            protected void paintComponent(Graphics graphics) {
                Graphics2D graphics2D = (Graphics2D) graphics.create();
                graphics2D.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2D.setColor(new Color(0, 84, 95, 45));
                graphics2D.fillOval(1, 2, getWidth() - 2, getHeight() - 2);
                graphics2D.setColor(getModel().isRollover() ? BRIGHT_LIME : WHITE);
                graphics2D.fillOval(0, 0, getWidth() - 2, getHeight() - 2);
                graphics2D.dispose();
                super.paintComponent(graphics);
            }
        };
        button.setPreferredSize(new Dimension(46, 46));
        button.setMaximumSize(new Dimension(46, 46));
        button.setFont(new Font(UI_FONT, Font.BOLD, 8));
        button.setForeground(TEXT_DARK);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.CENTER);
        button.setToolTipText(tooltip);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static JProgressBar createProgressBar(
            int currentValue, int maximumValue, Color fillColor, Color trackColor) {
        JProgressBar progressBar = new JProgressBar(0, maximumValue);
        progressBar.setValue(currentValue);
        progressBar.setForeground(fillColor);
        progressBar.setBackground(trackColor);
        progressBar.setBorderPainted(false);
        progressBar.setOpaque(false);
        progressBar.setPreferredSize(new Dimension(0, 14));
        progressBar.setUI(new BasicProgressBarUI() {
            @Override
            protected void paintDeterminate(Graphics graphics, JComponent progressComponent) {
                Graphics2D graphics2D = (Graphics2D) graphics.create();
                graphics2D.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int barWidth = progressComponent.getWidth();
                int barHeight = progressComponent.getHeight();
                graphics2D.setColor(progressBar.getBackground());
                graphics2D.fillRoundRect(0, 0, barWidth, barHeight, barHeight, barHeight);
                int filledWidth = (int) (barWidth * progressBar.getPercentComplete());
                graphics2D.setColor(progressBar.getForeground());
                graphics2D.fillRoundRect(0, 0, filledWidth, barHeight, barHeight, barHeight);
                graphics2D.dispose();
            }
        });
        return progressBar;
    }

    public static JPanel createRoundedCard(Color backgroundColor, int padding) {
        JPanel roundedCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics graphics) {
                Graphics2D graphics2D = (Graphics2D) graphics.create();
                graphics2D.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2D.setColor(backgroundColor);
                graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                graphics2D.dispose();
            }
        };
        roundedCard.setOpaque(false);
        roundedCard.setBorder(new EmptyBorder(padding, padding, padding, padding));
        return roundedCard;
    }

    public static ImageIcon loadAssetIcon(String path, int width, int height) {
        File asset = new File(path);
        if (!asset.isFile()) {
            System.err.println("Asset not found: " + path);
            return null;
        }

        ImageIcon originalIcon = new ImageIcon(asset.getPath());
        if (originalIcon.getIconWidth() <= 0 || originalIcon.getIconHeight() <= 0) {
            System.err.println("Unable to load image asset: " + path);
            return null;
        }

        Image scaledImage = originalIcon.getImage().getScaledInstance(
                width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
}

package App;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PracticePanel extends BaseScreen {
    public PracticePanel(ApplicationController controller) {
        super(controller);
        setLayout(new BorderLayout(10, 10));
        setBackground(Theme.PALE_LIME);
        setBorder(new EmptyBorder(14, 16, 14, 16));

        JLabel header = new JLabel("Practice Setup");
        header.setFont(new Font(Theme.UI_FONT, Font.BOLD, 20));
        header.setForeground(Theme.TEXT_DARK);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(Theme.PALE_LIME);

        JPanel topicGrid = new JPanel(new GridLayout(3, 2, 8, 8));
        topicGrid.setBackground(Theme.PALE_LIME);
        List<String> topics = Arrays.asList("Java Basics", "Conditionals", "Loops", "Methods", "Arrays", "OOP");
        List<JButton> buttons = new ArrayList<>();

        for (int i = 0; i < topics.size(); i++) {
            String topic = topics.get(i);
            JButton btn = Theme.createPrimaryButton(topic);
            buttons.add(btn);
            if (i == 0) {
                btn.setBackground(Theme.BRIGHT_LIME);
                btn.setForeground(Theme.TEXT_DARK);
            }
            btn.addActionListener(e -> {
                for (JButton b : buttons) {
                    b.setBackground(Theme.DEEP_TEAL);
                    b.setForeground(Theme.WHITE);
                }
                btn.setBackground(Theme.BRIGHT_LIME);
                btn.setForeground(Theme.TEXT_DARK);
            });
            topicGrid.add(btn);
        }

        JPanel optionsCard = Theme.createRoundedCard(Theme.SOFT_LIME_CARD, 12);
        optionsCard.setLayout(new GridLayout(2, 2, 8, 8));

        JComboBox<String> diffBox = new JComboBox<>();
        diffBox.addItem("Easy");
        diffBox.addItem("Medium");
        diffBox.addItem("Hard");
        Theme.styleComboBox(diffBox);

        JTextField countField = new JTextField("5");
        Theme.styleTextField(countField);

        JLabel dLabel = new JLabel("Difficulty:");
        dLabel.setFont(new Font(Theme.UI_FONT, Font.BOLD, 12));
        dLabel.setForeground(Theme.TEXT_DARK);
        JLabel qLabel = new JLabel("Questions (1-19):");
        qLabel.setFont(new Font(Theme.UI_FONT, Font.BOLD, 12));
        qLabel.setForeground(Theme.TEXT_DARK);

        optionsCard.add(dLabel);
        optionsCard.add(diffBox);
        optionsCard.add(qLabel);
        optionsCard.add(countField);

        center.add(topicGrid);
        center.add(Box.createVerticalStrut(12));
        center.add(optionsCard);

        JButton startBtn = Theme.createPrimaryButton("START PRACTICE");
        startBtn.setPreferredSize(new Dimension(0, 44));
        startBtn.addActionListener(e -> {
            try {
                int count = Integer.parseInt(countField.getText().trim());
                if (count < 1 || count > 19) {
                    throw new InvalidMenuChoiceException("Enter 1 to 19 questions.");
                }
                controller.launchQuizSession(count);
            } catch (NumberFormatException ex) {
                controller.showError("Please type a valid number!");
            } catch (InvalidMenuChoiceException ex) {
                controller.showError(ex.getMessage());
            }
        });

        add(header, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(startBtn, BorderLayout.SOUTH);
    }

    @Override
    public void refresh() {
        revalidate();
        repaint();
    }
}

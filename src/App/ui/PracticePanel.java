package App.ui;

import App.controller.ApplicationController;
import App.model.UserSession;
import App.model.QuizQuestionBank;
import App.util.InvalidMenuChoiceException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PracticePanel extends BaseScreen {
    public PracticePanel(ApplicationController controller) {
        super(controller);
        setLayout(new BorderLayout(10, 10));
        setBackground(Theme.PALE_LIME);
        setBorder(new EmptyBorder(14, 16, 14, 16));

        JLabel header = new JLabel("Quick Practice");
        header.setFont(new Font(Theme.UI_FONT, Font.BOLD, 20));
        header.setForeground(Theme.TEXT_DARK);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(Theme.PALE_LIME);

        JLabel topicLabel = new JLabel("Choose a topic:");
        topicLabel.setFont(new Font(Theme.UI_FONT, Font.BOLD, 13));
        topicLabel.setForeground(Theme.TEXT_DARK);
        topicLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel topicGrid = new JPanel(new GridLayout(3, 2, 8, 8));
        topicGrid.setBackground(Theme.PALE_LIME);
        topicGrid.setAlignmentX(Component.LEFT_ALIGNMENT);

        List<String> topics = QuizQuestionBank.getQuickPracticeTopics();
        List<JButton> topicButtons = new ArrayList<>();
        String[] selectedTopic = {topics.get(0)};

        JLabel questionCountLabel = new JLabel();
        questionCountLabel.setFont(new Font(Theme.UI_FONT, Font.BOLD, 12));
        questionCountLabel.setForeground(Theme.TEXT_DARK);

        JTextField countField = new JTextField("5");
        Theme.styleTextField(countField);

        for (String topic : topics) {
            JButton topicButton = Theme.createPrimaryButton(topic);
            topicButtons.add(topicButton);
            if (topic.equals(selectedTopic[0])) {
                topicButton.setBackground(Theme.BRIGHT_LIME);
                topicButton.setForeground(Theme.TEXT_DARK);
            }
            topicButton.addActionListener(e -> {
                selectedTopic[0] = topic;
                for (JButton button : topicButtons) {
                    button.setBackground(Theme.DEEP_TEAL);
                    button.setForeground(Theme.WHITE);
                }
                topicButton.setBackground(Theme.BRIGHT_LIME);
                topicButton.setForeground(Theme.TEXT_DARK);
                questionCountLabel.setText(questionCountText(topic));
            });
            topicGrid.add(topicButton);
        }

        questionCountLabel.setText(questionCountText(selectedTopic[0]));
        JPanel optionsCard = Theme.createRoundedCard(Theme.SOFT_LIME_CARD, 12);
        optionsCard.setLayout(new GridLayout(2, 1, 8, 8));
        optionsCard.add(questionCountLabel);
        optionsCard.add(countField);
        optionsCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        center.add(topicLabel);
        center.add(Box.createVerticalStrut(8));
        center.add(topicGrid);
        center.add(Box.createVerticalStrut(12));
        center.add(optionsCard);

        JButton startBtn = Theme.createPrimaryButton("START PRACTICE");
        startBtn.setPreferredSize(new Dimension(0, 44));
        startBtn.addActionListener(e -> {
            try {
                int count = Integer.parseInt(countField.getText().trim());
                int maxQuestions = QuizQuestionBank.getQuickPracticeQuestionCount(selectedTopic[0]);
                if (count < 1 || count > maxQuestions) {
                    throw new InvalidMenuChoiceException(
                            "Enter a number from 1 to " + maxQuestions + ".");
                }
                controller.launchQuickPractice(selectedTopic[0], count);
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

    private String questionCountText(String topic) {
        return "Questions (1-" + QuizQuestionBank.getQuickPracticeQuestionCount(topic) + "):";
    }

    @Override
    public void refresh() {
        revalidate();
        repaint();
    }
}

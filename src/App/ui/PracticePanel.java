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

        JPanel practiceContent = new JPanel();
        practiceContent.setLayout(new BoxLayout(practiceContent, BoxLayout.Y_AXIS));
        practiceContent.setBackground(Theme.PALE_LIME);
        header.setHorizontalAlignment(SwingConstants.CENTER);
        header.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel topicLabel = new JLabel("Choose a topic:");
        topicLabel.setFont(new Font(Theme.UI_FONT, Font.BOLD, 13));
        topicLabel.setForeground(Theme.TEXT_DARK);
        topicLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topicLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel topicGrid = new JPanel(new GridLayout(3, 2, 15, 10));
        topicGrid.setBackground(Theme.PALE_LIME);
        topicGrid.setAlignmentX(Component.CENTER_ALIGNMENT);

        List<String> topics = QuizQuestionBank.getQuickPracticeTopics();
        List<JButton> topicButtons = new ArrayList<>();
        String[] selectedTopicName = {topics.get(0)};

        JLabel questionCountLabel = new JLabel();
        questionCountLabel.setFont(new Font(Theme.UI_FONT, Font.BOLD, 12));
        questionCountLabel.setForeground(Theme.TEXT_DARK);
        questionCountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        questionCountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField countField = new JTextField("5");
        Theme.styleTextField(countField);
        countField.setHorizontalAlignment(SwingConstants.CENTER);
        Dimension countFieldSize = countField.getPreferredSize();
        countField.setPreferredSize(new Dimension(countFieldSize.width, 38));
        countField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        countField.setAlignmentX(Component.CENTER_ALIGNMENT);

        for (String topic : topics) {
            JButton topicButton = Theme.createPrimaryButton(topic);
            topicButton.setPreferredSize(new Dimension(0, 42));
            topicButtons.add(topicButton);
            if (topic.equals(selectedTopicName[0])) {
                topicButton.setBackground(Theme.BRIGHT_LIME);
                topicButton.setForeground(Theme.TEXT_DARK);
            }
            topicButton.addActionListener(event -> {
                selectedTopicName[0] = topic;
                for (JButton button : topicButtons) {
                    button.setBackground(Theme.DEEP_TEAL);
                    button.setForeground(Theme.WHITE);
                }
                topicButton.setBackground(Theme.BRIGHT_LIME);
                topicButton.setForeground(Theme.TEXT_DARK);
                questionCountLabel.setText(formatQuestionCount(topic));
            });
            topicGrid.add(topicButton);
        }

        questionCountLabel.setText(formatQuestionCount(selectedTopicName[0]));
        int controlHorizontalInset = 12;
        JPanel optionsCard = Theme.createRoundedCard(
                Theme.SOFT_LIME_CARD, controlHorizontalInset);
        optionsCard.setLayout(new BoxLayout(optionsCard, BoxLayout.Y_AXIS));
        optionsCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        optionsCard.add(questionCountLabel);
        optionsCard.add(Box.createVerticalStrut(10));
        optionsCard.add(countField);
        optionsCard.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, optionsCard.getPreferredSize().height));

        JPanel topicGridContainer = new JPanel(new BorderLayout());
        topicGridContainer.setOpaque(false);
        topicGridContainer.setBorder(
                new EmptyBorder(0, controlHorizontalInset, 0, controlHorizontalInset));
        topicGridContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        topicGridContainer.add(topicGrid, BorderLayout.CENTER);
        topicGridContainer.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, topicGridContainer.getPreferredSize().height));

        JButton startPracticeButton = Theme.createPrimaryButton("START PRACTICE");
        startPracticeButton.setPreferredSize(new Dimension(0, 44));
        startPracticeButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        startPracticeButton.addActionListener(event -> {
            try {
                int questionCount = Integer.parseInt(countField.getText().trim());
                int maximumQuestionCount =
                        QuizQuestionBank.getQuickPracticeQuestionCount(selectedTopicName[0]);
                if (questionCount < 1 || questionCount > maximumQuestionCount) {
                    throw new InvalidMenuChoiceException(
                            "Enter a number from 1 to " + maximumQuestionCount + ".");
                }
                controller.launchQuickPractice(selectedTopicName[0], questionCount);
            } catch (NumberFormatException numberFormatException) {
                controller.showError("Please type a valid number!");
            } catch (InvalidMenuChoiceException invalidChoiceException) {
                controller.showError(invalidChoiceException.getMessage());
            }
        });

        JPanel startButtonContainer = new JPanel(new BorderLayout());
        startButtonContainer.setOpaque(false);
        startButtonContainer.setBorder(
                new EmptyBorder(0, controlHorizontalInset, 0, controlHorizontalInset));
        startButtonContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButtonContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        startButtonContainer.add(startPracticeButton, BorderLayout.CENTER);

        practiceContent.add(header);
        practiceContent.add(Box.createVerticalStrut(16));
        practiceContent.add(topicLabel);
        practiceContent.add(Box.createVerticalStrut(8));
        practiceContent.add(topicGridContainer);
        practiceContent.add(Box.createVerticalStrut(12));
        practiceContent.add(optionsCard);
        practiceContent.add(Box.createVerticalStrut(12));
        practiceContent.add(startButtonContainer);
        add(practiceContent, BorderLayout.NORTH);
    }

    private String formatQuestionCount(String topic) {
        return "Questions (1-" + QuizQuestionBank.getQuickPracticeQuestionCount(topic) + "):";
    }

    @Override
    public void refresh() {
        revalidate();
        repaint();
    }
}

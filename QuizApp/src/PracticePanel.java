import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PracticePanel extends JPanel {
    private String selectedTopic = "Java Basics";

    public PracticePanel(ApplicationController controller) {
        setLayout(new BorderLayout(10, 10));
        setBackground(Theme.BG_CREAM);
        setBorder(new EmptyBorder(14, 16, 14, 16));

        JLabel header = new JLabel("Practice Setup");
        header.setFont(new Font("SansSerif", Font.BOLD, 20));
        header.setForeground(Theme.TEXT_DARK);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(Theme.BG_CREAM);

        JPanel topicGrid = new JPanel(new GridLayout(3, 2, 8, 8));
        topicGrid.setBackground(Theme.BG_CREAM);
        String[] topics = {"Java Basics", "Conditionals", "Loops", "Methods", "Arrays", "OOP"};
        JButton[] buttons = new JButton[topics.length];

        for (int i = 0; i < topics.length; i++) {
            String topic = topics[i];
            JButton btn = Theme.createRedButton(topic);
            buttons[i] = btn;
            if (i == 0) { btn.setBackground(Theme.WHITE); btn.setForeground(Theme.TEXT_DARK); }
            btn.addActionListener(e -> {
                selectedTopic = topic;
                for (JButton b : buttons) { b.setBackground(Theme.CARD_RED); b.setForeground(Theme.WHITE); }
                btn.setBackground(Theme.WHITE); btn.setForeground(Theme.TEXT_DARK);
            });
            topicGrid.add(btn);
        }

        JPanel optionsCard = Theme.createRoundedCard(Theme.BG_YELLOW, 12);
        optionsCard.setLayout(new GridLayout(2, 2, 8, 8));

        JComboBox<String> diffBox = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
        JTextField countField = new JTextField("10");

        JLabel dLabel = new JLabel("Difficulty:");
        dLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        JLabel qLabel = new JLabel("Questions (1-50):");
        qLabel.setFont(new Font("SansSerif", Font.BOLD, 12));

        optionsCard.add(dLabel); optionsCard.add(diffBox);
        optionsCard.add(qLabel); optionsCard.add(countField);

        center.add(topicGrid);
        center.add(Box.createVerticalStrut(12));
        center.add(optionsCard);

        JButton startBtn = Theme.createRedButton("START PRACTICE");
        startBtn.setPreferredSize(new Dimension(0, 44));
        startBtn.addActionListener(e -> {
            try {
                int count = Integer.parseInt(countField.getText().trim());
                if (count < 1 || count > 50) throw new InvalidMenuChoiceException("Enter 1 to 50 questions.");
                controller.showAlert("Starting " + selectedTopic + "\nDifficulty: " + diffBox.getSelectedItem() + "\nQuestions: " + count);
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
}

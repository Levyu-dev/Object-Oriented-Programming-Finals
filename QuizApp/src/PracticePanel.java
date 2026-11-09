import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PracticePanel extends JPanel {
    private String selectedTopic = "Java Basics";

    public PracticePanel(ApplicationController controller) {
        setLayout(new BorderLayout(15, 15));
        setBackground(Theme.BG_YELLOW);
        setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel header = new JLabel("Practice - Choose a Topic & Settings");
        header.setFont(new Font("SansSerif", Font.BOLD, 24));
        header.setForeground(Theme.TEXT_DARK);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(Theme.BG_YELLOW);

        JPanel topicGrid = new JPanel(new GridLayout(2, 3, 12, 12));
        topicGrid.setBackground(Theme.BG_YELLOW);
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

        JPanel options = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 15));
        options.setBackground(Theme.BG_YELLOW);
        JComboBox<String> diffBox = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
        JTextField countField = new JTextField("10", 4);
        options.add(new JLabel("Difficulty:")); options.add(diffBox);
        options.add(new JLabel("Questions:")); options.add(countField);

        center.add(topicGrid);
        center.add(options);

        JButton startBtn = Theme.createRedButton("START PRACTICE");
        startBtn.setPreferredSize(new Dimension(0, 48));
        startBtn.addActionListener(e -> {
            try {
                int count = Integer.parseInt(countField.getText().trim());
                if (count < 1 || count > 50) throw new InvalidMenuChoiceException("Enter 1 to 50 questions.");
                controller.showAlert("Starting " + selectedTopic + " (" + diffBox.getSelectedItem() + ", " + count + " questions)");
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

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ApplicationController extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainContainer;

    private String selectedTopic = "Java Basics";
    private String selectedDifficulty = "Easy";
    private String selectedType = "Mixed Questions";

    public ApplicationController() {
        setTitle("Learn to Code - OOP Project");
        setSize(900, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        mainContainer.add(createWelcomeScreen(), "WELCOME");
        mainContainer.add(createDashboardScreen(), "DASHBOARD");
        mainContainer.add(createPracticeScreen(), "PRACTICE");
        mainContainer.add(createStudyBuddyScreen(), "STUDY_BUDDY");

        add(mainContainer);
    }

    public void start() {
        setVisible(true);
        showScreen("WELCOME");
    }

    public void showScreen(String screenName) {
        cardLayout.show(mainContainer, screenName);
    }

    private JPanel createWelcomeScreen() {
        JPanel panel = new JPanel();
        panel.setBackground(Theme.BG_YELLOW);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(120, 50, 120, 50));

        JPanel logoBox = new JPanel();
        logoBox.setBackground(Theme.CARD_RED);
        logoBox.setMaximumSize(new Dimension(90, 90));
        logoBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Learn to Code.");
        title.setFont(new Font("SansSerif", Font.BOLD, 38));
        title.setForeground(Theme.TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Your coding journey starts here");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 18));
        subtitle.setForeground(Theme.TEXT_DARK);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton startBtn = Theme.createRedButton("GET STARTED");
        startBtn.setMaximumSize(new Dimension(240, 50));
        startBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        startBtn.addActionListener(e -> showScreen("DASHBOARD"));

        panel.add(logoBox);
        panel.add(Box.createVerticalStrut(25));
        panel.add(title);
        panel.add(Box.createVerticalStrut(10));
        panel.add(subtitle);
        panel.add(Box.createVerticalStrut(35));
        panel.add(startBtn);

        return panel;
    }

    private JPanel createDashboardScreen() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(Theme.BG_YELLOW);
        panel.setBorder(new EmptyBorder(25, 35, 25, 35));

        JPanel topSection = new JPanel();
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.setBackground(Theme.BG_YELLOW);

        JLabel welcomeLabel = new JLabel("Welcome! @user");
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        welcomeLabel.setForeground(Theme.TEXT_DARK);

        JLabel subLabel = new JLabel("Ready to sharpen your coding skills?");
        subLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        subLabel.setForeground(Theme.TEXT_DARK);

        JPanel statsGrid = new JPanel(new GridLayout(1, 4, 15, 0));
        statsGrid.setBackground(Theme.BG_YELLOW);
        statsGrid.setBorder(new EmptyBorder(15, 0, 15, 0));
        statsGrid.add(createStatCard("Level 5", "1,200 XP"));
        statsGrid.add(createStatCard("100%", "Accuracy"));
        statsGrid.add(createStatCard("100%", "Quizzes"));
        statsGrid.add(createStatCard("100%", "Day Streak"));

        topSection.add(welcomeLabel);
        topSection.add(subLabel);
        topSection.add(statsGrid);

        JPanel quickAccessGrid = new JPanel(new GridLayout(2, 2, 20, 20));
        quickAccessGrid.setBackground(Theme.BG_YELLOW);

        JButton practiceBtn = Theme.createRedButton("<html><center><h2>Practice</h2>Learn at your own pace</center></html>");
        practiceBtn.addActionListener(e -> showScreen("PRACTICE"));

        JButton timedBtn = Theme.createRedButton("<html><center><h2>Timed Quiz</h2>Challenge yourself</center></html>");
        timedBtn.addActionListener(e -> launchPlaceholderMode("Timed Quiz"));

        JButton bossBtn = Theme.createRedButton("<html><center><h2>Final Boss</h2>Test your knowledge</center></html>");
        bossBtn.addActionListener(e -> launchPlaceholderMode("Final Boss Challenge"));

        JButton partyBtn = Theme.createRedButton("<html><center><h2>Party Mode</h2>Play with friends</center></html>");
        partyBtn.addActionListener(e -> launchPlaceholderMode("Party Mode"));

        quickAccessGrid.add(practiceBtn);
        quickAccessGrid.add(timedBtn);
        quickAccessGrid.add(bossBtn);
        quickAccessGrid.add(partyBtn);

        JButton studyBuddyBanner = Theme.createWhiteButton("StudyBuddy: \"Every bug you fix makes you a better programmer.\" (Click to open)");
        studyBuddyBanner.setPreferredSize(new Dimension(0, 50));
        studyBuddyBanner.addActionListener(e -> showScreen("STUDY_BUDDY"));

        panel.add(topSection, BorderLayout.NORTH);
        panel.add(quickAccessGrid, BorderLayout.CENTER);
        panel.add(studyBuddyBanner, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createStatCard(String bigText, String smallText) {
        JPanel card = new JPanel();
        card.setBackground(Theme.CARD_RED);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel big = new JLabel(bigText);
        big.setFont(new Font("SansSerif", Font.BOLD, 22));
        big.setForeground(Theme.WHITE);
        big.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel small = new JLabel(smallText);
        small.setFont(new Font("SansSerif", Font.PLAIN, 13));
        small.setForeground(Theme.WHITE);
        small.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(big);
        card.add(small);
        return card;
    }
    private JPanel createPracticeScreen() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(Theme.BG_YELLOW);
        panel.setBorder(new EmptyBorder(20, 35, 20, 35));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Theme.BG_YELLOW);
        JLabel header = new JLabel("Practice - Choose a Topic & Settings");
        header.setFont(new Font("SansSerif", Font.BOLD, 24));
        header.setForeground(Theme.TEXT_DARK);

        JButton backBtn = Theme.createWhiteButton("Back");
        backBtn.addActionListener(e -> showScreen("DASHBOARD"));
        topBar.add(header, BorderLayout.WEST);
        topBar.add(backBtn, BorderLayout.EAST);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Theme.BG_YELLOW);

        JPanel topicGrid = new JPanel(new GridLayout(2, 3, 12, 12));
        topicGrid.setBackground(Theme.BG_YELLOW);
        String[] topics = {"Java Basics", "Conditionals", "Loops", "Methods", "Arrays", "OOP"};
        JLabel statusLabel = new JLabel("Selected: Java Basics | Easy | Mixed Questions");
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        statusLabel.setForeground(Theme.TEXT_DARK);

        for (String topic : topics) {
            JButton btn = Theme.createRedButton(topic);
            btn.addActionListener(e -> {
                selectedTopic = topic;
                statusLabel.setText("Selected: " + selectedTopic + " | " + selectedDifficulty + " | " + selectedType);
            });
            topicGrid.add(btn);
        }

        JPanel optionsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        optionsRow.setBackground(Theme.BG_YELLOW);

        String[] difficulties = {"Easy", "Medium", "Hard"};
        JComboBox<String> diffBox = new JComboBox<>(difficulties);
        diffBox.addActionListener(e -> {
            selectedDifficulty = (String) diffBox.getSelectedItem();
            statusLabel.setText("Selected: " + selectedTopic + " | " + selectedDifficulty + " | " + selectedType);
        });

        String[] qTypes = {"Mixed Questions", "Multiple Choice", "Code Completion", "Output Prediction", "Debugging"};
        JComboBox<String> typeBox = new JComboBox<>(qTypes);
        typeBox.addActionListener(e -> {
            selectedType = (String) typeBox.getSelectedItem();
            statusLabel.setText("Selected: " + selectedTopic + " | " + selectedDifficulty + " | " + selectedType);
        });

        JTextField numQuestionsField = new JTextField("10", 5);

        optionsRow.add(new JLabel("Difficulty:"));
        optionsRow.add(diffBox);
        optionsRow.add(new JLabel("Type:"));
        optionsRow.add(typeBox);
        optionsRow.add(new JLabel("Questions:"));
        optionsRow.add(numQuestionsField);

        centerPanel.add(topicGrid);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(optionsRow);
        centerPanel.add(statusLabel);

        JButton startPracticeBtn = Theme.createRedButton("START PRACTICE");
        startPracticeBtn.setPreferredSize(new Dimension(0, 50));
        startPracticeBtn.addActionListener(e -> {
            try {
                int count = Integer.parseInt(numQuestionsField.getText().trim());
                if (count < 1 || count > 50) {
                    throw new InvalidMenuChoiceException("Please enter between 1 and 50 questions.");
                }
                JOptionPane.showMessageDialog(this,
                    "Launching Quiz!\nTopic: " + selectedTopic +
                    "\nDifficulty: " + selectedDifficulty +
                    "\nType: " + selectedType +
                    "\nQuestions: " + count,
                    "Starting Practice", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please type a valid number!", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (InvalidMenuChoiceException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid Range", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(topBar, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(startPracticeBtn, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createStudyBuddyScreen() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(Theme.BG_YELLOW);
        panel.setBorder(new EmptyBorder(25, 35, 25, 35));

        JLabel title = new JLabel("Study Buddy");
        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        title.setForeground(Theme.TEXT_DARK);

        JTextArea tipArea = new JTextArea(
            "Hi @user! Here are quick OOP tips:\n\n" +
            "1. Encapsulation: Keep fields private and use getters/setters.\n" +
            "2. Inheritance: Use 'extends' so subclasses reuse parent code.\n" +
            "3. Polymorphism: Override methods so objects behave in their own way."
        );
        tipArea.setFont(new Font("SansSerif", Font.PLAIN, 18));
        tipArea.setEditable(false);
        tipArea.setBackground(Theme.WHITE);
        tipArea.setBorder(new EmptyBorder(20, 20, 20, 20));

        JButton backBtn = Theme.createRedButton("Back to Dashboard");
        backBtn.setPreferredSize(new Dimension(0, 45));
        backBtn.addActionListener(e -> showScreen("DASHBOARD"));

        panel.add(title, BorderLayout.NORTH);
        panel.add(tipArea, BorderLayout.CENTER);
        panel.add(backBtn, BorderLayout.SOUTH);

        return panel;
    }

    private void launchPlaceholderMode(String modeName) {
        JOptionPane.showMessageDialog(this,
            "Connecting to " + modeName + "...\n(Ready for Jumana's code!)",
            "Mode Selected", JOptionPane.INFORMATION_MESSAGE);
    }
}

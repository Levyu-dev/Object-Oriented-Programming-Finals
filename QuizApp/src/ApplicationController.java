import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class ApplicationController extends JFrame {
    private CardLayout rootLayout;
    private JPanel rootContainer;

    private CardLayout contentLayout;
    private JPanel contentContainer;

    private String selectedTopic = "Java Basics";
    private String selectedDifficulty = "Easy";
    private String selectedType = "Mixed Questions";

    public ApplicationController() {
        setTitle("Learn to Code - OOP Project");
        setSize(980, 660);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        rootLayout = new CardLayout();
        rootContainer = new JPanel(rootLayout);

        // 1. Full-screen Welcome page (no sidebar yet)
        rootContainer.add(createWelcomeScreen(), "WELCOME");

        // 2. Main App shell (Left Circular Sidebar + Right Screen Area)
        JPanel appShell = new JPanel(new BorderLayout());
        appShell.setBackground(Theme.WHITE);

        SidebarNav sidebar = new SidebarNav(this);
        contentLayout = new CardLayout();
        contentContainer = new JPanel(contentLayout);

        contentContainer.add(createDashboardScreen(), "DASHBOARD");
        contentContainer.add(createPracticeScreen(), "PRACTICE");
        contentContainer.add(createGameModeScreen(), "GAME_MODE");
        contentContainer.add(createStudyBuddyScreen(), "STUDY_BUDDY");

        appShell.add(sidebar, BorderLayout.WEST);
        appShell.add(contentContainer, BorderLayout.CENTER);

        rootContainer.add(appShell, "MAIN_APP");
        add(rootContainer);
    }

    public void start() {
        setVisible(true);
        showRootScreen("WELCOME");
    }

    public void showRootScreen(String name) {
        rootLayout.show(rootContainer, name);
    }

    public void showAppScreen(String name) {
        rootLayout.show(rootContainer, "MAIN_APP");
        contentLayout.show(contentContainer, name);
    }

    // --- SCREEN 1: GET STARTED ---
    private JPanel createWelcomeScreen() {
        JPanel panel = new JPanel();
        panel.setBackground(Theme.BG_YELLOW);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(130, 50, 130, 50));

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
        startBtn.addActionListener(e -> showAppScreen("DASHBOARD"));

        panel.add(logoBox);
        panel.add(Box.createVerticalStrut(25));
        panel.add(title);
        panel.add(Box.createVerticalStrut(10));
        panel.add(subtitle);
        panel.add(Box.createVerticalStrut(35));
        panel.add(startBtn);

        return panel;
    }

    // --- SCREEN 2: DASHBOARD WITH XP BAR & PROGRESS BAR ---
    private JPanel createDashboardScreen() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(Theme.WHITE);
        panel.setBorder(new EmptyBorder(20, 28, 20, 28));

        // Top Section: Header + Yellow Box with XP Bar & 3 Stat Cards
        JPanel topWrapper = new JPanel();
        topWrapper.setLayout(new BoxLayout(topWrapper, BoxLayout.Y_AXIS));
        topWrapper.setBackground(Theme.WHITE);

        JLabel welcomeLabel = new JLabel("Welcome! @user");
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        welcomeLabel.setForeground(Theme.TEXT_DARK);

        JLabel subLabel = new JLabel("Ready to sharpen your coding skills?");
        subLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subLabel.setForeground(Theme.TEXT_DARK);

        // Yellow Container matching Page 3
        JPanel yellowCard = new JPanel();
        yellowCard.setLayout(new BoxLayout(yellowCard, BoxLayout.Y_AXIS));
        yellowCard.setBackground(Theme.BG_YELLOW);
        yellowCard.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Red XP Bar Box
        JPanel xpBox = new JPanel(new BorderLayout(8, 8));
        xpBox.setBackground(Theme.CARD_RED);
        xpBox.setBorder(new EmptyBorder(12, 15, 12, 15));

        JLabel xpTop = new JLabel("Your current level: 1,200 XP");
        xpTop.setForeground(Theme.WHITE);
        xpTop.setFont(new Font("SansSerif", Font.BOLD, 15));

        JProgressBar xpBar = Theme.createProgressBar(1200, 1450, Theme.WHITE, new Color(140, 35, 12));

        JLabel xpBottom = new JLabel("250 XP until Level 6", SwingConstants.RIGHT);
        xpBottom.setForeground(Theme.WHITE);
        xpBottom.setFont(new Font("SansSerif", Font.PLAIN, 12));

        xpBox.add(xpTop, BorderLayout.NORTH);
        xpBox.add(xpBar, BorderLayout.CENTER);
        xpBox.add(xpBottom, BorderLayout.SOUTH);

        // 3 Stat Cards (Accuracy, Quizzes, Day Streak)
        JPanel statsRow = new JPanel(new GridLayout(1, 3, 12, 0));
        statsRow.setBackground(Theme.BG_YELLOW);
        statsRow.setBorder(new EmptyBorder(12, 0, 0, 0));
        statsRow.add(createStatCard("100%", "Accuracy"));
        statsRow.add(createStatCard("100%", "Quizzes"));
        statsRow.add(createStatCard("100%", "Day Streak"));

        yellowCard.add(xpBox);
        yellowCard.add(statsRow);

        // Continue Learning Progress Box (Matches Pages 3 & 4)
        JPanel continueBox = new JPanel(new BorderLayout(6, 6));
        continueBox.setBackground(Theme.WHITE);
        continueBox.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Theme.CARD_RED, 2, true),
            new EmptyBorder(10, 14, 10, 14)
        ));

        JLabel continueTitle = new JLabel("Continue Learning: Object-Oriented Programming");
        continueTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        continueTitle.setForeground(Theme.TEXT_DARK);

        JProgressBar courseBar = Theme.createProgressBar(6, 10, Theme.CARD_RED, Theme.BG_YELLOW);

        JLabel progressCount = new JLabel("6 of 10 questions completed", SwingConstants.RIGHT);
        progressCount.setFont(new Font("SansSerif", Font.PLAIN, 12));
        progressCount.setForeground(Theme.TEXT_DARK);

        continueBox.add(continueTitle, BorderLayout.NORTH);
        continueBox.add(courseBar, BorderLayout.CENTER);
        continueBox.add(progressCount, BorderLayout.SOUTH);

        topWrapper.add(welcomeLabel);
        topWrapper.add(subLabel);
        topWrapper.add(Box.createVerticalStrut(10));
        topWrapper.add(yellowCard);
        topWrapper.add(Box.createVerticalStrut(10));
        topWrapper.add(continueBox);
        // Center Quick Access 2x2 Grid (Matches Page 4)
        JPanel quickGrid = new JPanel(new GridLayout(2, 2, 15, 15));
        quickGrid.setBackground(Theme.WHITE);
        quickGrid.setBorder(new EmptyBorder(10, 0, 10, 0));

        JButton practiceBtn = Theme.createRedButton("<html><center><b>Practice</b><br><small>Learn at your own pace</small></center></html>");
        practiceBtn.addActionListener(e -> showAppScreen("PRACTICE"));

        JButton timedBtn = Theme.createRedButton("<html><center><b>Timed Quiz</b><br><small>Challenge yourself</small></center></html>");
        timedBtn.addActionListener(e -> launchPlaceholderMode("Timed Quiz"));

        JButton bossBtn = Theme.createRedButton("<html><center><b>Final Boss</b><br><small>Test your knowledge</small></center></html>");
        bossBtn.addActionListener(e -> launchPlaceholderMode("Final Boss Challenge"));

        JButton partyBtn = Theme.createRedButton("<html><center><b>Party Mode</b><br><small>Play with friends</small></center></html>");
        partyBtn.addActionListener(e -> launchPlaceholderMode("Party Mode"));

        quickGrid.add(practiceBtn);
        quickGrid.add(timedBtn);
        quickGrid.add(bossBtn);
        quickGrid.add(partyBtn);

        // Bottom StudyBuddy Quote Box (Matches Page 4)
        JButton buddyBanner = Theme.createWhiteButton("StudyBuddy says: \"Every bug you fix makes you a better programmer. Keep going!\"");
        buddyBanner.setBorder(new LineBorder(Theme.CARD_RED, 2, true));
        buddyBanner.setPreferredSize(new Dimension(0, 42));
        buddyBanner.addActionListener(e -> showAppScreen("STUDY_BUDDY"));

        panel.add(topWrapper, BorderLayout.NORTH);
        panel.add(quickGrid, BorderLayout.CENTER);
        panel.add(buddyBanner, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createStatCard(String bigText, String smallText) {
        JPanel card = new JPanel();
        card.setBackground(Theme.CARD_RED);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(12, 10, 12, 10));

        JLabel big = new JLabel(bigText);
        big.setFont(new Font("SansSerif", Font.BOLD, 20));
        big.setForeground(Theme.WHITE);
        big.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel small = new JLabel(smallText);
        small.setFont(new Font("SansSerif", Font.PLAIN, 12));
        small.setForeground(Theme.WHITE);
        small.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(big);
        card.add(small);
        return card;
    }

    // --- SCREEN 3: PRACTICE SETUP (Matches Pages 5 & 6) ---
    private JPanel createPracticeScreen() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(Theme.BG_YELLOW);
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel header = new JLabel("Practice - Choose a Topic & Settings");
        header.setFont(new Font("SansSerif", Font.BOLD, 24));
        header.setForeground(Theme.TEXT_DARK);

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

        JComboBox<String> diffBox = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
        diffBox.addActionListener(e -> {
            selectedDifficulty = (String) diffBox.getSelectedItem();
            statusLabel.setText("Selected: " + selectedTopic + " | " + selectedDifficulty + " | " + selectedType);
        });

        JComboBox<String> typeBox = new JComboBox<>(new String[]{"Mixed Questions", "Multiple Choice", "Code Completion", "Output Prediction", "Debugging"});
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

        panel.add(header, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(startPracticeBtn, BorderLayout.SOUTH);

        return panel;
    }

    // --- SCREEN 4: GAME MODE (Matches Page 7) ---
    private JPanel createGameModeScreen() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(Theme.WHITE);
        panel.setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel title = new JLabel("Game Mode - Try out different modes to level up!", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(Theme.TEXT_DARK);

        JPanel cards = new JPanel(new GridLayout(1, 3, 20, 20));
        cards.setBackground(Theme.BG_YELLOW);
        cards.setBorder(new EmptyBorder(25, 25, 25, 25));

        JButton m1 = Theme.createRedButton("Timed Quiz");
        m1.addActionListener(e -> launchPlaceholderMode("Timed Quiz"));
        JButton m2 = Theme.createRedButton("Final Boss");
        m2.addActionListener(e -> launchPlaceholderMode("Final Boss"));
        JButton m3 = Theme.createRedButton("Party Mode");
        m3.addActionListener(e -> launchPlaceholderMode("Party Mode"));

        cards.add(m1);
        cards.add(m2);
        cards.add(m3);

        panel.add(title, BorderLayout.NORTH);
        panel.add(cards, BorderLayout.CENTER);
        return panel;
    }

    // --- SCREEN 5: STUDY BUDDY (Matches Page 9) ---
    private JPanel createStudyBuddyScreen() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(Theme.BG_YELLOW);
        panel.setBorder(new EmptyBorder(25, 30, 25, 30));

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

        panel.add(title, BorderLayout.NORTH);
        panel.add(tipArea, BorderLayout.CENTER);
        return panel;
    }

    private void launchPlaceholderMode(String modeName) {
        JOptionPane.showMessageDialog(this,
            "Connecting to " + modeName + "...\n(Ready for Jumana's code!)",
            "Mode Selected", JOptionPane.INFORMATION_MESSAGE);
    }
}

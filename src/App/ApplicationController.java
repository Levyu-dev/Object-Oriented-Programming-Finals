package App;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ApplicationController extends JFrame {
    private CardLayout rootLayout = new CardLayout();
    private JPanel rootContainer = new JPanel(rootLayout);

    private CardLayout studentLayout = new CardLayout();
    private JPanel studentContent = new JPanel(studentLayout);

    private CardLayout teacherLayout = new CardLayout();
    private JPanel teacherContent = new JPanel(teacherLayout);

    private UserSession session = new UserSession();
    private DashboardPanel studentDash;
    private ClassroomPanel studentClass;
    private QuizPlayPanel quizPlayPanel;
    private TeacherDashboardPanel teacherDash;
    private ClassroomPanel teacherClass;

    public ApplicationController() {
        setTitle("Learn to Code - Mobile App");
        setSize(430, 780);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        rootContainer.add(createWelcomeScreen(), "WELCOME");

        studentDash = new DashboardPanel(this);
        studentClass = new ClassroomPanel(this, false);
        quizPlayPanel = new QuizPlayPanel(this);

        studentContent.add(studentDash, "STUDENT_DASH");
        studentContent.add(new PracticePanel(this), "PRACTICE");
        studentContent.add(studentClass, "STUDENT_CLASS");
        studentContent.add(quizPlayPanel, "QUIZ_PLAY");
        rootContainer.add(buildMobileShell(new SidebarNav(this, false), studentContent), "STUDENT_APP");

        teacherDash = new TeacherDashboardPanel(this);
        teacherClass = new ClassroomPanel(this, true);
        teacherContent.add(teacherDash, "TEACHER_DASH");
        teacherContent.add(teacherClass, "TEACHER_CLASS");
        rootContainer.add(buildMobileShell(new SidebarNav(this, true), teacherContent), "TEACHER_APP");

        add(rootContainer);
    }

    private JPanel buildMobileShell(SidebarNav sidebar, JPanel contentArea) {
        JPanel shell = new JPanel(new BorderLayout());
        shell.setBackground(Theme.PALE_LIME);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Theme.DEEP_TEAL);
        topBar.setBorder(new EmptyBorder(8, 12, 8, 14));

        JButton toggleBtn = Theme.createWhiteButton("Menu");
        toggleBtn.setPreferredSize(new Dimension(65, 32));
        toggleBtn.addActionListener(e -> {
            sidebar.setVisible(!sidebar.isVisible());
            shell.revalidate();
            shell.repaint();
        });

        JLabel appTitle = new JLabel("Learn to Code", SwingConstants.RIGHT);
        appTitle.setFont(new Font(Theme.UI_FONT, Font.BOLD, 14));
        appTitle.setForeground(Theme.BRIGHT_LIME);

        topBar.add(toggleBtn, BorderLayout.WEST);
        topBar.add(appTitle, BorderLayout.EAST);

        shell.add(topBar, BorderLayout.NORTH);
        shell.add(sidebar, BorderLayout.WEST);
        shell.add(contentArea, BorderLayout.CENTER);
        return shell;
    }

    public void start() {
        setVisible(true);
        logout();
    }

    public void logout() {
        rootLayout.show(rootContainer, "WELCOME");
    }

    public UserSession getSession() {
        return session;
    }

    public void launchQuizSession(int questionCount) {
        quizPlayPanel.startQuiz(questionCount);
        showStudentScreen("QUIZ_PLAY");
    }

    public void launchTeacherQuiz(UserSession.TeacherQuiz quiz) {
        quizPlayPanel.startTeacherQuiz(quiz);
        showStudentScreen("QUIZ_PLAY");
    }

    public void showStudentScreen(String name) {
        studentDash.refresh();
        studentClass.refresh();
        rootLayout.show(rootContainer, "STUDENT_APP");
        studentLayout.show(studentContent, name);
    }

    public void showTeacherScreen(String name) {
        teacherDash.refresh();
        teacherClass.refresh();
        rootLayout.show(rootContainer, "TEACHER_APP");
        teacherLayout.show(teacherContent, name);
    }

    private JPanel createWelcomeScreen() {
        JPanel panel = new JPanel();
        panel.setBackground(Theme.PALE_LIME);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(75, 35, 75, 35));

        JPanel logoBox = Theme.createRoundedCard(Theme.DEEP_TEAL, 0);
        logoBox.setPreferredSize(new Dimension(75, 75));
        logoBox.setMaximumSize(new Dimension(75, 75));
        logoBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Learn to Code.");
        title.setFont(new Font(Theme.UI_FONT, Font.BOLD, 34));
        title.setForeground(Theme.TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Your coding journey starts here");
        subtitle.setFont(new Font(Theme.UI_FONT, Font.PLAIN, 14));
        subtitle.setForeground(Theme.TEXT_DARK);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel form = new JPanel(new GridLayout(4, 1, 0, 6));
        form.setBackground(Theme.PALE_LIME);
        form.setMaximumSize(new Dimension(280, 130));

        JLabel nameLbl = new JLabel("Your Name / Username:");
        nameLbl.setForeground(Theme.TEXT_DARK);
        nameLbl.setFont(new Font(Theme.UI_FONT, Font.BOLD, 12));

        JLabel secLbl = new JLabel("Class Section Code:");
        secLbl.setForeground(Theme.TEXT_DARK);
        secLbl.setFont(new Font(Theme.UI_FONT, Font.BOLD, 12));

        JTextField nameField = new JTextField("Angel");
        JTextField sectionField = new JTextField("OOP101");
        Theme.styleTextField(nameField);
        Theme.styleTextField(sectionField);

        form.add(nameLbl);
        form.add(nameField);
        form.add(secLbl);
        form.add(sectionField);

        JPanel btnRow = new JPanel(new GridLayout(2, 1, 0, 12));
        btnRow.setBackground(Theme.PALE_LIME);
        btnRow.setMaximumSize(new Dimension(280, 105));

        JButton studentBtn = Theme.createPrimaryButton("ENTER AS STUDENT");
        studentBtn.addActionListener(e -> handleLogin(nameField.getText(), sectionField.getText(), false));

        JButton teacherBtn = Theme.createWhiteButton("ENTER AS TEACHER");
        teacherBtn.addActionListener(e -> handleLogin(nameField.getText(), sectionField.getText(), true));

        btnRow.add(studentBtn);
        btnRow.add(teacherBtn);

        panel.add(logoBox);
        panel.add(Box.createVerticalStrut(18));
        panel.add(title);
        panel.add(Box.createVerticalStrut(6));
        panel.add(subtitle);
        panel.add(Box.createVerticalStrut(22));
        panel.add(form);
        panel.add(Box.createVerticalStrut(22));
        panel.add(btnRow);
        return panel;
    }

    private void handleLogin(String rawName, String rawSection, boolean asTeacher) {
        try {
            String name = rawName.trim();
            String section = rawSection.trim().toUpperCase();
            if (name.isEmpty() || section.isEmpty()) {
                throw new InvalidMenuChoiceException("Please enter both your Name and Class Section Code.");
            }
            if (asTeacher) {
                if (session.isTeacherRegistered(name)) {
                    String pin = JOptionPane.showInputDialog(this, "Welcome back Prof. " + name + "!\nEnter your PIN:", "Teacher Login", JOptionPane.QUESTION_MESSAGE);
                    if (pin == null) return;
                    if (!session.authenticateTeacher(name, pin, section)) {
                        throw new InvalidMenuChoiceException("Access Denied: Incorrect PIN.");
                    }
                } else {
                    String schoolCode = JOptionPane.showInputDialog(this, "New Instructor Setup!\nEnter School Faculty Code (Hint: 1234):", "Faculty Verification", JOptionPane.QUESTION_MESSAGE);
                    if (schoolCode == null) return;
                    if (!schoolCode.trim().equals("1234")) {
                        throw new InvalidMenuChoiceException("Access Denied: Invalid Faculty Code.");
                    }
                    String newPin = JOptionPane.showInputDialog(this, "Create your personal 4-digit PIN:", "Set Instructor PIN", JOptionPane.QUESTION_MESSAGE);
                    if (newPin == null || newPin.trim().length() < 4) {
                        throw new InvalidMenuChoiceException("PIN must be at least 4 characters.");
                    }
                    session.registerNewTeacher(name, newPin, section);
                    showAlert("Account created for Prof. " + name + " (Section " + section + ")!");
                }
                showTeacherScreen("TEACHER_DASH");
            } else {
                session.loginStudent(name, section);
                showStudentScreen("STUDENT_DASH");
            }
        } catch (InvalidMenuChoiceException ex) {
            showError(ex.getMessage());
        }
    }

    public void showAlert(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Learn to Code", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}

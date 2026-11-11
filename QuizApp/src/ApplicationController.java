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
        studentContent.add(studentDash, "STUDENT_DASH");
        studentContent.add(new PracticePanel(this), "PRACTICE");
        studentContent.add(studentClass, "STUDENT_CLASS");
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
        shell.setBackground(Theme.BG_CREAM);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Theme.BG_YELLOW);
        topBar.setBorder(new EmptyBorder(6, 10, 6, 12));

        JButton toggleBtn = Theme.createWhiteButton("Menu");
        toggleBtn.setPreferredSize(new Dimension(65, 32));
        toggleBtn.addActionListener(e -> {
            sidebar.setVisible(!sidebar.isVisible());
            shell.revalidate();
            shell.repaint();
        });

        JLabel appTitle = new JLabel("Learn to Code", SwingConstants.RIGHT);
        appTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        appTitle.setForeground(Theme.TEXT_DARK);

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
        panel.setBackground(Theme.BG_YELLOW);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(75, 35, 75, 35));

        // Temporary Rounded Red Logo Square (matches Page 1 of your slides)
        JPanel logoBox = Theme.createRoundedCard(Theme.CARD_RED, 0);
        logoBox.setPreferredSize(new Dimension(75, 75));
        logoBox.setMaximumSize(new Dimension(75, 75));
        logoBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Learn to Code.");
        title.setFont(new Font("SansSerif", Font.BOLD, 34));
        title.setForeground(Theme.TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Your coding journey starts here");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(Theme.TEXT_DARK);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel form = new JPanel(new GridLayout(4, 1, 0, 6));
        form.setBackground(Theme.BG_YELLOW);
        form.setMaximumSize(new Dimension(280, 130));

        JTextField nameField = new JTextField("Angel");
        JTextField sectionField = new JTextField("OOP101");

        form.add(new JLabel("Your Name / Username:"));
        form.add(nameField);
        form.add(new JLabel("Class Section Code:"));
        form.add(sectionField);

        JPanel btnRow = new JPanel(new GridLayout(2, 1, 0, 12));
        btnRow.setBackground(Theme.BG_YELLOW);
        btnRow.setMaximumSize(new Dimension(280, 105));

        JButton studentBtn = Theme.createRedButton("ENTER AS STUDENT");
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
                String pin = JOptionPane.showInputDialog(this, "Enter Instructor PIN (Hint: 1234):", "Teacher Verification", JOptionPane.QUESTION_MESSAGE);
                if (pin == null) return;
                if (!pin.trim().equals("1234")) {
                    throw new InvalidMenuChoiceException("Access Denied: Invalid Instructor PIN.");
                }
                session.loginTeacher(name, section);
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

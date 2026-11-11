import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TeacherDashboardPanel extends JPanel {
    private ApplicationController controller;
    private JLabel welcomeLabel;
    private JLabel studentCountLabel;
    private DefaultTableModel tableModel;

    public TeacherDashboardPanel(ApplicationController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBackground(Theme.BG_CREAM);
        setBorder(new EmptyBorder(14, 16, 14, 16));

        JPanel topBox = new JPanel();
        topBox.setLayout(new BoxLayout(topBox, BoxLayout.Y_AXIS));
        topBox.setBackground(Theme.BG_CREAM);

        welcomeLabel = new JLabel("Instructor Portal");
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        welcomeLabel.setForeground(Theme.TEXT_DARK);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel statsGrid = new JPanel(new GridLayout(1, 2, 10, 0));
        statsGrid.setBackground(Theme.BG_CREAM);
        statsGrid.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel countCard = createStatBox("0 Students", "Enrolled");
        studentCountLabel = (JLabel) countCard.getComponent(0);
        statsGrid.add(countCard);
        statsGrid.add(createStatBox("Active", "Gradebook"));

        topBox.add(welcomeLabel);
        topBox.add(Box.createVerticalStrut(10));
        topBox.add(statsGrid);

        JPanel gradebookPanel = Theme.createRoundedCard(Theme.BG_YELLOW, 12);
        gradebookPanel.setLayout(new BorderLayout(6, 6));

        JLabel tableTitle = new JLabel("Student Roster & XP");
        tableTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        tableTitle.setForeground(Theme.TEXT_DARK);

        tableModel = new DefaultTableModel(new String[]{"Student", "Lvl", "XP", "Done"}, 0);
        JTable studentTable = new JTable(tableModel);
        studentTable.setRowHeight(26);
        studentTable.setFont(new Font("SansSerif", Font.PLAIN, 12));
        studentTable.setEnabled(false);

        gradebookPanel.add(tableTitle, BorderLayout.NORTH);
        gradebookPanel.add(new JScrollPane(studentTable), BorderLayout.CENTER);

        JButton manageBtn = Theme.createRedButton("Post Class Assignments →");
        manageBtn.setPreferredSize(new Dimension(0, 42));
        manageBtn.addActionListener(e -> controller.showTeacherScreen("TEACHER_CLASS"));

        add(topBox, BorderLayout.NORTH);
        add(gradebookPanel, BorderLayout.CENTER);
        add(manageBtn, BorderLayout.SOUTH);
    }

    public void refresh() {
        UserSession session = controller.getSession();
        String sec = session.getCurrentSection();
        welcomeLabel.setText("Prof. " + session.getCurrentUserName() + " (" + sec + ")");

        List<UserSession.StudentRecord> students = session.getStudentsInSection(sec);
        studentCountLabel.setText(students.size() + " Students");

        tableModel.setRowCount(0);
        for (UserSession.StudentRecord s : students) {
            int lvl = (s.xp / 250) + 1;
            tableModel.addRow(new Object[]{"@" + s.name, "L" + lvl, s.xp, s.completedQuizzes});
        }
    }

    private JPanel createStatBox(String big, String small) {
        JPanel box = Theme.createRoundedCard(Theme.CARD_RED, 12);
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));

        JLabel l1 = new JLabel(big);
        l1.setFont(new Font("SansSerif", Font.BOLD, 16));
        l1.setForeground(Theme.WHITE);
        l1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel l2 = new JLabel(small);
        l2.setFont(new Font("SansSerif", Font.PLAIN, 11));
        l2.setForeground(Theme.WHITE);
        l2.setAlignmentX(Component.CENTER_ALIGNMENT);

        box.add(l1);
        box.add(l2);
        return box;
    }
}

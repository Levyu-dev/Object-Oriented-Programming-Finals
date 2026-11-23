package App;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TeacherDashboardPanel extends BaseScreen {
    private JLabel welcomeLabel;
    private JLabel studentCountLabel;
    private DefaultTableModel tableModel;

    public TeacherDashboardPanel(ApplicationController controller) {
        super(controller);
        setLayout(new BorderLayout(10, 10));
        setBackground(Theme.PALE_LIME);
        setBorder(new EmptyBorder(14, 16, 14, 16));

        JPanel topBox = new JPanel();
        topBox.setLayout(new BoxLayout(topBox, BoxLayout.Y_AXIS));
        topBox.setBackground(Theme.PALE_LIME);

        welcomeLabel = new JLabel("Instructor Portal");
        welcomeLabel.setFont(new Font(Theme.UI_FONT, Font.BOLD, 18));
        welcomeLabel.setForeground(Theme.TEXT_DARK);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel statsGrid = new JPanel(new GridLayout(1, 2, 10, 0));
        statsGrid.setBackground(Theme.PALE_LIME);
        statsGrid.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel countCard = createStatBox("0 Students", "Enrolled");
        studentCountLabel = (JLabel) countCard.getComponent(0);
        statsGrid.add(countCard);
        statsGrid.add(createStatBox("Active", "Gradebook"));

        topBox.add(welcomeLabel);
        topBox.add(Box.createVerticalStrut(10));
        topBox.add(statsGrid);

        JPanel gradebookPanel = Theme.createRoundedCard(Theme.SOFT_LIME_CARD, 12);
        gradebookPanel.setLayout(new BorderLayout(6, 6));

        JLabel tableTitle = new JLabel("Student Roster & XP");
        tableTitle.setFont(new Font(Theme.UI_FONT, Font.BOLD, 14));
        tableTitle.setForeground(Theme.TEXT_DARK);

        tableModel = new DefaultTableModel(0, 4);
        tableModel.setColumnIdentifiers(new Object[]{"Student", "Lvl", "XP", "Done"});
        JTable studentTable = new JTable(tableModel);
        studentTable.setRowHeight(26);
        studentTable.setFont(new Font(Theme.UI_FONT, Font.PLAIN, 13));
        studentTable.setBackground(Theme.INPUT_BACKGROUND);
        studentTable.setForeground(Theme.TEXT_DARK);
        studentTable.setGridColor(Theme.INPUT_BORDER);
        studentTable.setSelectionBackground(Theme.BRIGHT_LIME);
        studentTable.setSelectionForeground(Theme.TEXT_DARK);
        studentTable.getTableHeader().setFont(new Font(Theme.UI_FONT, Font.BOLD, 12));
        studentTable.getTableHeader().setBackground(Theme.DEEP_TEAL);
        studentTable.getTableHeader().setForeground(Theme.WHITE);
        studentTable.setEnabled(false);

        gradebookPanel.add(tableTitle, BorderLayout.NORTH);
        gradebookPanel.add(new JScrollPane(studentTable), BorderLayout.CENTER);

        JButton manageBtn = Theme.createPrimaryButton("Create Quiz for Students ->");
        manageBtn.setPreferredSize(new Dimension(0, 42));
        manageBtn.addActionListener(e -> controller.showTeacherScreen("TEACHER_CLASS"));

        add(topBox, BorderLayout.NORTH);
        add(gradebookPanel, BorderLayout.CENTER);
        add(manageBtn, BorderLayout.SOUTH);
    }

    @Override
    public void refresh() {
        UserSession session = controller.getSession();
        String sec = session.getCurrentSection();
        welcomeLabel.setText("Prof. " + session.getCurrentUserName() + " (" + sec + ")");

        List<UserSession.StudentRecord> students = session.getStudentsInSection(sec);
        studentCountLabel.setText(students.size() + " Students");

        tableModel.setRowCount(0);
        for (UserSession.StudentRecord s : students) {
            int lvl = (s.getXp() / 250) + 1;
            tableModel.addRow(new Object[]{"@" + s.getName(), "L" + lvl, s.getXp(), s.getCompletedQuizzes()});
        }
    }

    private JPanel createStatBox(String big, String small) {
        JPanel box = Theme.createRoundedCard(Theme.DEEP_TEAL, 12);
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));

        JLabel l1 = new JLabel(big);
        l1.setFont(new Font(Theme.UI_FONT, Font.BOLD, 16));
        l1.setForeground(Theme.WHITE);
        l1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel l2 = new JLabel(small);
        l2.setFont(new Font(Theme.UI_FONT, Font.PLAIN, 11));
        l2.setForeground(Theme.WHITE);
        l2.setAlignmentX(Component.CENTER_ALIGNMENT);

        box.add(l1);
        box.add(l2);
        return box;
    }
}


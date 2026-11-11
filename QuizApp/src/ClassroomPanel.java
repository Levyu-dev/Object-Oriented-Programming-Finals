import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ClassroomPanel extends JPanel {
    private ApplicationController controller;
    private boolean isTeacher;
    private JLabel titleLabel;
    private JList<String> assignmentList;
    private JTextArea leaderboardArea;

    public ClassroomPanel(ApplicationController controller, boolean isTeacher) {
        this.controller = controller;
        this.isTeacher = isTeacher;

        setLayout(new BorderLayout(10, 10));
        setBackground(Theme.BG_CREAM);
        setBorder(new EmptyBorder(14, 16, 14, 16));

        titleLabel = new JLabel("Classroom");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(Theme.TEXT_DARK);

        JPanel stack = new JPanel(new GridLayout(2, 1, 0, 12));
        stack.setBackground(Theme.BG_CREAM);

        JPanel topCard = Theme.createRoundedCard(Theme.BG_YELLOW, 12);
        topCard.setLayout(new BorderLayout(6, 6));

        JLabel actHeader = new JLabel("Section Activities");
        actHeader.setFont(new Font("SansSerif", Font.BOLD, 14));
        actHeader.setForeground(Theme.TEXT_DARK);
        topCard.add(actHeader, BorderLayout.NORTH);

        assignmentList = new JList<>();
        assignmentList.setFont(new Font("SansSerif", Font.PLAIN, 12));
        topCard.add(new JScrollPane(assignmentList), BorderLayout.CENTER);

        if (!isTeacher) {
            JButton startBtn = Theme.createRedButton("Complete Selected (+150 XP)");
            startBtn.setPreferredSize(new Dimension(0, 36));
            startBtn.addActionListener(e -> {
                if (assignmentList.getSelectedValue() == null) {
                    controller.showError("Tap an activity from the list first.");
                } else {
                    controller.getSession().addXP(150);
                    refresh();
                    controller.showAlert("Completed: " + assignmentList.getSelectedValue() + "\nEarned +150 XP!");
                }
            });
            topCard.add(startBtn, BorderLayout.SOUTH);
        }

        JPanel bottomCard = Theme.createRoundedCard(Theme.BG_YELLOW, 12);
        bottomCard.setLayout(new BorderLayout(6, 6));

        if (isTeacher) {
            JLabel formTitle = new JLabel("Post New Assignment");
            formTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
            formTitle.setForeground(Theme.TEXT_DARK);
            bottomCard.add(formTitle, BorderLayout.NORTH);

            JPanel form = new JPanel(new GridLayout(4, 1, 0, 4));
            form.setBackground(Theme.BG_YELLOW);
            JTextField nameField = new JTextField();
            JTextField qField = new JTextField("10");

            form.add(new JLabel("Activity Title:")); form.add(nameField);
            form.add(new JLabel("Question Count:")); form.add(qField);
            bottomCard.add(form, BorderLayout.CENTER);

            JButton postBtn = Theme.createRedButton("POST ASSIGNMENT");
            postBtn.setPreferredSize(new Dimension(0, 36));
            postBtn.addActionListener(e -> {
                try {
                    String name = nameField.getText().trim();
                    if (name.isEmpty()) throw new InvalidMenuChoiceException("Title cannot be empty.");
                    int q = Integer.parseInt(qField.getText().trim());
                    if (q < 1 || q > 50) throw new InvalidMenuChoiceException("Questions must be 1-50.");
                    String sec = controller.getSession().getCurrentSection();
                    controller.getSession().getAssignmentsForSection(sec).addElement(name + " (" + q + " Qs)");
                    nameField.setText("");
                    controller.showAlert("Posted to Section " + sec + "!");
                } catch (NumberFormatException ex) {
                    controller.showError("Enter a valid number.");
                } catch (InvalidMenuChoiceException ex) {
                    controller.showError(ex.getMessage());
                }
            });
            bottomCard.add(postBtn, BorderLayout.SOUTH);
        } else {
            JLabel boardTitle = new JLabel("Section Leaderboard");
            boardTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
            boardTitle.setForeground(Theme.TEXT_DARK);
            bottomCard.add(boardTitle, BorderLayout.NORTH);

            leaderboardArea = new JTextArea();
            leaderboardArea.setEditable(false);
            leaderboardArea.setFont(new Font("SansSerif", Font.BOLD, 13));
            leaderboardArea.setForeground(Theme.TEXT_DARK);
            leaderboardArea.setBorder(new EmptyBorder(8, 10, 8, 10));
            bottomCard.add(new JScrollPane(leaderboardArea), BorderLayout.CENTER);
        }

        stack.add(topCard);
        stack.add(bottomCard);
        add(titleLabel, BorderLayout.NORTH);
        add(stack, BorderLayout.CENTER);
    }

    public void refresh() {
        UserSession session = controller.getSession();
        String sec = session.getCurrentSection();
        titleLabel.setText((isTeacher ? "Manage Class" : "Classroom") + " — " + sec);
        assignmentList.setModel(session.getAssignmentsForSection(sec));

        if (!isTeacher && leaderboardArea != null) {
            StringBuilder sb = new StringBuilder();
            int rank = 1;
            for (UserSession.StudentRecord s : session.getStudentsInSection(sec)) {
                sb.append(rank++).append(". @").append(s.name).append(" — ").append(s.xp).append(" XP\n");
            }
            leaderboardArea.setText(sb.toString());
        }
    }
}

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ClassroomPanel extends JPanel {

    public ClassroomPanel(ApplicationController controller, boolean isTeacher) {
        setLayout(new BorderLayout(15, 15));
        setBackground(Theme.WHITE);
        setBorder(new EmptyBorder(20, 28, 20, 28));

        JLabel title = new JLabel(isTeacher ? "Teacher Portal — Section OOP101" : "Student Classroom — Section OOP101");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(Theme.TEXT_DARK);

        JPanel grid = new JPanel(new GridLayout(1, 2, 15, 0));
        grid.setBackground(Theme.WHITE);

        // Left Side: Shared Assignments List
        JPanel left = new JPanel(new BorderLayout(8, 8));
        left.setBackground(Theme.BG_YELLOW);
        left.setBorder(new EmptyBorder(15, 15, 15, 15));
        left.add(new JLabel("Class Activities:"), BorderLayout.NORTH);

        JList<String> list = new JList<>(controller.getAssignmentModel());
        left.add(new JScrollPane(list), BorderLayout.CENTER);

        if (!isTeacher) {
            JButton startBtn = Theme.createRedButton("Start Selected Activity");
            startBtn.addActionListener(e -> {
                if (list.getSelectedValue() == null) controller.showError("Select an activity first.");
                else controller.showAlert("Starting: " + list.getSelectedValue());
            });
            left.add(startBtn, BorderLayout.SOUTH);
        }

        // Right Side: Teacher Post Form OR Student Leaderboard
        JPanel right = new JPanel(new BorderLayout(8, 8));
        right.setBackground(Theme.BG_YELLOW);
        right.setBorder(new EmptyBorder(15, 15, 15, 15));

        if (isTeacher) {
            right.add(new JLabel("Post New Assignment:"), BorderLayout.NORTH);
            JPanel form = new JPanel();
            form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
            form.setBackground(Theme.BG_YELLOW);
            JTextField nameField = new JTextField();
            JTextField qField = new JTextField("10");
            JButton postBtn = Theme.createRedButton("POST ASSIGNMENT");

            postBtn.addActionListener(e -> {
                try {
                    String name = nameField.getText().trim();
                    if (name.isEmpty()) throw new InvalidMenuChoiceException("Title cannot be empty.");
                    int q = Integer.parseInt(qField.getText().trim());
                    if (q < 1 || q > 50) throw new InvalidMenuChoiceException("Questions must be 1-50.");
                    controller.getAssignmentModel().addElement(name + " (" + q + " Questions)");
                    nameField.setText("");
                    controller.showAlert("Assignment posted!");
                } catch (NumberFormatException ex) {
                    controller.showError("Enter a valid number.");
                } catch (InvalidMenuChoiceException ex) {
                    controller.showError(ex.getMessage());
                }
            });

            form.add(new JLabel("Title:")); form.add(nameField);
            form.add(Box.createVerticalStrut(10));
            form.add(new JLabel("Questions:")); form.add(qField);
            form.add(Box.createVerticalStrut(15));
            form.add(postBtn);
            right.add(form, BorderLayout.CENTER);
        } else {
            right.add(new JLabel("Class Leaderboard:"), BorderLayout.NORTH);
            JTextArea board = new JTextArea("1. @alex — 1,450 XP\n2. @user — 1,200 XP\n3. @jumana — 1,120 XP\n4. @iyce — 980 XP");
            board.setEditable(false);
            board.setFont(new Font("SansSerif", Font.BOLD, 15));
            right.add(board, BorderLayout.CENTER);
        }

        grid.add(left);
        grid.add(right);
        add(title, BorderLayout.NORTH);
        add(grid, BorderLayout.CENTER);
    }
}

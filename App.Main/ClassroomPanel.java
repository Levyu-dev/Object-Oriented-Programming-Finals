package App.main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ClassroomPanel extends BaseScreen {
    private boolean isTeacher;
    private JLabel titleLabel;
    private JList<String> assignmentList;
    private JTextArea leaderboardArea;

    public ClassroomPanel(ApplicationController controller, boolean isTeacher) {
        super(controller);
        this.isTeacher = isTeacher;

        setLayout(new BorderLayout(10, 10));
        setBackground(Theme.BG_CREAM);
        setBorder(new EmptyBorder(14, 16, 14, 16));

        titleLabel = new JLabel("Classroom");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(Theme.TEXT_DARK);

        JPanel stack = new JPanel(new GridLayout(2, 1, 0, 12));
        stack.setBackground(Theme.BG_CREAM);

        // Top Card: Section Activities
        JPanel topCard = Theme.createRoundedCard(Theme.BG_YELLOW, 12);
        topCard.setLayout(new BorderLayout(6, 6));

        JLabel actHeader = new JLabel("Section Activities");
        actHeader.setFont(new Font("SansSerif", Font.BOLD, 14));
        actHeader.setForeground(Theme.TEXT_DARK);
        topCard.add(actHeader, BorderLayout.NORTH);

        assignmentList = new JList<>();
        assignmentList.setFont(new Font("SansSerif", Font.PLAIN, 12));
        assignmentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        assignmentList.setBackground(Theme.WHITE);
        assignmentList.setForeground(Theme.TEXT_DARK);

        JScrollPane listScroll = new JScrollPane(assignmentList);
        listScroll.setBorder(BorderFactory.createLineBorder(new Color(225, 200, 170)));
        topCard.add(listScroll, BorderLayout.CENTER);

        if (!isTeacher) {
            JButton startBtn = Theme.createRedButton("Start Selected Activity");
            startBtn.setPreferredSize(new Dimension(0, 38));
            startBtn.addActionListener(e -> {
                String selected = assignmentList.getSelectedValue();
                if (selected == null) {
                    controller.showError("Tap an activity from the list first.");
                } else {
                    int count = extractQuestionCount(selected);
                    controller.launchQuizSession(count);
                }
            });
            topCard.add(startBtn, BorderLayout.SOUTH);
        }

        // Bottom Card: Teacher Assignment Form OR Student Leaderboard
        JPanel bottomCard = Theme.createRoundedCard(Theme.BG_YELLOW, 12);
        bottomCard.setLayout(new BorderLayout(6, 6));

        if (isTeacher) {
            JLabel formTitle = new JLabel("Post New Assignment");
            formTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
            formTitle.setForeground(Theme.TEXT_DARK);
            bottomCard.add(formTitle, BorderLayout.NORTH);

            JPanel form = new JPanel(new GridLayout(4, 1, 0, 4));
            form.setBackground(Theme.BG_YELLOW);

            JLabel nameLbl = new JLabel("Activity Title:");
            nameLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
            nameLbl.setForeground(Theme.TEXT_DARK);

            JTextField nameField = new JTextField();
            nameField.setFont(new Font("SansSerif", Font.PLAIN, 12));

            JLabel qLbl = new JLabel("Question Count (1-19):");
            qLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
            qLbl.setForeground(Theme.TEXT_DARK);

            JTextField qField = new JTextField("5");
            qField.setFont(new Font("SansSerif", Font.PLAIN, 12));

            form.add(nameLbl);
            form.add(nameField);
            form.add(qLbl);
            form.add(qField);
            bottomCard.add(form, BorderLayout.CENTER);

            JButton postBtn = Theme.createRedButton("POST ASSIGNMENT");
            postBtn.setPreferredSize(new Dimension(0, 38));
            postBtn.addActionListener(e -> {
                try {
                    String name = nameField.getText().trim();
                    if (name.isEmpty()) {
                        throw new InvalidMenuChoiceException("Title cannot be empty.");
                    }
                    int q = Integer.parseInt(qField.getText().trim());
                    if (q < 1 || q > 19) {
                        throw new InvalidMenuChoiceException("Questions must be between 1 and 19.");
                    }
                    String sec = controller.getSession().getCurrentSection();
                    controller.getSession().getAssignmentsForSection(sec).addElement(name + " (" + q + " Qs)");
                    nameField.setText("");
                    qField.setText("5");
                    refresh();
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
            leaderboardArea.setBackground(Theme.WHITE);
            leaderboardArea.setBorder(new EmptyBorder(8, 10, 8, 10));

            JScrollPane boardScroll = new JScrollPane(leaderboardArea);
            boardScroll.setBorder(BorderFactory.createLineBorder(new Color(225, 200, 170)));
            bottomCard.add(boardScroll, BorderLayout.CENTER);
        }

        stack.add(topCard);
        stack.add(bottomCard);
        add(titleLabel, BorderLayout.NORTH);
        add(stack, BorderLayout.CENTER);
    }

    // Reads the number of questions from strings like "Lab 1: Classes (10 Questions)"
    private int extractQuestionCount(String activityText) {
        try {
            int open = activityText.lastIndexOf('(');
            int close = activityText.lastIndexOf(')');
            if (open != -1 && close > open) {
                String inside = activityText.substring(open + 1, close).replaceAll("[^0-9]", "");
                if (!inside.isEmpty()) {
                    int parsed = Integer.parseInt(inside);
                    return Math.max(1, Math.min(19, parsed));
                }
            }
        } catch (Exception e) {
            // Fallback to 5 questions
        }
        return 5;
    }

    @Override
    public void refresh() {
        UserSession session = controller.getSession();
        String sec = session.getCurrentSection();
        titleLabel.setText((isTeacher ? "Manage Class" : "Classroom") + " — " + sec);

        DefaultListModel<String> model = session.getAssignmentsForSection(sec);
        assignmentList.setModel(model);
        if (!model.isEmpty() && assignmentList.getSelectedIndex() == -1) {
            assignmentList.setSelectedIndex(0);
        }

        if (!isTeacher && leaderboardArea != null) {
            StringBuilder sb = new StringBuilder();
            int rank = 1;
            for (UserSession.StudentRecord s : session.getStudentsInSection(sec)) {
                sb.append(rank++).append(". @").append(s.getName()).append(" — ").append(s.getXp()).append(" XP\n");
            }
            if (sb.length() == 0) {
                sb.append("No students in this section yet.");
            }
            leaderboardArea.setText(sb.toString());
            leaderboardArea.setCaretPosition(0);
        }
    }
}

package App;

import javax.swing.DefaultListModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserSession {
    public enum QuestionType {
        SHORT_ANSWER,
        MULTIPLE_CHOICE
    }

    public static class TeacherQuestion {
        private final QuestionType type;
        private final String prompt;
        private final String correctAnswer;
        private final String[] choices;
        private final int points;

        public TeacherQuestion(QuestionType type, String prompt, String correctAnswer, String[] choices, int points) {
            this.type = type;
            this.prompt = prompt;
            this.correctAnswer = correctAnswer;
            this.choices = choices == null ? new String[0] : choices.clone();
            this.points = points;
        }

        public QuestionType getType() { return type; }
        public String getPrompt() { return prompt; }
        public String getCorrectAnswer() { return correctAnswer; }
        public String[] getChoices() { return choices.clone(); }
        public int getPoints() { return points; }
    }

    public static class TeacherQuiz {
        private final String title;
        private final List<TeacherQuestion> questions;

        public TeacherQuiz(String title, List<TeacherQuestion> questions) {
            this.title = title;
            this.questions = Collections.unmodifiableList(new ArrayList<>(questions));
        }

        public String getTitle() { return title; }
        public List<TeacherQuestion> getQuestions() { return questions; }
        public String getAssignmentLabel() { return title + " (" + questions.size() + " Questions)"; }
    }

    public static class TeacherRecord {
        private final String name;
        private final String pin;

        public TeacherRecord(String name, String pin) {
            this.name = name;
            this.pin = pin;
        }

        public String getName() { return name; }
        public String getPin() { return pin; }
    }

    public static class StudentRecord {
        private final String name;
        private final String section;
        private int xp;
        private int completedQuizzes;

        public StudentRecord(String name, String section, int xp, int completedQuizzes) {
            this.name = name;
            this.section = section;
            this.xp = xp;
            this.completedQuizzes = completedQuizzes;
        }

        public String getName() { return name; }
        public String getSection() { return section; }
        public int getXp() { return xp; }
        public int getCompletedQuizzes() { return completedQuizzes; }

        public void addPoints(int points) {
            xp += points;
            completedQuizzes++;
        }
    }

    private String currentUserName = "Student";
    private String currentSection = "OOP101";

    private final List<StudentRecord> allStudents = new ArrayList<>();
    private final Map<String, TeacherRecord> registeredTeachers = new HashMap<>();
    private final Map<String, DefaultListModel<String>> sectionAssignments = new HashMap<>();
    private final Map<String, Map<String, TeacherQuiz>> teacherQuizzes = new HashMap<>();

    public UserSession() {
        allStudents.add(new StudentRecord("Alex", "OOP101", 1450, 3));
        allStudents.add(new StudentRecord("Jumana", "OOP101", 1120, 2));
        allStudents.add(new StudentRecord("Iyce", "OOP101", 980, 1));

        registeredTeachers.put("santos", new TeacherRecord("Santos", "1111"));
        registeredTeachers.put("reyes", new TeacherRecord("Reyes", "2222"));
        registeredTeachers.put("cruz", new TeacherRecord("Cruz", "3333"));

        getAssignmentsForSection("OOP101").addElement("Lab 1: Classes & Objects (10 Questions)");
        getAssignmentsForSection("CS202").addElement("Lab 1: Inheritance Basics (10 Questions)");
        getAssignmentsForSection("IT301").addElement("Lab 1: Polymorphism Quiz (10 Questions)");
    }

    public void loginStudent(String name, String section) {
        this.currentUserName = name;
        this.currentSection = section.toUpperCase();

        if (findStudent(name) == null) {
            allStudents.add(new StudentRecord(name, this.currentSection, 500, 0));
        }
    }

    public boolean isTeacherRegistered(String name) {
        return registeredTeachers.containsKey(name.trim().toLowerCase());
    }

    public void registerNewTeacher(String name, String pin, String section) {
        String key = name.trim().toLowerCase();
        String cleanSec = section.trim().toUpperCase();
        registeredTeachers.put(key, new TeacherRecord(name.trim(), pin.trim()));
        this.currentUserName = name.trim();
        this.currentSection = cleanSec;
    }

    public boolean authenticateTeacher(String name, String inputPin, String section) {
        TeacherRecord t = registeredTeachers.get(name.trim().toLowerCase());
        if (t != null && t.getPin().equals(inputPin.trim())) {
            this.currentUserName = t.getName();
            this.currentSection = section.trim().toUpperCase();
            return true;
        }
        return false;
    }

    public StudentRecord getCurrentStudent() {
        StudentRecord s = findStudent(currentUserName);
        return (s != null) ? s : new StudentRecord(currentUserName, currentSection, 500, 0);
    }

    private StudentRecord findStudent(String name) {
        for (StudentRecord s : allStudents) {
            if (s.getName().equalsIgnoreCase(name)) return s;
        }
        return null;
    }

    public void addXP(int points) {
        getCurrentStudent().addPoints(points);
    }

    public DefaultListModel<String> getAssignmentsForSection(String section) {
        String key = section.trim().toUpperCase();
        sectionAssignments.putIfAbsent(key, new DefaultListModel<>());
        return sectionAssignments.get(key);
    }

    public boolean addTeacherQuiz(String section, TeacherQuiz quiz) {
        String sectionKey = section.trim().toUpperCase();
        DefaultListModel<String> assignments = getAssignmentsForSection(sectionKey);
        String label = quiz.getAssignmentLabel();
        for (int i = 0; i < assignments.size(); i++) {
            if (assignments.get(i).equalsIgnoreCase(label)) {
                return false;
            }
        }

        assignments.addElement(label);
        teacherQuizzes.putIfAbsent(sectionKey, new HashMap<>());
        teacherQuizzes.get(sectionKey).put(label, quiz);
        return true;
    }

    public TeacherQuiz getTeacherQuiz(String section, String assignmentLabel) {
        Map<String, TeacherQuiz> quizzes = teacherQuizzes.get(section.trim().toUpperCase());
        return quizzes == null ? null : quizzes.get(assignmentLabel);
    }

    public List<StudentRecord> getStudentsInSection(String section) {
        List<StudentRecord> list = new ArrayList<>();
        for (StudentRecord s : allStudents) {
            if (s.getSection().equalsIgnoreCase(section)) list.add(s);
        }
        list.sort((a, b) -> Integer.compare(b.getXp(), a.getXp()));
        return list;
    }

    public String getCurrentUserName() { return currentUserName; }
    public String getCurrentSection() { return currentSection; }
}

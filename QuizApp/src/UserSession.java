import javax.swing.DefaultListModel;
import java.util.*;

public class UserSession {
    public static class StudentRecord {
        public String name;
        public String section;
        public int xp;
        public int completedQuizzes;

        public StudentRecord(String name, String section, int xp, int completedQuizzes) {
            this.name = name;
            this.section = section;
            this.xp = xp;
            this.completedQuizzes = completedQuizzes;
        }
    }

    private String currentUserName = "Student";
    private String currentSection = "OOP101";
    private boolean isTeacher = false;

    private List<StudentRecord> allStudents = new ArrayList<>();
    private Map<String, DefaultListModel<String>> sectionAssignments = new HashMap<>();

    public UserSession() {
        allStudents.add(new StudentRecord("Alex", "OOP101", 1450, 3));
        allStudents.add(new StudentRecord("Jumana", "OOP101", 1120, 2));
        allStudents.add(new StudentRecord("Iyce", "OOP101", 980, 1));

        getAssignmentsForSection("OOP101").addElement("Lab 1: Classes & Objects (10 Questions)");
        getAssignmentsForSection("OOP101").addElement("Quiz 2: Inheritance & Polymorphism (15 Questions)");
    }

    public void loginStudent(String name, String section) {
        this.currentUserName = name;
        this.currentSection = section.toUpperCase();
        this.isTeacher = false;

        if (findStudent(name) == null) {
            allStudents.add(new StudentRecord(name, this.currentSection, 500, 0));
        }
    }

    public void loginTeacher(String name, String section) {
        this.currentUserName = name;
        this.currentSection = section.toUpperCase();
        this.isTeacher = true;
    }

    public StudentRecord getCurrentStudent() {
        StudentRecord s = findStudent(currentUserName);
        return (s != null) ? s : new StudentRecord(currentUserName, currentSection, 500, 0);
    }

    private StudentRecord findStudent(String name) {
        for (StudentRecord s : allStudents) {
            if (s.name.equalsIgnoreCase(name)) return s;
        }
        return null;
    }

    public void addXP(int points) {
        StudentRecord s = getCurrentStudent();
        s.xp += points;
        s.completedQuizzes++;
    }

    public DefaultListModel<String> getAssignmentsForSection(String section) {
        String key = section.toUpperCase();
        sectionAssignments.putIfAbsent(key, new DefaultListModel<>());
        return sectionAssignments.get(key);
    }

    public List<StudentRecord> getStudentsInSection(String section) {
        List<StudentRecord> list = new ArrayList<>();
        for (StudentRecord s : allStudents) {
            if (s.section.equalsIgnoreCase(section)) list.add(s);
        }
        list.sort((a, b) -> Integer.compare(b.xp, a.xp));
        return list;
    }

    public String getCurrentUserName() { return currentUserName; }
    public String getCurrentSection() { return currentSection; }
    public boolean isTeacher() { return isTeacher; }
}

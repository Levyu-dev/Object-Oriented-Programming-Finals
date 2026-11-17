package App;

import javax.swing.DefaultListModel;
import java.util.*;

public class UserSession {
    public static class StudentRecord {
        private String name;
        private String section;
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
            this.xp += points;
            this.completedQuizzes++;
        }
    }

    public static class TeacherRecord {
        private String name;
        private String pin;
        private String section;

        public TeacherRecord(String name, String pin, String section) {
            this.name = name;
            this.pin = pin;
            this.section = section;
        }

        public String getName() { return name; }
        public String getPin() { return pin; }
        public String getSection() { return section; }
    }

    private String currentUserName = "Student";
    private String currentSection = "OOP101";
    private boolean isTeacher = false;

    private List<StudentRecord> allStudents = new ArrayList<>();
    private Map<String, TeacherRecord> registeredTeachers = new HashMap<>();
    private Map<String, DefaultListModel<String>> sectionAssignments = new HashMap<>();

    public UserSession() {
        allStudents.add(new StudentRecord("Alex", "OOP101", 1450, 3));
        allStudents.add(new StudentRecord("Jumana", "OOP101", 1120, 2));
        allStudents.add(new StudentRecord("Iyce", "OOP101", 980, 1));

        registeredTeachers.put("santos", new TeacherRecord("Santos", "1111", "OOP101"));
        registeredTeachers.put("reyes", new TeacherRecord("Reyes", "2222", "CS202"));
        registeredTeachers.put("cruz", new TeacherRecord("Cruz", "3333", "IT301"));

        getAssignmentsForSection("OOP101").addElement("Lab 1: Classes & Objects (10 Questions)");
        getAssignmentsForSection("CS202").addElement("Lab 1: Inheritance Basics (10 Questions)");
        getAssignmentsForSection("IT301").addElement("Lab 1: Polymorphism Quiz (10 Questions)");
    }

    public void loginStudent(String name, String section) {
        this.currentUserName = name;
        this.currentSection = section.toUpperCase();
        this.isTeacher = false;

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
        registeredTeachers.put(key, new TeacherRecord(name.trim(), pin.trim(), cleanSec));
        this.currentUserName = name.trim();
        this.currentSection = cleanSec;
        this.isTeacher = true;
    }

    public boolean authenticateTeacher(String name, String inputPin, String section) {
        TeacherRecord t = registeredTeachers.get(name.trim().toLowerCase());
        if (t != null && t.getPin().equals(inputPin.trim())) {
            this.currentUserName = t.getName();
            this.currentSection = section.trim().toUpperCase();
            this.isTeacher = true;
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
        String key = section.toUpperCase();
        sectionAssignments.putIfAbsent(key, new DefaultListModel<>());
        return sectionAssignments.get(key);
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
    public boolean isTeacher() { return isTeacher; }
}

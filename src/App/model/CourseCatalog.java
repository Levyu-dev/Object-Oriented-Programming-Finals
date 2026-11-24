package App.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CourseCatalog {
    public static final String OOP_BASICS = "OOP Basics";

    public static final class Lesson {
        private final String title;
        private final String overview;
        private final String example;
        private final String checkpoint;
        private final String[] choices;
        private final String correctAnswer;
        private final String explanation;

        private Lesson(
                String title,
                String overview,
                String example,
                String checkpoint,
                String[] choices,
                String correctAnswer,
                String explanation) {
            this.title = title;
            this.overview = overview;
            this.example = example;
            this.checkpoint = checkpoint;
            this.choices = choices.clone();
            this.correctAnswer = correctAnswer;
            this.explanation = explanation;
        }

        public String getTitle() { return title; }
        public String getOverview() { return overview; }
        public String getExample() { return example; }
        public String getCheckpoint() { return checkpoint; }
        public String[] getChoices() { return choices.clone(); }
        public String getCorrectAnswer() { return correctAnswer; }
        public String getExplanation() { return explanation; }
    }

    private final Map<String, Integer> courses = new LinkedHashMap<>();
    private final List<Lesson> oopBasicsLessons = createOopBasicsLessons();

    public CourseCatalog() {
        loadCourses();
    }

    private void loadCourses() {
        File file = new File("data/courses.txt");
        if (file.isFile()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] values = line.split(",", 2);
                    if (values.length == 2 && OOP_BASICS.equals(values[0].trim())) {
                        try {
                            if (Integer.parseInt(values[1].trim()) > 0) {
                                courses.put(OOP_BASICS, oopBasicsLessons.size());
                            }
                        } catch (NumberFormatException ignored) {
                        }
                    }
                }
            } catch (IOException ignored) {
            }
        }

        if (courses.isEmpty()) {
            courses.put(OOP_BASICS, oopBasicsLessons.size());
        }
    }

    private List<Lesson> createOopBasicsLessons() {
        List<Lesson> lessons = new ArrayList<>();
        lessons.add(new Lesson(
                "1. Classes and Objects",
                "A class is a blueprint that describes data and behavior. An object is a usable instance created from that blueprint.",
                "class Student {\n    String name;\n    void study() { }\n}\n\nStudent learner = new Student();",
                "What is a class in Java?",
                new String[]{"A) A blueprint for creating objects", "B) A running method", "C) A value stored in a field", "D) A package"},
                "A",
                "A class defines the structure and behavior that its objects share."));
        lessons.add(new Lesson(
                "2. Fields and Methods",
                "Fields hold an object's state. Methods describe what an object can do. Together they keep related data and behavior in one class.",
                "class Lamp {\n    boolean isOn; // field\n\n    void turnOn() { // method\n        isOn = true;\n    }\n}",
                "Which member describes an object's behavior?",
                new String[]{"A) Package", "B) Method", "C) Import", "D) Comment"},
                "B",
                "Methods contain behavior; fields store state."));
        lessons.add(new Lesson(
                "3. Constructors",
                "A constructor runs when an object is created. Its name matches the class name, and it can initialize the new object's fields.",
                "class Student {\n    String name;\n\n    Student(String name) {\n        this.name = name;\n    }\n}\n\nStudent learner = new Student(\"Mia\");",
                "How is a constructor named?",
                new String[]{"A) It must be named start", "B) It must return void", "C) It has the same name as its class", "D) It must be static"},
                "C",
                "A constructor uses the class name and has no return type."));
        lessons.add(new Lesson(
                "4. Encapsulation",
                "Encapsulation protects an object's state. Keep fields private and expose intentional operations through methods such as getters and setters.",
                "class Student {\n    private int score;\n\n    public int getScore() {\n        return score;\n    }\n}",
                "How should a class protect data from direct outside changes?",
                new String[]{"A) Make every field public", "B) Use private fields and controlled methods", "C) Remove all methods", "D) Make the class abstract"},
                "B",
                "Private fields and controlled methods keep access intentional."));
        lessons.add(new Lesson(
                "5. Inheritance",
                "Inheritance lets a specialized class reuse and extend behavior from a parent class. In Java, a class uses `extends` to inherit from another class.",
                "class Animal {\n    void eat() { }\n}\n\nclass Cat extends Animal {\n    void meow() { }\n}",
                "Which keyword declares class inheritance in Java?",
                new String[]{"A) extends", "B) implements", "C) package", "D) import"},
                "A",
                "`extends` creates a class-to-class inheritance relationship."));
        lessons.add(new Lesson(
                "6. Polymorphism",
                "Polymorphism lets code use a parent type while the actual object's overridden method determines the behavior at runtime.",
                "Animal pet = new Cat();\npet.speak(); // runs Cat's override",
                "With an overridden method, which implementation runs?",
                new String[]{"A) Always the parent's", "B) The first method in the file", "C) No implementation", "D) The actual object's implementation"},
                "D",
                "Runtime polymorphism selects the overridden method for the actual object."));
        lessons.add(new Lesson(
                "7. Abstraction",
                "Abstraction exposes the essential operations while hiding implementation details. Abstract classes and interfaces help define those contracts.",
                "abstract class Shape {\n    abstract double area();\n}",
                "What is the main purpose of abstraction?",
                new String[]{"A) Store every value as public", "B) Sort objects", "C) Expose essential behavior and hide implementation detail", "D) Create duplicate classes"},
                "C",
                "Abstraction focuses on what an object does, not every implementation detail."));
        return Collections.unmodifiableList(lessons);
    }

    public Map<String, Integer> getCourses() {
        return Collections.unmodifiableMap(courses);
    }

    public List<Lesson> getLessons(String courseName) {
        return OOP_BASICS.equals(courseName) ? oopBasicsLessons : Collections.emptyList();
    }

    public int getLessonCount(String courseName) {
        return getLessons(courseName).size();
    }
}

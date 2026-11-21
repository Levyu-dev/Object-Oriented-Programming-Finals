package App;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizQuestionBank {
    public static List<Questions> createQuestions() {
        List<Questions> questions = new ArrayList<>();

        questions.add(new MultipleChoiceQuestions(
                "A method that calculates and returns a value.",
                new String[]{"A) Void Method", "B) Value-returning method", "C) User-defined Method", "D) Equivalent Method"},
                "B"));
        questions.add(new MultipleChoiceQuestions(
                "A keyword used to make a class inherit from another.",
                new String[]{"A) super", "B) static", "C) override", "D) extends"},
                "D"));
        questions.add(new MultipleChoiceQuestions(
                "Which keyword hides implementation details and shows essential features?",
                new String[]{"A) abstraction", "B) inheritance", "C) overloading", "D) encapsulation"},
                "A"));
        questions.add(new IdentificationQuestions(
                "What does polymorphism mean?", "many forms"));
        questions.add(new IdentificationQuestions(
                "A parent-child relationship between classes.", "inheritance"));
        questions.add(new CodeOutputQuestions(
                "class Low { Low() { System.out.println(1); } }\nclass High extends Low { High() { System.out.println(2); } }\nLow obj = new High();",
                "12"));
        questions.add(new CodeCompletionQuestions(
                "Expected Outcome: Meow.\nclass Animal { void speak() {} }\nclass Cat _____ Animal { void speak() { System.out.println(\"Meow\"); } }",
                "extends"));
        questions.add(new DebuggingQuestions(
                "class Parent { Parent(String msg) { System.out.println(msg); } }\nclass Child extends Parent { Child() { System.out.println(\"Child code\"); } }",
                "super(msg);"));

        return Collections.unmodifiableList(questions);
    }
}

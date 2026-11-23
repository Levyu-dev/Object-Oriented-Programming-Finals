package App;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuizQuestionBank {
    private static final List<String> QUICK_PRACTICE_TOPICS = Collections.unmodifiableList(Arrays.asList(
            "Java Basics", "OOP Basics", "Conditionals", "Loops", "Methods", "Arrays"));

    public static List<String> getQuickPracticeTopics() {
        return QUICK_PRACTICE_TOPICS;
    }

    public static int getQuickPracticeQuestionCount(String topic) {
        return createQuestions(topic).size();
    }

    public static List<Questions> createQuestions(String topic) {
        if ("OOP Basics".equals(topic)) return new ArrayList<>(createQuestions());

        List<Questions> questions = new ArrayList<>();
        if ("Java Basics".equals(topic)) {
            addChoice(questions, "Which primitive type stores whole numbers?", "A) int", "B) String", "C) boolean", "D) char", "A");
            addChoice(questions, "What ends most Java statements?", "A) :", "B) ;", "C) ,", "D) .", "B");
            addChoice(questions, "Which method is the usual Java program entry point?", "A) start()", "B) run()", "C) public static void main(String[] args)", "D) init()", "C");
            addChoice(questions, "Which type stores text?", "A) String", "B) int", "C) boolean", "D) double", "A");
            addChoice(questions, "Which statement prints a line to the console?", "A) Console.write()", "B) System.out.println()", "C) print.System()", "D) System.input()", "B");
            addChoice(questions, "Which primitive type can store decimal values with double precision?", "A) int", "B) char", "C) double", "D) boolean", "C");
            addChoice(questions, "Which symbol begins a single-line comment?", "A) //", "B) **", "C) ##", "D) ::", "A");
            addChoice(questions, "Which keyword declares a class?", "A) object", "B) class", "C) define", "D) new", "B");
        } else if ("Conditionals".equals(topic)) {
            addChoice(questions, "Which statement runs code only when a condition is true?", "A) if", "B) import", "C) class", "D) return", "A");
            addChoice(questions, "Which operator checks whether two primitive values are equal?", "A) =", "B) ==", "C) !=", "D) <=", "B");
            addChoice(questions, "What type represents true or false?", "A) int", "B) char", "C) boolean", "D) String", "C");
            addChoice(questions, "Which keyword handles the alternative when an if condition is false?", "A) else", "B) extends", "C) break", "D) static", "A");
            addChoice(questions, "Which operator means logical AND?", "A) ||", "B) &&", "C) !", "D) %", "B");
            addChoice(questions, "What does else if allow a program to do?", "A) Declare a class", "B) Test another condition", "C) Repeat a loop", "D) End the program", "B");
            addChoice(questions, "Which keyword selects among matching cases?", "A) switch", "B) while", "C) package", "D) new", "A");
            addChoice(questions, "What does !true evaluate to?", "A) true", "B) false", "C) 1", "D) null", "B");
        } else if ("Loops".equals(topic)) {
            addChoice(questions, "Which loop is convenient when the iteration count is known?", "A) for", "B) if", "C) switch", "D) try", "A");
            addChoice(questions, "Which loop checks its condition before each iteration?", "A) do-while", "B) while", "C) constructor", "D) if-else", "B");
            addChoice(questions, "Which loop always executes its body at least once?", "A) for", "B) while", "C) do-while", "D) enhanced for", "C");
            addChoice(questions, "Which keyword exits a loop immediately?", "A) continue", "B) break", "C) skip", "D) stop", "B");
            addChoice(questions, "Which keyword skips to the next loop iteration?", "A) continue", "B) break", "C) return", "D) exit", "A");
            addChoice(questions, "What does i++ do?", "A) Decreases i", "B) Increases i by one", "C) Resets i to zero", "D) Compares i", "B");
            addChoice(questions, "What can while (true) create if it never exits?", "A) An infinite loop", "B) A constructor", "C) An array", "D) A class", "A");
            addChoice(questions, "What is a loop inside another loop called?", "A) Infinite loop", "B) Nested loop", "C) Conditional loop", "D) Return loop", "B");
        } else if ("Methods".equals(topic)) {
            addChoice(questions, "Where is a method's return type written?", "A) Before its name", "B) After its body", "C) Inside its parameters", "D) It is omitted", "A");
            addChoice(questions, "What does void mean for a method?", "A) It returns an integer", "B) It returns no value", "C) It cannot be called", "D) It is a class", "B");
            addChoice(questions, "What is a parameter?", "A) A value received by a method", "B) A package name", "C) A loop condition", "D) An object created by new", "A");
            addChoice(questions, "What is an argument?", "A) The method name", "B) A value passed to a method call", "C) The return type", "D) A class field", "B");
            addChoice(questions, "When are two methods overloaded?", "A) Same name with different parameter lists", "B) Same body in two files", "C) Both are private", "D) Both return void", "A");
            addChoice(questions, "Which keyword sends a value back from a method?", "A) yield", "B) return", "C) send", "D) break", "B");
            addChoice(questions, "How can a static method be called without creating an object?", "A) Through its class name", "B) Through a loop", "C) Through a constructor only", "D) It cannot be called", "A");
            addChoice(questions, "A variable declared inside a method is generally what?", "A) A local variable", "B) A package", "C) A superclass", "D) An interface", "A");
        } else if ("Arrays".equals(topic)) {
            addChoice(questions, "What is the index of the first array element?", "A) 1", "B) 0", "C) -1", "D) It depends on the type", "B");
            addChoice(questions, "How do you get an array's number of elements?", "A) size()", "B) count", "C) length", "D) capacity()", "C");
            addChoice(questions, "Which declaration creates an integer array variable?", "A) int[] scores;", "B) array int scores;", "C) scores<int>;", "D) int scores[]()", "A");
            addChoice(questions, "What is the last valid index of an array?", "A) length", "B) length + 1", "C) length - 1", "D) 0", "C");
            addChoice(questions, "Which expression creates space for five integers?", "A) int[5] scores;", "B) new int[5]", "C) int scores(5)", "D) new array int", "B");
            addChoice(questions, "What kind of values does a Java array hold?", "A) Values of its declared element type", "B) Any unrelated values", "C) Methods only", "D) Classes only", "A");
            addChoice(questions, "Which loop is designed to visit each array value?", "A) enhanced for", "B) switch", "C) if", "D) do", "A");
            addChoice(questions, "How do you access the first value in an array named values?", "A) values[1]", "B) values.first", "C) values[0]", "D) values()", "C");
        }
        return questions;
    }

    private static void addChoice(
            List<Questions> questions, String prompt,
            String choiceA, String choiceB, String choiceC, String choiceD,
            String answer) {
        questions.add(new MultipleChoiceQuestions(
                prompt, new String[]{choiceA, choiceB, choiceC, choiceD}, answer));
    }

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

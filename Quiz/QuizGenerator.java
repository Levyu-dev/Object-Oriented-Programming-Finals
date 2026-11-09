package Quiz;

import java.util.*;

public class QuizGenerator {
    public static void main(String[] args) {
        Scanner input = new Scanner (System.in);
        List<Questions> quizBank = new ArrayList<>();

        quizBank.add(new MultipleChoiceQuestions("A method that calculates and returns a value.", new String[] {"A) Void Method", "B) Value-returning method", "C) User-defined Method", "D) Equivalent Method"} , "B"));
        quizBank.add(new MultipleChoiceQuestions("A keyword used to make a class inherit from another.", new String[] {"A) super", "B) static", "C) override", "D) extends"}, "D"));
        quizBank.add(new MultipleChoiceQuestions("This keyword is used so that variables are only visible to the class, its subclasses, and its package.", new String[] {"A) public", "B) final", "C) protected", "D) private"}, "C"));
        quizBank.add(new MultipleChoiceQuestions("How do you connect the child to is parent when making an UML diagram?", new String[] {"A) Dashed line with a hollow triangle arrow", "B) Solid line with a hollow triangle arrow", "C) Solid line with a bold triangle arrow", "D) Dashed line with a bold triangle arrow"}, "B"));
        quizBank.add(new MultipleChoiceQuestions("What does the final method do?", new String[] {"A) It cannot be overriden", "B) It cannot be inherited", "C) Hides the parent method", "D) Hides the object"}, "A"));

        quizBank.add(new IdentificationQuestions("A method override that resolves at runtime", "dynamic method dispatch"));
        quizBank.add(new IdentificationQuestions("What does Polymorphism mean?", "many forms"));
        quizBank.add(new IdentificationQuestions("A concept that establishes a Parent-child relationship", "Inheritance"));
        quizBank.add(new IdentificationQuestions("A method that resolves at compile-time.", "Overloading"));
        quizBank.add(new IdentificationQuestions("The process of hiding unecessary implementation details and showing only the essential features or functionalities of an object.", "Abstraction"));

        quizBank.add(new CodeOutputQuestions("class Low { Low() { System.out.println(1); }}\n class High extends Low { High() { System.out.println(2); } }\n Low obj = new High();", "12")); //Inheritance
        quizBank.add(new CodeOutputQuestions("class Parent { String name =  P;\n void show() { System.out.println(Parent); } class Child extends Parent { String name = C;\n void show() { System.out.println(Child); } }\n Parent p = new Child();\n System.out.println(p.name);\n p.show();", "PChild")); //Polymorphism
        quizBank.add(new CodeOutputQuestions("abstract class CoffeeMachine {\n final void makeCoffee() {\n System.out.println(Boil );\n brew(); }\n abstract void brew(); }\n class Espresso extends CoffeeMachine {\n void brew() { System.out.println(Pressurize ); } }\n class DripCoffee extends CoffeeMachine {\n void brew() { System.out.println(Filter ); } }\n CoffeeMachine m = new Espresso;\n m.makeCoffee();", "Boil Pressurize")); //Abstraction

        quizBank.add(new CodeCompletionQuestions("Expected Outcome: Start Run.\n class Vehicle {\n void start() { System.out.println(Start); }\n }\n class Car ____ Vehicle {\n void drive() {\n _____();\n System.out.println(Run);\n}\n }", "extends, super.start")); //Inheritance
        quizBank.add(new CodeCompletionQuestions("Expected Outcome: Meow.\n class Animal {\n void speak() { System.out.println(Sound); }\n}\n class Cat extends Animal {\n void speak() { System.out.println(Meow); }\n}\n _____ pet = new ____();\n pet.speak();", "Animal, Cat")); //Polymorphism
        quizBank.add(new CodeCompletionQuestions("Expected Outcome: Draw Circle\n _____ class Shape{\n _____ void draw();\n}\n class Circle extends Shape{\n void draw() { System.out.println(Draw Circle); }\n}", "abstract, abstract")); //Abstraction
        
        quizBank.add(new DebuggingQuestions("class Parent {\n Parent(String msg) { System.out.println(msg); }\n}\n class Child extends Parent {\n Child() {\n System.out.println(Child code)\n}\n}", "super(msg);")); //Inheritance
        quizBank.add(new DebuggingQuestions("class Employee {\n void work() { System.out.println(Working); }\n}\n class Developer extends Employee {\n code() {\n System.out.println(Coding)\n}\n}\n Employee e = new Developer();\n e.code();", "((Developer)e).code();")); //Polymorphism
        quizBank.add(new DebuggingQuestions("abstract class Device {\n private abstract void boot();\n }\n class Laptop extends Device {\n void boot()\n }", "protected abstract void boot();")); //Abstraction
        

        int score = 0;
        for(Questions q: quizBank) {
            if (q.askQuestions(input)) {
                score++;
            }
        }

        System.out.println("Quiz Over!");
        System.out.println("Your Final Score: " + score + "/" + quizBank.size());
    }
}
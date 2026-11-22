package quiz;
import java.util.Scanner;

public interface GameMode {
    void play(Scanner input);
    String getModeName();
}
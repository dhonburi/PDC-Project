
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author dhonl
 */
public class WordValidator implements FileInputReader{

    static ArrayList<String> WORDS = new ArrayList<>();

    public WordValidator() {
        readFile();
        // Word list found online.
    }

    @Override
    public void readFile() {
        try {
            FileReader s = new FileReader("./resources/words.txt");
            BufferedReader inStream = new BufferedReader(s);

            String line = inStream.readLine();
            while (line != null) {
                WORDS.add(line);
                line = inStream.readLine();
            }
            inStream.close();

        } catch (FileNotFoundException e) {
            System.out.println("Error finding file");
        } catch (IOException e) {
            System.out.println("Error reading from file");
        }
    }
    
    static boolean isValidGuessWord(String guess) {
        return WORDS.contains(guess);
    }
}

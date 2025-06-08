
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author dhonl
 */


public class WordProvider implements FileInputReader{
    
    static ArrayList<String> ANSWERS = new ArrayList<>();
    
    public WordProvider(){
        readFile();  
        // Word list found online.
    }
    
    @Override
    public void readFile() {    
        try {
            FileReader s = new FileReader("./resources/wordanswers.txt");
            BufferedReader inStream = new BufferedReader(s);

            String line = inStream.readLine();
            while (line != null) {
                ANSWERS.add(line);
                line = inStream.readLine();
            }
            inStream.close();
            
        } catch (FileNotFoundException e) {
            System.out.println("Error finding file");
        } catch (IOException e) {
            System.out.println("Error reading from file");
        }
    }

    private final Random random = new Random();

    public String getRandomWord() {
        return ANSWERS.get(random.nextInt(ANSWERS.size())).toLowerCase();
    }
}


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author dhonl
 */
public class StatSaver implements FileInputReader {
    ArrayList<Integer> stats = new ArrayList<>();
    public void saveStats(int attempts) {
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream("./resources/stats.txt", true));
            pw.println(attempts);
            pw.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error finding file");
        }
    }

    @Override
    public void readFile() {
        stats.clear();
        for (int i = 0; i < 7; i++) {
            stats.add(0);
        }
        try {
            FileReader s = new FileReader("./resources/stats.txt");
            BufferedReader inStream = new BufferedReader(s);

            String line = inStream.readLine();
            while (line != null) {
                int index = Integer.parseInt(line) - 1;
                stats.set(index, stats.get(index) + 1);
                line = inStream.readLine();
            }
            
            inStream.close();
            
        } catch (FileNotFoundException e) {
            System.out.println("Error finding file");
        } catch (IOException e) {
            System.out.println("Error reading from file");
        }
    }
    
    public int gamesPlayed() {
        int total = 0;
        for (int index = 0; index < 7; index++) {
            total += stats.get(index);
        }
        return total;
    }
    
    public int winPercentage() {
        int total = 0;
        for (int index = 0; index < 5; index++) {
            total += stats.get(index);
        }
        int won = total;
        total += stats.get(6);
        return ((won * 100) / total);
    }
    
    public int getGuessDist(int guesses) {
        return stats.get(guesses - 1);
    }
}


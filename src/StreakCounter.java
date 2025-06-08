
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author dhonl
 */
public class StreakCounter implements FileInputReader {
    
    int Streak;
    int MaxStreak;
    
    public void addStreak() {
        try {
            readFile();
            Streak++;
            PrintWriter pw = new PrintWriter(new FileOutputStream("./resources/streak.txt"));
            pw.println(Streak);
            if (Streak > MaxStreak) {
                pw.println(Streak);
                MaxStreak = Streak;
            } else {
                pw.println(MaxStreak);
            }
            pw.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error finding file");
        }
    }
    
    public void endStreak() {
        try {
            readFile();
            PrintWriter pw = new PrintWriter(new FileOutputStream("./resources/streak.txt"));
            pw.println(0);
            Streak = 0;
            pw.println(MaxStreak);
            pw.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error finding file");
        }
    }
    
    @Override
    public void readFile() {
        try {
            FileReader s = new FileReader("./resources/streak.txt");
            BufferedReader inStream = new BufferedReader(s);

            Streak = Integer.parseInt(inStream.readLine());
            MaxStreak = Integer.parseInt(inStream.readLine());
            
            inStream.close();
            
        } catch (FileNotFoundException e) {
            System.out.println("Error finding file");
        } catch (IOException e) {
            System.out.println("Error reading from file");
        }
    }
    
    public int getStreak() {
        return Streak;
    }
    
    public int getMaxStreak() {
        return MaxStreak;
    }
}

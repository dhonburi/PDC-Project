/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author dhonl
 */
public interface ModelListener {
    void onModelChanged(String word);
    void onStats(int played, double percent, int streak, int max, int dist1, int dist2, int dist3, int dist4, int dist5, int dist6);
    void onFeedback(String feedback, int attempt);
}

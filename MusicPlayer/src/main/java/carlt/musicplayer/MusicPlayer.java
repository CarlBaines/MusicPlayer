/*
 * Click nbfs:// nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package carlt.musicplayer;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Carlt
 */

public class MusicPlayer {
    public static void main(String[] args) {
        // Initialises a map. The keys in the map are of type String and the values are of type Song from the Song class.
        Map<String, Song> songs = new HashMap<>();
        
        // Ensures that the code inside the Runnable object is executed on EDT.
        // EDT is a thread in Swing responsible for handling GUI updates and events.
        // Creates a new instance of the MusicPlayerMenu class - passing in the songs hashmap.
        SwingUtilities.invokeLater(() -> new MusicPlayerMenu(songs));
    }
}

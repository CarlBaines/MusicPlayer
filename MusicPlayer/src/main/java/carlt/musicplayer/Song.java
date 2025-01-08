/*
 * Click nbfs:// nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package carlt.musicplayer;

import java.awt.Image;

/**
 *
 * @author Carlt
 */

public class Song {
    // Properties
    private String title;
    private String artist;
    private String filePath;
    private Image albumCover;
    
    // Constructor
    public Song(String title, String artist, String filePath, Image albumCover) {
        this.title = title;
        this.artist = artist;
        this.filePath = filePath;
        this.albumCover = albumCover;
    }
    
    // Getters
    public String getTitle(){
        return title;
    }
    
    public String getArtist(){
        return artist;
    }
    
    public String getFilePath(){
        return filePath;
    }
    
    public Image getAlbumCover(){
        return albumCover;
    }
}

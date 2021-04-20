package com.example.myapplication2;

public class DirectoryDB {

    private int Id;
    private String Folder;

    public DirectoryDB(int Id,String Folder){
        this.Id=Id;
        this.Folder=Folder;
    }

    public void setId(int Id){ this.Id=Id; }

    public void setFolder(String Folder){ this.Folder=Folder; }

    public int getId(){ return Id; }

    public String getFolder(){ return Folder; }
}

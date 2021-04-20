package com.example.myapplication2;

public class ElementDB {

    private int Id;
    private String Title;
    private String Note;
    private int Folder_Id;
    private byte[] Image;

    public ElementDB(int Id,int Folder_Id, String Title, String Note,byte[] Image){
        this.Id=Id;
        this.Note=Note;
        this.Title=Title;
        this.Folder_Id=Folder_Id;
        this.Image=Image;
    }

    public int getId(){
        return Id;
    }

    public String getTitle() {
        return Title;
    }

    public String getNote() {
        return Note;
    }

    public int getFolder_Id() { return Folder_Id; }

    public byte[] getImage() { return Image; }

    public void setId(int id) {
        this.Id = id;
    }

    public void setNote(String note) {
        this.Note = note;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public void setFolder_Id(int folder_id) { this.Folder_Id=folder_id; }

    public void setImage(byte[] Image) { this.Image=Image; }
}

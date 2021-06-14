package sample;

import java.io.Serializable;

public class FileDetails implements Serializable {
    private String fileName;
    private long fileSize ;
    private int id;
    private String createdAt;

    public String getFileName(){
        return fileName;
    }
    public long getFileSize(){
        return fileSize;
    }
    public int getId(){
        return id;
    }
    public String getCreatedAt(){
        return createdAt;
    }
    public  void setFileName(String fileName){
        this.fileName = fileName;
    }
    public  void setFileSize(long fileSize){
        this.fileSize = fileSize;
    }
    public  void setId(int id){
        this.id = id;
    }
    public  void setCreatedAt(String time){
        this.createdAt = time;
    }
    public FileDetails(String fileName,int id,long fileSize,String createdAt){
        this.fileName = fileName;
        this.id = id;
        this.fileSize = fileSize;
        this.createdAt = createdAt;
    }


}

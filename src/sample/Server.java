package sample;

import javafx.util.Pair;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread{
    private String fileDir;
    private int serverPort;
    private boolean stop;
    private int nextFileId;
    private ArrayList <Pair <String, Integer>> fileList = new ArrayList<Pair<String,Integer>>();

    public ArrayList <Pair <String, Integer>> getFileList(){
        return fileList;
    }
    public void updateArrayList(Pair<String,Integer> newFile){
        this.fileList.add(newFile);
    }
    public boolean deleteArrayListItem(int id){
        int index = -1;
        int total = this.fileList.size();
        for(int i = 0; i<total; i++)
        {
            if(id == this.fileList.get(i).getValue()){
                index = i;
                break;
            }
        }
        if(index == -1)
        {
            return false;
        }
        this.fileList.remove(index);
        return true;
    }
    public String getFileDir() {
        return fileDir;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }

    public int getPort() {
        return serverPort;
    }

    public void setPort(int port) {
        this.serverPort = port;
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public static void main(String[] args){
        System.out.println("here!");

        //Creating File path
        File file = new File("D:\\IdeaProject\\Test\\src\\AllFiles");
        file.mkdir();

        //Server Object
        Server serverObj = new Server();

        //Fetching files from directory
        File[] filesArray = file.listFiles();
        serverObj.nextFileId = 1;

        for(File eachFile:filesArray)
        {
            if(eachFile.isFile())
            {
                serverObj.fileList.add(new Pair<String,Integer>(eachFile.getName(),serverObj.nextFileId++));
            }
        }

        //Server Setup
        serverObj.setFileDir("D:\\IdeaProject\\Test\\src\\AllFiles\\");
        serverObj.setPort(9908);
        serverObj.start();
    }

    @Override
    public void run(){
        Socket socket = null;
        try{
            ServerSocket ss = new ServerSocket(serverPort);
            System.out.println("You can create a socket link now. Server is listing...");
            do{
                socket = ss.accept();

                DataInputStream inputStream = new DataInputStream(
                        new BufferedInputStream(socket.getInputStream()));
                int bufferSize = 8192;
                byte[] buf = new byte[bufferSize];
                long passedlen = 0;
                long len = 0;
                int whichOne = inputStream.readInt();
                System.out.println("Upload/Download(0/1) : "+whichOne);
                if(whichOne == 0)
                {
                    String fileName = inputStream.readUTF();
                    String filePath = fileDir+fileName;
                    DataOutputStream fileOut = new DataOutputStream(
                            new BufferedOutputStream(new FileOutputStream(filePath)));
                    len = inputStream.readLong();
                    System.out.println("The Length : "+len);
                    System.out.println("Staring receiving...");
                    while(true){
                        int read = 0;
                        if(inputStream !=null)
                        {
                            read = inputStream.read(buf);
                        }
                        passedlen+=read;
                        if(read == -1)
                        {
                            break;
                        }
                        fileOut.write(buf,0,read);
                    }
                    System.out.println("Receiving completed");
                    fileOut.close();
                    this.updateArrayList(new Pair<>(fileName,nextFileId++));
                    for(Pair<String,Integer> element : this.fileList)
                    {
                        System.out.println(element.getKey()+ " " +element.getValue());
                    }
                }
                else
                {
                    int fileIndex = inputStream.readInt();
                    String fileName = null;
                    for(Pair<String,Integer> element : this.fileList)
                    {
//                        System.out.println(element.getKey()+ " " +element.getValue());
                        if(element.getValue() == fileIndex)
                        {
                            fileName = element.getKey();
                            break;
                        }
                    }
                    System.out.println("Staring upload to client.File name : "+fileName);
                    File fi = new File(fileDir+fileName);
                    DataOutputStream ps = new DataOutputStream(socket.getOutputStream());
                    if(fileName == null)
                    {
                        ps.writeInt(0);
                        ps.flush();
                    }
                    else
                    {
                        DataInputStream fileInputStream = new DataInputStream(new FileInputStream(fileDir+fileName));
                        ps.writeInt(1);
                        ps.flush();
                        ps.writeUTF(fi.getName());
                        ps.flush();
                        ps.writeLong((long)fi.length());
                        ps.flush();

                        while(true){
                            int read = 0;
                            if(fileInputStream != null)
                            {
                                read = fileInputStream.read(buf);
                            }
                            if(read == -1)
                            {
                                break;
                            }
                            ps.write(buf,0,read);
                        }
                        ps.flush();
                        fileInputStream.close();
                        ps.close();
                        System.out.println("Successfully Transferred!");
                    }
                }

            }while(!stop);

        }catch(Exception e){
            System.out.println("An error has occurred!");
            e.printStackTrace();
            return;
        }
    }


}

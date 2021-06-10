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

                }

            }while(!stop);

        }catch(Exception e){
            System.out.println("An error has occurred!");
            e.printStackTrace();
            return;
        }
    }


}

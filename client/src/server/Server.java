package server;

import javafx.util.Pair;
import File.FileDetails;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread {
    private String fileDir;
    private int serverPort;
    private boolean stop;
    private int nextFileId;
    Socket socket = null;

    private ArrayList <FileDetails> fileList = new ArrayList<FileDetails>();

    public ArrayList <FileDetails> getFileList(){
        return fileList;
    }

    public void updateArrayList(FileDetails newFile){
        this.fileList.add(newFile);
    }
    public boolean deleteArrayListItem(int id){
        int index = -1;
        int total = this.fileList.size();
        for(int i = 0; i<total; i++)
        {
            if(id == this.fileList.get(i).getId()){
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

    private String getFileExtension(String name) {
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
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
        File file = new File("C:\\Users\\souha\\Desktop\\New Project\\client\\src\\AllFiles");
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
                serverObj.fileList.add(new FileDetails(eachFile.getName(),serverObj.nextFileId++,eachFile.length(),""));
            }
        }

        //Server Setup
        serverObj.setFileDir("C:\\Users\\souha\\Desktop\\New Project\\client\\src\\AllFiles\\");
//        serverObj.setPort(9908);
        serverObj.start();
    }
    public Server(){
        try{
            ServerSocket ss = new ServerSocket(9908);
            socket = ss.accept();
        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void run(){
//        Socket socket = null;
        try{

            System.out.println("You can create a socket link now. Server is listing...");
            do{
                System.out.println("Client Address:port =>" + socket.getInetAddress() + ":" + socket.getPort());


                //Sending all files details to client
                ObjectOutputStream objOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objOutputStream.writeObject(fileList);
//                objOutputStream.close();

                DataInputStream inputStream = new DataInputStream((socket.getInputStream()));
                int bufferSize = 8192;
                byte[] buf = new byte[bufferSize];
                long passedlen = 0;
                int len = 0;
                int whichOne = inputStream.readInt();
                System.out.println("Upload/Download(0/1) : "+whichOne);
                if(whichOne == 0)  //File Upload
                {
                    String fileName = inputStream.readUTF();
                    String filePath = fileDir+fileName;
                    DataOutputStream fileOut = new DataOutputStream(new FileOutputStream(filePath));
                    len = inputStream.readInt();
                    byte[] fileByte = new byte[len];
                    System.out.println("The Length : "+len);
                    System.out.println("Staring receiving..."+ fileName);
//                    while(true){
//                        int read = 0;
//                        if(inputStream !=null)
//                        {
//                            read = inputStream.read(buf);
//                        }
//                        passedlen+=read;
//                        System.out.println(read);
//                        if(read == -1)
//                        {
//                            break;
//                        }
//                        fileOut.write(buf,0,read);
//                    }
                    inputStream.readFully(fileByte,0,fileByte.length);

                    fileOut.write(fileByte);
                    fileOut.close();
                    System.out.println("Receiving completed");
//                    if(fileOut!= null)
//                    {
//                        fileOut.close();
//                    }
                    this.updateArrayList(new FileDetails(fileName,nextFileId++,len,""));
                    for(FileDetails element : this.fileList)
                    {
                        System.out.println(element.getFileName()+ " " +element.getId() + " " + element.getFileSize());
                    }
                }
                else if(whichOne == 1) // File Download
                {
                    int fileIndex = inputStream.readInt();
                    System.out.println("FileIndex :" + fileIndex);
                    String fileName = null;
                    for(FileDetails element : this.fileList)
                    {
//                        System.out.println(element.getKey()+ " " +element.getValue());
                        if(element.getId() == fileIndex)
                        {
                            fileName = element.getFileName();
                            break;
                        }
                    }
                    System.out.println("Staring upload to client.File name : "+fileName);
                    File fi = new File(fileDir+fileName);
                    DataOutputStream ps = new DataOutputStream(this.socket.getOutputStream());
                    if(fileName == null)
                    {
                        ps.writeInt(0);
//                        ps.flush();
                    }
                    else
                    {
                        ps.writeInt(1);
//                        ps.flush();
                        System.out.println("File Name -- > " + fileName);
                        ps.writeUTF(fi.getName());
//                        ps.flush();
                        ps.writeInt((int)fi.length());
//                        ps.flush();
//                        ps.flush();

//                        while(true){
//                            int read = 0;
//                            if(fileInputStream != null)
//                            {
//                                read = fileInputStream.read(buf);
//                            }
//                            if(read == -1)
//                            {
//                                break;
//                            }
//                            ps.write(buf,0,read);
//                        }
                        DataInputStream fileInputStream = new DataInputStream(new FileInputStream(fileDir+fileName));

                        byte[] fileByte = new byte[(int)fi.length()];
//                        ps.flush();
                        fileInputStream.readFully(fileByte,0,fileByte.length);
                        ps.write(fileByte);
                        fileInputStream.close();
//                        ps.close();
                        System.out.println("Successfully Transferred!");
                    }
                }
                else if(whichOne == 2) // File Delete
                {
                    int fileIndex = inputStream.readInt();
                    String fileName = null;
                    for(FileDetails eachFile:fileList)
                    {
                        if(eachFile.getId() == fileIndex)
                        {
                            fileName = eachFile.getFileName();
                            break;
                        }
                    }
                    DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                    if(fileName == null)
                    {
                        outputStream.writeInt(0);
                        outputStream.flush();
                        System.out.println("Couldn't  delete as the file was not found...");
                    }
                    else
                    {
                        File file = new File(fileDir+fileName);
                        if(file.delete())
                        {
                            deleteArrayListItem(fileIndex);
                            outputStream.writeInt(1);
                            outputStream.flush();
                            outputStream.writeUTF(fileName);
                            outputStream.flush();
                            System.out.println("Successfully deleted :"+fileName);
                        }
                        else
                        {
                            outputStream.writeInt(0);
                            outputStream.flush();
                            System.out.println("Couldn't delete for other reason...");
                        }

                    }
                    outputStream.close();
                }
                else //File Rename
                {
                    //Getting the file index
                    int fileIndex = inputStream.readInt();
                    System.out.println("file Index : "+fileIndex);
                    String fileName = null;
                    int indexInList = -1;
                    for(FileDetails eachFile:fileList)
                    {
                        if(eachFile.getId() == fileIndex)
                        {
                            fileName = eachFile.getFileName();
                            indexInList++;
                            break;
                        }
                    }
                    System.out.println("File Name: " + fileName);
                    DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                    String newFileName = inputStream.readUTF();
                    if(fileName == null)
                    {
                        //If the file is not found
                        outputStream.writeInt(0);
                        outputStream.flush();
                        System.out.println("Couldn't  Rename as the file was not found...");
                    }
                    else
                    {
                        //If the file is found
                        outputStream.writeInt(1);
                        outputStream.flush();


                        String fileExtension = getFileExtension(fileName);
                        newFileName+=fileExtension;
                        System.out.println("New file Name: "+newFileName);
                        File file = new File(fileDir+fileName);
                        File newFile = new File(fileDir+newFileName);
                        boolean isSuccess = file.renameTo(newFile);

                        //Updating the list
                        fileList.remove(indexInList);
                        updateArrayList(new FileDetails(newFileName,fileIndex,file.length(),""));
                        //Does file name changed or not
                        if(!isSuccess)
                        {
                            outputStream.writeInt(0);
                        }
                        else
                        {
                            outputStream.writeInt(1);
                        }
                        outputStream.flush();
                        System.out.println("Previous file name: "+fileName + " to " + newFileName);
                        outputStream.close();
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

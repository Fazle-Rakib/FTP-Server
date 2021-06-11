package sample;

import javafx.util.Pair;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {
    private String filePath;
    private String hostIp;
    private int hostPort;
    private int downPort;
    private String downFileDir;
    public String getFilePath(){
        return filePath;
    }
    public void setFilePath(String filePath){
        this.filePath = filePath;
    }
    public int getHostPort(){
        return hostPort;
    }
    public void setHostPort(int hostPort){
        this.hostPort = hostPort;
    }
    public String getHostIp(){
        return hostIp;
    }
    public void setHostIp(String hostIp){
        this.hostIp = hostIp;
    }
    public String getFileDir() {
        return downFileDir;
    }

    public void setFileDir(String fileDir) {
        this.downFileDir = fileDir;
    }

    public static void main(String[] args){
        //For file uploading
        Client fileUp = new Client();
        fileUp.setHostIp("127.0.0.1");
        fileUp.setHostPort(9908);
//        fileUp.setFilePath("../../../Books/Novel/");
//        fileUp.uploadFile("124612849_2825290834413757_6618010417400904494_o.jpg");

        //For file downloading
//        Client fileDown = new Client();
        File file = new File("D:\\IdeaProject\\Test\\src\\ClientFlies");
        file.mkdir();
        fileUp.setFileDir("D:\\IdeaProject\\Test\\src\\ClientFlies\\");
        fileUp.downloadFile(2);
    }
    public void uploadFile(String fileName){
        Socket sc = null;
        try{
            sc = new Socket(hostIp,hostPort);
            File fi = new File(filePath+fileName);
            System.out.println("File length: "+(int)fi.length());
            DataInputStream fileInputStream = new DataInputStream(new FileInputStream(filePath+fileName));
            DataOutputStream ps = new DataOutputStream(sc.getOutputStream());

            ps.writeInt(0);
            ps.flush();
            ps.writeUTF(fi.getName());
            ps.flush();
            ps.writeLong((long)fi.length());
            ps.flush();

            int bufferSize = 8192;
            byte[] buf = new byte[bufferSize];

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
            sc.close();
            System.out.println("Successfully Transferred!");

        }catch(Exception e)
        {
            System.out.println("An Error has occurred!");
            e.printStackTrace();
        }
    }

//    public void downloadFile(){
//        Socket socket = null;
//        this.downPort = 9889;
//        try{
//            ServerSocket ss = new ServerSocket(downPort);
//            System.out.println("You can create a socket link now. Clent is listing...");
//
//            socket = ss.accept();
//
//            DataInputStream inputStream = new DataInputStream(
//                    new BufferedInputStream(socket.getInputStream()));
//            int bufferSize = 8192;
//            byte[] buf = new byte[bufferSize];
//            long passedlen = 0;
//            long len = 0;
//            String fileName = inputStream.readUTF();
//            String filePath = downFileDir+fileName;
//            DataOutputStream fileOut = new DataOutputStream(
//                    new BufferedOutputStream(new FileOutputStream(filePath)));
//            len = inputStream.readLong();
//            System.out.println("The Length : "+len);
//            System.out.println("Staring receiving...");
//            while(true){
//                int read = 0;
//                if(inputStream !=null)
//                {
//                    read = inputStream.read(buf);
//                }
//                passedlen+=read;
//                if(read == -1)
//                {
//                    break;
//                }
//                fileOut.write(buf,0,read);
//            }
//            System.out.println("Receiving completed");
//            fileOut.close();
//        }catch(Exception e){
//            System.out.println("An error has occurred!");
//            e.printStackTrace();
//            return;
//        }
//
//    }
    public void downloadFile(int index){
        Socket sc = null;
        try{
            sc = new Socket(hostIp,hostPort);
            System.out.println("Successfully connected(client) for file retrieval");
            DataOutputStream ps = new DataOutputStream(sc.getOutputStream());

            //Sending message to determining Upload/Download(0/1) request
            ps.writeInt(1);
            ps.flush();
            //Sending Index of the file
            ps.writeInt(index);
            ps.flush();



            DataInputStream inputStream = new DataInputStream(
                    new BufferedInputStream(sc.getInputStream()));
            int hasFile = inputStream.readInt();
            System.out.println("Does have file? : "+hasFile);
            if(hasFile == 0)
            {
                System.out.println("Couldn't find the file");
            }
            else
            {
                System.out.println("File started downloading in client-side...");
                int bufferSize = 8192;
                byte[] buf = new byte[bufferSize];
                long passedlen = 0;
                long len = 0;

                String fileName = inputStream.readUTF();
                String fileDir = downFileDir+fileName;
                System.out.println("Filepath : " + fileDir);
                System.out.println("Filename receiving : "+fileName);
                DataOutputStream fileOut = new DataOutputStream(
                        new BufferedOutputStream(new FileOutputStream(fileDir)));
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
            }

            ps.close();
            inputStream.close();
            sc.close();
        }catch(Exception e)
        {
            System.out.println("An Error has occurred in file retrieval(client)!");
            e.printStackTrace();
        }
    }
}

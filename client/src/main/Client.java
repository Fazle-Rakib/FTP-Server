package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import File.FileDetails;

public class Client {
    private String filePath;
    private String hostIp;
    private int hostPort;
    private int downPort;
    private String downFileDir;
    private static ArrayList<FileDetails> fileList = new ArrayList<FileDetails>();
    private InputStream inputStreamObj = null;
    private OutputStream outputStreamObj = null;
//    private ObservableList<FileDetails> observableFileList = null;

    Socket clientSocket = null;
//    private String newFileName;


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getHostPort() {
        return hostPort;
    }

    public void setHostPort(int hostPort) {
        this.hostPort = hostPort;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public String getFileDir() {
        return downFileDir;
    }

    public void setFileDir(String fileDir) {
        this.downFileDir = fileDir;
    }

//    public String getNewFileName(){
//        return newFileName;
//    }

    public Client(String ipAddress, int port) throws Exception {
        //For file uploading
//        Client fileUp = new Client();


//        fileUp.setFilePath("../../../Books/Novel/");
//        fileUp.uploadFile("ANYODIN (www.amarbooks.org).pdf");

        //For file downloading
//        Client fileDown = new Client();
        File file = new File(System.getProperty("user.dir")+"\\src\\ClientFlies");
        file.mkdir();
        setFileDir(System.getProperty("user.dir")+"\\src\\ClientFlies\\");
//        fileUp.downloadFile(1);

        //For file deletion
//        fileUp.deleteFile(1);

        //For file rename
//        fileUp.newFileName="second.exe";
//        fileUp.fileRename(1,"third");


        this.setHostIp(ipAddress);
        this.setHostPort(port);
        clientSocket = new Socket(hostIp, hostPort);
        inputStreamObj = clientSocket.getInputStream();
        outputStreamObj = clientSocket.getOutputStream();
        fetchFileListFromServer();


//        this.setFilePath("../../../Books/Novel/");
//        this.uploadFile("ANYODIN (www.amarbooks.org).pdf");
    }

    public void fetchFileListFromServer() {
        try {
            //Getting file details from server
            InputStream inputStreamObj = this.inputStreamObj;
            ObjectInputStream objInputStream = new ObjectInputStream((inputStreamObj));
            fileList = (ArrayList<FileDetails>) objInputStream.readObject();

            for (FileDetails each : this.fileList) {
                System.out.println(each.getFileName() + " --> " + each.getId() );
            }

//                    objInputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in getFileList");

        }

    }

    public static ObservableList<FileDetails> getObservableFileList() {

        ObservableList<FileDetails> observableFileList = FXCollections.observableArrayList();
        for (FileDetails each : fileList) {
            System.out.println("Ok3");
            observableFileList.add(each);
            System.out.println(each.getFileName() + " " + each.getId() + " " + each.getFileSize());
        }
        return observableFileList;
    }

    public ArrayList<FileDetails> getFileList() {
        return fileList;

    }

    public void uploadFile(File fileToUpload) throws Exception {
        if (fileToUpload == null) {
            throw new Exception("No File Selected");
        }
        DataInputStream fileInputStream = null;
        DataOutputStream ps = null;
        try {

            String fileName = fileToUpload.getName();
            String filePath = fileToUpload.getAbsolutePath();
            System.out.println(fileName + filePath);

            File fi = new File(filePath);
            System.out.println("File length: " + (int) fi.length());
            fileInputStream = new DataInputStream(new FileInputStream(filePath));
            ps = new DataOutputStream(this.clientSocket.getOutputStream());


            ps.writeInt(0);
//            ps.flush();
            ps.writeUTF(fi.getName());
//            ps.flush();
            ps.writeInt((int) fi.length());

            byte[] fileByte = new byte[(int)fi.length()];

            fileInputStream.readFully(fileByte,0,fileByte.length);

            ps.write(fileByte);

//            ps.flush();

//            int bufferSize = 8192;
//            byte[] buf = new byte[bufferSize];

//            while (true) {
//                int read = 0;
//                if (fileInputStream != null) {
//                    read = fileInputStream.read(buf);
//                }
//                System.out.println(read);
//                if (read == -1) {
//                    break;
//                }
//                ps.write(buf, 0, read);
//            }
//            ps.flush();
//            objInputStream.close();
//            fileInputStream.close();
//            ps.close();
//            sc.close();
            System.out.println("Successfully Transferred!");
            fetchFileListFromServer();

        } catch (Exception e) {
            System.out.println("An Error has occurred!");
            e.printStackTrace();
        }

    }

    public void downloadFile(FileDetails fileDetails) {
//        Socket sc = null;
        DataOutputStream fileOut = null;
        try {
            DataOutputStream dataOutputStream =new DataOutputStream(this.clientSocket.getOutputStream());
//            sc = new Socket(hostIp, hostPort);
            System.out.println("Successfully connected(client) for file retrieval");

            //Sending message to determining Upload/Download(0/1) request
            dataOutputStream.writeInt(1);
//            dataOutputStream.flush();
            //Sending Index of the file
            dataOutputStream.writeInt(fileDetails.getId());
//            dataOutputStream.flush();
//            this.clientSocket.shutdownOutput();


            DataInputStream dataInputStream = new DataInputStream(this.clientSocket.getInputStream());
            int hasFile = 1;
            // hasFile = dataInputStream.readInt();
            System.out.println("Does have file? : " + hasFile);
            if (hasFile == 0) {
                System.out.println("Couldn't find the file");
            } else {
                System.out.println("File started downloading in client-side...");
//                int bufferSize = 8192;
//                byte[] buf = new byte[bufferSize];
                long passedlen = 0;
                long len = 0;


//                len = dataInputStream.readInt();
                len = fileDetails.getFileSize();
                System.out.println("The Length : " + len);
                System.out.println("Staring receiving...");

                int lengthInt = (int)len;
                byte[] fileByte = new byte[lengthInt];


//                ObjectInputStream objectInputStream = new ObjectInputStream(inputStreamObj);
//                String fileName =(String) objectInputStream.readObject();
                String fileName =(String) fileDetails.getFileName();

//                String fileName = (String) obj;
                String fileDir = downFileDir + fileName;
                System.out.println("Filepath : " + fileDir);
                System.out.println("Filename receiving : " + fileName);

//                File fileToDownload = new File(fileDir);
//                FileOutputStream fileOutputStream = new FileOutputStream(fileToDownload);
                DataOutputStream fileOutNew = new DataOutputStream(new FileOutputStream(fileDir));
                dataInputStream.readFully(fileByte,0,fileByte.length);
                fileOutNew.write(fileByte);
//                fileOutNew.close();

//                fileOutputStream.close();
//                fileOut = new DataOutputStream(new FileOutputStream(fileDir));
//                fileOut.write(fileByte);
//                fileOut.flush();
//                fileOut.close();
//                dataInputStream.close();
//                while (true) {
//                    int read = 0;
//                    if (dataInputStream != null) {
//                        read = dataInputStream.read(buf);
//                    }
//                    passedlen += read;
//                    if (read == -1) {
//                        break;
//                    }
//                    fileOut.write(buf, 0, read);
//                }
                System.out.println("Receiving completed");
                fetchFileListFromServer();
//                fileOut.close();
            }

//            dataOutputStream.close();
//            dataInputStream.close();
//            clientSocket.close();
//            new Client("127.0.0.1",9908);
//            clientSocket.close();
        } catch (Exception e) {
            System.out.println("An Error has occurred in file retrieval(client)!");
            e.printStackTrace();
        }
    }

    public void deleteFile(int index) {
//        Socket socket = null;
        try {
//            socket = new Socket(hostIp, hostPort);
            System.out.println("Successfully connected in the server for delete operation");

            DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());

            //Sending a integer to determine delete operation[In server]
            outputStream.writeInt(2);
            outputStream.flush();

            //Sending file index
            outputStream.writeInt(index);
            outputStream.flush();

            //Receiving feedback
            DataInputStream inputStream = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
            int isFound = inputStream.readInt();
            if (isFound == 0) {
                System.out.println("Couldn't find the file to delete");
            } else {
                String fileName = inputStream.readUTF();
                System.out.println(fileName + " has been deleted successfully");
//                fetchFileListFromServer();
            }
            fetchFileListFromServer();

//            outputStream.close();
//            inputStream.close();
//            clientSocket.close();
        } catch (Exception e) {
            System.out.println("Getting Exception from client-side delete operation");
            e.printStackTrace();
        }
    }

    public void fileRename(int index, String fileName) {
        String newFileName = fileName;
//        String fileExtension = getFileExtension(fileName);
        if (newFileName == null) {
            System.out.println("File name could not be empty");
            return;
        }
        System.out.println("New file name: " + newFileName);
        Socket socket = null;
        try {
            socket = new Socket(hostIp, hostPort);
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            //Sending rename operation request
            outputStream.writeInt(3);
            outputStream.flush();

            //Sending file index
            outputStream.writeInt(index);
            outputStream.flush();
            outputStream.writeUTF(newFileName);
            outputStream.flush();

            int isFound = inputStream.readInt();
            if (isFound == 0) {
                System.out.println("Could not find the file to rename");
            } else {

                int isSuccess = inputStream.readInt();
                if (isSuccess == 0) {
                    System.out.println("Could not find the file to rename");
                } else {
                    System.out.println("Changed the previous file name :" + " to " + newFileName);
                }
            }
            outputStream.close();
            inputStream.close();

        } catch (Exception e) {
            System.out.println("Exception from file rename module");
            e.printStackTrace();
        }

    }

    public void getFileListFromServer(){
        try{
            DataOutputStream dataOutputStream =new DataOutputStream(this.clientSocket.getOutputStream());
            dataOutputStream.writeInt(4);
            fetchFileListFromServer();
        }catch(Exception e)
        {
            System.out.println("Error from Get file List server");
            e.printStackTrace();
        }

    }

    public void closingSocket(){
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
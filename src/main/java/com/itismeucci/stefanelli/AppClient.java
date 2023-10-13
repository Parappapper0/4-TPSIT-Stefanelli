package com.itismeucci.stefanelli;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class AppClient {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Client client = new Client();

        String ip;
        int port;

        String serverInput;

        int clientID;

        System.out.println("Inserisci l'ip");
        ip = scanner.nextLine();
        System.out.println("Inserisci la porta");
        port = scanner.nextInt();

        client.start(ip, port);

        serverInput = client.receive();
        System.out.println(serverInput);

        clientID = Integer.parseInt(serverInput.split("#")[1]);

        client.send("Connessione riuscita con il Client #" + clientID + "\n");

        System.out.println(client.receive());

        int fileAmount = Integer.parseInt(client.receive());

        for (int i = 0; i < fileAmount; i++)
            System.out.println(client.receive());

        int fileID;
        scanner.nextLine();
        while (true) {
            try{fileID = Integer.parseInt(scanner.nextLine()); }catch(Exception e) { System.out.println("Not a number"); continue;}

            if (fileID < 0 || fileID >= fileAmount) { System.out.println("Not a valid number"); continue;}

            break;
        }

        client.send(String.valueOf(fileID) + '\n');

        String filename = client.receive();
        File download = new File(filename);
        try {download.createNewFile();} catch (IOException e) {e.printStackTrace();}
        
        FileOutputStream outToFile;

        try {outToFile = new FileOutputStream(download);} catch (FileNotFoundException e) {e.printStackTrace(); scanner.close(); return;}

        try {outToFile.write(client.receiveBytes());} catch (IOException e) {e.printStackTrace();}

        System.out.println("File scaricato");

        try {outToFile.close();} catch (IOException e) {e.printStackTrace();}
        client.close();
        scanner.close();
    }
}

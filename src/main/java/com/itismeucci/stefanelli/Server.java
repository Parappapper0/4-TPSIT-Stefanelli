package com.itismeucci.stefanelli;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;

public class Server {
    
    protected Socket clientSocket;
    protected BufferedReader input;
    protected DataOutputStream output;
    protected int clientID;

    protected static int clientNumber = 0;

    public void accept(ServerSocket serverSocket) {

        try {

            clientSocket = serverSocket.accept();
            clientNumber++;
            clientID = clientNumber;
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new DataOutputStream(clientSocket.getOutputStream());

        } catch (IOException e) {
            
            e.printStackTrace();
        }
    }

    public void send(String message) {

        try {

            output.writeBytes(message);

        } catch (IOException e) {
            
            e.printStackTrace();
        }
    }

    public void send(File file) {

        try {

            output.write(Files.readAllBytes(file.toPath()));

        } catch (IOException e) {
            
            e.printStackTrace();
        }
    }

    public String receive() {
        
        try {

            return input.readLine();

        } catch (IOException e) {
            
            e.printStackTrace();
            return "ERROR";
        }
    }

    public void closeClient() {

        try {
            
            clientSocket.close();

        } catch (IOException e) {
            
            e.printStackTrace();
        }
    }
}
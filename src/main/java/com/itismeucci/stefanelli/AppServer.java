package com.itismeucci.stefanelli;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

public class AppServer {
    public static void main(String[] args) {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(6789);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("[SERVER] Server aperto su porta 6789");

        FileThread.files = new File("src/main/resources/files/");


        while (true) {

            Server server = new Server();
            server.accept(serverSocket);
            FileThread thread = new FileThread(server);
            thread.start();
        }
    }

    private static class FileThread extends Thread {

        private Server server;
        protected static File files;
        protected String inputString;

        public FileThread(Server server) {

            this.server = server;
        }

        @Override
        public void run() {

            server.send("Connessione con il server riuscita, sei il client #" + server.clientID + "\n");
            System.out.println("[SERVER] " + server.receive());

            server.send("Seleziona quale file scaricare: \n");
            server.send(String.valueOf(files.list().length) + '\n');

            for (int i = 0; i < files.list().length; i++)
                    server.send("[" + i + "] " + files.list()[i] + '\n');
            
            while (true) {

                inputString = server.receive();

                int fileSelected;

                try {

                    fileSelected = Integer.parseInt(inputString);

                } catch (Exception e) {
                    
                    System.out.println("[SERVER] ### Non un numero");
                    server.send("ERROR\n");
                    continue;
                }

                if(fileSelected >= files.list().length || fileSelected < 0) {

                    System.out.println("[SERVER] ### Numero non disponibile");
                    server.send("ERROR\n");
                    continue;
                }

                System.out.println("[Client #" + server.clientID + "] Selezionato file id: " + inputString + " e nome: " + files.list()[fileSelected]);

                server.send("SUCCESS\n");
                server.send(files.list()[fileSelected] + '\n');
                server.send(files.listFiles()[fileSelected]);
                break;
            }

            server.send("Connessione terminata\n");
            System.out.println("[SERVER] Connessione con il Client #" + server.clientID + " terminata");
            server.closeClient();
        }
    }
}

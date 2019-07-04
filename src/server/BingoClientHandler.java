package server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class BingoClientHandler {
    private Socket socket;

    private BufferedReader in;
    private PrintWriter out;

    private BingoRoom room;

    private String name;
    private boolean bingo;
    private boolean ready;

    public BingoClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void setRoom(BingoRoom room) {
        this.room = room;
    }

    public String getName() {
        return name;
    }

    public boolean isBingo() {
        return bingo;
    }

    public boolean isReady() {
        return ready;
    }

    public void reset() {
        bingo = false;
        ready = false;
    }

    public void connect() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

            BingoServer.threadPool.submit(() ->{
                try {
                    String message;

                    while ((message = in.readLine()) != null) {
                        System.out.println(message);

                        String readLine[] = message.split("/");

                        if (readLine[0].equals("NAME")) {
                            name = readLine[1];
                            room.broadcast("ENTER/" + getName());
                        } else if (readLine[0].equals("MESSAGE")) {
                            room.broadcast("MESSAGE/" + name + ": " + readLine[1]);
                        } else if (readLine[0].equals("READY")) {
                            ready = true;
                            bingo = false;
                            room.start();
                        } else if (readLine[0].equals("UNREADY")) {
                            ready = false;
                        } else if (readLine[0].equals("VALUE")) {
                            room.temp(readLine[1]);
                        }  else if (readLine[0].equals("BINGO")) {
                            bingo = true;
                        }
                    }
                } catch(IOException e) {
                    e.printStackTrace();
                    disconnect();
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (room != null) {
                room.removeClient(this);
            }

            if (in != null) {
                in.close();
            }

            if (out != null) {
                out.close();
            }

            if (socket != null && !socket.isClosed()) {
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String message) {
        BingoServer.sendThreadPool.submit(() ->{
                out.println(message);
                out.flush();
        });
    }
}

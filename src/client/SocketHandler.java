package client;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SocketHandler {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public SocketHandler() throws IOException {
        socket =new Socket("127.0.0.1", 9000);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
    }

    public void disconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }

            if (in != null) {
                in.close();
            }

            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String message) {
        new Thread(() -> {
            System.out.println(message);
            out.println(message);
            out.flush();
        }).start();
    }

    public String receive() throws IOException {
        return in.readLine();
    }
}

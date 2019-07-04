package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BingoServer {
    private Vector<BingoClientHandler> clientHandlers;
    //private Vector<BingoRoom> rooms;
    private BingoRoom room;

    public static ExecutorService threadPool;
    public static ExecutorService sendThreadPool;

    private ServerSocket serverSocket;

    public BingoServer() {
        clientHandlers = new Vector<>();
        room = new BingoRoom(4);
        threadPool = Executors.newCachedThreadPool();
        sendThreadPool = Executors.newFixedThreadPool(10);
    }

    public void open() {
        try {
            System.out.println("빙고 서버 시작");
            serverSocket = new ServerSocket(9000);
        } catch (IOException e) {
            e.printStackTrace();
        }

        threadPool.submit(() ->{
            while (true) {
                try {
                    System.out.println("서버 리스닝 중");
                    Socket socket = serverSocket.accept();
                    System.out.println("클라이언트 접속 - " + socket.getInetAddress().getHostAddress());
                    BingoClientHandler clientHandler = new BingoClientHandler(socket);
                    clientHandler.connect();
                    clientHandler.setRoom(room);
                    room.addClient(clientHandler);
                    clientHandlers.add(clientHandler);
                } catch (IOException e) {
                    e.printStackTrace();
                    close();
                }
            }
        });
    }

    public void close() {
        try {
            System.out.println("빙고 서버 종료");

            if (threadPool != null && !threadPool.isShutdown()) {
                threadPool.shutdown();
            }

            if (sendThreadPool != null && sendThreadPool.isShutdown()) {
                sendThreadPool.shutdown();
            }

            for (BingoClientHandler clientHandler : clientHandlers) {
                clientHandler.disconnect();
            }

            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

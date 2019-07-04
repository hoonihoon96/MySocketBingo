package server;

import java.util.Collections;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

public class BingoRoom {
    private int maxUserCount;
    private int currentUserCount;
    private int currentTurn;

    private Vector<BingoClientHandler> clientHandlers;

    public BingoRoom(int maxUserCount) {
        this.maxUserCount = maxUserCount;
        currentUserCount = 0;
        currentTurn = 0;
        clientHandlers = new Vector<>();
    }

    public void addClient(BingoClientHandler clientHandler) {
        if (maxUserCount == currentUserCount) {
            clientHandler.send("FULL/");
            return;
        }

        clientHandlers.add(clientHandler);
        currentUserCount++;
    }

    public void removeClient(BingoClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
        currentUserCount--;
    }

    public void start() {
        for (BingoClientHandler clientHandler : clientHandlers) {
            if (!clientHandler.isReady()) {
                return;
            }
        }

        broadcast("START/");
        Collections.shuffle(clientHandlers);
        clientHandlers.get(currentTurn++).send("TURN/");
    }

    public void temp(String value) {
        broadcast("VALUE/" + value);

        try {
            System.out.println("대기 중");
            BingoServer.sendThreadPool.awaitTermination(1000, TimeUnit.NANOSECONDS);
            Thread.sleep(100);
            System.out.println("대기 끝");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        checkBingo();
    }

    public void checkBingo() {
        try {
            System.out.println("대기 중");
            BingoServer.sendThreadPool.awaitTermination(1000, TimeUnit.NANOSECONDS);
            Thread.sleep(100);
            System.out.println("대기 끝");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int count = 0;

        for (BingoClientHandler clientHandler : clientHandlers) {
            if (clientHandler.isBingo()) {
                count++;
            }
        }

        if (count > 1) {
            StringBuilder message = new StringBuilder();
            message.append("DRAW/");

            for (BingoClientHandler clientHandler : clientHandlers) {
                if (clientHandler.isBingo()) {
                    message.append(clientHandler.getName())
                            .append("/");
                }

                clientHandler.reset();
            }

            broadcast(message.toString());
        } else if (count == 1) {
            StringBuilder message = new StringBuilder();
            message.append("WIN/");

            for (BingoClientHandler clientHandler : clientHandlers) {
                if (clientHandler.isBingo()) {
                    message.append(clientHandler.getName());
                }

                clientHandler.reset();
            }

            broadcast(message.toString());
        } else {
            broadcast("STATUS/" + clientHandlers.get(currentTurn).getName());
            clientHandlers.get(currentTurn++).send("TURN/");

            if (currentTurn == currentUserCount) {
                currentTurn = 0;
            }
        }
    }

    public void broadcast(String message) {
        for (BingoClientHandler clientHandler : clientHandlers) {
            clientHandler.send(message);
        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

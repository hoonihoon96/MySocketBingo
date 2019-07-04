package client;

import java.util.ArrayList;

public class BingoGame {
    public static final int BINGO_TOTAL = 25;
    public static final int BINGO_WIDTH = 5;
    public static final int BINGO_HEIGHT = 5;
    public static final int BINGO_COUNT = 3;

    private ArrayList<BingoCard> bingoCards = new ArrayList<>();

    public ArrayList<BingoCard> getBingoCards() {
        return bingoCards;
    }

    public void setBingoCards(ArrayList<String> values) {
        for (int i = 0; i < BINGO_TOTAL; i++) {
            String value = values.get(i);

            bingoCards.add(new BingoCard(i, value));
        }
    }

    public void clearBingoCards() {
        bingoCards.clear();
    }

    public void checkBingoCardByValue(String value) {
        for (BingoCard bingoCard : bingoCards) {
            if (bingoCard.getValue().equals(value)) {
                System.out.println(value + "번 체크 됨");
                bingoCard.setChecked(true);
                break;
            }
        }
    }

    public boolean isBingo() {
        int count = 0;

        for (int i = 0; i < BINGO_WIDTH; i++) {
            if (bingoCards.get(i).isChecked() &&
                    bingoCards.get(i + 5).isChecked() &&
                    bingoCards.get(i + 10).isChecked() &&
                    bingoCards.get(i + 15).isChecked() &&
                    bingoCards.get(i + 20).isChecked()){
                count++;
            }
        }

        for (int i = 0; i < BINGO_TOTAL; i+=5) {
            if (bingoCards.get(i).isChecked() &&
                    bingoCards.get(i + 1).isChecked() &&
                    bingoCards.get(i + 2).isChecked() &&
                    bingoCards.get(i + 3).isChecked() &&
                    bingoCards.get(i + 4).isChecked()) {
                count++;
            }
        }

        if (bingoCards.get(0).isChecked() &&
                bingoCards.get(6).isChecked() &&
                bingoCards.get(12).isChecked() &&
                bingoCards.get(18).isChecked() &&
                bingoCards.get(24).isChecked()) {
            count++;
        }

        if (bingoCards.get(4).isChecked() &&
                bingoCards.get(8).isChecked() &&
                bingoCards.get(12).isChecked() &&
                bingoCards.get(16).isChecked() &&
                bingoCards.get(20).isChecked()) {
            count++;
        }

        System.out.println("내 빙고 카운트 : " + count);

        return count >= BINGO_COUNT;
    }
}

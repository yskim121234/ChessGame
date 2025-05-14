package ChessSystem;

import java.util.Scanner;

public class InputManager {
    private Scanner sc = new Scanner(System.in);

    public InputManager() {};

    public Position inputPosition(){
        String input = sc.next();
        if(input.length() != 2) return null;
        input = input.toUpperCase();

        char cRank = input.charAt(1);
        char cFile = input.charAt(0);

        int rank = 8 - (cRank - 48);
        int file = cFile - 65;

        return new Position(rank, file);
    }
}

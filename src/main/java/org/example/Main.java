package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws IOException {
        new Main().run();
    }

    record Position(int row, int col) {

        Position move(String direction) {
            return switch (direction){
                case "L" -> new Position(row, col-1);
                case "R" -> new Position(row, col+1);
                case "U" -> new Position(row+1, col);
                case "D" -> new Position(row-1, col);
                default -> throw new UnsupportedOperationException();
            };
        }

        Position follow(Position headPosition) {
            if (isInRange(headPosition)) {
                return new Position(row, col);
            }

            // vertical move
            if (headPosition.row == row) {
                return new Position(row, headPosition.col < col ? col-1 : col+1);
            }

            // horizontal move
            if (headPosition.col == col) {
                return new Position(headPosition.row < row ? row-1 : row+1, col);
            }

            // diagonal move
            return new Position(headPosition.row < row ? row-1 : row+1, headPosition.col < col ? col-1 : col+1);
        }

        boolean isInRange(Position headPosition) {
            return (headPosition.col >= col-1 && headPosition.col <= col+1 && headPosition.row >= row-1 && headPosition.row <= row+1);
        }
    }

    record Move(String direction, int steps) {}

    final String path = "TODO";
    List<Move> moves = new ArrayList<>();
    Set<Position> uniquePositions = new HashSet<>();
    Position headPosition = new Position(0, 0);
    Position tailPosition = new Position(0, 0);

    void run() throws IOException {
        this.readMoves();
        this.uniquePositions.add(tailPosition);

        this.moves.forEach(this::executeMove);
        System.out.println(this.uniquePositions.size());
       // this.uniquePositions.forEach(pos -> System.out.printf("%s\n", pos));
    }

    void executeMove(Move move) {
        int steps = move.steps;
        for (int i = 0; i < steps; i++) {
            this.headPosition = this.headPosition.move(move.direction);
            this.tailPosition = this.tailPosition.follow(headPosition);
            this.uniquePositions.add(this.tailPosition);
            System.out.printf("H%s -> T%s\n", this.headPosition, this.tailPosition);
        }
    }

    void readMoves() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(this.path + "input.txt"))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                String direction = parts[0];
                int steps = Integer.parseInt(parts[1]);
                this.moves.add(new Move(direction, steps));
            }
        }
    }

}
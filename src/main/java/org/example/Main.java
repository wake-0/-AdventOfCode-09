package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        new Main().run();
    }

    record Position(int row, int col) {

        Position move(String direction) {
            return switch (direction) {
                case "L" -> new Position(row, col - 1);
                case "R" -> new Position(row, col + 1);
                case "U" -> new Position(row + 1, col);
                case "D" -> new Position(row - 1, col);
                default -> throw new UnsupportedOperationException();
            };
        }

        Position follow(Position headPosition) {
            if (isInRange(headPosition)) {
                return new Position(row, col);
            }

            // vertical move
            if (headPosition.row == row) {
                return new Position(row, headPosition.col < col ? col - 1 : col + 1);
            }

            // horizontal move
            if (headPosition.col == col) {
                return new Position(headPosition.row < row ? row - 1 : row + 1, col);
            }

            // diagonal move
            return new Position(headPosition.row < row ? row - 1 : row + 1, headPosition.col < col ? col - 1 : col + 1);
        }

        boolean isInRange(Position headPosition) {
            return (headPosition.col >= col - 1 && headPosition.col <= col + 1 && headPosition.row >= row - 1 && headPosition.row <= row + 1);
        }
    }

    record Move(String direction, int steps) {
    }

    final String path = "TODO";
    List<Move> moves = new ArrayList<>();
    Set<Position> uniquePositions = new HashSet<>();
    Position[] snake = new Position[] {
            new Position(0, 0),
            new Position(0, 0),
            new Position(0, 0),
            new Position(0, 0),
            new Position(0, 0),
            new Position(0, 0),
            new Position(0, 0),
            new Position(0, 0),
            new Position(0, 0),
            new Position(0, 0)};

    void run() throws IOException {
        this.readMoves();
        this.uniquePositions.add(new Position(0, 0));

        this.moves.forEach(this::executeMove);
        System.out.println(this.uniquePositions.size());
    }

    void executeMove(Move move) {
        int steps = move.steps;
        for (int i = 0; i < steps; i++) {
            this.snake[0] = this.snake[0].move(move.direction);
            for (int s = 1; s < this.snake.length; s++) {
                this.snake[s] = this.snake[s].follow(this.snake[s-1]);

                if (s == this.snake.length-1) {
                    this.uniquePositions.add(this.snake[s]);
                }
            }
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
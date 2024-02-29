package by.ustsinovich.elementaryciphers.cryptography;

import java.util.*;

public class PlayfairEncryptor {
    private final AlphabetType alphabetType;

    private final String alphabet;

    private final int power;

    private final int matrixSize;

    public PlayfairEncryptor(AlphabetType alphabetType) {
        this.alphabetType = alphabetType;
        this.alphabet = alphabetType.getAlphabet();
        this.power = alphabetType.getPower();
        this.matrixSize = alphabetType.getMatrixSize();
    }

    private boolean isValidChar(char character) {
        return alphabet.indexOf(character) != -1;
    }

    private char[][][] generateKeyMatrix(String[] keys) {
        char[][][] keyMatrix = new char[4][][];

        int keyIndex = 0;
        for (int k = 0; k < 4; k++) {
            if (isOdd(k)) {
                keyMatrix[k] = fillMatrixByKeyword(keys[keyIndex]);
                keyIndex++;
            } else {
                keyMatrix[k] = fillMatrixByAlphabet();
            }
        }

        return keyMatrix;
    }

    private char[][] fillMatrixByKeyword(String key) {
        char[][] matrix = new char[matrixSize][matrixSize];
        Set<Character> usedChars = new HashSet<>();
        Queue<Character> charsQueue = new ArrayDeque<>();

        for (char c : key.toCharArray()) {
            if (!usedChars.contains(c)) {
                charsQueue.add(c);
                usedChars.add(c);
            }
        }

        for (char c : alphabet.toCharArray()) {
            if (!usedChars.contains(c)) {
                charsQueue.add(c);
            }
        }

        Iterator<Character> iterator = charsQueue.iterator();
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (iterator.hasNext()) {
                    matrix[i][j] = iterator.next();
                } else {
                    matrix[i][j] = ' ';
                }
            }
        }

        return matrix;
    }

    private char[][] fillMatrixByAlphabet() {
        char[][] matrix = new char[matrixSize][matrixSize];
        int index = 0;

        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (index < power) {
                    matrix[i][j] = alphabet.charAt(index);
                    index++;
                } else {
                    matrix[i][j] = ' ';
                }
            }
        }
        return matrix;
    }

    private static boolean isOdd(int num) {
        return (num & 1) == 1;
    }

    public String encrypt(String plainText, String key) {
        StringBuilder cipherTextBuilder = new StringBuilder();

        String plainTextCopy = plainText
                .toUpperCase()
                .replaceAll(" ", "");

        plainTextCopy = switch (alphabetType) {
            case RUSSIAN -> plainTextCopy
                    .replaceAll("[^А-Я]", "");
            case ENGLISH -> plainTextCopy
                    .replaceAll("[^A-Z]", "")
                    .replaceAll("J", "I");
        };

        if (isOdd(plainTextCopy.length())) {
            switch (alphabetType) {
                case RUSSIAN -> plainTextCopy += "Й";
                case ENGLISH -> plainTextCopy += "Z";
            }
        }

        String keyCopy = key.toUpperCase().replaceAll(" ", "");

        String[] keys = keyCopy.split(",");

        char[][][] keyMatrix = generateKeyMatrix(keys);
        for (int i = 0; i < 4; i++) {
            for (char[] chars : keyMatrix[i]) {
                System.out.println(chars);
            }

            System.out.println();
        }

        for (int i = 0; i < plainTextCopy.length(); i += 2) {
            char m1 = plainTextCopy.charAt(i);
            char m2 = plainTextCopy.charAt(i + 1);

            int[] m1Index = findPosition(keyMatrix[0], m1);
            int[] m2Index = findPosition(keyMatrix[2], m2);

            cipherTextBuilder.append(keyMatrix[1][m1Index[0]][m2Index[1]]);
            cipherTextBuilder.append(keyMatrix[3][m2Index[0]][m1Index[1]]);
        }

        return cipherTextBuilder.toString();
    }

    public String decrypt(String cipherText, String key) {
        StringBuilder plainTextBuilder = new StringBuilder();

        String keyCopy = key.toUpperCase().replaceAll(" ", "");

        String cipherTextCopy = cipherText
                .toUpperCase()
                .replaceAll(" ", "");

        cipherTextCopy = switch (alphabetType) {
            case RUSSIAN -> cipherTextCopy
                    .replaceAll("[^А-Я]", "");
            case ENGLISH -> cipherTextCopy
                    .replaceAll("[^A-Z]", "")
                    .replaceAll("J", "I");
        };

        if (isOdd(cipherTextCopy.length())) {
            switch (alphabetType) {
                case RUSSIAN -> cipherTextCopy += "Й";
                case ENGLISH -> cipherTextCopy += "Z";
            }
        }

        String[] keys = keyCopy.split(",");

        char[][][] keyMatrix = generateKeyMatrix(keys);

        for (int i = 0; i < cipherTextCopy.length(); i += 2) {
            char c1 = cipherTextCopy.charAt(i);
            char c2 = cipherTextCopy.charAt(i + 1);

            int[] c1Index = findPosition(keyMatrix[1], c1);
            int[] c2Index = findPosition(keyMatrix[3], c2);

            plainTextBuilder.append(keyMatrix[0][c1Index[0]][c2Index[1]]);
            plainTextBuilder.append(keyMatrix[2][c2Index[0]][c1Index[1]]);
        }

        return plainTextBuilder.toString();
    }

    private int[] findPosition(char[][] square, char c) {
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (square[i][j] == c) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{-1, -1};
    }

    public AlphabetType getAlphabetType() {
        return alphabetType;
    }

    public String getAlphabet() {
        return alphabet;
    }

    public int getPower() {
        return power;
    }

    public int getMatrixSize() {
        return matrixSize;
    }

    public enum AlphabetType {
        RUSSIAN("АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ"),
        ENGLISH("ABCDEFGHIKLMNOPQRSTUVWXYZ");

        private final String alphabet;
        private final int power;
        private final int matrixSize;

        AlphabetType(String alphabet) {
            this.alphabet = alphabet;
            this.power = alphabet.length();
            this.matrixSize = (int) Math.round(Math.sqrt(this.power));
        }

        public String getAlphabet() {
            return alphabet;
        }

        public int getPower() {
            return power;
        }

        public int getMatrixSize() {
            return matrixSize;
        }
    }
}

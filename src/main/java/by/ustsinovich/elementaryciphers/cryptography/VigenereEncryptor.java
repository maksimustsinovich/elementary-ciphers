package by.ustsinovich.elementaryciphers.cryptography;

/**
 * Класс, содержащий методы шифрования/дешифрования текста алгоритмом Виженера с прямым ключом.
 */
public class VigenereEncryptor {
    /**
     * Тип алфавита.
     */
    private AlphabetType alphabetType;
    /**
     * Строка, содержащая символы алфавита.
     */
    private String alphabet;

    /**
     * Мощность алфавита.
     */
    private int power;

    /**
     * @param alphabetType тип алфавита.
     */
    public VigenereEncryptor(AlphabetType alphabetType) {
        this.alphabetType = alphabetType;
        this.alphabet = alphabetType.getAlphabet();
        this.power = alphabetType.getPower();
    }

    /**
     * Шифрует исходный текст алгоритмом Виженера с прямым ключом.
     * @param plainText исходный текст (M);
     * @param key ключевая фраза (K).
     * @return зашифрованный текст (C).
     * @throws IllegalArgumentException ключ содержит невалидные символы.
     */
    public String encrypt(String plainText, String key) throws IllegalArgumentException {
        StringBuilder cipherTextBuilder = new StringBuilder();

        // Получаем копии исходного текста и ключевой фразы, приводим их к верхнему регистру.
        String plainTextCopy = plainText.toUpperCase();
        String keyCopy = key.toUpperCase().replaceAll(" ", ""); // В ключевой фразе убираем пробелы

        if (!isValidKey(keyCopy)) {
            throw new IllegalArgumentException("Невалидный ключ.");
        }

        int keyIndex = 0; // Начинаем с первого символа ключевой фразы
        for (char currentChar : plainTextCopy.toCharArray()) {
            if (isValidChar(currentChar)) {
                char keyTextChar = getKeyChar(keyCopy, keyIndex); // Получаем k[j]
                char cipherTextChar = encryptCharacter(currentChar, keyTextChar); // Получаем c[i]

                cipherTextBuilder.append(cipherTextChar); // Добавляем в строку с зашифрованным текстом c[i]

                keyIndex++; // Переходим к следующему символу ключевой фразы
            }
        }

        return cipherTextBuilder.toString();
    }

    /**
     * Расшифровывает зашифрованный текст алгоритмом Виженера с прямым ключом.
     * @param cipherText зашифрованный текст (C);
     * @param key ключевая фраза (K).
     * @return исходный текст (M).
     * @throws IllegalArgumentException ключ содержит невалидные символы.
     */
    public String decrypt(String cipherText, String key) throws IllegalArgumentException {
        StringBuilder plainTextBuilder = new StringBuilder();

        // Получаем копии исходного текста и ключевой фразы, приводим их к верхнему регистру.
        String cipherTextCopy = cipherText
                .toUpperCase();
        String keyCopy = key
                .toUpperCase()
                .replaceAll(" ", ""); // В ключевой фразе убираем пробелы

        if (!isValidKey(keyCopy)) {
            throw new IllegalArgumentException("Невалидный ключ.");
        }

        int keyIndex = 0; // Начинаем с первого символа ключевой фразы
        for (char currentChar : cipherTextCopy.toCharArray()) {
            if (isValidChar(currentChar)) {
                char keyTextChar = getKeyChar(keyCopy, keyIndex); // Получает k[j]
                char plainTextChar = decryptCharacter(currentChar, keyTextChar); // Получаем m[i]

                plainTextBuilder.append(plainTextChar); // Добавляем в строку с исходным текстом m[i]

                keyIndex++; // Переходим к следующему символу ключевой фразы
            }
        }

        return plainTextBuilder.toString();
    }

    /**
     * @param m символ исходного текста
     * @param k символ ключевой фразы
     * @return символ зашифрованного текста
     */
    private char encryptCharacter(char m, char k) {
        int plainTextCharIndex = alphabet.indexOf(m);
        int keyTextCharIndex = alphabet.indexOf(k);
        int cipherTextCharIndex = (plainTextCharIndex + keyTextCharIndex) % power;

        return alphabet.charAt(cipherTextCharIndex);
    }

    /**
     * @param c символ зашифрованного текста
     * @param k символ ключевой фразы
     * @return символ исходного текста
     */
    private char decryptCharacter(char c, char k) {
        int cipherTextCharIndex = this.alphabet.indexOf(c);
        int keyTextCharIndex = this.alphabet.indexOf(k);
        int plainTextCharIndex = (cipherTextCharIndex - keyTextCharIndex + this.power) % this.power;

        return this.alphabet.charAt(plainTextCharIndex);
    }

    /**
     * @param character символ
     * @return содержит ли алфавит этот символ
     */
    private boolean isValidChar(char character) {
        return alphabet.indexOf(character) != -1;
    }

    /**
     * @param key ключевая фраза
     * @return является ли ключевая фраза валидной
     */
    private boolean isValidKey(String key) {
        for (char k : key.toCharArray()) {
            if (!isValidChar(k)) {
                return false;
            }
        }

        return true;
    }

    /**
     * @param key ключевая фраза
     * @param i индекс
     * @return k[i]
     */
    private char getKeyChar(String key, int i) {
        return key.charAt(i % key.length());
    }

    public String getAlphabet() {
        return alphabet;
    }

    public int getPower() {
        return power;
    }

    public AlphabetType getAlphabetType() {
        return alphabetType;
    }

    public enum AlphabetType {
        RUSSIAN("АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ"),
        ENGLISH("ABCDEFGHIJKLMNOPQRSTUVWXYZ");

        private final String alphabet;
        private final int power;

        AlphabetType(String alphabet) {
            this.alphabet = alphabet;
            this.power = alphabet.length();
        }

        public String getAlphabet() {
            return alphabet;
        }

        public int getPower() {
            return power;
        }
    }
}

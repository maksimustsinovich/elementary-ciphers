package by.ustsinovich.elementaryciphers.controller;

import by.ustsinovich.elementaryciphers.cryptography.PlayfairEncryptor;
import by.ustsinovich.elementaryciphers.cryptography.VigenereEncryptor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.UnaryOperator;

public class MainController {
    @FXML
    public RadioButton playfair;
    @FXML
    public RadioButton vigenere;
    @FXML
    public RadioButton encrypt;
    @FXML
    public RadioButton decrypt;
    @FXML
    public Pane root;

    private static final FileChooser fileChooser = new FileChooser();
    public TextArea text;
    public TextField key;
    public TextArea result;

//    private UnaryOperator<TextFormatter.Change> getPlayfairFilter() {
//        return change -> {
//            String newText = change.getControlNewText();
//            return newText.matches("[a-zA-Z]+(\\s[a-zA-Z]+)*,[a-zA-Z]+(\\s[a-zA-Z]+)*") ? change : null;
//        };
//    }
//
//    private UnaryOperator<TextFormatter.Change> getVigenereFilter() {
//        return change -> {
//            String newText = change.getControlNewText();
//            return newText.matches("[а-яА-Я]+(\\s[а-яА-Я]+)*") ? change : null;
//        };
//    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void initialize() {
        ToggleGroup processType = new ToggleGroup();
        encrypt.setToggleGroup(processType);
        decrypt.setToggleGroup(processType);

        ToggleGroup cipherType = new ToggleGroup();
        playfair.setToggleGroup(cipherType);
        vigenere.setToggleGroup(cipherType);

        vigenere.setOnAction(event -> {
            key.setText("");
        });
        playfair.setOnAction(event -> {
            key.setText("");
        });

        decrypt.setOnAction(event -> result.setText(""));
        encrypt.setOnAction(event -> result.setText(""));
    }

    @FXML
    public void open(ActionEvent actionEvent) {
        File file = fileChooser.showOpenDialog(root.getScene().getWindow());
        if (file != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                if (content.length() <= 1000) {
                    text.setText(content.toString());
                } else {
                    showError("File too long");
                }
            } catch (IOException e) {
                showError("Cannot read");
            }
        }
    }

    @FXML
    public void save(ActionEvent actionEvent) {
        File file = fileChooser.showSaveDialog(root.getScene().getWindow());
        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(result.getText());
            } catch (IOException e) {
                showError("Cannot write");
            }
        }
    }

    @FXML
    public void process(ActionEvent actionEvent) {
        String keyText = key.getText();
        String inputText = text.getText();

        if (keyText.isEmpty() || inputText.isEmpty()) {
            showError("Key or text empty");
            return;
        }

        if (playfair.isSelected()) {
            if (!keyText.matches("[a-zA-Z]+(\\s[a-zA-Z]+)*,\\s[a-zA-Z]+(\\s[a-zA-Z]+)*,\\s[a-zA-Z]+(\\s[a-zA-Z]+)*,\\s[a-zA-Z]+(\\s[a-zA-Z]+)*")) {
                showError("Invalid key for Playfair cipher. It should be two English phrases separated by a comma.");
                return;
            }
            PlayfairEncryptor playfairEncryptor = new PlayfairEncryptor(PlayfairEncryptor.AlphabetType.ENGLISH);

            if (encrypt.isSelected()) {
                result.setText(playfairEncryptor.encrypt(inputText, keyText));
            } else {
                result.setText(playfairEncryptor.decrypt(inputText, keyText));
            }
        } else if (vigenere.isSelected()) {
            if (!keyText.matches("[а-яА-Я]+(\\s[а-яА-Я]+)*")) {
                showError("Invalid key for Vigenère cipher. It should be a Russian phrase.");
                return;
            }
            VigenereEncryptor vigenereEncryptor = new VigenereEncryptor(VigenereEncryptor.AlphabetType.RUSSIAN);

            if (encrypt.isSelected()) {
                result.setText(vigenereEncryptor.encrypt(inputText, keyText));
            } else {
                result.setText(vigenereEncryptor.decrypt(inputText, keyText));
            }
        }
    }
}
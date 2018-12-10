package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import sample.data.Matrice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Controller {
    private HashMap<String, Matrice> map = new HashMap<>();
    private ArrayList<TextField> textFields = new ArrayList<>();
    private int tfWidth = 60, tfHeight = 30;
    private ArrayList<String> listeNoms = new ArrayList<>();
    private ObservableList<String> observableList;
    private ChoiceBox<String> choixGauche, choixDroite;

    @FXML
    private Spinner spinnerLignes, spinnerColonnes;

    @FXML
    private GridPane gridPane;

    @FXML
    private HBox hBox;

    @FXML
    private TextArea textArea;

    public void initialize() {
        listeNoms.add("");
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER);

        setChoiceBox();

        gridPane.setAlignment(Pos.CENTER);

        gridPane.setGridLinesVisible(true);
        setGridPane();

        spinnerLignes.valueProperty().addListener((that, oldValue, newValue) -> setGridPane());
        spinnerColonnes.valueProperty().addListener((that, oldValue, newValue) -> setGridPane());

    }

    private void setGridPane() {
        gridPane.getChildren().clear();
        textFields.clear();

        gridPane.setMaxSize((int) spinnerColonnes.getValue() * tfWidth, (int) spinnerLignes.getValue() * tfHeight);

        for (int y = 0; y < (int) spinnerLignes.getValue(); y++) {
            for (int x = 0; x < (int) spinnerColonnes.getValue(); x++) {
                TextField temp = new TextField();
                temp.setPromptText("0");

                temp.setAlignment(Pos.CENTER);
                temp.setMaxWidth(Region.USE_PREF_SIZE);
                temp.setMaxHeight(Region.USE_PREF_SIZE);
                temp.setMinWidth(Region.USE_PREF_SIZE);
                temp.setMinHeight(Region.USE_PREF_SIZE);
                temp.setPrefWidth(tfWidth);
                temp.setPrefHeight(tfHeight);


                textFields.add(temp);
                gridPane.add(temp, x, y);
            }
        }
    }

    public void ajouterMatrice() {
        String resultat = "", texte = "Veuillez entrer le nom de la matrice";
        boolean notNew = true;

        while (notNew) {
            notNew = false;

            TextInputDialog alerte = new TextInputDialog("Entrez ici");

            alerte.setTitle("Nom de matrice");
            alerte.setHeaderText(texte);
            alerte.setContentText("Nom: ");
            resultat = alerte.showAndWait().get();

            for (String nom : listeNoms) {
                if (nom.equals(resultat) || resultat.isEmpty()) {
                    notNew = true;
                    texte = "Veuillez entrer un nom valide";
                }
            }
        }

        listeNoms.add(resultat);

        Matrice tempo = new Matrice(resultat, (int) spinnerColonnes.getValue(), (int) spinnerLignes.getValue());

        int value = 0;
        for (int i = 0; i < (int) spinnerLignes.getValue(); i++) {
            for (int j = 0; j < (int) spinnerColonnes.getValue(); j++) {
                if (!textFields.get(value).getText().isEmpty())
                    tempo.setValue(Double.parseDouble(textFields.get(value).getText()), j, i);
                else
                    tempo.setValue(0, j, i);

                value++;
            }
        }
        setChoiceBox();
        map.put(resultat, tempo);

    }

    private void setChoiceBox() {
        hBox.getChildren().clear();

        observableList = FXCollections.observableList((List) listeNoms);
        choixGauche = new ChoiceBox<>(observableList);
        choixGauche.setValue(listeNoms.get(0));
        choixGauche.setMaxWidth(Region.USE_PREF_SIZE);
        choixGauche.setMaxHeight(Region.USE_PREF_SIZE);
        choixGauche.setMinWidth(Region.USE_PREF_SIZE);
        choixGauche.setMinHeight(Region.USE_PREF_SIZE);
        choixGauche.setPrefWidth(70);
        choixGauche.setPrefHeight(25);

        choixDroite = new ChoiceBox<>(observableList);
        choixDroite.setValue(listeNoms.get(0));
        choixDroite.setMaxWidth(Region.USE_PREF_SIZE);
        choixDroite.setMaxHeight(Region.USE_PREF_SIZE);
        choixDroite.setMinWidth(Region.USE_PREF_SIZE);
        choixDroite.setMinHeight(Region.USE_PREF_SIZE);
        choixDroite.setPrefWidth(70);
        choixDroite.setPrefHeight(25);

        hBox.getChildren().addAll(choixGauche, choixDroite);
    }

    public Matrice[] getMatrices() {
        if (choixGauche.getValue().isEmpty() && choixDroite.getValue().isEmpty()) {
            Alert alerte = new Alert(Alert.AlertType.INFORMATION);
            alerte.setTitle("Erreur");
            alerte.setHeaderText("Aucune matrice entrée");
            alerte.setContentText("Veuillez entrer des matrices valides");
            alerte.showAndWait();
            return null;
        } else if (choixGauche.getValue().isEmpty())
            return new Matrice[]{map.get(choixDroite.getValue())};
        else if (choixDroite.getValue().isEmpty())
            return new Matrice[]{map.get(choixGauche.getValue())};
        else
            return new Matrice[]{map.get(choixGauche.getValue()), map.get(choixDroite.getValue())};
    }

    public void addition() {
        Matrice mats[] = getMatrices();

        if (mats.length == 2) {
            if (mats[0].getRows() == mats[1].getRows() && mats[0].getColumns() == mats[0].getColumns()) {
                int tempoA;
                int tempoB;
                int addition;
                Matrice resultat = new Matrice("", mats[0].getColumns(), mats[0].getRows());
                for (int i = 0; i < mats[0].getRows(); i++) {
                    for (int j = 0; j < mats[0].getColumns(); j++) {
                        tempoA = (int) mats[0].getValue(j, i);
                        tempoB = (int) mats[1].getValue(j, i);
                        addition = tempoA + tempoB;
                        resultat.setValue(addition, j, i);
                    }
                }
                String resultatTexte = mats[0].getNom() + " + " + mats[1].getNom() + " = \n" + resultat.toString();
                textArea.setText(resultatTexte);
            } else {
                textArea.setText("Les formats des matrices ne sont pas identiques");
            }
        } else {
            textArea.setText("Veuillez entrer deux matrices");
        }
    }

    public void soustraction() {
        Matrice mats[] = getMatrices();

        if (mats.length == 2) {
            if (mats[0].getRows() == mats[1].getRows() && mats[0].getColumns() == mats[0].getColumns()) {
                int tempoA;
                int tempoB;
                int soustraction;
                Matrice resultat = new Matrice("", mats[0].getColumns(), mats[0].getRows());
                for (int i = 0; i < mats[0].getRows(); i++) {
                    for (int j = 0; j < mats[0].getColumns(); j++) {
                        tempoA = (int) mats[0].getValue(j, i);
                        tempoB = (int) mats[1].getValue(j, i);
                        soustraction = tempoA - tempoB;
                        resultat.setValue(soustraction, j, i);
                    }
                }
                String resultatTexte = mats[0].getNom() + " + " + mats[1].getNom() + " = \n" + resultat.toString();
                textArea.setText(resultatTexte);
            } else {
                textArea.setText("Les formats des matrices ne sont pas identiques");
            }
        } else {
            textArea.setText("Veuillez entrer deux matrices");
        }
    }

    public void multScalaire() {

        Matrice mats[] = getMatrices();
        if (mats.length >= 1)
            multScalaireSuperieur(mats[0]);
        else
            textArea.setText("Vous devez entrer une seule matrice à multiplier");
    }

    private void multScalaireSuperieur(Matrice matrice) {
        TextInputDialog alerte = new TextInputDialog(" Entrez le scalaire");
        alerte.setTitle("Multiplication par un scalaire");
        alerte.setHeaderText("Veuillez entrer le scalaire");
        alerte.setContentText("Scalaire : ");
        String scalaire = alerte.showAndWait().get();
        int multiplicateur = Integer.parseInt(scalaire);


        Matrice resultat = new Matrice("", matrice.getColumns(), matrice.getRows());
        for (int i = 0; i < matrice.getRows(); i++) {
            for (int j = 0; j < matrice.getColumns(); j++) {
                int element = (int) matrice.getValue(j, i);
                resultat.setValue(multiplicateur * element, j, i);
            }
        }
        textArea.setText(resultat.toString());
    }

    public void puissance() { //Pas terminée

        ArrayList<Matrice> liste = new ArrayList<>();
        Matrice mats[] = getMatrices();
        if (mats.length == 1) {

            TextInputDialog alerte = new TextInputDialog(" Entrez l'exposant");
            alerte.setTitle("Multiplication par un exposant");
            alerte.setHeaderText("Veuillez entrer l'exposant");
            alerte.setContentText("Exposant : ");
            String exposant = alerte.showAndWait().get();
            int exp = Integer.parseInt(exposant);
            int valeur = 0;

            while (exp - Math.pow(2, valeur) > 0) {
                valeur++;
            }
            if (exp - Math.pow(2, valeur) < 0) {
                valeur--;
            }
            System.out.println(valeur);

            for (int i = 0; i < valeur + 1; i++) {
                mats[1] = mats[0];
                produitMat();
            }

        }
    }

    public void transposee() {
        Matrice mats[] = getMatrices();

        if (mats.length == 1) {
            Matrice resultat = new Matrice("", mats[0].getColumns(), mats[0].getRows());
            for (int i = 0; i < mats[0].getRows(); i++) {
                for (int j = 0; j < mats[0].getColumns(); j++) {
                    double transpo = mats[0].getValue(i, j);
                    resultat.setValue(transpo, j, i);
                }
            }
            textArea.setText(resultat.toString());
        }
        if (mats.length == 2) {
            textArea.setText("Veuillez entrer une seule matrice");
        }
        if (mats.length == 0)
            textArea.setText("Veuillez entrer une matrice à transposer");
    }

    public void inversion() {

    }

    public void produitMat() {
        int valeur = 0;
        Matrice mats[] = getMatrices();
        if (mats[0].getColumns() == mats[1].getRows()) {
            Matrice resultat = new Matrice("", mats[1].getColumns(), mats[0].getRows());

            for (int i = 0; i < resultat.getRows(); i++) {
                for (int j = 0; j < resultat.getColumns(); j++) {
                    double calculee = 0;
                    for (int k = 0; k < mats[0].getColumns(); k++) {
                        calculee += (mats[0].getValue(k, valeur) * mats[1].getValue(valeur, k));///???
                        resultat.setValue(calculee, j, i);
                    }
                    valeur++;

                }
                valeur = 0;
            }
            textArea.setText(resultat.toString());
        } else textArea.setText("Matrices incompatibles");
    }

    public void produitVect() {

    }

    public void produitHad() {

    }

    public void produitTens() {

    }

    public void determinant() {
        Matrice mat[] = getMatrices();

        if (mat.length == 1) {
            if (mat[0].getColumns() == mat[0].getRows()) {
                if (mat[0].getColumns() == 1)
                    textArea.setText("Det(" + mat[0].getNom() + ") = " + mat[0].getValue(0, 0));
                else if (mat[0].getColumns() == 2)
                    textArea.setText("Det(" + mat[0].getNom() + ") = " + ((mat[0].getValue(0, 0) * mat[0].getValue(1, 1)) - (mat[0].getValue(1, 0) * mat[0].getValue(0, 1))));
                else if (mat[0].getRows() > 2)
                    determinantSupeieur(mat[0], true);
            } else
                textArea.setText("La matrice doit être carrée pour pouvoir calculer son déterminant");
        } else
            textArea.setText("Vous devez entrer une seule matrice pour calculer son déterminant");
    }

    private double determinantSupeieur(Matrice matrice, boolean texte) {
        double valeurs[] = new double[matrice.getColumns()];
        double determinantDparD[] = new double[matrice.getColumns()];
        Matrice mineurs[] = new Matrice[matrice.getColumns()];

        for (int i = 0; i < matrice.getColumns(); i++) {
            valeurs[i] = matrice.getValue(i, 0);

            mineurs[i] = new Matrice("", matrice.getColumns() - 1, matrice.getRows() - 1);
            for (int a = 1; a < matrice.getRows(); a++) {
                for (int b = 0; b < matrice.getColumns(); b++) {
                    if (b != i) {
                        if (b < i)
                            mineurs[i].setValue(matrice.getValue(b, a), b, a - 1);
                        else if (b > i)
                            mineurs[i].setValue(matrice.getValue(b, a), b - 1, a - 1);
                    }
                }
            }
        }

        if (mineurs[0].getColumns() > 2) {
            for (int i = 0; i < mineurs.length; i++) {
                determinantDparD[i] = determinantSupeieur(mineurs[i], false);
            }
        } else if (mineurs[0].getColumns() == 2) {
            for (int i = 0; i < mineurs.length; i++)
                determinantDparD[i] = (mineurs[i].getValue(0, 0) * mineurs[i].getValue(1, 1)) - (mineurs[i].getValue(1, 0) * mineurs[i].getValue(0, 1));
        }

        double resultat = 0;
        for (int i = 0; i < valeurs.length; i++) {
            resultat += valeurs[i] * Math.pow(-1, i + 2) * determinantDparD[i];
        }

        if (texte)
            textArea.setText("Det(" + matrice.getNom() + ") = " + resultat);
        return resultat;
    }
}

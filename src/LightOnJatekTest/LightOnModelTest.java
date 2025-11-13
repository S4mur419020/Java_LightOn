package LightOnJatekTest;

import Model.LightOnModel;

public class LightOnModelTest {
    
    public static void main(String[] args) {
        LightOnModelTest teszt=new LightOnModelTest();
        
        teszt.tesztSetLepesekNegativ();
        teszt.tesztFelhasznaéloHelyes();
        teszt.tesztFelhasznaloUres();
    }

    private void tesztSetLepesekNegativ() {
        try {
            LightOnModel model = new LightOnModel();
            boolean hibas = false;
            try {
                model.setLepesek(-1);
            } catch (IllegalArgumentException e) {
                hibas = true;
            }
            assert hibas : "Negatív lépésszám nem dobott kivételt";
            System.out.println("tesztSetLepesekNegativ sikeres");
        } catch (AssertionError e) {
            System.err.println("tesztSetLepesekNegativ hiba: " + e.getMessage());
        }
    }

    private void tesztFelhasznaéloHelyes() {
         try {
            LightOnModel model = new LightOnModel();
            model.setFelhasznalo("Anna");
            assert model.getFelhasznalo().equals("Anna") : "Felhasználónév nem mentődött";
            System.out.println("tesztFelhasznaloHelyes sikeres");
        } catch (AssertionError e) {
            System.err.println("tesztFelhasznaloHelyes hiba: " + e.getMessage());
        }
    }
    
    private void tesztFelhasznaloUres() {
        try {
            LightOnModel model = new LightOnModel();
            model.setFelhasznalo("");
            assert model.getFelhasznalo().equals("Ismeretlen") : "Üres név nem kezelt";
            System.out.println("tesztFelhasznaloUres sikeres");
        } catch (AssertionError e) {
            System.err.println("tesztFelhasznaloUres hiba: " + e.getMessage());
        }
    }
    
    
    
    
    
}

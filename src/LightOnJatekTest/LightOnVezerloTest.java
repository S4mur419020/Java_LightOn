package LightOnJatekTest;

import Controll.LightONJatekVezerlo;
import Model.LightOnModel;
import Nezet.LightOnKinezet;
import javax.swing.JButton;


public class LightOnVezerloTest {

    public static void main(String[] args) {
        LightOnModel model = new LightOnModel();
        LightOnKinezet nezet = new LightOnKinezet(); 
        LightONJatekVezerlo vezerlo = new LightONJatekVezerlo(nezet, model);

        tesztFelhasznaloBeallitas(model, nezet, vezerlo);
        tesztUjJatek(model, nezet, vezerlo);
        tesztLampakapcsolas(model, nezet, vezerlo);

        System.out.println("Az összes vezérlő teszt lefutott!");
    }

    private static void tesztFelhasznaloBeallitas(LightOnModel model, LightOnKinezet nezet, LightONJatekVezerlo vezerlo) {
        try {
            nezet.getFelhasznalonev().setText("Tesztelő");
            nezet.getBtnOK().doClick();
            assert model.getFelhasznalo().equals("Tesztelő") : "Felhasználó beállítás hibás";
            System.out.println(" tesztFelhasznaloBeallitas sikeres");
        } catch (AssertionError e) {
            System.err.println("tesztFelhasznaloBeallitas hiba: " + e.getMessage());
        }
    }

    private static void tesztUjJatek(LightOnModel model, LightOnKinezet nezet, LightONJatekVezerlo vezerlo) {
        try {
            nezet.getBtnUjJatek().doClick(); 
            assert model.getLepesek() == 0 : "Lépésszám nem 0 új játék után";
            assert nezet.getFelhasznalonev().isEditable() : "Felhasználónév mező nem editable új játék után";
            System.out.println("tesztUjJatek sikeres");
        } catch (AssertionError e) {
            System.err.println("tesztUjJatek hiba: " + e.getMessage());
        }
    }

    private static void tesztLampakapcsolas(LightOnModel model, LightOnKinezet nezet, LightONJatekVezerlo vezerlo) {
        try {
            
            nezet.getFelhasznalonev().setText("Tesztelő");
            nezet.getBtnOK().doClick();

           
            JButton gomb = nezet.getjButton2();
            boolean eredeti = model.isLightOn(0, 0);
            gomb.doClick();
            assert model.isLightOn(0, 0) != eredeti : "Lámpa állapota nem változott";
            System.out.println("tesztLampakapcsolas sikeres");
        } catch (AssertionError e) {
            System.err.println(" tesztLampakapcsolas hiba: " + e.getMessage());
        }
    }
}

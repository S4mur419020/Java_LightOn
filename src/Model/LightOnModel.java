package Model;

public class LightOnModel {
    private boolean[][] lampak;
    private int lepesek;
    private String Felhasznalo;
    
     public LightOnModel() {
        try {
            ujraGomb();
        } catch (Exception e) {
            System.err.println("Hiba az inicializálás során: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void ujraGomb(){
        lampak=new boolean[3][3];
        lampak[1][1]=true;
        lepesek=0;
        Felhasznalo="";
    }
    
    private void valtas(int sor, int oszlop) {
        lampak[sor][oszlop] = !lampak[sor][oszlop];
    }
    
    public void lampaKapcsolo(int sor, int oszlop) {
    try {
        if (lampak == null) {
            throw new IllegalStateException("A lámpa tömb nincs inicializálva!");
        }

        if (sor < 0 || sor >= 3 || oszlop < 0 || oszlop >= 3) {
            throw new IndexOutOfBoundsException("Érvénytelen index: (" + sor + "," + oszlop + ")");
        }
        
        valtas(sor, oszlop);               
        if (sor > 0) valtas(sor - 1, oszlop); 
        if (sor < 2) valtas(sor + 1, oszlop); 
        if (oszlop > 0) valtas(sor, oszlop - 1); 
        if (oszlop < 2) valtas(sor, oszlop + 1); 

        lepesek++;

    } catch (Exception e) {
        System.err.println("Hiba a kapcsolás során: " + e.getMessage());
        e.printStackTrace();
    }
}

    public boolean[][] getLampak() {
        if (lampak == null) {
            throw new IllegalStateException("A lámpa tömb nincs beállítva!");
        }
        return lampak;
    }
    
     public boolean isLightOn(int sor, int oszlop){
        try {
            if (lampak == null) {
                throw new IllegalStateException("A lámpa tömb nincs inicializálva!");
            }
            if (sor < 0 || sor >= 3 || oszlop < 0 || oszlop >= 3) {
                throw new IndexOutOfBoundsException("Érvénytelen pozíció: (" + sor + "," + oszlop + ")");
            }
            return lampak[sor][oszlop];
        } catch (Exception e) {
            System.err.println("Hiba a lámpa állapot lekérdezésénél: " + e.getMessage());
            return false;
        }
    }
    
    public int getLepesek() {
        return lepesek;
    }

    public String getFelhasznalo() {
        return Felhasznalo == null ? "Nincs beállítva" : Felhasznalo;
    }
    public void ujrakalkulalas(){
        lepesek=0;
    }

    public void setFelhasznalo(String nev) {
         try {
            if (nev == null) {
                throw new IllegalArgumentException("A felhasználónév nem lehet null!");
            }
            if (nev.trim().isEmpty()) {
                throw new IllegalArgumentException("A felhasználónév nem lehet üres!");
            }
            this.Felhasznalo = nev.trim();
        } catch (IllegalArgumentException e) {
            System.err.println("Felhasználónév hiba: " + e.getMessage());
            this.Felhasznalo = "Ismeretlen";
        }
    } 
    
    public void setLampak(boolean[][] lampak) {
        if (lampak == null || lampak.length != 3 || lampak[0].length != 3) {
            throw new IllegalArgumentException("A lámpák két-dimenziós tömbjének 3x3 méretű kell lennie.");
        }
        for (int i = 0; i < 3; i++) {
            System.arraycopy(lampak[i], 0, this.lampak[i], 0, 3);
        }
    }

    public void setLepesek(int lepesek) {
        if (lepesek < 0) throw new IllegalArgumentException("A lépésszám nem lehet negatív.");
        this.lepesek = lepesek;
    }
}

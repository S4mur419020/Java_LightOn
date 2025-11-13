package Controll;

import Nezet.LightOnKinezet;
import Model.LightOnModel;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class LightONJatekVezerlo {
    private final LightOnKinezet nezet;
    private final LightOnModel model;
    private JButton[][] gombok;
    
    public LightONJatekVezerlo(LightOnKinezet nezet, LightOnModel model){
        this.model=model;
        this.nezet=nezet;
        init();
    }
    private void init() {
        try {
            gombok = new JButton[][]{
                {nezet.getjButton2(), nezet.getjButton10(), nezet.getjButton17()},
                {nezet.getjButton11(), nezet.getjButton12(), nezet.getjButton13()},
                {nezet.getjButton14(), nezet.getjButton15(), nezet.getjButton16()}
            };

            nezet.getBtnUjJatek().addActionListener(e -> ujraindit());
            nezet.getBtnOK().addActionListener(new Felhasznalo());

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    int sor = i;
                    int oszlop = j;
                    gombok[i][j].addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                if (nezet.getFelhasznalonev().isEditable()) {
                                    JOptionPane.showMessageDialog(
                                        nezet,
                                        "Előbb add meg a neved, mielőtt játszol!",
                                        "Figyelem",
                                        JOptionPane.WARNING_MESSAGE
                                    );
                                    return;
                                }

                                model.lampaKapcsolo(sor, oszlop);
                                frissit();
                            } catch (Exception ex) {
                                hibauzenet("Hiba történt a lámpa kapcsolásakor", ex);
                            }
                        }
                    });
                }
            }

            ujraindit();

        } catch (Exception e) {
            hibauzenet("Inicializálási hiba", e);
        }
    }
    private void ujraindit() {
        try {
            model.ujraGomb();

            nezet.getFelhasznalonev().setText("");
            nezet.getLblFelhasznalo().setText("");
            nezet.getLblSzamoltLepes().setText("0");

            nezet.getFelhasznalonev().setEditable(true);
            nezet.getBtnOK().setEnabled(true);

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    boolean vilagit = model.isLightOn(i, j);
                    gombok[i][j].setBackground(vilagit ? Color.YELLOW : new Color(204, 204, 204));
                }
            }

        } catch (Exception e) {
            hibauzenet("Hiba az új játék indításakor", e);
        }
    }
    
    private void frissit() {
        try {
            boolean[][] lampak = model.getLampak();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    gombok[i][j].setBackground(
                        lampak[i][j] ? Color.YELLOW : new Color(204, 204, 204)
                    );
                }
            }
            nezet.getLblSzamoltLepes().setText(String.valueOf(model.getLepesek()));

        } catch (Exception e) {
            hibauzenet("Hiba a nézet frissítése közben", e);
        }
    }

    private void hibauzenet(String uzenet, Exception e) {
        System.err.println(uzenet + ": " + e.getMessage());
        e.printStackTrace();
        JOptionPane.showMessageDialog(
            nezet,
            uzenet + "\n" + e.getMessage(),
            "Hiba",
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    
    
    
    private class Felhasznalo implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String nev = nezet.getFelhasznalonev().getText().trim();

                if (nev.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        nezet,
                        "A felhasználónév nem lehet üres!",
                        "Hiba",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                model.setFelhasznalo(nev);
                nezet.getLblFelhasznalo().setText(nev);

                nezet.getFelhasznalonev().setEditable(false);
                nezet.getBtnOK().setEnabled(false);

            } catch (Exception ex) {
                hibauzenet("Hiba a felhasználónév beállításakor", ex);
            }
        }
    }
}

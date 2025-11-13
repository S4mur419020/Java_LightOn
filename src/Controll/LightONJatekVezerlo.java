package Controll;

import Nezet.LightOnKinezet;
import Model.LightOnModel;
import java.awt.Color;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class LightONJatekVezerlo {

    private final LightOnKinezet nezet;
    private final LightOnModel model;
    private JButton[][] gombok;

    public LightONJatekVezerlo(LightOnKinezet nezet, LightOnModel model) {
        this.model = model;
        this.nezet = nezet;
        init();
    }

    private void init() {
        try {
            gombok = new JButton[][]{
                {nezet.getjButton2(), nezet.getjButton10(), nezet.getjButton17()},
                {nezet.getjButton11(), nezet.getjButton12(), nezet.getjButton13()},
                {nezet.getjButton14(), nezet.getjButton15(), nezet.getjButton16()}
            };
            nezet.getMnKilep().addActionListener(e -> kilepes());

            nezet.getJmnMentes().addActionListener(e -> {
                try {
                    String hely = System.getProperty("user.dir");
                    JFileChooser jfc = new JFileChooser(hely);

                    File kivalasztottFajl = new File(hely + "\\" + model.getFelhasznalo() + ".txt");
                    jfc.setSelectedFile(kivalasztottFajl);

                    int gomb = jfc.showSaveDialog(nezet);
                    if (gomb == JFileChooser.APPROVE_OPTION) {
                        kivalasztottFajl = jfc.getSelectedFile();
                        Path path = kivalasztottFajl.toPath();
                        Files.writeString(path, tartalom());
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(nezet, "Hiba a mentés során:\n" + ex.getMessage());
                    ex.printStackTrace();
                }
            });

            nezet.getBtnUjJatek().addActionListener(e -> ujraindit());
            nezet.getBtnOK().addActionListener(new Felhasznalo());

            nezet.getJmnBetoltes().addActionListener(e -> {
                JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
                int valasztas = jfc.showOpenDialog(nezet);

                if (valasztas == JFileChooser.APPROVE_OPTION) {
                    File fajl = jfc.getSelectedFile();
                    try {
                        var sorok = Files.readAllLines(fajl.toPath());

                        if (sorok.size() < 5) {
                            JOptionPane.showMessageDialog(nezet, "Érvénytelen mentés!");
                            return;
                        }

                        model.setFelhasznalo(sorok.get(0));
                        nezet.getLblFelhasznalo().setText(model.getFelhasznalo());

                        model.setLepesek(Integer.parseInt(sorok.get(1)));
                        nezet.getLblSzamoltLepes().setText(String.valueOf(model.getLepesek()));

                        boolean[][] lampak = new boolean[3][3];
                        for (int i = 0; i < 3; i++) {
                            String[] adatok = sorok.get(i + 2).split(",");
                            for (int j = 0; j < 3; j++) {
                                lampak[i][j] = adatok[j].equals("1");
                            }
                        }
                        model.setLampak(lampak);

                        frissit();

                        JOptionPane.showMessageDialog(nezet, "Betöltés sikeres!");
                    } catch (IOException | NumberFormatException | IndexOutOfBoundsException ex) {
                        JOptionPane.showMessageDialog(nezet, "Betöltés sikertelen!\n" + ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            });

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

    private void kilepes() {
        String uzenet = "Biztosan kilépsz?";
        String cim = "Kilépsz?";
        int valasz = JOptionPane.showConfirmDialog(nezet, uzenet, cim, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (valasz == JOptionPane.YES_OPTION) {
            System.exit(0);
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
            boolean nyert = true;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    boolean vilagit = lampak[i][j];
                    gombok[i][j].setBackground(
                            lampak[i][j] ? Color.YELLOW : new Color(204, 204, 204)
                    );
                    if (!vilagit) {
                        nyert = false;
                    }
                }
            }
            nezet.getLblSzamoltLepes().setText(String.valueOf(model.getLepesek()));

            if (nyert) {
                JOptionPane.showMessageDialog(nezet,
                        "Gratulálok " + model.getFelhasznalo() + "! Nyertél!", "Győzelem",
                        JOptionPane.INFORMATION_MESSAGE);
            }

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

    private String tartalom() {
        StringBuilder sb = new StringBuilder();
        sb.append(model.getFelhasznalo()).append("\n");
        sb.append(model.getLepesek()).append("\n");

        boolean[][] lampak = model.getLampak();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                sb.append(lampak[i][j] ? "1" : "0");
                if (j < 2) {
                    sb.append(",");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public void mentes() {
        String hely = System.getProperty("user.dir");
        JFileChooser jfc = new JFileChooser(hely);

        File kivalasztottFajl = new File(hely + "\\" + model.getFelhasznalo() + ".txt");
        jfc.setSelectedFile(kivalasztottFajl);

        int gomb = jfc.showSaveDialog(nezet);
        if (gomb == JFileChooser.APPROVE_OPTION) {
            kivalasztottFajl = jfc.getSelectedFile();
            Path path = Path.of(kivalasztottFajl.getAbsolutePath());
            try {
                Files.writeString(path, tartalom());
                JOptionPane.showMessageDialog(nezet, "Mentés sikeres!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(nezet, "IO hiba!\n" + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    public void betoltes() {
        JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
        int valasztas = jfc.showOpenDialog(nezet);

        if (valasztas == JFileChooser.APPROVE_OPTION) {
            File fajl = jfc.getSelectedFile();
            try {
                var sorok = Files.readAllLines(fajl.toPath());

                model.setFelhasznalo(sorok.get(0));
                model.setLepesek(Integer.parseInt(sorok.get(1)));

                boolean[][] lampak = new boolean[3][3];
                for (int i = 0; i < 3; i++) {
                    String[] adatok = sorok.get(i + 2).split(",");
                    for (int j = 0; j < 3; j++) {
                        lampak[i][j] = adatok[j].equals("1");
                    }
                }
                model.setLampak(lampak);

                nezet.getLblFelhasznalo().setText(model.getFelhasznalo());
                nezet.getLblSzamoltLepes().setText(String.valueOf(model.getLepesek()));
                frissit();

                JOptionPane.showMessageDialog(nezet, "Betöltés sikeres!");
            } catch (IOException | NumberFormatException | IndexOutOfBoundsException ex) {
                JOptionPane.showMessageDialog(nezet, "Betöltés sikertelen!\n" + ex.getMessage());
                ex.printStackTrace();
            }
        }
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

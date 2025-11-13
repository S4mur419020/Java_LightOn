package main;

import Nezet.LightOnKinezet;
import Model.LightOnModel;
import Controll.LightONJatekVezerlo;

public class LightOnJatek {

    public static void main(String[] args) {
        LightOnKinezet nezet=new LightOnKinezet();
        LightOnModel model=new LightOnModel();
        new LightONJatekVezerlo(nezet, model);
        nezet.setVisible(true);
        
    }
    
}

package net.pres.pres;

import net.pres.dao.DaoImpl;
import net.pres.metier.MetierImpl;

public class Pres1 {
    public static void main(String[] args) {
        MetierImpl metier=new MetierImpl();
        metier.setDao(new DaoImpl());
        System.out.println("RES ="+metier.calcul());
    }
}
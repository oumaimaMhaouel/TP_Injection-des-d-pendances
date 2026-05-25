package net.pres.pres;

import net.pres.dao.IDao;
import net.pres.metier.IMetier;
import net.pres.metier.MetierImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;

public class Pres2 {
    public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Scanner scanner = new Scanner(new File("config.txt"));
        String daoClasseName = scanner.next();
        Class cDao = Class.forName(daoClasseName);
        IDao dao = (IDao) cDao.newInstance();
        String metierClasseName = scanner.next();
        Class cMertier = Class.forName(metierClasseName);
        //IMetier metier = (IMetier) cMertier.getConstructor(IDao.class).newInstance(dao);
        IMetier metier=(IMetier) cMertier.getConstructor().newInstance();
        Method setDao=cMertier.getMethod("setDao", IDao.class);
        setDao.invoke(metier,dao);
        System.out.println("RES="+metier.calcul());
    }
}

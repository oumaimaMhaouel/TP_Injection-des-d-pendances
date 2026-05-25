package net.pres.pres;

import net.pres.metier.IMetier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class PresSpringAnnotation {


     public static void main(String[] args) {
         ApplicationContext applicationContext=new AnnotationConfigApplicationContext("net.pres");
         IMetier metier=applicationContext.getBean(IMetier.class);
         System.out.println("RES="+metier.calcul());
    }
}

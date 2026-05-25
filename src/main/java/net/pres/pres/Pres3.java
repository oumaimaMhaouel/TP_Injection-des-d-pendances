package net.pres.pres;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import net.pres.config.Configuration;
import net.pres.config.InjectionType;
import net.pres.config.IocAssembler;
import net.pres.metier.IMetier;

import java.io.InputStream;

/**
 * IoC manuelle : configuration dans {@code ioc-config.xml}, puis les trois modes d'injection.
 */
public class Pres3 {

    private static final String IOC_CONFIG = "ioc-config.xml";
    private static final String METIER_SETTER_CHAMP = "net.pres.metier.MetierImplSetter";

    public static void main(String[] args) throws JAXBException, ReflectiveOperationException {
        Configuration config = charger(IOC_CONFIG);
        String dao = config.getDao();

        IMetier metierConstructeur = IocAssembler.assemble(config);
        System.out.println("injection=constructeur RES=" + metierConstructeur.calcul());

        afficher(dao, "setter", InjectionType.SETTER);
        afficher(dao, "champ", InjectionType.CHAMP);
    }

    private static void afficher(String dao, String mode, InjectionType type)
            throws ReflectiveOperationException {
        IMetier metier = IocAssembler.assemble(dao, METIER_SETTER_CHAMP, type);
        System.out.println("injection=" + mode + " RES=" + metier.calcul());
    }

    private static Configuration charger(String resource) throws JAXBException {
        Unmarshaller unmarshaller = JAXBContext.newInstance(Configuration.class).createUnmarshaller();
        try (InputStream input = Pres3.class.getClassLoader().getResourceAsStream(resource)) {
            if (input == null) {
                throw new IllegalStateException(resource + " introuvable dans le classpath");
            }
            return (Configuration) unmarshaller.unmarshal(input);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }
}

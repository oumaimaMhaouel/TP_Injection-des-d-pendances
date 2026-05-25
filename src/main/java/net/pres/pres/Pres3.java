package net.pres.pres;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import net.pres.config.Configuration;
import net.pres.config.IocAssembler;
import net.pres.metier.IMetier;

import java.io.IOException;
import java.io.InputStream;

/**
 * IoC via OXM (JAXB) : {@code ioc-config.xml} choisit le mode d'injection
 * (constructeur, setter ou champ).
 */
public class Pres3 {

    private static final String[] EXEMPLES = {
            "ioc-config.xml",
            "ioc-config-setter.xml",
            "ioc-config-champ.xml"
    };

    public static void main(String[] args) throws JAXBException, ReflectiveOperationException {

        for (String fichier : EXEMPLES) {
            executer(fichier);
        }

    }

    private static void executer(String resource) throws JAXBException, ReflectiveOperationException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Configuration.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        try (InputStream input = Pres3.class.getClassLoader().getResourceAsStream(resource)) {
            if (input == null) {
                throw new IllegalStateException("Fichier " + resource + " introuvable dans le classpath");
            }
            Configuration configuration = (Configuration) unmarshaller.unmarshal(input);
            IMetier metier = IocAssembler.assemble(configuration);
            System.out.println("[" + resource + "] injection=" + configuration.getInjection()
                    + " RES=" + metier.calcul());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

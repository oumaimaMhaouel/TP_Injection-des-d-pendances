package net.pres.metier;

/**
 * Variante de {@link MetierImpl} pour l'IoC manuelle (Pres3) par setter ou champ :
 * constructeur sans argument, puis injection via {@link #setDao} ou réflexion sur {@code iDao}.
 */
public class MetierImplSetter extends MetierImpl {

    public MetierImplSetter() {
        super(null);
    }
}

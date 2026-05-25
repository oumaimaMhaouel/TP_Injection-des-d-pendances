package net.pres.metier;

import net.pres.dao.IDao;

/**
 * Couche métier sans Spring — injection manuelle (Pres1, Pres2, Pres3 / JAXB).
 */
public class MetierImpl extends MetierBase {

    public MetierImpl() {
    }

    public MetierImpl(IDao iDao) {
        this.iDao = iDao;
    }

    public void setDao(IDao iDao) {
        this.iDao = iDao;
    }
}

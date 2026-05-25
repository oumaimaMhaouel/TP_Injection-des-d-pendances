package net.pres.metier;

import net.pres.dao.IDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("metier")

public class MetierImpl implements IMetier {

    private IDao iDao;
    /**
     * Pour injecter dans l'attribut dao un objet d'une classe qui implémente l'interface IDao
     * au moment de l'instanciation
     */
    public MetierImpl(@Qualifier("d") IDao iDao) {
        this.iDao = iDao;
    }



    @Override
    public double calcul() {
        double temperature = iDao.getData();
        double res = temperature * 12 * Math.PI / 2 * Math.cos(temperature);
        return res;
    }

    /**
     * Pour injecter dans l'attribut dao un objet d'une classe qui implémente l'interface IDao
     * @param iDao
     */
    public void setDao(IDao iDao) {
        this.iDao = iDao;
    }
}

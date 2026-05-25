package net.pres.ext;

import net.pres.dao.IDao;

public class DaoImplV2 implements IDao {
    @Override
    public double getData() {
        System.out.println("version capteur");
        return 12.0;
    }
}

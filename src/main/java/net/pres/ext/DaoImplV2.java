package net.pres.ext;

import net.pres.dao.IDao;
import org.springframework.stereotype.Component;

@Component("dV2")

public class DaoImplV2 implements IDao {
    @Override
    public double getData() {
        System.out.println("version capteur");
        return 12.0;
    }
}

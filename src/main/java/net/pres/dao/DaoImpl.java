package net.pres.dao;

import org.springframework.stereotype.Component;

@Component("d")
public class DaoImpl implements IDao{
    @Override
    public double getData() {
        System.out.println("version base de données");
        double t =297l;
        return t;
    }
}

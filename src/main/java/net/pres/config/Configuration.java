package net.pres.config;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "configuration")
public class Configuration {

    public Configuration() {
    }

    private String dao;
    private String metier;
    private String injection;

    @XmlElement
    public String getDao() {
        return dao;
    }

    public void setDao(String dao) {
        this.dao = dao;
    }

    @XmlElement
    public String getMetier() {
        return metier;
    }

    public void setMetier(String metier) {
        this.metier = metier;
    }

    @XmlElement
    public String getInjection() {
        return injection;
    }

}

package net.pres.config;

import net.pres.dao.IDao;
import net.pres.metier.IMetier;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class IocAssembler {

    private IocAssembler() {
    }

    public static IMetier assemble(Configuration configuration) throws ReflectiveOperationException {
        Class<?> daoClass = Class.forName(configuration.getDao());
        IDao dao = (IDao) daoClass.getDeclaredConstructor().newInstance();

        Class<?> metierClass = Class.forName(configuration.getMetier());
        InjectionType type = InjectionType.fromXml(configuration.getInjection());

        return switch (type) {
            case CONSTRUCTEUR -> injectByConstructeur(metierClass, dao);
            case SETTER -> injectBySetter(metierClass, dao);
            case CHAMP -> injectByChamp(metierClass, dao);
        };
    }

    private static IMetier injectByConstructeur(Class<?> metierClass, IDao dao) throws ReflectiveOperationException {
        Constructor<?> constructor = metierClass.getConstructor(IDao.class);
        return (IMetier) constructor.newInstance(dao);
    }

    private static IMetier injectBySetter(Class<?> metierClass, IDao dao) throws ReflectiveOperationException {
        IMetier metier = (IMetier) metierClass.getDeclaredConstructor().newInstance();
        Method setDao = metierClass.getMethod("setDao", IDao.class);
        setDao.invoke(metier, dao);
        return metier;
    }

    private static IMetier injectByChamp(Class<?> metierClass, IDao dao) throws ReflectiveOperationException {
        IMetier metier = (IMetier) metierClass.getDeclaredConstructor().newInstance();
        Field field = findField(metierClass, "iDao");
        field.setAccessible(true);
        field.set(metier, dao);
        return metier;
    }

    private static Field findField(Class<?> type, String name) throws NoSuchFieldException {
        Class<?> current = type;
        while (current != null) {
            try {
                return current.getDeclaredField(name);
            } catch (NoSuchFieldException ignored) {
                current = current.getSuperclass();
            }
        }
        throw new NoSuchFieldException(name);
    }
}

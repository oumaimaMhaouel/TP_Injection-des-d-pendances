package net.pres.config;

public enum InjectionType {
    CONSTRUCTEUR,
    SETTER,
    CHAMP;

    public static InjectionType fromXml(String value) {
        if (value == null || value.isBlank()) {
            return SETTER;
        }
        return switch (value.trim().toLowerCase()) {
            case "constructeur", "constructor" -> CONSTRUCTEUR;
            case "setter" -> SETTER;
            case "champ", "field", "attribut" -> CHAMP;
            default -> throw new IllegalArgumentException("Type d'injection inconnu : " + value);
        };
    }
}

# TP — Inversion de contrôle (IoC)

Travaux pratiques **Master JEE** : inversion de contrôle et injection de dépendances.

## Les 5 présentations 

| # | Classe | Mécanisme |
|---|--------|-----------|
| **1** | `Pres1` | Sans IoC — instanciation directe (`new`) |
| **2** | `Pres2` | IoC manuelle — fichier texte `config.txt` + réflexion |
| **3** | `PresSpringXML` | IoC Spring — configuration **XML** (beans) |
| **4** | `PresSpringAnnotation` | IoC Spring — **annotations** (`@Component`, `@Autowired`) |
| **5** | `Pres3` | IoC manuelle — fichier **XML** + **JAXB** (OXM) |

## Objectifs

- Découpler la **présentation** (`pres`) du **métier** (`metier`) et du **DAO** (`dao`).
- Programmer contre les interfaces `IDao` et `IMetier`.
- Configurer les implémentations sans modifier le code client.

## Injection de dépendances (3 modes)

Utilisés à partir de la présentation **3** (Spring XML), **4** (annotations) et **5** (JAXB) :

| Mode | Description |
|------|-------------|
| **a. Constructeur** | Le DAO est passé au constructeur du métier |
| **b. Setter** | Le DAO est injecté via `setDao(IDao)` |
| **c. Champ (Field)** | Accès direct à l’attribut `iDao` |

| Mode | Spring XML (`PresSpringXML`) | Spring annotations (`PresSpringAnnotation`) | JAXB (`Pres3`) |
|------|------------------------------|--------------------------------------------|----------------|
| **a** | `config.xml` | `MetierImpl` (constructeur) | `ioc-config.xml` |
| **b** | `config-setter.xml` | idem + `setDao` | code (`MetierImplSetter`) |
| **c** | `config-field.xml` | idem + champ `iDao` | code (`MetierImplSetter`) |

## Prérequis

- JDK **17**
- Maven **3.x**

## Structure du projet

```
TP_inversion_de_controle/
├── config.txt
├── pom.xml
└── src/main/
    ├── java/net/pres/
    │   ├── config/          # JAXB + IocAssembler (Pres3)
    │   ├── dao/
    │   ├── ext/
    │   ├── metier/
    │   └── pres/
    │       ├── Pres1.java                 # 1
    │       ├── Pres2.java                 # 2
    │       ├── PresSpringXML.java         # 3
    │       ├── PresSpringAnnotation.java  # 4
    │       └── Pres3.java                 # 5 Mini projet
    └── resources/
        ├── config.xml / config-setter.xml / config-field.xml   # Spring (PresSpringXML)
        └── ioc-config.xml                                    # JAXB (Pres3)
```

## Modèle fonctionnel

| Composant | Rôle |
|-----------|------|
| `IDao` | Contrat d’accès aux données (`getData()`) |
| `DaoImpl` | Source « base de données » (`@Component("d")`) |
| `DaoImplV2` | Source « capteur » (`ext`) |
| `IMetier` | Contrat métier (`calcul()`) |
| `MetierImpl` | Métier sans Spring (Pres1, Pres2, Pres3 constructeur, Spring XML) |
| `MetierImplSetter` | Sous-classe avec constructeur vide — injection setter/champ (Pres3) |

### Changer d’implémentation DAO

| Fichier | Modification |
|---------|--------------|
| `config.txt` | 1ʳᵉ ligne : `net.pres.ext.DaoImplV2` |
| `ioc-config.xml` | `<dao>net.pres.ext.DaoImplV2</dao>` |
| Spring | Adapter le bean `d` ou `@Qualifier` |

---

## 1. `Pres1` — sans IoC

Couplage fort : le présentateur crée lui-même les objets.

```java
MetierImpl metier = new MetierImpl(new DaoImpl());
System.out.println("RES=" + metier.calcul());
```

Aucun fichier de configuration. Pour changer de DAO, il faut modifier le code source.

---

## 2. `Pres2` — IoC manuelle (fichier texte)

Lit `config.txt` à la **racine du projet** :

```text
net.pres.dao.DaoImpl
net.pres.metier.MetierImpl
```

- Charge les classes par réflexion (`Class.forName`).
- Injection par **setter** via `IocAssembler` (`setDao`).

Le **répertoire de travail** de la JVM doit être la racine du projet (sinon `config.txt` introuvable).

---

## 3. `PresSpringXML` — IoC Spring (configuration XML)

* `ClassPathXmlApplicationContext` charge le fichier  src/main/resources/config.xml :
```xml
<bean id="d" class="net.pres.dao.DaoImpl"/>
<bean id="metier" class="net.pres.metier.MetierImpl">
    <constructor-arg ref="d"/>
</bean>
```
* Bean d → DaoImpl
* Bean metier → MetierImpl, avec <constructor-arg ref="d"/>
Le contexte ClassPathXmlApplicationContext résout IMetier et injecte les dépendances.

---

## 4. `PresSpringAnnotation` — IoC Spring (annotations)

* @Component("d") sur DaoImpl, @Component("metier") sur MetierImpl.
* Scan du package `net.pres` : `AnnotationConfigApplicationContext("net.pres")`.
* Injection constructeur : @Qualifier("d") sur MetierImpl.

Pour basculer vers le capteur, il faudrait par exemple qualifier `DaoImplV2` et ajuster `@Qualifier` dans `MetierImpl`.


---

## 5. `Pres3` — IoC manuelle (XML + JAXB / OXM)

**OXM** (*Object XML Mapping*) : un seul fichier `ioc-config.xml` est lu avec **Jakarta XML Binding (JAXB)** dans la classe `Configuration`, puis `IocAssembler` assemble le métier par réflexion.

### Fichier `src/main/resources/ioc-config.xml`

```xml
<configuration>
    <dao>net.pres.dao.DaoImpl</dao>
    <metier>net.pres.metier.MetierImpl</metier>
    <injection>constructeur</injection>
</configuration>
```

| Balise | Rôle |
|--------|------|
| `<dao>` | Classe du DAO instanciée par réflexion |
| `<metier>` | Classe du métier pour le mode décrit par `<injection>` |
| `<injection>` | `constructeur`, `setter` ou `champ` (voir valeurs ci-dessous) |

Valeurs acceptées pour `<injection>` : `constructeur` / `constructor`, `setter`, `champ` / `field` / `attribut`.

### Déroulement de `Pres3`

1. Charger `ioc-config.xml` une fois (`charger` + JAXB).
2. **Constructeur** : `IocAssembler.assemble(config)` utilise le contenu du XML (`dao`, `metier`, `injection`).
3. **Setter** et **champ** : même `<dao>` que dans le XML, métier `MetierImplSetter` (constructeur sans argument requis pour `setDao` / accès au champ `iDao`).

| Mode | Classe métier | Mécanisme dans `IocAssembler` |
|------|---------------|-------------------------------|
| **a. Constructeur** | `MetierImpl` (via XML) | `getConstructor(IDao.class).newInstance(dao)` |
| **b. Setter** | `MetierImplSetter` (dans le code) | `newInstance()` + `setDao(dao)` |
| **c. Champ** | `MetierImplSetter` (dans le code) | `newInstance()` + `Field.set` sur `iDao` |

Sortie console attendue (trois lignes) :

```text
injection=constructeur RES=...
injection=setter RES=...
injection=champ RES=...
```

> `ioc-config.xml` (JAXB, Pres3) et `config.xml` (Spring, `PresSpringXML`) sont deux fichiers distincts.

---

## Compilation et exécution

```bash
mvn compile
```

Lancer les classes `main` **dans l’ordre du TP** :

| Ordre | Classe | Résultat attendu |
|-------|--------|------------------|
| 1 | `Pres1` | Une ligne `RES=...` |
| 2 | `Pres2` | `version base de données` + `RES=...` |
| 3 | `PresSpringXML` | Trois lignes (constructeur, setter, champ) |
| 4 | `PresSpringAnnotation` | Trois lignes (annotations) |
| 5 | `Pres3` | Trois lignes (`constructeur`, `setter`, `champ`) |

---

## Dépendances (`pom.xml`)

| Bibliothèque | Usage |
|--------------|--------|
| Spring 6.1.6 | `PresSpringXML`, `PresSpringAnnotation` |
| JAXB 4 | `Pres3` (OXM, hors JDK depuis Java 11) |

---

## Schéma (ordre des présentations)

```mermaid
flowchart LR
    P1[1. Pres1<br/>new] --> P2[2. Pres2<br/>config.txt]
    P2 --> P3[3. PresSpringXML<br/>config.xml]
    P3 --> P4[4. PresSpringAnnotation<br/>@Component]
    P4 --> P5[5. Pres3<br/>ioc-config + JAXB]
```

---

## Fichiers à ne pas versionner

- `target/` — classes compilées Maven
- `.idea/` — configuration IDE (selon équipe)

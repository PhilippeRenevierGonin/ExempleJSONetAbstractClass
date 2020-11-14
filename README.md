# ExempleJSONetAbstractClass
exemple d'utilisation d'annotations jackson pour la conversion (dans les deux sens) de type abstrait en JSON
Note : l'aspect test est sur un autre dépôt : https://github.com/PhilippeRenevierGonin/ExempleSocketIOClientServeurJava/tree/test

Avant de présenter les étapes, il y a des points particuluers à observer dans l'exemple : 
 - la synchro côté serveur entre le thread principal (d'exécution) et ceux de socketIO car il faut attendre les réponses des joueurs
 - cette synchro n'est pas nécessaire côté client : le thread principal lance la connexion, puis tout se fait sur les threads de socketIO
 - il y a un exemple de equals et hashCode dans Identification 
    - **REDÉFINIR equals** est nécessaire pour comparer des instances différentes mais avec les mêmes valeurs (cas de identification envoyé via le réseau)
    - Si on redéfinit equals, **IL FAUT REDÉFINIR hashCode()** car deux objets égaux doivent avoir le même hashcode.
 - il y a des toString pour permettre un affichage plus facilement des objet


## étape 00 : initialisation du projet 
ajout du .gitignore, configuration du projet maven multimodule. 
ajout de dépendances, laisons entre les modules.

## étape 01 : connexion, handshake évolué
le client se connecte au serveur
le client s'identifie une fois la connexion établie
le moteur (dans le serveur) accepte le joueur
le serveur envoie fin de partie (le client a gagné)

Exécution : 
 - lancer le serveur et le joueur dans deux terminaux différents (exemple cd serveur puis mvn exec:java )
 -  ou lancer le lanceur  ( cd lanceur puis mvn exec:java )
 - _Note : le module lanceur n'est pas nécessaire ni obligatoire_

## étape 02 : on passe à deux clients
juste pour voir l'acceptation des deux clients. Le premier connecté gagne. 
Quelques points particuliers : 
 - <cleanupDaemonThreads>false</cleanupDaemonThreads> : dans le pom de joueur ou de lanceur, car des threads de OkHttp (utilisé par socketIO) ont dû mal à se fermer
 - le lancement de la partie quand 2 joueurs sont connectés nécessite une synchro entre le serveur (exécution du main) et la réception des messages via socketIO
 - la méthode equals est implémentée dans Identification, pour comparer sur les valeurs des propriétés d'Identification (et non pas sur les adresses mémoires comme c'est le cas par défaut)
 - le nom du joueur est aléatoire et le niveau du joueur sont aléatoires

Exécution : 
 - lancer le serveur et 2 joueurs dans trois terminaux différents (exemple cd serveur puis mvn exec:java )
 -  ou lancer le lanceur  ( cd lanceur puis mvn exec:java )
 - _Note : le module lanceur n'est pas nécessaire ni obligatoire_
 
 
## étape 03 :  BUG : Étape qui plante : on essaie de jouer un tour : les joueurs retourne une action ()
Cela plante car il y a le problème de déserialisaiton d'une Action. C'est le but de cet exemple. Dans l'étape suivante, on règle le problème 
Une fois les joeurs connecter, le jeu va faire une tour : le serveur demande à chaque joueur (séquentiellement) son action en lui envoyant son Inventaire (nouvelle classe), puis le joueur répond quelle Action (nouvelle classe) il veut faire.
Côté Joueur, il y a une IA (qui fait toujours la même action pour l'instant) et qui la renvoie. 

## étape 04 : les clients envoient une Action et le serveur la reçoit 
Plusieurs points importants liés à Jackson, librairie utilisé par SocketIO côté serveur

### Annotation Jacksion 
Dans Action on rajoute les annotations suivantes :   
 ```java
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ActionRisquée.class, name = "ActionRisquée"),
        @JsonSubTypes.Type(value = ActionSure.class, name = "ActionSure")
})
public abstract class Action { /* ... */ }
```
 la première ligne dit de rajouter le type concret dans le json (un type est rajouté dans le json). L'envoi d'une Action ressemble à {"type":"ActionSure","joueur":{"nom":"DémoPlayer","niveau":1}}
 La deuxième ligne permet d'associer la classe concrète, utile pour la désérialisation (json -> java)
 
 Pour cela, il faut ajouter uné dépendance dans le pom de donnees : 
```xml
    <dependencies>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-annotations</artifactId>
            <version>2.9.7</version> <!--  version utilisée par socketIOserveur https://github.com/mrniko/netty-socketio/blob/641d64ad95c60177f629dd16cef461f9dd09875b/pom.xml -->
        </dependency>
    </dependencies>
```
 Dans lancer, EssaiJackson permet d'essayer les différentes conversions (il n'y a pas de dépendance à ajouter, elles le sont par transitivité via client et serveur).
 
### Il faut aider SocketIO (côté client)
 Côté client, pour l'envoi, il faut utiliser un ObjectMapper (objet de Jackson) pour la prise en compte des annotations : 
 ```java
    try {
            String json = jackson.writeValueAsString(pj);
            System.err.println(json);
            connexion.emit(Message.JOUER_CETTE_ACTION, json);
        } catch (JsonProcessingException e) {
            controleur.transfèreMessage("erreur avec jackson...");
        }
```
pour cela il faut ajouter une dépendance dans le pom de joueur
```xml
    <dependencies>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.9.7</version> <!--  version utilisée par socketIOserveur https://github.com/mrniko/netty-socketio/blob/641d64ad95c60177f629dd16cef461f9dd09875b/pom.xml -->
        </dependency>
    </dependencies>
```

### Il faut aider SocketIO (côté serveur)
L'utilisation en interne de Jackson dans SocketIO ne permet de pas de déserialiser correctement. Alors, on contourne le problème : on déclare recevoir une String, cela sera le json au format texte. Puis on utilise jackson nous même.
 ```java
        // réception du choix d'une action
        // IMPORTANT : en interne, la deserialisation pause problème, du coup on passe par une chaine qu'on désérialise à la main
        serveur.addEventListener(Message.JOUER_CETTE_ACTION, String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient socketIOClient, String action, AckRequest ackRequest) throws Exception {
                synchronized (synchro) {
                    // il y a un problème en interne à SocketIo, on l'aide un peu, on fait la conversion nous même
                    Action a = jackson.readValue(action, Action.class);
                    controleur.transfèreAction(a);
                }
            }
        });
```
Il n'y a pas de dépendance à ajouter côté serveur, c'est déjà présent dans SocketIO. 

### Implémentation côté moteur/serveur
L'appel des joueurs est séquentiel : 
 - après la connexion des deux joueurs, on lance un tour
 - un tour se passe de la façon suivante (_c'est sur le thread du serveur_) 
    - on demande l'action d'un joueur
    - on attend sa réponse (wait) --> sendEvent / Message.DEMANDE_DE_JOUER
    - _le sendEvent est effectué sur le thread de socketIO_
    - **quand** on reçoit sa réponse (onData sur Message.JOUER_CETTE_ACTION --> notify) (_c'est sur le thread de socketIO_)
    - **alors** on la vérifie (ce n'est pas implémenté, cela retourne tjs true)  (_c'est sur le thread du serveur_) 
    - **et si** c'est ok (tjs le cas), on l'applique
    - IMPORTANT : on ne demande qu'un un joueur à la fois
    - puis on passe à l'autre joueur (ou c'est la fin du tour)
Pour la fin de partie, on affiche l'état des joueurs (qui font tous la même action). Le gagnant est toujours mal désigné (c'est le 1er trouvé). 

### Implémentation côté joueur/client
Cela s'exécute sur les threads de socketIO :
 - On attend un message Message.DEMANDE_DE_JOUER
 - à sa réception on demande au bot de choisir son action
 - on renvoie l'action via un emit Message.JOUER_CETTE_ACTION

Cela se lance comme avant (1 moteur/serveur + 2 joueurs/clients OU lanceur). 


##étape 05 : les clients envoient une Action et le serveur la reçoit 
On fait un 2e type de BOT, qui fait l'ActionRisquée. 
Côté serveur, on compare les scores (l'égalité n'est pas gérée).
Le serveur reçoit bien les deux types d'action (mais ne sait pas la classe concrète).

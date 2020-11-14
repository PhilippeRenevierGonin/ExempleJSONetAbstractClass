# ExempleJSONetAbstractClass
exemple d'utilisation d'annotations jackson pour la conversion (dans les deux sens) de type abstrait en JSON

Note : l'aspect test est sur un autre dépôt : https://github.com/PhilippeRenevierGonin/ExempleSocketIOClientServeurJava/tree/test

##étape 00 : initialisation du projet 
ajout du .gitignore, configuration du projet maven multimodule. 
ajout de dépendances, laisons entre les modules.

##étape 01 : connexion, handshake évolué
le client se connecte au serveur
le client s'identifie une fois la connexion établie
le moteur (dans le serveur) accepte le joueur
le serveur envoie fin de partie (le client a gagné)

Exécution : 
 - lancer le serveur et le joueur dans deux terminaux différents (exemple cd serveur puis mvn exec:java )
 -  ou lancer le lanceur  ( cd lanceur puis mvn exec:java )
 - _Note : le module lanceur n'est pas nécessaire ni obligatoire_

##étape 02 : on passe à deux clients
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
 
 
##étape 03 :  BUG : Étape qui plante : on essaie de jouer un tour : les joueurs retourne une action ()
Cela plante car il y a le problème de déserialisaiton d'une Action. C'est le but de cet exemple. Dans l'étape suivante, on règle le problème 
Une fois les joeurs connecter, le jeu va faire une tour : le serveur demande à chaque joueur (séquentiellement) son action en lui envoyant son Inventaire (nouvelle classe), puis le joueur répond quelle Action (nouvelle classe) il veut faire.
Côté Joueur, il y a une IA (qui fait toujours la même action pour l'instant) et qui la renvoie. 


 
   
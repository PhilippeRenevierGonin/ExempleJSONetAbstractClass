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

##étape 02 : on passe à deux clients
juste pour voir l'acceptation des deux clients. Le premier connecté gagne. 
# Projet d'un Tetris en Java avec Swing
Ce projet est une implémentation du célèbre jeu Tetris en Java avec Swing, alliant les fonctionnalités de base du Tetris à des extensions innovantes. Découvrez les aspects clés de notre implémentation (*pour plus d'informations consulter le rapport*) :

## Fonctionnalités

### Paramétrage du jeu
Nous avons introduit un menu permettant aux joueurs de lancer différentes parties, accéder au leaderboard, ajuster les options sonores et quitter le jeu.

### Mouvement et rotation des pièces
Le jeu offre un mouvement fluide et une rotation précise des pièces grâce à l'utilisation d'un clone pour vérifier les collisions, garantissant une expérience de jeu sans accroc.

### Partie solo
Dans le mode solo, les joueurs peuvent profiter de sessions individuelles où l'objectif est de jouer jusqu'à ce que les pièces atteignent le sommet de la grille, en réalisant le meilleur score possible.

### Pause
À tout moment, les joueurs peuvent mettre leur jeu en pause, avec la possibilité de régler les options, continuer la partie, la recommencer ou la quitter.

### Score
Accumulez des points à chaque ligne détruite, avec pour objectif d'obtenir le meilleur score et de figurer dans le leaderboard compétitif.

## Extensions

### Niveaux de difficultés
Les niveaux de difficulté ajustent l'intensité du jeu en modifiant la vitesse de descente des pièces en fonction du score du joueur, offrant une progression graduelle du défi.

### Sauvegarde des meilleurs scores
Les meilleurs scores sont sauvegardés dans un leaderboard consultable depuis le menu, ajoutant un élément compétitif et incitatif à améliorer constamment ses résultats.

### Mode multijoueur
Dans ce mode, les joueurs peuvent affronter simultanément un autre joueur ou notre IA "Golmon". L'objectif est de supprimer 15 lignes pour remporter la partie, ajoutant une dimension compétitive et stratégique au jeu.

### Rendu graphique et sonore
Le rendu graphique comprend quatre vues distinctes, coordonnées par un JFrame, offrant une expérience visuelle fluide. Trois musiques distinctes accompagnent le jeu, ajustables depuis le menu options.

### Implémentation d’une IA (Golmon)
Notre IA, "Golmon", offre une expérience stratégique en solo et en multijoueur. Son fonctionnement repose sur des méthodes tactiques, offrant aux joueurs la possibilité de l'observer ou de la défier dans un mode multijoueur compétitif.

## Installation

1. Assurez-vous d'avoir Java installé sur votre machine.
2. Clonez ce dépôt vers votre machine locale.
   ```bash
   git clone https://github.com/miryamatmm/projet-LIFAPOO.git

## Compilation et éxécution 
```bash
cd projet-LIFAPOO
javac VC.java
java VC

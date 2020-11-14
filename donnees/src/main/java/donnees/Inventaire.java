package donnees;

public class Inventaire {

    private int points;

    public Inventaire() {
        setPoints(0);
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    /**
     * méthode pratique pour ajouter des points
     * @param nbPointÀAjouter le nombre de points à ajouter
     */
    public void ajouterPoints(int nbPointÀAjouter) {
        setPoints(getPoints()+nbPointÀAjouter);
    }


    public String toString() {
        return "[nbPoints : "+getPoints()+"]";
    }
}

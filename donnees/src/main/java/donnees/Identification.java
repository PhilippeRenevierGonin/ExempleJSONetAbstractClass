package donnees;

public class Identification {

    private String nom ;
    private int niveau;

    public Identification() {
        this("", 0);
    }

    public Identification(String nom, int level) {
        this.nom = nom;
        niveau = level;
    }


    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getNiveau() {
        return niveau;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }



    public boolean equals(Object o) {
        if (o instanceof Identification) {
            Identification id = (Identification) o;
            return  (getNom().equals(id.getNom()) && (getNiveau() == id.getNiveau())) ;
        }
        else return false;
    }

    /**
     * utilise pour les hashmap.
     * on se base sur le hashcode du toString, unique pour un joueur dans le moteur (c.f. equals ci-dessus)
     * @return le hash code de l'id
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    public String toString() {
        return getNom()+" ["+getNiveau()+"]";
    }
}

package moteur.vue;


import app.Vue;

public class VueServeur implements Vue {


    public VueServeur() {

    }


    public void afficheMessage(String msg) {
        System.out.println("Serveur>"+msg);

    }


    public void finit() {
        System.out.println("Serveur> on s'arrête !");
    }

    public void afficheMessageErreur(String s) {
        System.err.println("Serveur>"+s);
    }
}

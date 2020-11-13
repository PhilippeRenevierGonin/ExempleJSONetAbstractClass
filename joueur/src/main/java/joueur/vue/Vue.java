package joueur.vue;

import joueur.Client;

public class Vue {

    private final Client client;

    public Vue(Client client) {
        this.client = client;
        client.setVue(this);
    }


    public void afficheMessage(String msg) {
        System.out.println(client.getIdentification()+"> "+msg);

    }


    public void finit() {
        System.out.println(client.getIdentification()+"> on est déconnecté !");
    }

    public void afficheMessageErreur(String s) {
        System.err.println(client.getIdentification()+"> "+s);
    }
}

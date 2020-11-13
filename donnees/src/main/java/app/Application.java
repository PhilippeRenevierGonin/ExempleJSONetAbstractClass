package app;

public class Application {

    protected Vue vue;

    public void transfèreMessage(String msg) {
        getVue().afficheMessage(msg);
    }
    public void signaleErreur(String err) {
        getVue().afficheMessageErreur(err);
    }

    public void setVue(Vue vue) {
        this.vue = vue;
    }

    public Vue getVue() {
        return vue;
    }

}



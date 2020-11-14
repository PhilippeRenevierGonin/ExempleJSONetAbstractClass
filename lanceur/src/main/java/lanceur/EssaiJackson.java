package lanceur;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import donnees.Identification;
import donnees.action.Action;
import donnees.action.ActionRisquée;
import donnees.action.ActionSure;

public class EssaiJackson {

    public static void main(String [] args) throws Exception {
        ObjectMapper jackson = new ObjectMapper();
        Identification j = new Identification("toto", 99);

        String json = jackson.writeValueAsString(new ActionRisquée(j));
        jsonToObj(jackson, json);


        json = "{\"type\":\"ActionSure\",\"joueur\":{\"nom\":\"DémoPlayer\",\"niveau\":1}}";
        jsonToObj(jackson, json);


        json = "{\"type\":\"ActionSure\",\"joueur\":{\"nom\":\"DÃ©moPlayer\",\"niveau\":1}}";
        jsonToObj(jackson, json);

    }

    private static void jsonToObj(ObjectMapper jackson, String json) throws java.io.IOException {
        System.out.println(json);
        Action a = jackson.readValue(json, Action.class);
        System.out.println(a);
        System.out.println(a instanceof Action);
        System.out.println(a instanceof ActionRisquée);
        System.out.println(a instanceof ActionSure);
    }
}

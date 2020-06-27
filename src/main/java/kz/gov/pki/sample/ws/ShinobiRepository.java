package kz.gov.pki.sample.ws;

import kz.gov.pki.sample.soap.Shinobi;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class ShinobiRepository {

    private static final Map<String, Shinobi> shinobies = new HashMap<>();

    @PostConstruct
    public void initData() {
        Shinobi naruto = new Shinobi();
        naruto.setName("Naruto");
        naruto.setClan("Uzumaki");
        naruto.setJutsu("Rasenshuriken");
        shinobies.put(naruto.getName(), naruto);
        Shinobi sasuke = new Shinobi();
        sasuke.setName("Sasuke");
        sasuke.setClan("Uchiha");
        sasuke.setJutsu("Chidori");
        shinobies.put(sasuke.getName(), sasuke);
        Shinobi itachi = new Shinobi();
        itachi.setName("Itachi");
        itachi.setRogue(true);
        itachi.setClan("Uchiha");
        itachi.setJutsu("Amaterasu");
        shinobies.put(itachi.getName(), itachi);
        Shinobi kakashi = new Shinobi();
        kakashi.setName("Kakashi");
        kakashi.setClan("Hatake");
        kakashi.setJutsu("Sennen Goroshi");
        shinobies.put(kakashi.getName(), kakashi);
    }

    public Shinobi fetchShinobi(String name) {
        return shinobies.get(name);
    }

}

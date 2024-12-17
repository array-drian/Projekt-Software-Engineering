import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;

@ManagedBean(name = "greetingBean")
@ViewScoped
public class GreetingBean implements Serializable {
    
    private String name;      // Eingabewert vom Benutzer
    private String greeting;  // Ausgabewert für die Begrüßung
    
    // Getter und Setter für 'name'
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter für 'greeting'
    public String getGreeting() {
        return greeting;
    }

    // Aktion zur Erstellung der Begrüßung
    public String sayHello() {
        if (name != null && !name.trim().isEmpty()) {
            greeting = "Hallo, " + name + "! Willkommen bei JSF!";
        } else {
            greeting = "Bitte gib einen Namen ein.";
        }
        return null; // Bleibt auf der gleichen Seite
    }
}
package es.pagoru.planesimulatorx.windows.menus;

import es.pagoru.planesimulatorx.Window;
import es.pagoru.planesimulatorx.input.KeyListenerEvent;
import es.pagoru.planesimulatorx.utils.Vector3Di;
import es.pagoru.planesimulatorx.windows.MenuWindow;
import es.pagoru.planesimulatorx.windows.MenuWindows;
import es.pagoru.planesimulatorx.windows.menus.cockpit.Plane;

import java.awt.event.KeyEvent;
import java.util.HashMap;

/**
 * Created by Pablo on 30/10/2016.
 */
public class CreatePlaneMenuWindow extends MenuWindow {

    /**
     * Mapa de contingut de les opcions per completar el formulari de creació d'avions.
     */
    private HashMap<String, String> optionsText = new HashMap<>();

    /**
     * Mida del contingut del mapa.
     */
    private int emptyOptions = optionsText.size();
    
    public CreatePlaneMenuWindow() {
        super("CreatePlane");
        clearOptionsText();
    }

    /**
     * Neteja les opcions disponibles en el mapa i asigna les basiques.
     */
    private void clearOptionsText(){
        optionsText.clear();
        String[] options = {"[d]", "[e]", "[f]", "[g]", "[h]", "[i]", "[j]", "[k]", "[l]"};
        for (String opt :
                options) {
            optionsText.put(opt, "______________________________________");
        }
        emptyOptions = optionsText.size();
    }

    /**
     * Quan es selecciona i es prem una opció, es permet la escriptura en el camp de text,
     * comprovant si es pot escriure text o números.
     * a, b i c: Retornan el event.
     * d, e, f, g: Fan les funcions del formulari on es pot introduir qualsevol caracter.
     * h, i, j, k, l: Fan les funcions del formulari en las que només es pot introduir numeració.
     * @param keyListenerEvent
     */
    public void onKeyEvent(KeyListenerEvent keyListenerEvent){
        switch (getCurrentSelection()){
            case "[a]":
            case "[b]":
            case "[c]":
                return;
            case "[h]":
            case "[i]":
            case "[j]":
            case "[k]":
            case "[l]":
                if(keyListenerEvent.getKeyCode() >= 'A'
                        && keyListenerEvent.getKeyCode() <= 'Z'
                        || keyListenerEvent.getKeyCode() == ' '){
                    return;
                }
                break;
        }

        String currentSel = optionsText.get(getCurrentSelection());

        if(keyListenerEvent.getKeyCode() == KeyEvent.VK_DELETE){
            String newCurrentSel = currentSel.replaceAll("_", "");
            if(newCurrentSel.length() > 0){
                newCurrentSel = newCurrentSel.substring(0, newCurrentSel.length() - 1);
                for (int i = 0; i <= (currentSel.length() - currentSel.replace("_", "").length()); i++) {
                    newCurrentSel += "_";
                }
                currentSel = newCurrentSel;
            }
        } else {
            currentSel = currentSel.replaceFirst("_", (char)keyListenerEvent.getKeyCode() + "");
        }

        optionsText.put(getCurrentSelection(), currentSel);
        draw();
    }

    /**
     * Retorna un integer parsejat en funció del camp escrit amb un limit de 6 digits.
     * @param option
     * @return
     */
    private int getPureIntegerFromOption(String option){
        String fp = optionsText.get(option).replace("_", "");
        if(fp.length() == 0){
            return 0;
        }
        return Integer.parseInt(fp.substring(0, (fp.length() > 6 ? 6 : fp.length())));
    }

    /**
     * Al premer la selecció del menú actual, s'executa una funció.
     * a: Es comproven si els camps del formulari son correctes i es crea un nou avió.
     * b: Neteja el text dels camps del formulari.
     * c: Tanca el menú.
     */
    @Override
    public void openCurrentSelection() {
        switch (getCurrentSelection()) {
            case "[a]":
                optionsText.keySet().stream().forEach(opt -> {
                    if(optionsText.get(opt).replaceAll("_", "").length() == 0){
                        emptyOptions--;
                    }
                });
                if(emptyOptions != 0){
                    ((CockpitMenuWindow) MenuWindows.getMenuWindow("Cockpit")).addPlane(
                            new Plane(
                                    optionsText.get("[d]").replaceAll("_", ""),
                                    optionsText.get("[e]").replaceAll("_", ""),
                                    optionsText.get("[f]").replaceAll("_", ""),
                                    optionsText.get("[g]").replaceAll("_", ""),
                                    getPureIntegerFromOption("[h]"),
                                    getPureIntegerFromOption("[i]"),
                                    new Vector3Di(
                                            getPureIntegerFromOption("[j]"),
                                            getPureIntegerFromOption("[k]"),
                                            getPureIntegerFromOption("[l]")
                                    )
                            )
                    );
                }
                clearOptionsText();
                draw();
                break;
            case "[b]":
                clearOptionsText();
                draw();
                break;
            case "[c]":
                MenuWindows.closeLastMenu();
                break;
        }
    }

    /**
     * Mostra per pantalla el menu actual.
     */
    @Override
    public void draw() {
        String newRawWindow = getRawWindow();
        for (String opt : optionsText.keySet()) {
            newRawWindow = newRawWindow.replaceAll("@(" + opt + ")\\{(.*?)\\}", optionsText.get(opt));
        }
        
        Window.getInstance().loadWindow(
                newRawWindow
                .replace(selections.get(currentSelection), "[*]")
                .replaceAll("\\[([a-z])\\]", "[ ]")
                .replaceAll("@([a-z])\\{(.*?)\\}", "______________________________________")
                .replaceAll("#([a-z])\\{(.*?)\\}", "                              ")
                .replaceAll("(&)", " ")
        );
    }
    
}
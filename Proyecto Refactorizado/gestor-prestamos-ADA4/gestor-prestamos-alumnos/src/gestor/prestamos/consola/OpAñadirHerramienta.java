package gestor.prestamos.consola;

/**
 * Representa la opción de añadir una herramienta.
 */
public class OpAñadirHerramienta extends OpMenu {

    public OpAñadirHerramienta(MenuConsola menu) {
        super(menu);
    }

    @Override
    public boolean ejecutar() {
        menu.anadirHerramienta();
        return false;
    }
}

package gestor.prestamos.consola;

/**
 * Representa la opción de dar de baja una herramienta.
 */
public class OpDarBaja extends OpMenu {

    public OpDarBaja(MenuConsola menu) {
        super(menu);
    }

    @Override
    public boolean ejecutar() {
        menu.darDeBaja();
        return false;
    }
}

package gestor.prestamos.consola;

/**
 * Representa la opción de registrar un préstamo.
 */
public class OpPrestar extends OpMenu {

    public OpPrestar(MenuConsola menu) {
        super(menu);
    }

    @Override
    public boolean ejecutar() {
        menu.prestar();
        return false;
    }
}

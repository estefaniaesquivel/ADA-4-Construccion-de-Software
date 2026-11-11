package gestor.prestamos.consola;

/**
 * Representa la opción de consultar el historial de préstamos.
 */
public class OpVerHistorial extends OpMenu {

    public OpVerHistorial(MenuConsola menu) {
        super(menu);
    }

    @Override
    public boolean ejecutar() {
        menu.verHistorial();
        return false;
    }
}

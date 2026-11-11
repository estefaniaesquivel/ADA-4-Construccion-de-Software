package gestor.prestamos.consola;

/**
 * Representa la opción de registrar una devolución.
 */
public class OpDevolver extends OpMenu {

    public OpDevolver(MenuConsola menu) {
        super(menu);
    }

    @Override
    public boolean ejecutar() {
        menu.devolver();
        return false;
    }
}

package gestor.prestamos.consola;

/**
 * Representa la opción de reactivar una herramienta.
 */
public class OpReactivar extends OpMenu {

    public OpReactivar(MenuConsola menu) {
        super(menu);
    }

    @Override
    public boolean ejecutar() {
        menu.reactivar();
        return false;
    }
}

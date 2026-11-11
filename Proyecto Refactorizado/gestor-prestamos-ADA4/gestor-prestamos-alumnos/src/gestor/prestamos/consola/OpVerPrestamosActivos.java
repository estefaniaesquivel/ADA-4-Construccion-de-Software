package gestor.prestamos.consola;

/**
 * Representa la opción de consultar los préstamos activos.
 */
public class OpVerPrestamosActivos extends OpMenu {

    public OpVerPrestamosActivos(MenuConsola menu) {
        super(menu);
    }

    @Override
    public boolean ejecutar() {
        menu.verPrestamosActivos();
        return false;
    }
}

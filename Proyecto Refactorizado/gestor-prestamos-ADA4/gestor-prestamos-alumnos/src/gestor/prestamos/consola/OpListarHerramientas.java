package gestor.prestamos.consola;

/**
 * Representa la opción de consultar las herramientas registradas.
 */
public class OpListarHerramientas extends OpMenu {

    public OpListarHerramientas(MenuConsola menu) {
        super(menu);
    }

    @Override
    public boolean ejecutar() {
        menu.listarHerramientas();
        return false;
    }
}

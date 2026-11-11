package gestor.prestamos.consola;

/**
 * opción de salir de la aplicación.
 * TECNICA: REPLACE CONDITIONAL WITH POLYMORPHISM.
 * Esta clase encapsula el comportamiento de la opción 0.
 */
public class OpSalir extends OpMenu {

    public OpSalir(MenuConsola menu) {
        super(menu);
    }

    @Override
    public boolean ejecutar() {
        return true;
    }
}

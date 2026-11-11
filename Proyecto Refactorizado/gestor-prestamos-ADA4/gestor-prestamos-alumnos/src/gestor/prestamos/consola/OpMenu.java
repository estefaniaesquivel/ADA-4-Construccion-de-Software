package gestor.prestamos.consola;

/**
 * esta será una clase abstracta que represente una opción del menú.
 * TECNICA:REPLACE CONDITIONAL WITH POLYMORPHISM.
 *
 * Se establece un método abstracto ejecutar() que deberá será implementado por cada opciónn del menú
 * así, MenuConsola ya no necesita utilizar un switch para determinar qué operación debe realizar.
 */
public abstract class OpMenu {

    protected final MenuConsola menu;

    /**
     * Recibe el menú que contiene las operaciones que ejecutará cada opción
     * @param menu instancia del menú principal
     */
    public OpMenu(MenuConsola menu) {
        this.menu = menu;
    }

    /**
     * Ejecuta la operación correspondiente a la opción del menú
     * @return true si se debe salir de la aplicación; false en caso contrario
     */
    public abstract boolean ejecutar();
}

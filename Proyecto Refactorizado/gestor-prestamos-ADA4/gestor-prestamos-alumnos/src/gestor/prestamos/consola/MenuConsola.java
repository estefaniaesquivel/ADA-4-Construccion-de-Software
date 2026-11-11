package gestor.prestamos.consola;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import gestor.prestamos.modelo.EstadoPrestamo;
import gestor.prestamos.modelo.Herramienta;
import gestor.prestamos.modelo.PeriodoPrestamo;
import gestor.prestamos.modelo.Prestamo;
import gestor.prestamos.persistencia.RepositorioCsv;
import gestor.prestamos.servicio.GestorPrestamos;

/**
 * Menú principal de la aplicación. Muestra las opciones, pide los datos necesarios
 * y delega las operaciones en el servicio. Tras cada cambio, guarda los datos.
 */
public final class MenuConsola {

    private static final String MENU = """

            ========== GESTOR DE PRÉSTAMOS ==========
              1. Ver herramientas y disponibilidad
              2. Añadir herramienta
              3. Prestar herramienta
              4. Registrar devolución
              5. Ver préstamos activos
              6. Ver historial y penalizaciones
              7. Dar de baja una herramienta
              8. Reactivar herramienta
              0. Salir
            =========================================
            """;

    private static final List<Integer> OPCIONES = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8);

    private final GestorPrestamos gestor;
    private final RepositorioCsv repositorio;
    private final Entrada entrada;

    // TECNICA: REPLACE CONDITIONAL WITH POLYMORPHISM.
    // Se utiliza un mapa para relacionar cada número del menú con un objeto
    // que contiene el comportamiento correspondiente a esa opción.
    private final Map<Integer, OpMenu> opcionesMenu;

    /**
     * Crea el menú con sus dependencias.
     *
     * @param gestor servicio de préstamos
     * @param repositorio repositorio donde se guardan los datos
     * @param entrada lectura de datos desde la consola
     */
    public MenuConsola(GestorPrestamos gestor, RepositorioCsv repositorio, Entrada entrada) {
        this.gestor = gestor;
        this.repositorio = repositorio;
        this.entrada = entrada;

        // TECNICA: REPLACE CONDITIONAL WITH POLYMORPHISM
        // cada número se relaciona con una instancia de su clase concreta
        // Todas las clases heredan de OpcionMenu, pero cada una implementa su propio comportamiento con el método ejecutar()
        // Map.of permite crear un mapa con sus elementos iniciales, Significa que cuando busquemos la clave 3,
        // obtendremos un objeto de tipo OpcionPrestar.
        this.opcionesMenu = Map.of( 
                0, new OpSalir(this), //hacemos (this) para enviar la instancia actual de MenuConsola al constructor de OpSalir
                1, new OpListarHerramientas(this),
                2, new OpAñadirHerramienta(this),
                3, new OpPrestar(this),
                4, new OpDevolver(this),
                5, new OpVerPrestamosActivos(this),
                6, new OpVerHistorial(this),
                7, new OpDarBaja(this),
                8, new OpReactivar(this)
        );
    }

    /**
     * Muestra el menú en bucle hasta que el usuario elige salir. Los errores de
     * negocio se muestran sin cerrar la aplicación
     */
    public void ejecutar() {
        boolean salir = false;
        while (!salir) {
            System.out.println(MENU);
            int opcion = entrada.pedirOpcion("Elige una opción: ", OPCIONES);
            try {
                salir = ejecutarOpcion(opcion);
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("  Error: " + e.getMessage());
            }
        }
        System.out.println("¡Hasta luego!");
    }

    /**
     * Ejecuta la opción elegida.
     *
     * TECNICA: REPLACE CONDITIONAL WITH POLYMORPHISM.
     * Se eliminó la estructura switch que determinaba qué método ejecutar.
     * Ahora se obtiene el objeto asociado al número seleccionado y se delega la operación en su método ejecutar().
     *
     * @param opcion número de opción del menú
     * @return {@code true} si el usuario quiere salir de la aplicación
     */
    private boolean ejecutarOpcion(int opcion) {
        OpMenu accion = opcionesMenu.get(opcion);
        return accion.ejecutar();
    }

    // Se modifica la visibilidad de private a package-private para permitir
    // que las clases OpcionMenu concretas puedan invocar estas operaciones.
    // No se cambia la implementación original de los métodos.

    void listarHerramientas() {
        List<Herramienta> herramientas = gestor.getHerramientas();
        if (herramientas.isEmpty()) {
            System.out.println("\n  No hay herramientas registradas.");
            return;
        }
        System.out.println("\n  ID  Herramienta             Estado");
        System.out.println("  " + "-".repeat(60));
        for (Herramienta herramienta : herramientas) {
            Optional<Prestamo> activo = gestor.prestamoActivo(herramienta);
            String estado = activo
                    .map(p -> "Prestada a " + p.getPrestatario() + " (hasta " + p.getFechaLimite() + ")")
                    .orElse(herramienta.isDadaDeBaja() ? "Dada de baja" : "Disponible");
            System.out.printf("  %-4d%-24s%s%n", herramienta.getId(), herramienta.getNombre(), estado);
        }
    }

    void darDeBaja() {
        List<Herramienta> candidatas = gestor.getHerramientas().stream()
                .filter(h -> !h.isDadaDeBaja())
                .toList();
        if (candidatas.isEmpty()) {
            System.out.println("\n  No hay herramientas que dar de baja.");
            return;
        }
        System.out.println("\n  Herramientas en uso:");
        for (Herramienta herramienta : candidatas) {
            System.out.printf("    %d. %s%n", herramienta.getId(), herramienta.getNombre());
        }
        List<Integer> ids = candidatas.stream().map(Herramienta::getId).toList();
        int id = entrada.pedirOpcion("ID de la herramienta a dar de baja: ", ids);
        Herramienta elegida = candidatas.stream()
                .filter(h -> h.getId() == id)
                .findFirst()
                .orElseThrow();
        gestor.darDeBaja(id);
        guardar();
        System.out.printf("  '%s' dada de baja. Su historial se conserva.%n", elegida.getNombre());
    }

    void reactivar() {
        List<Herramienta> dadas = gestor.getHerramientas().stream()
                .filter(Herramienta::isDadaDeBaja)
                .toList();
        if (dadas.isEmpty()) {
            System.out.println("\n  No hay herramientas dadas de baja.");
            return;
        }
        System.out.println("\n  Herramientas dadas de baja:");
        for (Herramienta herramienta : dadas) {
            System.out.printf("    %d. %s%n", herramienta.getId(), herramienta.getNombre());
        }
        List<Integer> ids = dadas.stream().map(Herramienta::getId).toList();
        int id = entrada.pedirOpcion("ID de la herramienta a reactivar: ", ids);
        Herramienta elegida = dadas.stream()
                .filter(h -> h.getId() == id)
                .findFirst()
                .orElseThrow();
        gestor.reactivar(id);
        guardar();
        System.out.printf("  '%s' reactivada y disponible para préstamo.%n", elegida.getNombre());
    }

    void anadirHerramienta() {
        String nombre = entrada.pedirTexto("Nombre de la herramienta: ");
        Herramienta nueva = gestor.anadirHerramienta(nombre);
        guardar();
        System.out.printf("  Herramienta '%s' añadida con ID %d.%n", nueva.getNombre(), nueva.getId());
    }

    void prestar() {
        List<Herramienta> disponibles = gestor.herramientasDisponibles();
        if (disponibles.isEmpty()) {
            System.out.println("\n  No hay herramientas disponibles para prestar.");
            return;
        }

        //TECNICA: EXTRACT METHOD 
        // (SE APLICO A LOS SIGUIENTES 3 BLOQUES DE CODIGO: PETICION DE FECHAS, SELECCION DE HERRAMIENTA Y REGISTRO DEL PRESTAMO)
        //  CADA BLOQUE SE EXTRAE A UN METODO PRIVADO NUEVO.
        //NOTA: Solo cambia la clase MenuConsola ya que los métodos nuevos son privados, así que no afecta a Main

        //DESPUES DE EXTRAER LOS METODOS AHORA SI, SE LLAMAN DENTRO DE LA FUNCIÓN PRESTAR()
        //ERESPETANDO EL PRINCIPIO DE RESPONSABILIDAD UNICA

        //Se extrajó la seleccion de la herramienta a un metodo privado llamado seleccionarHerramienta()
        int id = seleccionarHerramienta(disponibles);
        String prestatario = entrada.pedirTexto("Nombre de quien la recibe: ");
        //La peticion de las fechas se extrajo a pedirPeriodo(), que devuelve el PeriodoPrestamo ya armado
        PeriodoPrestamo periodo = pedirPeriodo();
        //El registro del prestamo (prestar, guardar y confirmar) se extrajo a registrarPrestamo()
        registrarPrestamo(id, prestatario, periodo);
    }

    // TECNICA EXTRACT METHOD (SELECCION DE HERRAMIENTA)
    /**
     * Muestra las herramientas disponibles y pide al usuario que elija una.
     *
     * @param disponibles herramientas que pueden prestarse; no puede estar vacía
     * @return el identificador de la herramienta elegida
     */
    private int seleccionarHerramienta(List<Herramienta> disponibles) {
        System.out.println("\n  Herramientas disponibles:");
        for (Herramienta herramienta : disponibles) {
            System.out.printf("    %d. %s%n", herramienta.getId(), herramienta.getNombre());
        }
        List<Integer> ids = disponibles.stream().map(Herramienta::getId).toList();
        return entrada.pedirOpcion("ID de la herramienta: ", ids);
    }

    // TECNICA EXTRACT METHOD (PETICION DE FECHAS)
    private PeriodoPrestamo pedirPeriodo() {
        LocalDate hoy = LocalDate.now();
        LocalDate inicio = entrada.pedirFecha("Fecha de préstamo (AAAA-MM-DD, Enter = " + hoy + "): ", hoy);
        LocalDate limitePorDefecto = inicio.plusDays(GestorPrestamos.DIAS_PRESTAMO_DEFECTO);
        LocalDate limite = entrada.pedirFecha(
                "Fecha límite (AAAA-MM-DD, Enter = " + limitePorDefecto + "): ", limitePorDefecto);

        // Tecnica: Introduce Parameter Object.
        // Se empaquetan las fechas de inicio y limite en PeriodoPrestamo antes de solicitar el prestamo.
        return new PeriodoPrestamo(inicio, limite);
    }

    // TECNICA EXTRACT METHOD (REGISTRO DEL PRESTAMO)
    private void registrarPrestamo(int herramientaId, String prestatario, PeriodoPrestamo periodo) {
        Prestamo nuevo = gestor.prestar(herramientaId, prestatario, periodo);
        guardar();
        System.out.printf("  '%s' prestada a %s hasta %s.%n",
                nuevo.getHerramienta().getNombre(), nuevo.getPrestatario(), nuevo.getFechaLimite());
    }

    void devolver() {
        List<Prestamo> activos = gestor.prestamosActivos();
        if (activos.isEmpty()) {
            System.out.println("\n  No hay préstamos pendientes de devolución.");
            return;
        }
        System.out.println("\n  Préstamos activos:");
        for (Prestamo p : activos) {
            System.out.printf("    %d. %s - %s (límite %s)%n",
                    p.getId(), p.getHerramienta().getNombre(), p.getPrestatario(), p.getFechaLimite());
        }
        List<Integer> ids = activos.stream().map(Prestamo::getId).toList();
        int id = entrada.pedirOpcion("ID del préstamo a devolver: ", ids);

        LocalDate hoy = LocalDate.now();
        LocalDate fecha = entrada.pedirFecha("Fecha de devolución (AAAA-MM-DD, Enter = " + hoy + "): ", hoy);

        // Tecnica: Separate Query from Modifier.
        // Primero se invoca al modificador para registrar la devolucion y despues a la consulta para obtener el prestamo.
        gestor.devolver(id, fecha);
        Prestamo devuelto = gestor.buscarPrestamo(id);
        guardar();
        System.out.println("  Devolución registrada.");
        if (devuelto.getDiasRetraso() > 0) {
            System.out.printf("  Retraso de %d día(s). Penalización: %.2f €%n",
                    devuelto.getDiasRetraso(), devuelto.getPenalizacion());
        } else {
            System.out.println("  Devuelta a tiempo, sin penalización.");
        }
    }

    void verPrestamosActivos() {
        List<Prestamo> activos = gestor.prestamosActivos();
        if (activos.isEmpty()) {
            System.out.println("\n  No hay préstamos activos.");
            return;
        }
        //TECNICA REPLACE TEMP WITH QUERY
        //La variable temporal "hoy" (LocalDate hoy = LocalDate.now()) se asignaba una sola vez, asi que se
        //sustituyo por la consulta fechaActual(). Cada lugar donde se usaba "hoy" ahora llama a esa consulta
        //fechaActual() se puso debajo de esta funcion para que sea mas facil de encontrar
        //la refac replace temp with query no involucró cambios en otras clases, ya que la variable 
        //temporal "hoy" solo se usaba en esta funcion y no se pasaba a otras clases
        System.out.println("\n  Préstamos activos:");
        for (Prestamo p : activos) {
            //TECNICA: DECOMPOSE CONDITIONAL
            //El if/else mezclaba el calculo del retraso con la construccion del texto. Se extrajeron las condiciones a
            //métodos privados nuevos: la condicion (estaAtrasado), y si cumple ejecuta (describirAtraso) y si no 
            // ejecuta (describirAlDia), de modo que solo se lee que se decide y que se muestra en cada caso.
            //NOTA: se colocaron debajo de fechaActual() para que sea más facil buscarlos
            //Esto fue posible gracias a fechaActual() (que se hizo con Replace Temp with Query), ya que
            //los metodos nuevos obtienen la fecha por si mismos y no hace falta pasarles "hoy" como parametro
            String situacion;
            if (estaAtrasado(p)) {
                situacion = describirAtraso(p);
            } else {
                situacion = describirAlDia(p);
            }
            System.out.printf("    %s - %s | desde %s | %s%n",
                    p.getHerramienta().getNombre(), p.getPrestatario(), p.getFechaPrestamo(), situacion);
        }
    }

    //TECNICA REPLACE TEMP WITH QUERY (MÉTODO DE CONSULTA PARA FECHA ACTUAL)
    private LocalDate fechaActual() {
        return LocalDate.now();
    }

    //TECNICA DECOMPOSE CONDITIONAL (METODO PARA CONDICION SI ESTÁ ATRASADO)
    private boolean estaAtrasado(Prestamo prestamo) {
        return prestamo.estado(fechaActual()) == EstadoPrestamo.ATRASADO;
    }

    //TECNICA DECOMPOSE CONDITIONAL (METODO PARA CONDICION QUE DESCRIBE EL ATRASO)
    private String describirAtraso(Prestamo prestamo) {
        long dias = prestamo.getDiasRetrasoA(fechaActual());
        double estimada = prestamo.calcularPenalizacion(fechaActual(), Prestamo.PENALIZACION_POR_DIA); //Se cambió GestorPrestamo por Prestamo.PENALIZACION_POR_DIA
        return String.format("ATRASADO %d día(s), penalización estimada %.2f €", dias, estimada);
    }

    // TECNICA DECOMPOSE CONDITIONAL (METODO PARA CONDICION QUE DESCRIBE QUE EL PRESTAMO ESTÁ AL DÍA)
    private String describirAlDia(Prestamo prestamo) {
        return "Al día, vence " + prestamo.getFechaLimite();
    }

    void verHistorial() {
        List<Prestamo> historial = gestor.historial();
        if (historial.isEmpty()) {
            System.out.println("\n  No hay devoluciones registradas.");
            return;
        }
        System.out.println("\n  Historial de devoluciones:");
        for (Prestamo p : historial) {
            System.out.printf("    %s - %s | %s -> %s | retraso %d día(s) | penalización %.2f €%n",
                    p.getHerramienta().getNombre(), p.getPrestatario(), p.getFechaPrestamo(),
                    p.getFechaDevolucion(), p.getDiasRetraso(), p.getPenalizacion());
        }
        System.out.printf("  Total de penalizaciones cobradas: %.2f €%n", gestor.totalPenalizaciones());
    }

    private void guardar() {
        try {
            repositorio.guardarHerramientas(gestor.getHerramientas());
            repositorio.guardarPrestamos(gestor.getPrestamos());
        } catch (IOException e) {
            System.out.println("  Aviso: no se pudieron guardar los datos: " + e.getMessage());
        }
    }
}

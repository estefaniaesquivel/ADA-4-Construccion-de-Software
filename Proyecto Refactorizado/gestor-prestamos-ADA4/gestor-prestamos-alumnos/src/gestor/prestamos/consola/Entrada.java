package gestor.prestamos.consola;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;
import java.util.Scanner;

/**
 * Lectura de datos desde la consola con validación. Repite la petición hasta
 * obtener un valor correcto.
 */
public final class Entrada {

    private final Scanner scanner;

    /**
     * Crea una entrada que lee del escáner indicado.
     *
     * @param scanner fuente de las líneas introducidas por el usuario; no puede ser nula
     */
    public Entrada(Scanner scanner) {
        this.scanner = Objects.requireNonNull(scanner, "scanner");
    }

    /**
     * Pide un texto no vacío.
     *
     * @param mensaje texto que se muestra antes de leer
     * @return el texto introducido, sin espacios sobrantes
     */
    public String pedirTexto(String mensaje) {
        while (true) {
            String valor = leer(mensaje).trim();
            if (!valor.isEmpty()) {
                return valor;
            }
            System.out.println("  El campo no puede estar vacío.");
        }
    }

    /**
     * Pide un número entero que debe pertenecer a un conjunto de opciones válidas.
     *
     * @param mensaje texto que se muestra antes de leer
     * @param validas números aceptados
     * @return el número elegido
     */
    public int pedirOpcion(String mensaje, Collection<Integer> validas) {
        while (true) {
            String texto = leer(mensaje).trim();
            try {
                int numero = Integer.parseInt(texto);
                if (validas.contains(numero)) {
                    return numero;
                }
            } catch (NumberFormatException e) {
                // El texto no es un número: se informa abajo y se vuelve a pedir.
            }
            System.out.println("  Opción no válida.");
        }
    }

    /**
     * Pide una fecha en formato {@code AAAA-MM-DD}. Si el usuario pulsa Enter y hay un valor
     * por defecto, se devuelve ese valor.
     *
     * @param mensaje texto que se muestra antes de leer
     * @param valorPorDefecto fecha que se usa al pulsar Enter, o {@code null} si no hay
     * @return la fecha introducida o el valor por defecto
     */
    public LocalDate pedirFecha(String mensaje, LocalDate valorPorDefecto) {
        while (true) {
            String texto = leer(mensaje).trim();
            if (texto.isEmpty() && valorPorDefecto != null) {
                return valorPorDefecto;
            }
            // Se aplica Replace Exception with Test verificando primero si el formato es válido en lugar de usar try-catch
            if (!esFechaValida(texto)) { // Prueba condicional previa para evitar lanzar DateTimeParseException
                System.out.println("  Formato inválido. Usa AAAA-MM-DD."); // Se informa del error sin recurrir a excepciones
            } else { // Si pasa la prueba condicional, es seguro convertir el texto a fecha
                return LocalDate.parse(texto); // Se parsea la fecha sabiendo que cumple el formato y rango
            }
        }
    }

    // Método auxiliar de prueba para validar formato AAAA-MM-DD y rangos de calendario antes de parsear
    private boolean esFechaValida(String texto) {
        if (!texto.matches("\\d{4}-\\d{2}-\\d{2}")) { // Verifica con una expresión regular que tenga estructura AAAA-MM-DD
            return false; // No cumple con el patrón básico de dígitos y guiones
        }
        String[] partes = texto.split("-"); // Separa el año, mes y día para validar sus rangos
        int anio = Integer.parseInt(partes[0]); // Obtiene el valor numérico del año
        int mes = Integer.parseInt(partes[1]); // Obtiene el valor numérico del mes
        int dia = Integer.parseInt(partes[2]); // Obtiene el valor numérico del día
        if (mes < 1 || mes > 12) { // Comprueba que el mes esté entre 1 y 12
            return false; // Mes fuera de rango válido
        }
        int maxDias = java.time.Month.of(mes).length(java.time.Year.isLeap(anio)); // Calcula los días máximos del mes (considerando años bisiestos)
        return dia >= 1 && dia <= maxDias; // Devuelve true solo si el día existe dentro de ese mes y año
    }

    private String leer(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }
}

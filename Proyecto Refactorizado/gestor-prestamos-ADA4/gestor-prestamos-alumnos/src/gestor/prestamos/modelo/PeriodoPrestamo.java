package gestor.prestamos.modelo;

import java.time.LocalDate;

/**
 * Periodo de un préstamo: agrupa la fecha en que se presta la herramienta y la fecha
 * máxima de devolución. Las dos fechas viajan y se validan siempre juntas.
 */
// Tecnica: Introduce Parameter Object.
// Las fechas de inicio y limite del prestamo viajan juntas y comparten reglas de
// validacion; se encapsulan en PeriodoPrestamo para simplificar las firmas de Prestamo y GestorPrestamos.
public final class PeriodoPrestamo {

    private final LocalDate fechaInicio;
    private final LocalDate fechaLimite;

    /**
     * Crea un periodo de préstamo validando que ambas fechas existan y sean coherentes.
     *
     * @param fechaInicio fecha en que se presta la herramienta; no puede ser nula
     * @param fechaLimite fecha máxima de devolución; no puede ser nula ni anterior al inicio
     * @throws IllegalArgumentException si alguna fecha es nula o la fecha límite es anterior a la de inicio
     */
    public PeriodoPrestamo(LocalDate fechaInicio, LocalDate fechaLimite) {
        if (fechaInicio == null || fechaLimite == null) {
            throw new IllegalArgumentException("Las fechas de préstamo y límite son obligatorias");
        }
        if (fechaLimite.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha límite no puede ser anterior al préstamo");
        }
        this.fechaInicio = fechaInicio;
        this.fechaLimite = fechaLimite;
    }

    /**
     * Devuelve la fecha de inicio del préstamo.
     *
     * @return fecha en que se presta la herramienta
     */
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Devuelve la fecha máxima de devolución del préstamo.
     *
     * @return fecha límite del préstamo
     */
    public LocalDate getFechaLimite() {
        return fechaLimite;
    }
}

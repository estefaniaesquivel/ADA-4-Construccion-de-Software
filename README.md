# Universidad Autónoma de Yucatán
## Facultad de Matemáticas
### Asignatura: Construcción de Software — Actividad de Aprendizaje 4

- **Valor:** 6 pts
- **Forma de trabajo:** Grupal

---

## 1. Objetivo

El programa de esta actividad funciona correctamente, pero su diseño tiene problemas que dificultan leerlo y mantenerlo. Tu tarea es identificar esos problemas y corregirlos aplicando técnicas de refactorización, sin cambiar el comportamiento que el usuario percibe. Para comprobarlo, usarás las pruebas de caja negra del Anexo A.

---

## 2. Procedimiento

1. Guarda una copia del proyecto original. La necesitarás para comparar.
2. Ejecuta las pruebas del Anexo A sobre el programa original y registra el resultado de cada una como Pasa o No pasa (agrega una columna más a la tabla de pruebas).
3. Lee el código y localiza los problemas de diseño relacionados con la tabla de la sección 3.
4. Aplica las técnicas una por una. Después de cada cambio, compila y vuelve a ejecutar todas las pruebas del Anexo A de modo que valides que la funcionalidad sigue intacta.
5. Si una prueba que antes pasaba ahora falla, la técnica ha cambiado el comportamiento: corrígela antes de continuar.
6. Redacta en el código (comentarios) la técnica utilizada y una breve explicación de los cambios realizados, justamente en el lugar donde estés aplicando dicha técnica.

---

## 3. Técnicas a aplicar

Debes aplicar las siguientes diez técnicas. La columna «Dónde buscar» indica el punto de partida, pero debes confirmar el problema en el código antes de cambiarlo.

| # | Técnica | Unidad | Archivo | Dónde buscar |
| :-: | :--- | :--- | :--- | :--- |
| 1 | Extract Method | Composición de métodos | `consola/MenuConsola.java` | Método `prestar()`: separar la selección de herramienta, la petición de fechas y el registro del préstamo. |
| 2 | Replace Temp with Query | Composición de métodos | `consola/MenuConsola.java` | Método `verPrestamosActivos()`: la variable `hoy` puede sustituirse por un método que obtenga la fecha. |
| 3 | Introduce Explaining Variable | Composición de métodos | `persistencia/RepositorioCsv.java` | Método `parsearHerramienta()`: la condición que decide si la herramienta está dada de baja necesita un nombre. |
| 4 | Move Method | Movimiento de funciones entre objetos | `servicio/GestorPrestamos.java` y `modelo/Prestamo.java` | Cálculo de la penalización al devolver: decide en qué clase debe estar. |
| 5 | Separate Query from Modifier | Simplificación de llamadas a métodos | `servicio/GestorPrestamos.java` | Método `devolver()`: separa la modificación del préstamo de la consulta de lo que se devuelve. |
| 6 | Remove Parameter | Simplificación de llamadas a métodos | `modelo/Prestamo.java` | Método `registrarDevolucion()`: revisa si todos sus parámetros son necesarios. |
| 7 | Replace Exception with Test | Simplificación de llamadas a métodos | `consola/Entrada.java` | Método `pedirFecha()`: la excepción se usa para decidir si la fecha es válida. |
| 8 | Introduce Parameter Object | Simplificación de llamadas a métodos | `servicio/GestorPrestamos.java` y `modelo/Prestamo.java` | Fechas de préstamo y límite que viajan juntas en varios métodos. |
| 9 | Decompose Conditional | Simplificación de expresiones condicionales | `consola/MenuConsola.java` | Método `verPrestamosActivos()`: el bloque condicional mezcla cálculo y presentación. |
| 10 | Replace Conditional with Polymorphism | Simplificación de expresiones condicionales | `consola/MenuConsola.java` | Método `ejecutarOpcion()`: el `switch` decide el comportamiento según la opción elegida. |

---

## 4. Reglas

- No cambies el comportamiento visible: el menú, los mensajes, el formato de los archivos CSV y los cálculos de penalización deben seguir igual.
- Aplica cada técnica con un cambio pequeño y compila después de cada uno.
- Dale nombre a métodos y variables que digan qué hacen, no cómo lo hacen.
- *No* elimines funcionalidades para simplificar el código.
- Si cambias la firma de un método, actualiza su Javadoc.

---

## 5. Pruebas de caja negra

Las pruebas del Anexo A tratan el programa como una caja negra: indican qué introducir por teclado y qué debe aparecer en pantalla o en los archivos, sin mirar el código. Su objetivo es comprobar que la refactorización no ha cambiado el comportamiento.

- Cada prueba indica una precondición (el estado de los datos antes de empezar), la secuencia de entradas y el resultado esperado.
- Algunas pruebas dependen de la fecha de cuando se realice la prueba. Donde aparece «hoy» o «(Enter)», el resultado debe comprobarse con la fecha en que ejecutas la prueba.
- Para preparar una precondición, puedes crear los archivos CSV a mano con el contenido necesario, o llegar a ese estado con las mismas opciones del menú.
- Registra el resultado de cada prueba como Pasa o No pasa (creando una nueva columna), con la fecha de ejecución y, si falla, lo que mostró el programa.

---

## 6. Entrega

1. Repositorio que incluya:
   - El proyecto original y el proyecto refactorizado, compilable y con las mismas funcionalidades que el original.
   - Documento en PDF con el registro de pruebas del Anexo A, con el resultado antes y después de la refactorización.

---

## 7. Criterios de evaluación

| Criterio | Peso |
| :--- | :-: |
| Aplicación correcta de las diez técnicas | 40 % |
| El programa pasa las pruebas del Anexo A (antes y después) | 30 % |
| Repositorio en GitHub | 15 % |
| Comentarios en el código donde se aplicaron las técnicas | 15 % |

---

## Anexo A. Pruebas de caja negra

Las pruebas están agrupadas por opción del menú. Cada una indica su precondición, las entradas en orden y el resultado esperado.

### Caso general y opciones inválidas, y opción 0

| ID | Precondición | Entrada | Resultado esperado |
| :--- | :--- | :--- | :--- |
| CP-G-01 | Ninguna | `9`, `abc`, `(vacío)`, luego `0` | `"Opción no válida."` en cada entrada inválida y vuelve a pedir la opción |
| CP-G-02 | Ninguna | Fin de entrada (cerrar stdin) | `"Entrada finalizada."` y termina sin errores |
| CP-G-03 | Ninguna | `2`, `Taladro`, `0`; reiniciar; `1` | Taladro aparece en el listado tras reiniciar |
| CP-00-01 | Ninguna | `0` | `"¡Hasta luego!"` y el programa termina |

### Opción 1: Ver herramientas y disponibilidad

| ID | Precondición | Entrada | Resultado esperado |
| :--- | :--- | :--- | :--- |
| CP-01-01 | Sin herramientas | `1` | `"No hay herramientas registradas."` |
| CP-01-02 | Taladro activa; Escalera dada de baja | `1` | Taladro: Disponible. Escalera: Dada de baja |
| CP-01-03 | Taladro prestada a Ana hasta `2026-11-12` | `1` | Taladro: `"Prestada a Ana (hasta 2026-11-12)"` |

### Opción 2: Añadir herramienta

| ID | Precondición | Entrada | Resultado esperado |
| :--- | :--- | :--- | :--- |
| CP-02-01 | Ninguna | `2`, `Taladro` | `"añadida con ID 1"`; se guarda `1,Taladro,false` |
| CP-02-02 | Ninguna | `2`, `(vacío)`, `Escalera` | `"El campo no puede estar vacío."`; después se añade con ID 1 |
| CP-02-03 | Ninguna | `2`, `(solo espacios)`, `Escalera` | Igual que CP-02-02 |
| CP-02-04 | Dos herramientas existentes | `2`, `Sierra` | Se asigna ID 3 |
| CP-02-05 | Ninguna | `2`, `" Escalera "` | Se muestra `"Escalera"` sin espacios sobrantes |

### Opción 3: Prestar herramienta

| ID | Precondición | Entrada | Resultado esperado |
| :--- | :--- | :--- | :--- |
| CP-03-01 | Sin herramientas disponibles | `3` | `"No hay herramientas disponibles para prestar."` |
| CP-03-02 | Taladro disponible | `3`, `1`, `Ana`, `(Enter)`, `(Enter)` | Inicio hoy y límite hoy + 7 días |
| CP-03-03 | Taladro disponible | `3`, `1`, `Ana`, `2026-11-01`, `2026-11-08` | Se guardan esas fechas exactas |
| CP-03-04 | Taladro prestada | `3` | Taladro no aparece; si se introduce su ID: `"Opción no válida."` |
| CP-03-05 | Taladro disponible | `3`, `1`, `(vacío)` | `"El campo no puede estar vacío."` |
| CP-03-06 | Taladro disponible | `3`, `1`, `Ana`, `01/11/2026` | `"Formato inválido. Usa AAAA-MM-DD."` |
| CP-03-07 | Taladro disponible | `3`, `1`, `Ana`, `2026-11-05`, `2026-11-01` | `"La fecha límite no puede ser anterior al préstamo."`; no se guarda |
| CP-03-08 | Taladro disponible | `3`, `1`, `Ana`, `2026-11-05`, `2026-11-05` | Se acepta; límite igual al inicio |

### Opción 4: Registrar devolución

| ID | Precondición | Entrada | Resultado esperado |
| :--- | :--- | :--- | :--- |
| CP-04-01 | Sin préstamos activos | `4` | `"No hay préstamos pendientes de devolución."` |
| CP-04-02 | Límite `2026-11-08` | `4`, `1`, `2026-11-07` | `"Devuelta a tiempo, sin penalización."` |
| CP-04-03 | Límite `2026-11-08` | `4`, `1`, `2026-11-11` | `"Retraso de 3 día(s). Penalización: 6.00 €"` |
| CP-04-04 | Límite `2026-11-08` | `4`, `1`, `2026-11-08` | Sin retraso: `"Devuelta a tiempo, sin penalización."` |
| CP-04-05 | Inicio `2026-11-05` | `4`, `1`, `2026-11-01` | `"La devolución no puede ser anterior al préstamo."`; sigue activo |
| CP-04-06 | Préstamo activo con ID 1 | `4`, `5` | `"Opción no válida."` |
| CP-04-07 | Préstamo activo | `4`, `1`, `(Enter)` | Fecha de devolución = hoy; retraso calculado según hoy |
| CP-04-08 | Préstamo ya devuelto | `4` | El préstamo devuelto no aparece en la lista |

### Opción 5: Ver préstamos activos

| ID | Precondición | Entrada | Resultado esperado |
| :--- | :--- | :--- | :--- |
| CP-05-01 | Sin préstamos activos | `5` | `"No hay préstamos activos."` |
| CP-05-02 | Límite `2026-12-01` | `5` | `"Al día, vence 2026-12-01"` |
| CP-05-03 | Límite `2026-10-30`; hoy `2026-11-05` | `5` | `"ATRASADO 6 día(s), penalización estimada 12.00 €"` |
| CP-05-04 | Préstamo devuelto | `5` | No aparece en la lista |

### Opción 6: Ver historial y penalizaciones

| ID | Precondición | Entrada | Resultado esperado |
| :--- | :--- | :--- | :--- |
| CP-06-01 | Sin devoluciones | `6` | `"No hay devoluciones registradas."` |
| CP-06-02 | Dos devoluciones (3 días y 0 días; la primera 6.00 €) | `6` | Dos líneas y `"Total de penalizaciones cobradas: 6.00 €"` |
| CP-06-03 | Un préstamo activo y uno devuelto | `6` | Solo aparece el devuelto |

### Opción 7: Dar de baja una herramienta

| ID | Precondición | Entrada | Resultado esperado |
| :--- | :--- | :--- | :--- |
| CP-07-01 | Todas las herramientas dadas de baja | `7` | `"No hay herramientas que dar de baja."` |
| CP-07-02 | Escalera disponible | `7`, `2` | `"dada de baja. Su historial se conserva."`; se guarda `true`; deja de aparecer en la opción 3 |
| CP-07-03 | Taladro prestada | `7`, `1` | `"No se puede dar de baja: la herramienta está prestada."`; sin cambios |
| CP-07-04 | Escalera ya dada de baja | `7` | No aparece; si se introduce su ID: `"Opción no válida."` |
| CP-07-05 | Herramienta con préstamos antiguos | `7`, ID de esa herramienta | Los préstamos siguen en el historial |

### Opción 8: Reactivar herramienta

| ID | Precondición | Entrada | Resultado esperado |
| :--- | :--- | :--- | :--- |
| CP-08-01 | Ninguna dada de baja | `8` | `"No hay herramientas dadas de baja."` |
| CP-08-02 | Escalera dada de baja | `8`, `2` | `"reactivada y disponible para préstamo."`; se guarda `false` |
| CP-08-03 | Taladro activa | `8` | No aparece; si se introduce su ID: `"Opción no válida."` |
| CP-08-04 | Herramienta con historial | `8`, ID de esa herramienta | El historial de la opción 6 no cambia |

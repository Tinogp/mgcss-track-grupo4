# 1. ¿Qué métodos del dominio se ven afectados?

Nuevos métodos: Se deberá crear un nuevo método reabrir() en la clase Solicitud que gestione la transición de CERRADA a EN_PROCESO.

Métodos existentes modificados: Todos los métodos que actualmente cambian el estado (iniciarProceso() y cerrar()), así como el propio constructor, se ven afectados, ya que ahora tendrán la responsabilidad adicional de registrar dicho cambio en la nueva estructura del histórico.

Impacto colateral: El método setDescripcion(String descripcion) verifica que el estado no sea CERRADA. Al reabrirse (pasando a EN_PROCESO), la solicitud volverá a permitir la modificación de su descripción automáticamente.

# 2. ¿Qué reglas actuales cambian?

El estado CERRADA deja de ser un estado terminal: Actualmente, el ciclo de vida de la solicitud es unidireccional y finaliza en CERRADA. La regla cambia para permitir una transición hacia atrás (bidireccionalidad) desde CERRADA hacia EN_PROCESO.

Manejo de la fechaCierre: Al cambiar la regla y salir del estado CERRADA, habrá que decidir a nivel de negocio si el atributo fechaCierre se debe resetear a null o si simplemente se mantiene y se sobrescribe en un cierre posterior.

# 3. ¿Qué tests deberían romperse?

Se romperán los tests unitarios en SolicitudTest que afirmen que el ciclo de vida termina estrictamente en CERRADA (por ejemplo, aquellos que intenten operar sobre una solicitud cerrada y esperen un comportamiento de rechazo permanente).

Si existen tests de infraestructura/persistencia que aserten el tamaño exacto del objeto guardado o comprueben la ausencia de colecciones anidadas, estos fallarán al incluirse la nueva lista del histórico.

Cualquier test de integración que valide el flujo completo End-to-End y asuma que una solicitud cerrada es inmutable.

# 4. ¿Qué parte del modelo debe extenderse?

Deberá crearse un nuevo Value Object llamado registroSolicitud. Este objeto encapsula la información del histórico: el estado registrado y la fecha (LocalDateTime) en la que se produjo la transición.

# 5. ¿Qué impacto tiene en persistencia?
El histórico del estado de la solicitud debe guardarse permanentemente.

Entidades JPA: extender SolicitudEntity.java añadiendo una relación para mapear la nueva lista de histórico.

Mappers: actualizar la lógica para mapear bidireccionalmente el nuevo histórico entre el modelo de Dominio (Solicitud) y el modelo de Persistencia (SolicitudEntity).

Base de datos: crear una nueva tabla en la base de datos para almacenar la relación 1:N entre la solicitud y sus estados histórico
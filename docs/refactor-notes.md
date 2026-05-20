# Problema 1: renombrar estado_actual
- Problema identificado: Nombre de variable poco descriptivo o ambiguo que dificulta la comprensión de la lógica del método.
- Métrica asociada: Principalmente afecta a la Mantenibilidad (Maintainability Rating) y al tiempo de comprensión del código (esfuerzo cognitivo)
- Riesgo potencial si no se corrige: Aumento de la probabilidad de introducir errores en futuros mantenimientos debido a una mala interpretación de la intención del código.
- 

# Problema 2: eliminación de método duplicado en solicitud
- Problema identificado: repetición de código para extraer el estado de la solicitud.
- Métrica asociada: Principalmente afecta a la Mantenibilidad (Maintainability Rating) y a la complejidad ciclomática.
- Riesgo potencial si no se corrige: Aumento de la probabilidad de introducir errores en futuros mantenimientos debido a una mala interpretación de la lógica del código. Ademas de  un aumento del tamaño del codigo.

# Problema 3: controlar un paso de parámetros nulo
- Problema identificado: Falta de validación en los argumentos de entrada del método. El método estaba expuesto a recibir valores null, lo que podría desencadenar un error inesperado (NullPointerException) durante la ejecución y romper el flujo de la aplicación.
- Métrica asociada: Impacta negativamente en la Fiabilidad (Reliability) del sistema y genera un "Code Smell" o "Bug" en el análisis estático, aumentando la deuda técnica.
- Riesgo potencial si no se corrige: Alta probabilidad de que se produzca una excepción no controlada (NullPointerException) en tiempo de ejecución. Esto provocaría la caída de ese flujo de la aplicación, afectando la estabilidad del sistema y la experiencia del usuario.

# Problema 4: Optimización y reemplazo
- Problema identificado: Uso innecesariamente verboso o complejo de la biblioteca estándar para lograr un resultado que dispone de una alternativa directa. El código actual implementa lógica manual (bucles, condicionales) que reinventa la rueda.
- Métrica asociada: Principalmente afecta a la Complejidad Ciclomática (Cyclomatic Complexity). También mejora el Maintainability Rating.
- Riesgo potencial si no se corrige: Código difícil de leer y comprender de un vistazo. A mayor cantidad de líneas y estructuras de control manuales, mayor es el riesgo de introducir bugs lógicos ocultos y mayor el tiempo requerido para el mantenimiento futuro.

# Problema 5: corregir fallos detectados por Sonar
- Problema identificado: Las entidades JPA y la lógica de persistencia presentaban code smells que impedían pasar el análisis de Sonar. Se detectaron imports wildcard en entidades, constructores vacíos sin explicación y falta de validación nula en el repositorio.
- Métrica asociada: Principalmente afecta a la Mantenibilidad, Fiabilidad y al cumplimiento de las reglas de calidad de código de Sonar.
- Riesgo potencial si no se corrige: El código conserva deuda técnica, se incrementa el riesgo de errores en el mapeo entre dominio y persistencia, y el proyecto puede fallar en auditorías de calidad estática.
- Cambios aplicados: Reemplazo de imports wildcard por imports explícitos en entidades JPA, adición de comentarios en constructores vacíos para justificar su uso por JPA, y validación de `save(null)` con la correspondiente prueba unitaria.


# Problema 6: corregir nuevos code smells de Sonar
- Problema identificado: Duplicación de literales en el controlador de la vista y uso de aserciones poco idiomáticas en tests sobre mapas.
- Métrica asociada: Principalmente afecta a la Mantenibilidad (Maintainability Rating), reduciendo deuda técnica y mejorando la claridad de los tests.
- Riesgo potencial si no se corrige: Aumento de duplicación, mayor probabilidad de errores al cambiar claves de atributos o rutas, y persistencia de avisos de Sonar en el pipeline.
- Cambios aplicados: Extracción de constantes para `mensaje`, `error`, `redirect:/ui` y `redirect:/ui#solicitudes`, y reemplazo de aserciones con `get(...)` por `containsEntry(...)` cuando corresponde.


# Métricas finales

- Se han eliminado 22 code smells
- Se ha mejorado el porcentaje de debt ratio de un 0.4% a un 0%

# Notas de Lanzamiento y Justificación de Versión (SemVer)

Este documento justifica la asignación del número de versión para el próximo lanzamiento del proyecto `mgcss-track-grupo4` de acuerdo con las reglas de **Versionado Semántico (SemVer 2.0.0)**.

## Determinación de la Próxima Versión

Analizando los cambios y confirmaciones (*commits*) recientes incorporados al proyecto, se determinó lo siguiente:

### 1. Cambios Analizados:
- **Nuevas Funcionalidades**: Implementación de la interfaz de usuario (`feat/user-interface`). Este cambio añade características nuevas y retrocompatibles al sistema.
- **Refactorizaciones y Correcciones**: Ajustes de simplicidad en la interfaz (`fix/simplificación interfaz`) y resolución de issues críticos en SonarQube (`refactor: issues sonnarQube`).
- **Versión Anterior**: La última versión registrada en los tags del repositorio es la `v1.1.0`.

### 2. Justificación de la Versión `1.2.0`:
- **MAJOR (1.x.x)**: No se han introducido cambios incompatibles con la API o la interfaz previa que rompan la retrocompatibilidad. Por lo tanto, el número principal se mantiene en `1`.
- **MINOR (x.2.0)**: Se han incorporado nuevas funcionalidades retrocompatibles (como la interfaz de usuario completa). Esto requiere obligatoriamente incrementar el número secundario (*Minor*). Como la versión anterior era `1.1.0`, la nueva versión secundaria es `1.2.0`.
- **PATCH (x.x.0)**: Aunque existen correcciones de errores menores y refactorizaciones, al haber al menos una nueva funcionalidad (*Feature*), predomina el incremento *Minor*, reiniciando el contador de *Patch* a `0`.

Por lo tanto, la próxima versión correcta del sistema debe ser la **`1.2.0`** (o `1.2.0-test` para pruebas previas).

# Casos de Uso API REST (Solicitudes)

Este documento contiene ejemplos de peticiones y respuestas esperadas para interactuar con la API REST de Solicitudes.

La documentación viva de OpenAPI/Swagger estará disponible en `http://localhost:8080/swagger-ui.html` cuando se levante el proyecto.

## 1. Crear una Solicitud (POST)

**Petición:**
```bash
curl -X POST http://localhost:8080/api/solicitudes \
-H "Content-Type: application/json" \
-d '{
  "descripcion": "El servidor de base de datos no responde",
  "clienteId": 1
}'
```

**Respuesta Esperada (201 Created):**
```json
{
  "id": 10,
  "descripcion": "El servidor de base de datos no responde",
  "estadoActual": "ABIERTA",
  "fechaCreacion": "2026-05-08T10:00:00",
  "fechaCierre": null,
  "clienteId": 1,
  "clienteNombre": "Empresa S.A.",
  "tecnicoId": null,
  "tecnicoNombre": null
}
```

## 2. Obtener una Solicitud (GET)

**Petición:**
```bash
curl -X GET http://localhost:8080/api/solicitudes/10
```

**Respuesta Esperada (200 OK):**
```json
{
  "id": 10,
  "descripcion": "El servidor de base de datos no responde",
  "estadoActual": "ABIERTA",
  "fechaCreacion": "2026-05-08T10:00:00",
  "fechaCierre": null,
  "clienteId": 1,
  "clienteNombre": "Empresa S.A.",
  "tecnicoId": null,
  "tecnicoNombre": null
}
```

## 3. Asignar un Técnico (PUT)

**Petición:**
```bash
curl -X PUT http://localhost:8080/api/solicitudes/10/asignar \
-H "Content-Type: application/json" \
-d '{
  "tecnicoId": 5
}'
```

**Respuesta Esperada (200 OK):**
Sin contenido en el cuerpo, HTTP 200.

## 4. Iniciar Proceso (PUT)

**Petición:**
```bash
curl -X PUT http://localhost:8080/api/solicitudes/10/iniciar
```

**Respuesta Esperada (200 OK):**
Sin contenido. El estado de la solicitud cambia a `EN_PROCESO`.

## 5. Cerrar Solicitud (PUT)

**Petición:**
```bash
curl -X PUT http://localhost:8080/api/solicitudes/10/cerrar
```

**Respuesta Esperada (200 OK):**
Sin contenido. El estado cambia a `CERRADA`.

## 6. Reabrir Solicitud (PATCH)

**Petición:**
```bash
curl -X PATCH http://localhost:8080/api/solicitudes/10/reabrir
```

**Respuesta Esperada (200 OK):**
Sin contenido. El estado cambia a `ABIERTA`.

## 7. Crear un Cliente (POST)

**Petición:**
```bash
curl -X POST http://localhost:8080/api/clientes \
-H "Content-Type: application/json" \
-d '{
  "nombre": "Talleres Pérez",
  "email": "contacto@talleresperez.com",
  "tipoCliente": "PREMIUM"
}'
```

**Respuesta Esperada (201 Created):**
{
  "id": 1,
  "nombre": "Talleres Pérez",
  "email": "contacto@talleresperez.com",
  "tipoCliente": "PREMIUM"
}

## 8. Registrar un Técnico (POST)

**Petición:**
```bash
curl -X POST http://localhost:8080/api/tecnicos \
-H "Content-Type: application/json" \
-d '{
  "nombre": "Marta García",
  "especialidad": "REDES"
}'
```

**Respuesta Esperada (201 Created):**
Nota: Por defecto, los técnicos se crean en estado inactivo.
{
  "id": 5,
  "nombre": "Marta García",
  "especialidad": "REDES",
  "activo": false
}

## 9. Activar un Técnico (PUT)

**Petición:**
```bash
curl -X PUT http://localhost:8080/api/tecnicos/5/activar
```

**Respuesta Esperada (200 OK):**
Sin contenido. El técnico con ID 5 ahora puede recibir asignaciones.

## 10. Listar todas las Solicitudes (GET)

**Petición:**
```bash
curl -X GET http://localhost:8080/api/solicitudes
```

**Respuesta Esperada (200 OK):**
[
  {
    "id": 10,
    "descripcion": "El servidor de base de datos no responde",
    "estadoActual": "ABIERTA",
    "fechaCreacion": "2026-05-11T10:00:00",
    "fechaCierre": null,
    "clienteId": 1,
    "clienteNombre": "Talleres Pérez",
    "tecnicoId": null,
    "tecnicoNombre": null
  }
]
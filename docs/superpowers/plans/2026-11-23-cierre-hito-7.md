# Cierre del Hito 7 Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Completar el hito 7 definido por `features.md`, incluyendo funcionalidades avanzadas, persistencia local y correcciones visuales/funcionales.

**Architecture:** Mantener Java/XML/ViewBinding, Room Offline-First y AppContainer. Reutilizar repositorios existentes y conectar las pantallas a sus ViewModels; no introducir Compose, Kotlin nuevo ni servicios externos.

**Tech Stack:** Java 17, Android Views, Material Components 3, Room, LiveData, Navigation, MPAndroidChart donde ya exista.

**Spec:** `UI_DESIGN_SYSTEM.md`, `README.md`, `docs/pruebas-funcionales.md`.

## Global Constraints

- Java 17+ puro para código nuevo.
- XML tradicional + ViewBinding + Material 3 DayNight.
- Sin colores hexadecimales hardcodeados.
- Persistencia local mediante Room y preferencias existentes.
- `features.md` es la fuente de verdad del alcance del hito 7.
- No se excluyen Health Connect, WOD/temporizadores, macros dinámicos, rachas, estado de ánimo ni notas.

## Review Focus

- Valores `Double` en macros y cantidades deben mostrarse redondeados y con unidades separadas.
- Cierres de sesión no deben dejar referencias a ViewBinding.
- Guardar y reabrir debe conservar registros locales.
- Estados vacíos y estados con datos deben alternarse sin superposición.
- Acciones de botones deben navegar o persistir, nunca mostrar un placeholder silencioso.

### Task 1: Correcciones comunes y Diario

- [ ] Corregir presentación de macros y validar alta/edición de alimentos con el usuario local actual.
- [ ] Verificar Quick Add: favoritos, recientes y persistencia tras recreación.
- [ ] Probar hidratación desde Diario y persistencia del historial.

### Task 2: Ejercicio local

- [ ] Conectar ejercicios recientes al repositorio Room.
- [ ] Implementar selección/detalle de ejercicio y guardado de rutina local.
- [ ] Implementar WOD CrossFit y temporizadores definidos en `features.md`.
- [ ] Registrar calorías configurables, RPE, series, repeticiones y peso.

### Task 2b: Salud y gamificación

- [ ] Integrar Health Connect opcionalmente, con permisos y fallback offline.
- [ ] Implementar rachas calculadas a partir de Diario, agua y entrenamientos.
- [ ] Implementar estado de ánimo y notas locales.

### Task 2c: Nutrición avanzada

- [ ] Implementar macros dinámicos, metas por comida, porcentaje/gramo y Net Carbs.
- [ ] Completar Insights con tendencias, predicciones, patrones y resumen semanal reales.

### Task 3: Escaneo y recetas

- [ ] Completar resultado de escaneo para crear/actualizar alimento local y navegar a Diario.
- [ ] Completar mensajes de éxito/error y linterna si la API de cámara lo permite.
- [ ] Validar creación, edición y registro de recetas con datos locales.

### Task 4: Planes e Insights

- [ ] Implementar Quick Log desde Planes reutilizando el flujo de Diario.
- [ ] Reemplazar todos los datos de ejemplo de Insights por agregados de repositorios disponibles; mostrar vacío cuando no haya datos.

### Task 5: Perfil, configuración y exportación

- [ ] Completar persistencia de presupuesto, cuenta local, unidades, metas y notificaciones.
- [ ] Mantener sincronización remota como estado no conectado explícito sin bloquear flujos locales.
- [ ] Verificar exportación de todos los tipos seleccionables.

### Task 6: Verificación y documentación

- [ ] Ejecutar `:app:assembleDebug` y `git diff --check`.
- [ ] Instalar en Motorola e13, recorrer flujos de escritura y revisar `logcat -b crash`.
- [ ] Actualizar README y matriz funcional con resultados y pendientes reales.

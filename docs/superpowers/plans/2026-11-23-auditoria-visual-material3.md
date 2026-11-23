# Auditoría visual Material 3 Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Revisar y corregir la composición visual de todas las pantallas del hito 7, priorizando componentes compartidos y BottomSheets.

**Architecture:** Mantener XML tradicional y ViewBinding. Corregir primero tokens y contenedores reutilizados, luego layouts de cada módulo, y finalmente validar navegación y estados principales en el APK instalado.

**Tech Stack:** Android XML, Material Components/Material 3, ConstraintLayout, NestedScrollView, Room, Gradle, ADB.

**Spec:** `UI_DESIGN_SYSTEM.md`, `avances.md` (PASO 7), `docs/pruebas-funcionales.md`.

## Global Constraints

- Java 17 para código nuevo; XML tradicional y ViewBinding.
- Material 3 DayNight; sin colores hexadecimales en layouts ni Java.
- Espaciado sobre una grilla de 4dp y márgenes laterales de 16dp.
- BottomSheets largos deben desplazarse sin ocultar sus acciones principales.
- Todos los iconos nuevos deben ser Vector Drawable nativos de 24dp, con color por token y sin librerías externas.
- El hito 8 no se implementa en esta etapa.

## Review Focus

- Contenido mayor que el viewport: el test visual debe confirmar scroll y acciones accesibles.
- Estados vacíos y con datos: el mismo layout no debe superponer tarjetas ni botones.
- Modo claro/oscuro: textos, bordes e iconos deben conservar contraste.
- Componentes incluidos: macros, chips y botones deben mantener ancho y etiquetas consistentes.
- Acciones principales: cada pantalla debe conservar una acción visible y navegable.

### Task 1: Componentes compartidos y BottomSheets

**Files:** `app/src/main/res/layout/sheet_*.xml`, `feature/feature-diario/src/main/res/layout/sheet_*.xml`, `core/core-ui/src/main/res/values/themes.xml`

- [ ] Auditar contenedores, atributos Material 3 no disponibles y acciones fuera del viewport.
- [ ] Corregir scroll, constraints, paddings y tokens compartidos.
- [ ] Compilar el APK y revisar `git diff --check`.

### Task 2: Diario, Hidratación, Nutrición y Progreso

**Files:** layouts y fragments de `feature-diario`, `feature-agua`, `feature-nutricion` y `feature-progreso`.

- [x] Revisar estados vacío/con datos, tarjetas de macros, formularios y diálogos.
- [x] Verificar jerarquía tipográfica, iconos, colores, espaciado y botones.
- [ ] Probar navegación y cambios de estado en claro y oscuro.

### Task 3: Recetas, Escaneo, Ejercicio y Planes

**Files:** layouts y fragments de `feature-recetas`, `feature-escaneo`, `feature-ejercicio` y `feature-planes`.

- [x] Revisar listas, filtros, chips, formularios y estados vacíos.
- [ ] Mantener WOD y Escaneo IA como estados fuera de alcance del hito 8.
- [ ] Verificar que las acciones implementadas naveguen o guarden correctamente.

### Task 4: Configuración, Perfil e Insights

**Files:** layouts y fragments de `feature-configuracion`, `feature-perfil` y `feature-insights`.

- [x] Revisar formularios, switches, diálogos y navegación de regreso.
- [x] Marcar visualmente los datos de ejemplo de Insights sin presentarlos como datos reales.
- [x] Validar contraste y consistencia de botones secundarios.

### Task 5: Verificación final

**Files:** `docs/pruebas-funcionales.md`, `README.md`.

- [ ] Ejecutar compilación integrada.
- [ ] Instalar el APK y recorrer pantallas principales en Motorola e13.
- [ ] Registrar hallazgos reproducibles y limitaciones restantes.

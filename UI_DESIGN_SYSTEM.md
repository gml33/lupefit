# UI_DESIGN_SYSTEM.md — Guía Maestra de Diseño y Arquitectura UI (LuPeFit)

## 1. Stack Tecnológico y Reglas Estrictas
* **Lenguaje:** Java 17+ (Estricto. No generar código Kotlin).
* **Sistema de Vistas:** XML tradicional (Estricto. Prohibido Jetpack Compose).
* **Binding:** Uso obligatorio de `ViewBinding` en todas las Activities, Fragments, BottomSheets y Adapters.
* **Librería UI Base:** Material Components for Android (`com.google.android.material:material:1.12.0+`) heredando de `Theme.Material3.DayNight.NoActionBar`.
* **Gráficos y Estadísticas:** `MPAndroidChart` (`v3.1.0`) para gráficos de barras, líneas y donas complejas. Componentes nativos `CircularProgressIndicator` y `LinearProgressIndicator` para progreso diario.
* **Escaneo y Cámara:** CameraX + Google ML Kit (Barcode Scanning).
* **DI:** Hilt/Dagger (Java).
* **Base de Datos:** Room Database (Java).
* **Red:** Retrofit + OkHttp (Java).

---

## 2. Sistema de Color (Soporte Modo Claro y Modo Oscuro)
**REGLA DE ORO:** Nunca usar colores hexadecimales hardcodeados en los archivos `.xml` de layouts ni en clases `.java`. Usar siempre referencias a `@color/...` o atributos de tema (`?attr/colorSurface`, `?attr/colorOnSurface`, etc.).

### A. Archivo `res/values/colors.xml` (Modo Claro)
```xml
<resources>
    <!-- Identidad Principal -->
    <color name="md_theme_primary">#006C4C</color>
    <color name="md_theme_onPrimary">#FFFFFF</color>
    <color name="md_theme_primaryContainer">#89F8C7</color>
    <color name="md_theme_onPrimaryContainer">#002114</color>

    <!-- Secundarios y Superficies -->
    <color name="md_theme_secondary">#4D6357</color>
    <color name="md_theme_onSecondary">#FFFFFF</color>
    <color name="md_theme_secondaryContainer">#CFE9D9</color>
    <color name="md_theme_onSecondaryContainer">#0A1F16</color>

    <color name="md_theme_background">#F5FBF6</color>
    <color name="md_theme_onBackground">#171D1A</color>
    <color name="md_theme_surface">#FFFFFF</color>
    <color name="md_theme_onSurface">#171D1A</color>
    <color name="md_theme_surfaceVariant">#DBE5DD</color>
    <color name="md_theme_onSurfaceVariant">#404943</color>
    <color name="md_theme_outline">#707973</color>

    <!-- Semánticos: Macronutrientes -->
    <color name="macro_protein">#2563EB</color>
    <color name="macro_protein_track">#DBEAFE</color>
    <color name="macro_carbs">#D97706</color>
    <color name="macro_carbs_track">#FEF3C7</color>
    <color name="macro_fat">#7C3AED</color>
    <color name="macro_fat_track">#EDE9FE</color>

    <!-- Semánticos: Hidratación y Estados -->
    <color name="water_primary">#0284C7</color>
    <color name="water_track">#E0F2FE</color>
    <color name="status_success">#16A34A</color>
    <color name="status_error">#BA1A1A</color>
</resources>
```

### B. Archivo `res/values-night/colors.xml` (Modo Oscuro)
```xml
<resources>
    <!-- Identidad Principal -->
    <color name="md_theme_primary">#6CDBAC</color>
    <color name="md_theme_onPrimary">#003826</color>
    <color name="md_theme_primaryContainer">#005138</color>
    <color name="md_theme_onPrimaryContainer">#89F8C7</color>

    <!-- Secundarios y Superficies -->
    <color name="md_theme_secondary">#B3CCBE</color>
    <color name="md_theme_onSecondary">#1F352A</color>
    <color name="md_theme_secondaryContainer">#354B40</color>
    <color name="md_theme_onSecondaryContainer">#CFE9D9</color>

    <color name="md_theme_background">#0F1512</color>
    <color name="md_theme_onBackground">#DFE4DF</color>
    <color name="md_theme_surface">#171D1A</color>
    <color name="md_theme_onSurface">#DFE4DF</color>
    <color name="md_theme_surfaceVariant">#404943</color>
    <color name="md_theme_onSurfaceVariant">#BFC9C2</color>
    <color name="md_theme_outline">#8A938C</color>

    <!-- Semánticos: Macronutrientes -->
    <color name="macro_protein">#60A5FA</color>
    <color name="macro_protein_track">#1E3A8A</color>
    <color name="macro_carbs">#FBBF24</color>
    <color name="macro_carbs_track">#78350F</color>
    <color name="macro_fat">#A78BFA</color>
    <color name="macro_fat_track">#4C1D95</color>

    <!-- Semánticos: Hidratación y Estados -->
    <color name="water_primary">#38BDF8</color>
    <color name="water_track">#0C4A6E</color>
    <color name="status_success">#4ADE80</color>
    <color name="status_error">#FFB4AB</color>
</resources>
```

---

## 3. Tokens de Espaciado, Bordes y Elevación
* **Margen lateral estándar de pantalla:** `16dp`
* **Separación entre tarjetas (Cards):** `12dp` o `16dp`
* **Padding interno de tarjetas principales:** `20dp`
* **Padding interno de ítems de lista:** `12dp` - `16dp`
* **Radios de esquina (`cornerRadius`):**
  * Tarjetas principales / Dashboard: `24dp`
  * Tarjetas de ítems en `RecyclerView`: `16dp`
  * Botones principales (`MaterialButton`): `14dp` (o estilo pill `50%`)
  * `BottomSheetDialogFragment` (esquinas superiores): `28dp`
  * Barras de progreso (`trackCornerRadius`): `8dp`
* **Bordes y Sombras:** Preferir contraste de superficie sobre sombras pesadas. Usar `app:cardElevation="0dp"` o `"1dp"` con `app:strokeWidth="1dp"` y `app:strokeColor="@color/md_theme_surfaceVariant"` en Modo Oscuro para delimitar tarjetas limpiamente.

---

## 4. Arquitectura de Navegación Principal (`MainActivity`)
* Contenedor raíz: `CoordinatorLayout` con fondo `@color/md_theme_background`.
* Área de contenido: `FragmentContainerView` con `NavHostFragment` (Jetpack Navigation Component).
* Barra inferior: `BottomAppBar` o `BottomNavigationView` con 5 destinos:
  1. **Inicio** (Módulo 1: Diario + Módulo 7: Agua)
  2. **Tendencias** (Módulo 3: Nutrición + Módulo 4: Progreso + Módulo 10: Insights)
  3. **[Espacio Central]** ocupado por `FloatingActionButton` (`@color/md_theme_primary`, icono `+` en `@color/md_theme_onPrimary`) que despliega el menú rápido (`SheetQuickAdd` con acceso a Buscar, Escanear Código, Escanear IA, Registrar Ejercicio y Agua).
  4. **Planes** (Módulo 2: Ejercicio + Módulo 5: Recetas + Módulo 11: Planes)
  5. **Perfil** (Módulo 8: Configuración + Módulo 9: Perfil/Auth)

---

## 5. Catálogo de Componentes Reutilizables (Plantillas Base)
Antes de crear layouts nuevos en cualquier módulo, reutilizar estas estructuras:
1. **`view_header_date.xml`:** Encabezado superior con selector de fecha desplegable (`Hoy, 20 Nov v`), flechas `<` `>` y avatar circular de usuario a la derecha.
2. **`card_macro_summary.xml`:** Tarjeta de `24dp` con `CircularProgressIndicator` central (tamaño `150dp`, grosor `14dp`) y 3 `LinearProgressIndicator` horizontales (`trackThickness="10dp"`) usando `@color/macro_protein`, `@color/macro_carbs` y `@color/macro_fat`.
3. **`item_log_entry.xml`:** Fila estándar para alimentos, recetas o ejercicios dentro de un `RecyclerView`. Contiene icono circular a la izquierda, título (`15sp` Bold), subtítulo con porción/series (`13sp` `@color/md_theme_onSurfaceVariant`) y valor numérico (kcal/min) alineado a la derecha.
4. **`sheet_base_container.xml`:** Estructura estándar para los 19 `BottomSheetDialogFragment` del proyecto, incluyendo la píldora superior de arrastre (`BottomSheetDragHandleView`), título en `18sp` Bold y botón de acción inferior de ancho completo.

---

## 6. Convención de Nombres y Mapa de los 11 Módulos (~80 Pantallas)
Todo archivo generado debe respetar esta nomenclatura estricta:
* **Fragments:** `[Modulo][Nombre]Fragment.java` -> `fragment_[modulo]_[nombre].xml`
* **BottomSheets / Dialogs:** `[Modulo][Nombre]Sheet.java` -> `sheet_[modulo]_[nombre].xml`
* **Adapters / Items:** `[Nombre]Adapter.java` -> `item_[nombre].xml`

### Inventario de Módulos:
1. **Diario (5 Fragments, 5 Sheets):** `DiarioHomeFragment`, `DiarioBuscarFragment`, `DiarioDetalleAlimentoFragment`, `DiarioCrearAlimentoFragment`, `DiarioCategoriasFragment` | `QuickAddSheet`, `FiltrosBusquedaSheet`, `EditarPorcionSheet`, `OpcionesComidaSheet`, `MoverComidaSheet`.
2. **Ejercicio (6 Fragments, 4 Sheets):** `EjercicioHomeFragment`, `EjercicioCardioFragment`, `EjercicioFuerzaFragment`, `EjercicioRutinasFragment`, `EjercicioSesionActivaFragment`, `EjercicioCrearRutinaFragment` | `RegistrarCardioSheet`, `RegistrarSerieSheet`, `DescansoTimerSheet`, `SelectorMusculoSheet`.
3. **Nutrición (7 Fragments, 1 Sheet):** `NutricionResumenFragment`, `NutricionMetasFragment`, `NutricionMacrosFragment`, `NutricionMicrosFragment`, `NutricionPorComidaFragment`, `NutricionNetCarbsFragment`, `NutricionGraficosFragment` | `EditarMetasMacroSheet`.
4. **Progreso (5 Fragments, 3 Sheets):** `ProgresoHomeFragment`, `ProgresoPesoFragment`, `ProgresoMedidasFragment`, `ProgresoFotosCamaraFragment`, `ProgresoExportarFragment` | `RegistrarPesoSheet`, `RegistrarMedidaSheet`, `CompararFotosSheet`.
5. **Recetas (6 Fragments, 2 Sheets):** `RecetasHomeFragment`, `RecetasCrearPaso1Fragment`, `RecetasCrearPaso2Fragment`, `RecetasCrearPaso3Fragment`, `RecetasImportarUrlFragment`, `RecetasListaComprasFragment` | `AgregarIngredienteSheet`, `ColeccionesSheet`.
6. **Escaneo (3 Fragments, 1 Sheet):** `EscaneoCodigoBarrasFragment`, `EscaneoIaComidaFragment`, `EscaneoHistorialFragment` | `ResultadoEscaneoSheet`.
7. **Agua (1 Fragment, 2 Sheets):** `AguaDetalleFragment` | `AgregarAguaRapidoSheet`, `ConfigurarMetaAguaSheet`.
8. **Configuración (14 Fragments):** `ConfigHomeFragment`, `ConfigCuentaFragment`, `ConfigPerfilFisicoFragment`, `ConfigMetasCaloricasFragment`, `ConfigNotificacionesFragment`, `ConfigNotifComidasFragment`, `ConfigNotifAguaFragment`, `ConfigAparienciaThemeFragment`, `ConfigIdiomaFragment`, `ConfigUnidadesFragment`, `ConfigSyncHealthFragment`, `ConfigExportarDatosFragment`, `ConfigPrivacidadFragment`, `ConfigEliminarCuentaFragment`.
9. **Perfil / Auth (8 Fragments):** `AuthSplashFragment`, `AuthLoginFragment`, `AuthRegistroFragment`, `AuthRecuperarPassFragment`, `OnboardingPaso1ObjetivoFragment`, `OnboardingPaso2DatosFragment`, `OnboardingPaso3ActividadFragment`, `OnboardingPaso4ResumenFragment`.

---

## 7. Funcionalidades Extra (v2.0) — LuPeFit

### A. Nuevos Tokens de Color (Modo Claro / Modo Oscuro)
Agregar a `colors.xml` y `values-night/colors.xml` para soportar CrossFit, Rachas y Estado de Ánimo:
* **CrossFit / Alta Intensidad (`crossfit_accent`):** `#EA580C` (Claro) | `#FB923C` (Oscuro)
* **Rachas / Fuego (`streak_active`):** `#F59E0B` (Claro) | `#FBBF24` (Oscuro)
* **Humor / Bienestar (`mood_positive`, `mood_neutral`, `mood_low`):** `#10B981`, `#6366F1`, `#F43F5E`

### B. Arquitectura Offline-First y Sincronización
* **Persistencia Local:** Uso obligatorio de **Room Database** en Java como única fuente de verdad para la UI. Ninguna pantalla debe depender de una llamada de red directa para renderizarse.
* **Cola de Sincronización:** Cada entidad (alimento, WOD, nota, humor) debe incluir un campo booleano `isSynced` y un `timestamp`. **WorkManager** ejecutará tareas en segundo plano cuando haya conectividad.
* **Health Connect:** Integrar `androidx.health.connect:connect-client` en Java (en el Módulo 8: `ConfigSyncHealthFragment` y lectura pasiva en `DiarioHomeFragment`) para importar pasos, frecuencia cardíaca, sueño y calorías activas.

### C. Componentes Reutilizables Nuevos
1. **`view_streak_badge.xml`:** Píldora compacta en el encabezado superior (`view_header_date.xml`) con icono de llama (`@color/streak_active`) y contador numérico de días consecutivos cumpliendo objetivos.
2. **`card_voice_input.xml` (Hands-Free Logging):** Botón de micrófono animado (`Lottie` o cambio de escala en `MaterialButton`) dentro de `QuickAddSheet` y `DiarioBuscarFragment`. Muestra transcripción en tiempo real mediante `SpeechRecognizer` o endpoint externo (Whisper) antes de confirmar los alimentos.
3. **`view_wod_timer.xml`:** Cronómetro de alto contraste y números gigantes (`48sp` Bold tabular) para `EjercicioSesionActivaFragment`. Soporta modos **AMRAP**, **EMOM**, **For Time** y **Tabata**, con señales sonoras/vibración y botones táctiles grandes (`>= 56dp`) para marcar rondas sin mirar la pantalla.
4. **`card_daily_journal_mood.xml`:** Tarjeta compacta al pie de `DiarioHomeFragment` y `EjercicioSesionActivaFragment` con selector de 5 emojis/iconos de humor (`1` a `5`) y campo de texto expandible (`TextInputLayout`) para notas rápidas sobre energía, digestión o sensaciones del entrenamiento.

### D. Lógica de Macronutrientes Dinámicos (Ciclado)
* Cuando el usuario registra una sesión en el módulo de **CrossFit / Fuerza** o **Health Connect** reporta un gasto activo superior al umbral base, `NutricionResumenFragment` y `card_macro_summary.xml` deben recalcular automáticamente el objetivo diario de carbohidratos y proteínas, mostrando una etiqueta sutil (`+35g Carbs por WOD`).

### E. Pantallas Nuevas Incorporadas al Inventario
* **Módulo 1 (Diario):** `DiarioVozSheet` (registro manos libres), `DiarioNotaHumorSheet` (registro diario de estado de ánimo y comentarios).
* **Módulo 2 (Ejercicio):** `EjercicioWodCrossfitFragment` (constructor y lista de WODs), `EjercicioTimerFullFragment` (pantalla completa de cronómetro AMRAP/EMOM con contador de rondas).
# LuPeFit 🏋️‍♂️🥗

> **Aplicación de nutrición y entrenamiento offline-first, 100% gratuita, sin publicidad, con backend propio y en castellano argentino.**

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)](https://github.com/gml33/lupefit/actions)
[![Java 17+](https://img.shields.io/badge/Java-17+-orange)](https://openjdk.org/)
[![Android](https://img.shields.io/badge/Android-9%2B_(API_28)-green)](https://developer.android.com/)
[![Material 3](https://img.shields.io/badge/Material_3-Design-blue)](https://m3.material.io/)
[![License](https://img.shields.io/badge/License-MIT-purple)](LICENSE)

---

## 📱 Capturas de Pantalla

| Light Mode | Dark Mode |
|:----------:|:---------:|
| ![Light](docs/screenshots/home_light.svg) | ![Dark](docs/screenshots/home_dark.svg) |
| *Dashboard Principal* | *Dashboard Principal (Oscuro)* |
| ![Diario](docs/screenshots/diario_light.svg) | ![Diario Dark](docs/screenshots/diario_dark.svg) |
| *Diario de Alimentos* | *Diario de Alimentos (Oscuro)* |
| ![Ejercicio](docs/screenshots/ejercicio_light.svg) | ![Ejercicio Dark](docs/screenshots/ejercicio_dark.svg) |
| *Registro de Ejercicios* | *Registro de Ejercicios (Oscuro)* |
| ![Nutrición](docs/screenshots/nutricion_light.svg) | ![Nutrición Dark](docs/screenshots/nutricion_dark.svg) |
| *Resumen de Macronutrientes* | *Resumen de Macronutrientes (Oscuro)* |
| ![Progreso](docs/screenshots/progreso_light.svg) | ![Progreso Dark](docs/screenshots/progreso_dark.svg) |
| *Gráficos de Progreso* | *Gráficos de Progreso (Oscuro)* |

> **Nota**: Las capturas se agregarán conforme se implementen las pantallas. Actualmente el proyecto está en **Paso 7 - Feature Modules Logic (feature-diario, feature-ejercicio) COMPLETADO**.

---

## ✨ Características Principales

### 🎯 **Todas las Features son GRATIS** (Sin Premium, Sin Paywalls)
- ❌ Sin Blog / Community / Friends
- ❌ Sin Pagos / Suscripciones / Upsells
- ❌ Sin Publicidad / Analytics de terceros
- ✅ Todo funcional desde el día 1

### 🏗️ **Arquitectura Sólida**
| Capa | Tecnología | Estado |
|------|------------|--------|
| **UI** | Java 17+ + XML Views + ViewBinding + DataBinding | ✅ Paso 1 |
| **Tema** | Material 3 (Tokens `md_theme_*`) | ✅ Paso 1 |
| **BD Local** | Room (annotationProcessor) + Offline-First | ✅ Paso 1 |
| **DI** | Manual (AppContainer) - Sin Hilt/Dagger | ✅ Paso 1 |
| **Sync** | WorkManager + Backend propio | ✅ Paso 3 |
| **Backend** | FastAPI + PostgreSQL + Redis + Celery (Docker) | ✅ Paso 5 |

### 🌙 **Modo Oscuro Completo**
- Switch automático (DayNight) + toggle manual
- Paleta `values-night/colors.xml` optimizada
- Elevation overlays automáticos
- Contraste WCAG AA en ambos temas

### 🇦🇷 **Localización: Castellano Argentino**
- 365+ strings en `values-es-rAR/strings.xml`
- Formato fecha: `dd/MM/yyyy` | Hora: `HH:mm`
- Términos: "Registrate", "Entrar", "Probá", "Tocá", "Apuntá", "Almuerzo", "Merienda"

---

## 📦 Módulos Funcionales (Roadmap)

### ✅ **Implementados (Paso 1 - Esqueleto Base)**
| Módulo | Descripción |
|--------|-------------|
| **Core Data (Room)** | 14 entidades + 14 DAOs + TypeConverters |
| **Core Domain** | 14 Repositories + 5 Engines + 7 UseCases (stubs) |
| **Core UI** | Tema M3 completo, 4 componentes base, drawables, strings AR |
| **App Container** | DI Manual puro Java (218+ líneas, zero reflection) |

### ✅ **Implementados (Paso 2-3 - Core Network & Sync)**
| Módulo | Descripción |
|--------|-------------|
| **Core Network** | Retrofit, OkHttp, Moshi, API Service, DTOs, SyncEngine, AuthInterceptor |
| **Core Sync** | WorkManager SyncWorker, SyncQueue, ConflictResolver (4 estrategias), SyncScheduler |

### ✅ **Implementados (Paso 4 - Escáner + Catálogo + Voz + Costos)**
| Funcionalidad | Descripción |
|---------------|-------------|
| **FoodProduct Entity** | Catálogo offline-first con macros, `isFreshFood`, Parcelable |
| **Precarga JSON** | 30 alimentos frescos argentinos (carnes, huevos, lácteos, cereales, frutas, verduras) |
| **CameraX + ML Kit** | Escaneo códigos de barras (EAN-13, EAN-8, UPC-A, UPC-E) con overlay Material 3 |
| **FoodCatalogRepository** | Offline-First: Room → Open Food Facts API → Guarda en Room → Fallback manual |
| **VoiceTranscriptionEngine** | Strategy Pattern: SpeechRecognizer nativo `es-AR` + Whisper Server (MediaRecorder + OkHttp) |
| **VoiceFoodParser** | NLP español: "200 gramos de pollo y 100 de arroz" → extrae cantidad/unidad, busca catálogo, calcula macros |
| **DiarioVozSheet** | BottomSheet 28dp, micrófono animado (anillos pulsantes), transcripción live, lista editable con barras macro P/C/G |
| **ResultadoEscaneoSheet** | Producto detectado, selector gramos (+/- chips 100g/porción/200g), resumen calculado dinámico |
| **Cost Tracking** | `referencePrice`, `referenceQuantityGrams`, `priceUpdatedAt`, badge precio desactualizado (>30 días) |
| **Presupuesto Semanal** | Meta configurable, LinearProgressIndicator, estado vs meta, items sin precio |

### ✅ **Implementados (Paso 5 - Backend FastAPI + Docker)**
| Componente | Descripción |
|------------|-------------|
| **17 SQLAlchemy Models** | User, FoodLog, WorkoutLog, WorkoutSet, Exercise, Food, FoodProduct, Recipe, RecipeIngredient, WaterLog, ProgressPhoto, PersonalRecord, GlucoseLog, MoodEntry, UserNote, WeatherSnapshot, Streak, SyncQueue |
| **15 Pydantic Schemas** | Request/Response para todas las entidades |
| **16 Routers API v1** | Auth, Users, FoodLogs, WorkoutLogs, Exercises, Foods, Recipes, WaterLogs, ProgressPhotos, PersonalRecords, GlucoseLogs, MoodEntries, UserNotes, WeatherSnapshots, Streaks, Sync |
| **JWT Auth** | RS256, access 30min, refresh 7d, bcrypt |
| **Sync Batch** | Versionado global, resolución conflictos (4 estrategias), pull changes |
| **Docker Compose** | 5 servicios: postgres:16, redis:7, backend (uvicorn 4 workers), celery-worker, celery-beat |

### ✅ **Implementados (Paso 6 - Feature Modules: 11 módulos UI)**
| Módulo | Funcionalidades Clave | Estructura |
|--------|----------------------|------------|
| **feature-diario** | Búsqueda alimentos, Quick Add, Escaneo código barras, Voz, Timestamps por comida, Macros por comida | DiarioHomeFragment, DiarioVozSheet, DiarioEntryAdapter, nav_diario.xml |
| **feature-ejercicio** | Cardio/Fuerza, Rutinas, WOD CrossFit (AMRAP/EMOM/For Time/Tabata), Timer fullscreen | EjercicioHomeFragment, nav_ejercicio.xml |
| **feature-nutricion** | Resumen macros circular, Metas por comida, Net Carbs mode, Gráficos MPAndroidChart | NutricionHomeFragment, nav_nutricion.xml |
| **feature-progreso** | Peso, Medidas, Fotos progreso, Export CSV/JSON, Gráficos tendencia | ProgresoHomeFragment, nav_progreso.xml |
| **feature-recetas** | Crear, Importar URL, Ingredientes, Macros, Colecciones de recetas | RecetasHomeFragment, nav_recetas.xml |
| **feature-escaneo** | ML Kit Barcode + Passio Food Scan (IA) | EscanearHomeFragment, nav_escaneo.xml |
| **feature-agua** | Tracking hidratación, Meta diaria, Recordatorios | AguaHomeFragment, nav_agua.xml |
| **feature-configuracion** | Cuenta, Perfil, Metas, Notificaciones, Apariencia, Sync, Exportar, Costos, Presupuesto | ConfiguracionHomeFragment, nav_configuracion.xml |
| **feature-perfil** | Auth local (email/password), Sync account, Dashboard personalizable | PerfilHomeFragment, nav_perfil.xml |
| **feature-insights** | IA Nutricional, Tendencias, Predicciones, Weekly digest, Patrones | InsightsHomeFragment, nav_insights.xml |
| **feature-planes** | Planes comida semanales, Planes entrenamiento, Quick log recipe, Presupuesto | PlanesHomeFragment, nav_planes.xml |

**Infraestructura común por módulo:**
- Navigation Graph XML (`nav_*.xml`) con startDestinations, actions, dialog destinations
- ViewModel base (`*HomeViewModel.java`)
- Fragment Home base (`*HomeFragment.java`) con MaterialToolbar, NestedScrollView, CoordinatorLayout
- Layouts base (`fragment_*_home.xml`)
- Strings localizadas (`values/strings.xml` con terminología argentino)

### ✅ **Implementados (Paso 7 - Feature Modules Logic: feature-diario, feature-ejercicio, feature-nutricion, feature-progreso, feature-recetas, feature-escaneo, feature-agua, feature-configuracion, feature-perfil)** ✅ **COMPLETADO**

#### **feature-diario Logic** ✅ COMPLETADO
| Componente | Descripción |
|------------|-------------|
| **BuscarAlimentoFragment** | Búsqueda alimentos con filtros categoría, chips, RecyclerView con macros |
| **QuickAddFragment** | Alimentos frescos, recientes, favoritos con selección rápida |
| **EditarPorcionSheet** | Selector gramos (+/- chips presets 50/100/150/200/250/300g), selector tipo comida, macros calculados en vivo |
| **DiarioCrearAlimentoFragment** | Creación manual de alimentos con macros, porción, fresco |
| **DiarioHomeFragment** | Navegación integrada, currentDateKey, callback DiarioVozSheet, EscaneoCodigoBarrasFragment |
| **DiarioVozSheet callback** | Registra alimentos parseados por voz directamente al diario |
| **ViewModels** | BuscarAlimentoViewModel, QuickAddViewModel, DiarioCrearAlimentoViewModel |
| **Layouts** | fragment_buscar_alimento, fragment_quick_add, fragment_diario_crear_alimento, item_food_search_result, item_quick_add_food, sheet_editar_porcion |

#### **feature-ejercicio Logic** ✅ COMPLETADO
| Componente | Descripción |
|------------|-------------|
| **BuscarEjercicioFragment** | Búsqueda ejercicios con filtros categoría (Fuerza/Olímpico/Gymnastics/Cardio/Strongman/CrossFit), patrón movimiento, equipamiento |
| **NuevaRutinaFragment** | Crear rutinas agregando ejercicios con series, reps, peso, %1RM, descanso |
| **WodTimerFragment** | Timer fullscreen para AMRAP, EMOM, For Time, Tabata con alertas sonoras/vibración |
| **RutinaDetalleFragment** | Ver ejercicios de rutina, iniciar entrenamiento |
| **EjercicioHomeFragment** | Grid 4 accesos rápidos (Buscar, Nueva, Timer, Mis rutinas) + lista entrenamientos recientes |
| **BuscarEjercicioViewModel** | Búsqueda con filtros categoría/patrón, LiveData |
| **NavigationActions** | Inicialización IDs navegación via reflexión `R.id.action_*` |

#### **feature-nutricion Logic** ✅ COMPLETADO
| Componente | Descripción |
|------------|-------------|
| **NutricionHomeFragment** | Resumen macro circular, metas diarias, acciones rápidas, empty state |
| **MetasFragment** | Configuración metas calóricas, macros, fibra, agua, toggle Net Carbs |
| **MacrosDetalleFragment** | Detalle macros con % de meta, net carbs toggle, micronutrientes |
| **GraficosNutricionFragment** | Gráficos MPAndroidChart: tendencia calorías 7/30/90 días, distribución macro (pie), tendencias P/C/G |
| **NutricionHomeViewModel** | Totales diarios via DiarioRepository, LiveData, empty state handling |
| **MetasViewModel** | Metas persistidas en SharedPreferences, TextWatchers para two-way binding |
| **MacrosDetalleViewModel** | Macro details con % de meta, net carbs toggle, micronutrientes placeholder |
| **GraficosNutricionViewModel** | Period selector (7/30/90 días), mock data para gráficos MPAndroidChart |
| **Layouts** | fragment_nutricion_home, fragment_metas, fragment_macros_detalle, fragment_graficos_nutricion, view_empty_nutricion, item_macro_detail, item_micro_detail |
| **Modelos** | MacroDetail, MicroDetail |

#### **feature-progreso Logic** ✅ COMPLETADO
| Componente | Descripción |
|------------|-------------|
| **ProgresoHomeFragment** | Resumen peso actual/inicial/diferencia, grid 4 acciones rápidas, botones exportar/gráficos |
| **PesoFragment** | Registro peso con fecha/nota, historial RecyclerView, empty state |
| **MedidasFragment** | Registro medidas corporales (pecho, cintura, cadera, brazos, muslos, pantorrillas, cuello, hombro) |
| **FotosFragment** | Fotos progreso (frontal, lateral, espalda), galería, cámara |
| **ExportarFragment** | Exportar datos CSV/JSON, rango fechas, selección datos (peso, medidas, fotos) |
| **GraficosProgresoFragment** | Gráficos MPAndroidChart: tendencia peso 7/30/90 días/todo, medidas, periodo selector chips |
| **PesoViewModel** | Registro peso con validación, ProgresoRepository, fecha actual por defecto |
| **ProgresoRepository/DAO** | Room entities (PesoEntity), sync fields, LiveData history |
| **Navigation Safe Args** | Directions classes para navegación type-safe entre fragments |
| **Layouts** | fragment_progreso_home, fragment_peso, fragment_medidas, fragment_fotos, fragment_exportar, fragment_graficos_progreso, view_empty_progreso, view_empty_peso, item_peso_historial |
| **Modelos** | PesoEntry (pesoStr, diffStr formatted), PesoEntity |
| **Drawables** | ic_scale, ic_measure, ic_timeline, ic_download, ic_camera, ic_calendar, bg_circular_icon (core-ui) |

#### **feature-recetas Logic** ✅ COMPLETADO
| Componente | Descripción |
|------------|-------------|
| **RecetasHomeFragment** | Lista recetas con búsqueda, filtros (todas/favoritas/recientes), ordenamiento, empty state, FAB crear |
| **CrearRecetaFragment** | Formulario completo: nombre, descripción, categoría, tiempos, porciones, dificultad, foto, ingredientes dinámicos, instrucciones |
| **ImportarRecetaFragment** | Importar desde URL, texto pegado o escaneo OCR (placeholder) |
| **ColeccionesFragment** | Gestión de colecciones de recetas (crear, listar, agregar recetas) |
| **RecetaDetalleFragment** | Vista completa con imagen, info nutricional, ingredientes, instrucciones, agregar al diario, compartir |
| **RecetasHomeViewModel** | Lista recetas con búsqueda/filtro/orden, toggle favorito, delete |
| **CrearRecetaViewModel** | Formulario validado, ingredientes dinámicos (add/remove), cálculo nutrición placeholder |
| **RecetaRepository/DAO** | Room entities (RecetaEntity, IngredienteRecetaEntity), sync fields, JSON ingredientes, LiveData queries |
| **Navigation Safe Args** | Directions classes para navegación type-safe entre fragments |
| **Layouts** | fragment_recetas_home, fragment_crear_receta, fragment_importar_receta, fragment_colecciones, fragment_receta_detalle, view_empty_recetas, item_receta, item_ingrediente |
| **Modelos** | Receta (nombre, categoria, tiempos, porciones, dificultad, ingredientes[], nutrición), IngredienteReceta |
| **Drawables** | ic_book, ic_clock, ic_people, ic_fire, ic_favorite_filled/outline, ic_search, ic_chevron_down, ic_add, ic_camera, ic_image, ic_delete (core-ui) |

#### **feature-escaneo Logic** ✅ COMPLETADO
| Componente | Descripción |
|------------|-------------|
| **EscanearHomeFragment** | Grid 4 acciones: código barras, comida IA, historial, buscar manual; historial reciente con RecyclerView |
| **EscaneoCodigoBarrasFragment** | ML Kit Barcode Scanning, vista cámara, overlay guiado, resultado sheet con macros y agregar al diario |
| **EscanearComidaFragment** | Placeholder para Passio Food Scan IA, detección alimentos, selección múltiple, ajuste porciones |
| **HistorialEscaneosFragment** | Lista escaneos con tipo (código/IA), fecha, estado agregado, chip tipo, binding adapters |
| **ResultadoEscaneoSheet** | Bottom sheet con info producto, macros, selector porción/tipo comida, confirmar agregar al diario |
| **EscanearHomeViewModel** | Historial escaneos por tipo, load/delete, marcar como agregado al diario |
| **EscaneoRepository/DAO** | Room entity (EscaneoEntity), sync fields, LiveData queries por tipo |
| **Navigation Safe Args** | Directions classes para navegación type-safe entre fragments |
| **Binding Adapters** | EscaneoBindingAdapters: tipoIcon, tipoText, agregadoText, agregadoColor (core-ui) |
| **Layouts** | fragment_escanear_home, fragment_escaneo_codigo_barras, fragment_escanear_comida, fragment_historial_escaneos, sheet_resultado_escaneo, view_empty_escaneo, view_empty_historial_escaneos, item_historial_escaneo |
| **Modelos** | EscaneoEntry (tipo, productoNombre, marca, codigoBarras, nutrición, agregadoAlDiario) |
| **Drawables** | ic_barcode, ic_ai_food, ic_history (core-ui) |

#### **feature-agua Logic** ✅ COMPLETADO
| Componente | Descripción |
|------------|-------------|
| **AguaHomeFragment** | Circular progress 150dp, metas diarias, quick-add 100/250/500ml + custom, grid 2x2 acciones (vaso/botella/meta/historial), historial reciente 5 items, empty state |
| **ConfigurarMetaFragment** | Input numérico con validación (1-10000ml), presets 2000/2500/3000ml, guardado persistente |
| **HistorialAguaFragment** | Lista completa con RecyclerView, fecha/hora/cantidad, empty state, swipe-to-delete |
| **AguaHomeViewModel** | Circular progress % en tiempo real, consumido/meta/restante, historial load, quick-add con refresh automático |
| **ConfigurarMetaViewModel** | Presets, input validation, meta guardada en preferencias |
| **HistorialAguaViewModel** | Lista completa con delete, loading states |
| **WaterRepository/DAO** | Room entity (WaterEntity), sync fields, LiveData queries por fecha, totales diarios |
| **Navigation Safe Args** | Directions classes para navegación type-safe (AguaHome → ConfigurarMeta / HistorialAgua) |
| **Layouts** | fragment_agua_home, fragment_configurar_meta, fragment_historial_agua, item_historial_agua, view_empty_agua, view_empty_historial_agua |
| **Modelos** | WaterEntry (cantidadMl, fecha, hora, timestamp, tipoVaso) |
| **Drawables** | ic_water_drop, ic_bottle, ic_target (core-ui) |

#### **feature-configuracion Logic** ✅ COMPLETADO
| Componente | Descripción |
|------------|-------------|
| **CuentaFragment** | Configuración cuenta: nombre, email, autenticación 2FA, cambiar contraseña |
| **PerfilFragment** | Perfil físico: altura, peso, grasa corporal, nivel actividad, objetivo |
| **MetasFragment** | Metas calóricas/macros, toggle Net Carbs, metas por comida (desayuno/almuerzo/cena/merienda) |
| **NotificacionesFragment** | Push, resumen semanal, logros, recordatorios (agua, comida, ejercicio, peso) |
| **AparienciaFragment** | Tema (claro/oscuro/sistema), animaciones, reducción movimiento |
| **UnidadesFragment** | Sistema métrico/imperial, kcal/kJ, mostrar macros por gramo o % |
| **SyncFragment** | Auto-sync, solo WiFi, sincronizar ahora, pendientes count, última sync |
| **ExportarFragment** | CSV/JSON, rango fechas, selección datos (diario, ejercicio, progreso, recetas, agua, etc.) |
| **CostosFragment** | Toggle seguimiento costos, moneda (ARS), alerta precio >30 días |
| **PresupuestoFragment** | Meta semanal, LinearProgressIndicator, estado vs meta, items sin precio |
| **ViewModels** | 10 ViewModels con LiveData, validaciones, persistencias |
| **Layouts** | 10 fragment XMLs + nav_configuracion.xml con 10 destinos + actions |
| **Drawables** | ic_person, ic_email, ic_lock, ic_shield, ic_target, ic_scale, ic_bottle, ic_download, ic_calendar, ic_visibility (core-ui) |

#### **feature-perfil Logic** ✅ COMPLETADO
| Componente | Descripción |
|------------|-------------|
| **PerfilHomeFragment** | Dashboard perfil: avatar, nombre, email, stats rápidos, acciones |
| **EditarPerfilFragment** | Formulario completo: nombre, apellido, email, fecha nacimiento, sexo, altura, peso, grasa corporal, nivel actividad, objetivo |
| **LoginFragment** | Auth local email/password, navegar a registro, validaciones |
| **RegistroFragment** | Registro con validación email, password match, mínimo 8 chars, términos |
| **SyncAccountFragment** | Vincular/desvincular cuenta nube, estado sync, email servidor |
| **CambiarPasswordFragment** | Password actual, nueva, confirmar, validación fortaleza |
| **ViewModels** | 6 ViewModels con LiveData, validaciones, navegación Safe Args |
| **Layouts** | 6 fragment XMLs + nav_perfil.xml con 6 destinos + actions (login↔registro) |
| **Navigation Safe Args** | Directions classes: PerfilHome → EditarPerfil/Login/Registro/SyncAccount, Login→Registro, Registro→Login |

### 📋 **Planificados (Paso 7 - Resto / Paso 8-9)**
| Paso | Descripción |
|------|-------------|
| **7 (resto)** | feature-insights, feature-planes |
| **8** | Advanced Features: Health Connect, Voice Logging completo, CrossFit WOD, Dynamic Macros, Streaks, Mood |
| **9** | Testing, CI/CD, Release |

---

## 🗄️ Base de Datos (Room) - 15 Entidades Offline-First

Todas las entidades incluyen campos de sincronización:
```java
// Campos Offline-First (en TODAS las entidades)
boolean isSynced = false;           // Pendiente de sync
String syncAction = "CREATE";       // CREATE | UPDATE | DELETE
String serverId;                    // ID en backend
long timestamp;                     // Creación local
long lastModified = System.currentTimeMillis(); // Última modificación
```

| Entidad | Descripción | Índices Clave |
|---------|-------------|---------------|
| **User** | Usuario local + credenciales | email (unique) |
| **FoodLog** | Entrada diario alimentos | userId + dateKey + timestamp |
| **WorkoutLog** | Sesión de ejercicio | userId + dateKey + timestamp |
| **WorkoutSet** | Series/reps/peso por ejercicio | workoutLogId |
| **Exercise** | Maestro ejercicios (Fuerza/Olímpico/Gymnastics/Cardio) | name + category + isCustom |
| **Food** | Alimentos base (FTS5 search) | name + category |
| **Recipe** | Recetas usuario/públicas | userId + isPublic |
| **RecipeIngredient** | Ingredientes de receta | recipeId + orderIndex |
| **WaterLog** | Vasos de agua | userId + dateKey |
| **ProgressPhoto** | Fotos progreso (uri local) | userId + dateKey |
| **PersonalRecordEntity** | PRs (1RM, volumen, tiempo) | userId + exerciseId + dateKey |
| **GlucoseLog** | Glucosa mg/dL + tipo comida | userId + dateKey + timestamp |
| **MoodEntry** | Estado ánimo 1-5 + nota | userId + dateKey |
| **UserNote** | Notas libres | userId + dateKey |
| **WeatherSnapshot** | Clima correlacionado rendimiento | dateKey + timestamp |
| **Streak** | Rachas diarias | userId + dateKey |
| **FoodProduct** | Catálogo offline-first (sin sync fields, solo lectura local) + **costos opcionales** | barcode (unique), name, is_fresh_food |

---

## 🎨 Sistema de Diseño (UI_DESIGN_SYSTEM.md - Fuente de Verdad)

### Paleta Material 3 (Tokens `md_theme_*`)
| Token | Light | Dark | Uso |
|-------|-------|------|-----|
| `md_theme_primary` | `#006C4C` | `#6CDBAC` | Acciones principales, FAB |
| `md_theme_secondary` | `#4D6357` | `#B3CCBE` | Secundarios, acentos |
| `md_theme_background` | `#F5FBF6` | `#0F1512` | Fondo app |
| `md_theme_surface` | `#FFFFFF` | `#171D1A` | Tarjetas, sheets |
| `md_theme_onSurface` | `#171D1A` | `#DFE4DF` | Texto principal |

### Tokens Semánticos
| Categoría | Tokens |
|-----------|--------|
| **Proteínas** | `macro_protein` `#2563EB` / `#60A5FA` |
| **Carbohidratos** | `macro_carbs` `#D97706` / `#FBBF24` |
| **Grasas** | `macro_fat` `#7C3AED` / `#A78BFA` |
| **Hidratación** | `water_primary` `#0284C7` / `#38BDF8` |
| **CrossFit** | `crossfit_accent` `#EA580C` / `#FB923C` |
| **Streaks** | `streak_active` `#F59E0B` / `#FBBF24` |
| **Mood** | Positive `#10B981` / Neutral `#6366F1` / Low `#F43F5E` |

### Formas (Corner Radius)
| Componente | Radio | Token |
|------------|-------|-------|
| Chips / Chips Filter | 4dp | `CornerExtraSmall` |
| Botones / FAB / Inputs | 50% (Pill) | `Pill` |
| Items RecyclerView | 8dp | `CornerSmall` |
| Dialogs / Cards List | 16dp | `CornerLarge` |
| **Tarjetas Principales** | **24dp** | **`Card`** |
| **Bottom Sheets** | **28dp** | **`CornerExtraLarge`** |

### 4 Componentes Base Obligatorios
```xml
<!-- 1. Encabezado con selector fecha + avatar -->
view_header_date.xml

<!-- 2. Tarjeta macros: CircularProgress 150dp + 3 LinearProgress -->
card_macro_summary.xml

<!-- 3. Fila log: icono circular + título 15sp Bold + subtítulo + valor -->
item_log_entry.xml

<!-- 4. BottomSheet base: píldora arrastre + título 18sp Bold + botón full-width -->
sheet_base_container.xml
```

---

## 🚀 Inicio Rápido

### Prerrequisitos
- **JDK 17+** (recomendado: Temurin/Eclipse Adoptium)
- **Android SDK** (API 28+ / compileSdk 34)
- **Gradle 8.11+** (incluido via wrapper)

### Clonar y Compilar
```bash
git clone https://github.com/gml33/lupefit.git
cd lupefit

# Configurar JAVA_HOME (macOS ejemplo)
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home

# Compilar APK Debug
./gradlew :app:assembleDebug

# APK generado en:
# app/build/outputs/apk/debug/app-debug.apk
```

### Backend (Docker)
```bash
cd backend
docker-compose build
docker-compose up -d

# Ver logs
docker-compose logs -f backend

# Test endpoints
curl http://localhost:8000/health
curl -X POST http://localhost:8000/api/v1/auth/register -H "Content-Type: application/json" -d '{"email": "test@lupefit.app", "password": "testpass123"}'
```

### Estructura del Proyecto
```text
LuPeFit/
├── app/                          # Módulo principal
│   ├── src/main/java/com/lupefit/app/
│   │   ├── MyApplication.java           # Application class + SyncWorkerFactoryProvider
│   │   ├── AppContainer.java            # Manual DI (218+ líneas)
│   │   ├── data/local/                  # Room DB, Entities, DAOs, Repositories
│   │   │   ├── entity/                  # 15 Entidades Room (offline-first)
│   │   │   ├── dao/                     # 15 DAOs con LiveData queries
│   │   │   ├── repository/              # DiarioRepositoryImpl, FoodCatalogRepositoryImpl, ExerciseRepositoryImpl
│   │   │   ├── mapper/                  # DiarioMapper
│   │   │   └── preferences/             # UserPreferencesManager
│   │   ├── domain/                      # Repositories, Engines, UseCases
│   │   │   ├── engine/                  # MacroCalculator, StreakManager, HealthConnect, SyncEngine, Voice, Budget
│   │   │   ├── repository/              # 14 Repository interfaces
│   │   │   └── usecase/                 # 7 UseCases
│   │   └── ui/                          # Activities, Fragments, Sheets
│   │       ├── diario/                  # DiarioVozSheet
│   │       ├── escaneo/                 # EscaneoCodigoBarrasFragment, ResultadoEscaneoSheet
│   │       └── main/                    # MainActivity, MasBottomSheet
│   ├── src/main/res/                    # Recursos app (attrs, xml, mipmap, navigation, drawable)
│   └── build.gradle                     # Dependencias app
├── core/
│   ├── core-domain/              # Entidades dominio, interfaces, providers
│   │   ├── src/main/java/com/lupefit/core/domain/
│   │   │   ├── model/                  # DiarioEntry, FoodProduct, MacroTotals, Exercise
│   │   │   ├── repository/             # DiarioRepository, FoodCatalogRepository, ExerciseRepository
│   │   │   ├── engine/voice/           # VoiceTranscriptionEngine, NativeAndroidVoiceEngine, VoiceFoodParser
│   │   │   └── provider/               # Repository Providers
│   │   └── build.gradle.kts
│   ├── core-ui/                  # Tema M3, Styles, Components, Strings AR
│   │   ├── src/main/res/
│   │   │   ├── values/colors.xml          # Paleta Light (tokens md_theme_*)
│   │   │   ├── values-night/colors.xml    # Paleta Dark
│   │   │   ├── values/themes.xml          # Tema Light (480+ líneas)
│   │   │   ├── values-night/themes.xml    # Tema Dark
│   │   │   ├── values/styles.xml          # 20+ Widget Styles
│   │   │   ├── values/dimens.xml          # Espaciado, elevation, touch targets
│   │   │   ├── values/integers.xml        # Duraciones animaciones
│   │   │   ├── color/*.xml                # 12 ColorStateLists
│   │   │   ├── drawable/*.xml             # Icons, backgrounds
│   │   │   ├── layout/                    # 4 Componentes base
│   │   │   └── values-es-rAR/strings.xml  # 365 strings argentino
│   │   └── build.gradle.kts
│   ├── core-network/             # Retrofit, API Service, DTOs, SyncEngine ✅
│   │   └── build.gradle.kts
│   └── core-sync/                # WorkManager, SyncQueue, ConflictResolver ✅
│       └── build.gradle.kts
├── feature/                      # 11 Feature Modules
│   ├── feature-diario/           # DiarioHomeFragment, DiarioVozSheet, nav_diario.xml
│   ├── feature-ejercicio/        # EjercicioHomeFragment, nav_ejercicio.xml
│   ├── feature-nutricion/        # NutricionHomeFragment, nav_nutricion.xml
│   ├── feature-progreso/         # ProgresoHomeFragment, nav_progreso.xml
│   ├── feature-recetas/          # RecetasHomeFragment, nav_recetas.xml
│   ├── feature-escaneo/          # EscanearHomeFragment, nav_escaneo.xml
│   ├── feature-agua/             # AguaHomeFragment, nav_agua.xml
│   ├── feature-configuracion/    # ConfiguracionHomeFragment, nav_configuracion.xml
│   ├── feature-perfil/           # PerfilHomeFragment, nav_perfil.xml
│   ├── feature-insights/         # InsightsHomeFragment, nav_insights.xml
│   └── feature-planes/           # PlanesHomeFragment, nav_planes.xml
├── backend/                      # FastAPI + PostgreSQL + Docker
│   ├── app/                      # models, schemas, api/v1/endpoints, core
│   ├── requirements.txt
│   ├── Dockerfile
│   ├── docker-compose.yml
│   └── alembic/
├── gradle/
│   ├── libs.versions.toml        # Version Catalog
│   └── wrapper/                  # Gradle 8.11.1
├── settings.gradle.kts           # include(:app, :core:core-domain, :core:core-ui, :core:core-network, :core:core-sync, :feature:feature-*)
├── build.gradle.kts              # Root plugins
├── gradle.properties             # JVM args, Android configs
├── UI_DESIGN_SYSTEM.md           # Fuente de verdad diseño
├── features.md                   # Documento funcionalidades
├── avances.md                    # Historial desarrollo
└── README.md                     # Este archivo
```

---

## 📋 Estado del Desarrollo

| Paso | Descripción | Estado | Commit |
|------|-------------|--------|--------|
| **1** | **Esqueleto Base Compilable** | ✅ **COMPLETADO** | `d9942ea` |
| **2** | **Core Network Module** | ✅ **COMPLETADO** | `9414165` |
| **3** | **Core Sync Module** | ✅ **COMPLETADO** | `b4d06cd` |
| **4** | **Escáner + Catálogo + Voz + Costos** | ✅ **COMPLETADO** | `a96505d` |
| **5** | **Backend FastAPI + Docker Compose** | ✅ **COMPLETADO** | - |
| **6** | **Feature Modules (11 módulos UI)** | ✅ **COMPLETADO** | `a96505d` |
| **7** | **Feature Modules Logic (feature-diario, feature-ejercicio)** | ✅ **COMPLETADO** | `0f7a83d` |
| 7 (resto) | Feature Modules Logic (resto de módulos) | 🔄 En progreso | - |
| 8 | Advanced Features (Health Connect, Voice, CrossFit, Macros Dinámicos, Streaks, Mood) | ⏳ Pendiente | - |
| 9 | Testing, CI/CD, Release | ⏳ Pendiente | - |

### ✅ Paso 1 - Entregables Verificados
- [x] `build.gradle` (app) con dependencias exactas
- [x] `MyApplication.java` + `AppContainer.java` (DI Manual)
- [x] `AppDatabase.java` + 15 Entidades Room (offline-first)
- [x] 15 DAOs con LiveData queries
- [x] 14 Repositories + 5 Engines + 7 UseCases (stubs compilables)
- [x] `core:core-ui` - Tema M3 completo + 4 componentes base
- [x] `attrs.xml` - Atributos Material 3 custom
- [x] **APK Debug compila y genera exitosamente** (`./gradlew :app:assembleDebug`)

### ✅ Paso 4 - Entregables Verificados
- [x] `FoodProduct` Entity + DAO con búsqueda relevancia + índices
- [x] `fresh_foods.json` - 30 alimentos frescos argentinos precargados
- [x] `AppDatabase` Callback onCreate - precarga automática primer arranque
- [x] CameraX + ML Kit Barcode Scanner - PreviewView full-screen, overlay redondeado
- [x] `FoodCatalogRepository` Offline-First - Room → Open Food Facts → Room → Fallback
- [x] `VoiceTranscriptionEngine` Strategy Pattern - interfaz + Listener + ParsedFoodItem
- [x] `NativeAndroidVoiceEngine` - SpeechRecognizer nativo `es-AR`, resultados parciales
- [x] `WhisperServerVoiceEngine` - MediaRecorder MPEG-4 AAC 16kHz + OkHttp Multipart
- [x] `VoiceFoodParser` - NLP español, extrae cantidad/unidad, busca catálogo, calcula macros
- [x] `DiarioVozSheet` - BottomSheet 28dp, micrófono animado, transcripción live, lista editable
- [x] `ResultadoEscaneoSheet` - Producto detectado, selector gramos, resumen calculado
- [x] Layouts XML Material 3 - sheet_resultado_escaneo.xml, sheet_diario_voz.xml, item_parsed_food.xml, item_macro_mini.xml
- [x] **Cost Tracking** - referencePrice, referenceQuantityGrams, priceUpdatedAt, badge >30 días
- [x] **Presupuesto Semanal** - Meta configurable, LinearProgressIndicator, items sin precio
- [x] `UserPreferencesManager` - Feature toggle `enable_cost_tracking`, `weekly_budget_goal`, `currency_code` (ARS)
- [x] `CostBudgetEngine` - Motor completo: costo por porción, costo por 10g proteína, presupuesto semanal
- [x] Integración DI - AppContainer: FoodProductDao, FoodCatalogRepository, UserPreferencesManager wireados
- [x] **Build verificado**: BUILD SUCCESSFUL

### ✅ Paso 5 - Entregables Verificados
- [x] Estructura completa FastAPI - models, schemas, api/v1/endpoints, core
- [x] 17 SQLAlchemy Models con UUID, índices, relaciones, timestamps, offline-first fields
- [x] 15 Pydantic Schemas Request/Response
- [x] 16 Routers API v1 (Auth, Users, FoodLogs, WorkoutLogs, Exercises, Foods, Recipes, WaterLogs, ProgressPhotos, PersonalRecords, GlucoseLogs, MoodEntries, UserNotes, WeatherSnapshots, Streaks, Sync)
- [x] JWT Auth RS256, access 30min, refresh 7d, bcrypt
- [x] Sync Batch Endpoint versionado con resolución conflictos
- [x] Docker Compose 5 servicios con healthchecks
- [x] Alembic Migrations configuradas
- [x] Health Check `/health` endpoint
- [x] CORS Configurado
- [x] **Build y Deploy Verificado** - docker-compose build + up successful

### ✅ Paso 6 - Entregables Verificados
- [x] 11 Feature Modules agregados a settings.gradle.kts
- [x] Navigation Graphs XML (11 archivos nav_*.xml)
- [x] ViewModels base (11 clases)
- [x] Home Fragments base (11 fragmentos)
- [x] Layouts base con MaterialToolbar (11 layouts)
- [x] Strings localizadas por módulo (11 archivos)
- [x] Main Activity - BottomNavigationView (5 tabs) + MasBottomSheet (7 opciones)
- [x] 13 Vector Drawables para navegación y más opciones
- [x] **Build verificado**: BUILD SUCCESSFUL (`./gradlew :app:assembleDebug`)

### ✅ Paso 7 - feature-diario Logic COMPLETADO
- [x] `BuscarAlimentoFragment`: Búsqueda alimentos con filtros categoría, chips, RecyclerView con macros
- [x] `QuickAddFragment`: Alimentos frescos, recientes, favoritos con selección rápida
- [x] `EditarPorcionSheet`: Selector gramos (+/- chips 50/100/150/200/250/300g), selector tipo comida, macros calculados en vivo
- [x] `DiarioCrearAlimentoFragment`: Creación manual de alimentos con macros, porción, fresco
- [x] `DiarioHomeFragment`: Navegación integrada, `currentDateKey`, callback `DiarioVozSheet`, `EscaneoCodigoBarrasFragment`
- [x] `DiarioVozSheet callback`: Registra alimentos parseados por voz directamente al diario
- [x] ViewModels: `BuscarAlimentoViewModel`, `QuickAddViewModel`, `DiarioCrearAlimentoViewModel`
- [x] Layouts: `fragment_buscar_alimento`, `fragment_quick_add`, `fragment_diario_crear_alimento`, `item_food_search_result`, `item_quick_add_food`, `sheet_editar_porcion`
- [x] Menús: `menu_buscar_alimento`, `menu_crear_alimento`
- [x] **Build verificado**: BUILD SUCCESSFUL

### ✅ Paso 7 - feature-ejercicio Logic COMPLETADO
- [x] `BuscarEjercicioFragment`: Búsqueda ejercicios con filtros categoría (Fuerza/Olímpico/Gymnastics/Cardio/Strongman/CrossFit), patrón movimiento, equipamiento
- [x] `NuevaRutinaFragment`: Crear rutinas agregando ejercicios con series, reps, peso, %1RM, descanso
- [x] `WodTimerFragment`: Timer fullscreen para AMRAP, EMOM, For Time, Tabata con alertas sonoras/vibración
- [x] `RutinaDetalleFragment`: Ver ejercicios de rutina, iniciar entrenamiento
- [x] `EjercicioHomeFragment`: Grid 4 accesos rápidos (Buscar, Nueva, Timer, Mis rutinas) + lista entrenamientos recientes
- [x] `BuscarEjercicioViewModel`: Búsqueda con filtros categoría/patrón, LiveData
- [x] `NavigationActions`: Inicialización IDs navegación via reflexión `R.id.action_*`
- [x] Core-domain: `Exercise` model, `ExerciseRepository` interface/provider
- [x] App: `ExerciseRepositoryImpl` (Room entity ↔ domain mapping)
- [x] Drawables: `ic_add`, `ic_timer`, `ic_list`, `ic_dumbbell`
- [x] Strings: categorías, patrones, equipamiento, tipos WOD
- [x] **Build verificado**: BUILD SUCCESSFUL (APK 48MB)

### ✅ Paso 7 - feature-agua Logic COMPLETADO
- [x] `AguaHomeFragment`: Circular progress 150dp, quick-add 100/250/500ml, grid 2x2 acciones, historial reciente 5 items, empty state
- [x] `ConfigurarMetaFragment`: Input numérico validado (1-10000ml), presets 2000/2500/3000ml, guardado persistente
- [x] `HistorialAguaFragment`: Lista completa RecyclerView, fecha/hora/cantidad, empty state, delete
- [x] ViewModels: `AguaHomeViewModel`, `ConfigurarMetaViewModel`, `HistorialAguaViewModel`
- [x] `WaterRepository/DAO`: Room entity (WaterEntity), sync fields, LiveData queries por fecha, totales diarios
- [x] Navigation Safe Args: Directions classes para navegación type-safe
- [x] Layouts: `fragment_agua_home`, `fragment_configurar_meta`, `fragment_historial_agua`, `item_historial_agua`, `view_empty_agua`, `view_empty_historial_agua`
- [x] Modelos: `WaterEntry` (cantidadMl, fecha, hora, timestamp, tipoVaso)
- [x] Drawables: `ic_water_drop`, `ic_bottle`, `ic_target` (core-ui)
- [x] **Build verificado**: BUILD SUCCESSFUL

---

## 🐳 Backend (Paso 5 - COMPLETADO)

```yaml
# docker-compose.yml (implementado)
services:
  postgres:
    image: postgres:16-alpine
    environment:
      POSTGRES_DB: lupefit
      POSTGRES_USER: lupefit
      POSTGRES_PASSWORD: ${DB_PASSWORD}
    volumes:
      - postgres_data:/var/lib/postgresql/data
    ports: ["5432:5432"]

  redis:
    image: redis:7-alpine
    ports: ["6379:6379"]

  backend:
    build: ./backend
    ports: ["8000:8000"]
    environment:
      DATABASE_URL: postgresql://lupefit:***@postgres:5432/lupefit
      REDIS_URL: redis://redis:6379
      JWT_SECRET: ${JWT_SECRET}
    depends_on: [postgres, redis]

  celery-worker:
    build: ./backend
    command: celery -A app.tasks worker --loglevel=info
    depends_on: [redis, postgres]

  celery-beat:
    build: ./backend
    command: celery -A app.tasks beat --loglevel=info
    depends_on: [redis, postgres]
```

**Endpoints Principales (Implementados):**
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/v1/auth/register` | Registro email/password |
| POST | `/api/v1/auth/login` | Login + JWT tokens |
| POST | `/api/v1/auth/refresh` | Refresh access token |
| POST | `/api/v1/sync/batch` | **Sync bidireccional versionado** |
| GET | `/api/v1/foods/search` | Búsqueda alimentos (FTS5) |
| CRUD | `/api/v1/food-logs` | Diario alimentos |
| CRUD | `/api/v1/workout-logs` | Ejercicios |
| CRUD | `/api/v1/progress` | Peso, medidas, fotos |
| CRUD | `/api/v1/recipes` | Recetas + ingredientes |

---

## 🧪 Testing & Calidad

```bash
# Tests unitarios
./gradlew test

# Tests instrumentados (requiere device/emulator)
./gradlew connectedAndroidTest

# Lint
./gradlew lint

# Verificar dependencias
./gradlew dependencyUpdates
```

---

## 📄 Documentación del Proyecto

| Archivo | Descripción |
|---------|-------------|
| `UI_DESIGN_SYSTEM.md` | **Fuente de verdad** - Paleta, tipografía, formas, componentes, convenciones |
| `features.md` | Funcionalidades detalladas por módulo, schema DB, API endpoints |
| `avances.md` | Historial cronológico de hitos, decisiones, próximos pasos |
| `project_roadmap.md` | Roadmap visual por fases |
| `README.md` | Este archivo (actualizado por commit) |

---

## 🤝 Contribuir

1. Fork del repo
2. Rama feature: `git checkout -b feature/nueva-funcionalidad`
3. Commit convencional: `git commit -m "feat: descripción clara"`
4. Push: `git push origin feature/nueva-funcionalidad`
5. Pull Request

### Convención de Commits
```
feat:     Nueva funcionalidad
fix:      Corrección de bug
docs:     Documentación
style:    Formato (sin cambios lógicos)
refactor: Refactorización
test:     Tests
chore:    Mantenimiento, build, deps
```

---

## 📜 Licencia

**MIT License** - Libre para uso personal y comercial.

```
MIT License

Copyright (c) 2026 LuPeFit

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software...
```

---

## 🙏 Agradecimientos

- **Material Design 3** - Sistema de diseño
- **Room Database** - Persistencia offline-first
- **MPAndroidChart** - Gráficos nutricionales
- **ML Kit** - Escaneo códigos de barras
- **Health Connect** - Datos de salud unificados
- **Open Food Facts** - Base de datos alimentos abierta
- **SpeechRecognizer** - Transcripción voz nativa Android

---

## 📞 Contacto & Links

- **Repo**: https://github.com/gml33/lupefit
- **Issues**: https://github.com/gml33/lupefit/issues
- **Wiki**: https://github.com/gml33/lupefit/wiki (próximamente)

---

> **Desarrollado con ❤️ en Argentina** | **Stack**: Java 17 • XML Views • Room • Material 3 • WorkManager • FastAPI • PostgreSQL • Docker

**Última actualización**: 2026-11-22 | **Versión**: 1.0.0-Paso7-configuracion-perfil | **Build**: `latest` ✅
# Avances - LuPeFit: Replicación y Rediseño Completo

## Estado Actual: **PASO 6 - FEATURE MODULES (11 MÓDULOS UI) ESTRUCTURA COMPLETA** ✅

### Fecha de Inicio: 2026-11-20
### Fase Actual: Implementación - Paso 6 Completado (Estructura Feature Modules)

---

## Hitos Completados

### ✅ Fase 1: Ingeniería Reversa App de Referencia (COMPLETA)

#### Hito 1: Información Básica del APK (REA)
- Package: `com.myfitnesspal.android`, v22.24.2, SHA256 verificado

#### Hito 2: Manifest y Recursos (REA)
- Manifest completo (582 líneas) + 2000 strings extraídos

#### Hito 3: Desempaquetado Completo (apktool)
- 12 DEX files, assets/config, lib/ (4 .so × 4 archs), res/, navigation graphs

#### Hito 4: Decompilación Java (jadx)
- 32,783 clases, 40+ feature modules identificados

#### Hito 5: Análisis Multi-DEX → Feature Modules
- 12 DEX mapeados, 3 Dynamic Features confirmados (nutrition_insights, plans, recipe_collection)

#### Hito 6: Análisis Assets/Config JSON
- **abtests.json**: 180+ experimentos A/B
- **mpf_config.json**: 4 grupos beneficios, 20+ features premium
- **upsell.json**: 15 features con sort_order, hero config

#### Hito 7: Native Libraries (.so)
- libpassiolib.so, libbarhopper_v2.so, libshinobicharts-android.so, libtensorflowlite_jni.so

#### Hito 8: Navigation Graphs
- 3 graphs: if_nav_graph, create_recipe_manual_nav_graph, app_gallery_nav_graph

#### Hito 9: Documento de Decisiones (features.md v1)
- Module Map, SDK Plan, Feature Flags, Premium Gating Rules

---

### ✅ Fase 2: Diseño Arquitectónico LuPeFit (COMPLETA)

#### Hito 10: Definición de Stack y Arquitectura
- **Frontend**: Java 17+ + XML Views (tradicional), Feature-modular (Enfoque A)
- **Backend**: Python + FastAPI + PostgreSQL (Docker)
- **Auth**: Email + password (local + sync)
- **Sync**: Offline-first (Room + WorkManager)
- **Features excluidas**: Blog, Community, Friends, Payments, Fasting
- **Premium**: Eliminado - todas las features GRATIS

#### Hito 11: Capa de Datos (core-data) - Room Schema
- 14 entidades: User, FoodLog, WorkoutLog, WorkoutSet, Exercise, Food, Recipe, RecipeIngredient, WaterLog, ProgressPhoto, PersonalRecordEntity, GlucoseLog, MoodEntry, UserNote, WeatherSnapshot, Streak
- DAOs + Repositories + SyncQueue con reintentos exponenciales
- FTS5 en alimentos para búsqueda

#### Hito 12: Capa de Dominio (core-domain)
- Entidades de dominio puras (Java)
- 14 interfaces Repository
- 7+ Use Cases principales (FoodLog, WorkoutLog, PersonalRecord, GlucoseLog, MoodEntry, UserNote, UserProfile)

#### Hito 13: Capa de Red & Sincronización (core-network, core-sync) ✅ **COMPLETADO**
- ✅ **core:core-network module** - Retrofit + OkHttp + Moshi configurado
- ✅ **LuPeFitApiService** - 30+ endpoints REST definidos (Auth, Sync, Food Logs, Workout Logs, Progress, Recipes, Foods, Water, Exercises, PRs, Glucose, Mood, Notes, Settings)
- ✅ **DTOs completos** - Request/Response para todas las entidades (Auth, Food, Workout, Progress, Recipe, Water, Exercise, PR, Glucose, Mood, Note, Sync, User)
- ✅ **NetworkModule** - Configuración Retrofit/OkHttp/Moshi con AuthInterceptor
- ✅ **SyncEngine** - Motor de sincronización batch bidireccional con CompletableFuture
- ✅ **core:core-sync module** - WorkManager SyncWorker con factory pattern
- ✅ **SyncQueueDao/Repository** - Cola de sincronización offline-first con backoff exponencial
- ✅ **ConflictResolver** - Resolución de conflictos (SERVER_WINS, CLIENT_WINS, MERGE, MANUAL)
- ✅ **SyncScheduler** - Programación de sync periódico (15 min) + constraints (red, batería)
- ✅ **SyncQueueItem entity + DAO** en core-sync + app module (Room)
- ✅ **AppContainer** actualizado con networkSyncEngine (core-network SyncEngine)
- ✅ **MyApplication** implementa SyncWorkerFactoryProvider
- ✅ **Build verificado**: BUILD SUCCESSFUL

#### Hito 14: **PASO 4 - Escáner Offline-First + Open Food Facts + Catálogo JSON + Voz** ✅ **COMPLETADO**
- ✅ **FoodProduct Entity + DAO** - Room entity con macros, `isFreshFood`, Parcelable, índices (barcode unique, name, is_fresh_food)
- ✅ **Precarga JSON (assets/fresh_foods.json)** - 30 alimentos frescos argentinos (carnes, huevos, lácteos, cereales, frutas, verduras) con valores nutricionales estándar por 100g
- ✅ **AppDatabase Callback onCreate** - ExecutorService para precarga automática en primer arranque (búsqueda 0ms offline)
- ✅ **CameraX + ML Kit Barcode Scanner** - PreviewView full-screen, overlay recuadro redondeado `md_theme_primary`, formatos EAN-13, EAN-8, UPC-A, UPC-E, anti-duplicados 1.5s
- ✅ **FoodCatalogRepository (Offline-First)** - 1) Room local → 2) Open Food Facts API (OkHttp+Gson, User-Agent válido) → 3) Guarda en Room para uso offline → 4) Fallback crear alimento manual
- ✅ **VoiceTranscriptionEngine (Strategy Pattern)** - Interfaz con Listener (partial/final/error) + ParsedFoodItem
- ✅ **NativeAndroidVoiceEngine** - SpeechRecognizer nativo `es-AR`, resultados parciales tiempo real
- ✅ **WhisperServerVoiceEngine** - MediaRecorder MPEG-4 AAC 16kHz mono + OkHttp Multipart → `/v1/audio/transcriptions` endpoint configurable
- ✅ **VoiceFoodParser** - NLP español: "200 gramos de pollo y 100 de arroz" → extrae cantidad/unidad, busca en catálogo, calcula macros
- ✅ **DiarioVozSheet** - BottomSheetDialogFragment 28dp, micrófono animado (anillos pulsantes), transcripción live, lista editable con barras macro P/C/G, confirmar todo junto
- ✅ **ResultadoEscaneoSheet** - Producto detectado, selector gramos (+/- chips 100g/porción/200g), resumen calculado dinámico
- ✅ **Layouts XML Material 3** - sheet_resultado_escaneo.xml, sheet_diario_voz.xml, item_parsed_food.xml, item_macro_mini.xml, drawables, iconos
- ✅ **Integración DI** - AppContainer: FoodProductDao, FoodCatalogRepository wireados
- ✅ **Dependencias**: gson 2.11.0 agregada
- ✅ **Build verificado**: BUILD SUCCESSFUL

#### Hito 15: **PASO 4.5 - Estimación de Costos + Presupuesto Nutricional** ✅ **COMPLETADO**
- ✅ **FoodProduct Entity extendido** - `referencePrice` (0.0 default), `referenceQuantityGrams` (100.0 default), `priceUpdatedAt` (timestamp)
- ✅ **FoodLog Entity extendido** - `estimatedCostSnapshot` para congelar costo histórico (inflación)
- ✅ **UserPreferencesManager** - SharedPreferences con feature toggle `enable_cost_tracking` (default true), `weekly_budget_goal`, `currency_code` (ARS default)
- ✅ **CostBudgetEngine** - Motor de cálculo completo:
  - `calculateServingCost(product, grams)` - (referencePrice / referenceQuantityGrams) * consumedGrams
  - `isPriceOutdated(priceUpdatedAt)` - true si >30 días (alerta inflación)
  - `calculateCostPer10gProtein(product)` - para FoodSwapEngine ordenar por eficiencia proteica
  - `calculateWeeklyMenuBudget(items, goal)` - BudgetSummary con total, promedio diario, items sin precio, estado vs meta
  - `formatCost(cost, currencyCode)` - NumberFormat.getCurrencyInstance(Locale es-AR)
- ✅ **Layouts XML Material 3** (tokens only):
  - `view_optional_price_input.xml` - Bloque colapsable: header expandible, 2 TextInputLayout (precio + peso), chips rápidos (100g/500g/1kg), badge precio desactualizado (>30 días), botón guardar
  - `card_weekly_budget_summary.xml` - Card 24dp: título + meta, LinearProgressIndicator (acumulado vs meta), status row (estado + items sin precio), botón ver detalle
- ✅ **Integración en ResultadoEscaneoSheet**:
  - Feature toggle respeta `prefsManager.isCostTrackingEnabled()` → `View.GONE` sin espacios vacíos
  - Binding correcto con `binding.includedPriceInput.getRoot()`
  - Carga precio existente → muestra badge si >30 días
  - Guardado actualiza Room + UI en tiempo real
  - Costo estimado aparece en resumen calculado con formato moneda ARS
- ✅ **AppContainer** - UserPreferencesManager wireado
- ✅ **Strings** - costo_*, presupuesto_*, config_* agregados
- ✅ **Build verificado**: BUILD SUCCESSFUL

#### Hito 16: **PASO 5 - Backend FastAPI + Docker Compose** ✅ **COMPLETADO**
- ✅ **Estructura completa FastAPI** - models, schemas, api/v1/endpoints, core (database, security, config)
- ✅ **16 SQLAlchemy Models** - User, FoodLog, WorkoutLog, WorkoutSet, Exercise, Food, FoodProduct, Recipe, RecipeIngredient, WaterLog, ProgressPhoto, PersonalRecord, GlucoseLog, MoodEntry, UserNote, WeatherSnapshot, Streak, SyncQueue (17 con UUID, índices, relaciones, timestamps, campos offline-first)
- ✅ **15 Pydantic Schemas** - Request/Response para Auth, User, FoodLog, WorkoutLog, Exercise, Food, FoodProduct, Recipe, WaterLog, ProgressPhoto, PersonalRecord, GlucoseLog, MoodEntry, UserNote, WeatherSnapshot, Streak, Sync
- ✅ **API v1 Endpoints (16 routers)** - Auth (register, login, refresh, me), Users (me GET/PATCH, change-password, delete), FoodLogs (CRUD + daily-summary), WorkoutLogs, Exercises, Foods, Recipes, WaterLogs, ProgressPhotos, PersonalRecords, GlucoseLogs, MoodEntries, UserNotes, WeatherSnapshots, Streaks, Sync (batch, version, pull)
- ✅ **JWT Auth** - RS256 (configurable), access 30min, refresh 7d, bcrypt password hashing
- ✅ **Sync Batch Endpoint** - `/api/v1/sync/batch` con versionado global, resolución conflictos (4 estrategias por entidad), pull changes
- ✅ **Docker Compose** - 5 servicios: postgres:16, redis:7, backend (uvicorn 4 workers), celery-worker, celery-beat
- ✅ **Alembic Migrations** - Configuración para migraciones automáticas
- ✅ **Health Check** - `/health` endpoint
- ✅ **CORS Configurado** - orígenes permitidos configurables
- ✅ **Build y Deploy Verificado** - docker-compose build + up successful, endpoints testeados (register, login, me, food-logs CRUD, daily-summary, sync version, sync pull)

#### Hito 17: **PASO 6 - Feature Modules (11 Módulos UI + Navigation)** ✅ **COMPLETADO**
- ✅ **Estructura Gradle** - 11 feature modules agregados a settings.gradle.kts
- ✅ **feature-diario** - DiarioHomeFragment, BuscarAlimento, QuickAdd, EscanearCodigoBarras, DiarioVozSheet, DiarioEntryAdapter, nav_diario.xml, menu_diario_home.xml
- ✅ **feature-ejercicio** - EjercicioHomeFragment, BuscarEjercicio, NuevaRutina, WodTimerFragment, nav_ejercicio.xml
- ✅ **feature-nutricion** - NutricionHomeFragment, MetasFragment, MacrosDetalleFragment, GraficosNutricionFragment, nav_nutricion.xml
- ✅ **feature-progreso** - ProgresoHomeFragment, PesoFragment, MedidasFragment, FotosFragment, ExportarFragment, nav_progreso.xml
- ✅ **feature-recetas** - RecetasHomeFragment, CrearRecetaFragment, ImportarRecetaFragment, ColeccionesFragment, RecetaDetalleFragment, nav_recetas.xml
- ✅ **feature-escaneo** - EscanearHomeFragment, EscaneoCodigoBarrasFragment, ResultadoEscaneoSheet, EscanearComidaFragment, nav_escaneo.xml
- ✅ **feature-agua** - AguaHomeFragment, ConfigurarMetaFragment, HistorialAguaFragment, nav_agua.xml
- ✅ **feature-configuracion** - ConfiguracionHomeFragment, CuentaFragment, PerfilFragment, MetasFragment, NotificacionesFragment, AparienciaFragment, UnidadesFragment, SyncFragment, ExportarFragment, CostosFragment, PresupuestoFragment, nav_configuracion.xml
- ✅ **feature-perfil** - PerfilHomeFragment, EditarPerfilFragment, LoginFragment, RegistroFragment, SyncAccountFragment, CambiarPasswordFragment, nav_perfil.xml
- ✅ **feature-insights** - InsightsHomeFragment, SemanalFragment, TendenciasFragment, PrediccionesFragment, PatronesFragment, nav_insights.xml
- ✅ **feature-planes** - PlanesHomeFragment, PlanComidaFragment, PlanEntrenamientoFragment, MisPlanesFragment, PresupuestoPlanFragment, CrearPlanComidaFragment, CrearPlanEntrenamientoFragment, nav_planes.xml
- ✅ **Navigation Graphs XML** - 11 archivos nav_*.xml con startDestinations, actions, y dialog destinations
- ✅ **ViewModels** - 11 ViewModel classes base para cada feature module
- ✅ **Layouts base** - fragment_*_home.xml con MaterialToolbar, NestedScrollView, CoordinatorLayout
- ✅ **Strings localizadas** - values/strings.xml por módulo con terminología argentino
- ✅ **Main Activity** - BottomNavigationView (Diario, Ejercicio, Nutrición, Progreso, Más), MasBottomSheet con 7 opciones
- ✅ **Icons** - 13 vector drawables para bottom navigation y más opciones

---

## Entregables Generados

### Análisis App Referencia (Fase 1)
1. `features.md` - v1: Análisis App Referencia completo
2. `avances.md` - Historial fase 1
3. `manifest.xml` - 582 líneas
4. `strings.json` - 2000 strings
5. `apktool_out/` - Extracción completa
6. `jadx_out/` - 32,783 clases decompiladas
7. `apktool_out/assets/config/abtests.json` (60KB)
8. `apktool_out/assets/config/mpf_config.json` (14KB)
9. `apktool_out/assets/config/upsell.json` (6KB)

### Diseño LuPeFit (Fase 2)
10. `features.md` - v3: **Arquitectura LuPeFit completa**
11. `core/core-ui/src/main/res/values/colors.xml` - Paleta light (tokens `md_theme_*`)
12. `core/core-ui/src/main/res/values-night/colors.xml` - Paleta dark (tokens `md_theme_*`)
13. `core/core-ui/src/main/res/values/themes.xml` - Tema Material 3 light (480+ líneas)
14. `core/core-ui/src/main/res/values-night/themes.xml` - Tema Material 3 dark
15. `core/core-ui/src/main/res/values/styles.xml` - 20+ widget styles (UI_DESIGN_SYSTEM specs)
16. `core/core-ui/src/main/res/values/dimens.xml` - Espaciado, elevation, icon sizes, touch targets
17. `core/core-ui/src/main/res/color/*.xml` - 12 color state lists
18. `core/core-ui/src/main/res/drawable/nav_active_indicator.xml`
19. `core/core-ui/src/main/res/drawable/splash_background.xml`
20. `core/core-ui/src/main/res/values-es-rAR/strings.xml` - 365 strings argentino
21. `core/core-ui/src/main/res/layout/view_header_date.xml` - Componente base 1
22. `core/core-ui/src/main/res/layout/card_macro_summary.xml` - Componente base 2
23. `core/core-ui/src/main/res/layout/item_log_entry.xml` - Componente base 3
24. `core/core-ui/src/main/res/layout/sheet_base_container.xml` - Componente base 4
25. `core/core-ui/src/main/res/drawable/ic_chevron_left.xml` / `ic_chevron_right.xml`
26. `core/core-ui/src/main/res/drawable/ic_avatar_placeholder.xml` / `ic_food_placeholder.xml`
26. `core/core-ui/src/main/res/drawable/bg_rounded_xs.xml`
27. `core/core-ui/build.gradle.kts` + `proguard-rules.pro` + `consumer-rules.pro`

### Paso 1 - Esqueleto Base (App Module)
28. `app/build.gradle` - Dependencias completas (Material 3, Room, Health Connect, WorkManager, Play Services Location, MPAndroidChart, CameraX, ML Kit, Lottie, CircleImageView)
29. `app/src/main/java/com/lupefit/app/MyApplication.java` - Application class
30. `app/src/main/java/com/lupefit/app/AppContainer.java` - Manual DI Container (218+ líneas)
31. `app/src/main/java/com/lupefit/app/data/local/AppDatabase.java` - Room Database singleton
32. `app/src/main/java/com/lupefit/app/data/local/entity/*.java` - 15 Entidades (offline-first)
33. `app/src/main/java/com/lupefit/app/data/local/dao/*.java` - 15 DAOs (LiveData queries)
34. `app/src/main/java/com/lupefit/app/data/local/converters/TypeConverters.java` - TypeConverters
35. `app/src/main/java/com/lupefit/app/domain/repository/*.java` - 14 Repositories (stubs)
36. `app/src/main/java/com/lupefit/app/domain/engine/*.java` - 5 Engines (stubs)
37. `app/src/main/java/com/lupefit/app/domain/usecase/*.java` - 7 UseCases (stubs)
38. `app/src/main/res/values/attrs.xml` - Atributos Material 3 custom
38. `app/src/main/res/xml/data_extraction_rules.xml` + `backup_rules.xml`
39. `app/src/main/res/values/strings.xml` - app_name + escaneo/voz strings
40. `app/src/main/res/mipmap-*/ic_launcher*.xml` - Iconos vectoriales
41. `app/src/main/AndroidManifest.xml` - Permisos, Activities, Providers, Health Connect meta-data

### Paso 4 - Escáner + Catálogo + Voz (App Module)
43. `app/src/main/assets/fresh_foods.json` - 30 alimentos frescos argentinos precargados
44. `app/src/main/java/com/lupefit/app/data/local/entity/FoodProduct.java` - Entity Room + Parcelable
45. `app/src/main/java/com/lupefit/app/data/local/dao/FoodProductDao.java` - DAO con búsqueda relevancia
46. `app/src/main/java/com/lupefit/app/data/local/repository/FoodCatalogRepository.java` - Offline-First OpenFoodFacts
47. `app/src/main/java/com/lupefit/app/ui/escaneo/BarcodeAnalyzer.java` - ML Kit analyzer anti-duplicados
48. `app/src/main/java/com/lupefit/app/ui/escaneo/EscaneoCodigoBarrasFragment.java` - CameraX preview + permisos
49. `app/src/main/java/com/lupefit/app/ui/escaneo/ResultadoEscaneoSheet.java` - BottomSheet selector gramos
50. `app/src/main/java/com/lupefit/app/domain/engine/voice/VoiceTranscriptionEngine.java` - Strategy interface
51. `app/src/main/java/com/lupefit/app/domain/engine/voice/NativeAndroidVoiceEngine.java` - SpeechRecognizer es-AR
52. `app/src/main/java/com/lupefit/app/domain/engine/voice/WhisperServerVoiceEngine.java` - MediaRecorder + OkHttp
53. `app/src/main/java/com/lupefit/app/domain/engine/voice/VoiceFoodParser.java` - NLP español + macros
54. `app/src/main/java/com/lupefit/app/ui/diario/DiarioVozSheet.java` - BottomSheet micrófono animado + lista editable
55. `app/src/main/res/layout/fragment_escaneo_codigo_barras.xml` - Preview + overlay recuadro
56. `app/src/main/res/layout/sheet_resultado_escaneo.xml` - Producto + selector gramos + resumen
57. `app/src/main/res/layout/sheet_diario_voz.xml` - Micrófono animado + transcripción + lista
58. `app/src/main/res/layout/item_parsed_food.xml` - Card alimento + barras macro P/C/G
59. `app/src/main/res/layout/item_macro_mini.xml` - Macro compacto para sheets
60. `app/src/main/res/drawable/bg_focus_overlay.xml` - Overlay recuadro enfoque
61. `app/src/main/res/drawable/bg_mic_ring.xml` - Anillo micrófono animado
62. `app/src/main/res/drawable/bg_input_outline.xml` - Input outline style
63. `app/src/main/res/drawable/ic_flash_off.xml`, `ic_plus.xml`, `ic_minus.xml`, `ic_mic.xml`, `ic_edit.xml`, `ic_food_generic.xml`
64. `app/src/main/res/values/ids.xml` - Animation tag IDs

### Paso 4.5 - Costos + Presupuesto (App Module)
65. `app/src/main/java/com/lupefit/app/data/local/entity/FoodProduct.java` - Extendido: referencePrice, referenceQuantityGrams, priceUpdatedAt
66. `app/src/main/java/com/lupefit/app/data/local/entity/FoodLog.java` - Extendido: estimatedCostSnapshot
67. `app/src/main/java/com/lupefit/app/data/local/preferences/UserPreferencesManager.java` - Feature toggle + budget + currency
68. `app/src/main/java/com/lupefit/app/domain/engine/budget/CostBudgetEngine.java` - Motor completo de costos
69. `app/src/main/res/layout/view_optional_price_input.xml` - Bloque colapsable precio (header, inputs, chips, badge stale)
70. `app/src/main/res/layout/card_weekly_budget_summary.xml` - Card presupuesto semanal (progress, status, missing price)
71. `app/src/main/java/com/lupefit/app/ui/escaneo/ResultadoEscaneoSheet.java` - Integración price block + feature toggle
72. `app/src/main/java/com/lupefit/app/AppContainer.java` - UserPreferencesManager wireado
73. `app/src/main/res/values/strings.xml` - costo_*, presupuesto_*, config_* agregados

### Paso 5 - Backend FastAPI + Docker (Backend Module)
74. `backend/app/main.py` - FastAPI app con lifespan, CORS, health check
75. `backend/app/core/config.py` - Settings (pydantic-settings, env vars)
76. `backend/app/core/database.py` - Async SQLAlchemy engine, session, init_db
77. `backend/app/core/security.py` - JWT (RS256), bcrypt, token creation/validation
78. `backend/app/models/*.py` - 17 SQLAlchemy models con UUID, índices, relaciones, timestamps, offline-first fields
79. `backend/app/schemas/*.py` - 15 Pydantic schemas (Request/Response)
80. `backend/app/api/v1/endpoints/*.py` - 16 routers (auth, users, food_logs, workout_logs, exercises, foods, recipes, water_logs, progress_photos, personal_records, glucose_logs, mood_entries, user_notes, weather_snapshots, streaks, sync)
81. `backend/requirements.txt` - fastapi, uvicorn, sqlalchemy, asyncpg, alembic, redis, celery, python-jose, passlib, bcrypt, pydantic, httpx, email-validator, prometheus-client
82. `backend/Dockerfile` - Python 3.12-slim, non-root user, build dependencies
83. `backend/docker-compose.yml` - 5 servicios (postgres:16, redis:7, backend, celery-worker, celery-beat) con healthchecks
84. `backend/alembic.ini` + `backend/alembic/env.py` - Migraciones automáticas

### Paso 6 - Feature Modules (11 Módulos UI)
85. `settings.gradle.kts` - 11 feature modules agregados
86. `feature/feature-diario/` - DiarioHomeFragment, DiarioVozSheet, DiarioEntryAdapter, nav_diario.xml, menu_diario_home.xml, layouts, strings
87. `feature/feature-ejercicio/` - EjercicioHomeFragment, nav_ejercicio.xml, layouts, strings
88. `feature/feature-nutricion/` - NutricionHomeFragment, nav_nutricion.xml, layouts, strings
89. `feature/feature-progreso/` - ProgresoHomeFragment, nav_progreso.xml, layouts, strings
90. `feature/feature-recetas/` - RecetasHomeFragment, nav_recetas.xml, layouts, strings
91. `feature/feature-escaneo/` - EscanearHomeFragment, nav_escaneo.xml, layouts, strings
92. `feature/feature-agua/` - AguaHomeFragment, nav_agua.xml, layouts, strings
93. `feature/feature-configuracion/` - ConfiguracionHomeFragment, nav_configuracion.xml, layouts, strings
93. `feature/feature-perfil/` - PerfilHomeFragment, nav_perfil.xml, layouts, strings
94. `feature/feature-insights/` - InsightsHomeFragment, nav_insights.xml, layouts, strings
95. `feature/feature-planes/` - PlanesHomeFragment, nav_planes.xml, layouts, strings
96. `feature/*/build.gradle.kts` - 11 build files con ViewBinding, DataBinding, safe-args, dependencies
97. `feature/*/src/main/res/navigation/nav_*.xml` - 11 navigation graphs XML
98. `feature/*/src/main/java/.../ui/*HomeFragment.java` - 11 Home Fragments base
99. `feature/*/src/main/java/.../viewmodel/*HomeViewModel.java` - 11 ViewModels base
100. `feature/*/src/main/res/layout/fragment_*_home.xml` - 11 layouts base con MaterialToolbar
101. `feature/*/src/main/res/values/strings.xml` - 11 strings files localizados
101. `app/src/main/java/com/lupefit/app/ui/main/MainActivity.java` - BottomNavigationView + MasBottomSheet
102. `app/src/main/res/layout/activity_main.xml` - BottomNavigationView + NavHostFragment
103. `app/src/main/res/layout/sheet_mas.xml` - BottomSheet para "Más" opciones
104. `app/src/main/res/menu/menu_bottom_navigation.xml` - 5 tabs bottom navigation
105. `app/src/main/res/navigation/nav_main.xml` - Main nav graph con 5 NavHostFragments
106. `app/src/main/res/navigation/nav_mas.xml` - Nav graph para "Más" (7 features)
107. `app/src/main/res/drawable/ic_*.xml` - 13 icons vectoriales
108. `core/core-ui/src/main/res/layout/item_mas_option.xml` - Item para MasBottomSheet RecyclerView
109. `core/core-ui/src/main/res/values-night/themes.xml` - Dark theme override con colorBackground

---

## Resumen Técnico LuPeFit

### Arquitectura
- **Pattern**: Clean Architecture por feature module
- **Módulos Gradle**: app + core:core-domain + core:core-ui + core:core-network + core:core-sync + **11 feature modules = 16 módulos totales**
- **Offline-first**: Room + WorkManager sync bidireccional
- **Stack**: Java 17+, XML Views, ViewBinding, Room (annotationProcessor), Material 3

### Base de Datos (15 entidades Android + 17 entidades Backend)
- User, FoodLog, WorkoutLog, WorkoutSet, Exercise, Food, Recipe, RecipeIngredient
- WaterLog, ProgressPhoto, PersonalRecordEntity, GlucoseLog, MoodEntry, UserNote, WeatherSnapshot, Streak, **FoodProduct**
- **Todas con offline-first**: `isSynced` (boolean), `syncAction` (CREATE/UPDATE/DELETE), `serverId` (String), `timestamp` (long), `lastModified` (long)
- **FoodProduct**: catálogo offline-first (sin sync fields, solo lectura local) + **costos opcionales**

### Backend (FastAPI + PostgreSQL) - **COMPLETADO**
- 16 endpoints REST organizados por feature
- Sync batch endpoint versionado con resolución conflictos
- JWT Auth (access 30min, refresh 7d, RS256)
- Docker: postgres:16, redis:7, backend (uvicorn 4 workers), celery-worker, celery-beat
- Health checks, CORS, logging estructurado

### Native Libraries Reemplazo
| Original | LuPeFit |
|----------|---------|
| libbarhopper_v2.so | **ML Kit Barcode Scanning** |
| libshinobicharts-android.so | **MPAndroidChart** |
| libtensorflowlite_jni.so | **TF Lite Official** |
| libpassiolib.so | Passio SDK (mantener) o custom TFLite |

### Features Incluidas (Todas Gratis)
Diario, Ejercicio, Nutrición (Net Carbs, Metas por comida), Progreso (Export CSV/JSON), Recetas (Colecciones, Importar web), **Escaneo (Código barras + IA Comida)**, Agua, Configuración (Tema, Unidades, Sync), Perfil (Auth local), Insights (IA, Tendencias), Planes (Comida + Entrenamiento), **Voz (Hands-Free Logging)**, **Costos + Presupuesto (Opcional)**

### Features Excluidas
Blog, Community, Friends, Payments, Fasting, Premium/Upsell, Zendesk, Braze, Facebook/Google Login, Samsung Health, Ads/Analytics terceros

---

## Próximos Pasos: Implementación Continua

### PASO 7: Feature Modules Logic Completa
- `feature-diario` → BuscarAlimento, QuickAdd, EscanearCodigoBarras, VoiceLogging, CostTracking
- `feature-ejercicio` → Lista, Buscar, Nuevo, Rutinas, WOD CrossFit, Timer
- `feature-nutricion` → Resumen, Metas, Macros, Gráficos (MPAndroidChart)
- `feature-progreso` → Peso, Medidas, Fotos, Export CSV/JSON, Gráficos
- `feature-recetas` → Lista, Crear, Importar, Detalle, Colecciones
- `feature-escaneo` → ML Kit Barcode + Passio Food Scan
- `feature-agua` → Tracking, Meta, Recordatorios
- `feature-configuracion` → Cuenta, Perfil, Metas, Notificaciones, Apariencia, Sync, Exportar, **Cost Tracking Toggle**, **Presupuesto Semanal**
- `feature-perfil` → Perfil, Editar, Login, Registro, Password, Sync Account
- `feature-insights` → IA Nutricional, Tendencias, Predicciones, Weekly Digest, Patrones
- `feature-planes` → Planes comida semanales, Planes entrenamiento, Mis Planes, Quick Log Recipe, **Presupuesto**

### PASO 8: Advanced Features (v2.0-v4.0)
- Health Connect Integration (Steps, Active Calories, Exercise Sessions, Heart Rate)
- Voice Logging (SpeechRecognizer + Whisper Strategy) - **Base implementada en Paso 4**
- CrossFit WOD Module (AMRAP, EMOM, For Time, Tabata) + Timer
- Dynamic Macros (Ciclado por gasto energético)
- Streak Manager (Rachas diarias)
- Mood Tracking + User Notes

---

## Comandos de Referencia

```bash
# Build proyecto completo (Android)
cd /Users/marcelo/Desktop/Desarrollos/006
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home
./gradlew :app:assembleDebug

# Build y deploy backend
cd /Users/marcelo/Desktop/Desarrollos/006/backend
docker-compose build
docker-compose up -d

# Ver logs backend
docker-compose logs -f backend

# Test endpoints
curl http://localhost:8000/health
curl -X POST http://localhost:8000/api/v1/auth/register -H "Content-Type: application/json" -d '{"email": "test@lupefit.app", "password": "testpass123"}'

# Ver estructura app referencia analizada
find /Users/marcelo/Desktop/Desarrollos/006/apktool_out -type f -name "*.xml" | grep -E "(navigation|manifest)"

# Buscar clases clave app referencia
grep -r "class.*Activity" /Users/marcelo/Desktop/Desarrollos/006/jadx_out/sources/com/myfitnesspal/feature/ | head -30

# Ver experimentos pricing app referencia
jq '.[] | select(.name | contains("price"))' /Users/marcelo/Desktop/Desarrollos/006/apktool_out/assets/config/abtests.json
```

---

## Notas para Retomar

**Archivos clave:**
1. `features.md` - Arquitectura LuPeFit completa (v3)
2. `avances.md` - Este historial
3. `core/core-ui/` - Tema Material 3 completo listo para usar
4. `core/core-network/` - API Service, DTOs, SyncEngine, NetworkModule
5. `core/core-sync/` - SyncWorker, SyncQueue, ConflictResolver, SyncScheduler
6. `app/` - Esqueleto base compilable + Escáner + Catálogo + Voz + Costos (Paso 4.5 completado)
7. `backend/` - FastAPI + Docker Compose completo y funcionando (Paso 5 completado)
8. `feature/*/` - 11 Feature Modules con Navigation Graphs, Fragments, ViewModels (Paso 6 completado)
9. `apktool_out/` / `jadx_out/` - Referencia app referencia (regenerables)

**Estado**: **Paso 6 (Feature Modules 11 módulos UI) 100% estructura completada**. Próximo: **Paso 7 - Feature Modules Logic Completa**.

---

*Última actualización: 2026-11-21 - Paso 6 completado, 11 Feature Modules con Navigation Graphs, Fragments, ViewModels, Layouts base, Strings localizados, Main Activity con BottomNavigationView + MasBottomSheet, 13 icons vectoriales, listo para Paso 7*
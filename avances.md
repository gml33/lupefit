# Avances - LuPeFit: Replicación y Rediseño Completo

## Estado Actual: **PASO 4 - ESCÁNER + OPEN FOOD FACTS + CATÁLOGO + VOZ COMPLETADO** ✅

### Fecha de Inicio: 2026-11-20
### Fase Actual: Implementación - Paso 4 Completado (Scanner + Catalog + Voice)

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

#### Hito 15: Backend FastAPI + Docker - PENDIENTE
- Estructura completa: models, schemas, api/v1, services, tasks
- SQLAlchemy models con UUID, índices, full-text search
- JWT Auth (access/refresh tokens, bcrypt)
- docker-compose.yml: postgres, redis, backend, celery-worker, celery-beat
- Sync batch endpoint con versionado global

#### Hito 16: Feature Modules UI & Navigation - PENDIENTE
- 11 feature modules (diario, ejercicio, nutricion, progreso, recetas, escaneo, agua, configuracion, perfil, insights, planes)
- Navigation graphs por feature (XML)
- Bottom Nav: Inicio | Tendencias | [FAB] | Planes | Perfil
- Sin premium/upsell modules

#### Hito 17: UI - Tema Material 3 Completo (core:core-ui) ✅
**Paleta LuPeFit Oficial (Tokens `md_theme_*`):**
| Color | Light | Dark | Uso |
|-------|-------|------|-----|
| Primary | `#006C4C` | `#6CDBAC` | Verde petróleo - Acciones principales |
| Primary Container | `#89F8C7` | `#005138` | Contenedores primarios |
| Secondary | `#4D6357` | `#B3CCBE` | Secundarios |
| Secondary Container | `#CFE9D9` | `#354B40` | Contenedores secundarios |
| Background | `#F5FBF6` | `#0F1512` | Fondo app |
| Surface | `#FFFFFF` | `#171D1A` | Tarjetas |
| On Surface | `#171D1A` | `#DFE4DF` | Texto principal |
| On Surface Variant | `#404943` | `#BFC9C2` | Texto secundario |
| Outline | `#707973` | `#8A938C` | Bordes |
| Error | `#BA1A1A` | `#FFB4AB` | Solo errores |

**Tokens Semánticos (Macros, Hidratación, Estados):**
- Protein: `#2563EB` / `#60A5FA` (track: `#DBEAFE` / `#1E3A8A`)
- Carbs: `#D97706` / `#FBBF24` (track: `#FEF3C7` / `#78350F`)
- Fat: `#7C3AED` / `#A78BFA` (track: `#EDE9FE` / `#4C1D95`)
- Water: `#0284C7` / `#38BDF8`
- Success: `#16A34A` / `#4ADE80`
- Warning: `#F59E0B` / `#FBBF24`

**Tokens Funcionalidades Extra (v2.0-v4.0):**
- CrossFit: `#EA580C` / `#FB923C`
- Streak: `#F59E0B` / `#FBBF24`
- Mood Positive: `#10B981` / `#34D399`
- Mood Neutral: `#6366F1` / `#818CF8`
- Mood Low: `#F43F5E` / `#FB7185`

- **Tipografía**: Material 3 Type Scale (15 estilos: Display/Headline/Title/Body/Label)
- **Formas**: ExtraSmall 4dp, Small 8dp, Medium 12dp, Large 16dp, Card 24dp, ExtraLarge 28dp, Pill 50%, ProgressTrack 8dp
- **Espaciado**: Escala 4dp (2-48dp)
- **Componentes**: 20+ widget styles (Button Filled/Tonal/Outlined, FAB, TextInput, Chip Assist/Filter/Choice, BottomNavigation, BottomSheet, AlertDialog, CardView Elevated/Outlined, Circular/LinearProgress, Switch, CheckBox, RadioButton, Slider, Tooltip, Snackbar)
- **Modo oscuro**: Completo (values-night/colors.xml + themes.xml con DayNight auto-switch)
- **Elevation**: 0-1dp + stroke 1dp dark mode (tokens `md_theme_*`)

#### Hito 18: 4 Componentes Base Obligatorios ✅
1. `view_header_date.xml` - Selector fecha + avatar (Material 3)
2. `card_macro_summary.xml` - CircularProgress 150dp/14dp + 3 LinearProgress 10dp (macro rings)
3. `item_log_entry.xml` - Icono circular + título 15sp Bold + subtítulo 13sp + valor derecha
4. `sheet_base_container.xml` - Píldora arrastre + título 18sp Bold + botón ancho completo

#### Hito 19: Drawables Base ✅
- `ic_chevron_left.xml` / `ic_chevron_right.xml`
- `ic_avatar_placeholder.xml` / `ic_food_placeholder.xml`
- `bg_rounded_xs.xml` (4dp)
- `nav_active_indicator.xml`
- `splash_background.xml`

#### Hito 20: Strings - Castellano Argentino ✅
- Archivo: `core/core-ui/src/main/res/values-es-rAR/strings.xml`
- 365 strings localizadas (App, Auth, Diario, Ejercicio, Nutrición, Progreso, Recetas, Escaneo, Agua, Config, Perfil, Insights, Planes, Errores, Unidades, Macros, Accesibilidad)
- Formato fecha: dd/MM/yyyy, hora: HH:mm
- Términos: "Registrate", "Entrar", "Probá", "Tocá", "Apuntá", "Almuerzo", "Merienda"

#### Hito 21: Documentación Actualizada (features.md v3) ✅
- Arquitectura LuPeFit completa
- Module map actualizado (app + 2 core + 11 feature = 14 módulos)
- Paleta de colores documentada (tokens `md_theme_*`)
- Schema Room documentado (14 entidades + offline-first fields)
- Build gradle configurado

#### Hito 22: **PASO 1 - Esqueleto Base Compilable** ✅
- **build.gradle (app)**: Material 3, Room (annotationProcessor), Health Connect, WorkManager, Play Services Location, MPAndroidChart
- **MyApplication.java** + **AppContainer.java**: Inyección de Dependencias Manual (sin Hilt/Dagger/KAPT)
- **AppDatabase.java** + **14 Entidades Room** con campos offline-first (isSynced, syncAction, serverId, timestamp, lastModified)
- **14 DAOs** con queries LiveData
- **14 Repositories** (stubs) + **5 Engines** (stubs) + **7 UseCases** (stubs)
- **core:core-ui** module: Tema completo, 4 componentes base, drawables, strings AR
- **attrs.xml**: Atributos Material 3 inversos (colorInverseSurface, colorInverseOnSurface, etc.)
- **APK Debug generado exitosamente** (`./gradlew :app:assembleDebug` → BUILD SUCCESSFUL)

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
27. `core/core-ui/src/main/res/drawable/bg_rounded_xs.xml`
28. `core/core-ui/build.gradle.kts` + `proguard-rules.pro` + `consumer-rules.pro`

### Paso 1 - Esqueleto Base (App Module)
29. `app/build.gradle` - Dependencias completas (Material 3, Room, Health Connect, WorkManager, Play Services Location, MPAndroidChart, CameraX, ML Kit, Lottie, CircleImageView)
30. `app/src/main/java/com/lupefit/app/MyApplication.java` - Application class
31. `app/src/main/java/com/lupefit/app/AppContainer.java` - Manual DI Container (218+ líneas)
32. `app/src/main/java/com/lupefit/app/data/local/AppDatabase.java` - Room Database singleton
33. `app/src/main/java/com/lupefit/app/data/local/entity/*.java` - 15 Entidades (offline-first)
34. `app/src/main/java/com/lupefit/app/data/local/dao/*.java` - 15 DAOs (LiveData queries)
35. `app/src/main/java/com/lupefit/app/data/local/converters/TypeConverters.java` - TypeConverters
36. `app/src/main/java/com/lupefit/app/domain/repository/*.java` - 14 Repositories (stubs)
37. `app/src/main/java/com/lupefit/app/domain/engine/*.java` - 5 Engines (stubs)
38. `app/src/main/java/com/lupefit/app/domain/usecase/*.java` - 7 UseCases (stubs)
39. `app/src/main/res/values/attrs.xml` - Atributos Material 3 custom
40. `app/src/main/res/xml/data_extraction_rules.xml` + `backup_rules.xml`
41. `app/src/main/res/values/strings.xml` - app_name + escaneo/voz strings
42. `app/src/main/res/mipmap-*/ic_launcher*.xml` - Iconos vectoriales
43. `app/src/main/AndroidManifest.xml` - Permisos, Activities, Providers, Health Connect meta-data

### Paso 4 - Escáner + Catálogo + Voz (App Module)
44. `app/src/main/assets/fresh_foods.json` - 30 alimentos frescos argentinos precargados
45. `app/src/main/java/com/lupefit/app/data/local/entity/FoodProduct.java` - Entity Room + Parcelable
46. `app/src/main/java/com/lupefit/app/data/local/dao/FoodProductDao.java` - DAO con búsqueda relevancia
47. `app/src/main/java/com/lupefit/app/data/local/repository/FoodCatalogRepository.java` - Offline-First OpenFoodFacts
48. `app/src/main/java/com/lupefit/app/ui/escaneo/BarcodeAnalyzer.java` - ML Kit analyzer anti-duplicados
49. `app/src/main/java/com/lupefit/app/ui/escaneo/EscaneoCodigoBarrasFragment.java` - CameraX preview + permisos
50. `app/src/main/java/com/lupefit/app/ui/escaneo/ResultadoEscaneoSheet.java` - BottomSheet selector gramos
51. `app/src/main/java/com/lupefit/app/domain/engine/voice/VoiceTranscriptionEngine.java` - Strategy interface
52. `app/src/main/java/com/lupefit/app/domain/engine/voice/NativeAndroidVoiceEngine.java` - SpeechRecognizer es-AR
53. `app/src/main/java/com/lupefit/app/domain/engine/voice/WhisperServerVoiceEngine.java` - MediaRecorder + OkHttp
54. `app/src/main/java/com/lupefit/app/domain/engine/voice/VoiceFoodParser.java` - NLP español + macros
55. `app/src/main/java/com/lupefit/app/ui/diario/DiarioVozSheet.java` - BottomSheet micrófono animado + lista editable
56. `app/src/main/res/layout/fragment_escaneo_codigo_barras.xml` - Preview + overlay recuadro
57. `app/src/main/res/layout/sheet_resultado_escaneo.xml` - Producto + selector gramos + resumen
58. `app/src/main/res/layout/sheet_diario_voz.xml` - Micrófono animado + transcripción + lista
59. `app/src/main/res/layout/item_parsed_food.xml` - Card alimento + barras macro P/C/G
60. `app/src/main/res/layout/item_macro_mini.xml` - Macro compacto para sheets
61. `app/src/main/res/drawable/bg_focus_overlay.xml` - Overlay recuadro enfoque
61. `app/src/main/res/drawable/bg_mic_ring.xml` - Anillo micrófono animado
62. `app/src/main/res/drawable/bg_input_outline.xml` - Input outline style
63. `app/src/main/res/drawable/ic_flash_off.xml`, `ic_plus.xml`, `ic_minus.xml`, `ic_mic.xml`, `ic_edit.xml`, `ic_food_generic.xml`
64. `app/src/main/res/values/ids.xml` - Animation tag IDs

---

## Resumen Técnico LuPeFit

### Arquitectura
- **Pattern**: Clean Architecture por feature module
- **Módulos Gradle**: app + core:core-domain + core:core-ui + core:core-network + core:core-sync = **5 módulos actuales** (simplificado de 14 planeados)
- **Offline-first**: Room + WorkManager sync bidireccional
- **Stack**: Java 17+, XML Views, ViewBinding, Room (annotationProcessor), Material 3

### Base de Datos (15 entidades)
- User, FoodLog, WorkoutLog, WorkoutSet, Exercise, Food, Recipe, RecipeIngredient
- WaterLog, ProgressPhoto, PersonalRecordEntity, GlucoseLog, MoodEntry, UserNote, WeatherSnapshot, Streak, **FoodProduct**
- **Todas con offline-first**: `isSynced` (boolean), `syncAction` (CREATE/UPDATE/DELETE), `serverId` (String), `timestamp` (long), `lastModified` (long)
- **FoodProduct**: catálogo offline-first (sin sync fields, solo lectura local)

### Backend (FastAPI + PostgreSQL) - PENDIENTE
- 30+ endpoints REST organizados por feature
- Sync batch endpoint versionado
- JWT Auth (access 30min, refresh 7d)
- Docker: postgres, redis, backend, celery worker/beat

### Native Libraries Reemplazo
| Original | LuPeFit |
|----------|---------|
| libbarhopper_v2.so | **ML Kit Barcode Scanning** |
| libshinobicharts-android.so | **MPAndroidChart** |
| libtensorflowlite_jni.so | **TF Lite Official** |
| libpassiolib.so | Passio SDK (mantener) o custom TFLite |

### Features Incluidas (Todas Gratis)
Diario, Ejercicio, Nutrición (Net Carbs, Metas por comida), Progreso (Export CSV/JSON), Recetas (Colecciones, Importar web), **Escaneo (Código barras + IA Comida)**, Agua, Configuración (Tema, Unidades, Sync), Perfil (Auth local), Insights (IA, Tendencias), Planes (Comida + Entrenamiento), **Voz (Hands-Free Logging)**

### Features Excluidas
Blog, Community, Friends, Payments, Fasting, Premium/Upsell, Zendesk, Braze, Facebook/Google Login, Samsung Health, Ads/Analytics terceros

---

## Próximos Pasos: Implementación Continua

### PASO 5: Backend FastAPI + Docker Compose
1. Backend FastAPI + Docker Compose (PostgreSQL, Redis, Celery)
2. SQLAlchemy models + Pydantic schemas
3. JWT Auth endpoints
4. Sync batch endpoint `/api/v1/sync/batch`
5. docker-compose.yml con postgres, redis, backend, celery-worker, celery-beat

### PASO 6: Feature Modules (11 módulos)
- `feature-diario` → DiarioFragment, BuscarAlimento, QuickAdd, EscanearCodigoBarras
- `feature-ejercicio` → Lista, Buscar, Nuevo, Rutinas, WOD CrossFit, Timer
- `feature-nutricion` → Resumen, Metas, Macros, Gráficos (MPAndroidChart)
- `feature-progreso` → Peso, Medidas, Fotos, Exportar, Gráficos
- `feature-recetas` → Lista, Crear, Importar, Detalle, Colecciones
- `feature-escaneo` → ML Kit Barcode + Passio Food Scan
- `feature-agua` → Tracking, Meta, Recordatorios
- `feature-configuracion` → Cuenta, Perfil, Metas, Notificaciones, Apariencia, Sync, Exportar
- `feature-perfil` → Perfil, Editar, Login, Registro, Password, Sync Account
- `feature-insights` → Semanal, Tendencias, Predicciones, Patrones
- `feature-planes` → Comida, Entrenamiento, Mis Planes, Quick Log

### PASO 7: Advanced Features (v2.0-v4.0)
- Health Connect Integration (Steps, Active Calories, Exercise Sessions, Heart Rate)
- Voice Logging (SpeechRecognizer + Whisper Strategy) - **Base implementada en Paso 4**
- CrossFit WOD Module (AMRAP, EMOM, For Time, Tabata) + Timer
- Dynamic Macros (Ciclado por gasto energético)
- Streak Manager (Rachas diarias)
- Mood Tracking + User Notes

---

## Comandos de Referencia

```bash
# Build proyecto completo
cd /Users/marcelo/Desktop/Desarrollos/006
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home
./gradlew :app:assembleDebug

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
6. `app/` - Esqueleto base compilable + Escáner + Catálogo + Voz (Paso 4 completado)
7. `apktool_out/` / `jadx_out/` - Referencia app referencia (regenerables)

**Estado**: **Paso 4 (Escáner + Catálogo + Voz) 100% completo y compilando**. Próximo: **Paso 5 - Backend FastAPI + Docker**.

---

*Última actualización: 2026-11-20 - Paso 4 completado, BUILD SUCCESSFUL, listo para Paso 5*
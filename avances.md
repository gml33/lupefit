# Avances - LuPeFit: Replicación y Rediseño Completo

## Estado Actual: **PASO 1 - ESQUELETO BASE COMPILABLE COMPLETADO** ✅

### Fecha de Inicio: 2026-11-20
### Fase Actual: Implementación - Paso 1 Completado

---

## Hitos Completados

### ✅ Fase 1: Ingeniería Reversa MyFitnessPal (COMPLETA)

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

#### Hito 13: Capa de Red & Sincronización (core-network, core-sync) - PENDIENTE
- Retrofit API Service con 30+ endpoints
- Sync Batch endpoint bidireccional (/sync/batch)
- WorkManager SyncWorker (15 min + connectivity trigger)
- Estrategia de conflictos: Server-wins / Merge / Latest-wins por entidad

#### Hito 14: Backend FastAPI + Docker - PENDIENTE
- Estructura completa: models, schemas, api/v1, services, tasks
- SQLAlchemy models con UUID, índices, full-text search
- JWT Auth (access/refresh tokens, bcrypt)
- docker-compose.yml: postgres, redis, backend, celery-worker, celery-beat
- Sync batch endpoint con versionado global

#### Hito 15: Feature Modules UI & Navigation - PENDIENTE
- 11 feature modules (diario, ejercicio, nutricion, progreso, recetas, escaneo, agua, configuracion, perfil, insights, planes)
- Navigation graphs por feature (XML)
- Bottom Nav: Inicio | Tendencias | [FAB] | Planes | Perfil
- Sin premium/upsell modules

#### Hito 16: UI - Tema Material 3 Completo (core:core-ui) ✅
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

#### Hito 17: 4 Componentes Base Obligatorios ✅
1. `view_header_date.xml` - Selector fecha + avatar (Material 3)
2. `card_macro_summary.xml` - CircularProgress 150dp/14dp + 3 LinearProgress 10dp (macro rings)
3. `item_log_entry.xml` - Icono circular + título 15sp Bold + subtítulo 13sp + valor derecha
4. `sheet_base_container.xml` - Píldora arrastre + título 18sp Bold + botón ancho completo

#### Hito 18: Drawables Base ✅
- `ic_chevron_left.xml` / `ic_chevron_right.xml`
- `ic_avatar_placeholder.xml` / `ic_food_placeholder.xml`
- `bg_rounded_xs.xml` (4dp)
- `nav_active_indicator.xml`
- `splash_background.xml`

#### Hito 19: Strings - Castellano Argentino ✅
- Archivo: `core/core-ui/src/main/res/values-es-rAR/strings.xml`
- 365 strings localizadas (App, Auth, Diario, Ejercicio, Nutrición, Progreso, Recetas, Escaneo, Agua, Config, Perfil, Insights, Planes, Errores, Unidades, Macros, Accesibilidad)
- Formato fecha: dd/MM/yyyy, hora: HH:mm
- Términos: "Registrate", "Entrar", "Probá", "Tocá", "Apuntá", "Almuerzo", "Merienda"

#### Hito 20: Documentación Actualizada (features.md v3) ✅
- Arquitectura LuPeFit completa
- Module map actualizado (app + 2 core + 11 feature = 14 módulos)
- Paleta de colores documentada (tokens `md_theme_*`)
- Schema Room documentado (14 entidades + offline-first fields)
- Build gradle configurado

#### Hito 21: **PASO 1 - Esqueleto Base Compilable** ✅
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

### Análisis MFP (Fase 1)
1. `features.md` - v1: Análisis MFP completo
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
31. `app/src/main/java/com/lupefit/app/AppContainer.java` - Manual DI Container (218 líneas)
32. `app/src/main/java/com/lupefit/app/data/local/AppDatabase.java` - Room Database singleton
33. `app/src/main/java/com/lupefit/app/data/local/entity/*.java` - 14 Entidades (offline-first)
34. `app/src/main/java/com/lupefit/app/data/local/dao/*.java` - 14 DAOs (LiveData queries)
35. `app/src/main/java/com/lupefit/app/data/local/converters/TypeConverters.java` - TypeConverters
36. `app/src/main/java/com/lupefit/app/domain/repository/*.java` - 14 Repositories (stubs)
37. `app/src/main/java/com/lupefit/app/domain/engine/*.java` - 5 Engines (stubs)
38. `app/src/main/java/com/lupefit/app/domain/usecase/*.java` - 7 UseCases (stubs)
39. `app/src/main/res/values/attrs.xml` - Atributos Material 3 custom
40. `app/src/main/res/xml/data_extraction_rules.xml` + `backup_rules.xml`
41. `app/src/main/res/values/strings.xml` - app_name
42. `app/src/main/res/mipmap-*/ic_launcher*.xml` - Iconos vectoriales
43. `app/src/main/AndroidManifest.xml` - Permisos, Activities, Providers, Health Connect meta-data

---

## Resumen Técnico LuPeFit

### Arquitectura
- **Pattern**: Clean Architecture por feature module
- **Módulos Gradle**: app + core:core-domain + core:core-ui = **3 módulos actuales** (simplificado de 14 planeados para Paso 1)
- **Offline-first**: Room + WorkManager sync bidireccional
- **Stack**: Java 17+, XML Views, ViewBinding, Room (annotationProcessor), Material 3

### Base de Datos (14 entidades + Streak = 15)
- User, FoodLog, WorkoutLog, WorkoutSet, Exercise, Food, Recipe, RecipeIngredient
- WaterLog, ProgressPhoto, PersonalRecordEntity, GlucoseLog, MoodEntry, UserNote, WeatherSnapshot, Streak
- **Todas con offline-first**: `isSynced` (boolean), `syncAction` (CREATE/UPDATE/DELETE), `serverId` (String), `timestamp` (long), `lastModified` (long)

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
Diario, Ejercicio, Nutrición (Net Carbs, Metas por comida), Progreso (Export CSV/JSON), Recetas (Colecciones, Importar web), Escaneo (Código barras + IA Comida), Agua, Configuración (Tema, Unidades, Sync), Perfil (Auth local), Insights (IA, Tendencias), Planes (Comida + Entrenamiento)

### Features Excluidas
Blog, Community, Friends, Payments, Fasting, Premium/Upsell, Zendesk, Braze, Facebook/Google Login, Samsung Health, Ads/Analytics terceros

---

## Próximos Pasos: Implementación Continua

### PASO 2: Core Network & Sync Modules
1. `core:core-network` - Retrofit, API Service, DTOs, SyncEngine
2. `core:core-sync` - WorkManager SyncWorker, ConflictResolution
3. Backend FastAPI + Docker Compose

### PASO 3: Feature Modules (11 módulos)
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

### PASO 4: Advanced Features (v2.0-v4.0)
- Health Connect Integration (Steps, Active Calories, Exercise Sessions, Heart Rate)
- Voice Logging (SpeechRecognizer + Whisper Strategy)
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

# Ver estructura MFP analizada
find /Users/marcelo/Desktop/Desarrollos/006/apktool_out -type f -name "*.xml" | grep -E "(navigation|manifest)"

# Buscar clases clave MFP
grep -r "class.*Activity" /Users/marcelo/Desktop/Desarrollos/006/jadx_out/sources/com/myfitnesspal/feature/ | head -30

# Ver experimentos pricing MFP
jq '.[] | select(.name | contains("price"))' /Users/marcelo/Desktop/Desarrollos/006/apktool_out/assets/config/abtests.json
```

---

## Notas para Retomar

**Archivos clave:**
1. `features.md` - Arquitectura LuPeFit completa (v3)
2. `avances.md` - Este historial
3. `core/core-ui/` - Tema Material 3 completo listo para usar
4. `app/` - Esqueleto base compilable (Paso 1 completado)
5. `apktool_out/` / `jadx_out/` - Referencia MFP (regenerables)

**Estado**: **Paso 1 (Esqueleto Base) 100% completo y compilando**. Próximo: **Paso 2 - Core Network & Sync + Backend**.

---

*Última actualización: 2026-11-20 - Paso 1 completado, BUILD SUCCESSFUL, listo para Paso 2*
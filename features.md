# Features Document - LuPeFit Android App

## Project Overview
- **App Name**: LuPeFit
- **Package**: com.lupefit.app
- **Min SDK**: 28 (Android 9.0)
- **Target SDK**: 36 (Android 14)
- **Language**: Java + XML Views (traditional)
- **Architecture**: Feature-modular (Enfoque A), Clean Architecture por feature
- **Offline-first**: Room + WorkManager background sync
- **Backend**: Python + FastAPI + PostgreSQL (Docker)

## Paleta de Colores (Material 3) - UI_DESIGN_SYSTEM (Fuente de Verdad)

| Token | Light | Dark | Uso |
|-------|-------|------|-----|
| **md_theme_primary** | `#006C4C` | `#6CDBAC` | Verde petróleo - Acciones principales, FAB, botones primarios |
| **md_theme_primaryContainer** | `#89F8C7` | `#005138` | Contenedores primarios, chips seleccionados |
| **md_theme_secondary** | `#4D6357` | `#B3CCBE` | Verde musgo - Secundario, acentos |
| **md_theme_secondaryContainer** | `#CFE9D9` | `#354B40` | Contenedores secundarios |
| **md_theme_tertiary** | `#2563EB` | `#60A5FA` | Azul - Proteínas |
| **macro_protein** | `#2563EB` | `#60A5FA` | Proteínas |
| **macro_carbs** | `#D97706` | `#FBBF24` | Carbohidratos (Ámbar) |
| **macro_fat** | `#7C3AED` | `#A78BFA` | Grasas (Violeta) |
| **water_primary** | `#0284C7` | `#38BDF8` | Hidratación (Celeste) |
| **status_success** | `#16A34A` | `#4ADE80` | Éxito |
| **status_error** | `#BA1A1A` | `#FFB4AB` | Solo errores/alertas |
| **md_theme_background** | `#F5FBF6` | `#0F1512` | Fondo principal |
| **md_theme_surface** | `#FFFFFF` | `#171D1A` | Tarjetas |
| **md_theme_surfaceVariant** | `#DBE5DD` | `#404943` | Variante de superficie |
| **md_theme_surfaceContainer** | `#E8EFE8` | `#1C241F` | Contenedor superficie |
| **md_theme_surfaceContainerHigh** | `#DCE4DC` | `#242D27` | Contenedor superficie alto |
| **md_theme_surfaceContainerHighest** | `#D0D8D1` | `#2D372F` | Contenedor superficie máximo |
| **md_theme_onSurface** | `#171D1A` | `#DFE4DF` | Texto principal |
| **md_theme_onSurfaceVariant** | `#404943` | `#BFC9C2` | Texto secundario |
| **md_theme_outline** | `#707973` | `#8A938C` | Divisores, bordes |

**Regla de Oro**: Nunca usar colores hex hardcodeados en layouts ni Java. Usar siempre `@color/md_theme_*` o `?attr/colorSurface`, etc.

**Tipografía**: Material 3 Type Scale (Display > Headline > Title > Body > Label)
**Formas (UI_DESIGN_SYSTEM)**: 4dp (xs), 8dp (sm), 12dp (md - items RecyclerView), 16dp (lg - dialogs), 24dp (card - tarjetas principales), 28dp (xl - BottomSheet), 50% (pill - botones/FAB)
**Espaciado**: Escala 4dp (2, 4, 8, 12, 16, 20, 24, 32, 48)
**Elevation**: 0-1dp + stroke 1dp en dark mode
**Modo oscuro**: Completo con values-night/colors.xml

### Componentes Base Obligatorios (core-ui)
1. **view_header_date.xml** - Encabezado con selector fecha, flechas, avatar
2. **card_macro_summary.xml** - Tarjeta 24dp, CircularProgressIndicator 150dp/14dp + 3 LinearProgressIndicator 10dp
3. **item_log_entry.xml** - Fila estándar: icono circular, título 15sp Bold, subtítulo 13sp, valor derecha
4. **sheet_base_container.xml** - BottomSheet: píldora arrastre, título 18sp Bold, botón acción ancho completo

### Convención Nomenclatura (UI_DESIGN_SYSTEM)
- Fragments: `[Modulo][Nombre]Fragment.java` → `fragment_[modulo]_[nombre].xml`
- BottomSheets/Dialogs: `[Modulo][Nombre]Sheet.java` → `sheet_[modulo]_[nombre].xml`
- Adapters/Items: `[Nombre]Adapter.java` → `item_[nombre].xml`

## Multi-Módulo Gradle Structure

### Estado Actual: **Paso 5 - 6 Módulos Activos (Backend incluido)** (Simplificado de 14 planeados)

```
LuPeFit/
├── app/                          # Módulo principal (Application, DI Manual, Navigation)
│   ├── data/local/               # Room Database, Entities, DAOs, TypeConverters
│   ├── domain/                   # Repositories, Engines, UseCases (stubs Paso 1)
│   └── ui/                       # Activities, Fragments (Escáner, Voz, Costos ✅)
├── core/
│   ├── core-domain/              # Entidades dominio puras, Repository interfaces, Use Cases
│   ├── core-ui/                  # Tema Material 3, Styles, Strings, Base Views, Components
│   ├── core-network/             # Retrofit, API Service, DTOs, SyncEngine, NetworkModule ✅
│   └── core-sync/                # WorkManager Sync, Conflict Resolution, Queue ✅
└── backend/                      # FastAPI + PostgreSQL + Docker ✅ **COMPLETADO**
```

### Módulos Planificados para Pasos Posteriores

```
├── core/
│   └── core-sync/                # WorkManager Sync, Conflict Resolution, Queue ✅
├── feature/
│   ├── feature-diario/           # Diario de alimentos, búsqueda, quick-add, escaneo
│   ├── feature-ejercicio/        # Ejercicios cardio/fuerza, rutinas, WOD, Timer
│   ├── feature-nutricion/        # Nutrientes, macros, metas, gráficos, net carbs
│   ├── feature-progreso/         # Peso, medidas, fotos, exportar, gráficos
│   ├── feature-recetas/          # Recetas, importar, crear, colecciones
│   ├── feature-escaneo/          # Código de barras (ML Kit), escaneo comida (Passio)
│   ├── feature-agua/             # Seguimiento hidratación
│   ├── feature-configuracion/    # Settings, preferencias, exportar, sync
│   ├── feature-perfil/           # Perfil, auth local, sync account
│   ├── feature-insights/         # Insights nutricionales IA, tendencias, predicciones
│   └── feature-planes/           # Planes de comida semanales, planes de entrenamiento
```

## Features Principales (TODAS GRATIS - Sin Premium Gating)

| Feature Module | Funcionalidad Completa Incluida |
|----------------|--------------------------------|
| **feature-diario** | Diario, búsqueda alimentos, quick add, escaneo código barras, **timestamps comida**, **macros por comida**, escaneo IA comida, **voz (hands-free)**, **costos por porción** |
| **feature-ejercicio** | Cardio/Fuerza, rutinas, **calorías ejercicio configurables**, RPE, series/reps |
| **feature-nutricion** | Resumen macros, **metas por comida**, **macros por gramo/porcentaje**, **net carbs mode**, gráficos MPAndroidChart |
| **feature-progreso** | Peso, medidas, fotos, **exportar datos (CSV/JSON)**, gráficos tendencia |
| **feature-recetas** | Crear, importar URL, ingredientes, macros, **colecciones de recetas** |
| **feature-escaneo** | Código barras (ML Kit), **escaneo IA comida (Passio SDK)**, **catálogo offline-first** |
| **feature-agua** | Seguimiento hidratación, meta diaria, recordatorios |
| **feature-configuracion** | Settings, exportar datos, notificaciones, tema, **apariencia**, **metas personalizadas por día**, unidades, **cost tracking toggle**, **presupuesto semanal** |
| **feature-perfil** | Auth local (email/password), sync account, perfil, **dashboard personalizable** |
| **feature-insights** | **Insights nutricionales IA**, tendencias, predicciones, weekly digest, patrones |
| **feature-planes** | **Planes de comida semanales**, planes de entrenamiento, quick log recipe, **presupuesto semanal** |

### Features EXCLUIDAS (por request)
- ~~Blog~~ / ~~Community~~ / ~~Friends~~ / ~~Payments~~ / ~~Fasting~~
- ~~Premium/Upsell/Feature Flags~~ → todo integrado gratis
- ~~Zendesk/Braze/Segment/Amplitude/Firebase Analytics~~ → backend propio
- ~~Facebook/Google Login~~ → email/password local + sync
- ~~Samsung Health/Google Fit/Health Connect~~ → opcional futuro

## Base de Datos (Room) - Entidades Principales

| Entidad | Tabla | Descripción |
|---------|-------|-------------|
| Usuario | `usuarios` | Email, password_hash, perfil antropométrico, metas |
| PerfilNutricional | `perfiles_nutricionales` | Calorías, macros, metas por comida/día |
| **FoodProduct** | `food_products` | **Catálogo offline-first: macros 100g, código barras, isFreshFood, precargado JSON, referencePrice, referenceQuantityGrams, priceUpdatedAt** |
| Alimento | `alimentos` | Info nutricional 100g, código barras, FTS5 search |
| EntradaDiario | `entradas_diario` | Comida, porción, macros calculados, timestamp, **estimatedCostSnapshot** |
| Ejercicio | `ejercicios` | MET value, tipo cardio/fuerza |
| EntradaEjercicio | `entradas_ejercicio` | Duración, calorías, series, reps, peso, RPE |
| Peso | `pesos` | Peso, grasa%, músculo, fecha |
| Medida | `medidas` | Cintura, cadera, pecho, brazo, pierna, cuello |
| FotoProgreso | `fotos_progreso` | URI local/servidor, tipo (frente/lado/espejo) |
| Receta | `recetas` | Ingredientes, instrucciones, macros por porción |
| IngredienteReceta | `ingredientes_receta` | Alimento + cantidad + orden |
| Agua | `entradas_agua` | ml, fecha |
| Configuracion | `configuraciones` | Key-value tipado (string/int/bool/float/json) |
| SyncQueue | `cola_sincronizacion` | Operaciones pendientes, reintentos, prioridad |

## Sincronización Offline-First

- **WorkManager** periódico (15 min + conectividad)
- **Bidireccional**: Pull (cambios servidor desde sync_version) + Push (cola local)
- **Resolución conflictos**:
  - Configuraciones: Server-wins
  - Entradas diario/ejercicio: Merge (timestamp)
  - Peso/Fotos: Más reciente gana
  - Recetas: Merge ingredientes, server-wins metadatos
- **Cola persistente** con reintentos exponenciales

## Backend (FastAPI + PostgreSQL + Docker)

```
backend/
├── app/
│   ├── api/v1/           # Endpoints REST (auth, diario, ejercicio, progreso, recetas, alimentos, agua, sync)
│   ├── models/           # SQLAlchemy (Usuario, Alimento, EntradaDiario, etc.)
│   ├── schemas/          # Pydantic (Request/Response DTOs)
│   ├── services/         # Business logic
│   ├── core/             # Config, security (JWT, bcrypt), database
│   └── tasks/            # Celery background tasks
├── alembic/              # Migraciones DB
├── Dockerfile
├── docker-compose.yml    # postgres, redis, backend, celery-worker, celery-beat
└── requirements.txt
```

**Endpoints clave**:
- `POST /auth/login`, `/auth/register`, `/auth/refresh`
- `GET/POST/PUT/DELETE /diario`, `/ejercicios`, `/progreso/peso`, `/progreso/fotos`
- `GET /alimentos/buscar?q=...`, `GET /alimentos/codigo-barras/{codigo}`
- `POST /sync/batch` - Bidireccional offline-first

## Native Libraries & SDKs Reemplazo

| Original | Reemplazo LuPeFit |
|----------|-------------------|
| `libpassiolib.so` (Passio) | **Mantener Passio SDK** o custom TFLite |
| `libbarhopper_v2.so` | **ML Kit Barcode Scanning** (`com.google.mlkit:barcode-scanning`) |
| `libshinobicharts-android.so` | **MPAndroidChart** (`com.github.PhilJay:MPAndroidChart`) |
| `libtensorflowlite_jni.so` | **TF Lite Official** (`org.tensorflow:tensorflow-lite`) |

## Third-Party SDKs LuPeFit (Mínimos)

| SDK | Propósito |
|-----|-----------|
| **Room + SQLite** | DB local |
| **Retrofit + OkHttp** | Networking |
| **WorkManager** | Background sync |
| **MPAndroidChart** | Gráficos |
| **ML Kit (Barcode/Text)** | Escaneo |
| **Passio SDK** | IA comida (opcional) |
| **TF Lite** | Inferencia local |
| **Glide/Coil** | Imágenes |
| **Material Components 3** | UI |
| **Gson** | JSON parsing (catalog, OpenFoodFacts) |
| **CameraX** | Preview + análisis |
| **Lottie** | Animaciones |

## Navigation Graphs por Feature

Cada feature tiene su `nav_*.xml`:
- `nav_diario` → Diario, Buscar, QuickAdd, Escanear, **Voz**, **Costos**
- `nav_ejercicio` → Lista, Buscar, Nuevo, Rutinas
- `nav_nutricion` → Resumen, Metas, Macros, Gráficos
- `nav_progreso` → Peso, Medidas, Fotos, Gráficos, Exportar
- `nav_recetas` → Lista, Crear, Importar, Detalle, Colecciones
- `nav_configuracion` → Cuenta, Perfil, Metas, Notificaciones, Apariencia, Sync, Exportar, **Cost Tracking**, **Presupuesto Semanal**
- `nav_perfil` → Perfil, Editar, Password, Login, Registro
- `nav_insights` → Semanal, Tendencias, Predicciones, Patrones
- `nav_planes` → Comida, Entrenamiento, Mis Planes, Crear, **Presupuesto**

**Main Nav**: Bottom Navigation (Diario | Ejercicio | Nutrición | Progreso | Más)

## Strings - Castellano Argentino (values-es-rAR)

Completo en `core-ui/src/main/res/values-es-rAR/strings.xml`:
- Auth: "Registrate", "Entrar", "¿Olvidaste tu contraseña?"
- Diario: "Almuerzo", "Merienda", "Completar diario", "Agregado rápido", **"Registro por voz"**, **"Costo estimado"**
- Ejercicio: "Series", "Repeticiones", "RPE", "Mis rutinas"
- Nutrición: "Net Carbs", "Metas por comida", "Micronutrientes"
- Progreso: "Exportar mis datos", "Frente/Lado/Espejo"
- Config: "Sincronizar ahora", "Tema claro/oscuro/sistema", "Unidades métricas/imperiales", **"Seguimiento de costos"**, **"Presupuesto semanal"**
- Escaneo: **"Apuntá el código de barras al recuadro"**, "Ingreso manual", "No encontrado", **"Precio de referencia"**
- Errores: "Probá de nuevo", "Sin conexión. Trabajando en modo offline."

## Análisis Completado

- [x] REA + apktool + jadx - Análisis completo App Referencia
- [x] Multi-DEX mapping (12 DEX)
- [x] Assets/config JSON (abtests, mpf_config, upsell)
- [x] Native libs (4 × 4 archs)
- [x] Navigation graphs (3)
- [x] **Arquitectura LuPeFit definida** (Feature-modular)
- [x] **Esquema Room definido** (15 entidades incl. FoodProduct con costos)
- [x] **Sync engine diseñado** (Offline-first)
- [x] **Backend FastAPI + Docker diseñado**
- [x] **Tema Material 3 completo** (Light/Dark, colores, tipografía, formas, componentes)
- [x] **Strings castellano argentino**
- [x] **Features excluidas** (blog, community, friends, payments, fasting)

---

## Próximos Pasos: Implementación

1. **PASO 6 - Feature Modules UI** (11 módulos con Navigation graphs XML)
2. **PASO 7 - Advanced Features** (Health Connect, CrossFit WOD, Dynamic Macros, Streaks, Mood, Notes)
3. **Testing** (Unit, Integration, UI)
4. **CI/CD** (GitHub Actions, Docker build, Deploy)
*Última actualización: 2026-11-22 - **Paso 7 (feature-agua logic) COMPLETADO**: AguaHomeFragment, ConfigurarMetaFragment, HistorialAguaFragment, ViewModels, WaterRepository/DAO, Navigation Safe Args. **Compilación verificada** (BUILD SUCCESSFUL). APK generado: 50.1 MB.*

---


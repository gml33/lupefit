# Features Document - LuPeFit Android App

## Project Overview
- **App Name**: LuPeFit
- **Package**: com.lupefit.app
- **Min SDK**: 28 (Android 9.0)
- **Target SDK**: 34 (Android 14)
- **Language**: Java + XML Views (traditional)
- **Architecture**: Feature-modular (Enfoque A), Clean Architecture por feature
- **Offline-first**: Room + WorkManager background sync
- **Backend**: Python + FastAPI + PostgreSQL (Docker)

## Paleta de Colores (Material 3) - Dirección Visual

| Color | Hex | Uso |
|-------|-----|-----|
| **Primary** | `#167D62` | Verde petróleo - Acciones principales, FAB, botones primarios |
| **Primary Container** | `#B7D86B` | Verde lima suave - Contenedores primarios, chips seleccionados |
| **Secondary** | `#F2A541` | Ámbar - Actividad, datos destacados, acentos |
| **Secondary Container** | `#FFF3E0` / `#8B6000` | Contenedores secundarios |
| **Tertiary** | `#2196F3` | Azul - Proteínas, info |
| **Surface** | `#FFFFFF` / `#1E1E1E` | Tarjetas (claro/oscuro) |
| **Surface Variant** | `#F6F8F5` / `#2A2A2A` | Fondo claro / variante oscura |
| **Background** | `#F6F8F5` / `#121212` | Fondo principal |
| **On Surface** | `#18211D` / `#F6F8F5` | Texto principal |
| **On Surface Variant** | `#66736D` / `#B7B9B5` | Texto secundario |
| **Error** | `#B3261E` / `#F2B8B5` | Solo errores/alertas |
| **Outline** | `#C4C7C3` / `#8A8D88` | Divisores, bordes |

**Tipografía**: Material 3 Type Scale (Display > Headline > Title > Body > Label)
**Formas**: 4dp (xs), 8dp (sm), 12dp (md), 16dp (lg), 28dp (xl)
**Espaciado**: Escala de 4dp (2, 4, 8, 12, 16, 24, 32, 48)
**Modo oscuro**: Completo con values-night/colors.xml

## Multi-Módulo Gradle Structure

```
LuPeFit/
├── app/                          # Módulo principal (Application, DI, Navigation)
├── core/
│   ├── core-data/                # Room DB, Repositories, DataSources
│   ├── core-domain/              # Use Cases, Entities, Repository Interfaces
│   ├── core-network/             # Retrofit, API Service, DTOs, Sync Engine
│   ├── core-ui/                  # Base Activities/Fragments, Views, Theme, Strings
│   └── core-sync/                # WorkManager Sync, Conflict Resolution, Queue
├── feature/
│   ├── feature-diario/           # Diario de alimentos, búsqueda, quick-add, escaneo
│   ├── feature-ejercicio/        # Ejercicios cardio/fuerza, rutinas
│   ├── feature-nutricion/        # Nutrientes, macros, metas, gráficos, net carbs
│   ├── feature-progreso/         # Peso, medidas, fotos, exportar, gráficos
│   ├── feature-recetas/          # Recetas, importar, crear, colecciones
│   ├── feature-escaneo/          # Código de barras (ML Kit), escaneo comida (Passio)
│   ├── feature-agua/             # Seguimiento hidratación
│   ├── feature-configuracion/    # Settings, preferencias, exportar, sync
│   ├── feature-perfil/           # Perfil, auth local, sync account
│   ├── feature-insights/         # Insights nutricionales IA, tendencias, predicciones
│   └── feature-planes/           # Planes de comida semanales, planes de entrenamiento
└── backend/                      # FastAPI + PostgreSQL (repositorio separado)
```

## Features Principales (TODAS GRATIS - Sin Premium Gating)

| Feature Module | Funcionalidad Completa Incluida |
|----------------|--------------------------------|
| **feature-diario** | Diario, búsqueda alimentos, quick add, escaneo código barras, **timestamps comida**, **macros por comida**, escaneo IA comida |
| **feature-ejercicio** | Cardio/Fuerza, rutinas, **calorías ejercicio configurables**, RPE, series/reps |
| **feature-nutricion** | Resumen macros, **metas por comida**, **macros por gramo/porcentaje**, **net carbs mode**, gráficos MPAndroidChart |
| **feature-progreso** | Peso, medidas, fotos, **exportar datos (CSV/JSON)**, gráficos tendencia |
| **feature-recetas** | Crear, importar URL, ingredientes, macros, **colecciones de recetas** |
| **feature-escaneo** | Código barras (ML Kit), **escaneo IA comida (Passio SDK)** |
| **feature-agua** | Seguimiento hidratación, meta diaria, recordatorios |
| **feature-configuracion** | Settings, exportar datos, notificaciones, tema, **apariencia**, **metas personalizadas por día**, unidades |
| **feature-perfil** | Auth local (email/password), sync account, perfil, **dashboard personalizable** |
| **feature-insights** | **Insights nutricionales IA**, tendencias, predicciones, weekly digest, patrones |
| **feature-planes** | **Planes de comida semanales**, planes de entrenamiento, quick log recipe |

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
| Alimento | `alimentos` | Info nutricional 100g, código barras, FTS5 search |
| EntradaDiario | `entradas_diario` | Comida, porción, macros calculados, timestamp |
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
| **Hilt/Dagger** | DI |
| **MPAndroidChart** | Gráficos |
| **ML Kit (Barcode/Text)** | Escaneo |
| **Passio SDK** | IA comida (opcional) |
| **TF Lite** | Inferencia local |
| **Glide/Coil** | Imágenes |
| **Material Components 3** | UI |

## Navigation Graphs por Feature

Cada feature tiene su `nav_*.xml`:
- `nav_diario` → Diario, Buscar, QuickAdd, Escanear
- `nav_ejercicio` → Lista, Buscar, Nuevo, Rutinas
- `nav_nutricion` → Resumen, Metas, Macros, Gráficos
- `nav_progreso` → Peso, Medidas, Fotos, Gráficos, Exportar
- `nav_recetas` → Lista, Crear, Importar, Detalle, Colecciones
- `nav_configuracion` → Cuenta, Perfil, Metas, Notificaciones, Apariencia, Sync, Exportar
- `nav_perfil` → Perfil, Editar, Password, Login, Registro
- `nav_insights` → Semanal, Tendencias, Predicciones, Patrones
- `nav_planes` → Comida, Entrenamiento, Mis Planes, Crear

**Main Nav**: Bottom Navigation (Diario | Ejercicio | Nutrición | Progreso | Más)

## Strings - Castellano Argentino (values-es-rAR)

Completo en `core-ui/src/main/res/values-es-rAR/strings.xml`:
- Auth: "Registrate", "Entrar", "¿Olvidaste tu contraseña?"
- Diario: "Almuerzo", "Merienda", "Completar diario", "Agregado rápido"
- Ejercicio: "Series", "Repeticiones", "RPE", "Mis rutinas"
- Nutrición: "Net Carbs", "Metas por comida", "Micronutrientes"
- Progreso: "Exportar mis datos", "Frente/Lado/Espejo"
- Config: "Sincronizar ahora", "Tema claro/oscuro/sistema", "Unidades métricas/imperiales"
- Errores: "Probá de nuevo", "Sin conexión. Trabajando en modo offline."

## Análisis Completado

- [x] REA + apktool + jadx - Análisis completo MFP
- [x] Multi-DEX mapping (12 DEX)
- [x] Assets/config JSON (abtests, mpf_config, upsell)
- [x] Native libs (4 × 4 archs)
- [x] Navigation graphs (3)
- [x] **Arquitectura LuPeFit definida** (Feature-modular)
- [x] **Esquema Room definido** (13 entidades)
- [x] **Sync engine diseñado** (Offline-first)
- [x] **Backend FastAPI + Docker diseñado**
- [x] **Tema Material 3 completo** (Light/Dark, colores, tipografía, formas, componentes)
- [x] **Strings castellano argentino**
- [x] **Features excluidas** (blog, community, friends, payments, fasting)

---

## Próximos Pasos: Implementación

1. **Setup proyecto Gradle** (settings.gradle.kts, version catalogs, build-logic)
2. **Core modules** (core-data, core-domain, core-network, core-ui, core-sync)
3. **Feature modules** (diario, ejercicio, nutricion, progreso, recetas, escaneo, agua, configuracion, perfil, insights, planes)
4. **App module** (MainActivity, Hilt, Navigation, Bottom Nav)
5. **Backend** (FastAPI, PostgreSQL, Docker, Sync batch endpoint)
6. **Testing** (Unit, Integration, UI)
7. **CI/CD** (GitHub Actions, Docker build, Deploy)
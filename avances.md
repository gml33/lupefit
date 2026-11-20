# Avances - LuPeFit: Replicación y Rediseño Completo

## Estado Actual: **DISEÑO COMPLETADO** ✅ - Listo para Implementación

### Fecha de Inicio: 2026-11-20
### Fase Actual: Implementación (Próxima)

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
- **Frontend**: Java + XML Views (tradicional), Feature-modular (Enfoque A)
- **Backend**: Python + FastAPI + PostgreSQL (Docker)
- **Auth**: Email + password (local + sync)
- **Sync**: Offline-first (Room + WorkManager)
- **Features excluidas**: Blog, Community, Friends, Payments, Fasting
- **Premium**: Eliminado - todas las features GRATIS

#### Hito 11: Capa de Datos (core-data) - Room Schema
- 13 entidades: Usuario, PerfilNutricional, Alimento, EntradaDiario, Ejercicio, EntradaEjercicio, Peso, Medida, FotoProgreso, Receta, IngredienteReceta, Agua, Configuracion, SyncQueue
- DAOs + Repositories + SyncQueue con reintentos exponenciales
- FTS5 en alimentos para búsqueda

#### Hito 12: Capa de Dominio (core-domain)
- Entidades de dominio puras (Kotlin data classes)
- 10 interfaces Repository
- 10+ Use Cases principales (Auth, Diario, Ejercicio, Progreso, Recetas, Sync, Export)

#### Hito 13: Capa de Red & Sincronización (core-network, core-sync)
- Retrofit API Service con 30+ endpoints
- Sync Batch endpoint bidireccional (/sync/batch)
- WorkManager SyncWorker (15 min + connectivity trigger)
- Estrategia de conflictos: Server-wins / Merge / Latest-wins por entidad

#### Hito 14: Backend FastAPI + Docker
- Estructura completa: models, schemas, api/v1, services, tasks
- SQLAlchemy models con UUID, índices, full-text search
- JWT Auth (access/refresh tokens, bcrypt)
- docker-compose.yml: postgres, redis, backend, celery-worker, celery-beat
- Sync batch endpoint con versionado global

#### Hito 15: Feature Modules UI & Navigation
- 11 feature modules (diario, ejercicio, nutricion, progreso, recetas, escaneo, agua, configuracion, perfil, insights, planes)
- Navigation graphs por feature (XML)
- Bottom Nav: Diario | Ejercicio | Nutrición | Progreso | Más
- Sin premium/upsell modules

#### Hito 16: UI - Tema Material 3 Completo (core-ui)
**Paleta LuPeFit (Dirección Visual):**
| Color | Light | Dark | Uso |
|-------|-------|------|-----|
| Primary | `#167D62` | `#B7D86B` | Verde petróleo - Acciones principales |
| Primary Container | `#B7D86B` | `#167D62` | Verde lima suave - Contenedores |
| Secondary | `#F2A541` | `#F2A541` | Ámbar - Actividad, destacados |
| Surface | `#FFFFFF` | `#1E1E1E` | Tarjetas |
| Background | `#F6F8F5` | `#121212` | Fondo |
| On Surface | `#18211D` | `#F6F8F5` | Texto principal |
| On Surface Variant | `#66736D` | `#B7B9B5` | Texto secundario |
| Error | `#B3261E` | `#F2B8B5` | Solo errores |

- **Tipografía**: Material 3 Type Scale (Display/Headline/Title/Body/Label)
- **Formas**: 4dp, 8dp, 12dp, 16dp, 28dp
- **Espaciado**: Escala 4dp (2-48dp)
- **Componentes**: 25+ widget styles (Button, FAB, TextInput, Chip, NavBar, Card, Dialog, Progress, Switch, Slider, Tooltip, Snackbar, Toolbar)
- **Modo oscuro**: Completo (values-night/colors.xml + themes.xml)

#### Hito 17: Strings - Castellano Argentino
- Archivo: `core-ui/src/main/res/values-es-rAR/strings.xml`
- 200+ strings localizadas (Auth, Diario, Ejercicio, Nutrición, Progreso, Recetas, Escaneo, Agua, Config, Perfil, Insights, Planes, Errores, Unidades)
- Formato fecha: dd/MM/yyyy, hora: HH:mm
- Términos: "Registrate", "Entrar", "Probá", "Tocá", "Apuntá", "Almuerzo", "Merienda"

#### Hito 18: Documentación Actualizada (features.md v2)
- Arquitectura LuPeFit completa
- Module map actualizado (11 features, sin premium)
- Paleta de colores documentada
- Schema Room documentado
- Backend endpoints documentados
- Native libs replacement plan

#### Hito 19: Alineación con UI_DESIGN_SYSTEM (Fuente de Verdad) ✅
- **core-ui/** completamente reescrito para coincidir con UI_DESIGN_SYSTEM.md
- **Paleta oficial**: Tokens `md_theme_*` (Primary `#006C4C`, Secondary `#4D6357`, etc.)
- **Regla de Oro**: Cero hex hardcodeado - solo `@color/md_theme_*` / `?attr/`
- **Formas exactas**: Card 24dp, List items 16dp, Buttons pill (50%), BottomSheet 28dp, Progress track 8dp
- **Elevation**: 0-1dp + stroke 1dp dark mode
- **4 Componentes base obligatorios creados**:
  1. `view_header_date.xml` - Selector fecha + avatar
  2. `card_macro_summary.xml` - CircularProgress 150dp/14dp + 3 LinearProgress 10dp
  3. `item_log_entry.xml` - Icono circular + título 15sp Bold + subtítulo 13sp + valor derecha
  4. `sheet_base_container.xml` - Píldora arrastre + título 18sp Bold + botón ancho completo
- **Drawables base**: ic_chevron_left/right, ic_avatar_placeholder, ic_food_placeholder, bg_rounded_xs
- **Strings**: macros labels, fecha_anterior/siguiente, avatar_usuario
- **build.gradle.kts** + proguard-rules + consumer-rules para módulo core-ui
- **Convención nomenclatura estricta**: `[Modulo][Nombre]Fragment` → `fragment_[modulo]_[nombre].xml`

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
10. `features.md` - v2: **Arquitectura LuPeFit completa**
11. `core-ui/src/main/res/values/colors.xml` - Paleta light (tokens `md_theme_*`)
12. `core-ui/src/main/res/values-night/colors.xml` - Paleta dark (tokens `md_theme_*`)
13. `core-ui/src/main/res/values/themes.xml` - Tema Material 3 light
14. `core-ui/src/main/res/values-night/themes.xml` - Tema Material 3 dark
15. `core-ui/src/main/res/values/styles.xml` - 25+ widget styles (UI_DESIGN_SYSTEM specs)
16. `core-ui/src/main/res/values/dimens.xml` - Espaciado, elevation, icon sizes, touch targets
17. `core-ui/src/main/res/color/*.xml` - 12 color state lists
18. `core-ui/src/main/res/drawable/nav_active_indicator.xml`
19. `core-ui/src/main/res/drawable/splash_background.xml` + `_night.xml`
20. `core-ui/src/main/res/values-es-rAR/strings.xml` - 200+ strings argentino
21. `core-ui/src/main/res/layout/view_header_date.xml` - Componente base 1
22. `core-ui/src/main/res/layout/card_macro_summary.xml` - Componente base 2
23. `core-ui/src/main/res/layout/item_log_entry.xml` - Componente base 3
24. `core-ui/src/main/res/layout/sheet_base_container.xml` - Componente base 4
25. `core-ui/src/main/res/drawable/ic_chevron_left.xml` / `ic_chevron_right.xml`
26. `core-ui/src/main/res/drawable/ic_avatar_placeholder.xml` / `ic_food_placeholder.xml`
27. `core-ui/src/main/res/drawable/bg_rounded_xs.xml`
28. `core-ui/build.gradle.kts` + `proguard-rules.pro` + `consumer-rules.pro`

---

## Resumen Técnico LuPeFit

### Arquitectura
- **Pattern**: Clean Architecture por feature module
- **Módulos**: app + 5 core + 11 feature = 17 módulos Gradle
- **Offline-first**: Room + WorkManager sync bidireccional
- **Multi-DEX**: Preparado para 12+ DEX si crece

### Base de Datos (13 entidades)
- Usuario, PerfilNutricional, Alimento (FTS5), EntradaDiario, Ejercicio, EntradaEjercicio
- Peso, Medida, FotoProgreso, Receta, IngredienteReceta, Agua, Configuracion, SyncQueue

### Backend (FastAPI + PostgreSQL)
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

## Próximos Pasos: Implementación

### 1. Setup Proyecto Gradle
```kotlin
// settings.gradle.kts
include(":app")
include(":core:core-data", ":core:core-domain", ":core:core-network", ":core:core-ui", ":core:core-sync")
include(":feature:feature-diario", ":feature:feature-ejercicio", ":feature:feature-nutricion",
        ":feature:feature-progreso", ":feature:feature-recetas", ":feature:feature-escaneo",
        ":feature:feature-agua", ":feature:feature-configuracion", ":feature:feature-perfil",
        ":feature:feature-insights", ":feature:feature-planes")
```

### 2. Core Modules (orden de dependencias)
1. `core-domain` (sin deps) → Entidades, Repository interfaces, Use Cases
2. `core-data` (depende core-domain) → Room, DAOs, RepositoryImpl, SyncQueue
3. `core-network` (depende core-domain) → Retrofit, API Service, DTOs, SyncEngine
4. `core-sync` (depende core-data, core-network) → WorkManager, ConflictResolution
5. `core-ui` (sin deps) → Tema, Styles, Strings, Base Views, Navigation helpers

### 3. Feature Modules (cada uno: data, domain, presentation)
- `feature-diario` → DiarioFragment, BuscarAlimento, QuickAdd, EscanearCodigoBarras
- `feature-ejercicio` → Lista, Buscar, Nuevo, Rutinas
- `feature-nutricion` → Resumen, Metas, Macros, Gráficos (MPAndroidChart)
- `feature-progreso` → Peso, Medidas, Fotos, Exportar, Gráficos
- `feature-recetas` → Lista, Crear, Importar, Detalle, Colecciones
- `feature-escaneo` → ML Kit Barcode + Passio Food Scan
- `feature-agua` → Tracking, Meta, Recordatorios
- `feature-configuracion` → Cuenta, Perfil, Metas, Notificaciones, Apariencia, Sync, Exportar
- `feature-perfil` → Perfil, Editar, Login, Registro, Password, Sync Account
- `feature-insights` → Semanal, Tendencias, Predicciones, Patrones
- `feature-planes` → Comida, Entrenamiento, Mis Planes, Quick Log

### 4. App Module
- `LuPeFitApplication` (Hilt)
- `MainActivity` + Bottom Navigation (5 tabs)
- Navigation Host + Deep Links
- Splash Screen API

### 5. Backend
- FastAPI project structure
- SQLAlchemy models + Alembic migrations
- Pydantic schemas
- JWT Auth endpoints
- Sync batch endpoint
- Docker + docker-compose
- CI/CD GitHub Actions

---

## Comandos de Referencia

```bash
# Ver estructura MFP analizada
find /Users/marcelo/Desktop/Desarrollos/006/apktool_out -type f -name "*.xml" | grep -E "(navigation|manifest)"

# Buscar clases clave MFP
grep -r "class.*Activity" /Users/marcelo/Desktop/Desarrollos/006/jadx_out/sources/com/myfitnesspal/feature/ | head -30

# Ver experimentos pricing MFP
jq '.[] | select(.name | contains("price"))' /Users/marcelo/Desktop/Desarrollos/006/apktool_out/assets/config/abtests.json
```

---

## Notas para Retomar

**Archivos clave**:
1. `features.md` - Arquitectura LuPeFit completa (v2)
2. `avances.md` - Este historial
3. `core-ui/` - Tema Material 3 completo listo para usar
4. `apktool_out/` / `jadx_out/` - Referencia MFP (regenerables)

**Estado**: **Diseño 100% completo**. Próximo: **Implementación Gradle + Core Modules**.

---

*Última actualización: 2026-11-20 - Fase 2 completada, listo para implementar*
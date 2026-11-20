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

> **Nota**: Las capturas se agregarán conforme se implementen las pantallas. Actualmente el proyecto está en **Paso 1 - Esqueleto Base**.

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
| **UI** | Java 17+ + XML Views + ViewBinding | ✅ Paso 1 |
| **Tema** | Material 3 (Tokens `md_theme_*`) | ✅ Paso 1 |
| **BD Local** | Room (annotationProcessor) + Offline-First | ✅ Paso 1 |
| **DI** | Manual (AppContainer) - Sin Hilt/Dagger | ✅ Paso 1 |
| **Sync** | WorkManager + Backend propio | 🔄 Paso 2 |
| **Backend** | FastAPI + PostgreSQL + Docker | 🔄 Paso 2 |

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
| **App Container** | DI Manual puro Java (218 líneas, zero reflection) |

### 🔄 **En Desarrollo (Paso 2)**
| Módulo | Descripción |
|--------|-------------|
| **Core Network** | Retrofit, API Service, DTOs, SyncEngine |
| **Core Sync** | WorkManager SyncWorker, Conflict Resolution |
| **Backend** | FastAPI + PostgreSQL + Redis + Celery (Docker) |

### 📋 **Planificados (Pasos 3-4)**

#### **Feature Modules (11 módulos)**
| Módulo | Funcionalidades Clave |
|--------|----------------------|
| **feature-diario** | Búsqueda alimentos, Quick Add, Escaneo código barras, Timestamps por comida, Macros por comida |
| **feature-ejercicio** | Cardio/Fuerza, Rutinas, **WOD CrossFit (AMRAP/EMOM/For Time/Tabata)**, Timer fullscreen con alertas sonoras/vibración |
| **feature-nutricion** | Resumen macros circular, Metas por comida, Net Carbs mode, Gráficos MPAndroidChart |
| **feature-progreso** | Peso, Medidas, Fotos progreso, **Export CSV/JSON**, Gráficos tendencia |
| **feature-recetas** | Crear, Importar URL, Ingredientes, Macros, **Colecciones de recetas** |
| **feature-escaneo** | **ML Kit Barcode** + **Passio Food Scan (IA)** |
| **feature-agua** | Tracking hidratación, Meta diaria, Recordatorios |
| **feature-configuracion** | Cuenta, Perfil, Metas, Notificaciones, Apariencia, Sync, Exportar datos |
| **feature-perfil** | Auth local (email/password), Sync account, Dashboard personalizable |
| **feature-insights** | **IA Nutricional**, Tendencias, Predicciones, Weekly digest, Patrones |
| **feature-planes** | Planes comida semanales, Planes entrenamiento, Quick log recipe |

#### **Advanced Features (v2.0-v4.0)**
| Feature | Descripción |
|---------|-------------|
| **Health Connect** | Pasos, Calorías activas, Sesiones ejercicio, Frecuencia cardíaca |
| **Voice Logging** | SpeechRecognizer nativo + estrategia Whisper, onda visual, confirmación editable |
| **Dynamic Macros** | Recalculo automático carbs/proteína según gasto calórico (WOD + Health Connect) |
| **Streak Manager** | Rachas diarias con gamificación visual |
| **Mood Tracking** | 5 niveles (Muy bajo → Excelente) correlacionado con nutrición/entrenamiento |
| **User Notes** | Notas libres vinculadas a fecha/contexto |

---

## 🗄️ Base de Datos (Room) - 14 Entidades Offline-First

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

### Estructura del Proyecto
```
LuPeFit/
├── app/                          # Módulo principal
│   ├── src/main/java/com/lupefit/app/
│   │   ├── MyApplication.java           # Application class
│   │   ├── AppContainer.java            # Manual DI (218 líneas)
│   │   ├── data/local/                  # Room DB, Entities, DAOs
│   │   ├── domain/                      # Repositories, Engines, UseCases
│   │   └── ui/                          # Activities, Fragments (pendiente)
│   ├── src/main/res/                    # Recursos app (attrs, xml, mipmap)
│   └── build.gradle                     # Dependencias app
├── core/
│   ├── core-domain/              # Entidades dominio, interfaces
│   │   └── build.gradle.kts
│   └── core-ui/                  # Tema M3, Styles, Components, Strings AR
│       ├── src/main/res/
│       │   ├── values/colors.xml          # Paleta Light (tokens md_theme_*)
│       │   ├── values-night/colors.xml    # Paleta Dark
│       │   ├── values/themes.xml          # Tema Light (480+ líneas)
│       │   ├── values-night/themes.xml    # Tema Dark
│       │   ├── values/styles.xml          # 20+ Widget Styles
│       │   ├── values/dimens.xml          # Espaciado, elevation, touch targets
│       │   ├── values/integers.xml        # Duraciones animaciones
│       │   ├── color/*.xml                # 12 ColorStateLists
│       │   ├── drawable/*.xml             # Icons, backgrounds
│       │   ├── layout/                    # 4 Componentes base
│       │   └── values-es-rAR/strings.xml  # 365 strings argentino
│       └── build.gradle.kts
├── gradle/
│   ├── libs.versions.toml        # Version Catalog
│   └── wrapper/                  # Gradle 8.11.1
├── settings.gradle.kts           # include(:app, :core:core-domain, :core:core-ui)
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
| 2 | Core Network + Sync + Backend Docker | 🔄 En progreso | - |
| 3 | 11 Feature Modules (UI + Lógica) | ⏳ Pendiente | - |
| 4 | Advanced Features (Health Connect, Voice, CrossFit, Macros Dinámicos, Streaks, Mood) | ⏳ Pendiente | - |
| 5 | Testing, CI/CD, Release | ⏳ Pendiente | - |

### ✅ Paso 1 - Entregables Verificados
- [x] `build.gradle` (app) con dependencias exactas
- [x] `MyApplication.java` + `AppContainer.java` (DI Manual)
- [x] `AppDatabase.java` + 14 Entidades Room (offline-first)
- [x] 14 DAOs con LiveData queries
- [x] 14 Repositories + 5 Engines + 7 UseCases (stubs compilables)
- [x] `core:core-ui` - Tema M3 completo + 4 componentes base
- [x] `attrs.xml` - Atributos Material 3 custom
- [x] **APK Debug compila y genera exitosamente** (`./gradlew :app:assembleDebug`)

---

## 🐳 Backend (Próximo - Paso 2)

```yaml
# docker-compose.yml (planificado)
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
      DATABASE_URL: postgresql://lupefit:${DB_PASSWORD}@postgres:5432/lupefit
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

**Endpoints Principales (Planificados):**
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
- **Passio SDK** - Reconocimiento de alimentos por IA (opcional)

---

## 📞 Contacto & Links

- **Repo**: https://github.com/gml33/lupefit
- **Issues**: https://github.com/gml33/lupefit/issues
- **Wiki**: https://github.com/gml33/lupefit/wiki (próximamente)

---

> **Desarrollado con ❤️ en Argentina** | **Stack**: Java 17+ • XML Views • Room • Material 3 • WorkManager • FastAPI • PostgreSQL • Docker

**Última actualización**: 2026-11-20 | **Versión**: 1.0.0-Paso1 | **Build**: `d9942ea` ✅
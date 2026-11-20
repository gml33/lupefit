# PROJECT_ROADMAP.md — Visión de Producto, Arquitectura y Fases de Desarrollo

## 1. Resumen Ejecutivo y Stack Tecnológico Base
* **Propósito:** Ecosistema integral de nutrición, entrenamiento funcional (CrossFit), salud metabólica y seguimiento de hábitos, diseñado inicialmente como una herramienta personal *Offline-First* y escalable hacia una plataforma comunitaria para boxes/gimnasios.
* **Stack Técnico Estricto:**
  * **Lenguaje:** Java 17+ (Sin Kotlin, sin Dagger/Hilt, sin kapt/KSP).
  * **UI/UX:** Vistas tradicionales en XML + `ViewBinding` + Material Design 3 (`1.12.0+`) con soporte nativo para Modo Claro y Modo Oscuro (`DayNight`).
  * **Inyección de Dependencias:** Manual mediante patrón `AppContainer` en la clase `Application`.
  * **Persistencia y Sincronización:** Room Database (`annotationProcessor`) como única fuente de verdad local + `WorkManager` para sincronización en segundo plano.
  * **Gráficos y Sensores:** `MPAndroidChart`, Health Connect API, CameraX, Google ML Kit y `SpeechRecognizer`.

---

## 2. Fase 1: Producto Core Personal (v1.0 – v4.0 | Alcance Inmediato)
El núcleo de la aplicación consta de **11 módulos funcionales (~80 pantallas)** más 12 integraciones inteligentes diseñadas para uso diario sin fricción:

### A. Arquitectura y Conectividad Automática
* **Offline-First & Portabilidad:** Todas las entidades (`FoodLog`, `WorkoutLog`, `GlucoseLog`, `MoodEntry`, `UserNote`, `WeatherSnapshot`, `PersonalRecordEntity`) se almacenan localmente en Room con flags de sincronización (`isSynced`, `timestamp`). Incluye exportación/importación abierta en **JSON y CSV** y adaptador REST configurable para instancias *self-hosted* (ej. *SparkyFitness*).
* **Sincronización Pasiva con Smartwatch:** Conexión automática en segundo plano mediante **Health Connect** (`HealthSyncWorker` cada 2 horas + actualización en `onResume`) para importar pasos, frecuencia cardíaca, sueño y calorías activas desde el reloj inteligente.

### B. Nutrición Inteligente y Registro Rápido
* **Hands-Free Logging (Voz):** Registro rápido de comidas mediante dictado por voz (`SpeechRecognizer` con interfaz extensible para Whisper) que extrae cantidades, unidades y alimentos en una tarjeta de confirmación rápida.
* **Macronutrientes Dinámicos (Ciclado):** Ajuste automático de objetivos diarios de carbohidratos y proteínas cuando se registra un WOD de alta intensidad o Health Connect detecta un gasto calórico superior al umbral base.
* **Motor de Food Swaps:** Sugerencias inteligentes basadas en el historial frecuente del usuario para cubrir macros restantes sin exceder el límite calórico.
* **Modo Flexible / Restaurante:** Registro mediante porciones visuales (palma, puño, pulgar) para días de viaje o comidas fuera de casa sin perder la racha.
* **Planificador Meal Prep & Modo Cocina:** Generador de menús semanales con lista de compras agrupada (*offline*) y modo cocina manos libres (pantalla encendida y avance de pasos por voz).

### C. Entrenamiento Funcional (CrossFit) y Biometría
* **Temporizadores WOD:** Pantalla de alto contraste (`48sp` tabular) con soporte para **AMRAP, EMOM, For Time y Tabata**, alertas sonoras/vibración y botones táctiles grandes (`>= 56dp`).
* **Bóveda de Récords (1RM) y Calculadora de Discos:** Registro de marcas máximas con cálculo de porcentajes y vista personalizada (`PlateBarbellView`) que dibuja los discos estándar IWF a cargar en la barra.
* **Glucosa y Respuesta Metabólica:** Registro de mediciones (ayunas, pre/post entreno) sincronizado con Health Connect y cruzado gráficamente contra la ingesta de carbohidratos.
* **Hidratación + Clima por Geolocalización:** Consulta periódica de temperatura y humedad (`ACCESS_COARSE_LOCATION` + Open-Meteo) para ajustar metas de agua/electrolitos y correlacionar el clima con el rendimiento y el humor.
* **Readiness Score, Rachas, Humor y Notas:** Puntaje matutino (`0-100`) basado en sueño, pulso en reposo y carga reciente; sistema de rachas (*streaks*), registro diario de humor (`1-5`) y notas contextuales.

### D. Suite de Widgets de Escritorio (`RemoteViews` XML)
* **Widget 2x1 (Acciones Rápidas):** Acceso directo con un toque a micrófono (voz), escáner de código de barras y `+250ml` de agua.
* **Widget 4x2 (Resumen Diario):** Calorías restantes, barras de macros, racha activa y última lectura de glucosa.
* **Widget 2x2 (Hidratación y Humor):** Botones de carga rápida de agua y selector de estado de ánimo.

---

## 3. Fase 2: IA Privada, Visión y Automatización Doméstica (v5.0 | Backlog Técnico)
Iteraciones enfocadas en potenciar el análisis biomecánico y la privacidad de datos mediante procesamiento local:

* **Biomecánica y Visión por Computadora (CameraX + ML Kit Pose Detection):**
  * Contador automático de repeticiones validando rango de movimiento (ej. profundidad de sentadilla rompiendo el paralelo).
  * Trazador de trayectoria de barra (*Bar Path*) en tiempo real sobre el video para analizar levantamientos olímpicos.
  * Grabación de video con telemetría superpuesta (cronómetro AMRAP/EMOM, rondas y carga).
* **IA Conversacional Local (Self-Hosted LLM):**
  * Conector HTTP (`OkHttp`) hacia instancias locales de **Ollama** para consultar patrones históricos en lenguaje natural o generar rutinas personalizadas según el equipamiento disponible.
* **Hardware Avanzado y Domótica:**
  * Lectura directa por Bluetooth Low Energy (**BLE**) de bandas pectorales de frecuencia cardíaca durante WODs intensos.
  * Disparadores rápidos mediante etiquetas **NFC** (en recipientes de comida, botellas de agua o racks).
  * Integración vía **MQTT / Webhooks (Home Assistant / n8n)** para sincronizar luces del espacio de entrenamiento con intervalos EMOM/Tabata o automatizar reportes semanales.

---

## 4. Fase 3: Ecosistema del Gimnasio y Competencia (v6.0 | Expansión B2B2C)
Módulo opcional (`BoxCommunityModule`) activable mediante código de invitación o QR de un gimnasio, diseñado para mejorar la retención de alumnos y la gestión operativa del box:

### A. Sistema de Ligas y Competencia con Hándicap Híbrido
* **Nivelación Justa:** Permite competir de igual a igual entre perfiles diversos (ej. atleta joven con años de experiencia vs. atleta *Master* de +40 años en proceso de acondicionamiento) combinando:
  1. *Factor de Edad* (multiplicador progresivo por categoría Master).
  2. *Factor de Antigüedad / Experiencia* en el deporte.
  3. *Escalado Relativo de Cargas* basado en porcentajes de 1RM propios.
* **Modo Pizarra / TV del Box:** Vista de clasificación en vivo con doble pestaña (**Tabla Absoluta RX/Scaled** vs. **Tabla Nivelada con Hándicap**) y check-in de resultados mediante escaneo de QR en el gimnasio.

### B. Mensajería Operativa, Turnos y Comunidad del Box
* **Gestión de Turnos con Cupos en Vivo:** El dueño o coach publica los horarios del día con límite de asistentes; los alumnos confirman su turno con un toque, visualizan compañeros inscritos y gestionan listas de espera automáticas.
* **Tablón del WOD Diario Importable:** Publicación de la rutina del día con un botón de *"Cargar en mi Entrenamiento"* que traslada los ejercicios y configura el temporizador de la app del alumno al instante.
* **Canal de Anuncios y Alertas Administrativas (FCM):** Notificaciones push segmentadas para avisos operativos (feriados, eventos, equipamiento) y recordatorios automáticos de vencimiento de cuota con estado de membresía.

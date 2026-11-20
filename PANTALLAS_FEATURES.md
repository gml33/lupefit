# LuPeFit - Listado Completo de Pantallas y Features

## Navegación Principal (Bottom Navigation - 5 tabs)

| Tab | Fragmento Principal | Ícono | Descripción |
|-----|---------------------|-------|-------------|
| **1. Diario** | `DiarioFragment` | 📓 | Registro de comidas, resumen calorías/macros |
| **2. Ejercicio** | `EjercicioFragment` | 🏃 | Cardio, Fuerza, Rutinas |
| **3. Nutrición** | `NutricionFragment` | 🥗 | Metas, Macros, Gráficos, Net Carbs |
| **4. Progreso** | `ProgresoFragment` | 📊 | Peso, Medidas, Fotos, Exportar |
| **5. Más** | `MasFragment` (Drawer/Grid) | ⋮ | Recetas, Escaneo, Agua, Config, Perfil, Insights, Planes |

---

## 1. FEATURE-DIARIO (Diario de Alimentos)

### Pantallas
| Pantalla | Tipo | Descripción |
|----------|------|-------------|
| `DiarioFragment` | Fragment (Nav Host) | Vista principal: lista por comida (Desayuno/Almuerzo/Cena/Merienda/Otro), totales, FAB agregar |
| `BuscarAlimentoFragment` | Fragment | Búsqueda con suggestions, recientes, frecuentes, crear nuevo, escanear |
| `BuscarAlimentoActivity` | Activity (opcional) | Wrapper para deep link / share extension |
| `QuickAddDialogFragment` | BottomSheetDialogFragment | Agregado rápido: calorías, proteínas, carbs, grasas, selector comida |
| `EscanearCodigoBarrasFragment` | Fragment | CameraX + ML Kit Barcode, overlay, resultado → agregar |
| `EscanearComidaFragment` | Fragment | Passio SDK: cámara, análisis IA, resultados múltiples, agregar al diario |
| `DetalleAlimentoFragment` | Fragment | Info nutricional completa, selector porción (g/ml/unidad), agregar a comida |
| `CrearAlimentoFragment` | Fragment | Formulario: nombre, marca, macros 100g, porción base, código barras, foto |
| `EditarEntradaDiarioFragment` | BottomSheetDialogFragment | Cambiar porción, mover a otra comida, eliminar |
| `CompletarDiarioDialogFragment` | DialogFragment | Confirmación, resumen del día, felicitación |

### Funcionalidades
- [ ] CRUD entradas por comida (Desayuno, Almuerzo, Cena, Merienda, Otro)
- [ ] Búsqueda alimentos (local + remota paginada, FTS5)
- [ ] Búsquedas recientes y frecuentes (Room)
- [ ] Quick Add (solo macros + calorías)
- [ ] Escaneo código barras (ML Kit) → lookup local/remoto
- [ ] Escaneo IA comida (Passio SDK) → múltiples items detectados
- [ ] Timestamps por entrada (hora automática / editable)
- [ ] Macros por comida (desglose P/C/G por comida)
- [ ] Net Carbs mode (toggle en nutrición, afecta vista diario)
- [ ] Metas por comida (calorías/macros objetivo por comida)
- [ ] Completar diario (gamificación, streak)
- [ ] Copiar entradas (duplicar a otra fecha/comida)
- [ ] Modo offline completo (cola sync)

---

## 2. FEATURE-EJERCICIO (Ejercicio y Rutinas)

### Pantallas
| Pantalla | Tipo | Descripción |
|----------|------|-------------|
| `EjercicioFragment` | Fragment (Nav Host) | Tabs: Cardio / Fuerza / Rutinas, FAB agregar |
| `BuscarEjercicioFragment` | Fragment | Búsqueda ejercicios (local + custom), filtros tipo |
| `NuevoCardioFragment` | Fragment | Nombre, MET, calorías/min/kg, crear custom |
| `NuevaFuerzaFragment` | Fragment | Nombre, grupos musculares, series/reps/peso base |
| `RegistrarCardioFragment` | BottomSheetDialogFragment | Ejercicio, duración, intensidad, calorías calculadas |
| `RegistrarFuerzaFragment` | BottomSheetDialogFragment | Ejercicio, series, reps, peso, RPE, descanso |
| `RutinasFragment` | Fragment | Lista rutinas creadas, crear nueva, editar |
| `CrearRutinaFragment` | Fragment | Nombre, días, ejercicios ordenados, series/reps template |
| `DetalleRutinaFragment` | Fragment | Ver ejercicios, iniciar sesión, editar, duplicar |
| `SesionRutinaFragment` | Fragment | Pantalla activa: timer, series, peso, RPE, siguiente/previo |
| `MisEjerciciosFragment` | Fragment | Ejercicios creados por usuario, editar/eliminar |

### Funcionalidades
- [ ] Cardio: duración → calorías (MET × peso × tiempo)
- [ ] Fuerza: series × reps × peso → volumen, RPE opcional
- [ ] Calorías ejercicio configurables (override manual)
- [ ] Rutinas semanales (plantillas Lunes/Domingo)
- [ ] Sesión guiada con timer descanso
- [ ] Historial por ejercicio (progresión peso/reps)
- [ ] Ejercicios base (DB verificada) + custom usuario
- [ ] Filtros: tipo, grupo muscular, equipamiento

---

## 3. FEATURE-NUTRICION (Metas, Macros, Gráficos)

### Pantallas
| Pantalla | Tipo | Descripción |
|----------|------|-------------|
| `NutricionFragment` | Fragment (Nav Host) | Dashboard: resumen día, macros circulares, agua, metas |
| `MetasFragment` | Fragment | Calorías, macros (g/%): editor, calculadora automática (Mifflin-St Jeor) |
| `MetasPorComidaFragment` | Fragment | Editor metas por comida (Desayuno 25%, Almuerzo 35%, etc.) |
| `MetasPorDiaFragment` | Fragment | Metas diferentes por día semana (ej. más carbs día entreno) |
| `MacrosFragment` | Fragment | Detalle P/C/G/Fibra/Azúcar/Sodio, progreso barras |
| `MicronutrientesFragment` | Fragment | Vitaminas/minerales principales, % CDR |
| `GraficosNutricionFragment` | Fragment | MPAndroidChart: semanal/mensual calorías, macros, peso |
| `NetCarbsFragment` | Fragment | Toggle Net Carbs, explicación, vista diario ajustada |
| `AguaFragment` | Fragment (también en feature-agua) | Vasos/botellas, meta, recordatorios |

### Funcionalidades
- [ ] Calculadora BMR/TDEE (Mifflin-St Jeor × nivel actividad)
- [ ] Objetivo: perder/mantener/ganar → déficit/superávit automático
- [ ] Macros por gramo o porcentaje (toggle)
- [ ] Metas por comida (calorías + macros)
- [ ] Metas por día de la semana (7 perfiles)
- [ ] Net Carbs = Carbs totales - Fibra (toggle global)
- [ ] Gráficos: líneas (tendencia), barras (diario), circular (macro split)
- [ ] Alertas: "Te faltan Xg proteínas", "Excediste sodio"
- [ ] Resumen semanal automático

---

## 4. FEATURE-PROGRESO (Peso, Medidas, Fotos, Exportar)

### Pantallas
| Pantalla | Tipo | Descripción |
|----------|------|-------------|
| `ProgresoFragment` | Fragment (Nav Host) | Tabs: Peso / Medidas / Fotos / Gráficos / Exportar |
| `RegistrarPesoFragment` | BottomSheetDialogFragment | Peso, % grasa, músculo, nota, fecha/hora |
| `HistorialPesoFragment` | Fragment | Lista + gráfico tendencia, filtrar período |
| `RegistrarMedidasFragment` | BottomSheetDialogFragment | Cintura, cadera, pecho, brazo, pierna, cuello |
| `HistorialMedidasFragment` | Fragment | Lista + gráfico por medida |
| `FotosProgresoFragment` | Fragment | Grid por fecha/tipo (Frente/Lado/Espejo), cámara/galería |
| `CamaraProgresoFragment` | Fragment | Overlay guía (silhueta), timer, tipo, guardar |
| `GaleriaProgresoFragment` | Fragment | Comparar fechas, slider, zoom, compartir |
| `GraficosProgresoFragment` | Fragment | Peso + medidas combinados, tendencia, predicción |
| `ExportarDatosFragment` | Fragment | CSV/JSON, selector entidades, rango fechas, compartir |

### Funcionalidades
- [ ] Peso: kg, % grasa, músculo (opcional), notas
- [ ] Medidas: 6 puntos corporales, cm
- [ ] Fotos: 3 tipos (Frente, Lado, Espejo), cámara guiada
- [ ] Gráficos: peso + % grasa, medidas, IMC, predicción objetivo
- [ ] Exportar: CSV (Excel) / JSON (backup completo)
- [ ] Entidades exportables: diario, ejercicio, peso, medidas, fotos, recetas, agua, config
- [ ] Rango de fechas personalizable
- [ ] Compartir vía share sheet (Drive, Email, etc.)

---

## 5. FEATURE-RECETAS (Crear, Importar, Colecciones)

### Pantallas
| Pantalla | Tipo | Descripción |
|----------|------|-------------|
| `RecetasFragment` | Fragment (Nav Host) | Tabs: Mis Recetas / Colecciones / Importadas, FAB crear |
| `CrearRecetaFragment` | Fragment | Paso 1: Info (nombre, desc, porciones, tiempo, foto) |
| `IngredientesRecetaFragment` | Fragment | Paso 2: Buscar alimentos, cantidad g, orden, macros totales |
| `InstruccionesRecetaFragment` | Fragment | Paso 3: Pasos numerados, timer por paso |
| `DetalleRecetaFragment` | Fragment | Vista completa: ingredientes, instrucciones, macros/porción |
| `ImportarRecetaFragment` | Fragment | URL web / Texto plano → parsing automático ingredientes |
| `RevisionImportacionFragment` | Fragment | Matching ingredientes → alimentos DB, editar matches |
| `ColeccionesFragment` | Fragment | Grid colecciones (Alto Proteína, Bajo Carbs, Rápidas, etc.) |
| `DetalleColeccionFragment` | Fragment | Recetas de la colección, filtro tags |
| `EditarRecetaFragment` | Fragment | Editar cualquiera de los 3 pasos |

### Funcionalidades
- [ ] Crear: 3 pasos (Info → Ingredientes → Instrucciones)
- [ ] Ingredientes: busca en DB local, cantidad g, macros auto-sum
- [ ] Instrucciones: pasos con timer opcional por paso
- [ ] Importar URL: parse metadata (schema.org, OpenGraph)
- [ ] Importar texto: regex ingredientes + matching alimentos
- [ ] Colecciones: tags, destacadas, compartir
- [ ] Macros por porción calculados auto
- [ ] Log rápido al diario desde receta (porciones → entradas)
- [ ] Escalar receta (cambiar porciones → recalcular ingredientes)

---

## 6. FEATURE-ESCANEO (Código Barras + IA Comida)

### Pantallas
| Pantalla | Tipo | Descripción |
|----------|------|-------------|
| `EscanearCodigoBarrasFragment` | Fragment | CameraX + ML Kit, torch, zoom, historial escaneos |
| `ResultadoCodigoBarrasFragment` | Fragment | Alimento encontrado / no encontrado → crear / buscar manual |
| `EscanearComidaFragment` | Fragment | Passio SDK: cámara continua, detección multiple, confianza |
| `ResultadoEscaneoComidaFragment` | Fragment | Lista items detectados, editar cantidad, confirmar múltiple |
| `HistorialEscaneosFragment` | Fragment | Lista escaneos previos, re-log rápido |

### Funcionalidades
- [ ] Código barras: EAN-13, UPC, EAN-8, Code 128
- [ ] Lookup: local DB → remoto API → crear nuevo
- [ ] IA Comida (Passio): clasificación 1000+ alimentos, porción estimada
- [ ] Múltiples items en una foto (plato completo)
- [ ] Editar cantidad detectada antes de agregar
- [ ] Historial escaneos (últimos 50)
- [ ] Torch, zoom, gallery picker fallback

---

## 7. FEATURE-AGUA (Hidratación)

### Pantallas
| Pantalla | Tipo | Descripción |
|----------|------|-------------|
| `AguaFragment` | Fragment (Nav Host + Tab en Nutrición) | Círculo progreso, botones rápido, historial día |
| `MetaAguaDialogFragment` | BottomSheetDialogFragment | Meta diaria (ml), cálculo automático (35ml/kg) |
| `RecordatoriosAguaFragment` | Fragment | Horarios, intervalo, sonido, días activos |

### Funcionalidades
- [ ] Entrada rápida: Vaso (250ml), Botella (500ml, 1L), Personalizado
- [ ] Meta automática: 35ml × peso kg (configurable)
- [ ] Progreso circular animado
- [ ] Recordatorios: cada N horas, ventana horaria, días semana
- [ ] Historial 7/30 días, gráfico barras
- [ ] Widget home screen (opcional)

---

## 8. FEATURE-CONFIGURACION (Settings Completos)

### Pantallas
| Pantalla | Tipo | Descripción |
|----------|------|-------------|
| `ConfiguracionFragment` | Fragment (Nav Host) | Lista secciones con íconos, navegación |
| `CuentaFragment` | Fragment | Email, sincronización, eliminar cuenta |
| `PerfilConfigFragment` | Fragment | Nombre, fecha nac, género, altura, peso objetivo, actividad |
| `MetasConfigFragment` | Fragment | Acceso directo a MetasFragment (nutrición) |
| `NotificacionesFragment` | Fragment | Toggles: comida, agua, peso, ejercicio, insights, sync |
| `RecordatoriosComidaFragment` | Fragment | Horarios por comida (Desayuno 08:00, Almuerzo 13:00, etc.) |
| `RecordatoriosAguaConfigFragment` | Fragment | Ver feature-agua |
| `RecordatoriosPesoFragment` | Fragment | Frecuencia (diario/semanal), hora, día semana |
| `AparienciaFragment` | Fragment | Tema: Claro / Oscuro / Sistema, densidad, tamaño fuente |
| `IdiomaFragment` | Fragment | Español (AR) / English / Português (BR) |
| `UnidadesFragment` | Fragment | Métrico (kg, cm, kcal) / Imperial (lb, ft/in, cal) |
| `SincronizacionFragment` | Fragment | Estado sync, última vez, botón "Sincronizar ahora", auto-sync toggle |
| `ExportarDatosConfigFragment` | Fragment | Acceso directo a ExportarDatosFragment |
| `EliminarCuentaFragment` | Fragment | Confirmación, password, borrado local + remoto |
| `AcercaDeFragment` | Fragment | Versión, licencias OSS, privacidad, términos, contacto |

### Funcionalidades
- [ ] Tema: Light/Dark/System (Material 3 dynamic color ready)
- [ ] Unidades: Métrico/Imperial (persistencia + conversión vista)
- [ ] Idioma: es-rAR, en-US, pt-rBR (strings completos)
- [ ] Sync: Manual + Automático (WorkManager), estado visual
- [ ] Notificaciones granulares por feature
- [ ] Backup/Restore local (JSON) + Exportar CSV
- [ ] Eliminar cuenta: borrado cascada local + API remoto

---

## 9. FEATURE-PERFIL (Auth Local + Sync Account)

### Pantallas
| Pantalla | Tipo | Descripción |
|----------|------|-------------|
| `SplashActivity` | Activity | Logo animado, check auth, navegar a Login o Main |
| `LoginFragment` | Fragment | Email, password, "Recordarme", "¿Olvidaste?", "Registrate" |
| `RegistroFragment` | Fragment | Nombre, email, password, confirm, términos, onboarding redirect |
| `OnboardingFragment` | Fragment (Pager) | 5 pasos: Bienvenida → Datos antropométricos → Metas → Permisos → Sync |
| `PerfilFragment` | Fragment | Avatar, nombre, email, stats rápidos, editar, sync status |
| `EditarPerfilFragment` | Fragment | Nombre, avatar (cámara/galería), fecha nac, género |
| `CambiarPasswordFragment` | Fragment | Actual, nueva, confirmar, validación fortaleza |
| `RecuperarPasswordFragment` | Fragment | Email → link magic link / código OTP (backend) |
| `ResetearPasswordFragment` | Fragment | Token URL, nueva password, confirmar |
| `ConectarCuentaFragment` | Fragment | Login remoto, vincular cuenta local existente |
| `DesconectarCuentaFragment` | Fragment | Confirmar, mantener datos local, desvincular remoto |

### Funcionalidades
- [ ] Auth 100% local (bcrypt en dispositivo, Room)
- [ ] Sesión persistente (SharedPreferences encrypted + Room token)
- [ ] Onboarding guiado 5 pasos (datos → metas → permisos → sync)
- [ ] Cuenta remota opcional (sync multi-device, backup)
- [ ] Magic link / OTP para recuperación (backend FastAPI)
- [ ] Avatar: cámara + galería, crop circular, compresión
- [ ] Biométrico (opcional): huella/FaceID para desbloqueo app
- [ ] Cambio email (verificación nuevo email)

---

## 10. FEATURE-INSIGHTS (IA, Tendencias, Predicciones)

### Pantallas
| Pantalla | Tipo | Descripción |
|----------|------|-------------|
| `InsightsFragment` | Fragment (Nav Host) | Cards: Resumen semanal, Tendencias, Predicciones, Patrones |
| `ResumenSemanalFragment` | Fragment | Promedios: cal, macros, ejercicio, agua, peso vs semana anterior |
| `TendenciasFragment` | Fragment | Gráficos 4/12/52 semanas: calorías, proteína, peso, consistencia |
| `PrediccionesFragment` | Fragment | Peso proyectado (30/60/90d), fecha objetivo, ajustes sugeridos |
| `PatronesAlimentacionFragment` | Fragment | Horarios comidas, frecuencia snacks, macros por hora, fines semana |
| `RecomendacionesFragment` | Fragment | IA: "Aumentá proteína en cena", "Bebé más agua en tardes" |
| `DetalleInsightFragment` | Fragment | Explicación detallada, datos base, acción sugerida |

### Funcionalidades
- [ ] Resumen semanal automático (lunes AM notification)
- [ ] Tendencias: medias móviles, % cambio, consistencia (racha días log)
- [ ] Predicción peso: regresión lineal + ajustes por déficit real
- [ ] Patrones: heatmap horarios, macro timing, weekday vs weekend
- [ ] Reglas IA locales (TF Lite) + reglas heurísticas
- [ ] Insights accionables (botón "Aplicar" → ajusta metas/notifs)

---

## 11. FEATURE-PLANES (Comida + Entrenamiento)

### Pantallas
| Pantalla | Tipo | Descripción |
|----------|------|-------------|
| `PlanesFragment` | Fragment (Nav Host) | Tabs: Comida / Entrenamiento / Mis Planes |
| `PlanComidaSemanalFragment` | Fragment | Grid 7 días × 4 comidas, drag-drop recetas, macros día |
| `CrearPlanComidaFragment` | Fragment | Nombre, días, objetivo (alto prot, bajo carb, balanceado) |
| `DetallePlanComidaFragment` | Fragment | Vista día: recetas, macros, lista compras (agrupada) |
| `ListaComprasFragment` | Fragment | Ingredientes consolidados, checkbox, categorías, compartir |
| `PlanEntrenamientoFragment` | Fragment | Semanas × días, rutinas asignadas, progresión |
| `CrearPlanEntrenamientoFragment` | Fragment | Objetivo, nivel, días/sem, equipamiento, duración |
| `SesionPlanEntrenamientoFragment` | Fragment | Día actual: rutina, timer, tracking, completar |
| `MisPlanesFragment` | Fragment | Activos, archivados, plantillas, crear nuevo |
| `QuickLogPlanFragment` | BottomSheetDialogFragment | "Registrar del plan": comida/entreno de hoy → diario/ejercicio |

### Funcionalidades
- [ ] Plan Comida: 7 días × 4 comidas, recetas de DB, macros auto
- [ ] Lista compras: consolidada, categorías (verduras, carnes, etc.), share
- [ ] Plan Entreno: periodización simple (semanas 1-4, 5-8, 9-12)
- [ ] Progresión: aumento peso/reps/semanas automático
- [ ] Quick Log: un tap registra comida/entreno del plan en diario
- [ ] Plantillas: "Principiante 3d", "Hipertrofia 4d", "Corte 5d"
- [ ] Sync con diario/ejercicio (bidireccional)

---

## FLUJOS TRANSVERSALES

### Autenticación & Onboarding
```
Splash → (¿token válido?) → MainActivity : LoginFragment
LoginFragment → (registrate) → RegistroFragment → Onboarding (5 steps) → MainActivity
LoginFragment → (olvidé pass) → RecuperarPasswordFragment → Email → ResetearPasswordFragment
```

### Sincronización
```
WorkManager (15min + connectivity) → SyncWorker
  1. Pull: GET /sync/batch?since_version=X → merge local
  2. Push: POST /sync/batch {cola_local} → marcar sincronizados
  3. Conflictos: server-wins / merge / latest-wins por entidad
  4. UI: indicador "Sincronizando...", "Última sync: hace 2 min", error toast
```

### Deep Links
```
lupefit://diario/2026-01-15 → DiarioFragment fecha específica
lupefit://receta/123 → DetalleRecetaFragment
lupefit://plan/comida/456 → DetallePlanComidaFragment
lupefit://progreso/peso → ProgresoFragment tab Peso
lupefit://escaneo/codigo → EscanearCodigoBarrasFragment
```

### Permisos Runtime
| Permiso | Feature | Rationale |
|---------|---------|-----------|
| CAMERA | Escaneo, Fotos progreso, Avatar | Tomar fotos |
| READ_MEDIA_IMAGES | Importar fotos, avatar | Seleccionar galería |
| POST_NOTIFICATIONS | Recordatorios, insights semanales | Notificaciones push |
| BODY_SENSORS | (Opcional) Frecuencia cardíaca | Futuro |
| ACTIVITY_RECOGNITION | (Opcional) Pasos automáticos | Futuro |

---

## RESUMEN CUANTITATIVO

| Categoría | Cantidad |
|-----------|----------|
| **Feature Modules** | 11 |
| **Fragments Principales** | ~55 |
| **Dialogs/BottomSheets** | ~25 |
| **Activities** | 3 (Splash, Main, Auth wrapper) |
| **Navigation Graphs** | 11 (1 por feature + main) |
| **Entidades Room** | 13 |
| **DAO Interfaces** | 13 |
| **Repository Interfaces** | 10 |
| **Use Cases** | 25+ |
| **API Endpoints** | 35+ |
| **Strings (es-rAR)** | 250+ |
| **Colores (Light+Dark)** | 60+ |
| **Dimens** | 50+ |

---

## PRIORIZACIÓN IMPLEMENTACIÓN (Sugerida)

### Sprint 1: Core + Auth + Diario Básico
1. `core-domain` (entidades, interfaces)
2. `core-data` (Room, DAOs, SyncQueue)
3. `core-ui` (tema, base views, strings)
4. `core-network` (Retrofit, Auth API)
5. `core-sync` (WorkManager básico)
6. `feature-perfil` (Splash, Login, Registro, Onboarding)
7. `feature-diario` (DiarioFragment, BuscarAlimento, QuickAdd - sin escaneo)

### Sprint 2: Diario Completo + Ejercicio
8. `feature-diario` (Escanear código barras ML Kit, Escanear comida Passio)
9. `feature-ejercicio` (Cardio, Fuerza, Rutinas básicas)
10. `feature-nutricion` (Metas, Macros, Gráficos MPAndroidChart)

### Sprint 3: Progreso + Recetas + Agua
11. `feature-progreso` (Peso, Medidas, Fotos, Exportar)
12. `feature-recetas` (Crear, Importar, Detalle, Log rápido)
13. `feature-agua` (Tracking, Meta, Recordatorios)

### Sprint 4: Config + Perfil + Insights + Planes
14. `feature-configuracion` (Todas las secciones)
15. `feature-perfil` (Editar, Password, Sync Account, Avatar)
16. `feature-insights` (Semanal, Tendencias, Predicciones)
17. `feature-planes` (Comida, Entreno, Lista Compras, Quick Log)

### Sprint 5: Backend + Integración + Polish
18. Backend FastAPI completo + Docker deploy
19. Integración E2E sync bidireccional
20. Testing, bugs, performance, store assets

---

*Documento vivo - actualizar conforme avanza implementación*
# LuPeFit

Aplicación Android para registrar alimentación, consultar el progreso y organizar planes de comida y entrenamiento. La barra inferior reúne Diario, Ejercicio, Nutrición, Progreso y Más. Desde Más se accede a Recetas, Escaneo, Hidratación, Configuración, Perfil y Planes.

## Compilar y ejecutar

Requisitos de la app Android

- JDK 17 y Android SDK Platform 36.
- Android 9 (API 28) o posterior en el dispositivo.
- ADB y un dispositivo con depuración USB habilitada para la instalación manual.

El proyecto usa Gradle Wrapper, Android Gradle Plugin 8.9.1 y Kotlin 2.0.21. Abrí el repositorio en Android Studio y seleccioná JDK 17 para Gradle. Desde la raíz también podés ejecutar:

```sh
./gradlew :app:assembleDebug
adb devices
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n com.lupefit.app/.ui.main.MainActivity
```

Si hay varios dispositivos conectados, usá `adb -s <serial>` para indicar el destino.

## Organización del repositorio

| Directorio | Contenido |
| --- | --- |
| `app/` | Punto de entrada, navegación principal e integración de Room. |
| `core/` | Componentes compartidos, tema y recursos de interfaz. |
| `feature/` | Módulos funcionales de la app Android. |
| `backend/` | Servicio del proyecto y configuración de Docker Compose. |

Diario y Nutrición comparten la tarjeta de macronutrientes. El dashboard de Diario incluye encabezado de fecha, tarjetas de comida para desayuno, almuerzo, merienda y cena, y acciones conectadas al buscador con el tipo de comida correspondiente. El tema usa Material 3 DayNight para vistas XML, aplica la paleta nocturna desde los recursos de la app y desactiva Force Dark. Las migraciones de Room conservan los datos existentes y agregan las tablas locales de planes, rutinas y medidas corporales.

Los iconos de interfaz son Vector Drawable nativos de 24dp ubicados en `core/core-ui` o `app`, con colores derivados de tokens del tema. No se agregan librerías externas de iconos.

## Verificación en dispositivo

La cobertura funcional se registra en [la matriz de pruebas](docs/pruebas-funcionales.md).

En el Motorola e13 con Android 13 se comprobó la compilación, el arranque en claro y oscuro, el acceso a Planes, Mis Planes y el regreso a Diario. También se revisó visualmente el dashboard de Diario, sus tarjetas de comidas, hidratación y el estado vacío. La validación completa de todas las operaciones de escritura y en Android 14 queda pendiente.

Hasta el hito 7 están implementados los flujos locales de hidratación, peso, medidas, fotos de progreso, recetas, planes, presupuesto, perfil, configuración y exportación. Insights conserva datos de ejemplo y el ejercicio reciente no tiene todavía una fuente de entrenamiento real.

El hito 8 queda fuera de este corte. Incluye WOD/cronómetro, Health Connect, macros dinámicos, rachas, estado de ánimo y ampliaciones de voz. El escáner mantiene la lectura de código y la integración de alta automática como pendientes conocidos.

Para repetir la prueba, cambiá el modo de pantalla del teléfono desde los ajustes de Android, abrí la app y recorré las pestañas en cada modo. Revisá los cierres del proceso con:

```sh
adb logcat -b crash -d
```

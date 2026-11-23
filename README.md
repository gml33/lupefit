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

En el Motorola e13 con Android 13 se comprobó la compilación, instalación, arranque, Diario, Ejercicio y el WOD Timer. También se revisó visualmente el dashboard de Diario, sus tarjetas de comidas, hidratación, estado vacío y el diálogo WOD en modo claro. `logcat -b crash` quedó sin excepciones durante estas pruebas.

Hasta el hito 7 están implementados los flujos locales de hidratación, peso, medidas, fotos de progreso, recetas, planes, presupuesto, perfil, configuración, exportación, Quick Log, tendencias semanales, historial de entrenamientos, rachas, Health Connect opcional y WOD Timer configurable.

`features.md` es la fuente de verdad del alcance. Health Connect funciona de forma opcional con fallback offline; el escáner de código agrega productos encontrados al Diario. La integración remota y la validación completa en Android 14 continúan pendientes.

Para repetir la prueba, cambiá el modo de pantalla del teléfono desde los ajustes de Android, abrí la app y recorré las pestañas en cada modo. Revisá los cierres del proceso con:

```sh
adb logcat -b crash -d
```

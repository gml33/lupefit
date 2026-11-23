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

Diario y Nutrición comparten la tarjeta de macronutrientes. El tema usa Material Components DayNight, aplica la paleta nocturna desde los recursos de la app y desactiva Force Dark. La migración de Room de la versión 1 a la 2 agrega un índice para `personal_records.exerciseId` sin recrear la tabla.

## Verificación en dispositivo

En el Motorola e13 con Android 13 se comprobó el arranque, el recorrido de las cinco pestañas, el desplazamiento y los controles principales en modo claro y oscuro. También se comprobó el acceso a Perfil y el regreso de Mis Planes a Diario. Logcat quedó sin errores fatales durante los recorridos finales. La verificación en Android 14 queda pendiente.

Perfil contiene una vista en desarrollo. WOD y el registro de medidas muestran un aviso de disponibilidad. Los botones rápidos de hidratación de Diario aún no registran vasos.

Para repetir la prueba, cambiá el modo de pantalla del teléfono desde los ajustes de Android, abrí la app y recorré las pestañas en cada modo. Revisá los cierres del proceso con:

```sh
adb logcat -b crash -d
```

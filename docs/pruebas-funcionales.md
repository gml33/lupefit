# Pruebas funcionales de LuPeFit

Ejecutar la app con Material 3 en un dispositivo Android y comprobar las pantallas principales en claro y oscuro. Abrir una pantalla verifica navegación. Las operaciones de escritura requieren un dato de prueba y una comprobación después de reabrir la app.

| Módulo | Flujos que se deben comprobar | Alcance conocido |
| --- | --- | --- |
| Diario | Buscar alimento, alta manual, agregado rápido, edición de porción y acciones por comida | El dashboard muestra fecha, macros, hidratación y tarjetas de desayuno, almuerzo, merienda y cena. Las acciones de agua y el registro de recetas usan persistencia local. La voz depende del permiso y del servicio del dispositivo. |
| Ejercicio | Búsqueda, detalle de rutina, historial y WOD Timer | El historial lee Room; WOD permite AMRAP, EMOM, For Time y Tabata con duración/rondas configurables. |
| Nutrición | Resumen con datos y vacío, metas, detalle y gráficos | El acceso a Diario se puede probar desde el estado vacío. |
| Progreso | Consultar y registrar peso, medidas, fotos, gráficos y exportación | Los flujos están conectados a Room y al selector de archivos/cámara. Requieren una prueba de escritura aislada. |
| Recetas | Crear, editar, importar, buscar, filtrar, ordenar y registrar en Diario | La importación requiere conexión HTTPS y JSON-LD compatible. |
| Escaneo | Búsqueda manual, permiso de cámara, lectura de código y resultado | La lectura requiere un código físico. |
| Hidratación | Agregar agua, meta e historial | Verificar cantidades y persistencia. |
| Configuración | Acceso y persistencia de los ajustes implementados | Cuenta, apariencia, metas, costos, presupuesto, unidades, notificaciones y exportación guardan estado local. |
| Perfil | Apertura desde Más, edición y cambio de contraseña local | Las credenciales son locales. La cuenta remota requiere backend disponible. |
| Planes | Comida, entrenamiento, presupuesto, lista de compras, Mis Planes y Quick Log | Las semanas guardadas, avance, presupuesto y acceso a Agregado rápido persisten localmente. |
| Insights | Apertura desde Más, resumen semanal y tendencias | Calorías, proteínas, carbohidratos y grasas se agregan desde Diario; sin registros se muestra estado vacío. |

## Registro

La verificación reciente compiló el APK con JDK 17, lo instaló en un Motorola e13 con Android 13 y comprobó el arranque, Diario, Ejercicio, WOD Timer, Planes y navegación inferior. Se abrió el diálogo WOD, se verificó el contador y se revisó el layout de duración/rondas. `logcat -b crash` no registró excepciones. Android 14 y la validación completa de escritura siguen pendientes.

### Primera ronda con Material 3

- Compilación con JDK 17 y `:app:assembleDebug` exitosa.
- Motorola e13, Android 13. Aperturas en frío y recorrido de las cinco pestañas en claro y oscuro. Se visitaron también Recetas, Escaneo, Hidratación, Configuración, Perfil, Planes y Mis Planes, con regreso a Diario. El búfer de cierres de Logcat quedó vacío durante el recorrido.
- Se corrigió el recorte de las etiquetas de navegación y se inspeccionó el contraste de Diario en ambos modos.
- Se conectó el formulario de peso a su ViewModel. El guardado de un registro válido y la relectura del historial quedan por validar con datos de prueba.
- Diario mostró un resultado de catálogo al buscar «manzana» y abrió Agregado rápido.
- En el APK actualizado, el formulario de peso mostró la fecha inicial y permaneció en pantalla al intentar guardar con el campo de peso vacío.
- Planes abrió ambas pantallas de creación. En Presupuesto, un ítem de prueba de $123 actualizó el total simulado de $20.500 a $20.623. El presupuesto de prueba vive en memoria.
- El recorrido funcional adicional también dejó vacío el búfer de cierres.

### Auditoría visual Material 3

- Se corrigieron BottomSheets largos para que el contenido se desplace sin ocultar las acciones principales.
- Agregado rápido y resultado de escaneo muestran etiquetas reales para calorías, proteínas, carbohidratos y grasas.
- Diario incorpora tarjetas de comida con bordes Material 3, radio de 24dp y acciones táctiles.
- Macro summary, Progreso y Nutrición usan superficies planas con borde `colorSurfaceVariant` y elevación mínima.
- Los estados vacíos usan iconos Vector Drawable de `core-ui`.

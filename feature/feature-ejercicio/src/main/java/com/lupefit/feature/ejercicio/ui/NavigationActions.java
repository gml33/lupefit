package com.lupefit.feature.ejercicio.ui;

/**
 * Constantes de navegación para feature-ejercicio.
 * Los IDs de acción se generan automáticamente por Safe Args a partir del nav graph.
 */
public final class NavigationActions {

    private NavigationActions() {}

    // Acciones desde EjercicioHomeFragment
    public static int ACTION_TO_BUSCAR_EJERCICIO = 0;
    public static int ACTION_TO_NUEVA_RUTINA = 0;
    public static int ACTION_TO_WOD_TIMER = 0;
    public static int ACTION_TO_RUTINA_DETALLE = 0;

    /**
     * Inicializa los IDs de acción desde el nav graph.
     * Debe llamarse después de que el nav graph esté inflado.
     */
    public static void init(androidx.navigation.NavController navController) {
        try {
            java.lang.reflect.Field field;
            Class<?> rIdClass = Class.forName("com.lupefit.feature.ejercicio.R$id");

            java.lang.reflect.Field field1 = rIdClass.getField("action_ejercicioHomeFragment_to_buscarEjercicioFragment");
            ACTION_TO_BUSCAR_EJERCICIO = field1.getInt(null);

            java.lang.reflect.Field field2 = rIdClass.getField("action_ejercicioHomeFragment_to_nuevaRutinaFragment");
            ACTION_TO_NUEVA_RUTINA = field2.getInt(null);

            java.lang.reflect.Field field3 = rIdClass.getField("action_ejercicioHomeFragment_to_wodTimerFragment");
            ACTION_TO_WOD_TIMER = field3.getInt(null);

            java.lang.reflect.Field field4 = rIdClass.getField("action_ejercicioHomeFragment_to_rutinaDetalleFragment");
            ACTION_TO_RUTINA_DETALLE = field4.getInt(null);
        } catch (Exception e) {
            // Fallback: usar IDs conocidos si la reflexión falla
            e.printStackTrace();
        }
    }
}
🏋️ NanoGym (v1.4 Beta)
NanoGym es una aplicación nativa para Android diseñada bajo la filosofía de "Fricción Cero". Su objetivo es eliminar las barreras entre el atleta y su registro de entrenamiento, priorizando la disciplina inmutable, la privacidad total y el rendimiento offline.

[!IMPORTANT] Estado del Proyecto: Actualmente en fase Beta, utilizada a diario como herramienta principal de entrenamiento personal.

🚀 Características Principales
Sistema de Disciplina Inmutable: Lógica programada para impedir la manipulación de registros pasados y el borrado de entrenamientos el mismo día. Si no se completa, el sistema lo marca automáticamente como "Fallido".

Arquitectura Offline-First: Persistencia de datos local sin dependencia de servidores externos ni publicidad.

Control de Composición Corporal: Módulo independiente para el seguimiento de peso con gráficas de evolución.

Estética Pixel Art: Interfaz personalizada para una experiencia visual única y ligera.

🛠️ Stack Técnico
Lenguaje: Java (Android Nativo).

Persistencia: SQLite (Gestión de base de datos relacional local).

IDE: Android Studio & Google Antigravity.

UI: Material Design Components + Custom Drawables (Pixel Art).

🧠 Lógica de Negocio (Backend Focus)
El corazón de NanoGym es su gestor de estados. A diferencia de otras apps, aquí la integridad de los datos es clave para la disciplina:

Java
// Ejemplo conceptual de la validación de disciplina
if (fechaSeleccionada <= hoy && !entrenamientoCompletado) {
    bloquearBorrado(); // El compromiso no se negocia
}
Gris: Planificado (Futuro).

Verde: Completado (Éxito).

Rojo: Fallido (Día pasado sin completar).

📁 Estructura del Proyecto
/app/src/main/java: Lógica de controladores y gestión de SQLite.

/app/src/main/res/drawable: Recursos gráficos Pixel Art personalizados.

/app/src/main/res/layout: Definiciones de interfaz XML optimizadas.

🌐 Enlaces de Interés
Documentación y Web del Proyecto: nanogym.super.site

Contacto: [Tu LinkedIn]

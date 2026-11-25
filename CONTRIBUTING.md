# Guía de Contribución a HanziMemo

¡Gracias por tu interés en contribuir a **HanziMemo**! Este proyecto es una plataforma de código abierto orientada a facilitar el aprendizaje, la caligrafía y la retención mnemotécnica de caracteres chinos (Hanzi).

## Principios del Proyecto

1. **Preservación Funcional**: Cualquier modificación debe preservar la compatibilidad y las funcionalidades existentes.
2. **Offline-First & Privacidad**: El reconocimiento óptico (OCR) y el análisis de trazos se realizan en el dispositivo del usuario sin enviar datos a servidores externos.
3. **Clean Architecture**: Mantenemos una estricta separación de responsabilidades:
   - `data/`: Entidades Room, DAOs, repositorios e integraciones de hardware/ML.
   - `domain/`: Modelos de negocio, use cases e interfaces de repositorio.
   - `ui/`: Pantallas Compose, ViewModels, componentes reutilizables y temas Material 3.

## Flujo de Trabajo para Contribuir

1. **Fork y Creación de Rama**:
   ```bash
   git checkout -b feature/nueva-mejora
   # o
   git checkout -b fix/correccion-error
   ```

2. **Estilo de Código**:
   - Seguimos las directrices oficiales de estilo Kotlin de Jetpack Compose.
   - Mantén los nombres descriptivos y la semántica clara en funciones `@Composable`.
   - Evita dependencias innecesarias o código huérfano.

3. **Pruebas Unitarias**:
   - Toda lógica nueva de dominio, lexer o algoritmos SRS debe acompañarse de pruebas unitarias en `app/src/test`.
   - Ejecuta la suite de pruebas localmente antes de enviar cambios:
     ```bash
     ./gradlew test
     ```

4. **Pull Requests**:
   - Describe con claridad el propósito del cambio, las pruebas realizadas y capturas de pantalla si involucra cambios en la interfaz.
   - Asegúrate de que el comando `./gradlew check` finalice sin errores ni advertencias críticas.

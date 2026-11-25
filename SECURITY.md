# Política de Seguridad de HanziMemo

## Modelo de Seguridad y Privacidad

HanziMemo sigue un enfoque **Offline-First y Privacidad por Diseño**:
- **Procesamiento On-Device**: Tanto el reconocimiento óptico (OCR) mediante Google ML Kit como el reconocimiento de trazos en lienzo se ejecutan localmente en el dispositivo. No se transmiten imágenes, fotos ni datos personales a servidores externos.
- **Sin Credenciales Expuestas**: La aplicación no requiere ni almacena claves secretas ni tokens de API en el código fuente.
- **Permisos Mínimos**:
  - `CAMERA`: Utilizado exclusivamente bajo demanda para la digitalización de caracteres impresos o manuscritos.
  - `READ_EXTERNAL_STORAGE` / `WRITE_EXTERNAL_STORAGE`: Con `maxSdkVersion="32"` y compatibilidad con Scoped Storage moderno en Android 13+.
  - `FileProvider`: Configurado con `exported="false"` para el almacenamiento temporal seguro de recortes.

## Reporte de Vulnerabilidades

Si descubres una posible vulnerabilidad de seguridad, te solicitamos que la reportes de manera responsable creando un reporte de seguridad privado en GitHub o contactando directamente a los mantenedores del repositorio.

-Requisitos previos: Tener instalado Java 17 (o superior), IntelliJ IDEA y Docker Desktop (encendido).

-Levantar RabbitMQ: Abrir una terminal (PowerShell o CMD) y ejecutar este comando para crear y arrancar el bróker en su máquina local:

docker run -d --name rabbitmq-bikestore -p 5672:5672 -p 15672:15672 rabbitmq:3-management


-Abrir y ejecutar en IntelliJ:

-Descomprimir el .zip y abrir la carpeta desde IntelliJ (File > Open).

-Esperar unos segundos a que Maven descargue las dependencias automáticamente.

-Ejecutar BikestoreAsyncApplication.java con el botón verde de Run.

-Probar los pedidos: Ejecutar en terminal varias veces PowerShell o CMD:

curl.exe -X POST http://localhost:8080/api/orders/ordenar

PARA VER EN WEB:

Para ver los mensajes fallidos y las colas en la interfaz web de administración de RabbitMQ:

Abre tu navegador (Chrome, Edge, Firefox) y entra a la siguiente dirección:http://localhost:15672

Inicia sesión con las credenciales por defecto del contenedor:

Username: guest

Password: guest

En el menú superior, haz clic en la pestaña Queues and Streams (o Queues).

Verás la tabla con tus tres colas (bikestore.orders, bikestore.emails y bikestore.dlq). En la fila de bikestore.dlq, fíjate en la columna Ready: ahí aparecerá el número de pedidos que fallaron tras los 3 reintentos.

Para inspeccionar el JSON del pedido fallido:

Haz clic encima del nombre bikestore.dlq.

Baja hasta la sección Get messages y despliégala.

Asegúrate de que el Ack Mode esté en Nack message requeue true (para leerlo sin borrarlo de la cola).

Haz clic en el botón Get Message(s); abajo se desplegará el payload exacto ({"pedidoId": "e922de1b...", "paymentStatus": "PENDING"}) junto con los encabezados (x-death) que indican cuántas veces fue rechazado.
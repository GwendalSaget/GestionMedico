Proyecto Gestion de datos medical Gwendal Saget
 
Este proyecto es una aplicación Android diseñada para la gestión segura de información médica, utilizando Firebase para la autenticación y almacenamiento de datos. La aplicación tiene tres pantallas principales, cada una con funcionalidades clave:

Pantalla de Conexión: Esta pantalla permite al usuario autenticarse a través de Firebase. Si el usuario ya tiene credenciales almacenadas en la base de datos de Firebase, puede iniciar sesión introduciendo su nombre de usuario y contraseña. Si no hay registros, el usuario puede crear una nueva cuenta. En caso de que las credenciales sean incorrectas, la aplicación muestra un mensaje de error. Esta pantalla gestiona la lógica de autenticación, redirigiendo al usuario a las demás pantallas después de un inicio de sesión exitoso.

Pantalla de Gestión de Datos Médicos: En esta pantalla, el usuario puede ingresar, modificar y visualizar su información médica. Los datos incluyen información, como la presión arterial, la temperatura, el nombre de los medicamentos, y las horas de sueño. Además, la interfaz permite la actualización de estos datos, brindando flexibilidad al usuario para mantener su información médica actualizada.

Pantalla de Envío de Datos Médicos: Esta pantalla permite al usuario enviar sus datos médicos a un profesional de la salud a través de un correo electrónico. El usuario debe introducir la dirección de correo electrónico del médico, y la aplicación enviará un correo electrónico con la información médica. Esta funcionalidad se implementa utilizando una intent para enviar el correo, garantizando que los datos sean enviados de forma segura y privada.

Arquitectura y Tecnologías:
Firebase: Se utiliza para la autenticación de usuarios y el almacenamiento de datos médicos. Firebase Realtime Database se emplea para almacenar los datos cifrados.
Jetpack Compose: Para la creación de la interfaz de usuario, se utiliza Jetpack Compose, lo que permite desarrollar de manera declarativa interfaces intuitivas y dinámicas.
Email Intent: Para el envío de la información médica a un médico, se utiliza un intent de correo electrónico, que es una forma sencilla y segura de compartir la información a través de la aplicación.

GitHub : https://github.com/GwendalSaget/GestionMedico

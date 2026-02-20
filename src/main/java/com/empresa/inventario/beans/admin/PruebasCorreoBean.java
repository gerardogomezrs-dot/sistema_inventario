package com.empresa.inventario.beans.admin;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class PruebasCorreoBean {

    private String destinatario;
    private String asunto;
    private String mensaje;

    public void enviar() {
//        // 1. Configuración del servidor SMTP (ejemplo con Gmail)
//        Properties props = new Properties();
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.host", "smtp.gmail.com");
//        props.put("mail.smtp.port", "587");
//
//        // 2. Sesión y Autenticación
//        Session session = Session.getInstance(props, new Authenticator() {
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication("tu-correo@gmail.com", "tu-clave-de-app");
//            }
//        });
//
//        try {
//            // 3. Crear el mensaje
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress("tu-correo@gmail.com"));
//            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
//            message.setSubject(asunto);
//            message.setText(mensaje);
//
//            // 4. Enviar
//            Transport.send(message);
//            System.out.println("¡Correo enviado con éxito!");
//
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//    }
    
    // Getters y Setters...
}}
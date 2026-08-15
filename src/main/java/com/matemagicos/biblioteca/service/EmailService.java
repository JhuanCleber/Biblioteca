package com.matemagicos.biblioteca.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  private final JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String remetente;

  public EmailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  public void enviarCodigoRecuperacao(String destinatario, String nome, String codigo) {
    SimpleMailMessage mensagem = new SimpleMailMessage();
    mensagem.setFrom(remetente);
    mensagem.setTo(destinatario);
    mensagem.setSubject("Matemágicos - Código de recuperação de senha");
    mensagem.setText(
        "Oi, " + nome + "! 🧙‍♂️\n\n" +
            "Recebemos um pedido pra redefinir sua senha no Matemágicos.\n\n" +
            "Seu código é: " + codigo + "\n\n" +
            "Esse código vale por 15 minutos. Se você não pediu isso, pode ignorar " +
            "este email — sua senha continua a mesma.\n\n" +
            "Até já,\nEquipe Matemágicos");

    mailSender.send(mensagem);
  }

  public void enviarCodigoVerificacao(String destinatario, String nome, String codigo) {
    SimpleMailMessage mensagem = new SimpleMailMessage();
    mensagem.setFrom(remetente);
    mensagem.setTo(destinatario);
    mensagem.setSubject("Matemágicos - Confirme seu email");
    mensagem.setText(
        "Oi, " + nome + "! 🧙‍♂️\n\n" +
            "Bem-vindo(a) ao Matemágicos! Pra confirmar seu email, digite este código no app:\n\n" +
            "Seu código é: " + codigo + "\n\n" +
            "Esse código vale por 24 horas. Se você não criou essa conta, pode ignorar este email.\n\n" +
            "Até já,\nEquipe Matemágicos");

    mailSender.send(mensagem);
  }
}
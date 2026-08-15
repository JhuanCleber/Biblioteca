package com.matemagicos.biblioteca.service;

import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Primeira barreira de proteção pro nome que aparece no ranking público — não é
 * (nem tenta ser) um sistema de moderação de conteúdo completo, só uma checagem
 * simples adequada pro estágio atual do app. Usado hoje só no cadastro; se um
 * dia
 * existir edição de perfil (nome), reaproveitar esse mesmo service lá.
 */
@Service
public class FiltroDeNomeService {

  // Só letras (com acento), espaço, hífen e apóstrofo — sem números, símbolos ou
  // emojis
  private static final Pattern CARACTERES_PERMITIDOS = Pattern.compile("^[\\p{L} '\\-]+$");

  // Lista básica de termos ofensivos em português (já normalizados: sem espaço,
  // sem acento, minúsculo) — cobre os casos mais óbvios de nome impróprio.
  private static final List<String> PALAVRAS_PROIBIDAS = List.of(
      "arrombado", "babaca", "bosta", "buceta", "burro", "caralho", "corno",
      "cuzao", "desgracado", "estupro", "fdp", "filhodaputa", "foda", "fudido",
      "idiota", "imbecil", "krl", "lixo", "merda", "otario", "pariu", "paunocu",
      "peido", "piranha", "poha", "porra", "prostituta", "puta", "putaria",
      "retardado", "verga", "viado", "vagabundo", "vsf", "xoxota");

  public boolean nomeValido(String nome) {
    return CARACTERES_PERMITIDOS.matcher(nome).matches();
  }

  public boolean contemPalavraProibida(String nome) {
    String normalizado = normalizar(nome);
    for (String palavra : PALAVRAS_PROIBIDAS) {
      if (normalizado.contains(palavra)) {
        return true;
      }
    }
    return false;
  }

  // Remove acento, deixa minúsculo, troca substituições comuns tipo "0"->"o" e
  // tira
  // tudo que não é letra (incluindo espaços) — dificulta burlar com "P O R R A",
  // "p0rra" etc. sem precisar de uma lista gigante de variações.
  private String normalizar(String texto) {
    String semAcento = Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
    String minusculo = semAcento.toLowerCase();
    String substituido = minusculo
        .replace('0', 'o')
        .replace('1', 'i')
        .replace('3', 'e')
        .replace('4', 'a')
        .replace('5', 's')
        .replace('7', 't')
        .replace('@', 'a');
    return substituido.replaceAll("[^a-z]", "");
  }
}
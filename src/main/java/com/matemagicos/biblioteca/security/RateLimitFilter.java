package com.matemagicos.biblioteca.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Limita quantas vezes cada IP pode bater numa rota sensível dentro de uma
 * janela
 * de tempo — protege contra spam de cadastro, força bruta de login, e spam de
 * emails (esqueci-senha, verificação). Implementação simples de "janela fixa"
 * em
 * memória: reseta quando o app reinicia e não é compartilhada entre instâncias
 * (não é um problema aqui — o back roda numa instância só).
 *
 * Cada rota tem seu próprio limite, pensado pro caso de uso dela:
 * - login/cadastro: apertado, pra dificultar força bruta e spam de contas
 * - esqueci-senha/reenviar-verificação: apertado, porque cada tentativa manda
 * um email de verdade
 * - refresh/logout: mais folgado, porque são chamados automaticamente pelo app
 * com frequência normal
 */
public class RateLimitFilter extends OncePerRequestFilter {

  private static class Limite {
    final int maxTentativas;
    final long janelaMs;

    Limite(int maxTentativas, long janelaMs) {
      this.maxTentativas = maxTentativas;
      this.janelaMs = janelaMs;
    }
  }

  private static class Contador {
    long inicioJanela = System.currentTimeMillis();
    int tentativas = 0;
  }

  private final Map<String, Limite> limitesPorRota = new HashMap<>();
  private final Map<String, Contador> contadores = new ConcurrentHashMap<>();

  public RateLimitFilter() {
    limitesPorRota.put("/auth/login", new Limite(5, 60_000)); // 5 por minuto
    limitesPorRota.put("/auth/cadastro", new Limite(5, 600_000)); // 5 por 10min
    limitesPorRota.put("/auth/esqueci-senha", new Limite(3, 900_000)); // 3 por 15min
    limitesPorRota.put("/auth/redefinir-senha", new Limite(5, 900_000)); // 5 por 15min
    limitesPorRota.put("/auth/reenviar-verificacao", new Limite(3, 900_000)); // 3 por 15min
    limitesPorRota.put("/auth/verificar-email", new Limite(10, 900_000)); // 10 por 15min (criança pode digitar errado)
    limitesPorRota.put("/auth/refresh", new Limite(30, 60_000)); // 30 por minuto (uso automático)
    limitesPorRota.put("/auth/logout", new Limite(30, 60_000)); // 30 por minuto
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    Limite limite = limitesPorRota.get(request.getRequestURI());

    if (limite == null) {
      filterChain.doFilter(request, response);
      return;
    }

    String chave = obterIp(request) + ":" + request.getRequestURI();
    Contador contador = contadores.computeIfAbsent(chave, k -> new Contador());

    boolean bloqueado;
    synchronized (contador) {
      long agora = System.currentTimeMillis();
      if (agora - contador.inicioJanela > limite.janelaMs) {
        contador.inicioJanela = agora;
        contador.tentativas = 0;
      }
      contador.tentativas++;
      bloqueado = contador.tentativas > limite.maxTentativas;
    }

    if (bloqueado) {
      response.setStatus(429);
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write(
          "{\"ok\":false,\"erro\":\"Muitas tentativas. Espere um pouco e tente de novo.\"}");
      return;
    }

    filterChain.doFilter(request, response);
  }

  private String obterIp(HttpServletRequest request) {
    String encaminhadoPor = request.getHeader("X-Forwarded-For");
    if (encaminhadoPor != null && !encaminhadoPor.isBlank()) {
      return encaminhadoPor.split(",")[0].trim();
    }
    return request.getRemoteAddr();
  }
}
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

public class RateLimitFilter extends OncePerRequestFilter {

  private static class Limite {
    final int maxTentativas;
    final long janelaMs;
    final boolean contarSoFalhas;

    Limite(int maxTentativas, long janelaMs, boolean contarSoFalhas) {
      this.maxTentativas = maxTentativas;
      this.janelaMs = janelaMs;
      this.contarSoFalhas = contarSoFalhas;
    }
  }

  private static class Contador {
    long inicioJanela = System.currentTimeMillis();
    int tentativas = 0;
  }

  private final Map<String, Limite> limitesPorRota = new HashMap<>();
  private final Map<String, Contador> contadores = new ConcurrentHashMap<>();

  public RateLimitFilter() {
    limitesPorRota.put("/auth/login", new Limite(8, 60_000, true)); // 8 erradas por minuto
    limitesPorRota.put("/auth/cadastro", new Limite(5, 600_000, false)); // 5 por 10min
    limitesPorRota.put("/auth/esqueci-senha", new Limite(3, 900_000, false)); // 3 por 15min
    limitesPorRota.put("/auth/redefinir-senha", new Limite(8, 900_000, true)); // 8 erradas por 15min
    limitesPorRota.put("/auth/reenviar-verificacao", new Limite(3, 900_000, false)); // 3 por 15min
    limitesPorRota.put("/auth/verificar-email", new Limite(10, 900_000, true)); // 10 erradas por 15min
    limitesPorRota.put("/auth/refresh", new Limite(30, 60_000, false)); // 30 por minuto (uso automático)
    limitesPorRota.put("/auth/logout", new Limite(30, 60_000, false)); // 30 por minuto
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

    boolean bloqueadoAntes;
    synchronized (contador) {
      long agora = System.currentTimeMillis();
      if (agora - contador.inicioJanela > limite.janelaMs) {
        contador.inicioJanela = agora;
        contador.tentativas = 0;
      }
      bloqueadoAntes = contador.tentativas >= limite.maxTentativas;
    }

    if (bloqueadoAntes) {
      response.setStatus(429);
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write(
          "{\"ok\":false,\"erro\":\"Muitas tentativas. Espere um pouco e tente de novo.\"}");
      return;
    }

    filterChain.doFilter(request, response);

    boolean deuCerto = response.getStatus() < 400;

    synchronized (contador) {
      if (limite.contarSoFalhas && deuCerto) {
        // Acertou — zera o contador de erros. Erros anteriores dentro da
        // janela não devem continuar pesando depois de um acerto.
        contador.tentativas = 0;
      } else if (!limite.contarSoFalhas || !deuCerto) {
        contador.tentativas++;
      }
    }
  }

  private String obterIp(HttpServletRequest request) {
    String encaminhadoPor = request.getHeader("X-Forwarded-For");
    if (encaminhadoPor != null && !encaminhadoPor.isBlank()) {
      return encaminhadoPor.split(",")[0].trim();
    }
    return request.getRemoteAddr();
  }
}
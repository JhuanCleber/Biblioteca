
# Matemágicos — Setup completo (Banco + Back + Front)

git add .
git commit -m "Biblioteca"
git push origin main

Este guia conecta as três pontas: **MySQL**, **Spring Boot** e **React Native (Expo)**.

## 0. Pré-requisitos (instalar antes de tudo)

- **JDK 17**
- **Maven** (o `mvnw`/`mvnw.cmd` do projeto depende da pasta `.mvn/wrapper`, que
  às vezes não é copiada corretamente entre computadores — se der erro ao
  rodar `mvnw`, instale o Maven manualmente e use `mvn` direto, é mais confiável)
- **MySQL Server + MySQL Workbench**
- **Node.js** (para o front-end)
- **Postman** (para testar a API)

---

## 1. Variáveis de ambiente (fazer uma vez por computador)

O projeto **não tem mais senha nem chave secreta no código** — tudo vem de
variáveis de ambiente do Windows. Crie estas duas (Painel de Controle >
Variáveis de Ambiente > Variáveis de usuário > Novo):

| Variável | Valor |
| --- | --- |
| `DB_PASSWORD` | a senha do MySQL **desse** computador (pode ser diferente em cada máquina) |
| `JWT_SECRET` | uma chave secreta para assinar os tokens (pode reaproveitar a mesma de outro computador, ou gerar uma nova) |

Depois de criar, **feche e abra o VSCode de novo** — ele só enxerga variáveis de ambiente criadas depois que foi aberto.

---

## 2. Banco de Dados (MySQL Workbench)

1. Abra o MySQL Workbench e conecte em `localhost:3306` (usuário `root`, senha = a que você configurou nesse MySQL — a mesma do `DB_PASSWORD` acima).
2. Vá em **File > Open SQL Script** e abra:

   ``
   database\01_create_database.sql
   ``

3. Execute o script (raio / Ctrl+Shift+Enter). Ele cria o banco `app_biblioteca` e **todas as 6 tabelas**: `usuario`, `administrador`, `jogo`, `desempenho_jogo`, `avaliacao_final`, `pontuacao_historico`.
4. Confirme em `Schemas > app_biblioteca > Tables`.

> O script usa `CREATE TABLE IF NOT EXISTS`, então é seguro rodar de novo — nunca apaga dados existentes. O Hibernate (`ddl-auto=update`) também cria/ajusta as tabelas sozinho quando o Spring Boot sobe, mesmo sem rodar o script manualmente.

---

## 3. Back-end (Spring Boot)

```powershell
cd C:\Users\Kauan\Documents\Biblioteca
mvn spring-boot:run
```

Saída esperada (resumida):

``
Tomcat started on port 8080
Started MatemagicosApplication in X.XX seconds
``

### Endpoints disponíveis

| Método | URL | Autenticação | Body | Resposta |
| --- | --- | --- | --- | --- |
| POST | `/auth/cadastro` | Pública | `{ "nome", "idade", "email", "senha" }` | `{ "ok", "mensagem", "usuario" }` ou erro 400 |
| POST | `/auth/login` | Pública | `{ "email", "senha" }` | `{ "ok", "mensagem", "usuario", "token" }` ou erro 401 |
| GET | `/usuarios` | **Requer JWT** (`Authorization: Bearer <token>`) | — | `[{ id, nome, email, ... }]` ou 401 sem token válido |
| GET | `/actuator/health` | Pública | — | `{ "status": "UP" }` |

> `/usuarios` (e qualquer rota futura fora de `/auth/**`) só responde com um
> token JWT válido no header. O token vem no campo `token` da resposta do
> login e vale 24h (`jwt.expiration-ms`).

### Teste rápido no PowerShell

```powershell
# Cadastro
Invoke-RestMethod -Method POST -Uri "http://localhost:8080/auth/cadastro" `
  -ContentType "application/json" `
  -Body '{"nome":"Maria","idade":7,"email":"maria@exemplo.com","senha":"1234"}'

# Login (guarda o token retornado)
$login = Invoke-RestMethod -Method POST -Uri "http://localhost:8080/auth/login" `
  -ContentType "application/json" `
  -Body '{"email":"maria@exemplo.com","senha":"1234"}'
$token = $login.token

# Usando o token pra acessar uma rota protegida
Invoke-RestMethod -Method GET -Uri "http://localhost:8080/usuarios" `
  -Headers @{ Authorization = "Bearer $token" }
```

A senha é salva no banco com **hash BCrypt** (não texto puro).

### Testar pelo Postman (mais fácil)

Importe a coleção `Matemagicos.postman_collection.json` (Postman > Import).
Ela já vem com 3 requisições na ordem certa — Cadastro, Login, Listar
Usuários — e **captura o token automaticamente** depois do login, sem
precisar copiar/colar nada.

---

## 4. Front-end (React Native + Expo)

```powershell
cd C:\Users\Kauan\Documents\Matemagicos
npm install
npx expo start
```

### URL do backend

Está em `src/config/api.ts`:

```typescript
export const API_URL = 'http://10.0.2.2:8080';   // ← emulador Android
```

- **Emulador Android** (Expo Go / Android Studio): `10.0.2.2` (padrão, já configurado).
- **iOS simulator** ou **web**: troque para `http://localhost:8080`.
- **Dispositivo físico**: descubra o IP da máquina (`ipconfig` no Windows) e use `http://SEU_IP:8080`. O celular precisa estar no **mesmo Wi-Fi**.

---

## 5. Fluxo de teste ponta a ponta

1. Suba o back: `mvn spring-boot:run` (aguarde `Started MatemagicosApplication`).
2. Suba o front: `npx expo start` → abra com `a` (Android) ou `w` (web).
3. No app, toque em **"Criar minha conta"**.
4. Preencha:
   - Nome: `Maria`
   - Idade: `7`
   - Email: `maria@exemplo.com`
   - Senha: `1234`
   - Confirmar senha: `1234`
5. Toque em **"Criar minha conta"**. Deve aparecer "🎉 Conta criada!" e ir para a Home — a Home já mostra o nome real ("Olá, Maria! 👋") e os pontos/moedas reais vindos do back.
6. Volte para Login e tente entrar com o mesmo email/senha.
7. Para confirmar que está no banco, abra o MySQL Workbench:

   ```sql
   SELECT * FROM app_biblioteca.usuario;
   ```

   A senha aparece como hash `$2a$10$...`.

---

## 6. Estrutura criada/modificada

### Back (Spring Boot)

``
src/main/java/com/matemagicos/biblioteca/
├── MatemagicosApplication.java
├── config/
│   └── SecurityConfig.java            ← só /auth/** é público; resto exige JWT
├── security/
│   ├── JwtService.java                ← NOVO (gera/valida o token)
│   └── JwtAuthenticationFilter.java   ← NOVO (lê o header Authorization)
├── controller/
│   ├── AuthController.java            ← POST /auth/cadastro, /auth/login (retorna token)
│   └── UsuarioController.java         ← delega a conversão pro service, sem duplicar código
├── DTO/
│   ├── CadastroRequestDTO.java
│   ├── LoginRequestDTO.java
│   ├── LoginResponseDTO.java          ← agora tem o campo "token"
│   └── UsuarioDTO.java
├── exception/
│   └── GlobalExceptionHandler.java    ← erros de validação em JSON; erro 500 não vaza detalhe interno (só loga no servidor)
├── models/
│   └── Usuario.java
├── repository/
│   └── UsuarioRepository.java
└── service/
    └── UsuarioService.java            ← BCrypt + cadastro/login/geração de token + listar() já devolve DTO

src/main/resources/
└── application.properties             ← porta 8080, ddl-auto=update, senha e chave JWT vêm de variável de ambiente

database/
└── 01_create_database.sql             ← as 6 tabelas, com IF NOT EXISTS (nunca apaga dados)
``

### Front (React Native)

``
src/
├── config/
│   └── api.ts                         ← URL do backend
├── services/
│   └── authService.ts                 ← fetch + cadastro/login; mostra mensagens de erro por campo quando o back manda
├── screens/
│   ├── LoginScreen.tsx                ← chama a API e passa o usuário pra Home
│   ├── CadastroScreen.tsx             ← chama a API e passa o usuário pra Home
│   └── HomeScreen.tsx                 ← mostra nome/pontos/moedas reais do usuário logado
└── (data/usuarios.ts removido)        ← mock em memória apagado
``

---

## 7. Próximos passos sugeridos

- Persistir o usuário logado com **AsyncStorage** (instalar: `npx expo install @react-native-async-storage/async-storage`) — hoje, ao fechar o app, ele sempre volta pro Login.
- Adicionar endpoint de listar/cadastrar **Jogos** (tabelas já existem: `jogo`, `desempenho_jogo`, `avaliacao_final`, `pontuacao_historico`).
- Diferenciar papéis de usuário (comum vs administrador) — hoje qualquer usuário autenticado consegue acessar `/usuarios`, não só um admin.
- 📌 Se um dia o app for publicado de verdade (Play Store/App Store): revisar o fluxo de cadastro para incluir um responsável/adulto, por causa da LGPD e das políticas de apps infantis das lojas. Não é necessário para o projeto atual (uso educacional/portfólio).

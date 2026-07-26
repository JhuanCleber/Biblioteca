# Matemágicos — Setup completo (Banco + Back + Front)

Este guia conecta as três pontas: **MySQL**, **Spring Boot** e **React Native (Expo)**.

---

## 1. Banco de Dados (MySQL Workbench)

1. Abra o MySQL Workbench e conecte em `localhost:3306` (user `root` / senha `Refreskant123`).
2. Vá em **File > Open SQL Script** e abra:
   ```
   C:\Users\Kauan\Documents\Biblioteca\database\01_create_database.sql
   ```
3. Execute o script (raio / Ctrl+Shift+Enter). Ele cria o banco `app_biblioteca` e a tabela `usuario`.
4. Opcional: confirme em `Schemas > app_biblioteca > Tables > usuario`.

> O Hibernate (`ddl-auto=update`) também recria/ajusta as tabelas automaticamente quando o Spring sobe. Mesmo se você **não** rodar o script, o banco será criado — mas rodar o script é a forma documentada.

---

## 2. Back-end (Spring Boot)

```powershell
cd C:\Users\Kauan\Documents\Biblioteca
.\mvnw spring-boot:run
```

Saída esperada (resumida):
```
Tomcat started on port 8080
Started MatemagicosApplication in X.XX seconds
```

### Endpoints disponíveis

| Método | URL                   | Body                                                | Resposta                                                                 |
|--------|-----------------------|-----------------------------------------------------|--------------------------------------------------------------------------|
| POST   | `/auth/cadastro`      | `{ "nome", "idade", "email", "senha" }`              | `{ "ok": true, "mensagem": "...", "usuario": {...} }` ou erro 400/401    |
| POST   | `/auth/login`         | `{ "email", "senha" }`                              | `{ "ok": true, "mensagem": "...", "usuario": {...} }` ou erro 401        |
| GET    | `/usuarios`           | —                                                   | `[{ id, nome, email, ... }]` (útil pra debugar no navegador)            |
| GET    | `/actuator/health`    | —                                                   | `{ "status": "UP" }`                                                     |

### Teste rápido no navegador ou no PowerShell

```powershell
# Cadastro
Invoke-RestMethod -Method POST -Uri "http://localhost:8080/auth/cadastro" `
  -ContentType "application/json" `
  -Body '{"nome":"Maria","idade":7,"email":"maria@exemplo.com","senha":"1234"}'

# Login
Invoke-RestMethod -Method POST -Uri "http://localhost:8080/auth/login" `
  -ContentType "application/json" `
  -Body '{"email":"maria@exemplo.com","senha":"1234"}'
```

A senha é salva no banco com **hash BCrypt** (não texto puro).

---

## 3. Front-end (React Native + Expo)

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

## 4. Fluxo de teste ponta a ponta

1. Suba o back: `mvnw spring-boot:run` (aguarde `Started MatemagicosApplication`).
2. Suba o front: `npx expo start` → abra com `a` (Android) ou `w` (web).
3. No app, toque em **"Criar minha conta"**.
4. Preencha:
   - Nome: `Maria`
   - Idade: `7`
   - Email: `maria@exemplo.com`
   - Senha: `1234`
   - Confirmar senha: `1234`
5. Toque em **"Criar minha conta"**. Deve aparecer "🎉 Conta criada!" e ir para a Home.
6. Volte para Login e tente entrar com o mesmo email/senha.
7. Para confirmar que está no banco, abra o MySQL Workbench:
   ```sql
   SELECT * FROM app_biblioteca.usuario;
   ```
   A senha aparece como hash `$2a$10$...`.

---

## 5. Estrutura criada/modificada

### Back (Spring Boot)

```
src/main/java/com/matemagicos/biblioteca/
├── MatemagicosApplication.java
├── config/
│   └── SecurityConfig.java            ← NOVO (libera tudo + CORS + BCrypt)
├── controller/
│   ├── AuthController.java            ← NOVO (POST /auth/cadastro, /auth/login)
│   └── UsuarioController.java         ← refatorado (devolve DTO sem senha)
├── DTO/
│   ├── CadastroRequestDTO.java
│   ├── LoginRequestDTO.java
│   ├── LoginResponseDTO.java
│   └── UsuarioDTO.java
├── exception/
│   └── GlobalExceptionHandler.java    ← NOVO (erros de validação em JSON)
├── models/
│   └── Usuario.java                   ← ajustado (default 0 nos pontos)
├── repository/
│   └── UsuarioRepository.java
└── service/
    └── UsuarioService.java            ← BCrypt + cadastro/login

src/main/resources/
└── application.properties             ← porta 8080, ddl-auto=update

database/
└── 01_create_database.sql             ← NOVO
```

### Front (React Native)

```
src/
├── config/
│   └── api.ts                         ← NOVO (URL do backend)
├── services/
│   └── authService.ts                 ← NOVO (fetch + cadastro/login)
├── screens/
│   ├── LoginScreen.tsx                ← agora chama a API
│   ├── CadastroScreen.tsx             ← agora chama a API
│   └── HomeScreen.tsx                 ← inalterado
└── (data/usuarios.ts removido)        ← mock em memória apagado
```

---

## 6. Próximos passos sugeridos

- Persistir o usuário logado com **AsyncStorage** (instalar: `npx expo install @react-native-async-storage/async-storage`).
- Trocar a saudação fixa da Home pelo nome do usuário logado.
- Adicionar endpoint de listar/cadastrar **Jogos** (tabelas já previstas nas entities `Jogo`, `PontuacaoHistorico`).
- Quando quiser evoluir pra JWT: dá pra adicionar a dependência `jjwt-api` e gerar o token dentro do `AuthController` — o resto da estrutura já está pronta.

# 6trip

Aplicativo Android para descoberta e registro de locais de interesse, com integração de mapas, câmera e armazenamento em nuvem.

---

## Funcionalidades

- Autenticação com e-mail e senha (Firebase Auth)
- Cadastro com validação de CPF, e-mail e senha
- Verificação de e-mail e recuperação de senha
- Mapa interativo com exibição da localização do usuário
- Cadastro de locais com coordenadas GPS, descrição, categoria e imagem
- Captura de foto via câmera com nome de arquivo baseado nas coordenadas
- Upload de imagens via Cloudinary
- Inventário de locais cadastrados
- Menu de configurações

---

## Telas

| Tela | Descrição |
|---|---|
| **Welcome** | Splash screen com solicitação de permissões e verificação de autenticação |
| **Login** | Autenticação com e-mail e senha, opção de "lembrar acesso" |
| **Cadastro** | Registro de novo usuário com validação de CPF, e-mail e senha |
| **Redefinir senha** | Recuperação de senha por e-mail |
| **Home** | Tela principal com mapa, câmera, inventário e menu de configurações |

---

## Stack

- **Linguagem:** Kotlin
- **UI:** Jetpack Compose + Material Design 3
- **Navegação:** Navigation Compose
- **Autenticação:** Firebase Auth
- **Banco de dados:** Cloud Firestore
- **Armazenamento:** Firebase Storage + Cloudinary
- **Mapas:** Google Maps SDK + Maps Compose
- **Localização:** Google Play Services Location
- **Rede:** Retrofit2 + OkHttp3
- **Imagens:** Coil Compose
- **Validação:** simple-cpf-validator
- **Build:** Gradle (Kotlin DSL), AGP 8.8.0

---

## Estrutura do projeto

```
app/src/main/java/com/example/a6trip/
├── MainActivity.kt
├── data/
│   └── auth/
│       ├── AuthRepository.kt
│       ├── CloudinaryApi.kt
│       ├── CloudinaryClient.kt
│       └── CloudinaryResponse.kt
├── navigation/
│   └── NavGraph.kt
└── ui/
    ├── components/
    ├── model/
    │   ├── User.kt
    │   └── Place.kt
    ├── screens/
    │   ├── home/
    │   ├── login/
    │   ├── register/
    │   ├── resetPassword/
    │   └── welcome/
    └── theme/
```

---

## Configuração do ambiente

### Pré-requisitos

- Android Studio Hedgehog ou superior
- JDK 17
- Android SDK (API 34+)

### Variáveis de ambiente

Crie um arquivo `local.properties` na raiz do projeto com:

```properties
MAPS_API_KEY=sua_chave_aqui
```

> A chave do Google Maps é obrigatória para o mapa funcionar. Obtenha uma em [Google Cloud Console](https://console.cloud.google.com/).

### Firebase

O arquivo `google-services.json` já está incluído no repositório em `app/`. Caso queira apontar para outro projeto Firebase, substitua o arquivo e ajuste as configurações.

---

## Permissões necessárias

| Permissão | Uso |
|---|---|
| `ACCESS_FINE_LOCATION` | Localização precisa via GPS |
| `ACCESS_COARSE_LOCATION` | Localização aproximada via rede |
| `INTERNET` | Comunicação com Firebase e Cloudinary |
| `CAMERA` | Captura de fotos dos locais |

---

## CI/CD

O pipeline de integração contínua é gerenciado pelo GitHub Actions. Consulte [.github/CI.md](.github/CI.md) para detalhes sobre as etapas, artefatos gerados e secrets necessários.

**Secrets necessários no repositório:**

| Secret | Descrição |
|---|---|
| `MAPS_API_KEY` | Chave da API do Google Maps |

---

## Versionamento

| Tag | Descrição |
|---|---|
| `v0.1.0` | Setup inicial — Firebase, navegação e telas básicas |
| `v0.2.0` | Tela de cadastro com validação de CPF, e-mail e senha |
| `v0.3.0` | Mapa, câmera, Cloudinary, inventário e configurações |
| `v0.3.1` | Correção do pipeline de CI |

# CI — Integração Contínua

Este documento explica o pipeline de CI configurado em `.github/workflows/build.yml`.

## Quando o pipeline roda?

O workflow é disparado automaticamente nos seguintes eventos:

- **Push** nas branches `master` ou `develop`.
- **Push de tag** no formato `v*` (ex: `v0.3.0`, `v1.0.0`).
- **Pull Request** direcionado para `master` ou `develop`.

Isso garante que todo código que entra nessas branches foi validado, e que toda release taggeada gera um APK disponível para download.

## O que cada etapa faz?

| Etapa | Descrição |
|---|---|
| **Checkout do código** | Clona o repositório dentro da máquina virtual do GitHub Actions. |
| **Configurar JDK 17** | Instala o Java 17 (distribuição Temurin), necessário para compilar o projeto Android. |
| **Cache do Gradle** | Armazena as dependências do Gradle em cache entre execuções, evitando downloads repetidos e acelerando o pipeline. |
| **Criar local.properties** | Cria o arquivo `local.properties` com a `MAPS_API_KEY` lida dos secrets do repositório. Necessário para o `secrets-gradle-plugin` resolver a chave da API do Google Maps durante o build. |
| **Dar permissão ao Gradlew** | Torna o script `gradlew` executável (`chmod +x`), necessário no Linux. |
| **Lint** | Executa `./gradlew lint` — analisa o código em busca de problemas de qualidade, acessibilidade e boas práticas Android. |
| **Testes unitários** | Executa `./gradlew test` — roda todos os testes unitários do projeto. Se algum teste falhar, o pipeline falha. |
| **Build debug** | Executa `./gradlew assembleDebug` — compila o APK de debug. Valida que o projeto compila sem erros. |
| **Upload do APK** | Salva o APK gerado como artefato do GitHub Actions. Qualquer membro do time pode baixar o APK diretamente pela interface do GitHub, na aba "Actions" do repositório. |

## Como ver os resultados?

1. Acesse o repositório no GitHub.
2. Clique na aba **Actions**.
3. Selecione a execução desejada para ver o log de cada etapa.
4. Na seção **Artifacts**, é possível baixar o APK gerado.

## Como o cache funciona?

A chave do cache é baseada no hash dos arquivos `*.gradle*` e `gradle-wrapper.properties`. Quando esses arquivos não mudam, o Gradle reutiliza as dependências já baixadas. Quando mudam (ex: nova dependência adicionada), o cache é recriado.

## Secrets necessários

O pipeline depende dos seguintes secrets configurados em **Settings → Secrets and variables → Actions** do repositório:

| Secret | Descrição |
|---|---|
| `MAPS_API_KEY` | Chave da API do Google Maps, usada para resolver `${MAPS_API_KEY}` no `AndroidManifest.xml` via `secrets-gradle-plugin`. |

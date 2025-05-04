# FURIAFanProfile

![image](https://github.com/user-attachments/assets/7961f55f-469d-4b2b-b766-b745f250d2fb)

## 📋 Visão Geral

FURIAFanProfile é um aplicativo mobile criado para coletar e organizar informações valiosas sobre fãs da FURIA Esports. A proposta é fornecer uma solução completa para identificar, validar e entender o comportamento dos torcedores, possibilitando experiências personalizadas com base em dados reais e integrados.


## 🚀 Funcionalidades

- **Cadastro de Perfil**: Coleta de dados básicos como nome, CPF, endereço e interesses pessoais

- **Histórico de Atividades**: Registro de eventos, compras e interações com o universo FURIA no último ano

- **Upload de Documentos**: Upload de documentos oficiais com validação de identidade via Inteligência Artificial

- **Integração com Redes Sociais**: Conexão com Instagram, Twitter, entre outras plataformas para leitura de interações com conteúdo de e-sports

- **Validação de Links**: AI analisa e valida perfis em sites de e-sports vinculados ao usuário

- **Experiência Segura e Personalizada**: Proteção de dados sensíveis e insights sobre o perfil do fã

## 🖥️ Conteúdo Disponível

- **O aplicativo permite mapear e entender**:

- Dados Cadastrais: Nome completo, CPF, endereço e data de nascimento

- Eventos e Atividades: Participação em campeonatos, compras de produtos oficiais e presença em arenas

- Interesses Esportivos: Jogos favoritos, times acompanhados e frequência de consumo de conteúdo

- Documentação Oficial: Imagens/documentos de RG, CNH ou outros para validação com IA

- Redes Sociais: Perfis vinculados e atividades relacionadas a e-sports

- Links Externos Relevantes: Compartilhamento e verificação de perfis em sites especializados

## 🛠️ Tecnologias Utilizadas

- **Frontend**:

Android com Java

XML para construção de layouts

- **Backend / Validações**:

Firebase Authentication

Firebase Firestore

Firebase Storage (precisa ser premium)

- **Outras Tecnologias**:

ML Kit (para leitura e verificação de documentos)


## ⚙️ Instalação e Configuração

## Pré-requisitos

Android Studio instalado

Conta no Firebase com Firestore, Storage e Authentication ativados

## Passos para Instalação

1. **Clone o repositório**

```bash
git clone https://github.com/flpcvlh/FURIAFanProfile.git
cd FURIAFanProfile
```

2. **Abra o projeto no Android Studio**

- File > Open > Selecione a pasta do projeto

3. **Configure o Firebase**

- Baixe o google-services.json do seu projeto no Firebase e coloque em app/

- Configure Firestore, Authentication (e-mail/senha e redes sociais) e Storage

4. **Configure o ML Kit**

- No build.gradle (Module: app), certifique-se de incluir as dependências do ML Kit:

```groovy
implementation 'com.google.mlkit:text-recognition:16.0.0'
implementation 'com.google.mlkit:text-recognition:16.0.0'
```

- O ML Kit será usado diretamente no Android Studio, sem necessidade de configuração externa

- Garanta que as permissões de câmera e leitura de arquivos estão definidas no AndroidManifest.xml

5. **Execute o projeto**

- Conecte um dispositivo físico ou inicie um emulador Android

- Clique em Run no Android Studio ou use o atalho Shift + F10



## 🧠 Fluxo do Aplicativo

- Tela de Boas-vindas

- Cadastro de Usuário e Coleta de Dados

- Envio de Documentos e Validação

- Integração com Redes Sociais e Links

- Exibição do Perfil do Fã com Insights Personalizados

## 📁 Estrutura do Projeto


FURIAFanProfile/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/furia/fanprofile/
│   │   │   ├── res/
│   │   │   │   ├── layout/            # Layouts XML
│   │   │   │   ├── drawable/          # Ícones e imagens
│   │   │   │   ├── values/            # Strings e temas
│   │   │   └── AndroidManifest.xml
│   └── build.gradle
├── google-services.json
└── README.md


## 🔧 Personalização

- Campos do Formulário: Adicione ou remova informações conforme o perfil desejado

- Estilo do App: Altere o tema FURIA em themes.xml e colors.xml

- Funcionalidades Extras: É possível integrar gamificação, NFTs, cupons e muito mais

## 🌐 Implantação

Gerar APK

```bash

Build > Build Bundle(s) / APK(s) > Build APK(s)
Distribuir via Google Play (beta) ou sites como Itch.io
```

## 📱 Responsividade

**O aplicativo é compatível com**:

- Smartphones Android a partir da versão 8.0 (Oreo)

- Telas pequenas, médias e grandes

## 👥 Contribuição

**Contribuições são bem-vindas! Para colaborar**:

Fork este repositório

- Crie uma branch (git checkout -b nova-feature)

- Commit suas mudanças (git commit -m 'Adiciona nova funcionalidade')

- Push para sua branch (git push origin nova-feature)

- Abra um Pull Request com uma breve explicação

## 📄 Licença
Este projeto está licenciado sob a Licença MIT - veja o arquivo LICENSE para mais detalhes.

## ⚠️ Aviso Legal
Este é um projeto não oficial e não possui vínculo direto com a FURIA Esports. Todas as referências visuais e de marca são utilizadas apenas com fins educacionais e demonstrativos.

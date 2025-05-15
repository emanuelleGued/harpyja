# Requisito de Sistema: Onboarding de Organizações

## 1. Identificação
**RF-001:** Cadastro inicial de organização via intenção de registro

## 2. Descrição
O sistema deve permitir que organizações iniciem seu cadastro fornecendo e-mail e senha, criando um registro preliminar pendente de confirmação.

## 3. Fluxo Principal
**Input:** Usuário insere e-mail e senha válidos

**Processamento:**

1.  Sistema verifica a unicidade do e-mail.
2.  Criptografa a senha fornecida pelo usuário.
3.  Cria um novo registro de organização no sistema com o status inicial definido como `PENDING_VERIFICATION`.
4.  Nesse momento tambem eh gerado um usuario "ADMIN" e associado a organizacao
5.  Gera um token de confirmação único e com validade temporal de 24 horas.
6.  Associa o token gerado ao registro da organização.

**Output:**

1.  Persiste os dados da organização (e-mail, hash da senha, status `PENDING_VERIFICATION`, token de confirmação e data/hora de expiração do token) no banco de dados.
2.  Dispara um e-mail de confirmação para o endereço fornecido, contendo um link com o token de confirmação.

## 4. Regras de Negócio

| ID   | Regra                                                              |
| :--- | :------------------------------------------------------------------- |
| RN01 | O e-mail fornecido deve ser único em todo o sistema.                 |
| RN02 | A senha deve ter no mínimo 8 caracteres e conter letras e números. |
| RN03 | O token de confirmação gerado para ativação da conta expira em 24 horas após sua criação. |
| RN04 | É permitido no máximo 3 tentativas de registro por hora para o mesmo endereço de e-mail. |

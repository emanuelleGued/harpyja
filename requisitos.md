# Requisitos Funcionais

## 1. Gerenciamento de Usuários

1.1. **Criação de contas de usuário**  
&nbsp;&nbsp;&nbsp;&nbsp;Permitir a criação de contas de usuário, armazenando nome, e-mail, senha (com hash) e status de verificação de e-mail.

1.2. **Autenticação de usuários**  
&nbsp;&nbsp;&nbsp;&nbsp;Permitir a autenticação de usuários via login (comparando senhas e gerando tokens JWT).

1.3. **Atualização de dados do usuário**  
&nbsp;&nbsp;&nbsp;&nbsp;Possibilitar a atualização dos dados do usuário (nome, senha, verificação de e-mail etc.).

1.4. **Exclusão de usuários**  
&nbsp;&nbsp;&nbsp;&nbsp;Permitir a exclusão de usuários (soft delete ou hard delete, conforme definição).

---

## 2. Autenticação e Autorização

2.1. **Geração de tokens JWT seguros**  
&nbsp;&nbsp;&nbsp;&nbsp;Gerar tokens JWT seguros para sessões de usuário, com data de expiração configurável.

2.2. **Validação de tokens**  
&nbsp;&nbsp;&nbsp;&nbsp;Validar tokens para proteger endpoints que requerem acesso autenticado.

2.3. **Controle de permissões**  
&nbsp;&nbsp;&nbsp;&nbsp;- Atribuição de papéis organizacionais (por exemplo, `Owner`, `Admin`, `Member`) via `UserOrganization`.  
&nbsp;&nbsp;&nbsp;&nbsp;- Atribuição de papéis em projetos (por exemplo, `Manager`, `Developer`, `Viewer`) via `UserProject`.

---

## 3. Gerenciamento de Organizações

3.1. **Criação e gerenciamento**  
&nbsp;&nbsp;&nbsp;&nbsp;Criar e gerenciar Organizações, vinculando dados de endereço (`Address`), data de registro, status (ativo, inativo etc.).

3.2. **Associar usuários**  
&nbsp;&nbsp;&nbsp;&nbsp;Associar usuários a cada Organização, com um papel específico (`UserOrganization`).

3.3. **Tamanho do negócio e status**  
&nbsp;&nbsp;&nbsp;&nbsp;Controlar o tamanho do negócio (`businessSize`) e o status da organização (`Status`).

---

## 4. Gerenciamento de Projetos

4.1. **Criação e gerenciamento**  
&nbsp;&nbsp;&nbsp;&nbsp;Criar e gerenciar Projetos, vinculando-os a uma Organização (relacionamento muitos-para-um).

4.2. **Definição de atributos**  
&nbsp;&nbsp;&nbsp;&nbsp;Definir atributos do projeto, como data de expiração (`expiration`), chave (`key`), tipo e nome.

4.3. **Associação de usuários**  
&nbsp;&nbsp;&nbsp;&nbsp;Associar usuários a cada projeto, com um papel específico (`UserProject`).

---

## 5. Gerenciamento de Sessões

5.1. **Criação e gerenciamento**  
&nbsp;&nbsp;&nbsp;&nbsp;Criar e gerenciar Sessões, vinculando-as a um Projeto.

5.2. **Armazenamento de datas**  
&nbsp;&nbsp;&nbsp;&nbsp;Armazenar datas e horas de upload (`upload_time`), dispositivo (`device_time`) e gravação (`recording_time`).

5.3. **Vincular Atividades**  
&nbsp;&nbsp;&nbsp;&nbsp;Vincular Atividades (`Activity`) à Sessão correspondente.

---

## 6. Gerenciamento de Atividades e Gestos

6.1. **Criação de Atividades**  
&nbsp;&nbsp;&nbsp;&nbsp;Criar e gerenciar Atividades, que estão relacionadas a uma Sessão específica.

6.2. **Criação de Gestos**  
&nbsp;&nbsp;&nbsp;&nbsp;Criar e gerenciar Gestos (movimentos ou registros específicos), que estão relacionados a uma Atividade.

6.3. **Registro de dados**  
&nbsp;&nbsp;&nbsp;&nbsp;Registrar dados de tempo (ex.: `TargetTime`, `CreatedAt`) e coordenadas (`coordinates`) para cada Gesto.

---

## 7. Manipulação de Endereços

7.1. **Endereço por Organização**  
&nbsp;&nbsp;&nbsp;&nbsp;Cada Organização pode possuir um endereço (`Address`) com país, cidade, rua e CEP.

7.2. **Campo `OrganizationID`**  
&nbsp;&nbsp;&nbsp;&nbsp;Garantir que o campo `OrganizationID` seja único ou siga a regra de relacionamento definida (caso seja 1-para-1).

---

## 8. CRUD Completo

Para cada entidade principal (User, Organization, Project, Session, Activity, Gestures, Address), deve existir a possibilidade de **criar**, **ler (listar/consultar)**, **atualizar** e **excluir** (se aplicável), seguindo políticas de segurança.

---

# Requisitos Não Funcionais

## 1. Segurança

1.1. **Hash de senhas**  
&nbsp;&nbsp;&nbsp;&nbsp;Utilizar hashing seguro para as senhas de usuário (ex.: BCrypt ou Argon2).

1.2. **JWT para autenticação**  
&nbsp;&nbsp;&nbsp;&nbsp;Utilizar JWT para autenticação, garantindo confidencialidade e validade dos tokens.

1.3. **Permissões adequadas**  
&nbsp;&nbsp;&nbsp;&nbsp;Assegurar que apenas usuários autenticados e/ou com as permissões adequadas possam acessar ou modificar dados de Organizações, Projetos, Sessões etc.

---

## 2. Escalabilidade

2.1. **Microsserviços ou containers**  
&nbsp;&nbsp;&nbsp;&nbsp;Possibilidade de operar em ambiente de microsserviços ou containers, caso a aplicação cresça em número de usuários/organizações.

2.2. **Aumento de carga**  
&nbsp;&nbsp;&nbsp;&nbsp;Banco de dados e camada de aplicação devem suportar aumento de carga, com possibilidade de replicação ou balanceamento.

---

## 3. Confiabilidade e Disponibilidade

3.1. **Persistência e integridade transacional**  
&nbsp;&nbsp;&nbsp;&nbsp;Garantir persistência e integridade transacional em todas as operações de CRUD.

3.2. **Logs e monitoramento**  
&nbsp;&nbsp;&nbsp;&nbsp;Configurar logs e monitoramento para rastrear falhas e comportamentos anormais.

3.3. **Backup e recuperação de desastres**  
&nbsp;&nbsp;&nbsp;&nbsp;Ter mecanismos de backup e/ou recuperação de desastres, conforme níveis de SLA (Service Level Agreement).

---

## 4. Manutenibilidade e Extensibilidade

4.1. **Separação de camadas**  
&nbsp;&nbsp;&nbsp;&nbsp;Separar a lógica de negócio (Services) da camada de acesso a dados (Repositories/DAO) e da camada de apresentação (Controllers).

4.2. **Facilitação de adição de novos campos/entidades**  
&nbsp;&nbsp;&nbsp;&nbsp;Facilitar a adição de novos campos e entidades sem quebra de compatibilidade com a base de dados.

4.3. **Testes unitários e de integração**  
&nbsp;&nbsp;&nbsp;&nbsp;Fornecer testes unitários e/ou de integração para manter a confiabilidade das funcionalidades.

---

## 5. Performance

5.1. **Consultas e relatórios ágeis**  
&nbsp;&nbsp;&nbsp;&nbsp;Consultas e relatórios devem retornar resultados em tempo hábil, considerando índices e otimizações necessárias no banco.

5.2. **Grande volume de dados**  
&nbsp;&nbsp;&nbsp;&nbsp;O sistema deve ser capaz de manipular um grande volume de dados de atividades/gestos sem degradação excessiva do desempenho.

---

## 6. Conformidade e Padrões

6.1. **Boas práticas de desenvolvimento seguro**  
&nbsp;&nbsp;&nbsp;&nbsp;Seguir boas práticas de desenvolvimento seguro (ex.: OWASP Top 10).

6.2. **Documentação da API**  
&nbsp;&nbsp;&nbsp;&nbsp;Documentar a API (por exemplo, com Swagger/OpenAPI) para uso interno e por clientes do SaaS.

---

## 7. Configurações e Deploy

7.1. **Variáveis de ambiente**  
&nbsp;&nbsp;&nbsp;&nbsp;Facilitar a configuração de variáveis de ambiente, como chaves secretas e configurações de banco (`TOKEN_SECRET`, `DB_URL` etc.).

7.2. **Deployment automatizado**  
&nbsp;&nbsp;&nbsp;&nbsp;Possibilitar deployment automatizado em ambientes de produção, homologação e desenvolvimento.

# MiniMundo

Este projeto consiste em uma **aplicação SaaS** voltada para o gerenciamento de usuários, organizações, projetos e registros de atividades. De forma geral, o sistema possibilita que pessoas se cadastrem, façam login e participem de organizações, onde podem criar ou participar de projetos específicos. Cada projeto pode ter **sessões** para capturar interações ou eventos, os quais se desdobram em **atividades** e **gestos** que registram ações e coordenadas de tempo.

**Principais Elementos:**

1. **Usuários**  
   - Podem criar contas com e-mail, senha (armazenada de forma segura) e status de verificação de e-mail.  
   - Após autenticados, recebem tokens JWT que garantem acesso aos recursos do sistema.

2. **Organizações**  
   - Agrupam usuários com diferentes papéis (por exemplo, dono, administrador, membro) e mantêm informações como endereço, data de registro e status (ativo, inativo etc.).  
   - Cada organização pode conter vários **projetos**.

3. **Projetos**  
   - Definem uma unidade de trabalho dentro de uma organização, contendo atributos como chave de projeto, data de expiração e tipo.  
   - Usuários também têm papéis específicos em cada projeto (por exemplo, manager, developer), controlando permissões de acesso.

4. **Sessões**  
   - Representam blocos de tempo ou ocasiões em que atividades são registradas, vinculadas a um projeto.  
   - Armazenam horários de início, upload e gravação.

5. **Atividades e Gestos**  
   - **Atividades** são eventos ou processos dentro de uma sessão (por exemplo, “Teste de Login”).  
   - **Gestos** detalham ações específicas (toques de tela, cliques) com registro de tempo e coordenadas.

6. **Endereço**  
   - Vinculado à organização, define país, cidade, rua e CEP, centralizando informações de localização.

**Fluxo de Uso Simplificado:**
- Um usuário se cadastra, verifica seu e-mail e faz login.  
- Cria ou ingressa em uma organização, podendo adicionar outros usuários com papéis específicos.  
- Dentro da organização, podem ser criados projetos com data de expiração, chave de identificação etc.  
- Cada projeto permite registro de sessões para fins de monitoramento ou execução de tarefas.  
- Durante uma sessão, o usuário registra atividades e gestos, formando um histórico detalhado de ações.

Esse conjunto de entidades e relacionamentos garante uma visão completa do fluxo de **autenticação**, **autorização** e **monitoramento** de atividades, permitindo que o sistema seja utilizado em diferentes cenários, como análise de uso, rastreamento de comportamento ou teste de funcionalidades, servindo como um **SaaS** flexível para gerenciamento de usuários e projetos.

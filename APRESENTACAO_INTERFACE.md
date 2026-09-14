# Interface do Sistema - JALA Bank
## Módulo de Cartão de Crédito

---

## 🎯 Tecnologias Utilizadas

### Frontend
- **JavaFX 22** - Framework para interface gráfica
- **FXML** - Linguagem declarativa para layouts
- **CSS Customizado** - Estilização moderna
- **FontAwesome** - Biblioteca de ícones

### Backend
- **Java 17** - Linguagem principal
- **Hibernate/JPA** - ORM para persistência
- **MySQL** - Banco de dados relacional
- **Spring Boot** - Framework de segurança e utilitários

---

## 🏗️ Arquitetura das Telas

### Padrão MVC (Model-View-Controller)

```
📁 Estrutura
├── FXML (View)          → Define a estrutura visual
├── Controller (Java)    → Lógica de negócio e eventos
└── CSS (Styles)         → Estilização e aparência
```

### Telas Implementadas
- **Login** - Autenticação de usuários
- **Home** - Dashboard principal
- **Solicitação de Cartão** - Requisição de novo cartão
- **Faturas** - Visualização de faturas mensais
- **Segurança** - Bloqueio/desbloqueio de cartão
- **Configurações** - Atualização de limite e vencimento
- **Alteração de Senha** - Gestão de senha do cartão

---

## 🎨 Sistema de Design

### Paleta de Cores
```css
Primária:  #1565C0 (Azul Corporativo)
Escura:    #0D47A1 (Azul Escuro)
Clara:     #E3F2FD (Azul Claro)
Texto:     #212121 (Cinza Escuro)
Fundo:     #F5F5F5 (Cinza Claro)
```

### Tipografia
- **Fonte Principal**: Segoe UI, San Francisco, Roboto
- **Tamanhos**: 10px (tiny) → 24px (title)
- **Pesos**: Regular, Semibold (600), Bold (700)

---

## 💅 Estilização CSS

### Cards Modernos
```css
.card {
    background-color: white;
    border-radius: 12px;
    padding: 20px;
    box-shadow: 0 2px 8px rgba(0,0,0,0.08);
}
```

### Cartão de Crédito com Gradiente
```css
.credit-card {
    background: linear-gradient(
        #0D47A1 → #1565C0 → #1976D2
    );
    border-radius: 16px;
    box-shadow: 0 6px 16px rgba(21,101,192,0.4);
}
```

### Botões Interativos
- **Primary**: Azul com hover escuro
- **Secondary**: Outline com hover suave
- **Icon**: Circular com efeitos hover

---

## 🔧 Como Foi Feito

### 1. Estrutura FXML
- Layout declarativo em XML
- Binding com Controllers Java
- Componentes JavaFX nativos

### 2. Estilização CSS
- Arquivo global `application.css`
- Classes reutilizáveis
- Design system consistente
- Efeitos hover e transitions

### 3. Controllers JavaFX
- Gerenciam eventos da interface
- Comunicam com services
- Atualizam dados em tempo real
- Pattern MVC completo

---

## ✨ Destaques da Interface

### Design Moderno
- Cards com sombras suaves
- Bordas arredondadas (8-16px)
- Gradientes nos cartões
- Ícones FontAwesome

### Experiência do Usuário
- Feedback visual em botões
- Validações em tempo real
- Mensagens de erro/sucesso
- Navegação intuitiva

### Responsividade
- Layouts flexíveis (HBox, VBox)
- Componentes adaptáveis
- Espaçamentos consistentes

---

## 📊 Exemplo de Código

### FXML + CSS
```xml
<Button styleClass="button-primary"
        text="Confirmar"
        onAction="#handleSubmit"/>
```

```css
.button-primary {
    -fx-background-color: #1565C0;
    -fx-text-fill: white;
    -fx-background-radius: 8;
    -fx-padding: 10 20;
}

.button-primary:hover {
    -fx-background-color: #0D47A1;
}
```

---

## 🎯 Conclusão

### Pontos Fortes
✅ Interface moderna e profissional
✅ Código organizado e reutilizável
✅ Padrão MVC bem definido
✅ Estilização consistente
✅ Boa experiência do usuário

### Tecnologias Consolidadas
- JavaFX para desktop moderno
- CSS para design profissional
- MVC para código limpo

---

## 💡 Perguntas?

**Obrigado!**

JALA Bank - Sistema de Cartão de Crédito
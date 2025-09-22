# ColorPalette

Uma **biblioteca Android** simples que exibe uma tela de seleção de cores com botões **Confirmar** e **Cancelar**.  
Permite que qualquer app Android mostre uma paleta de cores de forma rápida e reutilizável.

---

## Funcionalidades

- Área de visualização da cor selecionada.
- Botão **Confirmar** retorna a cor selecionada.
- Botão **Cancelar** fecha a tela sem ação.

---

## Instalação

Você pode **clonar este repositório** e importar o módulo `colorpalette` no seu projeto Android Studio.

---

## Funções

Exemplo de estrutura sugerida:

```java
ColorPalette colorPalette = new ColorPalette(this);

colorPalette.show(new OnColorSelectedListener() {
    @Override
    public void onColorConfirmed(int color) {
        // Aqui você recebe a cor selecionada
    }

    @Override
    public void onCanceled() {
        // Aqui trata o cancelamento
    }
});
```

Você também pode optar por esconder a paleta de cores:
```java
colorPalette.hide();
```

package org.jala.university.domain.enums;

public enum PaymentMethod {
    PIX(
        "PIX",
        "Pagamento instantâneo",
        """
        Instruções para pagamento via PIX:

        1. Abra o aplicativo do seu banco
        2. Selecione a opção PIX
        3. Escolha "Pagar com QR Code" ou "Pix Copia e Cola"
        4. Escaneie o código QR ou cole o código fornecido
        5. Confirme os dados e o valor
        6. Digite sua senha e confirme o pagamento

        O pagamento será processado instantaneamente.
        """
    ),

    BOLETO(
        "Boleto Bancário",
        "Boleto com vencimento",
        """
        Instruções para pagamento via Boleto Bancário:

        1. Acesse o boleto através do link ou código de barras fornecido
        2. Você pode pagar através de:
           - Internet banking
           - Aplicativo do banco
           - Caixa eletrônico
           - Lotéricas
           - Agências bancárias
        3. Utilize o código de barras para efetuar o pagamento
        4. Guarde o comprovante de pagamento

        Atenção: O pagamento pode levar até 3 dias úteis para ser confirmado.
        """
    ),

    DEBIT_CARD(
        "Cartão de Débito",
        "Débito em conta",
        """
        Instruções para pagamento via Cartão de Débito:

        1. Selecione seu cartão de débito cadastrado
        2. Verifique os dados do pagamento
        3. Confirme o valor a ser debitado
        4. Digite sua senha do cartão
        5. Confirme a transação

        O valor será debitado imediatamente da sua conta corrente.
        Certifique-se de ter saldo disponível.
        """
    ),

    CREDIT_CARD(
        "Cartão de Crédito",
        "Crédito parcelado",
        """
        Instruções para pagamento via Cartão de Crédito:

        1. Selecione outro cartão de crédito para pagamento
        2. Escolha o número de parcelas desejado
        3. Verifique o valor total e das parcelas
        4. Confirme os dados do cartão
        5. Digite o código de segurança (CVV)
        6. Confirme o pagamento

        O pagamento poderá ser parcelado conforme disponibilidade.
        """
    );

    private final String displayName;
    private final String description;
    private final String instructions;

    PaymentMethod(String displayName, String description, String instructions) {
        this.displayName = displayName;
        this.description = description;
        this.instructions = instructions;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public String getInstructions() {
        return instructions;
    }
}
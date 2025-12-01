package org.jala.university.application.service;

import org.jala.university.application.dto.CreditCardPasswordUpdateRequestDto;
import org.jala.university.application.dto.CreditCardPasswordUpdateResponseDto;
import org.jala.university.domain.entity.CreditCard;
import org.jala.university.domain.entity.User;
import org.jala.university.domain.enums.CreditCardStatus;
import org.jala.university.domain.repository.CreditCardRepository;
import org.jala.university.domain.repository.InvoiceRepository;
import org.jala.university.application.mapper.CreditCardMapper;
import org.jala.university.infrastructure.util.PasswordHashUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreditCardService - CRE-010 Atualização de Senha")
class CreditCardServiceImplTest {

    @Mock
    private CreditCardRepository creditCardRepository;

    @Mock
    private CreditCardMapper creditCardMapper;

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private CreditCardServiceImpl creditCardService;

    @Test
    @DisplayName("Deve retornar erro quando request é nulo ou com dados inválidos")
    void deveRetornarErroQuandoRequestInvalido() {
        CreditCardPasswordUpdateResponseDto response = creditCardService.updateCardPassword(null);

        assertFalse(response.getSuccess());
        assertEquals("Dados inválidos para atualizar senha do cartão.", response.getMessage());
    }

    @Test
    @DisplayName("Deve retornar erro quando senhas não conferem ou não possuem 4 dígitos numéricos")
    void deveValidarNovaSenha() {
        UUID userId = UUID.randomUUID();
        CreditCardPasswordUpdateRequestDto request = CreditCardPasswordUpdateRequestDto.builder()
                .userId(userId)
                .userLoginPassword("login123")
                .cardNumber("1234567890123456")
                .newPassword("12a4")
                .confirmNewPassword("12a4")
                .build();

        CreditCardPasswordUpdateResponseDto response = creditCardService.updateCardPassword(request);

        assertFalse(response.getSuccess());
        assertEquals("A nova senha deve conter exatamente 4 dígitos numéricos.", response.getMessage());
    }

    @Test
    @DisplayName("Deve retornar erro quando cartão não é encontrado")
    void deveRetornarErroQuandoCartaoNaoEncontrado() {
        UUID userId = UUID.randomUUID();
        CreditCardPasswordUpdateRequestDto request = CreditCardPasswordUpdateRequestDto.builder()
                .userId(userId)
                .userLoginPassword("login123")
                .cardNumber("1234567890123456")
                .newPassword("1234")
                .confirmNewPassword("1234")
                .build();

        when(creditCardRepository.findByCardNumber("1234567890123456")).thenReturn(null);

        CreditCardPasswordUpdateResponseDto response = creditCardService.updateCardPassword(request);

        assertFalse(response.getSuccess());
        assertEquals("Cartão não encontrado.", response.getMessage());
    }

    @Test
    @DisplayName("Deve retornar erro quando usuário não é dono do cartão")
    void deveRetornarErroQuandoUsuarioNaoEhDonoDoCartao() {
        UUID userId = UUID.randomUUID();
        CreditCardPasswordUpdateRequestDto request = CreditCardPasswordUpdateRequestDto.builder()
                .userId(userId)
                .userLoginPassword("login123")
                .cardNumber("1234567890123456")
                .newPassword("1234")
                .confirmNewPassword("1234")
                .build();

        User otherUser = new User();
        otherUser.setId(UUID.randomUUID());

        CreditCard creditCard = CreditCard.builder()
                .cardNumber("1234567890123456")
                .maskCardNumber("**** **** **** 3456")
                .status(CreditCardStatus.ACTIVE)
                .user(otherUser)
                .passwordHash("oldHash")
                .build();

        when(creditCardRepository.findByCardNumber("1234567890123456")).thenReturn(creditCard);

        CreditCardPasswordUpdateResponseDto response = creditCardService.updateCardPassword(request);

        assertFalse(response.getSuccess());
        assertEquals("Usuário não autorizado para este cartão.", response.getMessage());
    }

    @Test
    @DisplayName("Deve retornar erro quando senha de login é inválida")
    void deveRetornarErroQuandoSenhaLoginInvalida() {
        UUID userId = UUID.randomUUID();
        CreditCardPasswordUpdateRequestDto request = CreditCardPasswordUpdateRequestDto.builder()
                .userId(userId)
                .userLoginPassword("senhaErrada")
                .cardNumber("1234567890123456")
                .newPassword("1234")
                .confirmNewPassword("1234")
                .build();

        User user = new User();
        user.setId(userId);
        user.setPasswordHash(PasswordHashUtil.hashPassword("senhaCorreta"));

        CreditCard creditCard = CreditCard.builder()
                .cardNumber("1234567890123456")
                .maskCardNumber("**** **** **** 3456")
                .status(CreditCardStatus.ACTIVE)
                .user(user)
                .passwordHash("oldCardHash")
                .build();

        when(creditCardRepository.findByCardNumber("1234567890123456")).thenReturn(creditCard);

        CreditCardPasswordUpdateResponseDto response = creditCardService.updateCardPassword(request);

        assertFalse(response.getSuccess());
        assertEquals("Senha de login inválida.", response.getMessage());
    }

    @Test
    @DisplayName("Deve retornar erro quando nova senha é igual à atual")
    void deveRetornarErroQuandoNovaSenhaIgualAtual() {
        UUID userId = UUID.randomUUID();
        CreditCardPasswordUpdateRequestDto request = CreditCardPasswordUpdateRequestDto.builder()
                .userId(userId)
                .userLoginPassword("login123")
                .cardNumber("1234567890123456")
                .newPassword("1234")
                .confirmNewPassword("1234")
                .build();

        User user = new User();
        user.setId(userId);
        user.setPasswordHash(PasswordHashUtil.hashPassword("login123"));

        CreditCard creditCard = CreditCard.builder()
                .cardNumber("1234567890123456")
                .maskCardNumber("**** **** **** 3456")
                .status(CreditCardStatus.ACTIVE)
                .user(user)
                .passwordHash("1234")
                .build();

        when(creditCardRepository.findByCardNumber("1234567890123456")).thenReturn(creditCard);

        CreditCardPasswordUpdateResponseDto response = creditCardService.updateCardPassword(request);

        assertFalse(response.getSuccess());
        assertEquals("A nova senha não pode ser igual à senha atual.", response.getMessage());
    }

    @Test
    @DisplayName("Deve atualizar senha do cartão com sucesso")
    void deveAtualizarSenhaComSucesso() {
        UUID userId = UUID.randomUUID();
        CreditCardPasswordUpdateRequestDto request = CreditCardPasswordUpdateRequestDto.builder()
                .userId(userId)
                .userLoginPassword("login123")
                .cardNumber("1234567890123456")
                .newPassword("5678")
                .confirmNewPassword("5678")
                .build();

        User user = new User();
        user.setId(userId);
        user.setPasswordHash(PasswordHashUtil.hashPassword("login123"));

        CreditCard creditCard = CreditCard.builder()
                .cardNumber("1234567890123456")
                .maskCardNumber("**** **** **** 3456")
                .status(CreditCardStatus.ACTIVE)
                .user(user)
                .passwordHash("1234")
                .build();

        when(creditCardRepository.findByCardNumber("1234567890123456")).thenReturn(creditCard);

        CreditCardPasswordUpdateResponseDto response = creditCardService.updateCardPassword(request);

        assertTrue(response.getSuccess());
        assertEquals("Senha do cartão atualizada com sucesso!", response.getMessage());
        assertNotNull(creditCard.getPasswordHash());
        assertNotEquals("1234", creditCard.getPasswordHash());
        assertEquals("**** **** **** 3456", response.getMaskCardNumber());
        assertEquals(CreditCardStatus.ACTIVE, response.getStatus());
    }
}
